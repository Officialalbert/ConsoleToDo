package ru.albert.consoletodo;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import ru.albert.consoletodo.validation.ValidationClass;
import ru.albert.consoletodo.validation.ValidationResult;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;


/**
 * Простой тестовый класс для ValidationClass.
 * Тестируем логику валидации ввода.
 */
public class ValidationClassTest {

    private ValidationClass validator;

    // Метод @Before выполняется перед каждым тестом
    // Это удобно для инициализации тестовых данных
    @Before("")
    public void setUp() {
        validator = new ValidationClass();
    }

    /**
     * Тест: валидный ввод "3".
     * Число 3 находится в диапазоне 0-6, ошибок быть не должно.
     */
    @Test
    public void testValidInput() {
        ValidationResult result = validator.validate("3");

        // Проверяем, что результат валидный (нет ошибок)
        assertTrue("Результат должен быть валидным", result.isValid());
        assertTrue("Список ошибок должен быть пустым", result.getErrors().isEmpty());
    }

    /**
     * Тест: ввод граничного значения "0".
     * 0 - минимальное допустимое значение.
     */
    @Test
    public void testBoundaryMinValue() {
        ValidationResult result = validator.validate("0");

        assertTrue("0 должен быть валидным значением", result.isValid());
    }

    /**
     * Тест: ввод граничного значения "6".
     * 6 - максимальное допустимое значение.
     */
    @Test
    public void testBoundaryMaxValue() {
        ValidationResult result = validator.validate("6");

        assertTrue("6 должен быть валидным значением", result.isValid());
    }

    /**
     * Тест: ввод значения за пределами диапазона "7".
     * 7 больше 6, должна быть ошибка.
     */
    @Test
    public void testOutOfRangeHigh() {
        ValidationResult result = validator.validate("7");

        assertFalse("Результат не должен быть валидным", result.isValid());
        assertFalse("Должны быть ошибки", result.getErrors().isEmpty());
    }

    /**
     * Тест: ввод отрицательного числа "-1".
     * Отрицательные числа не допускаются.
     */
    @Test
    public void testNegativeNumber() {
        ValidationResult result = validator.validate("-1");

        assertFalse("Отрицательные числа не валидны", result.isValid());
    }

    /**
     * Тест: ввод текста вместо числа.
     * Должна быть ошибка "Введите число, а не текст".
     */
    @Test
    public void testTextInput() {
        ValidationResult result = validator.validate("abc");

        assertFalse("Текст не должен приниматься", result.isValid());
        assertTrue("Должна быть ошибка", result.getErrors().size() > 0);
    }

    /**
     * Тест: пустая строка.
     * Пустой ввод не допускается.
     */
    @Test
    public void testEmptyString() {
        ValidationResult result = validator.validate("");

        assertFalse("Пустая строка не валидна", result.isValid());
    }

    /**
     * Тест: null-значение.
     * null не должен приниматься.
     */
    @Test
    public void testNullInput() {
        ValidationResult result = validator.validate(null);

        assertFalse("null не валиден", result.isValid());
    }

    /**
     * Тест: строка с пробелами.
     * Пробелы не считаются валидным вводом.
     */
    @Test
    public void testWhitespaceInput() {
        ValidationResult result = validator.validate("   ");

        assertFalse("Пробелы не валидны", result.isValid());
    }

    /**
     * Тест: число с буквами "123abc".
     * Смешанный ввод не принимается.
     */
    @Test
    public void testMixedInput() {
        ValidationResult result = validator.validate("123abc");

        assertFalse("Смешанный ввод не валиден", result.isValid());
    }
}