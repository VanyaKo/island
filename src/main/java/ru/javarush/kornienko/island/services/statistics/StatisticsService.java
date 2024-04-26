package ru.javarush.kornienko.island.services.statistics;

import org.jetbrains.annotations.NotNull;
import ru.javarush.kornienko.island.configs.PrototypeFactory;
import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.entities.abstracts.Animal;
import ru.javarush.kornienko.island.entities.abstracts.Organism;
import ru.javarush.kornienko.island.entities.plants.Plant;
import ru.javarush.kornienko.island.exceptions.AppException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StatisticsService {
    private final PrototypeFactory prototypeFactory;

    public StatisticsService(PrototypeFactory prototypeFactory) {
        this.prototypeFactory = prototypeFactory;
    }

    /**
     * Print overall or eat info dependent on input
     */
    public void printLongInfo(Map<Class<? extends Organism>, Long> organismClassesToCount, LongInfoType longInfoType) {
        boolean isOverallInfoType = LongInfoType.OVERALL_INFO == longInfoType;
        if(!isOverallInfoType && LongInfoType.EAT_INFO != longInfoType) {
            throw new AppException("No \"" + longInfoType + "\"" + " statistics type", new IllegalArgumentException());
        }

        Map<Class<? extends Organism>, Long> plantClassCount = filterBySuperclass(organismClassesToCount, Plant.class);
        Map<Class<? extends Organism>, Long> animalClassCount = filterBySuperclass(organismClassesToCount, Animal.class);
        long organismCount = getValueCount(organismClassesToCount.values());
        long plantCount = getValueCount(plantClassCount.values());
        long animalCount = getValueCount(animalClassCount.values());

        if(organismCount <= 0) {
            System.out.println((isOverallInfoType ? "На острове никого нет." : "Никого не съели.") + "\n");
            return;
        }

        System.out.printf("Всего %s %d организмов: %d растений и %d животных.%n",
                (isOverallInfoType ? "на острове" : "съедено"), organismCount, plantCount, animalCount);
        printOrganismsByClass(plantCount, isOverallInfoType, "Растения", "Съеденные растения", plantClassCount);
        printOrganismsByClass(animalCount, isOverallInfoType, "Животные", "Съеденные животные", animalClassCount);
        System.out.println();
    }

    private void printOrganismsByClass(long organismCount, boolean isOverallInfoType, String overallString,
                                       String eatString, Map<Class<? extends Organism>, Long> organismClassCount) {
        if(organismCount > 0) {
            System.out.print(Consts.LIST_PRINT_ITEM + (isOverallInfoType ? overallString : eatString) + ": ");
            printUnicodes(organismClassCount.entrySet());
        }
    }

    /**
     * Print reproduce, move, or die info dependent on input.
     */
    public void printShortInfo(Map<Class<? extends Organism>, Long> organismClassesToCount, ShortInfoType shortInfoType) {
        long animalCount = getValueCount(organismClassesToCount.values());
        if(animalCount <= 0) {
            System.out.println(shortInfoType.getNegativeInfo() + ".\n");
            return;
        }
        System.out.printf("Всего %s %d %s.%n", shortInfoType.getAction(), animalCount, shortInfoType.getOrganismType());
        System.out.print(Consts.LIST_PRINT_ITEM + shortInfoType.getDescriptive() + ": ");
        printUnicodes(organismClassesToCount.entrySet());
        System.out.println();
    }

    public void printDifferenceInfo(Map<Class<? extends Organism>, Long> initialClassesToCount, Map<Class<? extends Organism>, Long> currentClassesToCount) {
        long initialOrganismCount = getValueCount(initialClassesToCount.values());
        long currentOrganismCount = getValueCount(currentClassesToCount.values());
        printDifference("Всех организмов", initialOrganismCount, currentOrganismCount, ":");

        long initialPlantCount = getValueCount(filterBySuperclass(initialClassesToCount, Plant.class).values());
        long currentPlantCount = getValueCount(filterBySuperclass(currentClassesToCount, Plant.class).values());
        System.out.print(Consts.LIST_PRINT_ITEM);
        printDifference("Растений", initialPlantCount, currentPlantCount, "");

        long initialAnimalCount = getValueCount(filterBySuperclass(initialClassesToCount, Animal.class).values());
        long currentAnimalCount = getValueCount(filterBySuperclass(currentClassesToCount, Animal.class).values());
        System.out.print(Consts.LIST_PRINT_ITEM);
        printDifference("Животных", initialAnimalCount, currentAnimalCount, "");

        System.out.println();
    }

    private void printDifference(String object, long initialCount, long currentCount, String suffix) {
        long difference = initialCount - currentCount;
        if(difference == 0) {
            System.out.println("Количество " + object.toLowerCase() + " не изменилось.");
            return;
        }
        System.out.println(object + " стало " + (difference > 0 ? "меньше" : "больше") + " на " + Math.abs(difference) + suffix);
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
