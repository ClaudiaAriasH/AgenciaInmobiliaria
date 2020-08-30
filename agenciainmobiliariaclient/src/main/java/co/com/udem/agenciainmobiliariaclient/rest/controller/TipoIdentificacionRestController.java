package co.com.udem.agenciainmobiliariaclient.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import co.com.udem.agenciainmobiliariaclient.domain.TipoIdentificacionDTO;
import co.com.udem.agenciainmobiliariaclient.entities.UserToken;
import co.com.udem.agenciainmobiliariaclient.repositories.UserTokenRepository;
import co.com.udem.agenciainmobiliariaclient.util.Constantes;
import co.com.udem.agenciainmobiliariaclient.util.MapearRespuesta;

@RestController
public class TipoIdentificacionRestController {

	private static final String TIPOS_DOCUMENTOS = "tiposDocumentos/";

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	UserTokenRepository userTokenRepository;

	@Autowired
	UserToken userToken;

	@Value("${url.servicio}")
	public String url;

	@GetMapping("/consultarTipoDocumentos")
	public ResponseEntity<Object> listarTiposDocumentos() {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(Constantes.AUTHORIZATION, Constantes.BEARER + userToken.getToken());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url + "tiposDocumentos", HttpMethod.GET, entity,
					String.class);

			return MapearRespuesta.mapearRespuestaExitosa(response);

		} catch (HttpStatusCodeException e) {
			return MapearRespuesta.mapearRespuestaErrores(e);
		}
	}

	@GetMapping("/consultarTipoDocumento/{id}")
	public ResponseEntity<Object> buscarTipoDocumento(@PathVariable String id) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		headers.set(Constantes.AUTHORIZATION, Constantes.BEARER + userToken.getToken());

		HttpEntity<String> entity = new HttpEntity<>(headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url + TIPOS_DOCUMENTOS + id, HttpMethod.GET, entity,
					String.class);

			return MapearRespuesta.mapearRespuestaExitosa(response);
		} catch (HttpStatusCodeException e) {
			return MapearRespuesta.mapearRespuestaErrores(e);
		}
	}

	@DeleteMapping("/eliminarTipoDocumento/{id}")
	public ResponseEntity<Object> eliminarTipoDocumento(@PathVariable String id) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		headers.set(Constantes.AUTHORIZATION, Constantes.BEARER + userToken.getToken());

		HttpEntity<String> entity = new HttpEntity<>(headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url + TIPOS_DOCUMENTOS + id, HttpMethod.DELETE,
					entity, String.class);

			return MapearRespuesta.mapearRespuestaExitosa(response);

		} catch (HttpStatusCodeException e) {
			return MapearRespuesta.mapearRespuestaErrores(e);
		}
	}

	@PostMapping("/registrarTipoDocumento")
	public ResponseEntity<Object> registrarTipoDocumento(@RequestBody TipoIdentificacionDTO tipoIdentificacionDTO) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		headers.set(Constantes.AUTHORIZATION, Constantes.BEARER + userToken.getToken());

		HttpEntity<TipoIdentificacionDTO> entity = new HttpEntity<>(tipoIdentificacionDTO, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url + "adicionarTipoDocumento", HttpMethod.POST,
					entity, String.class);
			return MapearRespuesta.mapearRespuestaExitosa(response);
		} catch (HttpStatusCodeException e) {

			return MapearRespuesta.mapearRespuestaErrores(e);

		}
	}

	@PutMapping("/actualizarTipoDocumento/{id}")
	public ResponseEntity<Object> updateTipoDocumento(@RequestBody TipoIdentificacionDTO tipoIdentificacionDTO,
			@PathVariable Long id) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		headers.set(Constantes.AUTHORIZATION, Constantes.BEARER + userToken.getToken());

		HttpEntity<TipoIdentificacionDTO> entity = new HttpEntity<>(tipoIdentificacionDTO, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(url + TIPOS_DOCUMENTOS + id, HttpMethod.PUT, entity,
					String.class);
			return MapearRespuesta.mapearRespuestaExitosa(response);
		} catch (HttpStatusCodeException e) {

			return MapearRespuesta.mapearRespuestaErrores(e);

		}
	}

}
