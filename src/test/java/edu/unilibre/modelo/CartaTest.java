package edu.unilibre.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartaTest {
    @Test void rechazaCombinacionInvalidaYCalculaPuntos() {
        assertThrows(IllegalArgumentException.class, () -> new Carta("CAMBIO_COLOR", "8"));
        assertEquals(50, new Carta("CAMBIO_COLOR", "CAMBIO_COLOR").puntos());
        assertEquals(20, new Carta("AZUL", "MAS_DOS").puntos());
    }
}
