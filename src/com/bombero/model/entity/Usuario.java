package com.bombero.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="seg_usuario")
@NamedQueries({
	@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u"),
	@NamedQuery(name="Usuario.buscaUsuario", 
	query="SELECT u FROM Usuario u WHERE u.usuario = :nombreUsuario and u.estado = 'A'"),
	@NamedQuery(name="Usuario.buscarPorPatron", query="SELECT u FROM Usuario u where (lower(u.nombres) "
			+ "like(:patron) or lower(u.apellidos) like(:patron)) and u.estado = 'A' and "
			+ "(u.perfil.idPerfil = 1 or u.perfil.idPerfil = 2 )"),
	@NamedQuery(name="Usuario.buscarPorCedula", query="SELECT u FROM Usuario u where u.cedula = :cedula and u.estado = 'A'"),
	@NamedQuery(name="Usuario.buscarPorCedulaDiferenteAlUsuarioActual", query="SELECT u FROM Usuario u where u.cedula = :cedula and u.estado = 'A' "
			+ "and u.idUsuario <> :idUsuario"),
	@NamedQuery(name="Usuario.buscarPorUsuario", query="SELECT s FROM Usuario s where s.usuario = :patron and s.idUsuario <> :idUsuario and s.estado = 'A'"),
	@NamedQuery(name="Usuario.buscarUsuarioPorCedula", query="SELECT s FROM Usuario s where s.cedula = :cedula and s.estado = 'A'"),
	
})
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_usuario")
	private Integer idUsuario;

	private String apellidos;

	private String cedula;

	private String clave;

	private String direccion;

	private String estado;

	private String foto;

	private String nombres;

	private String telefono;

	private String usuario;

	//bi-directional many-to-one association to Aspirante
	@ManyToOne
	@JoinColumn(name="id_aspirante")
	private Aspirante aspirante;

	//bi-directional many-to-one association to Instructor
	@ManyToOne
	@JoinColumn(name="id_instructor")
	private Instructor instructor;

	//bi-directional many-to-one association to Perfil
	@ManyToOne
	@JoinColumn(name="id_perfil")
	private Perfil perfil;

	public Usuario() {
	}

	public Integer getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFoto() {
		return this.foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Aspirante getAspirante() {
		return this.aspirante;
	}

	public void setAspirante(Aspirante aspirante) {
		this.aspirante = aspirante;
	}

	public Instructor getInstructor() {
		return this.instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public Perfil getPerfil() {
		return this.perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

}