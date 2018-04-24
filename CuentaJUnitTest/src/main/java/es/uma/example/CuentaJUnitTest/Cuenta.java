package es.uma.example.CuentaJUnitTest;

// Esta es la versión depurada mediante tests JUnit

public class Cuenta {
    private double saldo;

    public Cuenta(double si) {
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
