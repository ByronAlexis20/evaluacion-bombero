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

import com.bombero.model.dao.EquipoVestimentaDAO;
import com.bombero.model.entity.EquipoVestimenta;

public class EquipoListaC {
	public String textoBuscar;
	EquipoVestimentaDAO equipoDAO = new EquipoVestimentaDAO();
	List<EquipoVestimenta> equipoLista;
	@Wire private Listbox lstEquipos;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		buscar();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("EquipoVestimenta.buscarPorPatron")
	@Command
	@NotifyChange({"equipoLista"})
	public void buscar(){
		if (equipoLista != null) {
			equipoLista = null; 
		}
		equipoLista = equipoDAO.getEquipoPorDescripcion(textoBuscar);
		lstEquipos.setModel(new ListModelList(equipoLista));
		if(equipoLista.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/administracion/equipoEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	
	@Command
	public void editar(@BindingParam("equipo") EquipoVestimenta equipoSeleccionado){
		if(equipoSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		// Actualiza la instancia antes de enviarla a editar.
		equipoDAO.getEntityManager().refresh(equipoSeleccionado);		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Equipo", equipoSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/administracion/equipoEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("equipo") EquipoVestimenta equipoSeleccionado){
		if (equipoSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						equipoDAO.getEntityManager().getTransaction().begin();
						equipoSeleccionado.setEstado("I");
						equipoDAO.getEntityManager().merge(equipoSeleccionado);
						equipoDAO.getEntityManager().getTransaction().commit();;
						buscar();
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						equipoDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}


	public List<EquipoVestimenta> getEquipoLista() {
		return equipoLista;
	}

	public void setEquipoLista(List<EquipoVestimenta> equipoLista) {
		this.equipoLista = equipoLista;
	}

	public String getTextoBuscar() {
		return textoBuscar;
	}
	
	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}	
}
