package agh.it.car_project_gui;

import car_simulation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class NewCarController {
    private Car car;

    @FXML
    private ComboBox<String> colorComboBox;
    @FXML
    private ComboBox<Engine> engineComboBox;
    @FXML
    private ComboBox<Gearbox> gearboxComboBox;
    @FXML
    private ComboBox<Clutch> clutchComboBox;
    
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;

    @FXML
    private TextField modelTxt;
    @FXML
    private TextField licensePlateTxt;



    public void initialize() {
        colorComboBox.getItems().addAll("Niebieski", "Czerwony", "Zielony", "Czarny");

        engineComboBox.getItems().addAll(
                new Engine("Diesel", 200, 15000),
                new Engine("Benzyna", 180, 12000),
                new Engine("Elektryczny", 250, 30000),
                new Engine("Hybryda", 220, 25000)
        );

        gearboxComboBox.getItems().addAll(
                new Gearbox("Manualna", 50, 3000, (Clutch) clutchComboBox.getValue()),
                new Gearbox("Automatyczna", 70, 5000, (Clutch) clutchComboBox.getValue()),
                new Gearbox("Półautomatyczna", 80, 4000, (Clutch) clutchComboBox.getValue())
        );

        clutchComboBox.getItems().addAll(
                new Clutch("Jednotarczowe", 15, 1000),
                new Clutch("Wielotarczowe", 20, 2000),
                new Clutch("Dwusprzęgłowe", 25, 3500)
        );
    }


    public void confirmCar(ActionEvent actionEvent) {
        String modelName = modelTxt.getText();
        String licensePlate = licensePlateTxt.getText();
        String color = colorComboBox.getValue();
        Engine engine = engineComboBox.getValue();
        Gearbox gearbox = gearboxComboBox.getValue();
        Clutch clutch = clutchComboBox.getValue();

        if (modelName.isEmpty() || licensePlate.isEmpty() || color == null || engine == null || gearbox == null || clutch == null) {
                showError("Błąd przy tworzeniu samochodu!");
                return;
        }

        car = new Car(
                licensePlate,
                modelName,
                color,
                new Engine(engine.getName(), engine.getWeight(), engine.getPrice()),
                new Gearbox(gearbox.getName(), gearbox.getWeight(), gearbox.getPrice(),
                        new Clutch(clutch.getName(), clutch.getWeight(), clutch.getPrice()))
        );

        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public Car getCar() {
        return car;
    }


    public void cancelCar(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void engineChoosed(ActionEvent actionEvent) {}

    public void gearboxChoosed(ActionEvent actionEvent) {}

    public void clutchChoosed(ActionEvent actionEvent) {}

    public void modelName(ActionEvent actionEvent) {}

    public void licensePlateNumber(ActionEvent actionEvent) {}
}
