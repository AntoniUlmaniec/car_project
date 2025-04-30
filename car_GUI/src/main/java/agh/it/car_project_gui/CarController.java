package agh.it.car_project_gui;

import car_simulation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class CarController implements Listener {

    @Override
    public void update() {
        refresh();
    }

    private final ObservableList<Car> cars = FXCollections.observableArrayList();
    private Car car;

    @FXML
    private TextArea modelTxt;
    @FXML
    private TextArea licensePlateTxt;
    @FXML
    private TextArea carWeightTxt;
    @FXML
    private TextArea carPriceTxt;
    @FXML
    private TextArea carSpeedTxt;
    @FXML
    private Button turnCarOnButton;
    @FXML
    private Button turnCarOffButton;

    @FXML
    private TextArea gearboxNameTxt;
    @FXML
    private TextArea gearboxPriceTxt;
    @FXML
    private TextArea gearboxWeightTxt;
    @FXML
    private TextArea gearboxGearTxt;
    @FXML
    private Button shiftUpButton;
    @FXML
    private Button shiftDownButton;

    @FXML
    private TextArea engineNameTxt;
    @FXML
    private TextArea enginePriceTxt;
    @FXML
    private TextArea engineWeightTxt;
    @FXML
    private TextArea engineSpeedTxt;
    @FXML
    private Button increaseSpeedButton;
    @FXML
    private Button decreaseSpeedButton;

    @FXML
    private TextArea clutchNameTxt;
    @FXML
    private TextArea clutchPriceTxt;
    @FXML
    private TextArea clutchWeightTxt;
    @FXML
    private TextArea clutchStateTxt;
    @FXML
    private Button pressClutchButton;
    @FXML
    private Button releaseClutchButton;

    @FXML
    private ImageView blueCarImage;
    @FXML
    private ImageView greenCarImage;
    @FXML
    private ImageView redCarImage;
    @FXML
    private ImageView blackCarImage;

    @FXML
    private Pane mapPane;
    @FXML
    private Button addNewCarButton;
    @FXML
    private Button deleteCarButton;
    @FXML
    private ComboBox<Car> carComboBox;

    @FXML
    public void initialize() {
        switchButtons(true, true);

        mapPane.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();
            Position targetPosition = new Position(x, y);
            if (car != null) {
                car.goTo(targetPosition);
                System.out.println("Samochód jedzie do: " + x + ", " + y);
            }
        });
    }

    public void turnCarOn(ActionEvent actionEvent) {
        car.turnOn();
        switchButtons(true, false);
        refresh();
    }

    public void turnCarOff(ActionEvent actionEvent) {
        car.turnOff();
        switchButtons(false, true);
        refresh();
    }

    public void shiftUp(ActionEvent actionEvent) throws GearboxException {
        car.changeGear(true);
        refresh();
    }

    public void shiftDown(ActionEvent actionEvent) throws GearboxException {
        car.changeGear(false);
        refresh();
    }

    public void increaseSpeed(ActionEvent actionEvent) {
        if (car.gearbox.getActualGear() == 0) {
            showError("Nie można jechać na luzie!");
        } else if (car.gearbox.clutch.getClutchState()) {
            showError("Nie można przyśpieszyć z wciśniętym sprzęgłem!");
        } else {
            car.engine.increaseEngineSpeed();
            refresh();
        }
    }

    public void decreaseSpeed(ActionEvent actionEvent) {
        if (car.gearbox.getActualGear() == 0) {
            showError("Nie można jechać na luzie!");
        } else if (car.gearbox.clutch.getClutchState()) {
            showError("Nie można zwolnić z wciśniętym sprzęgłem!");
        } else {
            car.engine.decreaseEngineSpeed();
            refresh();
        }
    }

    public void pressClutch(ActionEvent actionEvent) {
        car.gearbox.clutch.pressClutch();
        refresh();
    }

    public void releaseClutch(ActionEvent actionEvent) {
        car.gearbox.clutch.releaseClutch();
        refresh();
    }

    public void carChoosed(ActionEvent actionEvent) {
        Car selectedCar = carComboBox.getValue();
        if (selectedCar != null) {
            this.car = selectedCar;
            clearCarDetails();
            showCarImage(car);

            if (!selectedCar.isPowerState()) {
                switchButtons(false, true);
            } else {
                switchButtons(true, false);
            }
            refresh();
        }
    }

    public void addNewCar(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add-new-car.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Dodaj Nowy Samochód!");

        NewCarController newCarController = loader.getController();

        stage.setOnHiding(event -> {
            Car createdCar = newCarController.getCar();
            if (createdCar != null) {

                cars.add(createdCar);
                carComboBox.getItems().add(createdCar);
                carComboBox.setValue(createdCar);

                createdCar.addListener(this);
                showCarImage(createdCar);
                switchButtons(false, true);

                refresh();
            }
        });

        stage.show();
    }

    public void deleteCar(ActionEvent actionEvent) {
        Car selectedCar = carComboBox.getValue();

        if (selectedCar != null) {
            selectedCar.interrupt();
            selectedCar.removeListener(this);
            cars.remove(selectedCar);
            carComboBox.getItems().remove(selectedCar);

            if (cars.isEmpty()) {
                car = null;
                clearCarDetails();
                switchButtons(true, true);
            } else {
                car = carComboBox.getItems().get(0);
                carComboBox.setValue(car);
                clearCarDetails();
                showCarImage(car);
                refresh();
            }
        } else {
            showError("Brak dostępnych samochodów!");
        }
    }

    private void refresh() {
        if (car != null) {
            Platform.runLater(() -> {
                modelTxt.setText(car.getModel());
                licensePlateTxt.setText(car.getLicensePlate());
                carWeightTxt.setText(String.valueOf(car.getWeight()));
                carPriceTxt.setText(String.valueOf(car.getPrice()));
                carSpeedTxt.setText(String.valueOf(car.getSpeed()));

                gearboxNameTxt.setText(car.gearbox.getName());
                gearboxPriceTxt.setText(String.valueOf(car.gearbox.getPrice()));
                gearboxWeightTxt.setText(String.valueOf(car.gearbox.getWeight()));
                gearboxGearTxt.setText(String.valueOf(car.gearbox.getActualGear()));

                engineNameTxt.setText(car.engine.getName());
                enginePriceTxt.setText(String.valueOf(car.engine.getPrice()));
                engineWeightTxt.setText(String.valueOf(car.engine.getWeight()));
                engineSpeedTxt.setText(String.valueOf(car.engine.getEngineSpeed()));

                clutchNameTxt.setText(car.gearbox.clutch.getName());
                clutchPriceTxt.setText(String.valueOf(car.gearbox.clutch.getPrice()));
                clutchWeightTxt.setText(String.valueOf(car.gearbox.clutch.getWeight()));
                clutchStateTxt.setText(String.valueOf(car.gearbox.clutch.getClutchState()));

                switch (car.getColor()) {
                    case "Niebieski":
                        blueCarImage.setTranslateX(car.getPosition().getX());
                        blueCarImage.setTranslateY(car.getPosition().getY());
                        break;
                    case "Czerwony":
                        redCarImage.setTranslateX(car.getPosition().getX());
                        redCarImage.setTranslateY(car.getPosition().getY());
                        break;
                    case "Zielony":
                        greenCarImage.setTranslateX(car.getPosition().getX());
                        greenCarImage.setTranslateY(car.getPosition().getY());
                        break;
                    case "Czarny":
                        blackCarImage.setTranslateX(car.getPosition().getX());
                        blackCarImage.setTranslateY(car.getPosition().getY());
                        break;
                }
            });
        }
    }

    private void switchButtons(boolean state1, boolean state2) {
        turnCarOnButton.setDisable(state1);
        turnCarOffButton.setDisable(state2);
        shiftUpButton.setDisable(state2);
        shiftDownButton.setDisable(state2);
        increaseSpeedButton.setDisable(state2);
        decreaseSpeedButton.setDisable(state2);
        pressClutchButton.setDisable(state2);
        releaseClutchButton.setDisable(state2);
    }

    private void showCarImage(Car car) {
        switch (car.getColor()) {
            case "Niebieski":
                blueCarImage.setVisible(true);
                break;
            case "Czerwony":
                redCarImage.setVisible(true);
                break;
            case "Zielony":
                greenCarImage.setVisible(true);
                break;
            case "Czarny":
                blackCarImage.setVisible(true);
                break;
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearCarDetails() {
        modelTxt.setText("");
        licensePlateTxt.setText("");
        carWeightTxt.setText("");
        carPriceTxt.setText("");
        carSpeedTxt.setText("");

        gearboxNameTxt.setText("");
        gearboxPriceTxt.setText("");
        gearboxWeightTxt.setText("");
        gearboxGearTxt.setText("");

        engineNameTxt.setText("");
        enginePriceTxt.setText("");
        engineWeightTxt.setText("");
        engineSpeedTxt.setText("");

        clutchNameTxt.setText("");
        clutchPriceTxt.setText("");
        clutchWeightTxt.setText("");
        clutchStateTxt.setText("");

        blueCarImage.setVisible(false);
        redCarImage.setVisible(false);
        greenCarImage.setVisible(false);
        blackCarImage.setVisible(false);
    }
}
