package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the genero database table.
 * 
 */
@Entity
@NamedQuery(name="Genero.buscarGenero", query="SELECT g FROM Genero g where g.estado = 'A'")
public class Genero implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_genero")
	private int idGenero;

	private String estado;

	private String genero;

	//bi-directional many-to-one association to Aspirante
	@OneToMany(mappedBy="genero")
	private List<Aspirante> aspirantes;

	public Genero() {
	}

	public int getIdGenero() {
		return this.idGenero;
	}

	public void setIdGenero(int idGenero) {
		this.idGenero = idGenero;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getGenero() {
		return this.genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public List<Aspirante> getAspirantes() {
		return this.aspirantes;
	}

	public void setAspirantes(List<Aspirante> aspirantes) {
		this.aspirantes = aspirantes;
	}

	public Aspirante addAspirante(Aspirante aspirante) {
		getAspirantes().add(aspirante);
		aspirante.setGenero(this);

		return aspirante;
	}

	public Aspirante removeAspirante(Aspirante aspirante) {
		getAspirantes().remove(aspirante);
		aspirante.setGenero(null);

		return aspirante;
	}

}