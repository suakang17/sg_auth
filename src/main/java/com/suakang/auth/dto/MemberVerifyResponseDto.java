package com.suakang.auth.dto;

import com.suakang.memberserver.member.util.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
@ToString
public class MemberVerifyResponseDto {
    private boolean isValid;
    private Role role;
}