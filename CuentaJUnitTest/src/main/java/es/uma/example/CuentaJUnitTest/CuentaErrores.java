package es.uma.example.CuentaJUnitTest;

// Esta es la versión original con errores que se distribuye a los alumnos

public class CuentaErrores {
    private double saldo;

    public CuentaErrores(double si) {
        saldo = si;
    }

    public void ingresa(double cantidad) {
        saldo += cantidad;
    }

    public double extrae(double cantidad) {
        saldo -= cantidad;
        return cantidad;
    }

    public double saldo() {
        return saldo;
    }
}
