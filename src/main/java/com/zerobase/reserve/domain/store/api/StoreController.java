package com.zerobase.reserve.domain.store.api;

import com.zerobase.reserve.domain.store.dto.Registration;
import com.zerobase.reserve.domain.store.dto.SearchResponse;
import com.zerobase.reserve.domain.store.dto.StoreDto;
import com.zerobase.reserve.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
}
