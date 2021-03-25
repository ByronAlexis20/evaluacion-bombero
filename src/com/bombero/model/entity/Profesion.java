package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the profesion database table.
 * 
 */
@Entity
@NamedQuery(name="Profesion.buscarPorPatron", query="SELECT p FROM Profesion p where lower(p.profesion) like lower(:patron)")
public class Profesion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_profesion")
	private Integer idProfesion;

	private String estado;

	private String profesion;

	//bi-directional many-to-one association to Aspirante
	@OneToMany(mappedBy="profesion")
	private List<Aspirante> aspirantes;

	public Profesion() {
	}

	public Integer getIdProfesion() {
		return this.idProfesion;
	}

	public void setIdProfesion(Integer idProfesion) {
		this.idProfesion = idProfesion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getProfesion() {
		return this.profesion;
	}

	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}

	public List<Aspirante> getAspirantes() {
		return this.aspirantes;
	}

	public void setAspirantes(List<Aspirante> aspirantes) {
		this.aspirantes = aspirantes;
	}

	public Aspirante addAspirante(Aspirante aspirante) {
		getAspirantes().add(aspirante);
		aspirante.setProfesion(this);

		return aspirante;
	}

	public Aspirante removeAspirante(Aspirante aspirante) {
		getAspirantes().remove(aspirante);
		aspirante.setProfesion(null);

		return aspirante;
	}

}