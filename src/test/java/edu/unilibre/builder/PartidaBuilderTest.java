package edu.unilibre.builder;
import edu.unilibre.modelo.JuegoUno;
import edu.unilibre.modelo.RobotAgresivo;
import edu.unilibre.modelo.RobotEstandar;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PartidaBuilderTest {
    @Test void construyeJugadorHumanoYRobotsConEstrategias() {
        JuegoUno juego = new PartidaBuilder().conJugadorHumano(" Laura ").conRobots(2).build();
        assertEquals(3, juego.cantidadJugadores());
        assertEquals("Laura", juego.getJugadores().get(0).getNombre());
        assertInstanceOf(RobotEstandar.class, juego.getJugadores().get(1).getEstrategia());
        assertInstanceOf(RobotAgresivo.class, juego.getJugadores().get(2).getEstrategia());
    }
    @Test void rechazaNombreVacioYCantidadDeRobotsInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new PartidaBuilder().conJugadorHumano(" "));
        assertThrows(IllegalArgumentException.class, () -> new PartidaBuilder().conRobots(0));
        assertThrows(IllegalArgumentException.class, () -> new PartidaBuilder().conRobots(4));
    }
}
