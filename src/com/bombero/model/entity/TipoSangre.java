package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tipo_sangre database table.
 * 
 */
@Entity
@Table(name="tipo_sangre")
@NamedQuery(name="TipoSangre.buscarPorPatron", query="SELECT t FROM TipoSangre t where lower(t.tipoSangre) like lower(:patron) and t.estado = 'A'")
public class TipoSangre implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipo_sangre")
	private Integer idTipoSangre;

	private String estado;

	@Column(name="tipo_sangre")
	private String tipoSangre;

	//bi-directional many-to-one association to Aspirante
	@OneToMany(mappedBy="tipoSangre")
	private List<Aspirante> aspirantes;

	//bi-directional many-to-one association to Instructor
	@OneToMany(mappedBy="tipoSangre")
	private List<Instructor> instructors;

	public TipoSangre() {
	}

	public Integer getIdTipoSangre() {
		return this.idTipoSangre;
	}

	public void setIdTipoSangre(Integer idTipoSangre) {
		this.idTipoSangre = idTipoSangre;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getTipoSangre() {
		return this.tipoSangre;
	}

	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	public List<Aspirante> getAspirantes() {
		return this.aspirantes;
	}

	public void setAspirantes(List<Aspirante> aspirantes) {
		this.aspirantes = aspirantes;
	}

	public Aspirante addAspirante(Aspirante aspirante) {
		getAspirantes().add(aspirante);
		aspirante.setTipoSangre(this);

		return aspirante;
	}

	public Aspirante removeAspirante(Aspirante aspirante) {
		getAspirantes().remove(aspirante);
		aspirante.setTipoSangre(null);

		return aspirante;
	}

	public List<Instructor> getInstructors() {
		return this.instructors;
	}

	public void setInstructors(List<Instructor> instructors) {
		this.instructors = instructors;
	}

	public Instructor addInstructor(Instructor instructor) {
		getInstructors().add(instructor);
		instructor.setTipoSangre(this);

		return instructor;
	}

	public Instructor removeInstructor(Instructor instructor) {
		getInstructors().remove(instructor);
		instructor.setTipoSangre(null);

		return instructor;
	}

}