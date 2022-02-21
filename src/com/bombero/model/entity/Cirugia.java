package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="tbl_cirugia")
@NamedQuery(name="Cirugia.buscarPorFichaMedica", query="SELECT c FROM Cirugia c where c.fichaMedica.idFichaMedica = :id and c.estado = 'A'")
public class Cirugia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_cirugia")
	private Integer idCirugia;

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

	public Integer getIdCirugia() {
		return this.idCirugia;
	}

	public void setIdCirugia(Integer idCirugia) {
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