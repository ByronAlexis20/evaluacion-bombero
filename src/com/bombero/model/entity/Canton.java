package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="tbl_canton")
@NamedQuery(name="Canton.buscarPorProvincia", query="SELECT c FROM Canton c where c.provincia.idProvincia = :idProvincia")
public class Canton implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_canton")
	private int idCanton;

	private String canton;

	private String estado;

	//bi-directional many-to-one association to Aspirante
	@OneToMany(mappedBy="cantonNacimiento")
	private List<Aspirante> aspirantesNacimiento;

	//bi-directional many-to-one association to Aspirante
	@OneToMany(mappedBy="cantonResidencia")
	private List<Aspirante> aspirantesResidencia;

	//bi-directional many-to-one association to Provincia
	@ManyToOne
	@JoinColumn(name="id_provincia")
	private Provincia provincia;

	public Canton() {
	}

	public int getIdCanton() {
		return this.idCanton;
	}

	public void setIdCanton(int idCanton) {
		this.idCanton = idCanton;
	}

	public String getCanton() {
		return this.canton;
	}

	public void setCanton(String canton) {
		this.canton = canton;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<Aspirante> getAspirantesNacimiento() {
		return aspirantesNacimiento;
	}

	public void setAspirantesNacimiento(List<Aspirante> aspirantesNacimiento) {
		this.aspirantesNacimiento = aspirantesNacimiento;
	}

	public Aspirante addAspirantesNacimiento(Aspirante aspirantesNacimiento) {
		getAspirantesNacimiento().add(aspirantesNacimiento);
		aspirantesNacimiento.setCantonNacimiento(this);
		return aspirantesNacimiento;
	}

	public Aspirante removeAspirantesNacimiento(Aspirante aspirantesNacimiento) {
		getAspirantesNacimiento().remove(aspirantesNacimiento);
		aspirantesNacimiento.setCantonNacimiento(null);
		return aspirantesNacimiento;
	}

	public List<Aspirante> getAspirantesResidencia() {
		return aspirantesResidencia;
	}

	public void setAspirantesResidencia(List<Aspirante> aspirantesResidencia) {
		this.aspirantesResidencia = aspirantesResidencia;
	}

	public Aspirante addAspirantesResidencia(Aspirante aspirantes2) {
		getAspirantesResidencia().add(aspirantes2);
		aspirantes2.setCantonResidencia(this);

		return aspirantes2;
	}

	public Aspirante removeAspirantesResidencia(Aspirante aspirantes2) {
		getAspirantesResidencia().remove(aspirantes2);
		aspirantes2.setCantonResidencia(null);

		return aspirantes2;
	}

	public Provincia getProvincia() {
		return this.provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	@Override
	public String toString() {
		return "Canton [idCanton=" + idCanton + ", canton=" + canton + ", estado=" + estado + ", provincia=" + provincia
				+ "]";
	}

}