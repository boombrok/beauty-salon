package main.factory;

import main.logic.BeautyProcedure;
import java.util.ArrayList;
import java.util.List;

public class ProcedureFactory {

    public static List<BeautyProcedure> createDemoProcedures() {
        List<BeautyProcedure> list = new ArrayList<>();
        list.add(new BeautyProcedure(1, "Жіноча стрижка", "Перукарські послуги", 45, 450.0, 4));
        list.add(new BeautyProcedure(2, "Чоловіча стрижка", "Перукарські послуги", 30, 300.0, 3));
        list.add(new BeautyProcedure(3, "Фарбування волосся", "Перукарські послуги", 120, 1500.0, 2));
        list.add(new BeautyProcedure(4, "Манікюр класичний", "Нігтьовий сервіс", 60, 350.0, 5));
        list.add(new BeautyProcedure(5, "Педикюр", "Нігтьовий сервіс", 90, 600.0, 2));
        list.add(new BeautyProcedure(6, "Чистка обличчя", "Косметологія", 80, 800.0, 2));
        list.add(new BeautyProcedure(7, "Масаж обличчя", "Косметологія", 40, 400.0, 1));
        list.add(new BeautyProcedure(8, "Вечірній макіяж", "Візаж", 60, 700.0, 3));
        list.add(new BeautyProcedure(9, "Корекція брів", "Візаж", 25, 250.0, 6));
        list.add(new BeautyProcedure(10, "Ламінування вій", "Візаж", 75, 550.0, 2));
        return list;
    }
}