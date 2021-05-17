package com.bombero.control.registros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.CantonDAO;
import com.bombero.model.dao.EstadoCivilDAO;
import com.bombero.model.dao.GeneroDAO;
import com.bombero.model.dao.InstruccionDAO;
import com.bombero.model.dao.MatriculaDAO;
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
import com.bombero.model.entity.Periodo;
import com.bombero.model.entity.Profesion;
import com.bombero.model.entity.Provincia;
import com.bombero.model.entity.TipoSangre;
import com.bombero.util.ControllerHelper;
import com.bombero.util.Globals;

public class AspiranteEditarC {
	@Wire Window winAspirantesEditar;
	@Wire private Combobox cboProvincia;
	@Wire private Combobox cboCanton;
	@Wire private Combobox cboProvinciaResidencia;
	@Wire private Combobox cboCantonResidencia;
	
	@Wire private Textbox txtCedula;
	@Wire private Textbox txtTelefono;
	@Wire private Textbox txtCelular;
	@Wire private Textbox txtNombres;
	@Wire private Textbox txtApellidos;
	@Wire private Datebox dtpFechaNacimiento;
	@Wire private Textbox txtNoLibretaMilitar;
	@Wire private Textbox txtEstatura;
	@Wire private Textbox txtUltimoAnio;
	@Wire private Textbox txtNomnbreConyuge;
	@Wire private Textbox txtCantidadHijos;
	@Wire private Textbox txtDireccionDomiciliaria;
	@Wire private Textbox txtReferenciaDomiciliaria;
	@Wire private Textbox txtNoSolar;
	@Wire private Textbox txtCorreo;
	
	@Wire private Combobox cboNacionalidad;
	@Wire private Combobox cboGenero;
	@Wire private Combobox cboTipoSangre;
	@Wire private Combobox cboEstadoCivil;
	@Wire private Combobox cboInstruccion;
	@Wire private Combobox cboProfesion;
	
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
	Periodo periodo;
	
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
	
