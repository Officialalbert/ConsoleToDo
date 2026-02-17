package ru.albert.consoletodo;

import org.junit.jupiter.api.Test;
import ru.albert.consoletodo.model.DaoEntity;


import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Простой тестовый класс для DaoEntity.
 * Этот тест проверяет базовую работу модели данных.
 */
public class DaoEntityTest {

    /**
     * Тест: создание новой задачи без ID.
     * Проверяем, что объект создается и значение устанавливается корректно.
     */
    @Test
    public void testDaoEntityCreation() {
        // Создаем новую задачу
        DaoEntity entity = new DaoEntity();
        entity.setValue("Test task");

        // Проверяем, что значение установилось
        assertEquals("Test task", entity.getValue());
    }

    /**
     * Тест: проверка метода toString().
     * Каждый объект должен иметь строковое представление.
     */
    @Test
    public void testDaoEntityToString() {
        DaoEntity entity = new DaoEntity();
        entity.setValue("Simple task");

        // Вызываем toString() - просто проверяем, что метод не вызывает ошибку
        String result = entity.toString();

        // Проверяем, что строка содержит наше значение
        assertTrue(result.contains("Simple task"));
    }

    /**
     * Тест: проверка валидного ID задачи.
     */
    @Test
    public void testDaoEntityId() {
        DaoEntity entity = new DaoEntity();
        entity.setId(1);

        assertEquals(1, entity.getId());
    }

    /**
     * Тест: проверка timestamp (времени создания).
     */
    @Test
    public void testDaoEntityTimestamp() {
        DaoEntity entity = new DaoEntity();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        entity.setCreatedAt(now);

        assertEquals(now, entity.getCreatedAt());
    }
}