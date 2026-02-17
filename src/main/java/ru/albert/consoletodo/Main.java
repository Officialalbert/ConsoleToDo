package ru.albert.consoletodo;

import ru.albert.consoletodo.service.ToDoService;
import ru.albert.consoletodo.validation.ValidationClass;
import ru.albert.consoletodo.validation.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
@RequiredArgsConstructor
public class Main implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    private final ToDoService service;
    private final ValidationClass validator;//Валидация!
    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int option = -1;
        do {
            System.out.println("\n=== МЕНЮ ===");
            System.out.println("1 - Сохранить");
            System.out.println("2 - Изменить данные");
            System.out.println("3 - Удалить данные");
            System.out.println("4 - Просмотр всех");
            System.out.println("5 - Поиск одного по id");
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
                case 1 -> { // Сохранить новую запись
                    System.out.print("Введите строку для сохранения: ");
                    String value = scanner.nextLine();
                    service.save(value);
                }
                case 2 -> { // Изменить существующую запись по ID
                    System.out.println("Введите ID для обновления (или -1 для отмены):");
                    long id = scanner.nextLong();
                    scanner.nextLine();
                    System.out.println("Введите новую строку");
                    String newStr = scanner.nextLine();
                    if (id == -1) continue;
                    service.update(newStr, id);
                }
                case 3 -> { // Удалить запись по ID
                    System.out.println("Введите ID для удаления (или -1 для отмены):");
                    long id = scanner.nextLong();
                    scanner.nextLine();

                    if (id == -1) continue;
                    service.delete(id);
                }
                case 4 -> { // Просмотр всех записей с пагинацией
                    System.out.print("Введите номер страницы (начиная с 1): ");
                    int page = scanner.nextInt();

                    System.out.print("Введите размер страницы: ");
                    int size = scanner.nextInt();

                    service.findAll(page - 1, size).forEach(System.out::println);
                }
                case 5 -> { // Поиск одной записи по ID
                    System.out.println("Введите ID для поиска (или -1 для отмены):");
                    long id = scanner.nextLong();
                    scanner.nextLine();
                    if (id==-1) continue;
                    try {
                        System.out.println(service.findById(id));
                    } catch (Exception e) {
                        System.out.println("Запись не найдена");
                    }
                }
                case 0 -> System.out.println("Выход");
            }
        } while (option != 0) ;

            scanner.close();

    }
}