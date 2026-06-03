package main.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import main.factory.ProcedureFactory;
import main.logic.BeautyProcedure;
import main.service.ProcedureService;

import java.util.*;

public class ProcedureController {

    private final ProcedureService procedureService = new ProcedureService();
    private final List<BeautyProcedure> masterProcedureList = new ArrayList<>();
    private final ObservableList<BeautyProcedure> procedureData = FXCollections.observableArrayList();

    @FXML private TableView<BeautyProcedure> procedureTable;
    @FXML private TableColumn<BeautyProcedure, Integer> idColumn;
    @FXML private TableColumn<BeautyProcedure, String> nameColumn;
    @FXML private TableColumn<BeautyProcedure, String> categoryColumn;
    @FXML private TableColumn<BeautyProcedure, Integer> durationColumn;
    @FXML private TableColumn<BeautyProcedure, Double> priceColumn;
    @FXML private TableColumn<BeautyProcedure, Integer> mastersCountColumn;
    @FXML private ChoiceBox<String> operationsChoiceBox;
    @FXML private BorderPane mainAppPane;
    @FXML private AnchorPane welcomePane;

    @FXML private Label lblTotalProcedures;
    @FXML private Label lblAvgPrice;
    @FXML private Label lblTotalMasters;

    @FXML
    public void initialize() {
        mainAppPane.setVisible(false);
        mainAppPane.setManaged(false);
        welcomePane.setVisible(true);
        welcomePane.setManaged(true);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        mastersCountColumn.setCellValueFactory(new PropertyValueFactory<>("mastersCount"));

        priceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.2f", item));
            }
        });

        procedureTable.setItems(procedureData);
        procedureTable.setPlaceholder(new Label("Натисніть 'Почати' для завантаження даних."));

        fillOperationsChoiceBox();
    }

    private void recalculateDashboard() {
        long totalCount = masterProcedureList.size();
        double avgPrice = masterProcedureList.stream()
                .mapToDouble(BeautyProcedure::getPrice)
                .average()
                .orElse(0.0);
        int totalMasters = masterProcedureList.stream()
                .mapToInt(BeautyProcedure::getMastersCount)
                .sum();

        lblTotalProcedures.setText("Всього процедур: " + totalCount);
        lblAvgPrice.setText(String.format("Середня ціна: %.2f грн", avgPrice));
        lblTotalMasters.setText("Всього майстрів: " + totalMasters);
    }

    @FXML
    void handleStartApplication(ActionEvent event) {
        welcomePane.setVisible(false);
        welcomePane.setManaged(false);
        mainAppPane.setVisible(true);
        mainAppPane.setManaged(true);

        masterProcedureList.clear();
        masterProcedureList.addAll(ProcedureFactory.createDemoProcedures());
        procedureData.setAll(masterProcedureList);

        recalculateDashboard();
        showAlert(Alert.AlertType.INFORMATION, "Початкові дані", "Завантажено " + masterProcedureList.size() + " процедур за замовчуванням.");
    }

    @FXML
    void handleExitSimple(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void handleShowAll(ActionEvent event) {
        if (!mainAppPane.isVisible()) return;
        procedureData.setAll(masterProcedureList);
    }

    @FXML
    void handleClose(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Повернутися на стартовий екран?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            mainAppPane.setVisible(false);
            mainAppPane.setManaged(false);
            welcomePane.setVisible(true);
            welcomePane.setManaged(true);
            masterProcedureList.clear();
            procedureData.clear();
        }
    }

    @FXML
    void handleSaveAs(ActionEvent event) {
        if (masterProcedureList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Попередження", "Немає даних для збереження.");
            return;
        }
        TextInputDialog dialog = new TextInputDialog("procedures.txt");
        dialog.setTitle("Зберегти як...");
        dialog.setHeaderText("Введіть назву файлу (.txt або .bin):");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().trim().isEmpty()) {
            String fileName = result.get().trim();
            try {
                if (fileName.endsWith(".txt")) {
                    procedureService.outputListTxt(new ArrayList<>(masterProcedureList), fileName);
                    showAlert(Alert.AlertType.INFORMATION, "Успіх", "Дані збережено в текстовий файл.");
                } else if (fileName.endsWith(".bin")) {
                    procedureService.outputListBin(new ArrayList<>(masterProcedureList), fileName);
                    showAlert(Alert.AlertType.INFORMATION, "Успіх", "Дані збережено в бінарний файл.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Помилка", "Непідтримуваний формат файлу.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Не вдалося зберегти: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleReadFrom(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("procedures.txt");
        dialog.setTitle("Зчитати з...");
        dialog.setHeaderText("Введіть назву файлу (.txt або .bin):");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().trim().isEmpty()) {
            String fileName = result.get().trim();
            try {
                List<BeautyProcedure> loaded = null;
                if (fileName.endsWith(".txt")) {
                    loaded = procedureService.readListTxt(fileName);
                } else if (fileName.endsWith(".bin")) {
                    loaded = procedureService.readListBin(fileName);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Помилка", "Непідтримуваний формат.");
                    return;
                }
                if (loaded != null) {
                    masterProcedureList.clear();
                    masterProcedureList.addAll(loaded);
                    procedureData.setAll(masterProcedureList);
                    recalculateDashboard();
                    showAlert(Alert.AlertType.INFORMATION, "Успіх", "Завантажено елементів: " + loaded.size());
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Помилка читання: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleDeleteProcedure(ActionEvent event) {
        BeautyProcedure selected = procedureTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Попередження", "Оберіть процедуру для видалення.");
            return;
        }
        masterProcedureList.remove(selected);
        procedureData.setAll(masterProcedureList);
        recalculateDashboard();
        showAlert(Alert.AlertType.INFORMATION, "Успіх", "Процедуру видалено.");
    }

    @FXML
    void handleUpdateMasters(ActionEvent event) {
        BeautyProcedure selected = procedureTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Попередження", "Оберіть процедуру.");
            return;
        }
        TextInputDialog dialog = new TextInputDialog(String.valueOf(selected.getMastersCount()));
        dialog.setTitle("Оновлення кількості майстрів");
        dialog.setHeaderText("Введіть нову кількість майстрів:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int count = Integer.parseInt(result.get().trim());
                BeautyProcedure updated = new BeautyProcedure(selected.getId(), selected.getName(), selected.getCategory(), selected.getDuration(), selected.getPrice(), count);
                int index = masterProcedureList.indexOf(selected);
                if (index != -1) {
                    masterProcedureList.set(index, updated);
                    procedureData.setAll(masterProcedureList);
                    recalculateDashboard();
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Некоректні дані.");
            }
        }
    }

    @FXML
    void handleAddProcedure(ActionEvent event) {
        try {
            TextInputDialog idDlg = new TextInputDialog(); idDlg.setHeaderText("Введіть ID:"); Optional<String> idRes = idDlg.showAndWait();
            if (!idRes.isPresent()) return; int id = Integer.parseInt(idRes.get().trim());
            if (masterProcedureList.stream().anyMatch(p -> p.getId() == id)) { showAlert(Alert.AlertType.ERROR, "Помилка", "ID вже існує."); return; }

            TextInputDialog nameDlg = new TextInputDialog(); nameDlg.setHeaderText("Введіть назву:"); Optional<String> nameRes = nameDlg.showAndWait();
            if (!nameRes.isPresent()) return; String name = nameRes.get().trim();

            ChoiceDialog<String> catDlg = new ChoiceDialog<>("Перукарські послуги", Arrays.asList("Перукарські послуги", "Нігтьовий сервіс", "Косметологія", "Візаж"));
            catDlg.setHeaderText("Оберіть категорію:"); Optional<String> catRes = catDlg.showAndWait();
            if (!catRes.isPresent()) return; String cat = catRes.get();

            TextInputDialog durDlg = new TextInputDialog(); durDlg.setHeaderText("Введіть тривалість (хв):"); Optional<String> durRes = durDlg.showAndWait();
            if (!durRes.isPresent()) return; int dur = Integer.parseInt(durRes.get().trim());

            TextInputDialog prDlg = new TextInputDialog(); prDlg.setHeaderText("Введіть ціну (грн):"); Optional<String> prRes = prDlg.showAndWait();
            if (!prRes.isPresent()) return; double pr = Double.parseDouble(prRes.get().trim());

            TextInputDialog mDlg = new TextInputDialog(); mDlg.setHeaderText("Введіть кількість майстрів:"); Optional<String> mRes = mDlg.showAndWait();
            if (!mRes.isPresent()) return; int masters = Integer.parseInt(mRes.get().trim());

            BeautyProcedure newProc = new BeautyProcedure(id, name, cat, dur, pr, masters);
            masterProcedureList.add(newProc);
            procedureData.setAll(masterProcedureList);
            recalculateDashboard();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Не вдалося додати процедуру. Перевірте валідацію.");
        }
    }

    @FXML
    void handleAbout(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Про програму", "Курсова робота з ООП\nТема: Облік процедур у салоні краси\nВиконав студент НУК групи 122 спеціальності");
    }

    @FXML
    void handleRunOperation(ActionEvent event) {
        String operation = operationsChoiceBox.getValue();
        if (operation == null) {
            showAlert(Alert.AlertType.WARNING, "Увага", "Оберіть операцію.");
            return;
        }
        if (masterProcedureList.isEmpty() && !operation.startsWith("2.") && !operation.startsWith("6.") && !operation.startsWith("7.")) {
            showAlert(Alert.AlertType.WARNING, "Увага", "Список порожній.");
            return;
        }

        switch (operation) {
            case "1. Пошук процедури за ID":
                TextInputDialog idDlg = new TextInputDialog(); idDlg.setHeaderText("Введіть ID:");
                idDlg.showAndWait().ifPresent(res -> procedureData.setAll(procedureService.findProcedureById(masterProcedureList, Integer.parseInt(res.trim()))));
                break;
            case "2. Пошук процедури за назвою (показати ціну)":
                TextInputDialog nameDlg = new TextInputDialog(); nameDlg.setHeaderText("Введіть назву:");
                nameDlg.showAndWait().ifPresent(res -> {
                    Optional<BeautyProcedure> p = procedureService.findProcedureByName(masterProcedureList, res);
                    if (p.isPresent()) showAlert(Alert.AlertType.INFORMATION, "Знайдено", "Ціна: " + p.get().getPrice() + " грн");
                    else showAlert(Alert.AlertType.INFORMATION, "Результат", "Нічого не знайдено.");
                });
                break;
            case "3. Список процедур за категорією":
                ChoiceDialog<String> catDlg = new ChoiceDialog<>("Перукарські послуги", Arrays.asList("Перукарські послуги", "Нігтьовий сервіс", "Косметологія", "Візаж"));
                catDlg.setHeaderText("Оберіть категорію:");
                catDlg.showAndWait().ifPresent(res -> procedureData.setAll(procedureService.getProceduresByCategory(masterProcedureList, res)));
                break;
            case "4. Список навпіль за тривалістю":
                TextInputDialog durDlg = new TextInputDialog(); durDlg.setHeaderText("Введіть тривалість (хв):");
                durDlg.showAndWait().ifPresent(res -> procedureData.setAll(procedureService.getProceduresByDuration(masterProcedureList, Integer.parseInt(res.trim()))));
                break;
            case "5. Сортувати поточний список за ціною":
                ChoiceDialog<String> sortDlg = new ChoiceDialog<>("За зростанням", Arrays.asList("За зростанням", "За спаданням"));
                sortDlg.setHeaderText("Напрямок сортування:");
                sortDlg.showAndWait().ifPresent(res -> procedureData.setAll(procedureService.sortProceduresByPrice(new ArrayList<>(procedureData), res.equals("За зростанням"))));
                break;
            case "6. Звіт: Для кожної категорії – процедура з наибольшою ціною":
                Map<String, BeautyProcedure> mapMax = procedureService.getHighestPricedProcedurePerCategory(masterProcedureList);
                StringBuilder sb1 = new StringBuilder("Найдорожчі процедури за категоріями:\n");
                mapMax.forEach((k, v) -> sb1.append(k).append(" -> ").append(v.getName()).append(" (").append(v.getPrice()).append(" грн)\n"));
                showAlert(Alert.AlertType.INFORMATION, "Звіт", sb1.toString());
                break;
            case "7. Звіт: Процедури груповані за категоріями (сорт. за кількістю майстрів)":
                ChoiceDialog<String> sortMDlg = new ChoiceDialog<>("За зростанням", Arrays.asList("За зростанням", "За спаданням"));
                sortMDlg.setHeaderText("Сортування кількості майстрів:");
                sortMDlg.showAndWait().ifPresent(res -> {
                    Map<String, List<BeautyProcedure>> grouped = procedureService.getProceduresGroupedByCategorySortedByMasters(masterProcedureList, res.equals("За зростанням"));
                    StringBuilder sb2 = new StringBuilder("Звіт по майстрах:\n");
                    grouped.forEach((k, v) -> {
                        sb2.append("[").append(k).append("]\n");
                        v.forEach(p -> sb2.append("  - ").append(p.getName()).append(" (Майстрів: ").append(p.getMastersCount()).append(")\n"));
                    });
                    showAlert(Alert.AlertType.INFORMATION, "Звіт", sb2.toString());
                });
                break;
        }
    }

    private void fillOperationsChoiceBox() {
        operationsChoiceBox.getItems().clear();
        operationsChoiceBox.getItems().addAll(
                "1. Пошук процедури за ID",
                "2. Пошук процедури за назвою (показати ціну)",
                "3. Список процедур за категорією",
                "4. Список навпіль за тривалістю",
                "5. Сортувати поточний список за ціною",
                "6. Звіт: Для кожної категорії – процедура з наибольшою ціною",
                "7. Звіт: Процедури груповані за категоріями (сорт. за кількістю майстрів)"
        );
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}