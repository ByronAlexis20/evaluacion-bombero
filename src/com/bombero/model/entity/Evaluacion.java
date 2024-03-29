package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="tbl_evaluacion")
@NamedQueries({
	@NamedQuery(name="Evaluacion.buscarPorModuloYPeriodo", query="SELECT e FROM Evaluacion e where e.periodo.idPeriodo = :idPeriodo AND e.modulo.idModulo = :idModulo and e.estado = 'A'"),
	@NamedQuery(name="Evaluacion.buscarPorId", query="SELECT e FROM Evaluacion e where e.idEvaluacion = :idEvaluacion"),
	@NamedQuery(name="Evaluacion.buscarUltimo", query="SELECT e FROM Evaluacion e where e.estado = 'A' ORDER BY e.idEvaluacion desc"),
	@NamedQuery(name="Evaluacion.buscarPorPeriodo", query="SELECT e FROM Evaluacion e where e.periodo.idPeriodo = :idPeriodo AND e.estado = 'A'"),
	@NamedQuery(name="Evaluacion.buscarPorEstadoEvaluacion", query="SELECT e FROM Evaluacion e where e.estadoEvaluacion = :estado AND e.estado = 'A'")
})
public class Evaluacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_evaluacion")
	private Integer idEvaluacion;

	private String descripcion;

	@Column(name="estado_evaluacion")
	private String estadoEvaluacion;
	
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
	@OneToMany(mappedBy="evaluacion", cascade = CascadeType.ALL)
	private List<Pregunta> preguntas;

	//bi-directional many-to-one association to ResultadoEvaluacion
	@OneToMany(mappedBy="evaluacion", cascade = CascadeType.ALL)
	private List<ResultadoEvaluacion> resultadoEvaluacions;

	public Evaluacion() {
	}

	public Integer getIdEvaluacion() {
		return this.idEvaluacion;
	}

	public void setIdEvaluacion(Integer idEvaluacion) {
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

	public String getEstadoEvaluacion() {
		return estadoEvaluacion;
	}

	public void setEstadoEvaluacion(String estadoEvaluacion) {
		this.estadoEvaluacion = estadoEvaluacion;
	}

}