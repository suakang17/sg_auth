package com.suakang.memberserver.member.repository;

import com.suakang.memberserver.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Override
    <S extends Member> S save(S member);

    Member findByLoginId(String loginId);

    int countByLoginId(String loginId);

    Optional<Member> findByRefreshToken(String refreshToken);
}
