package ru.anykeyers.serviceofservices.processor;

import org.springframework.stereotype.Component;

/**
 * Генератор версий
 */
@Component
public class VersionGenerator {

    /**
     * Сгенерировать новую версию
     *
     * @param currentVersion текущая версия
     */
    public String generateVersion(String currentVersion) {
        String[] parts = currentVersion.split("\\.");
        int major = Integer.parseInt(parts[0]);
        int minor = Integer.parseInt(parts[1]);
        return major + "." + (minor + 1);
    }

}
