package servicioLoginTest;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import servicioLogin.ExcepcionCuentaEnUso;
import servicioLogin.ExcepcionUsuarioDesconocido;
import servicioLogin.GestorLogin;
import servicioLogin.ICuenta;
import servicioLogin.IRepositorioCuentas;

public class GestorLoginTestWithoutAnnotations {

	IRepositorioCuentas repo;
	ICuenta cuenta;

	@Before
	public void inicializarTest() {
		repo = mock(IRepositorioCuentas.class);
		cuenta = mock(ICuenta.class);
		// definimos comportamiento del mock
		when(repo.buscar("pepe")).thenReturn(cuenta);
	}

	@Test
	public void testAccesoConcedidoALaPrimera() {
		when(cuenta.claveCorrecta("1234")).thenReturn(true);

		GestorLogin login = new GestorLogin(repo);
		login.acceder("pepe", "1234");
		// verifica que el metodo entrarCuenta ha sido llamado
		verify(cuenta).entrarCuenta();
	}

	@Test
	public void testAccesoDenegadoALaPrimera() {
		when(cuenta.claveCorrecta("1234")).thenReturn(false);

		GestorLogin login = new GestorLogin(repo);
		login.acceder("pepe", "1234");
		// verificar que el metodo entrarCuenta nunca es llamado
		verify(cuenta, never()).entrarCuenta();
	}

	// verificar que la ejecución de este test producirá una excepción del tipo indicado
	@Test(expected = ExcepcionUsuarioDesconocido.class)
	public void testUsuarioIncorrecto() {
		when(repo.buscar("bartolo")).thenReturn(null);

		GestorLogin login = new GestorLogin(repo);
		login.acceder("bartolo", "1234");
	}

	@Test
	public void testBloquearTrasTresIntentos() {
		when(cuenta.claveCorrecta("1234")).thenReturn(false);

		GestorLogin login = new GestorLogin(repo);
		login.acceder("pepe", "1234");
		login.acceder("pepe", "1234");
		login.acceder("pepe", "1234");

		// verificar que no se llama a entrarCuenta y si se llama una vez a bloquearCuenta
		verify(cuenta, never()).entrarCuenta();
		verify(cuenta).bloquearCuenta();
	}

	@Test
	public void testAccederTrasDosIntentos() {
		when(cuenta.claveCorrecta(anyString())).thenReturn(false);
		when(cuenta.claveCorrecta("1234")).thenReturn(true);

		GestorLogin login = new GestorLogin(repo);
		login.acceder("pepe", "1243");
		login.acceder("pepe", "1224");
		login.acceder("pepe", "1234");

		// verificar que se llama a entrarCuenta y no se llama a bloquearCuenta
		verify(cuenta).entrarCuenta();
		verify(cuenta, never()).bloquearCuenta();
	}

	@Test
	public void testAccederTrasUnIntento() {
		when(cuenta.claveCorrecta(anyString())).thenReturn(false);
		when(cuenta.claveCorrecta("1234")).thenReturn(true);

		GestorLogin login = new GestorLogin(repo);
		login.acceder("pepe", "1243");
		login.acceder("pepe", "1234");

		verify(cuenta).entrarCuenta();
		verify(cuenta, never()).bloquearCuenta();
	}

	@Test
	public void testBloquearTrasCuatroIntentos() {
		when(cuenta.claveCorrecta("1234")).thenReturn(false);

		GestorLogin login = new GestorLogin(repo);
		login.acceder("pepe", "1234");
		login.acceder("pepe", "1234");
		login.acceder("pepe", "1234");
		login.acceder("pepe", "1234");

		verify(cuenta, never()).entrarCuenta();
		verify(cuenta).bloquearCuenta();
	}

	@Test
	public void testAccederTrasBloqueoConOtroUsuario() {
		ICuenta otraCuenta = mock(ICuenta.class);
		when(cuenta.claveCorrecta("1234")).thenReturn(false);
		when(repo.buscar("jose")).thenReturn(otraCuenta);
		when(otraCuenta.claveCorrecta("1234")).thenReturn(true);

		GestorLogin login = new GestorLogin(repo);
		login.acceder("pepe", "1234");
		login.acceder("pepe", "1234");
		login.acceder("pepe", "1234");
		login.acceder("jose", "1234");

		verify(otraCuenta).entrarCuenta();
		verify(otraCuenta, never()).bloquearCuenta();
		verify(cuenta, never()).entrarCuenta();
		verify(cuenta).bloquearCuenta();
	}

	@Test
	public void testAccesoDenegadoCuentaBloqueada() {
		when(cuenta.claveCorrecta("1234")).thenReturn(true);
		when(cuenta.estaBloqueada()).thenReturn(true);

		GestorLogin login = new GestorLogin(repo);
		login.acceder("pepe", "1234");

		verify(cuenta, never()).entrarCuenta();
	}

	@Test(expected = ExcepcionCuentaEnUso.class)
	public void testCuentaEnUso() {
		when(cuenta.claveCorrecta("1234")).thenReturn(true);
		when(cuenta.estaEnUso()).thenReturn(true);

		GestorLogin login = new GestorLogin(repo);
		login.acceder("pepe", "1234");
	}
}
