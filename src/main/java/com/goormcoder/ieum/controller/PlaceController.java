package com.goormcoder.ieum.controller;


import com.goormcoder.ieum.domain.Place;
import com.goormcoder.ieum.dto.request.PlaceCreateDto;
import com.goormcoder.ieum.dto.request.PlaceShareDto;
import com.goormcoder.ieum.dto.response.PlaceFindDto;
import com.goormcoder.ieum.dto.response.PlaceInfoDto;
import com.goormcoder.ieum.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/plans/{planId}/places")
@Tag(name = "Place", description = "장소 관련 API")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/share-place")
    public void addPlace(@Payload PlaceShareDto placeShareDto) {
        // UUID memberId = getMemberId(); - 검증 추가 예정
        PlaceFindDto placeFindDto = placeService.sharePlace(placeShareDto);
        messagingTemplate.convertAndSend("/sub/plans/" + placeShareDto.planId(), placeFindDto);
    }

    @PostMapping("/pre-place")
    @Operation(summary = "장소 추가", description = "사용자별로 방문하고 싶은 장소를 추가합니다. 카테고리 유형 - 1(명소) 또는 2(식당/카페) 또는 3(숙소)")
    public ResponseEntity<PlaceInfoDto> createPlace(@PathVariable Long planId, @RequestBody PlaceCreateDto placeCreateDto) {
        UUID memberId = getMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(placeService.createPlace(planId, memberId, placeCreateDto));
    }

    private UUID getMemberId() {
        return UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

    @GetMapping
    public List<Place> getAllPlaces(@PathVariable Long planId) {
        return placeService.findAllPlaces();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Long planId, @PathVariable Long id) {
//        Optional<Place> place = placeService.findPlaceById(id);
//        return place.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Place> updatePlace(@PathVariable Long planId, @PathVariable Long id, @RequestBody Place updatedPlace) {
        return ResponseEntity.ok(placeService.updatePlace(id, updatedPlace));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long planId, @PathVariable Long id) {
        placeService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }
}
