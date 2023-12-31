package com.suakang.memberserver.member.controller;

import com.suakang.auth.service.AuthService;
import com.suakang.memberserver.member.dto.MemberRequestDto;
import com.suakang.memberserver.member.dto.MemberResponseDto;
import com.suakang.memberserver.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    @GetMapping
    public String openLogin() {
        log.info("login page");
        return "member/login";
    }

    @PostMapping("/signup")
    public void signUp(@RequestBody MemberRequestDto memberRequestDto) {
        authService.signUp(memberRequestDto);
    }

    @GetMapping("/{loginId}")
    @ResponseBody
    public void findMemberByLoginId(@PathVariable String loginId) {
        memberService.findMemberByLoginId(loginId);
    }

    @PatchMapping
    @ResponseBody
    public void updateMember(@RequestBody MemberRequestDto memberRequestDto) {
        memberService.updateMember(memberRequestDto);
    }

    @DeleteMapping
    @ResponseBody
    public void deleteMemberById(Long id) {
        memberService.deleteMemberById(id);
    }

    @GetMapping("/duplicate-check")
    @ResponseBody
    public int checkMemberDuplication(@RequestParam String loginId) {
        return memberService.countMemberByLoginId(loginId);
    }

    @PostMapping("/login")
    @ResponseBody
    public MemberResponseDto login(HttpServletRequest request) {

        log.info("on AuthController login()");

        return null;
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login.do";
    }


}
