package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="modulo_asignado")
@NamedQuery(name="ModuloAsignado.findAll", query="SELECT m FROM ModuloAsignado m")
public class ModuloAsignado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_asignacion")
	private int idAsignacion;

	private String estado;

	//bi-directional many-to-one association to Calificacion
	@OneToMany(mappedBy="moduloAsignado")
	private List<Calificacion> calificacions;

	//bi-directional many-to-one association to Modulo
	@ManyToOne
	@JoinColumn(name="id_modulo")
	private Modulo modulo;

	//bi-directional many-to-one association to Instructor
	@ManyToOne
	@JoinColumn(name="id_instructor")
	private Instructor instructor;

	//bi-directional many-to-one association to Matricula
	@ManyToOne
	@JoinColumn(name="id_periodo")
	private Periodo periodo;

	public ModuloAsignado() {
	}

	public int getIdAsignacion() {
		return this.idAsignacion;
	}

	public void setIdAsignacion(int idAsignacion) {
		this.idAsignacion = idAsignacion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<Calificacion> getCalificacions() {
		return this.calificacions;
	}

	public void setCalificacions(List<Calificacion> calificacions) {
		this.calificacions = calificacions;
	}

	public Calificacion addCalificacion(Calificacion calificacion) {
		getCalificacions().add(calificacion);
		calificacion.setModuloAsignado(this);

		return calificacion;
	}

	public Calificacion removeCalificacion(Calificacion calificacion) {
		getCalificacions().remove(calificacion);
		calificacion.setModuloAsignado(null);

		return calificacion;
	}

	public Modulo getModulo() {
		return this.modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public Instructor getInstructor() {
		return this.instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

}