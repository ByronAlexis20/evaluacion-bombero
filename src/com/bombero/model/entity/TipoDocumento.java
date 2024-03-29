package com.bombero.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tbl_tipo_documento")
@NamedQuery(name="TipoDocumento.buscarPorPatron", query="SELECT t FROM TipoDocumento t where lower(t.tipoDocumento) like lower(:patron)")
public class TipoDocumento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipo_documento")
	private Integer idTipoDocumento;

	private String estado;

	@Column(name="tipo_documento")
	private String tipoDocumento;
	
	@Column(name="iniciales_archivo")
	private String inicialesArchivo;

	//bi-directional many-to-one association to Documento
	@OneToMany(mappedBy="tipoDocumento")
	private List<Documento> documentos;

	public TipoDocumento() {
	}

	public Integer getIdTipoDocumento() {
		return this.idTipoDocumento;
	}

	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public List<Documento> getDocumentos() {
		return this.documentos;
	}

	public void setDocumentos(List<Documento> documentos) {
		this.documentos = documentos;
	}

	public Documento addDocumento(Documento documento) {
		getDocumentos().add(documento);
		documento.setTipoDocumento(this);

		return documento;
	}

	public Documento removeDocumento(Documento documento) {
		getDocumentos().remove(documento);
		documento.setTipoDocumento(null);

		return documento;
	}

	public String getInicialesArchivo() {
		return inicialesArchivo;
	}

	public void setInicialesArchivo(String inicialesArchivo) {
		this.inicialesArchivo = inicialesArchivo;
	}

}