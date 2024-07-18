package com.goormcoder.ieum.service;

import com.goormcoder.ieum.config.DestinationDescriptions;
import com.goormcoder.ieum.domain.Category;
import com.goormcoder.ieum.domain.enumeration.CategoryType;
import com.goormcoder.ieum.domain.Destination;
import com.goormcoder.ieum.domain.enumeration.DestinationName;
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
                    Destination.of(DestinationName.JEJU, descriptions.getDescription().get(DestinationName.JEJU.toString())),
                    Destination.of(DestinationName.BUSAN, descriptions.getDescription().get(DestinationName.BUSAN.toString())),
                    Destination.of(DestinationName.SEOUL, descriptions.getDescription().get(DestinationName.SEOUL.toString())),
                    Destination.of(DestinationName.GYEONGJU, descriptions.getDescription().get(DestinationName.GYEONGJU.toString())),
                    Destination.of(DestinationName.GANGNEUNG, descriptions.getDescription().get(DestinationName.GANGNEUNG.toString())),
                    Destination.of(DestinationName.YEOSU, descriptions.getDescription().get(DestinationName.YEOSU.toString())),
                    Destination.of(DestinationName.JEONJU, descriptions.getDescription().get(DestinationName.JEONJU.toString())),
                    Destination.of(DestinationName.POHANG, descriptions.getDescription().get(DestinationName.POHANG.toString())),
                    Destination.of(DestinationName.INCHEON, descriptions.getDescription().get(DestinationName.INCHEON.toString())),
                    Destination.of(DestinationName.DAEJEON, descriptions.getDescription().get(DestinationName.DAEJEON.toString()))
            );

            destinationRepository.saveAll(destinations);
        }
    }

}
