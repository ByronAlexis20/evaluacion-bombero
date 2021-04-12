package com.bombero.control.registros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import com.bombero.model.dao.CantonDAO;
import com.bombero.model.dao.EstadoCivilDAO;
import com.bombero.model.dao.GeneroDAO;
import com.bombero.model.dao.InstruccionDAO;
import com.bombero.model.dao.PaisDAO;
import com.bombero.model.dao.ProfesionDAO;
import com.bombero.model.dao.ProvinciaDAO;
import com.bombero.model.dao.TipoSangreDAO;
import com.bombero.model.entity.Aspirante;
import com.bombero.model.entity.Canton;
import com.bombero.model.entity.EstadoCivil;
import com.bombero.model.entity.Genero;
import com.bombero.model.entity.Instruccion;
import com.bombero.model.entity.Matricula;
import com.bombero.model.entity.Pai;
import com.bombero.model.entity.Profesion;
import com.bombero.model.entity.Provincia;
import com.bombero.model.entity.TipoSangre;

public class AspiranteEditarC {
	@Wire Combobox cboProvincia;
	@Wire Combobox cboCanton;
	@Wire Combobox cboProvinciaResidencia;
	@Wire Combobox cboCantonResidencia;
	
	PaisDAO paisDAO = new PaisDAO();
	GeneroDAO generoDAO = new GeneroDAO();
	TipoSangreDAO tipoSangreDAO = new TipoSangreDAO();
	EstadoCivilDAO estadoivilDAO = new EstadoCivilDAO();
	InstruccionDAO instruccionDAO = new InstruccionDAO();
	ProfesionDAO profesionDAO = new ProfesionDAO();
	Pai paisSeleccionado;
	Genero generoSeleccionado;
	TipoSangre tipoSangreSeleccionado;
	EstadoCivil estadoCivilSeleccionado;
	Instruccion instruccionSeleccionado;
	Profesion profesionSeleccionado;
	
	Aspirante aspirante;
	Matricula matricula;
	
	//para los combos anidados
	List<Provincia> listaProvincia = new ArrayList<>();
	Provincia provinciaSeleccionada;
	ProvinciaDAO provinciaDAO = new ProvinciaDAO();
	
	List<Canton> listaCanton = new ArrayList<>();
	Canton cantonSeleccionado;
	CantonDAO cantonDAO = new CantonDAO();
	
	List<Provincia> listaProvinciaResidencia = new ArrayList<>();
	Provincia provinciaResidenciaSeleccionada;
	
