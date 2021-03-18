package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the evaluacion database table.
 * 
 */
@Entity
@NamedQuery(name="Evaluacion.findAll", query="SELECT e FROM Evaluacion e")
public class Evaluacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_evaluacion")
	private int idEvaluacion;

	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Modulo
	@ManyToOne
	@JoinColumn(name="id_modulo")
	private Modulo modulo;

	//bi-directional many-to-one association to Periodo
	@ManyToOne
	@JoinColumn(name="id_periodo")
	private Periodo periodo;

	//bi-directional many-to-one association to Pregunta
	@OneToMany(mappedBy="evaluacion")
	private List<Pregunta> preguntas;

	//bi-directional many-to-one association to ResultadoEvaluacion
	@OneToMany(mappedBy="evaluacion")
	private List<ResultadoEvaluacion> resultadoEvaluacions;

	public Evaluacion() {
	}

	public int getIdEvaluacion() {
		return this.idEvaluacion;
	}

	public void setIdEvaluacion(int idEvaluacion) {
		this.idEvaluacion = idEvaluacion;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

	public Periodo getPeriodo() {
		return this.periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	public List<Pregunta> getPreguntas() {
		return this.preguntas;
	}

	public void setPreguntas(List<Pregunta> preguntas) {
		this.preguntas = preguntas;
	}

	public Pregunta addPregunta(Pregunta pregunta) {
		getPreguntas().add(pregunta);
		pregunta.setEvaluacion(this);

		return pregunta;
	}

	public Pregunta removePregunta(Pregunta pregunta) {
		getPreguntas().remove(pregunta);
		pregunta.setEvaluacion(null);

		return pregunta;
	}

	public List<ResultadoEvaluacion> getResultadoEvaluacions() {
		return this.resultadoEvaluacions;
	}

	public void setResultadoEvaluacions(List<ResultadoEvaluacion> resultadoEvaluacions) {
		this.resultadoEvaluacions = resultadoEvaluacions;
	}

	public ResultadoEvaluacion addResultadoEvaluacion(ResultadoEvaluacion resultadoEvaluacion) {
		getResultadoEvaluacions().add(resultadoEvaluacion);
		resultadoEvaluacion.setEvaluacion(this);

		return resultadoEvaluacion;
	}

	public ResultadoEvaluacion removeResultadoEvaluacion(ResultadoEvaluacion resultadoEvaluacion) {
		getResultadoEvaluacions().remove(resultadoEvaluacion);
		resultadoEvaluacion.setEvaluacion(null);

		return resultadoEvaluacion;
	}

}