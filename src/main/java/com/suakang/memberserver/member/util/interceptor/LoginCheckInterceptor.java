package com.suakang.memberserver.member.util.interceptor;

import com.suakang.memberserver.member.dto.MemberResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        MemberResponseDto member = (MemberResponseDto) session.getAttribute("loginMember");

        if (member == null) {
            response.sendRedirect("/login");
            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
