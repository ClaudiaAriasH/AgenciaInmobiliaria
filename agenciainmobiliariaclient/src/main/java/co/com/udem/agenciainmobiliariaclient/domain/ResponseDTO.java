package co.com.udem.agenciainmobiliariaclient.domain;

public class ResponseDTO {

	private String mensaje;

	public ResponseDTO() {
		super();

	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public ResponseDTO(String mensaje) {
		super();
		this.mensaje = mensaje;
	}

}
