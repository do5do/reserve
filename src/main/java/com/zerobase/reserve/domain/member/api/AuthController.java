package com.zerobase.reserve.domain.member.api;

import com.zerobase.reserve.domain.member.dto.Signin;
import com.zerobase.reserve.domain.member.service.AuthService;
import com.zerobase.reserve.domain.member.dto.MemberDto;
import com.zerobase.reserve.domain.member.dto.Signup;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/signup")
    public ResponseEntity<MemberDto> signup(@RequestBody @Valid Signup request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<MemberDto> signin(@RequestBody @Valid Signin request) {
        return ResponseEntity.ok(authService.signin(request));
    }
}
