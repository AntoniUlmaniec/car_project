package car_simulation;

import javafx.scene.control.Alert;
import java.util.ArrayList;
import java.util.List;

public class Car extends Thread {
    public Gearbox gearbox;
    public Engine engine;

    private boolean powerState = false;
    private final String licensePlate;
    private final String model;
    private final String color;

    private final Position position = new Position();
    private Position targetPosition = null;
    private final List<Listener> listeners = new ArrayList<>();

    public Car(String licensePlate, String model, String color, Engine engine, Gearbox gearbox) {
        this.licensePlate = licensePlate;
        this.model = model;
        this.color = color;
        this.engine = engine;
        this.gearbox = gearbox;
        this.start();
    }

    public void turnOn() {
        powerState = true;
        engine.start();
    }

    public void turnOff() {
        powerState = false;
        gearbox.setActualGear(0);
        engine.stop();
    }

    public int getSpeed() {
        int actualGear = gearbox.getActualGear();
        int engineSpeed = engine.getEngineSpeed();
        int maxSpeed = 220;

        if (actualGear == 6 && engineSpeed == 3500) {
            return maxSpeed;
        }

        if (actualGear == 0) {
            return 0;
        }

        if (actualGear > 1 && engineSpeed < 800) {
            engine.setEngineSpeed(800);
            showError("Osiągnięto najmniejsze obroty!");
            engineSpeed = 800;
        }

        int[][] speedRanges = {
                {5, 10, 15, 20, 25, 30},   // Gear 1
                {30, 35, 40, 45, 50, 55},  // Gear 2
                {55, 60, 65, 70, 75, 80},   // Gear 3
                {80, 85, 90, 95, 100, 105}, // Gear 4
                {110, 120, 130, 140, 150, 160}, // Gear 5
                {160, 170, 180, 190, 200, 220} // Gear 6
        };

        int[] speedThresholds = {800, 1000, 1500, 2000, 2500, 3000, 3500};

        for (int i = 0; i < speedThresholds.length - 1; i++) {
            if (engineSpeed >= speedThresholds[i] && engineSpeed <= speedThresholds[i + 1]) {
                return speedRanges[actualGear - 1][i];
            }
        }

        return 0;
    }

    public void changeGear(boolean shiftUp) throws GearboxException {
        int currentGear = gearbox.getActualGear();
        int currentSpeed = engine.getEngineSpeed();

        if (shiftUp) {
            if (currentSpeed <= 2000) {
                if (currentGear == 0) {
                    gearbox.shiftUp();
                } else {
                    showError("Za małe obroty by zmienić bieg!");
                }
            } else {
                gearbox.shiftUp();
            }
        } else {
            if (currentSpeed >= 2000) {
                showError("Za duże obroty by zmienić bieg!");
            } else {
                gearbox.shiftDown();
            }
        }

        if (currentGear < gearbox.getActualGear()) {
            if (gearbox.getActualGear() == 1) {
                engine.setEngineSpeed(600);
            } else {
                engine.setEngineSpeed(800);
            }
        } else if (currentGear > gearbox.getActualGear()) {
            if (gearbox.getActualGear() == 0) {
                engine.setEngineSpeed(600);
            } else {
                engine.setEngineSpeed(3500);
            }
        }
    }

    @Override
    public void run() {
        double deltat = 0.04;
        while (true) {
            if (targetPosition != null) {
                double distance = Math.sqrt(Math.pow(targetPosition.x - position.x, 2) + Math.pow(targetPosition.y - position.y, 2));

                if (distance < 1) {
                    targetPosition = null;
                    notifyListeners();
                    continue;
                }

                double dx = getSpeed() * deltat * (targetPosition.x - position.x) / distance;
                double dy = getSpeed() * deltat * (targetPosition.y - position.y) / distance;

                position.x += dx;
                position.y += dy;

                notifyListeners();
            }
            try {
                Thread.sleep(50); //50 ms wait
            } catch (InterruptedException e) {
                System.out.println("Usunięto: " + getColor() + " " + getModel() + " (" + getLicensePlate() + ")");
            }
        }
    }

    public void goTo(Position targetPosition) {
        this.targetPosition = targetPosition;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (Listener listener : listeners) {
            listener.update();
        }
    }

    public Position getPosition() {
        return position;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public boolean isPowerState() {
        return powerState;
    }

    public int getWeight() {
        return engine.getWeight() + gearbox.getWeight() + gearbox.clutch.getWeight();
    }

    public int getPrice() {
        return engine.getPrice() + gearbox.getPrice() + gearbox.clutch.getPrice();
    }

    public String toString() {
        return getColor() + " " + getModel() + " (" + getLicensePlate() + ")";
    }
}
