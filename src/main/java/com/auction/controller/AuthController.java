package com.auction.controller;

import com.auction.dto.RegisterRequest;
import com.auction.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/login")
    public String loginRedirect() {
        return "redirect:/thymeleaf/login";
    }

    @GetMapping("/register")
    public String registerRedirect() {
        return "redirect:/thymeleaf/register";
    }

    @GetMapping("/thymeleaf/login")
    public String thymeleafLogin(Model model, HttpSession session,
                                 @RequestParam(required = false) String error,
                                 @RequestParam(required = false) String logout,
                                 @RequestParam(required = false) String registered) {
        session.setAttribute("LOGIN_ENGINE", "thymeleaf");
        fillLoginModel(model, "thymeleaf", error, logout, registered);
        return "thymeleaf/login";
    }

    @GetMapping("/mustache/login")
    public String mustacheLogin(Model model, HttpSession session,
                                @RequestParam(required = false) String error,
                                @RequestParam(required = false) String logout,
                                @RequestParam(required = false) String registered) {
        session.setAttribute("LOGIN_ENGINE", "mustache");
        fillLoginModel(model, "mustache", error, logout, registered);
        model.addAttribute("pageTitle", "Вход");
        return "mustache/login";
    }

    @GetMapping("/freemarker/login")
    public String freemarkerLogin(Model model, HttpSession session,
                                  @RequestParam(required = false) String error,
                                  @RequestParam(required = false) String logout,
                                  @RequestParam(required = false) String registered) {
        session.setAttribute("LOGIN_ENGINE", "freemarker");
        fillLoginModel(model, "freemarker", error, logout, registered);
        model.addAttribute("pageTitle", "Вход");
        return "freemarker/login";
    }

    @GetMapping("/thymeleaf/register")
    public String thymeleafRegister(Model model) {
        fillRegisterModel(model, "thymeleaf", new RegisterRequest(), null);
        return "thymeleaf/register";
    }

    @GetMapping("/mustache/register")
    public String mustacheRegister(Model model) {
        fillRegisterModel(model, "mustache", new RegisterRequest(), null);
        model.addAttribute("pageTitle", "Регистрация");
        return "mustache/register";
    }

    @GetMapping("/freemarker/register")
    public String freemarkerRegister(Model model) {
        fillRegisterModel(model, "freemarker", new RegisterRequest(), null);
        model.addAttribute("pageTitle", "Регистрация");
        return "freemarker/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest registerRequest,
                           BindingResult bindingResult,
                           @RequestParam(defaultValue = "thymeleaf") String engine,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return registerView(engine, model, registerRequest, null);
        }
        try {
            userService.register(registerRequest);
            return "redirect:/" + engine + "/login?registered";
        } catch (Exception ex) {
            return registerView(engine, model, registerRequest, ex.getMessage());
        }
    }

    private void fillLoginModel(Model model, String engine, String error, String logout, String registered) {
        model.addAttribute("engine", engine);
        model.addAttribute("errorMessage", error != null ? "Неверный логин или пароль" : null);
        model.addAttribute("logoutMessage", logout != null ? "Вы вышли из системы" : null);
        model.addAttribute("registeredMessage", registered != null ? "Регистрация завершена" : null);
        fillEngineSwitch(model, engine, "login");
    }

    private void fillRegisterModel(Model model, String engine, RegisterRequest request, String errorMessage) {
        model.addAttribute("engine", engine);
        model.addAttribute("registerRequest", request);
        model.addAttribute("username", request.getUsername() == null ? "" : request.getUsername());
        model.addAttribute("email", request.getEmail() == null ? "" : request.getEmail());
        model.addAttribute("errorMessage", errorMessage);
        fillEngineSwitch(model, engine, "register");
    }

    private String registerView(String engine, Model model, RegisterRequest request, String errorMessage) {
        fillRegisterModel(model, engine, request, errorMessage);
        if ("mustache".equals(engine)) {
            model.addAttribute("pageTitle", "Регистрация");
            return "mustache/register";
        }
        if ("freemarker".equals(engine)) {
            model.addAttribute("pageTitle", "Регистрация");
            return "freemarker/register";
        }
        return "thymeleaf/register";
    }

    private void fillEngineSwitch(Model model, String engine, String page) {
        model.addAttribute("currentEngine", engine);
        model.addAttribute("thymeleafUrl", "/thymeleaf/" + page);
        model.addAttribute("mustacheUrl", "/mustache/" + page);
        model.addAttribute("freemarkerUrl", "/freemarker/" + page);
    }
}
