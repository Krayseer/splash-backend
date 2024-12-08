package ru.anykeyers.commonsapi.domain.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    private Long id;

    private String name;

}
