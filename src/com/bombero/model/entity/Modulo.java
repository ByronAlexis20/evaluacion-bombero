package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the modulo database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Modulo.buscarPorPatron", query="SELECT m FROM Modulo m where lower(m.modulo) like lower(:patron)"),
	@NamedQuery(name="Modulo.buscarSinAsignacion", query="SELECT m FROM Modulo m LEFT JOIN ModuloAsignado ma ON m.idModulo = ma.modulo.idModulo where ma.modulo.idModulo is null and ma.periodo.idPeriodo = :idPeriodo"),
	@NamedQuery(name="Modulo.buscarPorInstructor", query="SELECT m FROM Modulo m, ModuloAsignado ma where m.idModulo = ma.modulo.idModulo and m.estado = 'A' and ma.estado = 'A' and ma.instructor.idInstructor = :idInstructor"),
})
public class Modulo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_modulo")
	private Integer idModulo;

	private String estado;

	private String modulo;

	//bi-directional many-to-one association to Evaluacion
	@OneToMany(mappedBy="modulo")
	private List<Evaluacion> evaluacions;

	//bi-directional many-to-one association to ModuloAsignado
	@OneToMany(mappedBy="modulo")
	private List<ModuloAsignado> moduloAsignados;

	@OneToMany(mappedBy="modulo")
	private List<Calificacion> calificacions;
	
	public Modulo() {
	}

	public Integer getIdModulo() {
		return this.idModulo;
	}

	public void setIdModulo(Integer idModulo) {
		this.idModulo = idModulo;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getModulo() {
		return this.modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public List<Evaluacion> getEvaluacions() {
		return this.evaluacions;
	}

	public void setEvaluacions(List<Evaluacion> evaluacions) {
		this.evaluacions = evaluacions;
	}

	public Evaluacion addEvaluacion(Evaluacion evaluacion) {
		getEvaluacions().add(evaluacion);
		evaluacion.setModulo(this);

		return evaluacion;
	}

	public Evaluacion removeEvaluacion(Evaluacion evaluacion) {
		getEvaluacions().remove(evaluacion);
		evaluacion.setModulo(null);

		return evaluacion;
	}

	public List<ModuloAsignado> getModuloAsignados() {
		return this.moduloAsignados;
	}

	public void setModuloAsignados(List<ModuloAsignado> moduloAsignados) {
		this.moduloAsignados = moduloAsignados;
	}

	public ModuloAsignado addModuloAsignado(ModuloAsignado moduloAsignado) {
		getModuloAsignados().add(moduloAsignado);
		moduloAsignado.setModulo(this);

		return moduloAsignado;
	}

	public ModuloAsignado removeModuloAsignado(ModuloAsignado moduloAsignado) {
		getModuloAsignados().remove(moduloAsignado);
		moduloAsignado.setModulo(null);

		return moduloAsignado;
	}

	public List<Calificacion> getCalificacions() {
		return this.calificacions;
	}

	public void setCalificacions(List<Calificacion> calificacions) {
		this.calificacions = calificacions;
	}

	public Calificacion addCalificacion(Calificacion calificacion) {
		getCalificacions().add(calificacion);
		calificacion.setModulo(this);

		return calificacion;
	}

	public Calificacion removeCalificacion(Calificacion calificacion) {
		getCalificacions().remove(calificacion);
		calificacion.setModulo(null);

		return calificacion;
	}

	@Override
	public String toString() {
		return "Modulo \n[idModulo=" + idModulo + ", \nestado=" + estado + ", \nmodulo=" + modulo + ", \nevaluacions="
				+ evaluacions + ", \nmoduloAsignados=" + moduloAsignados + ", \ncalificacions=" + calificacions + "]";
	}
}