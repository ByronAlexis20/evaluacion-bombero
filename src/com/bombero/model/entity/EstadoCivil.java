package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="tbl_estado_civil")
@NamedQuery(name="EstadoCivil.buscarEstadoCivil", query="SELECT e FROM EstadoCivil e where e.estado = 'A'")
public class EstadoCivil implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_estado_civil")
	private Integer idEstadoCivil;

	private String estado;

	@Column(name="estado_civil")
	private String estadoCivil;

	//bi-directional many-to-one association to Aspirante
	@OneToMany(mappedBy="estadoCivil")
	private List<Aspirante> aspirantes;

	//bi-directional many-to-one association to Familiar
	@OneToMany(mappedBy="estadoCivil")
	private List<Familiar> familiars;

	public EstadoCivil() {
	}

	public Integer getIdEstadoCivil() {
		return this.idEstadoCivil;
	}

	public void setIdEstadoCivil(Integer idEstadoCivil) {
		this.idEstadoCivil = idEstadoCivil;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstadoCivil() {
		return this.estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public List<Aspirante> getAspirantes() {
		return this.aspirantes;
	}

	public void setAspirantes(List<Aspirante> aspirantes) {
		this.aspirantes = aspirantes;
	}

	public Aspirante addAspirante(Aspirante aspirante) {
		getAspirantes().add(aspirante);
		aspirante.setEstadoCivil(this);

		return aspirante;
	}

	public Aspirante removeAspirante(Aspirante aspirante) {
		getAspirantes().remove(aspirante);
		aspirante.setEstadoCivil(null);

		return aspirante;
	}

	public List<Familiar> getFamiliars() {
		return this.familiars;
	}

	public void setFamiliars(List<Familiar> familiars) {
		this.familiars = familiars;
	}

	public Familiar addFamiliar(Familiar familiar) {
		getFamiliars().add(familiar);
		familiar.setEstadoCivil(this);

		return familiar;
	}

	public Familiar removeFamiliar(Familiar familiar) {
		getFamiliars().remove(familiar);
		familiar.setEstadoCivil(null);

		return familiar;
	}

}