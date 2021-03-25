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

import com.bombero.model.dao.ProfesionDAO;
import com.bombero.model.entity.Profesion;

public class ProfesionEditarC {
	@Wire private Window winProfesionEditar;
	@Wire private Textbox txtCodigo;
	@Wire private Textbox txtProfesion;

	private ProfesionDAO profesionDAO = new ProfesionDAO();
	private Profesion profesion;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		profesion = (Profesion) Executions.getCurrent().getArg().get("Profesion");
		if (profesion == null) {
			profesion = new Profesion();
			profesion.setEstado("A");
		}
	}

	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(txtProfesion.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar la Profesion","info",txtProfesion,"end_center",2000);
				txtProfesion.setFocus(true);
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
						
						profesionDAO.getEntityManager().getTransaction().begin();
						if (profesion.getIdProfesion() == null) {
							profesionDAO.getEntityManager().persist(profesion);
						}else{
							profesion = (Profesion) profesionDAO.getEntityManager().merge(profesion);
						}			
						profesionDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						profesionDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	

	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "Profesion.buscarPorPatron", null);
		winProfesionEditar.detach();
	}

	public Profesion getProfesion() {
		return profesion;
	}

	public void setProfesion(Profesion profesion) {
		this.profesion = profesion;
	}
}