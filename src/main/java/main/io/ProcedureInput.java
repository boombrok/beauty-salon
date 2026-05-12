package main.io;

import main.logic.BeautyProcedure;
import java.util.Scanner;

public class ProcedureInput {

    private static final Scanner scanner = new Scanner(System.in);

    public static BeautyProcedure readProcedure() {
        try {
            System.out.print("Введіть ID процедури: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Введіть назву процедури: ");
            String name = scanner.nextLine();

            System.out.print("Введіть категорію: ");
            String category = scanner.nextLine();

            System.out.print("Введіть тривалість (хв): ");
            int duration = Integer.parseInt(scanner.nextLine());

            System.out.print("Введіть ціну (грн): ");
            double price = Double.parseDouble(scanner.nextLine());

            System.out.print("Введіть кількість майстрів: ");
            int mastersCount = Integer.parseInt(scanner.nextLine());

            return new BeautyProcedure(id, name, category, duration, price, mastersCount);
        } catch (NumberFormatException e) {
            System.out.println("Помилка введення числового значення. Спробуйте знову.");
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка валідації: " + e.getMessage());
            return null;
        }
    }
}