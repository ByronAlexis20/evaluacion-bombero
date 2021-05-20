package com.bombero.control.registros;

import java.util.List;

import org.zkoss.bind.BindUtils;
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

import com.bombero.model.dao.InstructorDAO;
import com.bombero.model.dao.TipoSangreDAO;
import com.bombero.model.entity.Instructor;
import com.bombero.model.entity.TipoSangre;
import com.bombero.util.ControllerHelper;

public class InstructorEditarC {
	@Wire private Window winInstructorEditar;
	@Wire private Textbox txtCedula;
	@Wire private Textbox txtNombres;
	@Wire private Textbox txtApellidos;
	@Wire private Textbox txtGrado;
	@Wire private Textbox txtCargo;
	@Wire private Combobox cboTipoSangre;
	
	Instructor instructor;
	TipoSangreDAO tipoSangreDAO = new TipoSangreDAO();
	TipoSangre tipoSangreSeleccionado;
	ControllerHelper helper = new ControllerHelper();
	InstructorDAO instructorDAO = new InstructorDAO();
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		instructor = (Instructor) Executions.getCurrent().getArg().get("Instructor");
		if(instructor == null)
			instructor = new Instructor();
		else {
			cboTipoSangre.setText(instructor.getTipoSangre().getTipoSangre());
			tipoSangreSeleccionado = instructor.getTipoSangre();
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar(){
		if(validarDatos() == false) {
			return;
		}
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {		
					try {						
						instructor.setTipoSangre(tipoSangreSeleccionado);
						instructor.setEstado("A");
						instructorDAO.getEntityManager().getTransaction().begin();
						if (instructor.getIdInstructor() == null) {
							instructorDAO.getEntityManager().persist(instructor);
						}else{
							instructor = (Instructor) instructorDAO.getEntityManager().merge(instructor);
						}
						instructorDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						instructorDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}
	public boolean validarDatos() {
		if(txtCedula.getText().isEmpty()) {
			Clients.showNotification("Obligatoria regitrar cedula del instructor","info",txtCedula,"end_center",2000);
			txtCedula.setFocus(true);
			return false;
		}
		if(!helper.validarDeCedula(txtCedula.getText())) {
			Clients.showNotification("Número de CÉDULA NO VÁLIDA!","info",txtCedula,"end_center",2000);
			txtCedula.focus();
			return false;
		}
		if(instructor.getIdInstructor() == null) {
			if(this.validarPorCedula()) {
				Clients.showNotification("Número de cédula ya registrado!","info",txtCedula,"end_center",2000);
				txtCedula.focus();
				return false;
			}
		}else {
			if(this.validarPorCedulaInstructor()) {
				Clients.showNotification("Número de cédula ya registrado!","info",txtCedula,"end_center",2000);
				txtCedula.focus();
				return false;
			}
		}
		if(txtNombres.getText().isEmpty()) {
			Clients.showNotification("Obligatoria regitrar el nombre del instructor","info",txtNombres,"end_center",2000);
			txtNombres.setFocus(true);
			return false;
		}
		if(txtApellidos.getText().isEmpty()) {
			Clients.showNotification("Obligatoria regitrar el apellido del instructor","info",txtApellidos,"end_center",2000);
			txtApellidos.setFocus(true);
			return false;
		}
		if(cboTipoSangre.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar el Tipo de sangre","info",cboTipoSangre,"end_center",2000);
			return false;
		}
		return true;
	}
	private boolean validarPorCedulaInstructor() {
		try {
			List<Instructor> lista = instructorDAO.buscarPorCedulaInstructor(txtCedula.getText(),instructor.getIdInstructor());
			if(lista.size() > 0)
				return true;
			return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	private boolean validarPorCedula() {
		try {
			List<Instructor> lista = instructorDAO.buscarPorCedula(txtCedula.getText());
			if(lista.size() > 0)
				return true;
			return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "Instructor.buscarPorPatron", null);
		winInstructorEditar.detach();
	}
	public List<TipoSangre> getTipoSangres(){
		return tipoSangreDAO.getTipoSangrePorDescripcion("");
	}
	public Instructor getInstructor() {
		return instructor;
	}
	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}
	public TipoSangre getTipoSangreSeleccionado() {
		return tipoSangreSeleccionado;
	}
	public void setTipoSangreSeleccionado(TipoSangre tipoSangreSeleccionado) {
		this.tipoSangreSeleccionado = tipoSangreSeleccionado;
	}
}