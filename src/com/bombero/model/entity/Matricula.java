package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="matricula")
@NamedQueries({
	@NamedQuery(name="Matricula.buscarAspirantePorPeriodo", query="SELECT m FROM Matricula m where m.periodo.idPeriodo = :idPeriodo and m.estado = 'A'")
})
public class Matricula implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_matricula")
	private int idMatricula;

	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_ingreso")
	private Date fechaIngreso;

	//bi-directional many-to-one association to Aspirante
	@ManyToOne
	@JoinColumn(name="id_aspirante")
	private Aspirante aspirante;

	//bi-directional many-to-one association to Periodo
	@ManyToOne
	@JoinColumn(name="id_periodo")
	private Periodo periodo;

	//bi-directional many-to-one association to ModuloAsignado
	@OneToMany(mappedBy="matricula")
	private List<ModuloAsignado> moduloAsignados;

	//bi-directional many-to-one association to ResultadoEvaluacion
	@OneToMany(mappedBy="matricula")
	private List<ResultadoEvaluacion> resultadoEvaluacions;

	public Matricula() {
	}

	public int getIdMatricula() {
		return this.idMatricula;
	}

	public void setIdMatricula(int idMatricula) {
		this.idMatricula = idMatricula;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaIngreso() {
		return this.fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Aspirante getAspirante() {
		return this.aspirante;
	}

	public void setAspirante(Aspirante aspirante) {
		this.aspirante = aspirante;
	}

	public Periodo getPeriodo() {
		return this.periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	public List<ModuloAsignado> getModuloAsignados() {
		return this.moduloAsignados;
	}

	public void setModuloAsignados(List<ModuloAsignado> moduloAsignados) {
		this.moduloAsignados = moduloAsignados;
	}

	public ModuloAsignado addModuloAsignado(ModuloAsignado moduloAsignado) {
		getModuloAsignados().add(moduloAsignado);
		moduloAsignado.setMatricula(this);

		return moduloAsignado;
	}

	public ModuloAsignado removeModuloAsignado(ModuloAsignado moduloAsignado) {
		getModuloAsignados().remove(moduloAsignado);
		moduloAsignado.setMatricula(null);

		return moduloAsignado;
	}

	public List<ResultadoEvaluacion> getResultadoEvaluacions() {
		return this.resultadoEvaluacions;
	}

	public void setResultadoEvaluacions(List<ResultadoEvaluacion> resultadoEvaluacions) {
		this.resultadoEvaluacions = resultadoEvaluacions;
	}

	public ResultadoEvaluacion addResultadoEvaluacion(ResultadoEvaluacion resultadoEvaluacion) {
		getResultadoEvaluacions().add(resultadoEvaluacion);
		resultadoEvaluacion.setMatricula(this);

		return resultadoEvaluacion;
	}

	public ResultadoEvaluacion removeResultadoEvaluacion(ResultadoEvaluacion resultadoEvaluacion) {
		getResultadoEvaluacions().remove(resultadoEvaluacion);
		resultadoEvaluacion.setMatricula(null);

		return resultadoEvaluacion;
	}

}