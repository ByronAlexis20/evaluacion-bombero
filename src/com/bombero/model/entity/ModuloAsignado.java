package com.bombero.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="tbl_modulo_asignado")
@NamedQueries({
	@NamedQuery(name="ModuloAsignado.buscarAsignacionesPorPeriodo", query="SELECT m FROM ModuloAsignado m where m.periodo.idPeriodo = :idPeriodo and m.estado = 'A'"),
	@NamedQuery(name="ModuloAsignado.buscarAsignacionesPorInstructorPeriodo", query="SELECT m FROM ModuloAsignado m where m.periodo.idPeriodo = :idPeriodo and m.instructor.idInstructor = :idInstructor and m.estado = 'A'"),
})
public class ModuloAsignado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_asignacion")
	private Integer idAsignacion;

	private String estado;

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

	public Integer getIdAsignacion() {
		return this.idAsignacion;
	}

	public void setIdAsignacion(Integer idAsignacion) {
		this.idAsignacion = idAsignacion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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

	@Override
	public String toString() {
		return "ModuloAsignado \n[idAsignacion=" + idAsignacion + ", \nestado=" + estado + ", \nmodulo=" + modulo
				+ ", \ninstructor=" + instructor + ", \nperiodo=" + periodo + "]";
	}

}