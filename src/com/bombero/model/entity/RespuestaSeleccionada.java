package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="tbl_respuesta_seleccionada")
@NamedQuery(name="RespuestaSeleccionada.findAll", query="SELECT r FROM RespuestaSeleccionada r")
public class RespuestaSeleccionada implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_respuesta_seleccionada")
	private Integer idRespuestaSeleccionada;

	private String estado;

	@Column(name="id_pregunta")
	private int idPregunta;

	@Column(name="id_respuesta")
	private int idRespuesta;

	//bi-directional many-to-one association to ResultadoEvaluacion
	@ManyToOne
	@JoinColumn(name="id_resultado_evaluacion")
	private ResultadoEvaluacion resultadoEvaluacion;

	public RespuestaSeleccionada() {
	}

	public Integer getIdRespuestaSeleccionada() {
		return this.idRespuestaSeleccionada;
	}

	public void setIdRespuestaSeleccionada(Integer idRespuestaSeleccionada) {
		this.idRespuestaSeleccionada = idRespuestaSeleccionada;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getIdPregunta() {
		return this.idPregunta;
	}

	public void setIdPregunta(int idPregunta) {
		this.idPregunta = idPregunta;
	}

	public int getIdRespuesta() {
		return this.idRespuesta;
	}

	public void setIdRespuesta(int idRespuesta) {
		this.idRespuesta = idRespuesta;
	}

	public ResultadoEvaluacion getResultadoEvaluacion() {
		return this.resultadoEvaluacion;
	}

	public void setResultadoEvaluacion(ResultadoEvaluacion resultadoEvaluacion) {
		this.resultadoEvaluacion = resultadoEvaluacion;
	}

}