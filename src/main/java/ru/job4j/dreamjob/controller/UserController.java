package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

@ThreadSafe
@Controller
@RequestMapping("/users")/*  URI /users/** */
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getCreationPage() {
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        try {
            userService.save(user);
            return "redirect:/users/register";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/register/{email}/{password}")
    public String getUserByEmaillPass(Model model, @PathVariable String email, @PathVariable String password) {
        var userOptional = userService.findByEmailAndPassword(email, password);
        if (userOptional.isEmpty()) {
            model.addAttribute("message", "Пользователь с указанными email и password не найден");
            return "errors/404";
        }
        model.addAttribute("user", userOptional.get());
        return "users/register";
    }
}
