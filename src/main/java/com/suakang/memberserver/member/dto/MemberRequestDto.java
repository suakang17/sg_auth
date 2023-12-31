package com.suakang.memberserver.member.dto;

import com.suakang.memberserver.member.util.constant.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@ToString
public class MemberRequestDto {

    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private Gender gender;
    @NotBlank
    private LocalDate birth;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        if (StringUtils.isEmpty(password)) {
            return;
        }
        this.password = passwordEncoder.encode(password);
    }
}
