package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import web.models.User;
import web.services.RoleService;
import web.services.UserService;

@Controller
@RequestMapping("/registration")
public class RegController {

    final
    UserService userService;
    final
    RoleService roleService;

    public RegController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ModelAndView showRegistrationForm(User user) {
        ModelAndView modelAndView = new ModelAndView("registration");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("/newUser")
    public String createUser(User user) {
        user.getRoles().add(roleService.getDefaultRole());
        userService.save(user);
        return "redirect:/index";
    }
}