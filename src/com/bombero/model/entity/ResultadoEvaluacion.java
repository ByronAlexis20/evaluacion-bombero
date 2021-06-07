package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the resultado_evaluacion database table.
 * 
 */
@Entity
@Table(name="resultado_evaluacion")
@NamedQueries({
	@NamedQuery(name="ResultadoEvaluacion.buscarPorEvaluacionYAspirante", query="SELECT r FROM ResultadoEvaluacion r where r.evaluacion.idEvaluacion = :idEvaluacion and r.matricula.idMatricula = :idMatricula and r.estado = 'A'")
})
public class ResultadoEvaluacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_resultado_evaluacion")
	private Integer idResultadoEvaluacion;

	private float calificacion;

	private String estado;

	//bi-directional many-to-one association to RespuestaSeleccionada
	@OneToMany(mappedBy="resultadoEvaluacion")
	private List<RespuestaSeleccionada> respuestaSeleccionadas;

	//bi-directional many-to-one association to Evaluacion
	@ManyToOne
	@JoinColumn(name="id_evaluacion")
	private Evaluacion evaluacion;

	//bi-directional many-to-one association to Matricula
	@ManyToOne
	@JoinColumn(name="id_matricula")
	private Matricula matricula;

	public ResultadoEvaluacion() {
	}

	public Integer getIdResultadoEvaluacion() {
		return this.idResultadoEvaluacion;
	}

	public void setIdResultadoEvaluacion(Integer idResultadoEvaluacion) {
		this.idResultadoEvaluacion = idResultadoEvaluacion;
	}

	public float getCalificacion() {
		return this.calificacion;
	}

	public void setCalificacion(float calificacion) {
		this.calificacion = calificacion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<RespuestaSeleccionada> getRespuestaSeleccionadas() {
		return this.respuestaSeleccionadas;
	}

	public void setRespuestaSeleccionadas(List<RespuestaSeleccionada> respuestaSeleccionadas) {
		this.respuestaSeleccionadas = respuestaSeleccionadas;
	}

	public RespuestaSeleccionada addRespuestaSeleccionada(RespuestaSeleccionada respuestaSeleccionada) {
		getRespuestaSeleccionadas().add(respuestaSeleccionada);
		respuestaSeleccionada.setResultadoEvaluacion(this);

		return respuestaSeleccionada;
	}

	public RespuestaSeleccionada removeRespuestaSeleccionada(RespuestaSeleccionada respuestaSeleccionada) {
		getRespuestaSeleccionadas().remove(respuestaSeleccionada);
		respuestaSeleccionada.setResultadoEvaluacion(null);

		return respuestaSeleccionada;
	}

	public Evaluacion getEvaluacion() {
		return this.evaluacion;
	}

	public void setEvaluacion(Evaluacion evaluacion) {
		this.evaluacion = evaluacion;
	}

	public Matricula getMatricula() {
		return this.matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

}