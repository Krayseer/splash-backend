package ru.anykeyers.storageservice.domain;

import com.amazonaws.util.StringUtils;
import lombok.Getter;

/**
 * Тип файла
 */
@Getter
public enum FileType {
    /**
     * Картинка
     */
    PHOTO(".jpg"),
    /**
     * Видео
     */
    VIDEO(".mp4");

    private final String format;

    FileType(String format) {
        this.format = format;
    }

    public static FileType getFileType(String format) {
        return StringUtils.isNullOrEmpty(format) ? null : FileType.valueOf(format);
    }

}
