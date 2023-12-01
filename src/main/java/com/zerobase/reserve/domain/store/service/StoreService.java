package com.zerobase.reserve.domain.store.service;

import com.zerobase.reserve.domain.common.utils.KeyGenerator;
import com.zerobase.reserve.domain.member.entity.Member;
import com.zerobase.reserve.domain.member.service.MemberService;
import com.zerobase.reserve.domain.store.dto.EditRequest;
import com.zerobase.reserve.domain.store.dto.Registration;
import com.zerobase.reserve.domain.store.dto.Location;
import com.zerobase.reserve.domain.store.dto.StoresResponse;
import com.zerobase.reserve.domain.store.dto.model.AddressDto;
import com.zerobase.reserve.domain.store.dto.model.StoreDto;
import com.zerobase.reserve.domain.store.entity.Address;
import com.zerobase.reserve.domain.store.entity.Store;
import com.zerobase.reserve.domain.store.exception.StoreException;
import com.zerobase.reserve.domain.store.repository.StoreRepository;
import com.zerobase.reserve.global.infra.address.CoordinateClient;
import com.zerobase.reserve.global.infra.address.dto.CoordinateDto;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zerobase.reserve.global.exception.ErrorCode.STORE_NOT_FOUND;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final MemberService memberService;
    private final KeyGenerator keyGenerator;
    private final CoordinateClient coordinateClient;
    private static final String POINT = "POINT(%f %f)";

    public Store getStoreOrThrow(String storeKey) {
        return storeRepository.findByStoreKey(storeKey)
                .orElseThrow(() -> new StoreException(STORE_NOT_FOUND));
    }

    @Transactional
    public StoreDto registration(Registration.Request request) {
        Member member = memberService.getMemberOrThrow(request.getMemberKey());
        CoordinateDto response = getCoordinate(request.getAddress().address());

        Store store = request.toEntity(
                keyGenerator.generateKey(),
                response.getX(),
                response.getY()
        );

        store.addMember(member);
        storeRepository.save(store);
        return StoreDto.fromEntity(store);
    }

    private CoordinateDto getCoordinate(String address) {
        return coordinateClient.getCoordinate(address);
    }

    public List<String> searchKeyword(String keyword) {
        Pageable limit = PageRequest.of(0, 50);
        return storeRepository.findAllByNameContains(keyword, limit)
                .stream()
                .map(Store::getName)
                .toList();
    }

    public StoreDto information(String storeKey) {
        return StoreDto.fromEntity(getStoreOrThrow(storeKey));
    }

    public Slice<StoresResponse> stores(Location location, Pageable pageable) {
        return storeRepository.findByDistance(
                String.format(POINT, location.y(), location.x()), pageable)
                .map(StoresResponse::from);
    }

    @Transactional
    public StoreDto edit(EditRequest request) {
        Store store = getStoreOrThrow(request.getStoreKey());

        Address savedAddress = store.getAddress();
        Point coordinate = savedAddress.getCoordinate();
        Double x = coordinate.getX();
        Double y = coordinate.getY();

        String requestAddr = request.getAddress().address();

        if (!requestAddr.equals(savedAddress.getAddress())) {
            CoordinateDto newCoordinate = getCoordinate(requestAddr);
            x = newCoordinate.getX();
            y = newCoordinate.getY();
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
