package main.logic;

import java.io.IOException;
import java.util.List;


public interface Repository<T> {
    void outputList(List<T> list, String fileName) throws IOException;
    List<T> readList(String fileName) throws IOException, ClassNotFoundException;
}