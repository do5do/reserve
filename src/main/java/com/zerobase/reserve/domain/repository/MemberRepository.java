package com.zerobase.reserve.domain.repository;

import com.zerobase.reserve.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
