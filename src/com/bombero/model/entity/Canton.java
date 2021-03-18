package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the canton database table.
 * 
 */
@Entity
@NamedQuery(name="Canton.findAll", query="SELECT c FROM Canton c")
public class Canton implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_canton")
	private int idCanton;

	private String canton;

	private String estado;

	//bi-directional many-to-one association to Aspirante
	@OneToMany(mappedBy="canton1")
	private List<Aspirante> aspirantes1;

	//bi-directional many-to-one association to Aspirante
	@OneToMany(mappedBy="canton2")
	private List<Aspirante> aspirantes2;

	//bi-directional many-to-one association to Provincia
	@ManyToOne
	@JoinColumn(name="id_provincia")
	private Provincia provincia;

	public Canton() {
	}

	public int getIdCanton() {
		return this.idCanton;
	}

	public void setIdCanton(int idCanton) {
		this.idCanton = idCanton;
	}

	public String getCanton() {
		return this.canton;
	}

	public void setCanton(String canton) {
		this.canton = canton;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<Aspirante> getAspirantes1() {
		return this.aspirantes1;
	}

	public void setAspirantes1(List<Aspirante> aspirantes1) {
		this.aspirantes1 = aspirantes1;
	}

	public Aspirante addAspirantes1(Aspirante aspirantes1) {
		getAspirantes1().add(aspirantes1);
		aspirantes1.setCanton1(this);

		return aspirantes1;
	}

	public Aspirante removeAspirantes1(Aspirante aspirantes1) {
		getAspirantes1().remove(aspirantes1);
		aspirantes1.setCanton1(null);

		return aspirantes1;
	}

	public List<Aspirante> getAspirantes2() {
		return this.aspirantes2;
	}

	public void setAspirantes2(List<Aspirante> aspirantes2) {
		this.aspirantes2 = aspirantes2;
	}

	public Aspirante addAspirantes2(Aspirante aspirantes2) {
		getAspirantes2().add(aspirantes2);
		aspirantes2.setCanton2(this);

		return aspirantes2;
	}

	public Aspirante removeAspirantes2(Aspirante aspirantes2) {
		getAspirantes2().remove(aspirantes2);
		aspirantes2.setCanton2(null);

		return aspirantes2;
	}

	public Provincia getProvincia() {
		return this.provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

}