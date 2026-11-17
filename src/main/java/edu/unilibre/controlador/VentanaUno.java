package edu.unilibre.controlador;

import edu.unilibre.facade.JuegoFacade;
import edu.unilibre.facade.ResultadoTurnoRobot;
import edu.unilibre.modelo.Carta;
import edu.unilibre.modelo.Jugador;
import edu.unilibre.modelo.ObservadorJuego;
import edu.unilibre.vista.AccionesTableroPanel;
import edu.unilibre.vista.InicioMenuPanel;
import edu.unilibre.vista.MenuPanel;
import edu.unilibre.vista.ResultadoPanel;
import edu.unilibre.vista.TableroPanel;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;

public class VentanaUno extends JFrame implements InicioMenuPanel, AccionesTableroPanel, ObservadorJuego {

    private static final int PAUSA_ROBOT_MS = 2000;

    private final JuegoFacade juego;
    private final MenuPanel menu;
    private final CardLayout cartas = new CardLayout();
    private final JPanel contenedor = new JPanel(cartas);

    private TableroPanel tablero;
    private ResultadoPanel resultado;
    private JPanel pantallaJuego;
    private Timer temporizadorRobot;
    private Jugador humano;
    private String nombreHumano;
    private int robotsActuales;

    public VentanaUno() {
        super("Juego - UNO - Software");
        this.juego = new JuegoFacade();
        this.juego.agregarObservador(this);
        this.menu = new MenuPanel(this, this::mostrarHistorial);

        contenedor.add(menu, "menu");
        contenedor.setPreferredSize(new Dimension(1050, 700));
        setContentPane(contenedor);
        cartas.show(contenedor, "menu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(880, 640));
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaUno().setVisible(true));
    }


    @Override
    public void iniciar(String nombreHumano, int cantidadRobots) {
        try {
            detenerTemporizador();
            tablero = null;
            juego.iniciarPartida(nombreHumano, cantidadRobots);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            menu.mostrarError(ex.getMessage());
            return;
        }
        this.nombreHumano = nombreHumano;
        this.robotsActuales = cantidadRobots;
        humano = juego.getJugadores().get(0);
        construirPantallaJuego();
        cartas.show(contenedor, "juego");
        programarTurnoRobot();
    }

    @Override
    public void jugar(Carta carta) {
        if (juego.hayGanador() || !tablero.esTurnoHumano()) {
            tablero.mostrarToast("Todavía no es tu turno", true);
            return;
        }
        StringBuilder mensaje = new StringBuilder();
        boolean error;
        try {
            juego.jugarCarta(carta);
            mensaje.append("Jugaste ").append(describir(carta));
            error = anotarPenalizacionUno(mensaje, penalizadoPorUno());
            if (juego.hayColorPendiente()) {
                String color = pedirColor();
                juego.cambiarColor(color);
                mensaje.append(" · color ").append(nombreColor(color));
            }
        } catch (IllegalArgumentException | IllegalStateException ex) {
            tablero.mostrarToast(ex.getMessage(), true);
            return;
        }
        tablero.mostrarToast(mensaje.toString(), error);
        despuesDeJugada();
    }

    @Override
    public void robar() {
        if (juego.hayGanador() || !tablero.esTurnoHumano()) {
            tablero.mostrarToast("Todavía no es tu turno", true);
            return;
        }
        int cantidad = Math.max(1, juego.getPenalizacionAcumulada());
        StringBuilder mensaje = new StringBuilder();
        boolean error;
        try {
            Carta robada = juego.robarCarta();
            if (cantidad == 1 && robada != null) {
                mensaje.append("Robaste: ").append(describir(robada));
            } else {
                mensaje.append("Robaste ").append(cantidad).append(" cartas");
            }
            error = anotarPenalizacionUno(mensaje, penalizadoPorUno());
        } catch (IllegalStateException ex) {
            tablero.mostrarToast(ex.getMessage(), true);
            return;
        }
        tablero.mostrarToast(mensaje.toString(), error);
        despuesDeJugada();
    }

    @Override
    public void decirUno() {
        if (tablero == null) {
            return;
        }
        try {
            juego.decirUno();
            tablero.mostrarToast("¡UNO!", false);
        } catch (IllegalStateException ex) {
            tablero.mostrarToast(ex.getMessage(), true);
        }
    }

    @Override
    public void botonFin() {
        if (!juego.hayGanador()) {
            return;
        }
        if (juego.getGanadorPartida() != null) {
            volverMenu();
            return;
        }
        juego.nuevaRonda();
        resultado.setVisible(false);
        tablero.reiniciarVista();
        programarTurnoRobot();
    }

    @Override
    public void volverMenu() {
        detenerTemporizador();
        cartas.show(contenedor, "menu");
    }

    private void continuarDesdeResultado() {
        if (juego.getGanadorPartida() != null) {
            iniciar(nombreHumano, robotsActuales);
        }
        else {
            botonFin();
        }
    }

