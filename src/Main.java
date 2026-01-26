import Dao.DaoClass;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DaoClass dao = new DaoClass();
        Scanner choice = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n=== МЕНЮ ===");
            System.out.println("1 - Сохранить");
            System.out.println("2 - Изменить данные");
            System.out.println("3 - Удалить данные");
            System.out.println("4 - Просмотр всех");
            System.out.println("0 - Выход");
            System.out.print("Выберите: ");

            option = choice.nextInt();  // Читаем внутри цикла
            choice.nextLine();
            switch (option) {
                case 1: dao.save(choice); break;
                case 2: dao.update(choice); break;
                case 3: dao.delete(choice); break;
                case 4: dao.watch(); break;
                case 0: System.out.println("Выход"); break;
                default: System.out.println("0-4!");
            }
        } while (option != 0);  // Цикл до 0

        choice.close();
    }
}
