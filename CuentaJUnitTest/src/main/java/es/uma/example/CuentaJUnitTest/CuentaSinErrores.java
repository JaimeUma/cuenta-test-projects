package es.uma.example.CuentaJUnitTest;

// Esta es la versión sin errores

public class CuentaSinErrores {
    private double saldo;

    public CuentaSinErrores(double si) {
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
