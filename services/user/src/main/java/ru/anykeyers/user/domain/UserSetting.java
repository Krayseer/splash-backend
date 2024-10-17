//package ru.anykeyers.authorizationserver.domain;
//
//import jakarta.persistence.*;
//import lombok.*;
//
///**
// * Настройки пользователя
// */
//@Getter
//@Setter
//@Entity
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class UserSetting {
//    /**
//     * Идентификатор
//     */
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    /**
//     * Разрешена отправка push уведомлений
//     */
//    private boolean pushEnabled;
//
//    /**
//     * Разрешена отправка уведомлений по email
//     */
//    private boolean emailEnabled;
//
//    /**
//     * Пользователь, которому принадлежат настройки
//     */
//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//}
