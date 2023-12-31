package com.suakang.memberserver.member.service;

import com.suakang.memberserver.member.dto.MemberRequestDto;
import com.suakang.memberserver.member.dto.MemberResponseDto;
import com.suakang.memberserver.member.entity.Member;
import com.suakang.memberserver.member.repository.MemberRepository;
import com.suakang.memberserver.member.util.converter.MemberToResponseConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberToResponseConverter converter;
    private final PasswordEncoder passwordEncoder;

    public MemberResponseDto findMemberByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId);
        if (member != null) {
            return converter.convert(member);
        }
        return null;  // TODO 예외처리
    }

    public void updateMember(MemberRequestDto memberRequestDto) {
        Member member = memberRepository.findByLoginId(memberRequestDto.getLoginId());
        if(member != null) {
            memberRequestDto.encodePassword(passwordEncoder);

            member.updateMember(memberRequestDto.getPassword(), memberRequestDto.getEmail(), memberRequestDto.getGender(), memberRequestDto.getBirth());
            memberRepository.save(member);
        }
    }

    public void deleteMemberById(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        member.ifPresent(memberRepository::delete);
    }

    public int countMemberByLoginId(String loginId) {
        return memberRepository.countByLoginId(loginId);
    }


}
