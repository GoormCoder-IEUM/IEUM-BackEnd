package com.goormcoder.ieum.service;


import com.goormcoder.ieum.domain.Category;
import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.Place;
import com.goormcoder.ieum.domain.Plan;
import com.goormcoder.ieum.dto.request.PlaceCreateDto;
import com.goormcoder.ieum.exception.ErrorMessages;
import com.goormcoder.ieum.repository.CategoryRepository;
import com.goormcoder.ieum.repository.PlaceRepository;
import com.goormcoder.ieum.repository.PlanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    public void createPlace(UUID memberId, PlaceCreateDto dto) {
        Member member = memberService.findById(memberId);
        Plan plan = planService.findByPlanId(dto.planId());
        Category category = findByCategoryId(dto.categoryId());
        Place place = placeRepository.save(Place.of(plan, member, null, null, dto.placeName(), dto.address(), category));
    }

    public List<Place> findAllPlaces() {
        return placeRepository.findAll();
    }

    public Optional<Place> findPlaceById(Long id) {
        return placeRepository.findById(id);
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
}
