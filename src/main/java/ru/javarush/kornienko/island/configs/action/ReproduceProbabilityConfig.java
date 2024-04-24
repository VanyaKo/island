package ru.javarush.kornienko.island.configs.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.kornienko.island.exceptions.AppException;
import ru.javarush.kornienko.island.models.abstracts.Organism;

import java.io.IOException;
import java.net.URL;

public class ReproduceProbabilityConfig {
    private final ObjectMapper objectMapper;
    private final String pathToJson;

    public ReproduceProbabilityConfig(ObjectMapper objectMapper, String pathToJson) {
        this.objectMapper = objectMapper;
        this.pathToJson = pathToJson;
    }

    public ReproduceProbabilityEntry[] readReproduceProbability() {
        URL resource = Organism.class.getClassLoader().getResource(pathToJson);
        try {
            StringReproduceProbability[] stringReproduceProbabilities = objectMapper.readValue(resource, StringReproduceProbability[].class);
            return convertToReproduceProbabilityEntries(stringReproduceProbabilities);
        } catch(IOException e) {
            throw new AppException("Error with reading of reproduce probability json", e);
        }
    }

    private ReproduceProbabilityEntry[] convertToReproduceProbabilityEntries(StringReproduceProbability[] stringReproduceProbabilities) {
        ReproduceProbabilityEntry[] reproduceProbabilityEntries = new ReproduceProbabilityEntry[stringReproduceProbabilities.length];
        for(int i = 0; i < stringReproduceProbabilities.length; i++) {
            String reproducer = stringReproduceProbabilities[i].reproducer;
            int couplingProbability = stringReproduceProbabilities[i].couplingProbability;
            int birthProbability = stringReproduceProbabilities[i].birthProbability;
            try {
                ReproduceProbabilityEntry reproduceProbabilityEntry = new ReproduceProbabilityEntry(
                        Class.forName(reproducer), couplingProbability, birthProbability
                );
                reproduceProbabilityEntries[i] = reproduceProbabilityEntry;
            } catch(ClassNotFoundException e) {
                throw new AppException("Cannot create class for name " + reproducer, e);
            }
        }
        return reproduceProbabilityEntries;
    }


    private static class StringReproduceProbability {
        private String reproducer;
        private int couplingProbability;
        private int birthProbability;

        public StringReproduceProbability() {
        }

        public void setReproducer(String reproducer) {
            this.reproducer = reproducer;
        }

        public void setCouplingProbability(int couplingProbability) {
            this.couplingProbability = couplingProbability;
        }

        public void setBirthProbability(int birthProbability) {
            this.birthProbability = birthProbability;
        }
    }
}
