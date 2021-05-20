package com.bombero.control.registros;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.InstructorDAO;
import com.bombero.model.entity.Instructor;

public class InstructorListaC {
	public String textoBuscar;
	@Wire private Listbox lstInstructores;
	List<Instructor> instructorLista;
	InstructorDAO instructorDAO = new InstructorDAO();
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		buscar();
	}
	
	@GlobalCommand("Instructor.buscarPorPatron")
	@Command
	@NotifyChange({"instructorLista"})
	public void buscar(){
		if (instructorLista != null) {
			instructorLista = null; 
		}
		instructorLista = instructorDAO.buscarPorNombreApellido(textoBuscar);
	}
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/registros/instructores/instructorEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	@Command
	public void editar(@BindingParam("instructor") Instructor insSel){
		if(insSel == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		instructorDAO.getEntityManager().refresh(insSel);		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Instructor", insSel);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/registros/instructores/instructorEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("instructor") Instructor insSel){
		if (insSel == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						instructorDAO.getEntityManager().getTransaction().begin();
						insSel.setEstado("I");
						instructorDAO.getEntityManager().merge(insSel);
						instructorDAO.getEntityManager().getTransaction().commit();;
						BindUtils.postGlobalCommand(null, null, "Instructor.buscarPorPatron", null);
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						instructorDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}
	public String getTextoBuscar() {
		return textoBuscar;
	}
	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}
	public List<Instructor> getInstructorLista() {
		return instructorLista;
	}
	public void setInstructorLista(List<Instructor> instructorLista) {
		this.instructorLista = instructorLista;
	}
	
}
