package ru.anykeyers.commonsapi.domain.configuration;

import lombok.*;

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
    private Type type;
    /**
     * Почта
     */
    private String email;
    /**
     * Название
     */
    private String name;
    /**
     * Описание
     */
    private String description;
    /**
     * Номер телефона
     */
    private String phoneNumber;

    /**
     * Тип организации
     */
    public enum Type {
        /**
         * ООО
         */
        OOO,
        /**
         * ИП
         */
        IP
    }
}
