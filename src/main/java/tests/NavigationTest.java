package tests;

import org.testng.annotations.Test;

public class NavigationTest extends BaseTest {

    @Test
    public void testNavigationOnly() {
        // We don't need to put any code here!
        // Why? Because BaseTest.setUp() runs BEFORE this method.
        // And your setUp() already contains 'masukKeMenuAyoLari()'.
        
        System.out.println("--- Navigation Logic Finished Successfully ---");
        
        // Let's sleep for 5 seconds just so you can see the result on the screen
        try { 
            Thread.sleep(5000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}