package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="tbl_asignar_guardia")
@NamedQueries({
	@NamedQuery(name="AsignarGuardia.buscarPorCompania", query="SELECT a FROM AsignarGuardia a where a.estado = 'A' and a.compania.idCompania = :idCompania"),
	@NamedQuery(name="AsignarGuardia.buscarAsignacion", query="SELECT a FROM AsignarGuardia a where a.estado = 'A'"),
})

public class AsignarGuardia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_guardia")
	private Integer idGuardia;

	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_fin")
	private Date fechaFin;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_ingreso")
	private Date fechaIngreso;

	private String observacion;

	//bi-directional many-to-one association to Compania
	@ManyToOne
	@JoinColumn(name="id_compania")
	private Compania compania;

	//bi-directional many-to-one association to PersonalAutorizado
	@ManyToOne
	@JoinColumn(name="id_personal")
	private PersonalAutorizado personalAutorizado;

	//bi-directional many-to-one association to CabeceraAsignarEquipo
	@OneToMany(mappedBy="asignarGuardia")
	private List<CabeceraAsignarEquipo> cabeceraAsignarEquipos;

	public AsignarGuardia() {
	}

	public Integer getIdGuardia() {
		return this.idGuardia;
	}

	public void setIdGuardia(Integer idGuardia) {
		this.idGuardia = idGuardia;
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

	public Date getFechaIngreso() {
		return this.fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Compania getCompania() {
		return this.compania;
	}

	public void setCompania(Compania compania) {
		this.compania = compania;
	}

	public PersonalAutorizado getPersonalAutorizado() {
		return this.personalAutorizado;
	}

	public void setPersonalAutorizado(PersonalAutorizado personalAutorizado) {
		this.personalAutorizado = personalAutorizado;
	}

	public List<CabeceraAsignarEquipo> getCabeceraAsignarEquipos() {
		return this.cabeceraAsignarEquipos;
	}

	public void setCabeceraAsignarEquipos(List<CabeceraAsignarEquipo> cabeceraAsignarEquipos) {
		this.cabeceraAsignarEquipos = cabeceraAsignarEquipos;
	}

	public CabeceraAsignarEquipo addCabeceraAsignarEquipo(CabeceraAsignarEquipo cabeceraAsignarEquipo) {
		getCabeceraAsignarEquipos().add(cabeceraAsignarEquipo);
		cabeceraAsignarEquipo.setAsignarGuardia(this);

		return cabeceraAsignarEquipo;
	}

	public CabeceraAsignarEquipo removeCabeceraAsignarEquipo(CabeceraAsignarEquipo cabeceraAsignarEquipo) {
		getCabeceraAsignarEquipos().remove(cabeceraAsignarEquipo);
		cabeceraAsignarEquipo.setAsignarGuardia(null);

		return cabeceraAsignarEquipo;
	}

}