package com.kleben.contas.controller;

import com.kleben.contas.security.JwtTokenProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autenticacao")
public class AutenticacaoController {

    private final JwtTokenProvider jwtTokenProvider;

    public AutenticacaoController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/gerar-token")
    public String getToken() {
        return jwtTokenProvider.generateToken("user");
    }
}