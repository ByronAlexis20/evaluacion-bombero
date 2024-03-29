package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="tbl_aspirante")
@NamedQuery(name="Aspirante.findAll", query="SELECT a FROM Aspirante a")
public class Aspirante implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_aspirante")
	private Integer idAspirante;

	private String apellidos;

	private String cedula;

	private String celular;

	private String correo;

	@Column(name="direccion_domiciliaria")
	private String direccionDomiciliaria;

	private String estado;

	private double estatura;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_nacimiento")
	private Date fechaNacimiento;

	@Column(name="no_hijos")
	private int noHijos;

	@Column(name="no_libreta_militar")
	private String noLibretaMilitar;
	
	@Column(name="nombre_conyuge")
	private String nombreConyuge;

	private String nombres;

	@Column(name="numero_solar")
	private String numeroSolar;

	@Column(name="referencia_domiciliaria")
	private String referenciaDomiciliaria;

	private String telefono;

	@Column(name="ultimo_anio_estudio")
	private int ultimoAnioEstudio;

	//bi-directional many-to-one association to Canton
	@ManyToOne
	@JoinColumn(name="id_canton_nacimiento")
	private Canton cantonNacimiento;

	//bi-directional many-to-one association to Canton
	@ManyToOne
	@JoinColumn(name="id_canton_residencia")
	private Canton cantonResidencia;

	//bi-directional many-to-one association to EstadoCivil
	@ManyToOne
	@JoinColumn(name="id_estado_civil")
	private EstadoCivil estadoCivil;

	//bi-directional many-to-one association to Genero
	@ManyToOne
	@JoinColumn(name="id_genero")
	private Genero genero;

	//bi-directional many-to-one association to Profesion
	@ManyToOne
	@JoinColumn(name="id_profesion")
	private Profesion profesion;

	//bi-directional many-to-one association to TipoSangre
	@ManyToOne
	@JoinColumn(name="id_tipo_sangre")
	private TipoSangre tipoSangre;
	
	@ManyToOne
	@JoinColumn(name="id_instruccion")
	private Instruccion instruccion;

	//bi-directional many-to-one association to Documento
	@OneToMany(mappedBy="aspirante", cascade = CascadeType.ALL)
	private List<Documento> documentos;

	//bi-directional many-to-one association to FichaMedica
	@OneToMany(mappedBy="aspirante", cascade = CascadeType.ALL)
	private List<FichaMedica> fichaMedicas;

	//bi-directional many-to-one association to Matricula
	@OneToMany(mappedBy="aspirante", cascade = CascadeType.ALL)
	private List<Matricula> matriculas;

	//bi-directional many-to-one association to PersonalAutorizado
	@OneToMany(mappedBy="aspirante", cascade = CascadeType.ALL)
	private List<PersonalAutorizado> personalAutorizados;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="aspirante", cascade = CascadeType.ALL)
	private List<Usuario> usuarios;

	public Aspirante() {
	}

	public Integer getIdAspirante() {
		return this.idAspirante;
	}

	public void setIdAspirante(Integer idAspirante) {
		this.idAspirante = idAspirante;
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

	public String getCelular() {
		return this.celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDireccionDomiciliaria() {
		return this.direccionDomiciliaria;
	}

	public void setDireccionDomiciliaria(String direccionDomiciliaria) {
		this.direccionDomiciliaria = direccionDomiciliaria;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public double getEstatura() {
		return this.estatura;
	}

	public void setEstatura(double estatura) {
		this.estatura = estatura;
	}

	public Date getFechaNacimiento() {
		return this.fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public int getNoHijos() {
		return this.noHijos;
	}

	public void setNoHijos(int noHijos) {
		this.noHijos = noHijos;
	}

	public String getNoLibretaMilitar() {
		return this.noLibretaMilitar;
	}

	public void setNoLibretaMilitar(String noLibretaMilitar) {
		this.noLibretaMilitar = noLibretaMilitar;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getNumeroSolar() {
		return this.numeroSolar;
	}

	public void setNumeroSolar(String numeroSolar) {
		this.numeroSolar = numeroSolar;
	}

	public String getReferenciaDomiciliaria() {
		return this.referenciaDomiciliaria;
	}

	public void setReferenciaDomiciliaria(String referenciaDomiciliaria) {
		this.referenciaDomiciliaria = referenciaDomiciliaria;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public int getUltimoAnioEstudio() {
		return this.ultimoAnioEstudio;
	}

	public void setUltimoAnioEstudio(int ultimoAnioEstudio) {
		this.ultimoAnioEstudio = ultimoAnioEstudio;
	}

	public Canton getCantonNacimiento() {
		return cantonNacimiento;
	}

	public void setCantonNacimiento(Canton cantonNacimiento) {
		this.cantonNacimiento = cantonNacimiento;
	}

	public Canton getCantonResidencia() {
		return cantonResidencia;
	}

	public void setCantonResidencia(Canton cantonResidencia) {
		this.cantonResidencia = cantonResidencia;
	}

	public EstadoCivil getEstadoCivil() {
		return this.estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public Genero getGenero() {
		return this.genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public Profesion getProfesion() {
		return this.profesion;
	}

	public void setProfesion(Profesion profesion) {
		this.profesion = profesion;
	}

	public TipoSangre getTipoSangre() {
		return this.tipoSangre;
	}

	public void setTipoSangre(TipoSangre tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	public List<Documento> getDocumentos() {
		return this.documentos;
	}

	public void setDocumentos(List<Documento> documentos) {
		this.documentos = documentos;
	}

	public Documento addDocumento(Documento documento) {
		getDocumentos().add(documento);
		documento.setAspirante(this);

		return documento;
	}

	public Documento removeDocumento(Documento documento) {
		getDocumentos().remove(documento);
		documento.setAspirante(null);

		return documento;
	}

	public List<FichaMedica> getFichaMedicas() {
		return this.fichaMedicas;
	}

	public void setFichaMedicas(List<FichaMedica> fichaMedicas) {
		this.fichaMedicas = fichaMedicas;
	}

	public FichaMedica addFichaMedica(FichaMedica fichaMedica) {
		getFichaMedicas().add(fichaMedica);
		fichaMedica.setAspirante(this);

		return fichaMedica;
	}

	public FichaMedica removeFichaMedica(FichaMedica fichaMedica) {
		getFichaMedicas().remove(fichaMedica);
		fichaMedica.setAspirante(null);

		return fichaMedica;
	}

	public List<Matricula> getMatriculas() {
		return this.matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	public Matricula addMatricula(Matricula matricula) {
		getMatriculas().add(matricula);
		matricula.setAspirante(this);

		return matricula;
	}

	public Matricula removeMatricula(Matricula matricula) {
		getMatriculas().remove(matricula);
		matricula.setAspirante(null);

		return matricula;
	}

	public List<PersonalAutorizado> getPersonalAutorizados() {
		return this.personalAutorizados;
	}

	public void setPersonalAutorizados(List<PersonalAutorizado> personalAutorizados) {
		this.personalAutorizados = personalAutorizados;
	}

	public PersonalAutorizado addPersonalAutorizado(PersonalAutorizado personalAutorizado) {
		getPersonalAutorizados().add(personalAutorizado);
		personalAutorizado.setAspirante(this);

		return personalAutorizado;
	}

	public PersonalAutorizado removePersonalAutorizado(PersonalAutorizado personalAutorizado) {
		getPersonalAutorizados().remove(personalAutorizado);
		personalAutorizado.setAspirante(null);

		return personalAutorizado;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setAspirante(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setAspirante(null);

		return usuario;
	}

	public Instruccion getInstruccion() {
		return instruccion;
	}

	public void setInstruccion(Instruccion instruccion) {
		this.instruccion = instruccion;
	}

	public String getNombreConyuge() {
		return nombreConyuge;
	}

	public void setNombreConyuge(String nombreConyuge) {
		this.nombreConyuge = nombreConyuge;
	}

	@Override
	public String toString() {
		return "Aspirante \n[idAspirante=" + idAspirante + ", \napellidos=" + apellidos + ", \ncedula=" + cedula
				+ ", \ncelular=" + celular + ", \ncorreo=" + correo + ", \ndireccionDomiciliaria="
				+ direccionDomiciliaria + ", \nestado=" + estado + ", \nestatura=" + estatura + ", \nfechaNacimiento="
				+ fechaNacimiento + ", \nnoHijos=" + noHijos + ", \nnoLibretaMilitar=" + noLibretaMilitar
				+ ", \nnombreConyuge=" + nombreConyuge + ", \nnombres=" + nombres + ", \nnumeroSolar=" + numeroSolar
				+ ", \nreferenciaDomiciliaria=" + referenciaDomiciliaria + ", \ntelefono=" + telefono
				+ ", \nultimoAnioEstudio=" + ultimoAnioEstudio + ", \ncantonNacimiento=" + cantonNacimiento
				+ ", \ncantonResidencia=" + cantonResidencia + ", \nestadoCivil=" + estadoCivil + ", \ngenero=" + genero
				+ ", \nprofesion=" + profesion + ", \ntipoSangre=" + tipoSangre + ", \ninstruccion=" + instruccion
				+ ", \ndocumentos=" + documentos + ", \nfichaMedicas=" + fichaMedicas + ", \nmatriculas=" + matriculas
				+ ", \npersonalAutorizados=" + personalAutorizados + ", \nusuarios=" + usuarios + "]";
	}

}