	MatriculaDAO matriculaDAO = new MatriculaDAO();
	ControllerHelper helper = new ControllerHelper();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		matricula = (Matricula) Executions.getCurrent().getArg().get("Matricula");
		periodo = (Periodo) Executions.getCurrent().getArg().get("Periodo");
		if (matricula == null) {
			matricula = new Matricula();
			aspirante = new Aspirante();
			matricula.setEstado("A");
			paisSeleccionado = null;
			generoSeleccionado = null;
			tipoSangreSeleccionado = null;
			estadoCivilSeleccionado = null;
			instruccionSeleccionado = null;
			profesionSeleccionado = null;
		} else {
			aspirante = matricula.getAspirante();
			recuperarDatos();
		}
	}
	private void recuperarDatos() {
		cboNacionalidad.setText(aspirante.getCantonNacimiento().getProvincia().getPai().getNacionalidad());
		paisSeleccionado = aspirante.getCantonNacimiento().getProvincia().getPai();
		seleccionarPais();
		cboProvincia.setText(aspirante.getCantonNacimiento().getProvincia().getProvincia());
		provinciaSeleccionada = aspirante.getCantonNacimiento().getProvincia();
		cboProvinciaResidencia.setText(aspirante.getCantonResidencia().getProvincia().getProvincia());
		provinciaResidenciaSeleccionada = aspirante.getCantonResidencia().getProvincia();
		
		seleccionarProvincia();
		cboCanton.setText(aspirante.getCantonNacimiento().getCanton());
		cantonSeleccionado = aspirante.getCantonNacimiento();
		
		seleccionarProvinciaResidencia();
		cboCantonResidencia.setText(aspirante.getCantonResidencia().getCanton());
		cantonResidenciaSeleccionado = aspirante.getCantonResidencia();
		
		txtCedula.setText(aspirante.getCedula());
		txtTelefono.setText(aspirante.getTelefono());
		txtCelular.setText(aspirante.getCelular());
		txtNombres.setText(aspirante.getNombres());
		txtApellidos.setText(aspirante.getApellidos());
		dtpFechaNacimiento.setValue(aspirante.getFechaNacimiento());
		txtNoLibretaMilitar.setText(aspirante.getNoLibretaMilitar());
		txtEstatura.setText(String.valueOf(aspirante.getEstatura()));
		txtUltimoAnio.setText(String.valueOf(aspirante.getUltimoAnioEstudio()));
		txtNomnbreConyuge.setText(aspirante.getNombreConyuge());
		txtCantidadHijos.setText(String.valueOf(aspirante.getNoHijos()));
		txtDireccionDomiciliaria.setText(aspirante.getDireccionDomiciliaria());
		txtReferenciaDomiciliaria.setText(aspirante.getReferenciaDomiciliaria());
		txtNoSolar.setText(String.valueOf(aspirante.getNumeroSolar()));
		txtCorreo.setText(aspirante.getCorreo());
		
		cboGenero.setText(aspirante.getGenero().getGenero());
		generoSeleccionado = aspirante.getGenero();
		cboTipoSangre.setText(aspirante.getTipoSangre().getTipoSangre());
		tipoSangreSeleccionado = aspirante.getTipoSangre();
		cboEstadoCivil.setText(aspirante.getEstadoCivil().getEstadoCivil());
		estadoCivilSeleccionado = aspirante.getEstadoCivil();
		cboInstruccion.setText(aspirante.getInstruccion().getInstruccion());
		instruccionSeleccionado = aspirante.getInstruccion();
		cboProfesion.setText(aspirante.getProfesion().getProfesion());
		profesionSeleccionado = aspirante.getProfesion();
		
		if(aspirante.getEstadoCivil().getIdEstadoCivil() != Globals.CODIGO_ESTADO_CIVIL_CASADO) {
			txtNomnbreConyuge.setText("");
			txtNomnbreConyuge.setDisabled(true);
			txtCantidadHijos.setText("");
			txtCantidadHijos.setDisabled(true);
		}else {
			txtCantidadHijos.setDisabled(false);
			txtNomnbreConyuge.setDisabled(false);
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@NotifyChange({"listaProvincia","listaProvinciaResidencia"})
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
		listaProvinciaResidencia = listaProvincia; 
		cboProvinciaResidencia.setModel(new ListModelList(listaProvinciaResidencia));
		cboProvincia.setModel(new ListModelList(listaProvincia));
		
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
	@NotifyChange({"listaCantonResidencia"})
	@Command
	public void seleccionarProvinciaResidencia(){
		if(listaCantonResidencia != null)
			listaCantonResidencia = null;
		listaCantonResidencia = cantonDAO.buscarPorIdProvincia(provinciaResidenciaSeleccionada.getIdProvincia());
		cboCantonResidencia.setModel(new ListModelList(listaCantonResidencia));
	}
	@Command
	public void seleccionarEstadoCivil() {
		if(cboEstadoCivil.getSelectedItem() == null) {
			Clients.showNotification("Debe seleccionar estado civil");
			return;
		}
		EstadoCivil estado = (EstadoCivil)cboEstadoCivil.getSelectedItem().getValue();
		if(estado.getIdEstadoCivil() != Globals.CODIGO_ESTADO_CIVIL_CASADO) {
			txtNomnbreConyuge.setText("");
			txtNomnbreConyuge.setDisabled(true);
		}else {
			txtNomnbreConyuge.setDisabled(false);
		}
	}
	//documentos
	@Command
	public void nuevoDocumento() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Aspirante", null);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/registros/aspirantes/documentoRegistro.zul", null, params);
		ventanaCargar.doModal();
	}
	//ficha medica
	@Command
	public void nuevoFamiliar() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Aspirante", aspirante);
		params.put("Ventana", this);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/registros/aspirantes/familiares.zul", null, params);
		ventanaCargar.doModal();
	}
	@Command
	public void nuevaCirugia() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Aspirante", null);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/registros/aspirantes/cirugia.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar() {
		try {
			if(validarDatos() == false) {
				return;
			}
			Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getName().equals("onYes")) {		
						try {
							copiarDatosAspirante();
							if(matricula.getIdMatricula() == null) {
								matricula.setEstado("A");
								matricula.setFechaIngreso(new Date());
								matricula.setPeriodo(periodo);
								matricula.setAspirante(aspirante);
								List<Matricula> listaM = new ArrayList<>();
								listaM.add(matricula);
								aspirante.setMatriculas(listaM);
							}
							matriculaDAO.getEntityManager().getTransaction().begin();
							if(matricula.getIdMatricula() == null)
								matriculaDAO.getEntityManager().persist(aspirante);
							else {
								matriculaDAO.getEntityManager().merge(aspirante);
							}
							matriculaDAO.getEntityManager().getTransaction().commit();
							Clients.showNotification("Proceso Ejecutado con exito.");
							salir();						
						} catch (Exception e) {
							e.printStackTrace();
							matriculaDAO.getEntityManager().getTransaction().rollback();
						}
					}
				}
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			matriculaDAO.getEntityManager().getTransaction().rollback();
		}
	}
	private void copiarDatosAspirante() {
		aspirante.setApellidos(txtApellidos.getText().toString());
		Canton cNacimiento = (Canton) cboCanton.getSelectedItem().getValue();
		aspirante.setCantonNacimiento(cNacimiento);
		Canton cReseidencia = (Canton) cboCantonResidencia.getSelectedItem().getValue();
		aspirante.setCantonResidencia(cReseidencia);
		aspirante.setCedula(txtCedula.getText().toString());
		aspirante.setCelular(txtCelular.getText().toString());
		aspirante.setCorreo(txtCorreo.getText().toString());
		aspirante.setDireccionDomiciliaria(txtDireccionDomiciliaria.getText().toString());
		aspirante.setEstado("A");
		EstadoCivil eCivil = (EstadoCivil) cboEstadoCivil.getSelectedItem().getValue();
		aspirante.setEstadoCivil(eCivil);
		if(!txtEstatura.getText().isEmpty())
			aspirante.setEstatura(Double.valueOf(txtEstatura.getText()));
		aspirante.setFechaNacimiento(dtpFechaNacimiento.getValue());
		Genero genero = (Genero) cboGenero.getSelectedItem().getValue();
		aspirante.setGenero(genero);
		Instruccion instruccion = (Instruccion) cboInstruccion.getSelectedItem().getValue();
		aspirante.setInstruccion(instruccion);
		if(!txtCantidadHijos.getText().isEmpty())
			aspirante.setNoHijos(Integer.parseInt(txtCantidadHijos.getText()));
		aspirante.setNoLibretaMilitar(txtNoLibretaMilitar.getText());
		aspirante.setNombreConyuge(txtNomnbreConyuge.getText());
		aspirante.setNombres(txtNombres.getText());
		aspirante.setNumeroSolar(txtNoSolar.getText());
		Profesion profesion = (Profesion) cboProfesion.getSelectedItem().getValue();
		aspirante.setProfesion(profesion);
		aspirante.setReferenciaDomiciliaria(txtReferenciaDomiciliaria.getText());
		aspirante.setTelefono(txtTelefono.getText());
		TipoSangre tSangre = (TipoSangre) cboTipoSangre.getSelectedItem().getValue();
		aspirante.setTipoSangre(tSangre);
		if(!txtUltimoAnio.getText().isEmpty())
			aspirante.setUltimoAnioEstudio(Integer.parseInt(txtUltimoAnio.getText()));
	}
	private boolean validarDatos() {//devuelve falso cuando hay algun inconveniente
		boolean bandera = true;
		if(txtCedula.getText().isEmpty()) {
			Clients.showNotification("Debe registrar cédula de identidad","info",txtCedula,"end_center",2000);
			txtCedula.setFocus(true);
			return false;
		}
		if(!helper.validarDeCedula(txtCedula.getText())) {
			Clients.showNotification("Número de CÉDULA NO VÁLIDA!","info",txtCedula,"end_center",2000);
			txtCedula.focus();
			return false;
		}
		if(validarAspiranteExistente() == true) {
			Clients.showNotification("Ya hay un Aspirante con el número de documento " + txtCedula.getText() + ", registrado en éste periodo!","info",txtCedula,"end_center",2000);
			txtCedula.focus();
			return false;
		}
		
		if(txtNombres.getText().isEmpty()) {
			Clients.showNotification("Debe registrar Nombres","info",txtNombres,"end_center",2000);
			txtNombres.setFocus(true);
			return false;
		}
		if(txtApellidos.getText().isEmpty()) {
			Clients.showNotification("Debe registrar Apellidos","info",txtApellidos,"end_center",2000);
			txtApellidos.setFocus(true);
			return false;
		}
		if(cboNacionalidad.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar Nacionalidad","info",cboNacionalidad,"end_center",2000);
			cboNacionalidad.setFocus(true);
			return false;
		}
		if(cboProvincia.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar Provincia de nacimiento","info",cboProvincia,"end_center",2000);
			cboProvincia.setFocus(true);
			return false;
		}
		if(cboCanton.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar Cantón de nacimiento","info",cboCanton,"end_center",2000);
			cboCanton.setFocus(true);
			return false;
		}
		if(dtpFechaNacimiento.getValue() == null) {
			Clients.showNotification("Debe registrar Fecha de Nacimiento","info",dtpFechaNacimiento,"end_center",2000);
			dtpFechaNacimiento.setFocus(true);
			return false;
		}
		if(cboGenero.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar Género","info",cboGenero,"end_center",2000);
			cboGenero.setFocus(true);
			return false;
		}
		if(cboTipoSangre.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar Tipo de sangre","info",cboTipoSangre,"end_center",2000);
			cboTipoSangre.setFocus(true);
			return false;
		}
		if(cboEstadoCivil.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar Estado Civil","info",cboEstadoCivil,"end_center",2000);
			cboEstadoCivil.setFocus(true);
			return false;
		}
		if(cboInstruccion.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar Nivel de Instrucción","info",cboInstruccion,"end_center",2000);
			cboInstruccion.setFocus(true);
			return false;
		}
		if(cboProfesion.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar Profesión","info",cboProfesion,"end_center",2000);
			cboProfesion.setFocus(true);
			return false;
		}
		if(cboEstadoCivil.getSelectedItem() != null) {
			EstadoCivil estado = (EstadoCivil) cboEstadoCivil.getSelectedItem().getValue();
			if(estado.getIdEstadoCivil() == Globals.CODIGO_ESTADO_CIVIL_CASADO) {
				if(txtNomnbreConyuge.getText().isEmpty()) {
					Clients.showNotification("Debe registrar Nombre del Cónyuge","info",txtNomnbreConyuge,"end_center",2000);
					txtNomnbreConyuge.setFocus(true);
					return false;
				}
			}
		}
		if(cboProvinciaResidencia.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar Provincia de residencia","info",cboProvinciaResidencia,"end_center",2000);
			cboProvinciaResidencia.setFocus(true);
			return false;
		}
		if(cboCantonResidencia.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar Cantón de residencia","info",cboCantonResidencia,"end_center",2000);
			cboCantonResidencia.setFocus(true);
			return false;
		}
		if(txtDireccionDomiciliaria.getText().isEmpty()) {
			Clients.showNotification("Debe Registrar dirección domiciliaria","info",cboCantonResidencia,"end_center",2000);
			cboCantonResidencia.setFocus(true);
			return false;
		}
		if(!txtCorreo.getText().isEmpty()) {
			if(!ControllerHelper.validarEmail(txtCorreo.getText())) {
				Clients.showNotification("El correo ingresado no es valido","info",txtCorreo,"end_center",2000);
				txtCorreo.setFocus(true);
				return false;
			}
		}
		return bandera;
	}
	private boolean validarAspiranteExistente() {
		try {
			boolean bandera = false;
			List<Matricula> listaMatricula;
			if (matricula.getIdMatricula() == null) {
				listaMatricula = matriculaDAO.validarAspiranteExistente(txtCedula.getText().toString(), periodo.getIdPeriodo());
			}else {
				listaMatricula = matriculaDAO.validarAspiranteExistenteDiferente(txtCedula.getText().toString(),matricula.getIdMatricula(),periodo.getIdPeriodo());
			}
			if(listaMatricula.size() != 0)
				bandera = true;
			else
				bandera = false;
			return bandera;
		}catch(Exception ex) {
			return false;
		}
	}
	@Command
	public void salir() {
		BindUtils.postGlobalCommand(null, null, "Matricula.buscarAspirantePorPeriodo", null);
		winAspirantesEditar.detach();
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
	public Periodo getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
}