package ru.anykeyers.storageproto;

/**
 * Тип файла
 */
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

    public String getFormat() {
        return format;
    }
}
