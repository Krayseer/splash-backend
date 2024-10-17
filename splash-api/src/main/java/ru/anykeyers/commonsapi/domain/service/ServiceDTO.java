package ru.anykeyers.commonsapi.domain.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
    Long id;
    String name;
    long duration;
    int price;
}