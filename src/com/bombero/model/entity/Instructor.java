package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the instructor database table.
 * 
 */
@Entity
@NamedQuery(name="Instructor.findAll", query="SELECT i FROM Instructor i")
public class Instructor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_instructor")
	private int idInstructor;

	private String apellido;

	private String cargo;

	private String cedula;

	private String codigo;

	private String estado;

	private String grado;

	private String nombre;

	//bi-directional many-to-one association to TipoSangre
	@ManyToOne
	@JoinColumn(name="id_tipo_sangre")
	private TipoSangre tipoSangre;

	//bi-directional many-to-one association to ModuloAsignado
	@OneToMany(mappedBy="instructor")
	private List<ModuloAsignado> moduloAsignados;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="instructor")
	private List<Usuario> usuarios;

	public Instructor() {
	}

	public int getIdInstructor() {
		return this.idInstructor;
	}

	public void setIdInstructor(int idInstructor) {
		this.idInstructor = idInstructor;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCargo() {
		return this.cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getGrado() {
		return this.grado;
	}

	public void setGrado(String grado) {
		this.grado = grado;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public TipoSangre getTipoSangre() {
		return this.tipoSangre;
	}

	public void setTipoSangre(TipoSangre tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	public List<ModuloAsignado> getModuloAsignados() {
		return this.moduloAsignados;
	}

	public void setModuloAsignados(List<ModuloAsignado> moduloAsignados) {
		this.moduloAsignados = moduloAsignados;
	}

	public ModuloAsignado addModuloAsignado(ModuloAsignado moduloAsignado) {
		getModuloAsignados().add(moduloAsignado);
		moduloAsignado.setInstructor(this);

		return moduloAsignado;
	}

	public ModuloAsignado removeModuloAsignado(ModuloAsignado moduloAsignado) {
		getModuloAsignados().remove(moduloAsignado);
		moduloAsignado.setInstructor(null);

		return moduloAsignado;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setInstructor(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setInstructor(null);

		return usuario;
	}

}