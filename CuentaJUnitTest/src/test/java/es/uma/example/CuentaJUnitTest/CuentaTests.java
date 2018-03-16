package es.uma.example.CuentaJUnitTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CuentaTests {

	private static final double EPSILON = 1e-6;
	private static final double SALDO_INICIAL = 0;
	private Cuenta cuenta;

	@Before
	public void setUp() {
		cuenta = new Cuenta(SALDO_INICIAL);
	}

	@After
	public void tearDown() {
		cuenta = null;
	}

	// pruebas de constructor

	@Test
	public void crearConSaldoInicialPositivo() {
		final double inicial = 100;
		Cuenta cuentaLocal = new Cuenta(inicial);
		assertEquals("saldo inicial positivo incorrecto", inicial, cuentaLocal.saldo(), EPSILON);
	}

	@Test
	public void crearConSaldoInicialCero() {
		Cuenta cuentaLocal = new Cuenta(0);
		assertEquals("saldo inicial cero incorrecto", 0, cuentaLocal.saldo(), EPSILON);
	}

	@Test
	public void crearConSaldoInicialNegativo() {
		final double inicial = -100;
		Cuenta cuentaLocal = new Cuenta(inicial);
		assertEquals("saldo inicial negativo incorrecto", 0, cuentaLocal.saldo(), EPSILON);
	}

	// pruebas de ingresa

	@Test
	public void ingresoPositivoConSaldoCero() {
		final double ingreso = 100;
		cuenta.ingresa(ingreso);
		assertEquals("ingreso positivo incorrecto", ingreso, cuenta.saldo(), EPSILON);
	}

	@Test
	public void ingresoPositivoConSaldoPositivo() {
		final double inicial = 10;
		final double ingreso = 100;
		Cuenta cuentaLocal = new Cuenta(inicial);
		cuentaLocal.ingresa(ingreso);
		assertEquals("ingreso positivo incorrecto", inicial + ingreso, cuentaLocal.saldo(), EPSILON);
	}

	// pruebas de ingresa que elevan excepciones

	@Test(expected = IngresoNegativoException.class)
	public void ingresoNegativoElevaExcepcion() {
		cuenta.ingresa(-100);
	}

	@Test
	public void ingresoNegativoElevaExcepcionNoModificaSaldo() {
		try {
			cuenta.ingresa(-100);
			fail("se debió elevar una excepción");
		} catch (Exception e) {
			assertEquals(0, cuenta.saldo(), EPSILON);
			assertTrue(e instanceof IngresoNegativoException);
		}
	}

	@Rule
	public ExpectedException esperada = ExpectedException.none();

	@Test
	public void ingresoNegativoElevaExcepcionConMensajeAdecuado() {
		final double ingreso = -175.22;
		esperada.expect(IngresoNegativoException.class);
		esperada.expectMessage("ingreso negativo: " + ingreso);
		cuenta.ingresa(ingreso);
	}

	// pruebas de extrae

	@Test
	public void reintegroMenorQueSaldo() {
		final double inicial = 100;
		final double reintegro = 10;
		Cuenta cuentaLocal = new Cuenta(inicial);
		cuentaLocal.extrae(reintegro);
		assertEquals(inicial - reintegro, cuentaLocal.saldo(), EPSILON);
	}

	@Test
	public void reintegroIgualQueSaldo() {
		final double inicial = 100;
		Cuenta cuentaLocal = new Cuenta(inicial);
		cuentaLocal.extrae(inicial);
		assertEquals(0, cuentaLocal.saldo(), EPSILON);
	}

	@Test
	public void reintegroMayorQueSaldo() {
		final double inicial = 100;
		final double reintegro = inicial + 10;
		Cuenta cuentaLocal = new Cuenta(inicial);
		cuentaLocal.extrae(reintegro);
		assertEquals(0, cuentaLocal.saldo(), EPSILON);
	}

	@Test
	public void crearDosCuentasDistintas() {
		Cuenta otraCuenta = new Cuenta(500);
		assertNotSame("solo existe una cuenta", cuenta, otraCuenta);
	}

}
