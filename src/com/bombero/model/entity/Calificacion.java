package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the calificacion database table.
 * 
 */
@Entity
@NamedQuery(name="Calificacion.findAll", query="SELECT c FROM Calificacion c")
public class Calificacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_calificacion")
	private int idCalificacion;

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
	@JoinColumn(name="id_asignacion")
	private ModuloAsignado moduloAsignado;

	public Calificacion() {
	}

	public int getIdCalificacion() {
		return this.idCalificacion;
	}

	public void setIdCalificacion(int idCalificacion) {
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

	public ModuloAsignado getModuloAsignado() {
		return this.moduloAsignado;
	}

	public void setModuloAsignado(ModuloAsignado moduloAsignado) {
		this.moduloAsignado = moduloAsignado;
	}

}