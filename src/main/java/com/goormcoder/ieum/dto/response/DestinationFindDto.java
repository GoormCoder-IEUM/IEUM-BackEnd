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
                        destination.getDestinationType().getKrName(),
                        destination.getDestinationType().getEnName(),
                        destination.getDescription()
                )).toList();
    }

}