    private void programarTurnoRobot() {
        detenerTemporizador();
        if (juego.hayGanador() || juego.getJugadorActual() == humano) {
            return;
        }
        temporizadorRobot = new Timer(PAUSA_ROBOT_MS, e -> jugarTurnoRobot());
        temporizadorRobot.setRepeats(false);
        temporizadorRobot.start();
    }

    private void jugarTurnoRobot() {
        if (juego.hayGanador()) {
            return;
        }
        Jugador robot = juego.getJugadorActual();
        if (robot == humano) {
            return;
        }
        String mensaje;
        boolean error;
        try {
            ResultadoTurnoRobot turno = juego.jugarTurnoRobot();
            mensaje = describirTurno(turno);
            error = turno.penalizadoPorUno() == humano;
        } catch (IllegalArgumentException | IllegalStateException ex) {
            mensaje = "Error en el turno de " + robot.getNombre() + ": " + ex.getMessage();
            error = true;
        }
        tablero.mostrarToast(mensaje, error);
        despuesDeJugada();
    }

    private String describirTurno(ResultadoTurnoRobot turno) {
        StringBuilder mensaje = new StringBuilder(turno.robot().getNombre());
        if (turno.jugoCarta()) {
            mensaje.append(" jugó ").append(describir(turno.cartaJugada()));
            anotarPenalizacionUno(mensaje, turno.penalizadoPorUno());
            if (turno.colorElegido() != null) {
                mensaje.append(" · color ").append(nombreColor(turno.colorElegido()));
            }
            if (turno.dijoUno()) {
                mensaje.append(" · ¡UNO!");
            }
        }
        else {
            mensaje.append(" robó ").append(turno.cartasRobadas())
                    .append(turno.cartasRobadas() == 1 ? " carta" : " cartas");
            anotarPenalizacionUno(mensaje, turno.penalizadoPorUno());
        }
        return mensaje.toString();
    }

    private void detenerTemporizador() {
        if (temporizadorRobot != null) {
            temporizadorRobot.stop();
        }
    }

    private void despuesDeJugada() {
        if (juego.hayGanador()) {
            terminarRonda();
        }
        else {
            programarTurnoRobot();
        }
    }

    private void terminarRonda() {
        detenerTemporizador();
        if (juego.getGanadorPartida() != null) {
            guardarHistorial();
        }
        resultado.mostrar(juego, humano);
    }

    public void guardarHistorial() {
        try {
            juego.guardarHistorial();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el historial: " + ex.getMessage(),
                    "Historial", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void mostrarHistorial() {
        JTextArea texto = new JTextArea(16, 56);
        texto.setEditable(false);
        texto.setText(juego.textoHistorial());
        texto.setCaretPosition(0);
        JOptionPane.showMessageDialog(this, new JScrollPane(texto), "Historial de partidas",
                JOptionPane.PLAIN_MESSAGE);
    }

    private Jugador penalizadoPorUno() {
        return juego.huboPenalizacionUno() ? juego.getJugadorPenalizadoUno() : null;
    }

    private boolean anotarPenalizacionUno(StringBuilder mensaje, Jugador olvidadizo) {
        if (olvidadizo == null) {
            return false;
        }
        mensaje.append(" · ").append(olvidadizo.getNombre()).append(" no dijo UNO y roba 2 cartas");
        return olvidadizo == humano;
    }

    private String pedirColor() {
        String[] opciones = {"ROJO", "AMARILLO", "VERDE", "AZUL"};
        int eleccion = JOptionPane.showOptionDialog(this, "Elige el nuevo color", "Cambio de color",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        return eleccion >= 0 ? opciones[eleccion] : juego.elegirColorAutomatico(humano);
    }

    private String nombreColor(String color) {
        return color.charAt(0) + color.substring(1).toLowerCase();
    }

    private String describir(Carta carta) {
        if (carta.esMasCuatro()) {
            return "Comodín +4";
        }
        if (carta.esCambioColor()) {
            return "Comodín de color";
        }
        String valor = carta.esMasDos() ? "+2" : carta.esSalta() ? "Salta"
                : carta.esReversa() ? "Reversa" : carta.getValor();
        return nombreColor(carta.getColor()) + " " + valor;
    }

    @Override
    public void actualizar(String evento) {
        if (tablero != null) {
            tablero.actualizar();
        }
    }

    private void construirPantallaJuego() {
        if (pantallaJuego != null) {
            contenedor.remove(pantallaJuego);
        }
        tablero = new TableroPanel(juego, humano, this);
        tablero.setAlignmentX(Component.CENTER_ALIGNMENT);
        tablero.setAlignmentY(Component.CENTER_ALIGNMENT);
        tablero.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        resultado = new ResultadoPanel(juego.cantidadJugadores(), this::volverMenu, this::continuarDesdeResultado);
        resultado.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultado.setAlignmentY(Component.CENTER_ALIGNMENT);
        resultado.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        pantallaJuego = new JPanel() {
            @Override
            public boolean isOptimizedDrawingEnabled() {
                return false;
            }
        };
        pantallaJuego.setLayout(new OverlayLayout(pantallaJuego));
        pantallaJuego.add(resultado);
        pantallaJuego.add(tablero);
        contenedor.add(pantallaJuego, "juego");
    }
}

