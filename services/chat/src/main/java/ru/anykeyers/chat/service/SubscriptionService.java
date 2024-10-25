package ru.anykeyers.chat.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Сервис подписок
 */
@Service
public class SubscriptionService {

    /**
     * Карта вида [подписка -> список подписчиков]
     */
    private final Map<UUID, Set<UUID>> subscriptions;

    public SubscriptionService() {
        subscriptions = new ConcurrentHashMap<>();
    }

    /**
     * Добавить подписку
     *
     * @param id идентификатор подписки
     */
    public void addSubscription(UUID id) {
        subscriptions.put(id, new HashSet<>());
    }

    /**
     * Удалить подписку
     *
     * @param id идентификатор подписки
     */
    public void removeSubscription(UUID id) {
        subscriptions.remove(id);
    }

    /**
     * Подписаться
     *
     * @param subscription  идентификатор подписки
     * @param subscriber    идентификатор подписчика
     */
    public void subscribe(UUID subscription, UUID subscriber) {
        subscriptions.get(subscriber).add(subscription);
    }

    /**
     * Отписаться
     *
     * @param subscription  идентификатор подписки
     * @param subscriber    идентификатор подписчика
     */
    public void unsubscribe(UUID subscription, UUID subscriber) {
        subscriptions.get(subscriber).remove(subscription);
    }

}
