package ru.anykeyers.configurationservice.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Бокс
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BOX")
public class Box {

    /**
     * Идентификатор
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название
     */
    @Column(name = "NAME")
    private String name;

    /**
     * Автомойка, которой принадлежит бокс
     */
    @ManyToOne
    @JoinColumn(name = "CONFIGURATION_ID")
    private Configuration configuration;

}
