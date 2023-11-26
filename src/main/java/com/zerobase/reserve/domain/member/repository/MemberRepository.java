package com.zerobase.reserve.domain.member.repository;

import com.zerobase.reserve.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Member> findByMemberKey(String memberKey);

    Optional<Member> findByPhoneNumber(String phoneNumber);
}
