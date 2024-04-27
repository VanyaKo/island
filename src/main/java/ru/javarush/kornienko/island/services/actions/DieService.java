package ru.javarush.kornienko.island.services.actions;

import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.entities.abstracts.Animal;
import ru.javarush.kornienko.island.entities.abstracts.Organism;
import ru.javarush.kornienko.island.entities.island.Cell;
import ru.javarush.kornienko.island.entities.island.Island;
import ru.javarush.kornienko.island.services.utils.MapWorker;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DieService extends ActionService {
    private final Island island;

    public DieService(Island island) {
        this.island = island;
    }

    public synchronized void killCellHungryAnimals(Map.Entry<Cell, Set<Organism>> cellToOrganismsEntry) {
        synchronized(island) {
            Set<Organism> killedOrganisms = new HashSet<>();
            for(Organism organism : cellToOrganismsEntry.getValue()) {
                if(organism instanceof Animal animal && hasNoHealth(animal)) {
                    MapWorker.putDuplicateValueToCountEntry(organismClassCountMap, animal.getClass());
                    killedOrganisms.add(animal);
                }
            }
            killedOrganisms.forEach(animal -> island.removeOrganismFromCell(animal, cellToOrganismsEntry.getKey()));
        }
    }

    private boolean hasNoHealth(Animal animal) {
        return animal.decreaseHealth(Consts.PERCENT_TO_DECREASE_FROM_ANIMAL_STARVATION) <= 0;
    }
}
