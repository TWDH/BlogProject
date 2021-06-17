package com.hezhu.controller.admin;

import com.hezhu.po.User;
import com.hezhu.service.BlogService;
import com.hezhu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    //登录页面
    @GetMapping
    public String loginPage() {

        return "admin/login";
    }

    //登录页面-提交
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes) {

        //在数据库中查询
        User user = userService.checkUser(username, password);
        //判断用户是否存在
        if (user != null) {
            user.setPassword(null);
            session.setAttribute("user", user);
            return "admin/index";
        }else {
            attributes.addFlashAttribute("message", "username / password error");
            return "redirect:/admin"; //返回到登录页面
        }
    }

    //登出
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }

    //下方页面动态获取最新文章
    @GetMapping("/admin_footer/newblog")
    public String newblogs(Model model) {
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        System.out.println("=========================================");
        return "admin/_fragments :: admin-newblogList";
    }
}
