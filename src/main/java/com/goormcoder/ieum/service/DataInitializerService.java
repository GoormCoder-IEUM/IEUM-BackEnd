package com.goormcoder.ieum.service;

import com.goormcoder.ieum.config.DestinationDescriptions;
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
    private final DestinationDescriptions descriptions;

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
                    Destination.of(DestinationType.JEJU, descriptions.getDescription().get(DestinationType.JEJU.toString())),
                    Destination.of(DestinationType.BUSAN, descriptions.getDescription().get(DestinationType.BUSAN.toString())),
                    Destination.of(DestinationType.SEOUL, descriptions.getDescription().get(DestinationType.SEOUL.toString())),
                    Destination.of(DestinationType.GYEONGJU, descriptions.getDescription().get(DestinationType.GYEONGJU.toString())),
                    Destination.of(DestinationType.GANGNEUNG, descriptions.getDescription().get(DestinationType.GANGNEUNG.toString())),
                    Destination.of(DestinationType.YEOSU, descriptions.getDescription().get(DestinationType.YEOSU.toString())),
                    Destination.of(DestinationType.JEONJU, descriptions.getDescription().get(DestinationType.JEONJU.toString())),
                    Destination.of(DestinationType.POHANG, descriptions.getDescription().get(DestinationType.POHANG.toString())),
                    Destination.of(DestinationType.INCHEON, descriptions.getDescription().get(DestinationType.INCHEON.toString())),
                    Destination.of(DestinationType.DAEJEON, descriptions.getDescription().get(DestinationType.DAEJEON.toString()))
            );

            destinationRepository.saveAll(destinations);
        }
    }

}
