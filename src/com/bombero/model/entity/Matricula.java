package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.bombero.util.Globals;

import java.util.Date;
import java.util.List;


@Entity
@Table(name="matricula")
@NamedQueries({
	@NamedQuery(name="Matricula.buscarAspirantePorPeriodo", query="SELECT m FROM Matricula m where m.periodo.idPeriodo = :idPeriodo "
			+ " and m.estado = 'A'"),
	@NamedQuery(name="Matricula.buscarPorCedula", query="SELECT m FROM Matricula m where m.periodo.idPeriodo = :idPeriodo "
			+ " and m.aspirante.cedula = :cedula and m.estado = 'A'"),
	@NamedQuery(name="Matricula.buscarPorCedulaDiferenteAlUsuarioActual", query="SELECT m FROM Matricula m where m.periodo.idPeriodo = :idPeriodo "
			+ " and m.aspirante.cedula = :cedula and m.idMatricula <> :idMatricula and m.estado = 'A'"),
	@NamedQuery(name="Matricula.buscarPorAspiranteYPeriodo", query="SELECT m FROM Matricula m where m.periodo.idPeriodo = :idPeriodo "
			+ " and m.aspirante.idAspirante = :idAspirante and m.estado = 'A'"),
	@NamedQuery(name="Matricula.buscarPorPeriodoTerminado", query="SELECT m FROM Matricula m where m.periodo.idPeriodo = :idPeriodo "
			+ "and m.periodo.estadoPeriodo = '" + Globals.ESTADO_PERIODO_FINALIZADO + "' and m.estado = 'A'"),
})
public class Matricula implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_matricula")
	private Integer idMatricula;

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

	//bi-directional many-to-one association to ResultadoEvaluacion
	@OneToMany(mappedBy="matricula")
	private List<ResultadoEvaluacion> resultadoEvaluacions;
	
	//bi-directional many-to-one association to Calificacion
	@OneToMany(mappedBy="matricula")
	private List<Calificacion> calificacions;

	public Matricula() {
	}

	public Integer getIdMatricula() {
		return this.idMatricula;
	}

	public void setIdMatricula(Integer idMatricula) {
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
	public List<Calificacion> getCalificacions() {
		return this.calificacions;
	}

	public void setCalificacions(List<Calificacion> calificacions) {
		this.calificacions = calificacions;
	}

	public Calificacion addCalificacion(Calificacion calificacion) {
		getCalificacions().add(calificacion);
		calificacion.setMatricula(this);

		return calificacion;
	}

	public Calificacion removeCalificacion(Calificacion calificacion) {
		getCalificacions().remove(calificacion);
		calificacion.setMatricula(null);

		return calificacion;
	}
}