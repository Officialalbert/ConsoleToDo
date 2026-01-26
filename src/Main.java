import java.util.Scanner;
import Dao.DaoClass;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");
        System.out.println("что вы хотите сделать?");
        Scanner choice = new Scanner(System.in);
        switch (choice.nextInt()){
            case 1 :
                save();
                break;
            case 2:
                System.out.println("изменить даныне");
                break;
            case 3:
                System.out.println("Удалить данные");
                break;
            case 4:
                System.out.println("Просмотреть данные");
                break;
            default:
                System.out.println("введите от 1 до 4 число которое нужно вам");
        }
    }
}