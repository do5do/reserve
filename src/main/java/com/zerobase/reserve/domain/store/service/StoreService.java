package com.zerobase.reserve.domain.store.service;

import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.member.exception.MemberException;
import com.zerobase.reserve.domain.member.repository.MemberRepository;
import com.zerobase.reserve.domain.store.dto.Registration;
import com.zerobase.reserve.domain.store.dto.StoreDto;
import com.zerobase.reserve.domain.store.entity.Store;
import com.zerobase.reserve.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.reserve.global.exception.ErrorCode.MEMBER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public StoreDto registration(Registration request) {
        Member member = memberRepository.findByMemberId(request.memberId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Store store = request.toEntity();
        member.addStore(store);
        return StoreDto.fromEntity(storeRepository.save(store));
    }
}
