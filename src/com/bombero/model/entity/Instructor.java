package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="tbl_instructor")
@NamedQueries({
	@NamedQuery(name="Instructor.buscarPorPatron", query="SELECT i FROM Instructor i where (lower(i.nombre) like(:patron) or "
		+ "lower(i.apellido) like(:patron)) and i.estado = 'A'"),
	@NamedQuery(name="Instructor.buscarPorCedulaInstructor", query="SELECT i FROM Instructor i where i.cedula = :cedula and i.idInstructor <> :id and i.estado = 'A'"),
	@NamedQuery(name="Instructor.buscarPorCedula", query="SELECT i FROM Instructor i where i.cedula = :cedula and i.estado = 'A'"),
	@NamedQuery(name="Instructor.buscarSinAsignacion", query="SELECT i FROM Instructor i LEFT JOIN ModuloAsignado ma ON i.idInstructor = ma.idAsignacion and "
			+ "ma.periodo.idPeriodo = :idPeriodo and ma.estado = 'A' and i.estado = 'A' where ma.idAsignacion is null ")
})
public class Instructor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_instructor")
	private Integer idInstructor;

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

	public Integer getIdInstructor() {
		return this.idInstructor;
	}

	public void setIdInstructor(Integer idInstructor) {
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

	@Override
	public String toString() {
		return "Instructor \n[idInstructor=" + idInstructor + ", \napellido=" + apellido + ", \ncargo=" + cargo
				+ ", \ncedula=" + cedula + ", \ncodigo=" + codigo + ", \nestado=" + estado + ", \ngrado=" + grado
				+ ", \nnombre=" + nombre + ", \ntipoSangre=" + tipoSangre + ", \nmoduloAsignados=" + moduloAsignados
				+ ", \nusuarios=" + usuarios + "]";
	}

}