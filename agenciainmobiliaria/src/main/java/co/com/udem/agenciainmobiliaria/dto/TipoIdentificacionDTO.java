package co.com.udem.agenciainmobiliaria.dto;

public class TipoIdentificacionDTO {

	private Long id;
	private String tipoDocumento;
	private String descripcion;

	public TipoIdentificacionDTO() {
		super();

	}

	public TipoIdentificacionDTO(Long id, String tipoDocumento, String descripcion) {
		super();
		this.id = id;
		this.tipoDocumento = tipoDocumento;
		this.descripcion = descripcion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
