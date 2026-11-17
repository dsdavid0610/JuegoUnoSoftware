package edu.unilibre.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JugadorTest {
    @Test void calculaPuntosDeLaMano() {
        Jugador jugador = new Jugador("Karol");
        jugador.agregarCarta(new Carta("CAMBIO_COLOR", "MAS_CUATRO"));
        jugador.agregarCarta(new Carta("ROJO", "SALTO"));
        jugador.agregarCarta(new Carta("AZUL", "4"));
        assertEquals(40, jugador.calcularPuntosMano());
    }
}