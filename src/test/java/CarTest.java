import org.example.Car;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CarTest {

    Car testCar = new Car();
   @Test
    public void testEngine(){
        String checkEngineName = "Engine101";
        assertEquals(checkEngineName,testCar.getEngineName());
    }
}
