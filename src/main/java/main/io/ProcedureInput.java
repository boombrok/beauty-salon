package main.io;

import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import main.logic.BeautyProcedure;
import java.util.Arrays;
import java.util.Optional;

public class ProcedureInput {

    public static BeautyProcedure readProcedure() {
        try {
            TextInputDialog idDlg = new TextInputDialog();
            idDlg.setTitle("Додавання процедури");
            idDlg.setHeaderText("Введіть ID:");
            Optional<String> idRes = idDlg.showAndWait();
            if (!idRes.isPresent()) return null;
            int id = Integer.parseInt(idRes.get().trim());

            TextInputDialog nameDlg = new TextInputDialog();
            nameDlg.setTitle("Додавання процедури");
            nameDlg.setHeaderText("Введіть назву:");
            Optional<String> nameRes = nameDlg.showAndWait();
            if (!nameRes.isPresent()) return null;
            String name = nameRes.get().trim();

            ChoiceDialog<String> catDlg = new ChoiceDialog<>("Перукарські послуги",
                    Arrays.asList("Перукарські послуги", "Нігтьовий сервіс", "Косметологія", "Візаж"));
            catDlg.setTitle("Додавання процедури");
            catDlg.setHeaderText("Оберіть категорію:");
            Optional<String> catRes = catDlg.showAndWait();
            if (!catRes.isPresent()) return null;
            String cat = catRes.get();

            TextInputDialog durDlg = new TextInputDialog();
            durDlg.setTitle("Додавання процедури");
            durDlg.setHeaderText("Введіть тривалість (хв):");
            Optional<String> durRes = durDlg.showAndWait();
            if (!durRes.isPresent()) return null;
            int dur = Integer.parseInt(durRes.get().trim());

            TextInputDialog prDlg = new TextInputDialog();
            prDlg.setTitle("Додавання процедури");
            prDlg.setHeaderText("Введіть ціну (грн):");
            Optional<String> prRes = prDlg.showAndWait();
            if (!prRes.isPresent()) return null;
            double pr = Double.parseDouble(prRes.get().trim());

            TextInputDialog mDlg = new TextInputDialog();
            mDlg.setTitle("Додавання процедури");
            mDlg.setHeaderText("Введіть кількість майстрів:");
            Optional<String> mRes = mDlg.showAndWait();
            if (!mRes.isPresent()) return null;
            int masters = Integer.parseInt(mRes.get().trim());

            return new BeautyProcedure(id, name, cat, dur, pr, masters);
        } catch (Exception e) {
            return null;
        }
    }
}