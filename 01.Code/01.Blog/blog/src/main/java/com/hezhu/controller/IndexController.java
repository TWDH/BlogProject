package com.hezhu.controller;

import com.hezhu.NotFoundException;
import com.hezhu.service.BlogService;
import com.hezhu.service.CommentService;
import com.hezhu.service.TagService;
import com.hezhu.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    //查询主页博客
    @GetMapping("/")
    public String index(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC)Pageable pageable,
                        Model model) {
        //查询所有博客并分页
        model.addAttribute("page", blogService.listBlog(pageable));
        //查询并设定右侧type显示的数目
        model.addAttribute("types", typeService.listTypeTop(6));
        //查询并设定右侧tag显示的数目
        model.addAttribute("tags", tagService.listTagTop(10));
        //查询并设定右侧recommend显示的数目
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));

        return "index";
    }

    //根据id进入博客主页
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        //根据id在数据库中查询到blog的所有属性
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "blog";
    }

    //全局搜索
    @PostMapping("/search")
    public String Search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC)Pageable pageable,
                         @RequestParam String query, Model model){
        model.addAttribute("page", blogService.listBlog(query, pageable));
        model.addAttribute("query", query);
        return "search";
    }

    //下方页面动态获取最新文章
    @GetMapping("/footer/newblog")
    public String newblogs(Model model) {
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }

}
