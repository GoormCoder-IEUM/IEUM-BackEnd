package com.goormcoder.ieum.controller;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.dto.request.PlaceCreateDto;
import com.goormcoder.ieum.dto.request.PlaceShareDto;
import com.goormcoder.ieum.dto.request.PlaceVisitTimeUpdateDto;
import com.goormcoder.ieum.dto.response.PlaceFindDto;
import com.goormcoder.ieum.dto.response.PlaceInfoDto;
import com.goormcoder.ieum.security.CustomUserDetails;
import com.goormcoder.ieum.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans/{planId}/places")
@Tag(name = "Place", description = "장소 관련 API")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/share-place")
    public void addPlace(@Valid @Payload PlaceShareDto placeShareDto, SimpMessageHeaderAccessor accessor) {
        CustomUserDetails userDetails = (CustomUserDetails) accessor.getSessionAttributes().get("userDetails");
        Member member = userDetails.getMember();
        PlaceFindDto placeFindDto = placeService.sharePlace(placeShareDto, member);
        messagingTemplate.convertAndSend("/sub/plans/" + placeShareDto.planId(), placeFindDto);
    }

    @PostMapping()
    @Operation(summary = "장소 추가", description = "사용자별로 방문하고 싶은 장소를 추가합니다. 카테고리 유형 - 1(명소) 또는 2(식당/카페) 또는 3(숙소)")
    public ResponseEntity<PlaceInfoDto> createPlace(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                    @PathVariable Long planId, @Valid @RequestBody PlaceCreateDto placeCreateDto) {
        Member member = userDetails.getMember();
        return ResponseEntity.status(HttpStatus.OK).body(placeService.createPlace(planId, member, placeCreateDto));
    }

    @GetMapping("/{placeId}")
    @Operation(summary = "장소 조회", description = "장소를 조회합니다. 공유되지 않은 장소의 경우 방문일시가 응답에 포함되지 않습니다.")
    public ResponseEntity<PlaceFindDto> getPlace(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @PathVariable Long planId, @PathVariable Long placeId) {
        Member member = userDetails.getMember();
        return ResponseEntity.status(HttpStatus.OK).body(placeService.getPlace(planId, placeId, member));
    }

    @GetMapping()
    @Operation(summary = "장소 전체 조회", description = "사용자별 장소를 전체 조회합니다. 사용자가 공유한 장소는 조회되지 않으며, 장소 방문일시는 응답에 포함되지 않습니다.")
    public ResponseEntity<List<PlaceFindDto>> getAllPlaces(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long planId) {
        Member member = userDetails.getMember();
        return ResponseEntity.status(HttpStatus.OK).body(placeService.getAllPlaces(planId, member));
    }

    @GetMapping("/shared")
    @Operation(summary = "공유 장소 전체 조회", description = "공유된 장소를 전체 조회합니다.")
    public ResponseEntity<List<PlaceFindDto>> getSharedPlaces(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long planId) {
        Member member = userDetails.getMember();
        return ResponseEntity.status(HttpStatus.OK).body(placeService.getSharedPlaces(planId, member));
    }

    @GetMapping("/shared/{day}")
    @Operation(summary = "공유 장소 일차별 조회", description = "해당 일차에 방문 예정인 공유 장소를 조회합니다.")
    public ResponseEntity<List<PlaceFindDto>> getSharedPlacesByDay(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                   @PathVariable Long planId, @PathVariable Long day) {
        Member member = userDetails.getMember();
        return ResponseEntity.status(HttpStatus.OK).body(placeService.getSharedPlacesByDay(planId, day, member));
    }

    @DeleteMapping("/{placeId}")
    @Operation(summary = "장소 삭제", description = "장소를 삭제합니다.")
    public ResponseEntity<String> createPlace(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable Long planId, @PathVariable Long placeId) {
        Member member = userDetails.getMember();
        placeService.deletePlace(planId, placeId, member);
        return ResponseEntity.status(HttpStatus.OK).body("장소가 삭제되었습니다.");
    }

    @PatchMapping("/{placeId}")
    @Operation(summary = "장소 방문일시 설정", description = "공유한 장소의 방문일시를 설정합니다.")
    public ResponseEntity<String> updateVisitTime(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long planId,
                                                  @PathVariable Long placeId, @Valid @RequestBody PlaceVisitTimeUpdateDto placeVisitTimeUpdateDto) {
        Member member = userDetails.getMember();
        placeService.updateVisitTime(planId, placeId, member, placeVisitTimeUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body("방문일시가 설정되었습니다.");
    }

}
