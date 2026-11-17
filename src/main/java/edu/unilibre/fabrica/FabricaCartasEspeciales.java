package edu.unilibre.fabrica;

import edu.unilibre.modelo.Carta;
import edu.unilibre.modelo.ColorCarta;
import edu.unilibre.modelo.TipoCarta;

import java.util.ArrayList;
import java.util.List;

public class FabricaCartasEspeciales extends FabricaCartas {

    @Override
    public Carta crearCarta(String color, String valor) {
        if (valor.matches("[0-9]")) {
            throw new IllegalArgumentException("Una carta especial no puede ser numérica: " + valor);
        }
        return new Carta(color, valor);
    }

    @Override
    public List<Carta> crearGrupo() {
        List<Carta> cartas = new ArrayList<>();
        TipoCarta[] conAccion = {TipoCarta.SALTO, TipoCarta.REVERSA, TipoCarta.MAS_DOS};
        for (String color : coloresNormales()) {
            for (TipoCarta tipo : conAccion) {
                cartas.add(crearCarta(color, tipo.name()));
                cartas.add(crearCarta(color, tipo.name()));
            }
        }
        for (int i = 0; i < 4; i++) {
            cartas.add(crearCarta(ColorCarta.CAMBIO_COLOR.name(), TipoCarta.CAMBIO_COLOR.name()));
            cartas.add(crearCarta(ColorCarta.CAMBIO_COLOR.name(), TipoCarta.MAS_CUATRO.name()));
        }
        return cartas;
    }
}
