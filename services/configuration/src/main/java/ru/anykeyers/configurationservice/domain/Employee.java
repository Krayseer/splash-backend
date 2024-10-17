package ru.anykeyers.configurationservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Работник
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя
     */
    private UUID userId;

    /**
     * Автомойка, которой принадлежнит работник
     */
    @ManyToOne
    @JoinColumn(name = "CONFIGURATION_ID")
    private Configuration configuration;

}
