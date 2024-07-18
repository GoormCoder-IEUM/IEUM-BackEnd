package com.goormcoder.ieum.dto.response;

import com.goormcoder.ieum.domain.Destination;

import java.util.List;

public record DestinationFindDto(

        Long id,
        String krName,
        String enName,
        String description

) {

    public static List<DestinationFindDto> listOf(List<Destination> destinations) {
        return destinations.stream()
                .map(destination -> new DestinationFindDto(
                        destination.getId(),
                        destination.getDestinationName().getKrName(),
                        destination.getDestinationName().getEnName(),
                        destination.getDescription()
                )).toList();
    }

}
