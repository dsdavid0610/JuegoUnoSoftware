package edu.unilibre.fabrica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FabricaCartasNumericasTest {
    @Test void creaElGrupoNumericoEsperado() {
        var cartas = new FabricaCartasNumericas().crearGrupo();
        assertEquals(76, cartas.size());
        assertTrue(cartas.stream().allMatch(c -> !c.esComodin()));
    }
}
