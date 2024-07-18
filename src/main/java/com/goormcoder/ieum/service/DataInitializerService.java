package com.goormcoder.ieum.service;

import com.goormcoder.ieum.domain.Category;
import com.goormcoder.ieum.domain.CategoryType;
import com.goormcoder.ieum.domain.Destination;
import com.goormcoder.ieum.domain.DestinationType;
import com.goormcoder.ieum.repository.CategoryRepository;
import com.goormcoder.ieum.repository.DestinationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataInitializerService {

    private final CategoryRepository categoryRepository;
    private final DestinationRepository destinationRepository;

    @PostConstruct
    public void init() {
        createCategories();
        createDestinations();
    }

    public void createCategories() {
        if(categoryRepository.count() == 0) {
            List<Category> categories = Arrays.asList(
                    Category.of(CategoryType.ATTRACTION),
                    Category.of(CategoryType.FOOD),
                    Category.of(CategoryType.ACCOMMODATION)
            );
            
            categoryRepository.saveAll(categories);
        }
    }

    public void createDestinations() {
        if(destinationRepository.count() == 0) {
            List<Destination> destinations = Arrays.asList(
                    Destination.of(DestinationType.JEJU),
                    Destination.of(DestinationType.BUSAN),
                    Destination.of(DestinationType.SEOUL),
                    Destination.of(DestinationType.GYEONGJU),
                    Destination.of(DestinationType.GANGNEUNG),
                    Destination.of(DestinationType.YEOSU),
                    Destination.of(DestinationType.JEONJU),
                    Destination.of(DestinationType.POHANG),
                    Destination.of(DestinationType.INCHEON),
                    Destination.of(DestinationType.DAEJEON)
            );

            destinationRepository.saveAll(destinations);
        }
    }

}
