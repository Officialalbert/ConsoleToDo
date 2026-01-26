import Dao.DaoClass;
import Validation.ValidationClass;
import Validation.ValidationResult;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DaoClass dao = DaoClass.getInstance();
        Scanner scanner = new Scanner(System.in);
        int option = -1;
        ValidationClass validator = new ValidationClass();

        do {
            System.out.println("\n=== МЕНЮ ===");
            System.out.println("1 - Сохранить");
            System.out.println("2 - Изменить данные");
            System.out.println("3 - Удалить данные");
            System.out.println("4 - Просмотр всех");
            System.out.println("0 - Выход");
            System.out.print("Выберите: ");

            String input = scanner.nextLine();
            input = input.trim();
            ValidationResult result = validator.validate(input);
            if (!result.isValid()) {
                result.getErrors().forEach(System.out::println);
                continue;
            }

            option = Integer.parseInt(input);

            switch (option) {
                case 1 -> dao.save(scanner);
                case 2 -> dao.update(scanner);
                case 3 -> dao.delete(scanner);
                case 4 -> dao.watch();
                case 0 -> System.out.println("Выход");
            }

        } while (option != 0);

        scanner.close();
    }
}
