package ru.anykeyers.commonsapi.domain.configuration;

import jakarta.validation.constraints.Null;
import lombok.*;
import ru.anykeyers.commonsapi.validation.OnCreate;

/**
 * Информация об организации
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationInfo {
    /**
     * ИНН
     */
    private String tin;
    /**
     * Тип организации
     */
    private TypeOrganization typeOrganization;
    /**
     * Почта
     */
    private String email;
    /**
     * Название
     */
    @Null(
            groups = OnCreate.class
    )
    private String name;
    /**
     * Описание
     */
    @Null(
            groups = OnCreate.class
    )
    private String description;
    /**
     * Номер телефона
     */
    @Null(
            groups = OnCreate.class
    )
    private String phoneNumber;
}
