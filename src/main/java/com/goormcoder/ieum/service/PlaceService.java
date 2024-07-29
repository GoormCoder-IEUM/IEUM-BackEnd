package com.goormcoder.ieum.service;

import com.goormcoder.ieum.domain.Category;
import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.Place;
import com.goormcoder.ieum.domain.Plan;
import com.goormcoder.ieum.dto.request.PlaceCreateDto;
import com.goormcoder.ieum.dto.request.PlaceShareDto;
import com.goormcoder.ieum.dto.request.PlaceVisitTimeUpdateDto;
import com.goormcoder.ieum.dto.response.PlaceFindDto;
import com.goormcoder.ieum.dto.response.PlaceInfoDto;
import com.goormcoder.ieum.exception.ConflictException;
import com.goormcoder.ieum.exception.ErrorMessages;
import com.goormcoder.ieum.exception.ForbiddenException;
import com.goormcoder.ieum.repository.CategoryRepository;
import com.goormcoder.ieum.repository.PlaceRepository;
import com.goormcoder.ieum.repository.PlanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlanRepository planRepository;
    private final CategoryRepository categoryRepository;

    private final MemberService memberService;
    private final PlanService planService;

    @Transactional
    public PlaceInfoDto createPlace(Long planId, UUID memberId, PlaceCreateDto dto) {
        Member member = memberService.findById(memberId);
        Plan plan = planService.findByPlanId(planId);
        Category category = findByCategoryId(dto.categoryId());

        planService.validatePlanMember(plan, member);
        validateDuplicatePlace(plan, member, dto.placeName());

        Place place = Place.of(plan, member, null, null, dto.placeName(), dto.address(), category);
        plan.addPlace(place);
        planRepository.save(plan);

        return PlaceInfoDto.of(findByPlaceNameAndMember(dto.placeName(), member, plan));
    }

    @Transactional
    public PlaceFindDto sharePlace(PlaceShareDto dto) {
        // memberService.findById(memberId); - 검증 추가 예정
        // validatePlanMember(plan, member);
        Place place = findPlaceById(dto.placeId());
        Plan plan = planService.findByPlanId(dto.planId());
        validateSharedPlace(plan, place);

        place.marksActivatedAt();
        place.marksStartedAt(plan.getStartedAt());
        place.marksEndedAt(plan.getEndedAt());
        plan.addPlace(place);
        planRepository.save(plan);

        return PlaceFindDto.of(place);
    }

    @Transactional(readOnly = true)
    public PlaceFindDto getPlace(Long planId, Long placeId, UUID memberId) {
        Member member = memberService.findById(memberId);
        Plan plan = planService.findByPlanId(planId);
        planService.validatePlanMember(plan, member);

        Place place = findPlaceById(placeId);
        handleUnActivePlace(place, member);

        return PlaceFindDto.of(place);
    }

    @Transactional(readOnly = true)
    public List<PlaceFindDto> getAllPlaces(Long planId, UUID memberId) {
        Member member = memberService.findById(memberId);
        Plan plan = planService.findByPlanId(planId);
        planService.validatePlanMember(plan, member);

        return PlaceFindDto.listOf(placeRepository.findByMemberAndPlanAndActivatedAtIsNullAndDeletedAtIsNull(member, plan));
    }

    @Transactional(readOnly = true)
    public List<PlaceFindDto> getSharedPlaces(Long planId, UUID memberId) {
        Member member = memberService.findById(memberId);
        Plan plan = planService.findByPlanId(planId);
        planService.validatePlanMember(plan, member);

        return PlaceFindDto.listOf(placeRepository.findByPlanAndActivatedAtIsNotNullAndDeletedAtIsNull(plan));
    }

    @Transactional(readOnly = true)
    public List<PlaceFindDto> getSharedPlacesByDay(Long planId, Long day, UUID memberId) {
        Member member = memberService.findById(memberId);
        Plan plan = planService.findByPlanId(planId);
        planService.validatePlanMember(plan, member);

        LocalDate date = validateDayAndGetDate(plan, day);
        return PlaceFindDto.listOf(placeRepository.findByPlanAndDate(plan, date));
    }

    @Transactional
    public void deletePlace(Long planId, Long placeId, UUID memberId) {
        Member member = memberService.findById(memberId);
        Plan plan = planService.findByPlanId(planId);
        planService.validatePlanMember(plan, member);

        Place place = findPlaceById(placeId);
        handleUnActivePlace(place, member);
        place.markAsDeleted();
    }

    @Transactional
    public void updateVisitTime(Long planId, Long placeId, UUID memberId, PlaceVisitTimeUpdateDto dto) {
        Member member = memberService.findById(memberId);
        Plan plan = planService.findByPlanId(planId);
        planService.validatePlanMember(plan, member);
        validatePlaceVisitTimeUpdateDto(dto, plan);

        Place place = findPlaceById(placeId);
        if(place.isActivated()) {
            throw new IllegalArgumentException(ErrorMessages.BAD_REQUEST_PLACE_NOT_ACTIVE.getMessage());
        }

        place.marksStartedAt(dto.startedAt());
        place.marksEndedAt(dto.endedAt());
        placeRepository.save(place);
    }

    public Place updatePlace(Long id, Place updatedPlace) {
        return updatedPlace;
//        return placeRepository.findById(id)
//                .map(place -> {
//                    place.setDeletedAt(updatedPlace.getDeletedAt());
//                    return placeRepository.save(place);
//                })
//                .orElseGet(() -> {
//                    updatedPlace.setId(id);
//                    return placeRepository.save(updatedPlace);
//                });
    }

    private Category findByCategoryId(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.CATEGORY_NOT_FOUND.getMessage()));
    }

    private Place findPlaceById(Long placeId) {
        return placeRepository.findByIdAndDeletedAtIsNull(placeId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.PLACE_NOT_FOUND.getMessage()));
    }

    private Place findByPlaceNameAndMember(String placeName, Member member, Plan plan) {
        return placeRepository.findByPlaceNameAndMemberAndPlanAndDeletedAtIsNull(placeName, member, plan);
    }

    private void validateDuplicatePlace(Plan plan, Member member, String placeName) {
        if(placeRepository.existsByPlaceNameAndMemberAndPlanAndDeletedAtIsNull(placeName, member, plan)) {
            throw new ConflictException(ErrorMessages.PLACE_CONFLICT);
        }
    }

    private void validatePlaceVisitTimeUpdateDto(PlaceVisitTimeUpdateDto dto, Plan plan) {
        LocalDateTime start = dto.startedAt();
        LocalDateTime end = dto.endedAt();

        if(start.isBefore(plan.getStartedAt()) || start.isAfter(plan.getEndedAt())
                || end.isBefore(plan.getStartedAt()) || end.isAfter(plan.getEndedAt())) {
            throw new IllegalArgumentException(ErrorMessages.BAD_REQUEST_PLACE_VISIT_TIME.getMessage());
        }

        if(start.isAfter(end) || start.isEqual(end)) {
            throw new IllegalArgumentException(ErrorMessages.BAD_REQUEST_PLACE_VISIT_START_TIME.getMessage());
        }
    }

    private void handleUnActivePlace(Place place, Member member) {
        if(place.isActivated()) {
            if(!place.getMember().equals(member)) {
                throw new ForbiddenException(ErrorMessages.FORBIDDEN_ACCESS);
            }
        }
    }

    private LocalDate validateDayAndGetDate(Plan plan, Long day) {
        long duration = plan.getDuration();
        if(day < 1 || day > duration) {
            throw new IllegalArgumentException(ErrorMessages.BAD_REQUEST_DAY_NOT_IN_DURATION.getMessage());
        }

        return plan.getNthDayDate(day);
    }

    private void validateSharedPlace(Plan plan, Place place) {
        if(placeRepository.existsByPlanAndPlaceNameAndActivatedAtIsNotNullAndDeletedAtIsNull(plan, place.getPlaceName())) {
            throw new ConflictException(ErrorMessages.SHARED_PLACE_CONFLICT);
        }
    }

}
