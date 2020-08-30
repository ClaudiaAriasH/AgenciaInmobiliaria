package co.com.udem.agenciainmobiliariaclient.rest.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import co.com.udem.agenciainmobiliariaclient.domain.AutenticationRequestDTO;
import co.com.udem.agenciainmobiliariaclient.domain.AutenticationResponseDTO;
import co.com.udem.agenciainmobiliariaclient.domain.RegistrarUsuarioDTO;
import co.com.udem.agenciainmobiliariaclient.entities.UserToken;
import co.com.udem.agenciainmobiliariaclient.repositories.UserTokenRepository;
import co.com.udem.agenciainmobiliariaclient.util.Constantes;
import co.com.udem.agenciainmobiliariaclient.util.MapearRespuesta;

@RestController
public class RegistrarUsuarioClientRestController {

	private static final String USUARIOS = "usuarios/";
	private static final Logger logger = LogManager.getLogger(RegistrarUsuarioClientRestController.class);
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	UserTokenRepository userTokenRepository;

	@Autowired
	UserToken userToken;
	@Autowired
	private LoadBalancerClient loadBalancer;

	@Value("${url.servicio}")
	public String url;

	@PostMapping("/autenticar")
	public String autenticar(@RequestBody AutenticationRequestDTO autenticationRequestDTO) {

		ServiceInstance serviceInstance = loadBalancer.choose("agenciainmobiliaria");
		logger.info(serviceInstance.getUri());
		String baseUrl = serviceInstance.getUri().toString();
		baseUrl = baseUrl + "/auth/signin";
		ResponseEntity<String> postResponse = restTemplate.postForEntity(baseUrl, autenticationRequestDTO,
				String.class);
		logger.info("Respuesta Token: ");
		logger.info(postResponse.getBody());
		Gson g = new Gson();
		AutenticationResponseDTO autenticationResponseDTO = g.fromJson(postResponse.getBody(),
				AutenticationResponseDTO.class);

		userToken.setUsername(autenticationResponseDTO.getUsername());

		userToken.setToken(autenticationResponseDTO.getToken());

		Optional<UserToken> usuario = userTokenRepository.findByUsername(autenticationResponseDTO.getUsername());
		if (usuario.isPresent()) {
			UserToken infoUsuario = usuario.get();
			infoUsuario.setToken(userToken.getToken());
			userTokenRepository.save(infoUsuario);

		} else {
			userTokenRepository.save(userToken);
		}

		logger.info("Respuesta Token: ");

		return autenticationResponseDTO.getToken();

	}

	@GetMapping("/consultarUsuarios")
	public ResponseEntity<Object> listarUsuarios() {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(Constantes.AUTHORIZATION, Constantes.BEARER + userToken.getToken());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url + "usuarios", HttpMethod.GET, entity,
					String.class);

			return MapearRespuesta.mapearRespuestaExitosa(response);

		} catch (HttpStatusCodeException e) {
			return MapearRespuesta.mapearRespuestaErrores(e);
		}
	}

	@GetMapping("/consultarUsuario/{id}")
	public ResponseEntity<Object> buscarUsuario(@PathVariable String id) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		headers.set(Constantes.AUTHORIZATION, Constantes.BEARER + userToken.getToken());

		HttpEntity<String> entity = new HttpEntity<>(headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url + USUARIOS + id, HttpMethod.GET, entity,
					String.class);

			return MapearRespuesta.mapearRespuestaExitosa(response);

		} catch (HttpStatusCodeException e) {
			return MapearRespuesta.mapearRespuestaErrores(e);
		}
	}

	@DeleteMapping("/eliminarUsuario/{id}")
	public ResponseEntity<Object> eliminarUsuario(@PathVariable String id) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		headers.set(Constantes.AUTHORIZATION, Constantes.BEARER + userToken.getToken());

		HttpEntity<String> entity = new HttpEntity<>(headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url + USUARIOS + id, HttpMethod.DELETE, entity,
					String.class);

			return MapearRespuesta.mapearRespuestaExitosa(response);

		} catch (HttpStatusCodeException e) {
			return MapearRespuesta.mapearRespuestaErrores(e);
		}
	}

	@PostMapping("/registrarUsuario")
	public ResponseEntity<Object> registrarUsuario(@RequestBody RegistrarUsuarioDTO registrarUsuarioDTO) {

		try {
			ResponseEntity<String> response = restTemplate.postForEntity(url + "adicionarUsuario", registrarUsuarioDTO,
					String.class);
			return MapearRespuesta.mapearRespuestaExitosa(response);
		} catch (HttpStatusCodeException e) {

			return MapearRespuesta.mapearRespuestaErrores(e);

		}
	}

	@PutMapping("/actualizarUsuario/{id}")
	public ResponseEntity<Object> updateUsuario(@RequestBody RegistrarUsuarioDTO registrarUsuarioDTO,
			@PathVariable Long id) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String servicio = USUARIOS;

		headers.set(Constantes.AUTHORIZATION, Constantes.BEARER + userToken.getToken());

		HttpEntity<RegistrarUsuarioDTO> entity = new HttpEntity<>(registrarUsuarioDTO, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url + servicio + id, HttpMethod.PUT, entity,
					String.class);
			return MapearRespuesta.mapearRespuestaExitosa(response);
		} catch (HttpStatusCodeException e) {

			return MapearRespuesta.mapearRespuestaErrores(e);

		}
	}

}
