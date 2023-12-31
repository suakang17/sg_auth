package com.suakang.memberserver.member.util.converter;

import com.suakang.memberserver.member.dto.MemberResponseDto;
import com.suakang.memberserver.member.entity.Member;

public class MemberToResponseConverter {

    public MemberResponseDto convert(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .name(member.getLoginId())
                .password(member.getPassword())
                .name(member.getName())
                .email(member.getEmail())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
