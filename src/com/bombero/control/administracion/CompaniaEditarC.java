package com.bombero.control.administracion;

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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.CompaniaDAO;
import com.bombero.model.entity.Compania;

public class CompaniaEditarC {
	@Wire private Window winCompaniaEditar;
	@Wire private Textbox txtCodigo;
	@Wire private Textbox txtNombre;

	private CompaniaDAO companiaDAO = new CompaniaDAO();
	private Compania compania;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		compania = (Compania) Executions.getCurrent().getArg().get("Compania");
		if (compania == null) {
			compania = new Compania();
			compania.setEstado("A");
		}
	}

	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(txtNombre.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar el nombre de la Compañía","info",txtNombre,"end_center",2000);
				txtNombre.setFocus(true);
				return retorna;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar(){
		if(isValidarDatos() == true) {
			return;
		}
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {		
					try {
						
						companiaDAO.getEntityManager().getTransaction().begin();
						if (compania.getIdCompania() == null) {
							companiaDAO.getEntityManager().persist(compania);
						}else{
							compania = (Compania) companiaDAO.getEntityManager().merge(compania);
						}			
						companiaDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						companiaDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	

	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "Compania.buscarPorPatron", null);
		winCompaniaEditar.detach();
	}

	public Compania getCompania() {
		return compania;
	}

	public void setCompania(Compania compania) {
		this.compania = compania;
	}
}
