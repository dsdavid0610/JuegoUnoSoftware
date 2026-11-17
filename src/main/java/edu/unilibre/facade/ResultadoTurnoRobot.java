package edu.unilibre.facade;

import edu.unilibre.modelo.Carta;
import edu.unilibre.modelo.Jugador;

public record ResultadoTurnoRobot(Jugador robot, Carta cartaJugada, String colorElegido, boolean dijoUno, int cartasRobadas, Jugador penalizadoPorUno) {
    public boolean jugoCarta() {
        return cartaJugada != null;
    }
}
