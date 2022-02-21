package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="tbl_cabecera_asignar_equipo")
@NamedQuery(name="CabeceraAsignarEquipo.findAll", query="SELECT c FROM CabeceraAsignarEquipo c")
public class CabeceraAsignarEquipo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_asignar")
	private int idAsignar;

	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_registro")
	private Date fechaRegistro;

	private float total;

	//bi-directional many-to-one association to AsignarGuardia
	@ManyToOne
	@JoinColumn(name="id_guardia")
	private AsignarGuardia asignarGuardia;

	//bi-directional many-to-one association to DetalleAsignarEquipo
	@OneToMany(mappedBy="cabeceraAsignarEquipo")
	private List<DetalleAsignarEquipo> detalleAsignarEquipos;

	public CabeceraAsignarEquipo() {
	}

	public int getIdAsignar() {
		return this.idAsignar;
	}

	public void setIdAsignar(int idAsignar) {
		this.idAsignar = idAsignar;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public float getTotal() {
		return this.total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public AsignarGuardia getAsignarGuardia() {
		return this.asignarGuardia;
	}

	public void setAsignarGuardia(AsignarGuardia asignarGuardia) {
		this.asignarGuardia = asignarGuardia;
	}

	public List<DetalleAsignarEquipo> getDetalleAsignarEquipos() {
		return this.detalleAsignarEquipos;
	}

	public void setDetalleAsignarEquipos(List<DetalleAsignarEquipo> detalleAsignarEquipos) {
		this.detalleAsignarEquipos = detalleAsignarEquipos;
	}

	public DetalleAsignarEquipo addDetalleAsignarEquipo(DetalleAsignarEquipo detalleAsignarEquipo) {
		getDetalleAsignarEquipos().add(detalleAsignarEquipo);
		detalleAsignarEquipo.setCabeceraAsignarEquipo(this);

		return detalleAsignarEquipo;
	}

	public DetalleAsignarEquipo removeDetalleAsignarEquipo(DetalleAsignarEquipo detalleAsignarEquipo) {
		getDetalleAsignarEquipos().remove(detalleAsignarEquipo);
		detalleAsignarEquipo.setCabeceraAsignarEquipo(null);

		return detalleAsignarEquipo;
	}

}