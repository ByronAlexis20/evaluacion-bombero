package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="tbl_calificacion")
@NamedQueries({
	@NamedQuery(name="Calificacion.obtenerCalificacionPorMatriculaYModulo", query="SELECT c FROM Calificacion c where c.modulo.idModulo = :idModulo and c.matricula.idMatricula = :idMatricula and c.estado = 'A'"),
	@NamedQuery(name="Calificacion.buscarPorAspirante", query="SELECT c FROM Calificacion c where c.matricula.idMatricula = :idMatricula and c.estado = 'A'"),
	@NamedQuery(name="Calificacion.buscarPorMatriculaYModulo", query="SELECT c FROM Calificacion c where c.matricula.idMatricula = :idMatricula and c.modulo.idModulo = :idModulo and c.estado = 'A'"),
	@NamedQuery(name="Calificacion.buscarPorMatricula", query="SELECT c FROM Calificacion c where c.matricula.idMatricula = :idMatricula and c.estado = 'A'"),
})
public class Calificacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_calificacion")
	private Integer idCalificacion;

	private String estado;

	private float examen;

	@Column(name="nota_1")
	private float nota1;

	@Column(name="nota_2")
	private float nota2;

	@Column(name="nota_3")
	private float nota3;

	@Column(name="nota_4")
	private float nota4;

	@Column(name="nota_final")
	private float notaFinal;

	//bi-directional many-to-one association to ModuloAsignado
	@ManyToOne
	@JoinColumn(name="id_modulo")
	private Modulo modulo;

	@ManyToOne
	@JoinColumn(name="id_matricula")
	private Matricula matricula;
	
	public Calificacion() {
	}

	public Integer getIdCalificacion() {
		return this.idCalificacion;
	}

	public void setIdCalificacion(Integer idCalificacion) {
		this.idCalificacion = idCalificacion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public float getExamen() {
		return this.examen;
	}

	public void setExamen(float examen) {
		this.examen = examen;
	}

	public float getNota1() {
		return this.nota1;
	}

	public void setNota1(float nota1) {
		this.nota1 = nota1;
	}

	public float getNota2() {
		return this.nota2;
	}

	public void setNota2(float nota2) {
		this.nota2 = nota2;
	}

	public float getNota3() {
		return this.nota3;
	}

	public void setNota3(float nota3) {
		this.nota3 = nota3;
	}

	public float getNota4() {
		return this.nota4;
	}

	public void setNota4(float nota4) {
		this.nota4 = nota4;
	}

	public float getNotaFinal() {
		return this.notaFinal;
	}

	public void setNotaFinal(float notaFinal) {
		this.notaFinal = notaFinal;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	public Matricula getMatricula() {
		return matricula;
	}

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}

	@Override
	public String toString() {
		return "Calificacion \n[idCalificacion=" + idCalificacion + ", \nestado=" + estado + ", \nexamen=" + examen
				+ ", \nnota1=" + nota1 + ", \nnota2=" + nota2 + ", \nnota3=" + nota3 + ", \nnota4=" + nota4
				+ ", \nnotaFinal=" + notaFinal + ", \nmodulo=" + modulo + ", \nmatricula=" + matricula + "]";
	}
	
}