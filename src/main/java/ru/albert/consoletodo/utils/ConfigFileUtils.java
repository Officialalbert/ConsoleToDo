package ru.albert.consoletodo.utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@UtilityClass
public class ConfigFileUtils {
    private static final Properties CONFIG = new Properties();
    private static final String CONFIG_FILE = "application.properties";

    static {
        loadConfig();
    }

    public static String getCONFIG(String key) {
        return CONFIG.getProperty(key);
    }

    private static void loadConfig() {
        // Более простой и читаемый вариант
        try (InputStream is = ConfigFileUtils.class.getResourceAsStream("/" + CONFIG_FILE)) {

            if (is == null) {
                throw new IllegalStateException("Файл " + CONFIG_FILE + " не найден в classpath");
            }

            CONFIG.load(is);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки конфигурации: " + e.getMessage(), e);
        }
    }
}