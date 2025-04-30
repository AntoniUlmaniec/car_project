package car_simulation;

import javafx.scene.control.Alert;

public class Engine extends Component {
    private final int maxEngineSpeed = 3500;
    private int engineSpeed;

    public Engine(String name, int weight, int price) {
        super(name, weight, price);
    }

    public void start() {
        engineSpeed = 600;
        System.out.println("Samochód włączony!");
    }

    public void stop() {
        engineSpeed = 0;
        System.out.println("Samochód wyłączony!");
    }

    public void increaseEngineSpeed() {
        engineSpeed = engineSpeed + 100;
        if (engineSpeed > maxEngineSpeed) {
            engineSpeed = maxEngineSpeed;
            showError("Osiągnięto maksymalne obroty!");
        }
    }

    public void decreaseEngineSpeed() {
        engineSpeed = engineSpeed - 100;
        if (engineSpeed < 600) {
            engineSpeed = 600;
            showError("Osiągnięto najmniejsze obroty!");
        }
    }

    public int getEngineSpeed() {
        return engineSpeed;
    }

    public void setEngineSpeed(int newSpeed) {
        if (newSpeed < 600) {
            engineSpeed = 600;
        } else if (newSpeed > maxEngineSpeed) {
            engineSpeed = maxEngineSpeed;
        } else {
            engineSpeed = newSpeed;
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}