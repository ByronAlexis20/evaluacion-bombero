package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="tbl_respuesta")
@NamedQuery(name="Respuesta.buscarPorPregunta", query="SELECT r FROM Respuesta r where r.pregunta.idPregunta = :idPregunta AND r.estado = 'A'")
public class Respuesta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_respuesta")
	private Integer idRespuesta;

	private String correcta;

	private String estado;

	private String respuesta;

	//bi-directional many-to-one association to Pregunta
	@ManyToOne
	@JoinColumn(name="id_pregunta")
	private Pregunta pregunta;

	public Respuesta() {
	}

	public Integer getIdRespuesta() {
		return this.idRespuesta;
	}

	public void setIdRespuesta(Integer idRespuesta) {
		this.idRespuesta = idRespuesta;
	}

	public String getCorrecta() {
		return this.correcta;
	}

	public void setCorrecta(String correcta) {
		this.correcta = correcta;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getRespuesta() {
		return this.respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public Pregunta getPregunta() {
		return this.pregunta;
	}

	public void setPregunta(Pregunta pregunta) {
		this.pregunta = pregunta;
	}

}