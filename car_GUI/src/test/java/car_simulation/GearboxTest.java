package car_simulation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GearboxTest {
    private Gearbox gearbox;
    private Clutch clutch;

    @Before
    public void setUp() { // wywolywane przed kazdym testem
        clutch = new Clutch("Sprzęgło", 10, 500);
        gearbox = new Gearbox("Skrzynia biegów", 50, 1000, clutch);
    }

    @After
    public void tearDown() { // wywolywane po kazdym tescie
        gearbox = null;
        clutch = null;
    }

    @Test
    public void testShiftUp() throws GearboxException {
        clutch.pressClutch();
        gearbox.shiftUp();
        assertEquals(1, gearbox.getActualGear());

        gearbox.setActualGear(6);
        try {
            gearbox.shiftUp();
            System.out.println("Brak wyjątku!");
        } catch (GearboxException e) {
            System.out.println("Rzucony wyjątek: " + e.getMessage());
        }
    }

    @Test
    public void testShiftDown() throws GearboxException {
        clutch.pressClutch();
        gearbox.shiftUp();
        gearbox.shiftDown();
        assertEquals(0, gearbox.getActualGear());

        try {
            gearbox.shiftDown();
            System.out.println("Brak wyjątku!");
        } catch (GearboxException e) {
            System.out.println("Rzucony wyjątek: " + e.getMessage());
        }
    }

    @Test
    public void testShowError() {}

    @Test
    public void testGetActualGear() {
        gearbox.setActualGear(3);
        assertEquals(3, gearbox.getActualGear());
    }

    @Test
    public void testSetActualGear() {
        gearbox.setActualGear(2);
        assertEquals(2, gearbox.getActualGear());
    }
}
