package main.service;

import main.logic.BeautyProcedure;
import main.logic.ProcedureRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProcedureRepositoryBinary implements ProcedureRepository {

    @Override
    public void outputList(List<BeautyProcedure> list, String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(list);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BeautyProcedure> readList(String fileName) throws IOException, ClassNotFoundException {
        File file = new File(fileName);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<BeautyProcedure>) ois.readObject();
        }
    }
}