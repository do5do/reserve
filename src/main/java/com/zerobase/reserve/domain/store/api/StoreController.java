package com.zerobase.reserve.domain.store.api;

import com.zerobase.reserve.domain.store.dto.Registration;
import com.zerobase.reserve.domain.store.dto.SearchResponse;
import com.zerobase.reserve.domain.store.dto.SortCondition;
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

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<Registration.Response> registration(
            @RequestBody @Valid Registration.Request request) {
        StoreDto storeDto = storeService.registration(request);
        return ResponseEntity.ok(
                Registration.Response.from(storeDto));
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponse> searchKeyword(
            @RequestParam String keyword) {
        return ResponseEntity.ok(
                new SearchResponse(storeService.searchKeyword(keyword)));
    }

    @GetMapping("/{storeKey}")
    public ResponseEntity<StoreDto> information(@PathVariable String storeKey) {
        return ResponseEntity.ok(storeService.information(storeKey));
    }

    // todo 상점 목록 (가나다순, 별점순, 거리순) 가나다 순은 sort=name 으로 하면 되는데, 별점, 거리를 어떻게 구현하지,, if로 나누고 싶진 않은데,,
    @GetMapping
    public ResponseEntity<?> stores(final Pageable pageable,
                                    @Valid SortCondition sortCondition) {
        return ResponseEntity.ok(storeService.stores(pageable, sortCondition));
    }
}
