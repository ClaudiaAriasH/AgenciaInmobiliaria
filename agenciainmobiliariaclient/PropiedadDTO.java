package co.com.udem.agenciainmobiliaria.dto;

import org.springframework.beans.factory.annotation.Autowired;

public class PropiedadDTO {

	private Long id;
	private String area;
	private int numeroHabitaciones;
	private int numeroBanos;
	private String tipoPropiedad;
	private double valor;

	@Autowired
	private RegistrarUsuarioDTO registrarUsuarioDTO;

	public PropiedadDTO() {
		super();

	}

	public PropiedadDTO(Long id, String area, int numeroHabitaciones, int numeroBanos, String tipoPropiedad,
			double valor, RegistrarUsuarioDTO registrarUsuarioDTO) {
		super();
		this.id = id;
		this.area = area;
		this.numeroHabitaciones = numeroHabitaciones;
		this.numeroBanos = numeroBanos;
		this.tipoPropiedad = tipoPropiedad;
		this.valor = valor;
		this.registrarUsuarioDTO = registrarUsuarioDTO;
	}

	public RegistrarUsuarioDTO getRegistrarUsuarioDTO() {
		return registrarUsuarioDTO;
	}

	public void setRegistrarUsuarioDTO(RegistrarUsuarioDTO registrarUsuarioDTO) {
		this.registrarUsuarioDTO = registrarUsuarioDTO;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public int getNumeroHabitaciones() {
		return numeroHabitaciones;
	}

	public void setNumeroHabitaciones(int numeroHabitaciones) {
		this.numeroHabitaciones = numeroHabitaciones;
	}

	public int getNumeroBanos() {
		return numeroBanos;
	}

	public void setNumeroBanos(int numeroBanos) {
		this.numeroBanos = numeroBanos;
	}

	public String getTipoPropiedad() {
		return tipoPropiedad;
	}

	public void setTipoPropiedad(String tipoPropiedad) {
		this.tipoPropiedad = tipoPropiedad;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

}
