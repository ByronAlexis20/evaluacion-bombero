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

import com.bombero.model.dao.TipoSangreDAO;
import com.bombero.model.entity.TipoSangre;

public class TipoSangreListaC {
	public String textoBuscar;
	TipoSangreDAO tipoSangreDAO = new TipoSangreDAO();
	List<TipoSangre> tipoSangreLista;
	@Wire private Listbox lstTipoSangre;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		buscar();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("TipoSangre.buscarPorPatron")
	@Command
	@NotifyChange({"tipoSangreLista"})
	public void buscar(){
		if (tipoSangreLista != null) {
			tipoSangreLista = null; 
		}
		tipoSangreLista = tipoSangreDAO.getTipoSangrePorDescripcion(textoBuscar);
		lstTipoSangre.setModel(new ListModelList(tipoSangreLista));
		if(tipoSangreLista.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/administracion/tipoSangreEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	
	@Command
	public void editar(@BindingParam("tipoSangre") TipoSangre tipoSangreSeleccionado){
		if(tipoSangreSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		// Actualiza la instancia antes de enviarla a editar.
		tipoSangreDAO.getEntityManager().refresh(tipoSangreSeleccionado);		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("TipoSangre", tipoSangreSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/administracion/tipoSangreEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("tipoSangre") TipoSangre tipoSangreSeleccionado){
		if (tipoSangreSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						tipoSangreDAO.getEntityManager().getTransaction().begin();
						tipoSangreSeleccionado.setEstado("I");
						tipoSangreDAO.getEntityManager().merge(tipoSangreSeleccionado);
						tipoSangreDAO.getEntityManager().getTransaction().commit();;
						buscar();
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						tipoSangreDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}

	public List<TipoSangre> getTipoSangreLista() {
		return tipoSangreLista;
	}

	public void setTipoSangreLista(List<TipoSangre> tipoSangreLista) {
		this.tipoSangreLista = tipoSangreLista;
	}

	public String getTextoBuscar() {
		return textoBuscar;
	}
	
	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}
}
