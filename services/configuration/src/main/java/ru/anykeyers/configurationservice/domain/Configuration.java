package ru.anykeyers.configurationservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.anykeyers.commonsapi.domain.Address;
import ru.anykeyers.commonsapi.domain.configuration.OrderProcessMode;
import ru.anykeyers.commonsapi.domain.configuration.OrganizationInfo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Конфигурация автомойки
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CONFIGURATION")
public class Configuration {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Идентификатор хозяина автомойки
     */
    private UUID userId;
    /**
     * Информация об организации
     */
    @Embedded
    private OrganizationInfo organizationInfo;
    /**
     * Адрес
     */
    @Embedded
    private Address address;
    /**
     * Время открытия
     */
    private String openTime;
    /**
     * Время закрытия
     */
    private String closeTime;
    /**
     * Список боксов
     */
    @OneToMany(
            mappedBy = "configuration",
            cascade = CascadeType.ALL
    )
    private List<Box> boxes;
    /**
     * Список фотографий
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "configuration_photo_urls",
            joinColumns = @JoinColumn(
                    name = "configuration_id"
            )
    )
    @Column(
            name = "photo_url"
    )
    private List<String> photoUrls;
    /**
     * Режим обработки заказов
     */
    @Column(
            name = "order_process_mode"
    )
    private OrderProcessMode orderProcessMode;
    /**
     * URL рекламного видеоролика
     */
    @Column(
            name = "video_id"
    )
    private String videoId;
    /**
     * Время регистрации автомойки
     */
    @CreationTimestamp
    private Instant createdAt;
    /**
     * Валидна ли организация
     */
    @Column(
            name = "valid"
    )
    private boolean valid = false;

    public void addPhotoUrls(List<String> photoUrls) {
        if (this.photoUrls == null) {
            this.photoUrls = new ArrayList<>();
        }
        this.photoUrls.addAll(photoUrls);
    }

    public void addVideo(String fileId) {
        this.videoId = fileId;
    }
}
