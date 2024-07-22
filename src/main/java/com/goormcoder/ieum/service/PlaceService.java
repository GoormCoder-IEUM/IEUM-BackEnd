package com.goormcoder.ieum.service;


import com.goormcoder.ieum.domain.Category;
import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.Place;
import com.goormcoder.ieum.domain.Plan;
import com.goormcoder.ieum.dto.request.PlaceCreateDto;
import com.goormcoder.ieum.dto.request.PlaceShareDto;
import com.goormcoder.ieum.dto.response.PlaceFindDto;
import com.goormcoder.ieum.dto.response.PlaceInfoDto;
import com.goormcoder.ieum.exception.ErrorMessages;
import com.goormcoder.ieum.repository.CategoryRepository;
import com.goormcoder.ieum.repository.PlaceRepository;
import com.goormcoder.ieum.repository.PlanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PlaceInfoDto createPlace(UUID memberId, PlaceCreateDto dto) {
        Member member = memberService.findById(memberId);
        Plan plan = planService.findByPlanId(dto.planId());
        Category category = findByCategoryId(dto.categoryId());
        Place place = Place.of(plan, member, null, null, dto.placeName(), dto.address(), category);
        plan.addPlace(place);
        planRepository.save(plan);

        return PlaceInfoDto.of(findByPlaceNameAndMember(dto.placeName(), member));
    }

    @Transactional
    public PlaceFindDto sharePlace(PlaceShareDto dto) {
        // memberService.findById(memberId); - 검증 추가 예정
        Place place = findPlaceById(dto.placeId());
        place.marksActivatedAt();

        Plan plan = planService.findByPlanId(dto.planId());
        plan.addPlace(place);
        planRepository.save(plan);

        return PlaceFindDto.of(place);
    }

    public List<Place> findAllPlaces() {
        return placeRepository.findAll();
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

    public void deletePlace(Long id) {
        placeRepository.deleteById(id);
    }

    private Category findByCategoryId(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.CATEGORY_NOT_FOUND.getMessage()));
    }

    private Place findPlaceById(Long placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.PLACE_NOT_FOUND.getMessage()));
    }

    private Place findByPlaceNameAndMember(String placeName, Member member) {
        return placeRepository.findByPlaceNameAndMember(placeName, member);
    }

}
