package Service;

import Dao.DaoHibernate;
import Errors.WrongException;
import Validation.ValidationClass;
import Validation.ValidationResult;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws WrongException {
        DaoHibernate dao = DaoHibernate.getInstance();
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
                    System.out.print("Введите строку для сохранения: ");
                    String value = scanner.nextLine();
                    dao.save(value);
                }
                case 2 -> {
                    System.out.println("Введите ID для обновления (или -1 для отмены):");
                    long id = scanner.nextLong();
                    scanner.nextLine();
                    System.out.println("Введите новую строку");
                    String newStr = scanner.nextLine();
                    if (id == -1) continue;
                    dao.update(newStr,id);
                }
                case 3 -> {
                    System.out.println("Введите ID для удаления (или -1 для отмены):");
                    long id = scanner.nextLong();
                    scanner.nextLine();

                    if (id == -1) continue;
                    dao.delete(id);
                }
                case 4 -> {
                    System.out.print("Введите номер страницы (начиная с 1): ");
                    int page = scanner.nextInt();

                    System.out.print("Введите размер страницы: ");
                    int size = scanner.nextInt();

                    dao.findAll(page - 1,size).forEach(System.out::println);
                }
                case 5 -> {
                    System.out.println("Введите ID для поиска (или -1 для отмены):");
                    long id = scanner.nextLong();
                    scanner.nextLine();
                    dao.findById(id)
                            .ifPresentOrElse(
                                    System.out::println,
                                    () -> System.out.println("Запись не найдена")
                            );
                }
                case 6 -> dao.exception();
                case 0 -> System.out.println("Выход");
            }

        } while (option != 0);

        scanner.close();


    }
}
