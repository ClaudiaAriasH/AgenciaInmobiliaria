package co.com.udem.agenciainmobiliaria.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class TipoIdentificacion implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String tipoDocumento;
	private String descripcion;

	@JsonManagedReference
	@OneToMany(mappedBy = "tipoIdentificacion")
	private Collection<RegistrarUsuario> registrarUsuario;

	public TipoIdentificacion() {
		super();

	}

	public TipoIdentificacion(Long id, String tipoDocumento, String descripcion) {
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
