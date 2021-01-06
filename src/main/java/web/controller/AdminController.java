package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.models.User;
import web.services.RoleService;
import web.services.UserService;

import javax.validation.Valid;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {

    final UserService userService;
    final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.listUsers());
        return "admin/index";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "admin/new";
    }

    @PostMapping
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "admin/new";
        }
        /*TODO добавить checkbox если нужно сразу сделать админом при создании*/
        //по умолчанию создается с ролью USER, чекбокса для выбора не сделал
        user.getRoles().add(roleService.getDefaultRole());
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.getUserById(id));
        return "admin/edit";
    }

    @PostMapping("/{id}")
    public String update(@Valid @ModelAttribute("user") User user,
                         BindingResult bindingResult,
                         @RequestParam("roles") Set<String> roles) {

        /*TODO разобраться с валидатором*/
        /*if (bindingResult.hasErrors()) {
            return "admin/edit";
        }*/
        user.setRoles(roleService.getRoles(roles));
        userService.update(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}