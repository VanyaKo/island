package ru.javarush.kornienko.island.services;

import org.jetbrains.annotations.NotNull;
import ru.javarush.kornienko.island.configs.PrototypeFactory;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.plants.Plant;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StatisticsService {
    private final PrototypeFactory prototypeFactory;

    public StatisticsService(PrototypeFactory prototypeFactory) {
        this.prototypeFactory = prototypeFactory;
    }

    public void printCurrentOrganismInfo(Map<Class<? extends Organism>, Long> organismClassesToCount) {
        Map<Class<? extends Organism>, Long> plantClassCount = filterBySuperclass(organismClassesToCount, Plant.class);
        Map<Class<? extends Organism>, Long> animalClassCount = filterBySuperclass(organismClassesToCount, Animal.class);

        long organismCount = getValueCount(organismClassesToCount.values());
        long plantCount = getValueCount(plantClassCount.values());
        long animalCount = getValueCount(animalClassCount.values());

        if(organismCount <= 0) {
            System.out.println("На острове никого нет.");
            return;
        }
        System.out.println("Всего на острове " + organismCount + " организмов: "
                           + plantCount + " растений и " + animalCount + " животных.");
        if(plantCount > 0) {
            System.out.print("Растения: ");
            printUnicodes(plantClassCount.entrySet());
        }
        if(animalCount > 0) {
            System.out.print("Животные: ");
            printUnicodes(animalClassCount.entrySet());
        }
        System.out.println();
    }

    public void printPlantsGrownInfo(long plantsGrown) {
        System.out.println("Выросло " + plantsGrown + " растений.\n");
    }

    public void printEatInfo(Map<Class<? extends Organism>, Long> organismClassesToCount) {
        Map<Class<? extends Organism>, Long> plantClassCount = filterBySuperclass(organismClassesToCount, Plant.class);
        Map<Class<? extends Organism>, Long> animalClassCount = filterBySuperclass(organismClassesToCount, Animal.class);

        long eatenOrganismCount = getValueCount(organismClassesToCount.values());
        long eatenPlantCount = getValueCount(plantClassCount.values());
        long eatenAnimalCount = getValueCount(animalClassCount.values());

        if(eatenOrganismCount <= 0) {
            System.out.println("Никого не съели.");
            return;
        }
        System.out.println("Всего съедено " + eatenOrganismCount + " организмов: "
                           + eatenPlantCount + " растений и " + eatenAnimalCount + " животных.");
        if(eatenPlantCount > 0) {
            System.out.print("Съеденные растения: ");
            printUnicodes(plantClassCount.entrySet());
        }
        if(eatenAnimalCount > 0) {
            System.out.print("Съеденные животные: ");
            printUnicodes(animalClassCount.entrySet());
        }
        System.out.println();
    }

    public void printReproduceInfo(Map<Class<? extends Organism>, Long> organismClassesToCount) {
        long newbornAnimalCount = getValueCount(organismClassesToCount.values());
        if(newbornAnimalCount <= 0) {
            System.out.println("Никто не размножился.");
            return;
        }
        System.out.println("Всего размножилось " + newbornAnimalCount + " животных.");
        System.out.print("Родившиеся животные: ");
        printUnicodes(organismClassesToCount.entrySet());
        System.out.println();
    }

    public void printMoveInfo(Map<Class<? extends Organism>, Long> organismClassesToCount) {
        long movedAnimalCount = getValueCount(organismClassesToCount.values());
        if(movedAnimalCount <= 0) {
            System.out.println("Никто никуда не перемещался.");
            return;
        }
        System.out.println("Всего переместилось " + movedAnimalCount + " животных.");
        System.out.print("Перемещенные животные: ");
        printUnicodes(organismClassesToCount.entrySet());
        System.out.println();
    }

    public void printDieInfo(Map<Class<? extends Organism>, Long> organismClassesToCount) {
        long diedAnimalCount = getValueCount(organismClassesToCount.values());
        if(diedAnimalCount <= 0) {
            System.out.println("Никто не умер от голода.\n");
            return;
        }
        System.out.println("Всего умерло от голода " + diedAnimalCount + " животных.");
        System.out.print("Умершие животные: ");
        printUnicodes(organismClassesToCount.entrySet());
        System.out.println();
    }

    public void printDifferenceInfo(Map<Class<? extends Organism>, Long> initialClassesToCount, Map<Class<? extends Organism>, Long> currentClassesToCount) {
        long initialOrganismCount = getValueCount(initialClassesToCount.values());
        long currentOrganismCount = getValueCount(currentClassesToCount.values());
        printDifference("Всех организмов", initialOrganismCount, currentOrganismCount, ":");
        long initialPlantCount = getValueCount(filterBySuperclass(initialClassesToCount, Plant.class).values());
        long currentPlantCount = getValueCount(filterBySuperclass(currentClassesToCount, Plant.class).values());
        printDifference("Растений", initialPlantCount, currentPlantCount, "");
        long initialAnimalCount = getValueCount(filterBySuperclass(initialClassesToCount, Animal.class).values());
        long currentAnimalCount = getValueCount(filterBySuperclass(currentClassesToCount, Animal.class).values());
        printDifference("Животных", initialAnimalCount, currentAnimalCount, "");
        System.out.println();
    }

    private void printDifference(String object, long initialCount, long currentCount, String suffix) {
        long difference = initialCount - currentCount;
        if(difference == 0) {
            System.out.println("Количество " + object.toLowerCase() + " не изменилось");
            return;
        }
        System.out.println(object + " стало " + (difference > 0 ? "меньше" : "больше") + " на " + difference + suffix);
    }

    private @NotNull <V> Map<Class<? extends Organism>, V> filterBySuperclass(Map<Class<? extends Organism>, V> classMap, Class<? extends Organism> clazz) {
        return classMap.entrySet().stream()
                .filter(entry -> clazz.isAssignableFrom(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void printUnicodes(Set<Map.Entry<Class<? extends Organism>, Long>> entries) {
        for(Map.Entry<Class<? extends Organism>, Long> classLongEntry : entries) {
            System.out.print(prototypeFactory.getUnicodeByClass(classLongEntry.getKey()) + "=" + classLongEntry.getValue() + ", ");
        }
        System.out.println();
    }

    private long getValueCount(Collection<Long> values) {
        return values.stream()
                .mapToLong(count -> count)
                .sum();
    }
}
