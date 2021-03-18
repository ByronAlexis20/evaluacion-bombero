package com.bombero.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ficha_medica database table.
 * 
 */
@Entity
@Table(name="ficha_medica")
@NamedQuery(name="FichaMedica.findAll", query="SELECT f FROM FichaMedica f")
public class FichaMedica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_ficha_medica")
	private int idFichaMedica;

	private String estado;

	@Column(name="no_abortos")
	private int noAbortos;

	@Column(name="no_cesarea")
	private int noCesarea;

	@Column(name="no_hijos")
	private int noHijos;

	@Column(name="no_partos")
	private int noPartos;

	@Column(name="nombre_alimentos_alergia")
	private String nombreAlimentosAlergia;

	@Column(name="nombre_medicinas_alergia")
	private String nombreMedicinasAlergia;

	@Column(name="nombre_vacunas")
	private String nombreVacunas;

	@Column(name="ruta_coproparasitario")
	private String rutaCoproparasitario;

	@Column(name="ruta_electrocardiograma")
	private String rutaElectrocardiograma;

	@Column(name="ruta_examen_orina")
	private String rutaExamenOrina;

	@Column(name="ruta_examen_sangre")
	private String rutaExamenSangre;

	@Column(name="ruta_ficha_medica")
	private String rutaFichaMedica;

	@Column(name="ruta_radiografia_torax")
	private String rutaRadiografiaTorax;

	//bi-directional many-to-one association to Cirugia
	@OneToMany(mappedBy="fichaMedica")
	private List<Cirugia> cirugias;

	//bi-directional many-to-one association to Familiar
	@OneToMany(mappedBy="fichaMedica")
	private List<Familiar> familiars;

	//bi-directional many-to-one association to Aspirante
	@ManyToOne
	@JoinColumn(name="id_aspirante")
	private Aspirante aspirante;

	public FichaMedica() {
	}

	public int getIdFichaMedica() {
		return this.idFichaMedica;
	}

	public void setIdFichaMedica(int idFichaMedica) {
		this.idFichaMedica = idFichaMedica;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getNoAbortos() {
		return this.noAbortos;
	}

	public void setNoAbortos(int noAbortos) {
		this.noAbortos = noAbortos;
	}

	public int getNoCesarea() {
		return this.noCesarea;
	}

	public void setNoCesarea(int noCesarea) {
		this.noCesarea = noCesarea;
	}

	public int getNoHijos() {
		return this.noHijos;
	}

	public void setNoHijos(int noHijos) {
		this.noHijos = noHijos;
	}

	public int getNoPartos() {
		return this.noPartos;
	}

	public void setNoPartos(int noPartos) {
		this.noPartos = noPartos;
	}

	public String getNombreAlimentosAlergia() {
		return this.nombreAlimentosAlergia;
	}

	public void setNombreAlimentosAlergia(String nombreAlimentosAlergia) {
		this.nombreAlimentosAlergia = nombreAlimentosAlergia;
	}

	public String getNombreMedicinasAlergia() {
		return this.nombreMedicinasAlergia;
	}

	public void setNombreMedicinasAlergia(String nombreMedicinasAlergia) {
		this.nombreMedicinasAlergia = nombreMedicinasAlergia;
	}

	public String getNombreVacunas() {
		return this.nombreVacunas;
	}

	public void setNombreVacunas(String nombreVacunas) {
		this.nombreVacunas = nombreVacunas;
	}

	public String getRutaCoproparasitario() {
		return this.rutaCoproparasitario;
	}

	public void setRutaCoproparasitario(String rutaCoproparasitario) {
		this.rutaCoproparasitario = rutaCoproparasitario;
	}

	public String getRutaElectrocardiograma() {
		return this.rutaElectrocardiograma;
	}

	public void setRutaElectrocardiograma(String rutaElectrocardiograma) {
		this.rutaElectrocardiograma = rutaElectrocardiograma;
	}

	public String getRutaExamenOrina() {
		return this.rutaExamenOrina;
	}

	public void setRutaExamenOrina(String rutaExamenOrina) {
		this.rutaExamenOrina = rutaExamenOrina;
	}

	public String getRutaExamenSangre() {
		return this.rutaExamenSangre;
	}

	public void setRutaExamenSangre(String rutaExamenSangre) {
		this.rutaExamenSangre = rutaExamenSangre;
	}

	public String getRutaFichaMedica() {
		return this.rutaFichaMedica;
	}

	public void setRutaFichaMedica(String rutaFichaMedica) {
		this.rutaFichaMedica = rutaFichaMedica;
	}

	public String getRutaRadiografiaTorax() {
		return this.rutaRadiografiaTorax;
	}

	public void setRutaRadiografiaTorax(String rutaRadiografiaTorax) {
		this.rutaRadiografiaTorax = rutaRadiografiaTorax;
	}

	public List<Cirugia> getCirugias() {
		return this.cirugias;
	}

	public void setCirugias(List<Cirugia> cirugias) {
		this.cirugias = cirugias;
	}

	public Cirugia addCirugia(Cirugia cirugia) {
		getCirugias().add(cirugia);
		cirugia.setFichaMedica(this);

		return cirugia;
	}

	public Cirugia removeCirugia(Cirugia cirugia) {
		getCirugias().remove(cirugia);
		cirugia.setFichaMedica(null);

		return cirugia;
	}

	public List<Familiar> getFamiliars() {
		return this.familiars;
	}

	public void setFamiliars(List<Familiar> familiars) {
		this.familiars = familiars;
	}

	public Familiar addFamiliar(Familiar familiar) {
		getFamiliars().add(familiar);
		familiar.setFichaMedica(this);

		return familiar;
	}

	public Familiar removeFamiliar(Familiar familiar) {
		getFamiliars().remove(familiar);
		familiar.setFichaMedica(null);

		return familiar;
	}

	public Aspirante getAspirante() {
		return this.aspirante;
	}

	public void setAspirante(Aspirante aspirante) {
		this.aspirante = aspirante;
	}

}