package com.hezhu.controller;

import com.hezhu.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArchiveShowController {
    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String archives(Model model) {
        //根据年份返回blog, Map<String, List<Blog>>形式
        model.addAttribute("archiveMap", blogService.archiveBlog());
        //返回总博客数量
        model.addAttribute("blogCount", blogService.countBlog());
        return "archives";
    }
}
