package com.zerobase.reserve.domain.store.service;

import com.zerobase.reserve.domain.common.utils.KeyGenerator;
import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.member.exception.MemberException;
import com.zerobase.reserve.domain.member.repository.MemberRepository;
import com.zerobase.reserve.domain.store.dto.EditRequest;
import com.zerobase.reserve.domain.store.dto.Registration;
import com.zerobase.reserve.domain.store.dto.SortCondition;
import com.zerobase.reserve.domain.store.dto.model.AddressDto;
import com.zerobase.reserve.domain.store.dto.model.StoreDto;
import com.zerobase.reserve.domain.store.entity.Address;
import com.zerobase.reserve.domain.store.entity.Store;
import com.zerobase.reserve.domain.store.exception.StoreException;
import com.zerobase.reserve.domain.store.repository.StoreRepository;
import com.zerobase.reserve.global.infra.address.CoordinateClient;
import com.zerobase.reserve.global.infra.address.dto.CoordinateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zerobase.reserve.global.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.reserve.global.exception.ErrorCode.STORE_NOT_FOUND;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final KeyGenerator keyGenerator;
    private final CoordinateClient coordinateClient;

    @Transactional
    public StoreDto registration(Registration.Request request) {
        Member member = getMember(request.getMemberKey());
        CoordinateDto response = getCoordinate(request.getAddress().address());

        Store store = request.toEntity(
                keyGenerator.generateKey(),
                response.getX(),
                response.getY()
        );
        member.addStore(store);
        storeRepository.save(store);
        return StoreDto.fromEntity(store);
    }

    private CoordinateDto getCoordinate(String address) {
        return coordinateClient.getCoordinate(address);
    }

    private Member getMember(String memberKey) {
        return memberRepository.findByMemberKey(memberKey)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    public List<String> searchKeyword(String keyword) {
        Pageable limit = PageRequest.of(0, 50);
        return storeRepository.findAllByNameContains(keyword, limit)
                .stream()
                .map(Store::getName)
                .toList();
    }

    public StoreDto information(String storeKey) {
        return StoreDto.fromEntity(getStore(storeKey));
    }

    private Store getStore(String storeKey) {
        return storeRepository.findByStoreKey(storeKey)
                .orElseThrow(() -> new StoreException(STORE_NOT_FOUND));
    }

    public Page<StoreDto> stores(Pageable pageable, SortCondition sortCondition) {
        return storeRepository.findAll(pageable)
                .map(StoreDto::fromEntity);
    }

    @Transactional
    public StoreDto edit(EditRequest request) {
        Store store = getStore(request.getStoreKey());

        Address savedAddress = store.getAddress();
        Double x = savedAddress.getX();
        Double y = savedAddress.getY();

        String requestAddr = request.getAddress().address();

        if (!requestAddr.equals(savedAddress.getAddress())) {
            CoordinateDto coordinate = getCoordinate(requestAddr);
            x = coordinate.getX();
            y = coordinate.getY();
        }

        store.updateStore(request,
                AddressDto.toEntity(request.getAddress(), x, y));
        return StoreDto.fromEntity(store);
    }

    @Transactional
    public String delete(String storeKey) {
        validateStoreExists(storeKey);
        storeRepository.deleteByStoreKey(storeKey);
        return storeKey;
    }

    private void validateStoreExists(String storeKey) {
        if (!storeRepository.existsByStoreKey(storeKey)) {
            throw new StoreException(STORE_NOT_FOUND);
        }
    }
}
