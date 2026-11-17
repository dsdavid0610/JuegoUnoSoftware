package edu.unilibre.vista;

import edu.unilibre.facade.JuegoFacade;
import edu.unilibre.modelo.Jugador;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.Comparator;
import java.util.List;

public class ResultadoPanel extends JPanel {

    private JuegoFacade juegoFacade;
    private final int cantidadJugadores;
    private final JLabel lblTitulo;
    private final JLabel lblMensaje;
    private final JLabel lblDetalle;
    private final JLabel lblTituloPuntos;
    private final JLabel[] etiquetas;
    private final JTextField[] campos;
    private final JButton btnSecundario;
    private final JButton btnContinuar;

    public ResultadoPanel(int cantidadJugadores, Runnable alVolverAlMenu, Runnable alContinuar) {
        super(new GridBagLayout());
        this.cantidadJugadores = cantidadJugadores;
        this.etiquetas = new JLabel[cantidadJugadores];
        this.campos = new JTextField[cantidadJugadores];
        setOpaque(false);
        setVisible(false);

        JPanel tarjeta = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                Dimension tam = super.getPreferredSize();
                tam.width = 480;
                return tam;
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 80));
                g2.fill(new RoundRectangle2D.Double(0, 10, getWidth(), getHeight() - 10, 34, 34));
                g2.setColor(new Color(12, 38, 29, 242));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight() - 8, 34, 34));
                g2.setColor(new Color(255, 255, 255, 45));
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 1, getHeight() - 9, 34, 34));
                g2.dispose();
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(22, 32, 30, 32));

        lblTitulo = texto("", new Color(246, 196, 32), Font.BOLD, 13);
        lblMensaje = texto("", Color.WHITE, Font.BOLD, 40);
        lblDetalle = texto("", new Color(190, 205, 198), Font.PLAIN, 15);
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(lblMensaje);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(lblDetalle);
        tarjeta.add(Box.createVerticalStrut(16));

        JPanel caja = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 70));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 18, 18));
                g2.setColor(new Color(255, 255, 255, 38));
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 2, getHeight() - 2, 18, 18));
                g2.dispose();
            }
        };
        caja.setOpaque(false);
        caja.setLayout(new BoxLayout(caja, BoxLayout.Y_AXIS));
        caja.setBorder(BorderFactory.createEmptyBorder(12, 18, 10, 18));

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        lblTituloPuntos = texto("PUNTUACIÓN FINAL", new Color(205, 220, 212), Font.BOLD, 12);
        encabezado.add(lblTituloPuntos, BorderLayout.WEST);
        encabezado.add(texto("Meta 250", new Color(246, 196, 32), Font.BOLD, 12), BorderLayout.EAST);
        caja.add(encabezado);
        caja.add(Box.createVerticalStrut(10));
        for (int i = 0; i < cantidadJugadores; i++) {
            etiquetas[i] = texto("", Color.WHITE, Font.PLAIN, 14);
            etiquetas[i].setIconTextGap(12);
            campos[i] = new JTextField("0", 4);
            campos[i].setEditable(false);
            campos[i].setOpaque(false);
            campos[i].setBorder(null);
            campos[i].setForeground(Color.WHITE);
            campos[i].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
            campos[i].setHorizontalAlignment(JTextField.RIGHT);
            JPanel fila = new JPanel(new BorderLayout());
            fila.setOpaque(false);
            fila.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
            fila.add(etiquetas[i], BorderLayout.WEST);
            fila.add(campos[i], BorderLayout.EAST);
            caja.add(fila);
        }
        caja.setAlignmentX(Component.CENTER_ALIGNMENT);
        caja.setMaximumSize(new Dimension(Integer.MAX_VALUE, caja.getPreferredSize().height));
        tarjeta.add(caja);
        tarjeta.add(Box.createVerticalStrut(18));

        btnSecundario = MenuPanel.crearBoton("Volver al menú", new Color(18, 52, 40), 15, true);
        btnContinuar = MenuPanel.crearBoton("Jugar de nuevo", new Color(229, 62, 52), 15, false);
        btnSecundario.addActionListener(e -> alVolverAlMenu.run());
        btnContinuar.addActionListener(e -> alContinuar.run());
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        botones.setOpaque(false);
        botones.add(btnSecundario);
        botones.add(btnContinuar);
        botones.setAlignmentX(Component.CENTER_ALIGNMENT);
        tarjeta.add(botones);

        GridBagConstraints posicion = new GridBagConstraints();
        posicion.insets = new Insets(90, 0, 0, 0);
        add(tarjeta, posicion);
    }

    public void mostrar(JuegoFacade juego, Jugador humano) {
        this.juegoFacade = juego;
        boolean fin = juego.getGanadorPartida() != null;
        Jugador ganador = fin ? juego.getGanadorPartida() : juego.getGanador();
        boolean humanoGano = ganador == humano;
        lblTituloPuntos.setText(fin ? "PUNTUACIÓN FINAL" : "PUNTUACIÓN");
        if (fin) {
            lblTitulo.setText(humanoGano ? "CAMPEONA" : "FIN DE LA PARTIDA");
            lblMensaje.setText(humanoGano ? "¡Victoria!" : "Derrota");
            lblDetalle.setText(humanoGano
                    ? "Terminaste la partida en " + rondas(juego.getNumeroRonda())
                    : ganador.getNombre() + " llegó a " + ganador.getPuntos() + " puntos en "
                    + rondas(juego.getNumeroRonda()));
            btnSecundario.setText("Volver al menú");
            btnContinuar.setText("Jugar de nuevo");
        } else {
            lblTitulo.setText("RONDA " + juego.getNumeroRonda());
            lblMensaje.setText("Fin de la ronda");
            lblDetalle.setText(ganador.getNombre() + " suma " + juego.getPuntosGanadorRonda() + " puntos");
            btnSecundario.setText("Menú");
            btnContinuar.setText("Siguiente ronda");
        }
        List<Jugador> jugadores = juego.getJugadores();
        List<Jugador> ordenados = jugadores.stream()
                .sorted(Comparator.comparingInt(Jugador::getPuntos).reversed()).toList();
        for (int i = 0; i < cantidadJugadores; i++) {
            Jugador jugador = ordenados.get(i);
            etiquetas[i].setIcon(punto(TableroPanel.colorJugador(jugadores.indexOf(jugador))));
            etiquetas[i].setText(jugador.getNombre());
            campos[i].setText(String.valueOf(jugador.getPuntos()));
        }
        setVisible(true);
        revalidate();
        repaint();
    }

    private static String rondas(int cantidad) {
        return cantidad + (cantidad == 1 ? " ronda" : " rondas");
    }

    private static JLabel texto(String texto, Color color, int estilo, int tamano) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(color);
        etiqueta.setFont(new Font(Font.SANS_SERIF, estilo, tamano));
        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        return etiqueta;
    }

    private static Icon punto(Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(x, y + 1, 10, 10);
                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return 10;
            }

            @Override
            public int getIconHeight() {
                return 12;
            }
        };
    }
}
