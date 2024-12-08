package ru.anykeyers.commonsapi.domain.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {

    private Long id;

    private String name;

    private long duration;

    private int price;

    @JsonProperty(
            access = JsonProperty.Access.READ_ONLY
    )
    private String version;

    @JsonProperty(
            access = JsonProperty.Access.READ_ONLY
    )
    private String originalServiceId;

    @JsonProperty(
            access = JsonProperty.Access.READ_ONLY
    )
    private boolean actual;

    public ServiceDTO(Long id, String name, long duration, int price) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.price = price;
    }

}