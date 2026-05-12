package main.io;

import main.logic.BeautyProcedure;
import java.util.List;
import java.util.Map;

public class ProcedurePrinter {

    public static void printList(List<BeautyProcedure> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("Список процедур порожній.");
            return;
        }
        list.forEach(System.out.println);
    }

    public static void printHighestPricedByCategory(Map<String, BeautyProcedure> map) {
        if (map == null || map.isEmpty()) {
            System.out.println("Немає даних для виведення найдорожчих процедур.");
            return;
        }
        System.out.println("=== НАЙДОРОЖЧІ ПРОЦЕДУРИ ЗА КАТЕГОРІЯМИ ===");
        map.forEach((category, procedure) -> {
            System.out.println("Категорія: " + category);
            if (procedure != null) {
                System.out.println("  Назва: " + procedure.getName() + " | Ціна: " + procedure.getPrice() + " грн");
            }
        });
    }

    public static void printGroupedByCategorySortedByMasters(Map<String, List<BeautyProcedure>> map) {
        if (map == null || map.isEmpty()) {
            System.out.println("Немає даних для виведення групованих процедур.");
            return;
        }
        System.out.println("=== ГРУПУВАННЯ ЗА КАТЕГОРІЯМИ (СОРТУВАННЯ ЗА МАЙСТРАМИ) ===");
        map.forEach((category, list) -> {
            System.out.println("Категорія: " + category);
            list.forEach(p -> System.out.println("  -> " + p.getName() + " (Майстрів: " + p.getMastersCount() + ")"));
        });
    }
}