package Dao;

import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DaoClass {
    private final ConcurrentHashMap<AtomicInteger, String> hashMap = new ConcurrentHashMap<AtomicInteger, String>();
    AtomicInteger id = new AtomicInteger();

    public void save(){
        System.out.println("Напишите что сохранить");
        Scanner choice = new Scanner(System.in);
        id.getAndIncrement();
        hashMap.put(id, String.valueOf(choice));
    }
}
