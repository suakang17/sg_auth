package com.suakang.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suakang.auth.dto.MemberLoginDto;
import com.suakang.auth.dto.MemberVerifyResponseDto;
import com.suakang.auth.entity.AuthenticateMember;
import com.suakang.auth.repository.AuthRepository;
import com.suakang.auth.util.filter.VerifyMemberFilter;
import com.suakang.auth.util.jwt.Jwt;
import com.suakang.auth.util.jwt.JwtProvider;
import com.suakang.memberserver.member.dto.MemberRequestDto;
import com.suakang.memberserver.member.dto.MemberResponseDto;
import com.suakang.memberserver.member.entity.Member;
import com.suakang.memberserver.member.repository.MemberRepository;
import com.suakang.memberserver.member.util.constant.Role;
import com.suakang.memberserver.member.util.converter.MemberToResponseConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final MemberToResponseConverter converter;

    @Transactional
    public void signUp(MemberRequestDto memberRequestDto) {

        log.info("memberRequestDto= {}", memberRequestDto.toString());
        memberRequestDto.encodePassword(passwordEncoder);
        Member newMember = Member.builder()
                .loginId((memberRequestDto.getLoginId()))
                .password(memberRequestDto.getPassword())
                .name(memberRequestDto.getName())
                .email(memberRequestDto.getEmail())
                .gender(memberRequestDto.getGender())
                .birth(memberRequestDto.getBirth())
                .build();

        newMember.setRole(Role.USER);

        log.info("password= {}", memberRequestDto.getPassword());
        log.info("genderType={}", memberRequestDto.getGender().getClass());
        authRepository.save(newMember);
        memberRepository.save(newMember);
    }

    // login
    public MemberVerifyResponseDto verifyMember(MemberLoginDto memberLoginDto) {
        log.info("memberLoginId={}", memberLoginDto.getLoginId());
        Member member = memberRepository.findByLoginId(memberLoginDto.getLoginId());
        String encodedPassword = (member == null) ? "" : member.getPassword();

        if (member != null) {
            log.info("member is present. LoginId={}", member.getLoginId());
            if(!passwordEncoder.matches(memberLoginDto.getPassword(), encodedPassword)) {
                log.info("password not matched");
                return MemberVerifyResponseDto.builder()
                        .isValid(false)
                        .build();
            }
            return MemberVerifyResponseDto.builder()
                    .isValid(true)
                    .role(member.getRole())
                    .build();
        } else {
            log.info("member is empty");
            return MemberVerifyResponseDto.builder()
                    .isValid(false)
                    .build();
        }
    }


    @Transactional
    public void updateRefreshToken(String loginId, String refreshToken){
        Optional<Member> member = authRepository.findByLoginId(loginId);
        if(member.isEmpty())
            return;
        log.info("memberRefreshToken={}", member.get().getRefreshToken());
        member.get().updateRefreshToken(refreshToken);
    }

    @Transactional
    public boolean changeMemberRole(String loginId, Role role){
        Optional<Member> temp = authRepository.findByLoginId(loginId);
        if(temp.isEmpty()) return false;

        Member member = temp.get();
        if(member.getRole() == Role.USER) {
            member.setRole(Role.ADMIN);
        } else {
            member.setRole(Role.USER);
        }
        return true;
    }

    @Transactional
    public Jwt refreshToken(String refreshToken){
        try{
            jwtProvider.getClaims(refreshToken);
            Optional<Member> temp = authRepository.findByRefreshToken(refreshToken);
            if(temp.isEmpty())
                return null;
            Member member = temp.get();

            HashMap<String, Object> claims = new HashMap<>();
            AuthenticateMember authenticateMember = new AuthenticateMember(member.getLoginId(),
                    member.getRole());
            String authenticateUserJson = objectMapper.writeValueAsString(authenticateMember);
            claims.put(VerifyMemberFilter.AUTHENTICATE_MEMBER,authenticateUserJson);
            Jwt jwt = jwtProvider.createJwt(claims);
            updateRefreshToken(member.getLoginId(),jwt.getRefreshToken());
            return jwt;
        } catch (Exception e){
            return null;
        }
    }

}
