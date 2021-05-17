package com.bombero.control.registros;

import java.io.IOException;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.EstadoCivilDAO;
import com.bombero.model.dao.TipoFamiliaDAO;
import com.bombero.model.entity.EstadoCivil;
import com.bombero.model.entity.Familiar;
import com.bombero.model.entity.FichaMedica;
import com.bombero.model.entity.TipoFamiliar;

public class FamiliaresC {
	@Wire private Window winFamiliares;
	@Wire private Textbox txtCedula;
	@Wire private Combobox cboFamiliar;
	@Wire private Combobox cboEstadoCivil;
	@Wire private Textbox txtNombres;
	@Wire private Textbox txtApellidos;
	@Wire private Textbox txtTelefono;
	@Wire private Textbox txtEducacion;
	@Wire private Textbox txtProfesion;
	@Wire private Textbox txtTrabajo;
	FichaMedicaC fichaMedicaC;
	FichaMedica fichaMedica;
	TipoFamiliaDAO tipoFamiliarDAO = new TipoFamiliaDAO();
	EstadoCivilDAO estadoCivilDAO = new EstadoCivilDAO();
	TipoFamiliar tipoFamiliarSerleccionado;
	EstadoCivil estadoCivilSeleccionado;
	Familiar familiar;
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		fichaMedicaC = (FichaMedicaC) Executions.getCurrent().getArg().get("Ventana");
		fichaMedica = (FichaMedica) Executions.getCurrent().getArg().get("FichaMedica");
		familiar = new Familiar();
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
						copiarDatos();
						fichaMedicaC.agregarListaFamiliar(familiar);
						salir();
					}
				}
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private boolean validarDatos() {
		if(txtCedula.getText().isEmpty()) {
			Clients.showNotification("Debe registrar el número de cédula","info",txtCedula,"end_center",2000);
			txtCedula.focus();
			return false;
		}
		if(cboFamiliar.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar el Parentesco","info",cboFamiliar,"end_center",2000);
			return false;
		}
		if(txtNombres.getText().isEmpty()) {
			Clients.showNotification("Debe registrar el nombre","info",txtNombres,"end_center",2000);
			txtNombres.focus();
			return false;
		}
		if(txtApellidos.getText().isEmpty()) {
			Clients.showNotification("Debe registrar apellidos","info",txtApellidos,"end_center",2000);
			txtApellidos.focus();
			return false;
		}
		if(cboEstadoCivil.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar el Estado Civil","info",cboEstadoCivil,"end_center",2000);
			return false;
		}
		return true;
	}
	private void copiarDatos() {
		familiar.setApellido(txtApellidos.getText());
		familiar.setCedula(txtCedula.getText());
		familiar.setEducacion(txtEducacion.getText());
		familiar.setEstado("A");
		familiar.setEstadoCivil(estadoCivilSeleccionado);
		familiar.setFichaMedica(fichaMedica);
		familiar.setIdFamiliar(null);
		familiar.setNombre(txtNombres.getText());
		familiar.setProfesion(txtProfesion.getText());
		familiar.setTelefono(txtTelefono.getText());
		familiar.setTipoFamiliar(tipoFamiliarSerleccionado);
		familiar.setTrabajo(txtTrabajo.getText());
	}
	@Command
	public void salir() {
		winFamiliares.detach();
	}
	public List<TipoFamiliar> getTipoFamiliar(){
		return tipoFamiliarDAO.getTipoFamiliarPorEstado("A");
	}
	public List<EstadoCivil> getEstadoCivil(){
		return estadoCivilDAO.getEstadoCivils();
	}
	public TipoFamiliar getTipoFamiliarSerleccionado() {
		return tipoFamiliarSerleccionado;
	}
	public void setTipoFamiliarSerleccionado(TipoFamiliar tipoFamiliarSerleccionado) {
		this.tipoFamiliarSerleccionado = tipoFamiliarSerleccionado;
	}
	public Familiar getFamiliar() {
		return familiar;
	}
	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}
	public EstadoCivil getEstadoCivilSeleccionado() {
		return estadoCivilSeleccionado;
	}
	public void setEstadoCivilSeleccionado(EstadoCivil estadoCivilSeleccionado) {
		this.estadoCivilSeleccionado = estadoCivilSeleccionado;
	}
}
