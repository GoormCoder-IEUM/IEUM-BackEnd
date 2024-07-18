package com.goormcoder.ieum.service;


import com.goormcoder.ieum.domain.Accommodation;
import com.goormcoder.ieum.domain.Plan;
import com.goormcoder.ieum.repository.AccommodationRepository;
import com.goormcoder.ieum.repository.PlanRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final PlanRepository planRepository;

    @Autowired
    public AccommodationService(AccommodationRepository accommodationRepository, PlanRepository planRepository) {
        this.accommodationRepository = accommodationRepository;
        this.planRepository = planRepository;
    }

    public List<Accommodation> findAllAccommodations() {
        return accommodationRepository.findAll();
    }

    public Optional<Accommodation> findAccommodationById(Long id) {
        return accommodationRepository.findById(id);
    }

    public Accommodation createAccommodation(Long planId, Accommodation accommodation) {
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new RuntimeException("Plan not found"));
        accommodation = Accommodation.builder()
                .plan(plan)
                .name(accommodation.getName())
                .location(accommodation.getLocation())
                .stayDate(accommodation.getAccomDay())
                .build();
        return accommodationRepository.save(accommodation);
    }

    public Accommodation updateAccommodation(Long id, Accommodation updatedAccommodation) {
        return accommodationRepository.findById(id)
                .map(accommodation -> {
                    accommodation.setDeletedAt(updatedAccommodation.getDeletedAt());
                    return accommodationRepository.save(accommodation);
                })
                .orElseGet(() -> {
                    updatedAccommodation.setId(id);
                    return accommodationRepository.save(updatedAccommodation);
                });
    }

    public void deleteAccommodation(Long id) {
        accommodationRepository.deleteById(id);
    }
}
