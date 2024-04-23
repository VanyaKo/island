package ru.javarush.kornienko.island.services.impls;

import org.jetbrains.annotations.NotNull;
import ru.javarush.kornienko.island.configs.PrototypeFactory;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.plants.Plant;
import ru.javarush.kornienko.island.services.StatisticsService;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StatisticsServiceImpl implements StatisticsService {
    private final PrototypeFactory prototypeFactory;

    public StatisticsServiceImpl(PrototypeFactory prototypeFactory) {
        this.prototypeFactory = prototypeFactory;
    }

    @Override
    public void printCurrentOrganismInfo() {

    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public void printDieInfo(Map<Class<? extends Organism>, Long> organismClassesToCount) {
        long diedAnimalCount = getValueCount(organismClassesToCount.values());
        if(diedAnimalCount <= 0) {
            System.out.println("Никто не умер от голода.");
            return;
        }
        System.out.println("Всего умерло от голода " + diedAnimalCount + " животных.");
        System.out.print("Умершие животные: ");
        printUnicodes(organismClassesToCount.entrySet());
        System.out.println();
    }

    private @NotNull Map<Class<? extends Organism>, Long> filterBySuperclass(Map<Class<? extends Organism>, Long> classMap, Class<? extends Organism> clazz) {
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
