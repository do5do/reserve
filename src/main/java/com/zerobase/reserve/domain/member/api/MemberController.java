package com.zerobase.reserve.domain.member.api;

import com.zerobase.reserve.domain.member.dto.Signin;
import com.zerobase.reserve.domain.member.service.MemberService;
import com.zerobase.reserve.domain.member.dto.MemberDto;
import com.zerobase.reserve.domain.member.dto.Signup;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<MemberDto> signup(@RequestBody @Valid Signup request) {
        return ResponseEntity.ok(memberService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<MemberDto> signin(@RequestBody @Valid Signin request) {
        return ResponseEntity.ok(memberService.signin(request));
    }
}
