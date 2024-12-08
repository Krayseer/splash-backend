package ru.anykeyers.serviceofservices.processor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Тесты для {@link VersionGenerator}
 */
@ExtendWith(MockitoExtension.class)
class VersionGeneratorTest {

    private final VersionGenerator generator = new VersionGenerator();

    /**
     * Тест генерации версии
     */
    @Test
    void generateVersion() {
        String result = generator.generateVersion("1.0");
        Assertions.assertEquals("1.1", result);

        String secondResult = generator.generateVersion("2.0");
        Assertions.assertEquals("2.1", secondResult);

        String thirdResult = generator.generateVersion("3.125");
        Assertions.assertEquals("3.126", thirdResult);
    }

}