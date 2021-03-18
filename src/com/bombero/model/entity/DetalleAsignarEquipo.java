package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the detalle_asignar_equipo database table.
 * 
 */
@Entity
@Table(name="detalle_asignar_equipo")
@NamedQuery(name="DetalleAsignarEquipo.findAll", query="SELECT d FROM DetalleAsignarEquipo d")
public class DetalleAsignarEquipo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_detalle")
	private int idDetalle;

	private int cantidad;

	private String estado;

	private float precio;

	//bi-directional many-to-one association to CabeceraAsignarEquipo
	@ManyToOne
	@JoinColumn(name="id_asignar")
	private CabeceraAsignarEquipo cabeceraAsignarEquipo;

	//bi-directional many-to-one association to EquipoVestimenta
	@ManyToOne
	@JoinColumn(name="id_equipo")
	private EquipoVestimenta equipoVestimenta;

	public DetalleAsignarEquipo() {
	}

	public int getIdDetalle() {
		return this.idDetalle;
	}

	public void setIdDetalle(int idDetalle) {
		this.idDetalle = idDetalle;
	}

	public int getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public float getPrecio() {
		return this.precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public CabeceraAsignarEquipo getCabeceraAsignarEquipo() {
		return this.cabeceraAsignarEquipo;
	}

	public void setCabeceraAsignarEquipo(CabeceraAsignarEquipo cabeceraAsignarEquipo) {
		this.cabeceraAsignarEquipo = cabeceraAsignarEquipo;
	}

	public EquipoVestimenta getEquipoVestimenta() {
		return this.equipoVestimenta;
	}

	public void setEquipoVestimenta(EquipoVestimenta equipoVestimenta) {
		this.equipoVestimenta = equipoVestimenta;
	}

}