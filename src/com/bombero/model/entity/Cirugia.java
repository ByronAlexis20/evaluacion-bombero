package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the cirugia database table.
 * 
 */
@Entity
@NamedQuery(name="Cirugia.findAll", query="SELECT c FROM Cirugia c")
public class Cirugia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_cirugia")
	private int idCirugia;

	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_cirugia")
	private Date fechaCirugia;

	@Column(name="nombre_organo_comprometido")
	private String nombreOrganoComprometido;

	//bi-directional many-to-one association to FichaMedica
	@ManyToOne
	@JoinColumn(name="id_ficha_medica")
	private FichaMedica fichaMedica;

	public Cirugia() {
	}

	public int getIdCirugia() {
		return this.idCirugia;
	}

	public void setIdCirugia(int idCirugia) {
		this.idCirugia = idCirugia;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaCirugia() {
		return this.fechaCirugia;
	}

	public void setFechaCirugia(Date fechaCirugia) {
		this.fechaCirugia = fechaCirugia;
	}

	public String getNombreOrganoComprometido() {
		return this.nombreOrganoComprometido;
	}

	public void setNombreOrganoComprometido(String nombreOrganoComprometido) {
		this.nombreOrganoComprometido = nombreOrganoComprometido;
	}

	public FichaMedica getFichaMedica() {
		return this.fichaMedica;
	}

	public void setFichaMedica(FichaMedica fichaMedica) {
		this.fichaMedica = fichaMedica;
	}

}