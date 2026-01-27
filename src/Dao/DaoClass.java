package Dao;

import Errors.WrongException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DaoClass {
    private final ConcurrentHashMap<Integer, String> hashMapen = new ConcurrentHashMap<>();
    private final AtomicInteger idGen = new AtomicInteger(0);
    private static final Logger logger = LoggerFactory.getLogger(DaoClass.class);

    private static DaoClass INSTANCE;

    private DaoClass() {
    }

    public static DaoClass getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DaoClass();
        }
        return INSTANCE;
    }

    // В DAOClass
    public void update(Scanner scanner) {
        System.out.println("Введите ID для обновления (или -1 для отмены):");
        int id = scanner.nextInt();
        scanner.nextLine();  // Съедаем \n

        if (id == -1) {
            System.out.println("Отмена обновления");
            return;
        }

        String oldValue = hashMapen.get(id);
        if (oldValue == null) {
            System.out.println("ID " + id + " не найден!");
            return;
        }

        System.out.println("Текущее значение: " + oldValue);
        System.out.print("Новое значение: ");
        String newValue = scanner.nextLine();

        if (hashMapen.replace(id, newValue) != null) {
            System.out.println("Обновлено!");
            logger.info("Информация обновлена!");
        }
    }

    public void delete(Scanner scanner) {
        System.out.println("Введите ID для удаления (или -1 для отмены):");
        int id = scanner.nextInt();
        scanner.nextLine();

        if (id == -1) return;

        String removed = hashMapen.remove(id);
        System.out.println(removed != null ? "Удалено ID " + id : "ID не найден");
        logger.info("Id удален или ненайден");
    }

    public void save(Scanner scanner) {
        System.out.print("Что сохранить: ");
        int id = idGen.getAndIncrement();
        String value = scanner.nextLine();
        hashMapen.put(id, value);
        System.out.println("Сохранено ID " + id);
        logger.info("Сохранен новый Id");
    }

    public void watch() {
        if (hashMapen.isEmpty()) {
            System.out.println("HashMap пустая");
            return;
        }

        hashMapen.forEach((id, value) ->
                System.out.println("ID: " + id + ", Значение: " + value)
        );
    }

    public void exception() { int a = 3; if (a == 3) { try { throw new WrongException("Exception"); } catch (WrongException e) { e.printStackTrace(); } } }

}
