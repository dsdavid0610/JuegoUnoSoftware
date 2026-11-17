package edu.unilibre.builder;

import edu.unilibre.fabrica.FabricaCartas;
import edu.unilibre.modelo.JuegoUno;
import edu.unilibre.modelo.Jugador;
import edu.unilibre.modelo.RobotAgresivo;
import edu.unilibre.modelo.RobotEstandar;

import java.util.List;


public class PartidaBuilder {

    public static final int MIN_ROBOTS = 1;
    public static final int MAX_ROBOTS = 3;

    private String nombreHumano = "Jugador";
    private int cantidadRobots = MAX_ROBOTS;
    private List<FabricaCartas> fabricas;

    public PartidaBuilder conJugadorHumano(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del jugador es obligatorio");
        }
        this.nombreHumano = nombre.trim();
        return this;
    }

    public PartidaBuilder conRobots(int cantidad) {
        if (cantidad < MIN_ROBOTS || cantidad > MAX_ROBOTS) {
            throw new IllegalArgumentException("Debe haber entre 1 y 3 robots");
        }
        this.cantidadRobots = cantidad;
        return this;
    }

    public PartidaBuilder conFabricas(List<FabricaCartas> fabricas) {
        if (fabricas == null || fabricas.isEmpty()) {
            throw new IllegalArgumentException("Se necesita al menos una fábrica de cartas");
        }
        this.fabricas = List.copyOf(fabricas);
        return this;
    }

    public JuegoUno build() {
        JuegoUno juego = fabricas == null ? new JuegoUno() : new JuegoUno(fabricas);
        juego.agregarJugador(new Jugador(nombreHumano));
        for (int i = 1; i <= cantidadRobots; i++) {
            Jugador robot = i % 2 == 1
                    ? new Jugador("Robot " + i, new RobotEstandar())
                    : new Jugador("Robot " + i, new RobotAgresivo());
            juego.agregarJugador(robot);
        }
        return juego;
    }
}

