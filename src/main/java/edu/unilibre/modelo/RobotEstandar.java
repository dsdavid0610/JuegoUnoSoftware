package edu.unilibre.modelo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


public class RobotEstandar implements EstrategiaRobot {

    private static final List<String> colores = Arrays.stream(ColorCarta.values())
            .filter(color -> color != ColorCarta.CAMBIO_COLOR)
            .map(ColorCarta::name)
            .toList();
    protected final Random azar = new Random();

    @Override
    public Optional<Carta> elegirCarta(JuegoUno juego, Jugador robot) {
        List<Carta> jugables = robot.getMano().stream()
                .filter(carta -> juego.esJugadaValida(robot, carta))
                .toList();
        if (jugables.isEmpty()) {
            return Optional.empty();
        }
        int mejor = jugables.stream().mapToInt(this::prioridad).max().orElse(0);
        List<Carta> opciones = jugables.stream().filter(carta -> prioridad(carta) == mejor).toList();
        return Optional.of(opciones.get(azar.nextInt(opciones.size())));
    }

    protected int prioridad(Carta carta) {
        return carta.esComodin() ? 0 : 1;
    }

    @Override
    public String elegirColor(Jugador robot) {
        Map<String, Long> conteo = colores.stream().collect(Collectors.toMap(
                color -> color,
                color -> robot.getMano().stream()
                        .filter(carta -> carta.getColor().equals(color))
                        .count()));
        long maximo = Collections.max(conteo.values());
        List<String> mejores = colores.stream().filter(color -> conteo.get(color) == maximo).toList();
        return mejores.get(azar.nextInt(mejores.size()));
    }
}
