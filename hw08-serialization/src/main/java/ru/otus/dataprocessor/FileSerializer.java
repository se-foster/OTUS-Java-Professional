package ru.otus.dataprocessor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;

public class FileSerializer implements Serializer {

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    /**
     * формирует результирующий json и сохраняет его в файл
     */
    @Override
    public void serialize(Map<String, Double> data) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(data);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(jsonString);
        } catch (IOException e) {
            throw new FileProcessException(e.getMessage());
        }
    }
}
