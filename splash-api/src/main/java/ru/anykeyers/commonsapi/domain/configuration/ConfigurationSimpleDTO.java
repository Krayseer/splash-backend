package ru.anykeyers.commonsapi.domain.configuration;

import lombok.Builder;

@Builder
public record ConfigurationSimpleDTO(Long id, String name) {
}
