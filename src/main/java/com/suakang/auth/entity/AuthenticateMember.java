package com.suakang.auth.entity;

import com.suakang.memberserver.member.util.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Getter
@AllArgsConstructor
@ToString
public class AuthenticateMember {
    private String loginId;
    private Role role;
}
