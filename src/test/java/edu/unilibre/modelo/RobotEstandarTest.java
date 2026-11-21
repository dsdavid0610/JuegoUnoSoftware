package edu.unilibre.modelo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RobotEstandarTest {
    @Test void eligeColorValidoParaElRobot() {
        Jugador robot = new Jugador("Robot", new RobotEstandar());
        robot.agregarCarta(new Carta("ROJO", "2"));
        String color = robot.getEstrategia().elegirColor(robot);
        assertTrue(java.util.List.of("ROJO", "AZUL", "VERDE", "AMARILLO").contains(color));
    }
}
