package com.zerobase.reserve.domain.member.api;

import com.zerobase.reserve.domain.member.dto.Signin;
import com.zerobase.reserve.domain.member.service.AuthService;
import com.zerobase.reserve.domain.member.dto.model.MemberDto;
import com.zerobase.reserve.domain.member.dto.Signup;
import com.zerobase.reserve.global.security.TokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;
    private final TokenProvider tokenProvider;

    /**
     * 회원 가입
     */
    @PostMapping("/signup")
    public ResponseEntity<MemberDto> signup(@RequestBody @Valid Signup request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    /**
     * 로그인
     */
    @PostMapping("/signin")
    public ResponseEntity<MemberDto> signin(@RequestBody @Valid Signin request) {
        MemberDto memberDto = authService.signin(request);
        String token = tokenProvider.generateToken(memberDto);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, token);

        return ResponseEntity.ok()
                .headers(headers)
                .body(memberDto);
    }
}
