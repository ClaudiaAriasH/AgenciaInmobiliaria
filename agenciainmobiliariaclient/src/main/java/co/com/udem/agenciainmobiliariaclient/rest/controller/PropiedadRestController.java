package co.com.udem.agenciainmobiliariaclient.rest.controller;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import co.com.udem.agenciainmobiliariaclient.domain.PropiedadDTO;
import co.com.udem.agenciainmobiliariaclient.entities.UserToken;
import co.com.udem.agenciainmobiliariaclient.repositories.UserTokenRepository;
import co.com.udem.agenciainmobiliariaclient.util.Balanceador;
import co.com.udem.agenciainmobiliariaclient.util.Constantes;
import co.com.udem.agenciainmobiliariaclient.util.MapearRespuesta;

@RestController
public class PropiedadRestController {

	private static final String PROPIEDAD = "/agenciaInmobiliaria/propiedad/";

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	Balanceador balanceador;
	
	@Autowired
	UserTokenRepository userTokenRepository;

	@Autowired
	UserToken userToken;

	@GetMapping("/consultarPropiedades")
	public ResponseEntity<Object> listarPropiedades() {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(Constantes.AUTHORIZATION, Constantes.BEARER + userToken.getToken());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(balanceador.urlBalanceador() + "/agenciaInmobiliaria/propiedades",
					HttpMethod.GET, entity, String.class);

			return MapearRespuesta.mapearRespuestaExitosa(response);

		} catch (HttpStatusCodeException e) {
			return MapearRespuesta.mapearRespuestaErrores(e);
		}
	}

	@GetMapping("/filtrarPropiedades")
	public ResponseEntity<Object> filtrarPropiedades(@RequestParam(value = "area", required = false) String area,
			@RequestParam(value = "numeroHabitaciones", required = false) Integer numeroHabitaciones,
			@RequestParam(value = "precioIni", required = false) BigInteger precioIni,
			@RequestParam(value = "precioFinal", required = false) BigInteger precioFinal) {

		String urlServices = balanceador.urlBalanceador() + "/agenciaInmobiliaria/propiedades/filtros/";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(Constantes.AUTHORIZATION, Constantes.BEARER + userToken.getToken());
		HttpEntity<String> entity = new HttpEntity<>(headers);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlServices).queryParam("area", area)
				.queryParam("numeroHabitaciones", numeroHabitaciones).queryParam("precioIni", precioIni)
				.queryParam("precioFinal", precioFinal);

		try {

			ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
					String.class);
			return MapearRespuesta.mapearRespuestaExitosa(response);

		} catch (HttpStatusCodeException e) {
			return MapearRespuesta.mapearRespuestaErrores(e);
		}
	}

	@GetMapping("/consultarPropiedad/{id}")
	public ResponseEntity<Object> buscarPropiedad(@PathVariable String id) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		headers.set(Constantes.AUTHORIZATION, Constantes.BEARER + userToken.getToken());

		HttpEntity<String> entity = new HttpEntity<>(headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(balanceador.urlBalanceador() + PROPIEDAD + id,
					HttpMethod.GET, entity, String.class);
			return MapearRespuesta.mapearRespuestaExitosa(response);

		} catch (HttpStatusCodeException e) {
			return MapearRespuesta.mapearRespuestaErrores(e);
		}
	}

	@DeleteMapping("/eliminarPropiedad/{id}")
	public ResponseEntity<Object> eliminarPropiedad(@PathVariable String id) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		headers.set(Constantes.AUTHORIZATION, Constantes.BEARER + userToken.getToken());

		HttpEntity<String> entity = new HttpEntity<>(headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(balanceador.urlBalanceador() + PROPIEDAD + id,
					HttpMethod.DELETE, entity, String.class);

			return MapearRespuesta.mapearRespuestaExitosa(response);

		} catch (HttpStatusCodeException e) {
			return MapearRespuesta.mapearRespuestaErrores(e);
		}
	}

	@PostMapping("/registrarPropiedad")
	public ResponseEntity<Object> registrarPropiedad(@RequestBody PropiedadDTO propiedadDTO) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		headers.set(Constantes.AUTHORIZATION, Constantes.BEARER + userToken.getToken());

		HttpEntity<PropiedadDTO> entity = new HttpEntity<>(propiedadDTO, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(balanceador.urlBalanceador() + "/agenciaInmobiliaria/adicionarPropiedad",
					HttpMethod.POST, entity, String.class);
			return MapearRespuesta.mapearRespuestaExitosa(response);
		} catch (HttpStatusCodeException e) {

			return MapearRespuesta.mapearRespuestaErrores(e);

		}
	}

	@PutMapping("/actualizarPropiedad/{id}")
	public ResponseEntity<Object> updatePropiedad(@RequestBody PropiedadDTO propiedadDTO, @PathVariable Long id) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		headers.set(Constantes.AUTHORIZATION, Constantes.BEARER + userToken.getToken());

		HttpEntity<PropiedadDTO> entity = new HttpEntity<>(propiedadDTO, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(balanceador.urlBalanceador() + PROPIEDAD + id,
					HttpMethod.PUT, entity, String.class);
			return MapearRespuesta.mapearRespuestaExitosa(response);

		} catch (HttpStatusCodeException e) {

			return MapearRespuesta.mapearRespuestaErrores(e);

		}
	}

}
