package edu.unilibre.modelo;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JuegoUnoTest {
    @Test void reparteSieteCartasEInformaInicioDeRonda() {
        JuegoUno juego = new JuegoUno();
        juego.agregarJugador("Humano");
        juego.agregarJugador("Robot");
        List<String> eventos = new ArrayList<>();
        juego.agregarObservador(eventos::add);
        juego.iniciarJuego();
        assertEquals(7, juego.getJugadores().get(0).cantidadCartas());
        assertEquals(7, juego.getJugadores().get(1).cantidadCartas());
        assertEquals(1, juego.getNumeroRonda());
        assertTrue(eventos.contains("Ronda Iniciada"));
        assertNotNull(juego.getCartaSuperior());
    }
}

