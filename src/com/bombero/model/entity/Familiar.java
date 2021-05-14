package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the familiar database table.
 * 
 */
@Entity
@NamedQuery(name="Familiar.findAll", query="SELECT f FROM Familiar f")
public class Familiar implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_familiar")
	private int idFamiliar;

	private String cedula;
	
	private String apellido;

	private String educacion;

	private String estado;

	private String nombre;

	private String profesion;

	private String telefono;

	private String trabajo;

	//bi-directional many-to-one association to EstadoCivil
	@ManyToOne
	@JoinColumn(name="id_estado_civil")
	private EstadoCivil estadoCivil;

	//bi-directional many-to-one association to FichaMedica
	@ManyToOne
	@JoinColumn(name="id_ficha_medica")
	private FichaMedica fichaMedica;

	//bi-directional many-to-one association to TipoFamiliar
	@ManyToOne
	@JoinColumn(name="id_tipo_familiar")
	private TipoFamiliar tipoFamiliar;

	public Familiar() {
	}

	public int getIdFamiliar() {
		return this.idFamiliar;
	}

	public void setIdFamiliar(int idFamiliar) {
		this.idFamiliar = idFamiliar;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEducacion() {
		return this.educacion;
	}

	public void setEducacion(String educacion) {
		this.educacion = educacion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getProfesion() {
		return this.profesion;
	}

	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTrabajo() {
		return this.trabajo;
	}

	public void setTrabajo(String trabajo) {
		this.trabajo = trabajo;
	}

	public EstadoCivil getEstadoCivil() {
		return this.estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public FichaMedica getFichaMedica() {
		return this.fichaMedica;
	}

	public void setFichaMedica(FichaMedica fichaMedica) {
		this.fichaMedica = fichaMedica;
	}

	public TipoFamiliar getTipoFamiliar() {
		return this.tipoFamiliar;
	}

	public void setTipoFamiliar(TipoFamiliar tipoFamiliar) {
		this.tipoFamiliar = tipoFamiliar;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

}