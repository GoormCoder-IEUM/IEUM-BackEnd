package com.goormcoder.ieum.service;


import com.goormcoder.ieum.domain.Place;
import com.goormcoder.ieum.domain.Plan;
import com.goormcoder.ieum.repository.PlaceRepository;
import com.goormcoder.ieum.repository.PlanRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlanRepository planRepository;

    @Autowired
    public PlaceService(PlaceRepository placeRepository, PlanRepository planRepository) {
        this.placeRepository = placeRepository;
        this.planRepository = planRepository;
    }

    public List<Place> findAllPlaces() {
        return placeRepository.findAll();
    }

    public Optional<Place> findPlaceById(Long id) {
        return placeRepository.findById(id);
    }

    public Place createPlace(Long planId, Place place) {
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new RuntimeException("Plan not found"));
        place = Place.builder()
                .plan(plan)
                .placeOrder(place.getPlaceOrder())
                .placeDay(place.getPlaceDay())
                .placeName(place.getPlaceName())
                .placeLocation(place.getPlaceLocation())
                .build();
        return placeRepository.save(place);
    }

    public Place updatePlace(Long id, Place updatedPlace) {
        return placeRepository.findById(id)
                .map(place -> {
                    place.setDeletedAt(updatedPlace.getDeletedAt());
                    return placeRepository.save(place);
                })
                .orElseGet(() -> {
                    updatedPlace.setId(id);
                    return placeRepository.save(updatedPlace);
                });
    }

    public void deletePlace(Long id) {
        placeRepository.deleteById(id);
    }
}
