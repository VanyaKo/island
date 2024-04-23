package ru.javarush.kornienko.island.services.impls;

import ru.javarush.kornienko.island.configs.PrototypeFactory;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.plants.Plant;
import ru.javarush.kornienko.island.services.StatisticsService;

import java.util.HashMap;
import java.util.Map;

public class StatisticsServiceImpl implements StatisticsService {
    public void printEatInfo(PrototypeFactory prototypeFactory, Map<Class<? extends Organism>, Long> eatenOrganismClassCount) {
        long eatenAnimalCount = 0;
        long eatenPlantCount = 0;
        Map<Class<? extends Organism>, Long> classCount = new HashMap<>();
        for(Map.Entry<Class<? extends Organism>, Long> classCountEntry : eatenOrganismClassCount.entrySet()) {
            Class<? extends Organism> clazz = classCountEntry.getKey();
            Organism prototype = prototypeFactory.getPrototype(clazz);
            if(prototype instanceof Animal) {
                eatenAnimalCount++;
            } else if(prototype instanceof Plant) {
                eatenPlantCount++;
            }
            classCount.putIfAbsent(clazz, 0L);
            classCount.put(clazz, classCount.get(clazz) + 1);
        }
        System.out.println("Всего съедено " + (eatenPlantCount + eatenAnimalCount) + " организмов:");
        System.out.println(eatenPlantCount + " растений");
        System.out.println(eatenAnimalCount + " животных");
        System.out.println("Подробная статистика организмов:");
        for(Map.Entry<Class<? extends Organism>, Long> classLongEntry : classCount.entrySet()) {
            System.out.print(prototypeFactory.getUnicodeByClass(classLongEntry.getKey()) + " = " + classLongEntry.getValue() + ", ");
        }
        System.out.println();
    }
}
