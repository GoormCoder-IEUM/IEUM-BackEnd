package com.goormcoder.ieum.service;

import com.goormcoder.ieum.config.DestinationDescriptions;
import com.goormcoder.ieum.domain.Category;
import com.goormcoder.ieum.domain.Destination;
import com.goormcoder.ieum.domain.enumeration.CategoryType;
import com.goormcoder.ieum.domain.enumeration.DestinationName;
import com.goormcoder.ieum.repository.CategoryRepository;
import com.goormcoder.ieum.repository.DestinationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataInitializerService {

    private final CategoryRepository categoryRepository;
    private final DestinationRepository destinationRepository;
    private final DestinationDescriptions descriptions;

    private final CategoryType[] categoryTypes = {
            CategoryType.ATTRACTION,
            CategoryType.FOOD,
            CategoryType.ACCOMMODATION
    };

    private final DestinationName[] destinationNames = {
            DestinationName.JEJU,
            DestinationName.BUSAN,
            DestinationName.SEOUL,
            DestinationName.GYEONGJU,
            DestinationName.GANGNEUNG,
            DestinationName.YEOSU,
            DestinationName.JEONJU,
            DestinationName.POHANG,
            DestinationName.INCHEON,
            DestinationName.DAEJEON
    };

    @PostConstruct
    public void init() {
        createCategories();
        createDestinations();
    }

    public void createCategories() {
        for(CategoryType categoryType : categoryTypes) {
            if(!categoryRepository.existsByCategoryType(categoryType)) {
                categoryRepository.save(Category.of(categoryType));
            }
        }
    }

    public void createDestinations() {
        for(DestinationName destinationName : destinationNames) {
            if(!destinationRepository.existsByDestinationName(destinationName)) {
                String description = descriptions.getDescription().get(destinationName.toString());
                destinationRepository.save(Destination.of(destinationName, description));
            }
        }
    }

}
