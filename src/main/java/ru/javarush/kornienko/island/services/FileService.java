package ru.javarush.kornienko.island.services;

import ru.javarush.kornienko.island.exceptions.AppException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileService {
    public Path getPath(String inputPath) {
        return Path.of(inputPath);
    }

    public void writeTo(Path file, List<String> text) {
        try {
            Files.write(file, text);
        } catch(IOException e) {
            throw new AppException("Cannot write data to \"" + file.getFileName() + "\" file", e);
        }
    }

    public void createPath(String inputPath, boolean isFile) {
        try {
            Path path = Path.of(inputPath);
            if(!Files.exists(path)) {
                if(isFile) {
                    Files.createFile(path);
                } else {
                    Files.createDirectories(path);
                }
            }
        } catch(IOException e) {
            throw new AppException(e.getMessage());
        }
    }
}

