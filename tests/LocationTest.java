package tests;

import imp.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {
    Location location=new Location("35.205764353510894,32.106326494117646,0.0");
    Location location2=new Location("35.605764353510894,32.806326494117646,0.0");

    @Test
    void x() {
        assertEquals(location.x(),35.205764353510894);
        assertEquals(location2.x(),35.605764353510894);
    }

    @Test
    void y() {
        assertEquals(location.y(),32.106326494117646);
        assertEquals(location2.y(),32.806326494117646);
    }

    @Test
    void z() {
        assertEquals(location.z(),0);
        assertEquals(location2.z(),0);
    }

    @Test
    void distance() {
    }
}