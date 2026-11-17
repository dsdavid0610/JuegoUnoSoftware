package edu.unilibre.facade;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JuegoFacadeTest {
    @Test void iniciaPartidaYExponeJugadoresPorLaFachada() {
        JuegoFacade facade = new JuegoFacade();
        facade.iniciarPartida("Natalia", 2);
        assertEquals(3, facade.cantidadJugadores());
        assertEquals("Natalia", facade.getJugadores().get(0).getNombre());
        assertNotNull(facade.getJugadorActual());
        assertNotNull(facade.getCartaSuperior());
    }
}
