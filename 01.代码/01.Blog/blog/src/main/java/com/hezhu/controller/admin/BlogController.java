package com.hezhu.controller.admin;


import com.hezhu.po.Blog;
import com.hezhu.po.Type;
import com.hezhu.po.User;
import com.hezhu.service.BlogService;
import com.hezhu.service.TagService;
import com.hezhu.service.TypeService;
import com.hezhu.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    //分页查询
    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) {
        model.addAttribute("types", typeService.listType());
        //查询分页
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return LIST;
    }

    //分页
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {

        //查询分页
        Page<Blog> blogs = blogService.listBlog(pageable, blog);


        model.addAttribute("page", blogs);
        return "admin/blogs :: blogList"; //blogs.html页面下的blogList片段
    }

    //新建
    @GetMapping("/blogs/input")
    public String input(Model model) {
        //初始化,前端就可以拿到数据并赋值
        //types包括数据库中的所有type，比如java, python, javascript, ...
        setTypeAndTag(model);
        Blog newblog = new Blog();
        model.addAttribute("blog", newblog);

        return INPUT;
    }

    //发布博客
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {

        //需要知道是谁发了博客，之前在LoginController中已经向session中注入过user了
        blog.setUser((User) session.getAttribute("user"));
        //通过当前类型的id找到当前博客的分类
        blog.setType(typeService.getType(blog.getType().getId()));
        //Tag：这时候只有tagId（编号），而没有tag属性
        blog.setTags(tagService.listTag(blog.getTagIds()));

        //Dao保存博客
        Blog b;
        if(blog.getId() == null){
            //新建
            b = blogService.saveBlog(blog);
        }else {
            //编辑
            b = blogService.updateBlog(blog.getId(), blog);
        }


        if (b == null) {
            attributes.addFlashAttribute("message", "Failed");
        } else {
            attributes.addFlashAttribute("message", "Success");
        }
        return REDIRECT_LIST;
    }

    //修改页面
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        //通过id得到全部blog的信息
        Blog blog = blogService.getBlog(id);
        //把tagId处理成字符串
        blog.init();
        model.addAttribute("blog",blog);

        return INPUT;
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    //删除
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","delete successfully");
        return REDIRECT_LIST;
    }

}






















