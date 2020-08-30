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
import co.com.udem.agenciainmobiliaria.dto.RegistrarUsuarioDTO;
import co.com.udem.agenciainmobiliaria.dto.TipoIdentificacionDTO;
import co.com.udem.agenciainmobiliaria.entities.TipoIdentificacion;
import co.com.udem.agenciainmobiliaria.repositories.TipoIdentificacionRepository;
import co.com.udem.agenciainmobiliaria.util.ConvertTipoIdentificacion;

@RunWith(SpringRunner.class)

@SpringBootTest(classes = AgenciainmobiliariaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TipoDocumentoRestControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private TipoIdentificacionRepository tipoIdentificacionRepository;

	@Autowired
	private ConvertTipoIdentificacion convertTipoIdentificacion;

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
		autenticationRequestDTO.setUsername("1037641034");
		autenticationRequestDTO.setPassword("claudia9528");
		ResponseEntity<String> response = restTemplate.postForEntity(getRootUrl() + "/auth/signin",
				autenticationRequestDTO, String.class);
		Gson g = new Gson();
		AutenticationResponseDTO autenticationResponseDTO = g.fromJson(response.getBody(),
				AutenticationResponseDTO.class);
		token = autenticationResponseDTO.getToken();
	}

	@Test
	public void adicionarTipoDocumentoTest() {

		TipoIdentificacionDTO tipoIdentificacionDTO = new TipoIdentificacionDTO();
		tipoIdentificacionDTO.setId(2L);
		tipoIdentificacionDTO.setTipoDocumento("CE");

		HttpEntity<TipoIdentificacionDTO> entity = new HttpEntity<TipoIdentificacionDTO>(tipoIdentificacionDTO,
				this.construiHeaders());
		ResponseEntity<String> reponse = restTemplate.exchange(
				getRootUrl() + "/agenciaInmobiliaria/adicionarTipoDocumento", HttpMethod.POST, entity, String.class);
		System.err.println("adicionarTipoDocumentoTest-> " + reponse);
		assertEquals(200, reponse.getStatusCode().value());

	}

	@Test
	public void buscarTipoDocumentoTest() {

		HttpEntity<String> entity = new HttpEntity<String>(null, this.construiHeaders());
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/agenciaInmobiliaria/tiposDocumentos/1",
				HttpMethod.GET, entity, String.class);
		System.err.println("buscarTipoDocumentoTest-> " + response);
		assertEquals(200, response.getStatusCode().value());
	}

	@Test
	public void listarTiposDocumentos() {

		HttpEntity<String> entity = new HttpEntity<String>(null, this.construiHeaders());
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/agenciaInmobiliaria/tiposDocumentos",
				HttpMethod.GET, entity, String.class);
		assertNotNull(response.getBody());
		System.err.println("listarTiposDocumentos-> " + response);
		assertEquals(200, response.getStatusCode().value());
	}

	@Test
	public void actualizarTipoDocumentoTest() {
		int id = 1;
		TipoIdentificacionDTO tipoIdentificacionDTO = new TipoIdentificacionDTO();
		tipoIdentificacionDTO.setId(1L);
		tipoIdentificacionDTO.setTipoDocumento("PA");

		HttpEntity<TipoIdentificacionDTO> entity = new HttpEntity<TipoIdentificacionDTO>(tipoIdentificacionDTO,
				this.construiHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				getRootUrl() + "/agenciaInmobiliaria/tiposDocumentos/" + id, HttpMethod.PUT, entity, String.class);
		System.err.println("actualizarTipoDocumentoTest-> " + response);
		assertEquals(200, response.getStatusCode().value());
	}

	@Test
	public void eliminarUsuarioTest() {
		int id = 2;

		HttpEntity<String> entity = new HttpEntity<String>(null, this.construiHeaders());
		ResponseEntity<String> response = restTemplate.exchange(
				getRootUrl() + "/agenciaInmobiliaria/tiposDocumentos/" + id, HttpMethod.DELETE, entity, String.class);
		System.err.println("eliminarUsuarioTest-> " + response);
		assertEquals(200, response.getStatusCode().value());
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

	public void adicionarUsuarioTest() {
		RegistrarUsuarioDTO registrolDTO = new RegistrarUsuarioDTO();
		adicionarTipoDocumentoInicial();
		registrolDTO.setNumeroIdentificacion("1037641034");
		registrolDTO.setNombres("Claudia");
		registrolDTO.setApellidos("Arias Hernandez");
		registrolDTO.setDireccion("Calle 62");
		registrolDTO.setTelefono("5982252");
		registrolDTO.setEmail("claudia-ah@hotmail.com");
		registrolDTO.setPassword("claudia9528");
		TipoIdentificacionDTO tipoIdentificacionDTO = new TipoIdentificacionDTO();
		tipoIdentificacionDTO.setId(1L);
		registrolDTO.setTipoIdentificacionDTO(tipoIdentificacionDTO);
		ResponseEntity<RegistrarUsuarioDTO> response = restTemplate.postForEntity(
				getRootUrl() + "/agenciaInmobiliaria/adicionarUsuario", registrolDTO, RegistrarUsuarioDTO.class);
		assertNotNull(response);
		assertNotNull(response.getBody());

	}

	private HttpHeaders construiHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + this.token);

		return headers;
	}

}