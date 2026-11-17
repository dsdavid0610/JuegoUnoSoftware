package edu.unilibre.facade;

import edu.unilibre.builder.PartidaBuilder;
import edu.unilibre.modelo.Carta;
import edu.unilibre.modelo.EstrategiaRobot;
import edu.unilibre.modelo.JuegoUno;
import edu.unilibre.modelo.Jugador;
import edu.unilibre.modelo.ObservadorJuego;
import edu.unilibre.modelo.RobotEstandar;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JuegoFacade {

    private final List<ObservadorJuego> observadores = new ArrayList<>();
    private final EstrategiaRobot estrategiaPorDefecto = new RobotEstandar();
    private static final String SEPARADOR = ";";

    private final Path archivoHistorial;
    private JuegoUno juego = new JuegoUno();

    public JuegoFacade() {
        this(Paths.get("historial_uno.csv"));
    }

    public JuegoFacade(Path archivoHistorial) {
        this.archivoHistorial = archivoHistorial;
    }
    public void agregarObservador(ObservadorJuego observador) {
        observadores.add(observador);
        juego.agregarObservador(observador);
    }
    public void iniciarPartida(String nombreHumano, int cantidadRobots) {
        JuegoUno nuevo = new PartidaBuilder()
                .conJugadorHumano(nombreHumano)
                .conRobots(cantidadRobots)
                .build();
        observadores.forEach(nuevo::agregarObservador);
        JuegoUno anterior = juego;
        juego = nuevo;
        try {
            nuevo.iniciarJuego();
        } catch (RuntimeException ex) {
            juego = anterior;
            throw ex;
        }
    }

    public void jugarCarta(Carta carta) {
        juego.jugarCarta(carta);
    }

    public void cambiarColor(String color) {
        juego.cambiarColor(color);
    }

    public Carta robarCarta() {
        return juego.robarCarta();
    }

    public void decirUno() {
        juego.decirUno();
    }

    public void nuevaRonda() {
        juego.nuevaRonda();
    }
    public void guardarHistorial() {
        Jugador ganador = juego.getGanadorPartida();
        if (ganador == null) {
            throw new IllegalStateException("La partida todavía no tiene ganador");
        }
        Jugador humano = juego.getJugadores().get(0);
        registrarPartida(humano.getNombre(), ganador.getNombre(), ganador == humano,
                ganador.getPuntos(), juego.getNumeroRonda());
    }

    public String textoHistorial() {
        List<String[]> partidas = leerHistorial();
        if (partidas.isEmpty()) {
            return "Todavía no hay partidas guardadas.";
        }
        StringBuilder texto = new StringBuilder();
        int victorias = 0;
        double rondas = 0;
        for (String[] p : partidas) {
            texto.append("Partida #").append(p[0]).append(" (").append(p[1]).append("): ").append(p[2])
                    .append(" - ganó ").append(p[3]).append(" con ").append(p[5]).append(" puntos en ")
                    .append(p[6]).append(" rondas\n");
            victorias += Boolean.parseBoolean(p[4]) ? 1 : 0;
            rondas += Integer.parseInt(p[6]);
        }
        return texto.append("\n").append(String.format("Partidas: %d | Victorias: %d | Derrotas: %d | Rondas promedio: %.1f", partidas.size(), victorias, partidas.size() - victorias, rondas / partidas.size())).toString();
    }

    void registrarPartida(String humano, String ganador, boolean ganoHumano, int puntos, int rondas) {
        List<String[]> partidas = leerHistorial();
        int id = partidas.stream().mapToInt(p -> Integer.parseInt(p[0])).max().orElse(0) + 1;
        partidas.add(new String[]{String.valueOf(id), LocalDate.now().toString(), limpiar(humano), limpiar(ganador),
                String.valueOf(ganoHumano), String.valueOf(puntos), String.valueOf(rondas)});
        try {
            Path carpeta = archivoHistorial.toAbsolutePath().getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            Files.write(archivoHistorial, partidas.stream().map(p -> String.join(SEPARADOR, p)).toList());
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo escribir " + archivoHistorial, e);
        }
    }

    private List<String[]> leerHistorial() {
        List<String[]> partidas = new ArrayList<>();
        if (!Files.exists(archivoHistorial)) {
            return partidas;
        }
        try {
            for (String linea : Files.readAllLines(archivoHistorial)) {
                String[] campos = linea.split(SEPARADOR, -1);
                try {
                    if (campos.length == 7) {
                        Integer.parseInt(campos[0]);
                        LocalDate.parse(campos[1]);
                        Integer.parseInt(campos[5]);
                        Integer.parseInt(campos[6]);
                        partidas.add(campos);
                    }
                } catch (RuntimeException lineaDanada) {
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer " + archivoHistorial, e);
        }
        return partidas;
    }

    private static String limpiar(String texto) {
        return texto.replace(SEPARADOR, ",").replace("\n", " ").replace("\r", " ");
    }

    public ResultadoTurnoRobot jugarTurnoRobot() {
        Jugador robot = juego.getJugadorActual();
        if (!robot.esRobot()) {
            throw new IllegalStateException("No es el turno de un robot");
        }
        EstrategiaRobot estrategia = robot.getEstrategia();
        Optional<Carta> eleccion = estrategia.elegirCarta(juego, robot);
        if (eleccion.isEmpty()) {
            int cantidad = Math.max(1, juego.getPenalizacionAcumulada());
            juego.robarCarta();
            return new ResultadoTurnoRobot(robot, null, null, false, cantidad, penalizadoPorUno());
        }
        Carta carta = eleccion.get();
        juego.jugarCarta(carta);
        Jugador penalizado = penalizadoPorUno();
        String color = null;
        if (juego.hayColorPendiente()) {
            color = estrategia.elegirColor(robot);
            juego.cambiarColor(color);
        }
        boolean dijoUno = false;
        if (juego.hayUnoPendiente() && juego.getJugadorQueDebeDecirUno() == robot) {
            juego.decirUno();
            dijoUno = true;
        }
        return new ResultadoTurnoRobot(robot, carta, color, dijoUno, 0, penalizado);
    }

    private Jugador penalizadoPorUno() {
        return juego.huboPenalizacionUno() ? juego.getJugadorPenalizadoUno() : null;
    }

    public String elegirColorAutomatico(Jugador jugador) {
        return estrategiaPorDefecto.elegirColor(jugador);
    }
    public List<Jugador> getJugadores() {
        return juego.getJugadores();
    }

    public int cantidadJugadores() {
        return juego.cantidadJugadores();
    }

    public Jugador getJugadorActual() {
        return juego.getJugadorActual();
    }

    public Carta getCartaSuperior() {
        return juego.getCartaSuperior();
    }

    public String getColorActual() {
        return juego.getColorActual();
    }

    public int getPenalizacionAcumulada() {
        return juego.getPenalizacionAcumulada();
    }

    public int cartasEnMazo() {
        return juego.cartasEnMazo();
    }

    public int cartasEnPila() {
        return juego.cartasEnPila();
    }

    public int getNumeroRonda() {
        return juego.getNumeroRonda();
    }

    public boolean hayColorPendiente() {
        return juego.hayColorPendiente();
    }

    public boolean hayUnoPendiente() {
        return juego.hayUnoPendiente();
    }

    public Jugador getJugadorQueDebeDecirUno() {
        return juego.getJugadorQueDebeDecirUno();
    }

    public boolean huboPenalizacionUno() {
        return juego.huboPenalizacionUno();
    }

    public Jugador getJugadorPenalizadoUno() {
        return juego.getJugadorPenalizadoUno();
    }

    public boolean hayGanador() {
        return juego.hayGanador();
    }

    public Jugador getGanador() {
        return juego.getGanador();
    }

    public Jugador getGanadorPartida() {
        return juego.getGanadorPartida();
    }

    public int getPuntosGanadorRonda() {
        return juego.getPuntosGanadorRonda();
    }
}
