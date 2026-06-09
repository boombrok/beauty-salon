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
        list.forEach(System.out::println);
    }

    public static String printHighestPricedByCategory(Map<String, BeautyProcedure> map) {
        if (map == null || map.isEmpty()) {
            return "Немає даних для виведення найдорожчих процедур.";
        }
        StringBuilder sb = new StringBuilder("НАЙДОРОЖЧІ ПРОЦЕДУРИ ЗА КАТЕГОРІЯМИ\n\n");
        map.forEach((category, procedure) -> {
            sb.append("Категорія: ").append(category).append("\n");
            if (procedure != null) {
                sb.append("  Назва: ").append(procedure.getName())
                        .append(" | Ціна: ").append(String.format("%.2f", procedure.getPrice())).append(" грн\n\n");
            }
        });
        return sb.toString();
    }

    public static String printGroupedByCategorySortedByMasters(Map<String, List<BeautyProcedure>> map) {
        if (map == null || map.isEmpty()) {
            return "Немає даних для виведення групованих процедур.";
        }
        StringBuilder sb = new StringBuilder("ГРУПУВАННЯ ЗА КАТЕГОРІЯМИ (СОРТУВАННЯ ЗА МАЙСТРАМИ)\n\n");
        map.forEach((category, list) -> {
            sb.append("[").append(category).append("]\n");
            list.forEach(p -> sb.append("  -> ").append(p.getName())
                    .append(" (Майстрів: ").append(p.getMastersCount()).append(")\n"));
            sb.append("\n");
        });
        return sb.toString();
    }
}