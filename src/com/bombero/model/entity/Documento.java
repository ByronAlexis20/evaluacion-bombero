package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the documento database table.
 * 
 */
@Entity
@NamedQuery(name="Documento.buscarPorAspirante", query="SELECT d FROM Documento d where d.aspirante.idAspirante = :idAspirante and d.estado = 'A'")
public class Documento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_documento")
	private Integer idDocumento;

	private String estado;

	@Column(name="ruta_documento")
	private String rutaDocumento;

	//bi-directional many-to-one association to Aspirante
	@ManyToOne
	@JoinColumn(name="id_aspirante")
	private Aspirante aspirante;

	//bi-directional many-to-one association to TipoDocumento
	@ManyToOne
	@JoinColumn(name="id_tipo_documento")
	private TipoDocumento tipoDocumento;

	public Documento() {
	}

	public Integer getIdDocumento() {
		return this.idDocumento;
	}

	public void setIdDocumento(Integer idDocumento) {
		this.idDocumento = idDocumento;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getRutaDocumento() {
		return this.rutaDocumento;
	}

	public void setRutaDocumento(String rutaDocumento) {
		this.rutaDocumento = rutaDocumento;
	}

	public Aspirante getAspirante() {
		return this.aspirante;
	}

	public void setAspirante(Aspirante aspirante) {
		this.aspirante = aspirante;
	}

	public TipoDocumento getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

}