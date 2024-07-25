package com.goormcoder.ieum.controller;


import com.goormcoder.ieum.domain.Place;
import com.goormcoder.ieum.dto.request.PlaceCreateDto;
import com.goormcoder.ieum.dto.request.PlaceShareDto;
import com.goormcoder.ieum.dto.request.PlaceVisitTimeUpdateDto;
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

    @GetMapping("/{placeId}")
    @Operation(summary = "장소 조회", description = "장소를 조회합니다.")
    public ResponseEntity<PlaceFindDto> getPlace(@PathVariable Long planId, @PathVariable Long placeId) {
        UUID memberId = getMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(placeService.getPlace(planId, placeId, memberId));
    }

    @DeleteMapping("/{placeId}")
    @Operation(summary = "장소 삭제", description = "장소를 삭제합니다.")
    public ResponseEntity<String> createPlace(@PathVariable Long planId, @PathVariable Long placeId) {
        UUID memberId = getMemberId();
        placeService.deletePlace(planId, placeId, memberId);
        return ResponseEntity.status(HttpStatus.OK).body("장소가 삭제되었습니다.");
    }

    @PatchMapping("/{placeId}")
    @Operation(summary = "장소 방문일시 설정", description = "공유한 장소의 방문일시를 설정합니다.")
    public ResponseEntity<String> updateVisitTime(
            @PathVariable Long planId, @PathVariable Long placeId, @RequestBody PlaceVisitTimeUpdateDto placeVisitTimeUpdateDto) {
        UUID memberId = getMemberId();
        placeService.updateVisitTime(planId, placeId, memberId, placeVisitTimeUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body("방문일시가 설정되었습니다.");
    }

    private UUID getMemberId() {
        return UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

    @GetMapping
    public List<Place> getAllPlaces(@PathVariable Long planId) {
        return placeService.findAllPlaces();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Place> updatePlace(@PathVariable Long planId, @PathVariable Long id, @RequestBody Place updatedPlace) {
        return ResponseEntity.ok(placeService.updatePlace(id, updatedPlace));
    }

}
