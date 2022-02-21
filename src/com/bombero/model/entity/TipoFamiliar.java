package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="tbl_tipo_familiar")
@NamedQueries({
	@NamedQuery(name="TipoFamiliar.buscarPorPatron", query="SELECT t FROM TipoFamiliar t where lower(t.tipoFamiliar) like lower(:patron)"),
	@NamedQuery(name="TipoFamiliar.buscarPorEstado", query="SELECT t FROM TipoFamiliar t where t.estado = :estado")
})
public class TipoFamiliar implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipo_familiar")
	private Integer idTipoFamiliar;

	private String estado;

	@Column(name="tipo_familiar")
	private String tipoFamiliar;

	//bi-directional many-to-one association to Familiar
	@OneToMany(mappedBy="tipoFamiliar")
	private List<Familiar> familiars;

	public TipoFamiliar() {
	}

	public Integer getIdTipoFamiliar() {
		return this.idTipoFamiliar;
	}

	public void setIdTipoFamiliar(Integer idTipoFamiliar) {
		this.idTipoFamiliar = idTipoFamiliar;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getTipoFamiliar() {
		return this.tipoFamiliar;
	}

	public void setTipoFamiliar(String tipoFamiliar) {
		this.tipoFamiliar = tipoFamiliar;
	}

	public List<Familiar> getFamiliars() {
		return this.familiars;
	}

	public void setFamiliars(List<Familiar> familiars) {
		this.familiars = familiars;
	}

	public Familiar addFamiliar(Familiar familiar) {
		getFamiliars().add(familiar);
		familiar.setTipoFamiliar(this);

		return familiar;
	}

	public Familiar removeFamiliar(Familiar familiar) {
		getFamiliars().remove(familiar);
		familiar.setTipoFamiliar(null);

		return familiar;
	}

}