package ru.javarush.kornienko.island;

import ru.javarush.kornienko.island.controllers.IslandController;

import java.util.Scanner;

public class ConsoleRunner {

    public static void main(String[] args) {
        System.out.println("Выберите одно из следующих условий остановки симуляции:");
        System.out.println("1. Остались только хищники");
        System.out.println("2. Закончились все растения");
        System.out.println("3. Все животные умерли");
        System.out.print("Введите соответствующую цифру: ");
        Scanner console = new Scanner(System.in);
        byte inputNumber = 0;
        try {
            inputNumber = console.nextByte();
            if(inputNumber < 1 || inputNumber > 3) {
                throw new IllegalArgumentException();
            }
        } catch(Exception e) {
            System.err.println("Чел... тебе нужно было просто ввести цифру. Мне лень обрабатывать исключения, поэтому запусти игру снова. Пока!");
            System.exit(-1);
        }
        String condition = "";
        if(inputNumber == 1) {
            condition = "ONLY_PREDATORS";
        } else if(inputNumber == 2) {
            condition = "NO_PLANTS";
        } else {
            condition = "ANIMALS_DIED";
        }
        System.out.println("Запуск игры...\n\n");
        IslandController islandController = new IslandController();
        islandController.run(condition);
    }
}