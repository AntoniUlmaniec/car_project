package car_simulation;

import javafx.scene.control.Alert;

public class Gearbox extends Component {
    public Clutch clutch;

    private int actualGear = 0;

    public Gearbox(String name, int weight, int price, Clutch clutch) {
        super(name, weight, price);
        this.clutch = clutch;
    }


    public void shiftUp() throws GearboxException {
        int maxGear = 6;
        if (!clutch.getClutchState()) {
            showError("Aby zmienić bieg, proszę wcisnąć sprzęgło!");
        } else {
            actualGear++;
            if (actualGear > maxGear) {
                actualGear--;
                throw new GearboxException("Błąd podczas zmiany biegu!");
            }
        }
    }

    public void shiftDown() throws GearboxException {
        if (!clutch.getClutchState()) {
            showError("Aby zmienić bieg, proszę wcisnąć sprzęgło!");
        } else {
            actualGear--;
            if (actualGear < 0) {
                actualGear++;
                throw new GearboxException("Błąd podczas zmiany biegu!");
            }
        }
    }

    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public int getActualGear(){
        return actualGear;
    }

    public void setActualGear(int actualGear) {
        this.actualGear = actualGear;
    }


}
