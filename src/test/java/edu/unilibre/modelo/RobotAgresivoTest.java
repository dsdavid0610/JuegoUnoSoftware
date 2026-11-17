package edu.unilibre.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RobotAgresivoTest {
    @Test void priorizaMasDosSobreUnaCartaNumerica() {
        RobotAgresivo robot = new RobotAgresivo();
        Carta masDos = new Carta("ROJO", "MAS_DOS");
        Carta numerica = new Carta("ROJO", "5");
        assertTrue(robot.prioridad(masDos) > robot.prioridad(numerica));
    }
}
