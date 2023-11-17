package com.zerobase.reserve.domain.store.api;

import com.zerobase.reserve.domain.store.dto.Registration;
import com.zerobase.reserve.domain.store.dto.StoreDto;
import com.zerobase.reserve.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@RestController
public class StoreController {
    private final StoreService storeService;

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<StoreDto> registration(
            @RequestBody @Valid Registration request) {
        return ResponseEntity.ok(storeService.registration(request));
    }

    // 매장 검색, 매장 상세 정보
}
