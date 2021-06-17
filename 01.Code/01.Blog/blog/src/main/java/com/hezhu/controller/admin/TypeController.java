package com.hezhu.controller.admin;

import com.hezhu.po.Type;
import com.hezhu.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.Id;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    //分页
    @GetMapping("/types")
    public String types(@PageableDefault(size = 5, sort = {"id"}, direction = Sort.Direction.DESC)Pageable pageable, Model model) {
        //查询分页信息
        model.addAttribute("page", typeService.listType(pageable));
        return "admin/types";
    }

    //新建
    @GetMapping("/types/input")
    public String input(Model model) {
        //这里对应`types-input.html`中的`th:object="${type}` ->然后才可以取到`th:value="*{id}"`
        model.addAttribute("type", new Type());

        return "admin/types-input";
    }

    //提交-新增标签
    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes) { //@Valid后端检验，输入name不能为空
        //得到标签名字，校验标签是否已经存在，若存在则不添加
        Type typeByName = typeService.getTypeByName(type.getName());
        System.out.println(typeByName);

        if (typeByName != null) {
            result.rejectValue("name", "nameError", "Can not duplicate tag"); //name必须与实体类中name一致
        }
        //保存标签
        Type t = typeService.saveType(type);
        if (t == null) {
            attributes.addFlashAttribute("message", "Error");
        }else {
            attributes.addFlashAttribute("message", "Success");
        }
        return "redirect:/admin/types"; //需要重新调用上面的types函数，来查询分页信息
    }

    //编辑标签
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("type", typeService.getType(id));
        return "admin/types-input";
    }

    //提交-编辑标签
    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult result,@PathVariable Long id, RedirectAttributes attributes) { //@Valid后端检验，输入name不能为空
        //校验标签是否已经存在，若存在则不添加
        Type typeByName = typeService.getTypeByName(type.getName());
        if (typeByName != null) {
            result.rejectValue("name", "nameError", "Can not duplicate tag"); //name必须与实体类中name一致
        }
        //保存标签
        Type t = typeService.updateType(id, type);
        if (t == null) {
            attributes.addFlashAttribute("message", "Update Error");
        }else {
            attributes.addFlashAttribute("message", "Update Success");
        }
        return "redirect:/admin/types"; //需要重新调用上面的types函数，来查询分页信息
    }

    //删除
    @GetMapping("/delete/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "Deleted");
        return "redirect:/admin/types";
    }
}
