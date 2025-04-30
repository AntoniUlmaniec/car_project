package car_simulation;

public class Clutch extends Component {
    private boolean clutchState = false;

    public Clutch(String name, int weight, int price) {
        super(name, weight, price);
    }

    public void pressClutch() {
        clutchState = true;
    }

    public void releaseClutch() {
        clutchState = false;
    }

    public boolean getClutchState() {
        return clutchState;
    }
}
