package ru.javarush.kornienko.island;

import org.jetbrains.annotations.NotNull;
import ru.javarush.kornienko.island.controllers.IslandController;

import java.util.Scanner;

public class ConsoleRunner {

    public static void main(String[] args) {
        showStartDialog();
        Scanner console = new Scanner(System.in);
        byte inputNumber = getInputNumber(console);
        String stopGameCondition = getStopGameCondition(inputNumber);
        System.out.println("Запуск игры...\n\n");

        IslandController islandController = new IslandController();
        islandController.run(stopGameCondition);
    }

    private static @NotNull String getStopGameCondition(byte inputNumber) {
        String stopGameCondition = "";
        if(inputNumber == 1) {
            stopGameCondition = "ONLY_PREDATORS";
        } else if(inputNumber == 2) {
            stopGameCondition = "NO_PLANTS";
        } else {
            stopGameCondition = "ANIMALS_DIED";
        }
        return stopGameCondition;
    }

    private static byte getInputNumber(Scanner console) {
        byte inputNumber = 0;
        try {
            inputNumber = console.nextByte();
            if(inputNumber < 1 || inputNumber > 3) {
                throw new IllegalArgumentException();
            }
        } catch(Exception e) {
            System.err.println("\nЧел... тебе нужно было просто ввести цифру...");
            System.err.println("Короче, я кое-как написал этот проект и в благородство играть не буду: запустишь игру снова - и мы в расчете.");
            System.exit(-1);
        }
        return inputNumber;
    }

    private static void showStartDialog() {
        System.out.println("Выберите одно из следующих условий остановки симуляции:");
        System.out.println("1. Остались только хищники");
        System.out.println("2. Закончились все растения");
        System.out.println("3. Все животные умерли");
        System.out.print("Введите соответствующую цифру: ");
    }
}