package co.com.udem.agenciainmobiliariaclient.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

import com.google.gson.Gson;

import co.com.udem.agenciainmobiliariaclient.domain.ResponseDTO;

public class MapearRespuesta {

	private MapearRespuesta() {

	}

	public static ResponseEntity<Object> mapearRespuestaErrores(HttpStatusCodeException e) {
		Map<String, Object> res = new HashMap<>();
		String data = e.getResponseBodyAsString();

		Gson gson = new Gson();
		ResponseDTO mensaje = gson.fromJson(data, ResponseDTO.class);

		res.put("respuesta", mensaje);
		res.put("estado", e.getRawStatusCode());

		return new ResponseEntity<>(res, HttpStatus.valueOf(e.getRawStatusCode()));
	}

	public static ResponseEntity<Object> mapearRespuestaExitosa(ResponseEntity<String> response) {
		Map<String, Object> res = new HashMap<>();
		Gson g = new Gson();
		Object registroUsuarioDTO = g.fromJson(response.getBody(), Object.class);
		Object listadoUsuarios = registroUsuarioDTO;

		res.put("respuesta", listadoUsuarios);
		res.put("estado", response.getStatusCodeValue());
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
}
