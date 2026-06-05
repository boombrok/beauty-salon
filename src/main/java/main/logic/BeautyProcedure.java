package main.logic;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class BeautyProcedure implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int id;
    private final String name;
    private final String category;
    private final int duration;
    private final double price;
    private final int mastersCount;

    public BeautyProcedure(int id, String name, String category, int duration, double price, int mastersCount) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва процедури не може бути порожньою.");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Категорія процедури не може бути порожньою.");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Тривалість процедури повинна бути більшою за нуль.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Ціна не може бути від'ємною.");
        }
        if (mastersCount < 0) {
            throw new IllegalArgumentException("Кількість майстрів не може бути від'ємною.");
        }

        this.id = id;
        this.name = name;
        this.category = category;
        this.duration = duration;
        this.price = price;
        this.mastersCount = mastersCount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getDuration() {
        return duration;
    }

    public double getPrice() {
        return price;
    }

    public int getMastersCount() {
        return mastersCount;
    }

    @Override
    public String toString() {
        return String.format(
                """
                --- Процедура (ID: %d) ---
                  Назва             : %s
                  Категорія         : %s
                  Тривалість (хв)   : %d
                  Ціна (грн)        : %.2f
                  Кількість майстрів: %d
                -------------------------""",
                id, name, category, duration, price, mastersCount
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeautyProcedure that = (BeautyProcedure) o;
        return id == that.id &&
                duration == that.duration &&
                Double.compare(that.price, price) == 0 &&
                mastersCount == that.mastersCount &&
                Objects.equals(name, that.name) &&
                Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, duration, price, mastersCount);
    }
}