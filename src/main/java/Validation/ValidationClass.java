package Validation;

public class ValidationClass implements Validaton<String> {

    @Override
    public ValidationResult validate(String input) {
        ValidationResult result = new ValidationResult();

        if (input == null || input.isBlank()) {
            result.addError("Ввод не может быть пустым");
            return result;
        }

        // Проверка: только число
        if (!input.matches("\\d+")) {
            result.addError("Введите число, а не текст");
            return result;
        }

        int option = Integer.parseInt(input);

        // Проверка диапазона
        if (option < 0 || option > 6) {
            result.addError("Введите число от 0 до 6");
        }

        return result;
    }
}
