package com.zerobase.reserve.domain.store.api;

import com.zerobase.reserve.domain.store.dto.*;
import com.zerobase.reserve.domain.store.dto.model.StoreDto;
import com.zerobase.reserve.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@RestController
public class StoreController {
    private final StoreService storeService;

    /**
     * 매장 등록
     */
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<Registration.Response> registration(
            @RequestBody @Valid Registration.Request request) {
        StoreDto storeDto = storeService.registration(request);
        return ResponseEntity.ok(
                Registration.Response.from(storeDto));
    }

    /**
     * 매장 검색
     */
    @GetMapping("/search")
    public ResponseEntity<SearchResponse> searchKeyword(
            @RequestParam String keyword) {
        return ResponseEntity.ok(
                new SearchResponse(storeService.searchKeyword(keyword)));
    }

    /**
     * 매장 정보
     */
    @GetMapping("/{storeKey}")
    public ResponseEntity<StoreDto> information(@PathVariable String storeKey) {
        return ResponseEntity.ok(storeService.information(storeKey));
    }

    // todo 상점 목록 (가나다순, 별점순, 거리순) 팩토리 패턴 사용
    @GetMapping
    public ResponseEntity<?> stores(final Pageable pageable,
                                    @Valid SortCondition sortCondition) {
        return ResponseEntity.ok(storeService.stores(pageable, sortCondition));
    }

    /**
     * 매장 수정
     */
    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping
    public ResponseEntity<StoreDto> edit(@RequestBody @Valid EditRequest request) {
        return ResponseEntity.ok(storeService.edit(request));
    }

    /**
     * 매장 삭제
     */
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{storeKey}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable String storeKey) {
        return ResponseEntity.ok(
                new DeleteResponse(storeService.delete(storeKey))
        );
    }
}
