package main.service;

import main.logic.BeautyProcedure;
import main.logic.ProcedureRepository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ProcedureService {

    private final ProcedureRepository textRepository;
    private final ProcedureRepository binaryRepository;

    public ProcedureService() {
        this.textRepository = new ProcedureRepositoryText();
        this.binaryRepository = new ProcedureRepositoryBinary();
    }

    public void outputListTxt(List<BeautyProcedure> procedures, String fileName) {
        try {
            textRepository.outputList(procedures, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void outputListBin(List<BeautyProcedure> procedures, String fileName) {
        try {
            binaryRepository.outputList(procedures, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public List<BeautyProcedure> readListTxt(String fileName) {
        try {
            return textRepository.readList(fileName);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public List<BeautyProcedure> readListBin(String fileName) {
        try {
            return binaryRepository.readList(fileName);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public List<BeautyProcedure> findProcedureById(List<BeautyProcedure> procedures, int id) {
        if (procedures == null) return new ArrayList<>();
        return procedures.stream()
                .filter(p -> p != null && p.getId() == id)
                .toList();
    }

    public Optional<BeautyProcedure> findProcedureByName(List<BeautyProcedure> procedures, String name) {
        if (procedures == null || name == null || name.trim().isEmpty()) return Optional.empty();
        String lowerCaseName = name.trim().toLowerCase();
        return procedures.stream()
                .filter(p -> p != null && p.getName() != null && p.getName().toLowerCase().contains(lowerCaseName))
                .findFirst();
    }

    public List<BeautyProcedure> getProceduresByCategory(List<BeautyProcedure> procedures, String category) {
        if (procedures == null || category == null || category.trim().isEmpty()) return new ArrayList<>();
        String lowerCaseCategory = category.trim().toLowerCase();
        return procedures.stream()
                .filter(p -> p != null && p.getCategory() != null && p.getCategory().toLowerCase().equals(lowerCaseCategory))
                .toList();
    }

    public List<BeautyProcedure> getProceduresByDuration(List<BeautyProcedure> procedures, int duration) {
        if (procedures == null) return new ArrayList<>();
        return procedures.stream()
                .filter(p -> p != null && p.getDuration() == duration)
                .toList();
    }

    public List<BeautyProcedure> sortProceduresByPrice(List<BeautyProcedure> procedures, boolean ascending) {
        if (procedures == null) return new ArrayList<>();
        List<BeautyProcedure> sortedList = new ArrayList<>(procedures);
        if (ascending) {
            sortedList.sort(Comparator.comparingDouble(BeautyProcedure::getPrice));
        } else {
            sortedList.sort(Comparator.comparingDouble(BeautyProcedure::getPrice).reversed());
        }
        return sortedList;
    }

    public Map<String, BeautyProcedure> getHighestPricedProcedurePerCategory(List<BeautyProcedure> procedures) {
        if (procedures == null || procedures.isEmpty()) return new TreeMap<>();
        return procedures.stream()
                .filter(p -> p != null && p.getCategory() != null && !p.getCategory().trim().isEmpty())
                .collect(Collectors.toMap(
                        BeautyProcedure::getCategory,
                        p -> p,
                        (p1, p2) -> p1.getPrice() >= p2.getPrice() ? p1 : p2,
                        TreeMap::new
                ));
    }

    public Map<String, List<BeautyProcedure>> getProceduresGroupedByCategorySortedByMasters(List<BeautyProcedure> procedures, boolean ascendingSort) {
        if (procedures == null || procedures.isEmpty()) return new TreeMap<>();
        List<BeautyProcedure> filtered = procedures.stream()
                .filter(p -> p != null && p.getCategory() != null)
                .toList();
        Map<String, List<BeautyProcedure>> grouped = filtered.stream()
                .collect(Collectors.groupingBy(BeautyProcedure::getCategory, TreeMap::new, Collectors.toList()));
        Comparator<BeautyProcedure> mastersComparator = Comparator.comparingInt(BeautyProcedure::getMastersCount);
        if (!ascendingSort) {
            mastersComparator = mastersComparator.reversed();
        }
        final Comparator<BeautyProcedure> finalComparator = mastersComparator;
        grouped.forEach((category, list) -> list.sort(finalComparator));
        return grouped;
    }
}