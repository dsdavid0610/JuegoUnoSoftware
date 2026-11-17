package edu.unilibre.modelo;
public class RobotAgresivo extends RobotEstandar {

    @Override
    protected int prioridad(Carta carta) {
        if (carta.esMasDos()) {
            return 4;
        }
        if (carta.esSalta()) {
            return 3;
        }
        if (carta.esReversa()) {
            return 2;
        }
        return super.prioridad(carta);
    }
}
