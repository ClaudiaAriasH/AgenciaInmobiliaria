package co.com.udem.agenciainmobiliaria.rest.contoller;

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
public class RegistrarUsuarioRestControllerTest {

	private String token;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private TipoIdentificacionRepository tipoIdentificacionRepository;

	@Autowired
	private ConvertTipoIdentificacion convertTipoIdentificacion;

	@LocalServerPort
	private int port;

	private AutenticationRequestDTO autenticationRequestDTO = new AutenticationRequestDTO();

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Before
	public void authorization() {
		autenticationRequestDTO.setUsername("39206924");
		autenticationRequestDTO.setPassword("mar123*");
		ResponseEntity<String> postResponse = restTemplate.postForEntity(getRootUrl() + "/auth/signin",
				autenticationRequestDTO, String.class);
		Gson g = new Gson();
		AutenticationResponseDTO autenticationResponseDTO = g.fromJson(postResponse.getBody(),
				AutenticationResponseDTO.class);
		token = autenticationResponseDTO.getToken();
	}

	@Test
	public void adicionarUsuarioTest() {
		String telefono = "5982252";
		RegistrarUsuarioDTO registrolDTO = this.setearRegistrarUsuarioDTO(telefono);
		adicionarTipoIdTest();
		ResponseEntity<RegistrarUsuarioDTO> postResponse = restTemplate.postForEntity(
				getRootUrl() + "/agenciaInmobiliaria/adicionarUsuario", registrolDTO, RegistrarUsuarioDTO.class);
		assertNotNull(postResponse);
		assertNotNull(postResponse.getBody());
		System.err.println("adicionarUsuarioTest-> " + postResponse);
	}

	@Test
	public void testObtenerUsuarios() {
		HttpEntity<String> entity = new HttpEntity<String>(null, this.construiHeaders());
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/agenciaInmobiliaria/usuarios",
				HttpMethod.GET, entity, String.class);
		assertNotNull(response.getBody());
		System.err.println("testObtenerUsuarios-> " + response);
	}

	@Test
	public void buscarUsuarioTest() {
		HttpEntity<String> entity = new HttpEntity<String>(null, this.construiHeaders());
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/agenciaInmobiliaria/usuarios/1",
				HttpMethod.GET, entity, String.class);
		assertNotNull(response.getBody());
		System.err.println("buscarUsuarioTest-> " + response);
	}

	@Test
	public void modificarUsuarioTest() {
		String telefono = "598225233";
		RegistrarUsuarioDTO registroDTO = this.setearRegistrarUsuarioDTO(telefono);
		int id = 1;
		HttpEntity<RegistrarUsuarioDTO> entity = new HttpEntity<RegistrarUsuarioDTO>(registroDTO,
				this.construiHeaders());
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/agenciaInmobiliaria/usuarios/" + id,
				HttpMethod.PUT, entity, String.class);
		assertNotNull(response.getBody());
		System.err.println("modificarUsuarioTest-> " + response);
	}

	@Test
	public void eliminarUsuarioTest() {
		int id = 1;
		HttpEntity<String> entity = new HttpEntity<String>(null, this.construiHeaders());
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/agenciaInmobiliaria/usuarios/" + id,
				HttpMethod.DELETE, entity, String.class);
		assertNotNull(response.getBody());
		System.err.println("eliminarUsuarioTest-> " + response);
	}

	private RegistrarUsuarioDTO setearRegistrarUsuarioDTO(String telefono) {
		RegistrarUsuarioDTO registrolDTO = new RegistrarUsuarioDTO();
		registrolDTO.setNumeroIdentificacion("39206924");
		registrolDTO.setNombres("Martha");
		registrolDTO.setApellidos("Ligia Hernandez");
		registrolDTO.setDireccion("Calle 63 # 67-07");
		registrolDTO.setTelefono(telefono);
		registrolDTO.setEmail("mar@hotmail.com");
		registrolDTO.setPassword("mar123*");
		TipoIdentificacionDTO tipoIdentificacionDTO = new TipoIdentificacionDTO();
		tipoIdentificacionDTO.setId(1L);
		registrolDTO.setTipoIdentificacionDTO(tipoIdentificacionDTO);

		return registrolDTO;
	}

	public void adicionarTipoIdTest() {
		TipoIdentificacionDTO tipoIdentificacionDTO = new TipoIdentificacionDTO();
		tipoIdentificacionDTO.setId(1L);
		tipoIdentificacionDTO.setTipoDocumento("PA");
		TipoIdentificacion tipoid;
		try {
			tipoid = convertTipoIdentificacion.convertToEntity(tipoIdentificacionDTO);
			tipoIdentificacionRepository.save(tipoid);
		} catch (Exception e) {

			System.err.println("No se pudo guardar registro en BD");
		}

	}

	private HttpHeaders construiHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + this.token);

		return headers;
	}

}
