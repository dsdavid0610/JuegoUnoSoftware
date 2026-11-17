package edu.unilibre.fabrica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FabricaCartasEspecialesTest {
    @Test void creaElGrupoDeCartasEspecialesEsperado() {
        var cartas = new FabricaCartasEspeciales().crearGrupo();
        assertEquals(32, cartas.size());
        assertTrue(cartas.stream().allMatch(c -> c.esComodin() || c.esMasDos() || c.esSalta() || c.esReversa()));
    }
}
