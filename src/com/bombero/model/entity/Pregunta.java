package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="tbl_pregunta")
@NamedQueries({
	@NamedQuery(name="Pregunta.buscarPorPeriodoYModulo", query="SELECT p FROM Pregunta p where p.evaluacion.periodo.idPeriodo = :idPeriodo "
			+ "AND p.evaluacion.modulo.idModulo = :idModulo AND p.estado = 'A' AND p.evaluacion.estado = 'A' AND "
			+ "p.evaluacion.periodo.estado = 'A' AND p.evaluacion.modulo.estado = 'A'"),
	@NamedQuery(name="Pregunta.buscarPorId", query="SELECT p FROM Pregunta p where p.idPregunta = :idPregunta"),
	@NamedQuery(name="Pregunta.buscarUltimo", query="SELECT p FROM Pregunta p where p.estado = 'A' order by p.idPregunta desc"),
	@NamedQuery(name="Pregunta.buscarPorEvaluacion", query="SELECT p FROM Pregunta p where p.evaluacion.idEvaluacion = :idEvaluacion and p.estado = 'A'")
})
public class Pregunta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_pregunta")
	private Integer idPregunta;

	private String estado;

	private String pregunta;

	//bi-directional many-to-one association to Evaluacion
	@ManyToOne
	@JoinColumn(name="id_evaluacion")
	private Evaluacion evaluacion;

	//bi-directional many-to-one association to Respuesta
	@OneToMany(mappedBy="pregunta", cascade = CascadeType.ALL)
	private List<Respuesta> respuestas;

	public Pregunta() {
	}

	public Integer getIdPregunta() {
		return this.idPregunta;
	}

	public void setIdPregunta(Integer idPregunta) {
		this.idPregunta = idPregunta;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getPregunta() {
		return this.pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	public Evaluacion getEvaluacion() {
		return this.evaluacion;
	}

	public void setEvaluacion(Evaluacion evaluacion) {
		this.evaluacion = evaluacion;
	}

	public List<Respuesta> getRespuestas() {
		return this.respuestas;
	}

	public void setRespuestas(List<Respuesta> respuestas) {
		this.respuestas = respuestas;
	}

	public Respuesta addRespuesta(Respuesta respuesta) {
		getRespuestas().add(respuesta);
		respuesta.setPregunta(this);

		return respuesta;
	}

	public Respuesta removeRespuesta(Respuesta respuesta) {
		getRespuestas().remove(respuesta);
		respuesta.setPregunta(null);

		return respuesta;
	}

	@Override
	public String toString() {
		return "Pregunta \n[idPregunta=" + idPregunta + ", \nestado=" + estado + ", \npregunta=" + pregunta
				+ ", \nevaluacion=" + evaluacion + ", \nrespuestas=" + respuestas + "]";
	}

}