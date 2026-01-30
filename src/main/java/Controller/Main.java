package Controller;

import Dao.DaoClass;
import Errors.WrongException;
import Validation.ValidationClass;
import Validation.ValidationResult;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws WrongException {
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
            System.out.println("5 - Поиск одного по id");
            System.out.println("6 - для ошибки");
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
                case 1 -> {
                    System.out.print("Что сохранить: ");
                    String value = scanner.nextLine();
                    dao.save(value);
                }
                case 2 -> {
                    System.out.println("Введите ID для обновления (или -1 для отмены):");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    String newStr = scanner.nextLine();
                    if (id == -1) return;
                    dao.update(newStr,id);
                }
                case 3 -> {
                    System.out.println("Введите ID для удаления (или -1 для отмены):");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    if (id == -1) return;
                    dao.delete(id);
                }
                case 4 -> dao.findAll();
                case 5 -> dao.exception();
                case 6 -> {
                    System.out.println("Введите ID для поиска (или -1 для отмены):");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    dao.findById(id);
                }

                case 0 -> System.out.println("Выход");
            }

        } while (option != 0);

        scanner.close();
    }
}
