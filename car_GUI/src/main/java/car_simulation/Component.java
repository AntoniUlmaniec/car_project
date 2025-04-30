package car_simulation;

public class Component {
    private final String name;
    private final int weight;
    private final int price;

    public Component(String name, int weight, int price) {
        this.name = name;
        this.weight = weight;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public int getPrice() {
        return price;
    }

    public String toString() {
        return name + " - ( Cena: " + price + " zł, Waga: " + weight + " kg)";
    }
}
