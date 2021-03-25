package com.bombero.control.administracion;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.ProfesionDAO;
import com.bombero.model.entity.Profesion;

public class ProfesionListaC {
	public String textoBuscar;
	ProfesionDAO profesionDAO = new ProfesionDAO();
	List<Profesion> profesionLista;
	@Wire private Listbox lstProfesion;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		buscar();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("Profesion.buscarPorPatron")
	@Command
	@NotifyChange({"profesionLista"})
	public void buscar(){
		if (profesionLista != null) {
			profesionLista = null; 
		}
		profesionLista = profesionDAO.getProfesionPorDescripcion(textoBuscar);
		lstProfesion.setModel(new ListModelList(profesionLista));
		if(profesionLista.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/administracion/profesionEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	
	@Command
	public void editar(@BindingParam("profesion") Profesion profesionSeleccionado){
		if(profesionSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		// Actualiza la instancia antes de enviarla a editar.
		profesionDAO.getEntityManager().refresh(profesionSeleccionado);		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Profesion", profesionSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/administracion/profesionEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("profesion") Profesion profesionSeleccionado){
		if (profesionSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						profesionDAO.getEntityManager().getTransaction().begin();
						profesionSeleccionado.setEstado("I");
						profesionDAO.getEntityManager().merge(profesionSeleccionado);
						profesionDAO.getEntityManager().getTransaction().commit();;
						buscar();
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						profesionDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}

	public List<Profesion> getProfesionLista() {
		return profesionLista;
	}

	public void setProfesionLista(List<Profesion> profesionLista) {
		this.profesionLista = profesionLista;
	}

	public String getTextoBuscar() {
		return textoBuscar;
	}
	
	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}
}
