package co.com.udem.agenciainmobiliaria.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Propiedad implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String area;
	private int numeroHabitaciones;
	private int numeroBanos;
	private String tipoPropiedad;
	private double valor;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	private RegistrarUsuario registrarUsuario;

	public Propiedad() {
		super();

	}

	public Propiedad(Long id, String area, int numeroHabitaciones, int numeroBanos, String tipoPropiedad,
			double valor) {
		super();
		this.id = id;
		this.area = area;
		this.numeroHabitaciones = numeroHabitaciones;
		this.numeroBanos = numeroBanos;
		this.tipoPropiedad = tipoPropiedad;
		this.valor = valor;
	}

	public RegistrarUsuario getRegistrarUsuario() {
		return registrarUsuario;
	}

	public void setRegistrarUsuario(RegistrarUsuario registrarUsuario) {
		this.registrarUsuario = registrarUsuario;
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
