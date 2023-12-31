package com.suakang.auth.controller;

import com.suakang.auth.dto.TokenRefreshDto;
import com.suakang.auth.entity.AuthenticateMember;
import com.suakang.auth.service.AuthService;
import com.suakang.auth.util.jwt.Jwt;
import com.suakang.memberserver.member.dto.MemberRequestDto;
import com.suakang.memberserver.member.util.constant.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseBody
    public void saveMember(@RequestBody MemberRequestDto memberRequestDto) {
        authService.signUp(memberRequestDto);
    }

    @GetMapping("/verify")
    public String verifyMemberRole(HttpServletRequest request) {

        AuthenticateMember member = (AuthenticateMember) request.getHeaders("AUTHENTICATE_MEMBER");
        log.info("AuthenticateMember={}", member.toString());
        if(member.getRole().equals(Role.USER)) return "member/view";
        return "member/list";

    }

    @PostMapping("/refresh/token")
    public ResponseEntity<Jwt> tokenRefresh(@RequestBody TokenRefreshDto tokenRefreshDto) {
        Jwt jwt = authService.refreshToken(tokenRefreshDto.getRefreshToken());
        if (jwt == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
        return ResponseEntity.ok(jwt);
    }
}
