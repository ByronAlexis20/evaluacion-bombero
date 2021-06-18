package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.bombero.util.Globals;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the periodo database table.
 * 
 */
@Entity
@Table(name="periodo")
@NamedQueries({
	@NamedQuery(name="Periodo.buscarPorPatron", query="SELECT p FROM Periodo p where lower(p.descripcion) like lower(:patron)"),
	@NamedQuery(name="Periodo.buscarActivos", query="SELECT p FROM Periodo p where p.estado = 'A' ORDER BY p.estadoPeriodo "),
	@NamedQuery(name="Periodo.buscarEnProceso", query="SELECT p FROM Periodo p where p.estado = 'A' and p.estadoPeriodo = '" + Globals.ESTADO_PERIODO_EN_PROCESO + "'"),
	@NamedQuery(name="Periodo.buscarTerminados", query="SELECT p FROM Periodo p where p.estado = 'A' and p.estadoPeriodo = '" + Globals.ESTADO_PERIODO_FINALIZADO + "'")
})
public class Periodo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_periodo")
	private Integer idPeriodo;

	private String descripcion;

	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_fin")
	private Date fechaFin;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_inicio")
	private Date fechaInicio;
	
	@Column(name="estado_periodo")
	private String estadoPeriodo;

	//bi-directional many-to-one association to Evaluacion
	@OneToMany(mappedBy="periodo")
	private List<Evaluacion> evaluacions;

	//bi-directional many-to-one association to Matricula
	@OneToMany(mappedBy="periodo")
	private List<Matricula> matriculas;
	
	//bi-directional many-to-one association to ModuloAsignado
	@OneToMany(mappedBy="periodo")
	private List<ModuloAsignado> moduloAsignados;

	public Periodo() {
	}

	public Integer getIdPeriodo() {
		return this.idPeriodo;
	}

	public void setIdPeriodo(Integer idPeriodo) {
		this.idPeriodo = idPeriodo;
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

	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public List<Evaluacion> getEvaluacions() {
		return this.evaluacions;
	}

	public void setEvaluacions(List<Evaluacion> evaluacions) {
		this.evaluacions = evaluacions;
	}

	public Evaluacion addEvaluacion(Evaluacion evaluacion) {
		getEvaluacions().add(evaluacion);
		evaluacion.setPeriodo(this);

		return evaluacion;
	}

	public Evaluacion removeEvaluacion(Evaluacion evaluacion) {
		getEvaluacions().remove(evaluacion);
		evaluacion.setPeriodo(null);

		return evaluacion;
	}

	public List<Matricula> getMatriculas() {
		return this.matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	public Matricula addMatricula(Matricula matricula) {
		getMatriculas().add(matricula);
		matricula.setPeriodo(this);

		return matricula;
	}

	public Matricula removeMatricula(Matricula matricula) {
		getMatriculas().remove(matricula);
		matricula.setPeriodo(null);

		return matricula;
	}
	
	public List<ModuloAsignado> getModuloAsignados() {
		return this.moduloAsignados;
	}

	public void setModuloAsignados(List<ModuloAsignado> moduloAsignados) {
		this.moduloAsignados = moduloAsignados;
	}

	public ModuloAsignado addModuloAsignado(ModuloAsignado moduloAsignado) {
		getModuloAsignados().add(moduloAsignado);
		moduloAsignado.setPeriodo(this);

		return moduloAsignado;
	}

	public ModuloAsignado removeModuloAsignado(ModuloAsignado moduloAsignado) {
		getModuloAsignados().remove(moduloAsignado);
		moduloAsignado.setPeriodo(null);

		return moduloAsignado;
	}

	public String getEstadoPeriodo() {
		return estadoPeriodo;
	}

	public void setEstadoPeriodo(String estadoPeriodo) {
		this.estadoPeriodo = estadoPeriodo;
	}

	@Override
	public String toString() {
		return "Periodo \n[idPeriodo=" + idPeriodo + ", \ndescripcion=" + descripcion + ", \nestado=" + estado
				+ ", \nfechaFin=" + fechaFin + ", \nfechaInicio=" + fechaInicio + ", \nestadoPeriodo=" + estadoPeriodo
				+ ", \nevaluacions=" + evaluacions + ", \nmatriculas=" + matriculas + ", \nmoduloAsignados="
				+ moduloAsignados + "]";
	}

}