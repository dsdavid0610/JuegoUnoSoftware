package edu.unilibre.fabrica;

import edu.unilibre.modelo.Carta;
import edu.unilibre.modelo.ColorCarta;

import java.util.Arrays;
import java.util.List;

public abstract class FabricaCartas {

    public abstract Carta crearCarta(String color, String valor);

    public abstract List<Carta> crearGrupo();

    protected static List<String> coloresNormales() {
        return Arrays.stream(ColorCarta.values())
                .filter(color -> color != ColorCarta.CAMBIO_COLOR)
                .map(ColorCarta::name)
                .toList();
    }
}
