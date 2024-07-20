package com.goormcoder.ieum.controller;


import com.goormcoder.ieum.domain.Place;
import com.goormcoder.ieum.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    public List<Place> getAllPlaces() {
        return placeService.findAllPlaces();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Long id) {
        Optional<Place> place = placeService.findPlaceById(id);
        return place.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Place> updatePlace(@PathVariable Long id, @RequestBody Place updatedPlace) {
        return ResponseEntity.ok(placeService.updatePlace(id, updatedPlace));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }
}
