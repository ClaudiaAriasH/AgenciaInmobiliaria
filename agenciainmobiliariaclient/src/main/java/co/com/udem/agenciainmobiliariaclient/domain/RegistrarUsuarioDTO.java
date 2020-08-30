package co.com.udem.agenciainmobiliariaclient.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class RegistrarUsuarioDTO {

	private Long id;
	private String nombres;
	private String apellidos;
	private String numeroIdentificacion;
	private String direccion;
	private String telefono;
	private String email;
	private String password;

	@Autowired
	private TipoIdentificacionDTO tipoIdentificacionDTO;

	private List<String> roles = new ArrayList<>();

	public RegistrarUsuarioDTO() {
		super();
	}

	public RegistrarUsuarioDTO(Long id, String nombres, String apellidos, String numeroIdentificacion, String direccion,
			String telefono, String email, String password, TipoIdentificacionDTO tipoIdentificacionDTO) {
		super();
		this.id = id;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.numeroIdentificacion = numeroIdentificacion;
		this.direccion = direccion;
		this.telefono = telefono;
		this.email = email;
		this.password = password;
		this.tipoIdentificacionDTO = tipoIdentificacionDTO;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public TipoIdentificacionDTO getTipoIdentificacionDTO() {
		return tipoIdentificacionDTO;
	}

	public void setTipoIdentificacionDTO(TipoIdentificacionDTO tipoIdentificacionDTO) {
		this.tipoIdentificacionDTO = tipoIdentificacionDTO;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
