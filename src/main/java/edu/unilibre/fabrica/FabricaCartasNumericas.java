package edu.unilibre.fabrica;

import edu.unilibre.modelo.Carta;

import java.util.ArrayList;
import java.util.List;

public class FabricaCartasNumericas extends FabricaCartas {

    @Override
    public Carta crearCarta(String color, String valor) {
        if (!valor.matches("[0-9]")) {
            throw new IllegalArgumentException("Una carta numérica debe valer de 0 a 9: " + valor);
        }
        return new Carta(color, valor);
    }

    @Override
    public List<Carta> crearGrupo() {
        List<Carta> cartas = new ArrayList<>();
        for (String color : coloresNormales()) {
            cartas.add(crearCarta(color, "0"));
            for (int numero = 1; numero <= 9; numero++) {
                cartas.add(crearCarta(color, String.valueOf(numero)));
                cartas.add(crearCarta(color, String.valueOf(numero)));
            }
        }
        return cartas;
    }
}