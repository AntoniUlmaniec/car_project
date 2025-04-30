package car_simulation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EngineTest {
    private Engine engine;

    @Before
    public void setUp() throws Exception { // wywolywane przed kazdym testem
        engine = new Engine("Silnik", 200, 5000);
    }

    @After
    public void tearDown() throws Exception { // wywolywane po kazdym tescie
        engine = null;
    }

    @Test
    public void start() {
        engine.start();
        assertEquals(600, engine.getEngineSpeed());
    }

    @Test
    public void stop() {
        engine.start();
        engine.stop();
        assertEquals(0, engine.getEngineSpeed());
    }

    @Test
    public void increaseEngineSpeed() {
        engine.start();
        engine.increaseEngineSpeed();
        assertEquals(700, engine.getEngineSpeed());
    }

    @Test
    public void decreaseEngineSpeed() {
        engine.start();
        engine.increaseEngineSpeed();
        engine.decreaseEngineSpeed();
        assertEquals(600, engine.getEngineSpeed());
    }

    @Test
    public void getEngineSpeed() {
        engine.start();
        assertEquals(600, engine.getEngineSpeed());
    }

    @Test
    public void setEngineSpeed() {
        engine.setEngineSpeed(1000);
        assertEquals(1000, engine.getEngineSpeed());

        engine.setEngineSpeed(4000);
        assertEquals(3500, engine.getEngineSpeed());

        engine.setEngineSpeed(500);
        assertEquals(600, engine.getEngineSpeed());
    }
}