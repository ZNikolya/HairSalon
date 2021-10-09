package com.example.hairsalonweb.controller;

import com.example.hairsalonweb.security.CurrentUser;
import com.hairsaloncommon.model.Gender;
import com.hairsaloncommon.model.User;
import com.hairsaloncommon.model.UserType;
import com.hairsaloncommon.service.EmailService;
import com.hairsaloncommon.service.UserService;
import com.hairsaloncommon.webDto.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Value("${file.upload.dir}")
    private String uploadDir;
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @GetMapping("/")
    public String homePage() {
        return "home";
    }


    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("userDto") @Valid UserDto userDto, BindingResult br,
                          @RequestParam("image") MultipartFile file, ModelMap modelMap, Locale locale) throws IOException {
        if (br.hasErrors()) {
            List<User> allUser = userService.findAll();
            modelMap.addAttribute("users", allUser);
            return "home";
        }
        String msg;
//        if(!TextUtil.VALID_EMAIL_ADDRESS_REGEX.matcher(userRequest.getEmail()).find()) {
//            msg = "Email does not valid";
//            return "redirect:/?msg=" + msg;
//        }
        if (!userDto.getPassword(

        ).equals(userDto.getConfirmPassword())) {
            msg = "Password and Confirm Password does not match!";
            return "redirect:/?msg=" + msg;
        }
        Optional<User> byEmail = userService.findByEmail(userDto.getEmail());
        if (byEmail.isPresent()) {
            msg = "User already exists";
            return "redirect:/?msg=" + msg;
        }
        String profilePic = null;
        if (!file.isEmpty()) {
            profilePic = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File image = new File(uploadDir, profilePic);
            file.transferTo(image);
        }
        User user = modelMapper.map(userDto, User.class);
        user.setMainPhoto(profilePic);
        user.setGender(Gender.valueOf(userDto.getGender()));
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setToken(UUID.randomUUID().toString());
        userService.save(user);
        String link = "http://localhost:8080/activate?email=" + userDto.getEmail() + "&token=" + user.getToken();
        msg = "Check email";
        try {
            emailService.setHtmlEmail(userDto.getEmail(), "Welcome", user, link, "email/UserWelcomeMail.html", locale);
        } catch (MessagingException e) {
            e.printStackTrace();
            return "redirect:/";
        }
        return "redirect:/?msg=" + msg;

    }


    @GetMapping("/successLogin")
    public String successLogin(@AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser == null) {
            return "redirect:/";
        }
        User user = currentUser.getUser();
        if (user.getUserType() == UserType.ADMIN) {
            return "redirect:/adminPage";
        }
        if (user.getUserType() == UserType.MANAGER) {
            return "redirect:/managerPage";
        }
        if (user.getUserType() == UserType.USER) {
            return "redirect:/userPage";
        }
        return "home";
    }

    @GetMapping("/activate")
    public String activateUser(@RequestParam("email") String email, @RequestParam("token") String token) {
        String msg;
        Optional<User> byEmail = userService.findByEmail(email);
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            if (user.getToken().equals(token)) {
                user.setActive(true);
                user.setToken("");
                userService.save(user);
                msg = "User was activate";
                return "redirect:/?msg=" + msg;
            }
        }
        msg = "Something went wrong. Please try again";
        return "redirect:/?msg=" + msg;
    }
}