	List<Canton> listaCantonResidencia = new ArrayList<>();
	Canton cantonResidenciaSeleccionado;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		matricula = (Matricula) Executions.getCurrent().getArg().get("Matricula");
		if (matricula == null) {
			matricula = new Matricula();
			matricula.setEstado("A");
			paisSeleccionado = null;
			generoSeleccionado = null;
			tipoSangreSeleccionado = null;
			estadoCivilSeleccionado = null;
			instruccionSeleccionado = null;
			profesionSeleccionado = null;
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@NotifyChange({"listaProvincia"})
	@Command
	public void seleccionarPais(){
		if(listaCanton != null)
			listaCanton = new ArrayList<>();
		if(listaProvincia != null)
			listaProvincia = null;
		if(listaProvinciaResidencia != null)
			listaProvinciaResidencia = null;
		provinciaSeleccionada = null;
		provinciaResidenciaSeleccionada = null;
		cantonSeleccionado = null;
		listaProvincia = provinciaDAO.buscarProvinciaPorIdPais(paisSeleccionado.getIdPais());
		cboProvincia.setModel(new ListModelList(listaProvincia));
		cboProvinciaResidencia.setModel(new ListModelList(listaProvincia));
		cboCanton.setModel(new ListModelList(listaCanton));
		cboProvincia.setText("");
		cboCanton.setText("");
		cboProvinciaResidencia.setText("");
		cboCantonResidencia.setText("");
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@NotifyChange({"listaCanton"})
	@Command
	public void seleccionarProvincia(){
		if(listaCanton != null)
			listaCanton = null;
		listaCanton = cantonDAO.buscarPorIdProvincia(provinciaSeleccionada.getIdProvincia());
		cboCanton.setModel(new ListModelList(listaCanton));
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@NotifyChange({"listaCanton"})
	@Command
	public void seleccionarProvinciaResidencia(){
		if(listaCantonResidencia != null)
			listaCantonResidencia = null;
		listaCantonResidencia = cantonDAO.buscarPorIdProvincia(provinciaResidenciaSeleccionada.getIdProvincia());
		cboCantonResidencia.setModel(new ListModelList(listaCantonResidencia));
	}
	@Command
	public void documentos() {
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/registros/documentoAspirante.zul", null, null);
		ventanaCargar.doModal();
	}
	
	public List<Pai> getPaises(){
		return paisDAO.getPaises();
	}
	public List<Genero> getGeneros(){
		return generoDAO.getGeneros();
	}
	public List<TipoSangre> getTipoSangres(){
		return tipoSangreDAO.getTipoSangrePorDescripcion("");
	}
	public List<EstadoCivil> getEstadoCivils(){
		return estadoivilDAO.getEstadoCivils();
	}
	public List<Instruccion> getInstrucciones(){
		return instruccionDAO.getInstrucciones();
	}
	public List<Profesion> getProfesiones(){
		return profesionDAO.getProfesionPorDescripcion("");
	}
	public Pai getPaisSeleccionado() {
		return paisSeleccionado;
	}
	public void setPaisSeleccionado(Pai paisSeleccionado) {
		this.paisSeleccionado = paisSeleccionado;
	}
	public Genero getGeneroSeleccionado() {
		return generoSeleccionado;
	}
	public void setGeneroSeleccionado(Genero generoSeleccionado) {
		this.generoSeleccionado = generoSeleccionado;
	}
	public TipoSangre getTipoSangreSeleccionado() {
		return tipoSangreSeleccionado;
	}
	public void setTipoSangreSeleccionado(TipoSangre tipoSangreSeleccionado) {
		this.tipoSangreSeleccionado = tipoSangreSeleccionado;
	}
	public EstadoCivil getEstadoCivilSeleccionado() {
		return estadoCivilSeleccionado;
	}
	public void setEstadoCivilSeleccionado(EstadoCivil estadoCivilSeleccionado) {
		this.estadoCivilSeleccionado = estadoCivilSeleccionado;
	}
	public Instruccion getInstruccionSeleccionado() {
		return instruccionSeleccionado;
	}
	public void setInstruccionSeleccionado(Instruccion instruccionSeleccionado) {
		this.instruccionSeleccionado = instruccionSeleccionado;
	}
	public Profesion getProfesionSeleccionado() {
		return profesionSeleccionado;
	}
	public void setProfesionSeleccionado(Profesion profesionSeleccionado) {
		this.profesionSeleccionado = profesionSeleccionado;
	}
	public Aspirante getAspirante() {
		return aspirante;
	}
	public void setAspirante(Aspirante aspirante) {
		this.aspirante = aspirante;
	}
	public Matricula getMatricula() {
		return matricula;
	}
	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
	public List<Provincia> getListaProvincia() {
		return listaProvincia;
	}
	public void setListaProvincia(List<Provincia> listaProvincia) {
		this.listaProvincia = listaProvincia;
	}
	public Provincia getProvinciaSeleccionada() {
		return provinciaSeleccionada;
	}
	public void setProvinciaSeleccionada(Provincia provinciaSeleccionada) {
		this.provinciaSeleccionada = provinciaSeleccionada;
	}
	public List<Canton> getListaCanton() {
		return listaCanton;
	}
	public void setListaCanton(List<Canton> listaCanton) {
		this.listaCanton = listaCanton;
	}
	public Canton getCantonSeleccionado() {
		return cantonSeleccionado;
	}
	public void setCantonSeleccionado(Canton cantonSeleccionado) {
		this.cantonSeleccionado = cantonSeleccionado;
	}
	public List<Provincia> getListaProvinciaResidencia() {
		return listaProvinciaResidencia;
	}
	public void setListaProvinciaResidencia(List<Provincia> listaProvinciaResidencia) {
		this.listaProvinciaResidencia = listaProvinciaResidencia;
	}
	public Provincia getProvinciaResidenciaSeleccionada() {
		return provinciaResidenciaSeleccionada;
	}
	public void setProvinciaResidenciaSeleccionada(Provincia provinciaResidenciaSeleccionada) {
		this.provinciaResidenciaSeleccionada = provinciaResidenciaSeleccionada;
	}
	public List<Canton> getListaCantonResidencia() {
		return listaCantonResidencia;
	}
	public void setListaCantonResidencia(List<Canton> listaCantonResidencia) {
		this.listaCantonResidencia = listaCantonResidencia;
	}
	public Canton getCantonResidenciaSeleccionado() {
		return cantonResidenciaSeleccionado;
	}
	public void setCantonResidenciaSeleccionado(Canton cantonResidenciaSeleccionado) {
		this.cantonResidenciaSeleccionado = cantonResidenciaSeleccionado;
	}
}
