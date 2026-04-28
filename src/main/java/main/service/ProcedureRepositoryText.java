package main.service;

import main.logic.BeautyProcedure;
import main.logic.ProcedureRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProcedureRepositoryText implements ProcedureRepository {

    @Override
    public void outputList(List<BeautyProcedure> list, String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (BeautyProcedure procedure : list) {
                if (procedure != null) {
                    writer.println(procedure.getId() + ";" +
                            procedure.getName() + ";" +
                            procedure.getCategory() + ";" +
                            procedure.getDuration() + ";" +
                            procedure.getPrice() + ";" +
                            procedure.getMastersCount());
                }
            }
        }
    }

    @Override
    public List<BeautyProcedure> readList(String fileName) throws IOException {
        List<BeautyProcedure> list = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String category = parts[2];
                    int duration = Integer.parseInt(parts[3]);
                    double price = Double.parseDouble(parts[4]);
                    int mastersCount = Integer.parseInt(parts[5]);
                    list.add(new BeautyProcedure(id, name, category, duration, price, mastersCount));
                }
            }
        }
        return list;
    }
}