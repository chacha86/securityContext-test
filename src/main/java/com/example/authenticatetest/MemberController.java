package com.example.authenticatetest;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @PostConstruct
    private void postConstruct() {
        String username = "차태진";
        Member member = memberRepository.findByUsername(username).orElse(null);
        if (member == null) {
            saveMember();
        }
    }

    private void saveMember() {
        String username = "차태진";
        String password = "1234";
        String email = "cha@cha.com";
        Member member = new Member();
        member.setUsername(username);
        member.setPassword(passwordEncoder.encode(password));
        member.setEmail(email);

        memberRepository.save(member);
    }

    @GetMapping("/login")
    public String login() {
        return "test";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    private final SecurityContextRepository securityContextRepository;

    @GetMapping("/test")
    public String test(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("123");
        User user = new User("cha", "1234", Collections.emptyList());
        Authentication authentication = new UsernamePasswordAuthenticationToken("cha", "1234", Collections.emptyList());
//        authentication = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();
//        SecurityContext context = strategy.createEmptyContext();
//        context.setAuthentication(authentication);
//        strategy.setContext(context);
        securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
        return "redirect:/test2";
    }

    @GetMapping("/test2")
    @ResponseBody
    public String test2(Authentication authentication, HttpServletRequest request) {
//        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(authentication1);
        System.out.println(authentication);
        return "test2";
    }
}
