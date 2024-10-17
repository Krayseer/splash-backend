package ru.anykeyers.commonsapi.domain.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Данные о боксе
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoxDTO {
    private Long carWashId;
    private String name;

    private Long id;
}
