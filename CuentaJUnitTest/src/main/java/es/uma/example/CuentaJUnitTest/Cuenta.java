package es.uma.example.CuentaJUnitTest;

// Esta es la versión depurada mediante tests JUnit

public class Cuenta {
    private double saldo;

    public Cuenta(double si) {
        saldo = Math.max(0, si);
    }

    public void ingresa(double cantidad) {
        if (cantidad < 0) {
            throw new IngresoNegativoException("ingreso negativo: " + cantidad);
        }
        saldo += cantidad;
    }

    public double extrae(double cantidad) {
        final double extraido = Math.min(saldo, cantidad);
        saldo -= extraido;
        return extraido;
    }

    public double saldo() {
        return saldo;
    }
}
