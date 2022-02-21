package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="seg_configuracion")
@NamedQuery(name="Configuracion.findAll", query="SELECT c FROM Configuracion c")
public class Configuracion implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_configuracion")
	private Integer idConfiguracion;
	
	private float puntaje;
	
	@Column(name="cantidad_preguntas")
	private Integer cantidadPreguntas;
	
	@Column(name="nombre_primer_jefe")
	private String nombrePrimerJefe;
	
	@Column(name="cargo_primer_jefe")
	private String cargoPrimerJefe;
	
	@Column(name="nombre_segundo_jefe")
	private String nombreSegundoJefe;
	
	@Column(name="cargo_segundo_jefe")
	private String cargoSegundoJefe;
	
	@Column(name="nombre_capitan")
	private String nombreCapitan;
	
	@Column(name="cargo_capitan")
	private String cargoCapitan;
	
	public Configuracion() {
		
	}

	public Integer getIdConfiguracion() {
		return idConfiguracion;
	}

	public void setIdConfiguracion(Integer idConfiguracion) {
		this.idConfiguracion = idConfiguracion;
	}

	public float getPuntaje() {
		return puntaje;
	}

	public void setPuntaje(float puntaje) {
		this.puntaje = puntaje;
	}

	public Integer getCantidadPreguntas() {
		return cantidadPreguntas;
	}

	public void setCantidadPreguntas(Integer cantidadPreguntas) {
		this.cantidadPreguntas = cantidadPreguntas;
	}

	public String getNombrePrimerJefe() {
		return nombrePrimerJefe;
	}

	public void setNombrePrimerJefe(String nombrePrimerJefe) {
		this.nombrePrimerJefe = nombrePrimerJefe;
	}

	public String getCargoPrimerJefe() {
		return cargoPrimerJefe;
	}

	public void setCargoPrimerJefe(String cargoPrimerJefe) {
		this.cargoPrimerJefe = cargoPrimerJefe;
	}

	public String getNombreSegundoJefe() {
		return nombreSegundoJefe;
	}

	public void setNombreSegundoJefe(String nombreSegundoJefe) {
		this.nombreSegundoJefe = nombreSegundoJefe;
	}

	public String getCargoSegundoJefe() {
		return cargoSegundoJefe;
	}

	public void setCargoSegundoJefe(String cargoSegundoJefe) {
		this.cargoSegundoJefe = cargoSegundoJefe;
	}

	public String getNombreCapitan() {
		return nombreCapitan;
	}

	public void setNombreCapitan(String nombreCapitan) {
		this.nombreCapitan = nombreCapitan;
	}

	public String getCargoCapitan() {
		return cargoCapitan;
	}

	public void setCargoCapitan(String cargoCapitan) {
		this.cargoCapitan = cargoCapitan;
	}
	
}