package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the equipo_vestimenta database table.
 * 
 */
@Entity
@Table(name="equipo_vestimenta")
@NamedQuery(name="EquipoVestimenta.findAll", query="SELECT e FROM EquipoVestimenta e")
public class EquipoVestimenta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_equipo")
	private int idEquipo;

	private String descripcion;

	private String estado;

	@Column(name="precio_compra")
	private float precioCompra;

	private int stock;

	//bi-directional many-to-one association to DetalleAsignarEquipo
	@OneToMany(mappedBy="equipoVestimenta")
	private List<DetalleAsignarEquipo> detalleAsignarEquipos;

	public EquipoVestimenta() {
	}

	public int getIdEquipo() {
		return this.idEquipo;
	}

	public void setIdEquipo(int idEquipo) {
		this.idEquipo = idEquipo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public float getPrecioCompra() {
		return this.precioCompra;
	}

	public void setPrecioCompra(float precioCompra) {
		this.precioCompra = precioCompra;
	}

	public int getStock() {
		return this.stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public List<DetalleAsignarEquipo> getDetalleAsignarEquipos() {
		return this.detalleAsignarEquipos;
	}

	public void setDetalleAsignarEquipos(List<DetalleAsignarEquipo> detalleAsignarEquipos) {
		this.detalleAsignarEquipos = detalleAsignarEquipos;
	}

	public DetalleAsignarEquipo addDetalleAsignarEquipo(DetalleAsignarEquipo detalleAsignarEquipo) {
		getDetalleAsignarEquipos().add(detalleAsignarEquipo);
		detalleAsignarEquipo.setEquipoVestimenta(this);

		return detalleAsignarEquipo;
	}

	public DetalleAsignarEquipo removeDetalleAsignarEquipo(DetalleAsignarEquipo detalleAsignarEquipo) {
		getDetalleAsignarEquipos().remove(detalleAsignarEquipo);
		detalleAsignarEquipo.setEquipoVestimenta(null);

		return detalleAsignarEquipo;
	}

}