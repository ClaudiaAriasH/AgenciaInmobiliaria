
package co.com.udem.agenciainmobiliaria.rest.contoller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;

import co.com.udem.agenciainmobiliaria.AgenciainmobiliariaApplication;
import co.com.udem.agenciainmobiliaria.dto.AutenticationRequestDTO;
import co.com.udem.agenciainmobiliaria.dto.AutenticationResponseDTO;
import co.com.udem.agenciainmobiliaria.dto.PropiedadDTO;
import co.com.udem.agenciainmobiliaria.dto.RegistrarUsuarioDTO;
import co.com.udem.agenciainmobiliaria.dto.TipoIdentificacionDTO;
import co.com.udem.agenciainmobiliaria.entities.TipoIdentificacion;
import co.com.udem.agenciainmobiliaria.repositories.TipoIdentificacionRepository;
import co.com.udem.agenciainmobiliaria.util.ConvertTipoIdentificacion;

@RunWith(SpringRunner.class)

@SpringBootTest(classes = AgenciainmobiliariaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PropiedadRestControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ConvertTipoIdentificacion convertTipoIdentificacion;

	@Autowired
	private TipoIdentificacionRepository tipoIdentificacionRepository;

	@LocalServerPort
	private int port;

	private String token;

	private AutenticationRequestDTO autenticationRequestDTO = new AutenticationRequestDTO();

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Before
	public void authorization() {
		adicionarUsuarioTest();
		autenticationRequestDTO.setUsername("1028456620");
		autenticationRequestDTO.setPassword("cam123*");
		ResponseEntity<String> response = restTemplate.postForEntity(getRootUrl() + "/auth/signin",
				autenticationRequestDTO, String.class);
		Gson g = new Gson();
		AutenticationResponseDTO autenticationResponseDTO = g.fromJson(response.getBody(),
				AutenticationResponseDTO.class);
		token = autenticationResponseDTO.getToken();
	}

	@Test
	public void adicionarPropiedadTest() {

		PropiedadDTO propiedadDTO = new PropiedadDTO();

		propiedadDTO.setArea("45m2");
		propiedadDTO.setNumeroBanos(2);
		propiedadDTO.setNumeroHabitaciones(3);
		propiedadDTO.setTipoPropiedad("Arriendo");
		propiedadDTO.setValor(120000);

		RegistrarUsuarioDTO registroDTO = new RegistrarUsuarioDTO();
		registroDTO.setId(1L);
		propiedadDTO.setRegistrarUsuarioDTO(registroDTO);

		HttpEntity<PropiedadDTO> entity = new HttpEntity<PropiedadDTO>(propiedadDTO, this.construiHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				getRootUrl() + "/agenciaInmobiliaria/adicionarPropiedad", HttpMethod.POST, entity, String.class);
		System.err.println("adicionarPropiedadTest-> " + response);
		assertEquals(200, response.getStatusCode().value());

	}

	@Test
	public void buscarPropiedadTest() {

		HttpEntity<String> entity = new HttpEntity<String>(null, this.construiHeaders());
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/agenciaInmobiliaria/propiedad/1",
				HttpMethod.GET, entity, String.class);
		System.err.println("buscarPropiedadTest-> " + response);
		assertNotNull(response.getBody());
	}

	@Test
	public void listarPropiedades() {

		HttpEntity<String> entity = new HttpEntity<String>(null, this.construiHeaders());
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/agenciaInmobiliaria/propiedades",
				HttpMethod.GET, entity, String.class);
		assertNotNull(response.getBody());
		System.err.println("listarPropiedades-> " + response);
		assertEquals(200, response.getStatusCode().value());
	}

	@Test
	public void actualizarPropiedadesTest() {
		int id = 1;
		PropiedadDTO propiedadDTO = new PropiedadDTO();
		propiedadDTO.setArea("45m2");
		propiedadDTO.setNumeroBanos(2);
		propiedadDTO.setNumeroHabitaciones(3);
		propiedadDTO.setTipoPropiedad("Arriendo");
		propiedadDTO.setValor(120000);

		RegistrarUsuarioDTO registroDTO = new RegistrarUsuarioDTO();
		registroDTO.setId(1L);
		propiedadDTO.setRegistrarUsuarioDTO(registroDTO);

		HttpEntity<PropiedadDTO> entity = new HttpEntity<PropiedadDTO>(propiedadDTO, this.construiHeaders());
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/agenciaInmobiliaria/propiedad/" + id,
				HttpMethod.PUT, entity, String.class);
		System.err.println("actualizarPropiedadesTest-> " + response);
		assertNotNull(response.getBody());
	}

	@Test
	public void eliminarPropiedadTest() {
		int id = 1;

		HttpEntity<String> entity = new HttpEntity<String>(null, this.construiHeaders());
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/agenciaInmobiliaria/propiedad/" + id,
				HttpMethod.DELETE, entity, String.class);
		System.err.println("eliminarPropiedadTest-> " + response);
		assertNotNull(response);
		assertNotNull(response.getBody());
	}

	@Test
	public void filtrarPorHabitaciones() {

		HttpEntity<String> entity = new HttpEntity<String>(null, this.construiHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				getRootUrl() + "/agenciaInmobiliaria/propiedades/filtros/?area=65m2", HttpMethod.GET, entity,
				String.class);
		System.out.println("filtrarPorHabitaciones-> " + response);
		assertNotNull(response);
		assertNotNull(response.getBody());
	}

	public void adicionarUsuarioTest() {
		RegistrarUsuarioDTO registrolDTO = new RegistrarUsuarioDTO();
		adicionarTipoDocumentoInicial();
		registrolDTO.setNumeroIdentificacion("1028456620");
		registrolDTO.setNombres("Camilo");
		registrolDTO.setApellidos("Velasquez");
		registrolDTO.setDireccion("Carrera 24");
		registrolDTO.setTelefono("5983452");
		registrolDTO.setEmail("cam93@hotmail.com");
		registrolDTO.setPassword("cam123*");
		TipoIdentificacionDTO tipoIdentificacionDTO = new TipoIdentificacionDTO();
		tipoIdentificacionDTO.setId(1L);
		registrolDTO.setTipoIdentificacionDTO(tipoIdentificacionDTO);
		ResponseEntity<RegistrarUsuarioDTO> response = restTemplate.postForEntity(
				getRootUrl() + "/agenciaInmobiliaria/adicionarUsuario", registrolDTO, RegistrarUsuarioDTO.class);
		assertNotNull(response);
		assertNotNull(response.getBody());

	}

	public void adicionarTipoDocumentoInicial() {
		TipoIdentificacionDTO tipoIdentificacionDTO = new TipoIdentificacionDTO();
		tipoIdentificacionDTO.setId(1L);
		tipoIdentificacionDTO.setTipoDocumento("CC");
		TipoIdentificacion tipoIdentificacion;
		try {
			tipoIdentificacion = convertTipoIdentificacion.convertToEntity(tipoIdentificacionDTO);
			tipoIdentificacionRepository.save(tipoIdentificacion);
		} catch (Exception e) {

			System.err.println("Fallo al guardar el tipo de documento.");
		}

	}

	private HttpHeaders construiHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + this.token);

		return headers;
	}

}