package edu.unilibre.controlador;
import edu.unilibre.modelo.ObservadorJuego;
import org.junit.jupiter.api.Test;
import javax.swing.JFrame;
import static org.junit.jupiter.api.Assertions.*;

class VentanaUnoTest {
    @Test void ventanaIntegraVistaSwingYObserverSinInstanciarLaVentana() {
        assertTrue(JFrame.class.isAssignableFrom(VentanaUno.class));
        assertTrue(ObservadorJuego.class.isAssignableFrom(VentanaUno.class));
    }
}
