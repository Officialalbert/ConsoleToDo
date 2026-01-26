package Dao;

import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DaoClass {
    private final ConcurrentHashMap<Integer, String> hashMapen = new ConcurrentHashMap<>();
    private final AtomicInteger idGen = new AtomicInteger(0);

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
            System.out.println("✅ Обновлено!");
        }
    }

    public void delete(Scanner scanner) {
        System.out.println("Введите ID для удаления (или -1 для отмены):");
        int id = scanner.nextInt();
        scanner.nextLine();

        if (id == -1) return;

        String removed = hashMapen.remove(id);
        System.out.println(removed != null ? "✅ Удалено ID " + id : "❌ ID не найден");
    }

    public void save(Scanner scanner) {
        System.out.print("Что сохранить: ");
        int id = idGen.getAndIncrement();
        String value = scanner.nextLine();
        hashMapen.put(id, value);
        System.out.println("✅ Сохранено ID " + id);
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


}
