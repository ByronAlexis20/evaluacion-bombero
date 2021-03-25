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

import com.bombero.model.dao.TipoFamiliaDAO;
import com.bombero.model.entity.TipoFamiliar;

public class TipoFamiliaEditarC {
	@Wire private Window winTipoFamiliaEditar;
	@Wire private Textbox txtCodigo;
	@Wire private Textbox txtTipoFamilia;

	private TipoFamiliaDAO tipoFamiliaDAO = new TipoFamiliaDAO();
	private TipoFamiliar tipoFamilia;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		tipoFamilia = (TipoFamiliar) Executions.getCurrent().getArg().get("TipoFamilia");
		if (tipoFamilia == null) {
			tipoFamilia = new TipoFamiliar();
			tipoFamilia.setEstado("A");
		}
	}

	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(txtTipoFamilia.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar el tipo de familiar","info",txtTipoFamilia,"end_center",2000);
				txtTipoFamilia.setFocus(true);
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
						
						tipoFamiliaDAO.getEntityManager().getTransaction().begin();
						if (tipoFamilia.getIdTipoFamiliar() == null) {
							tipoFamiliaDAO.getEntityManager().persist(tipoFamilia);
						}else{
							tipoFamilia = (TipoFamiliar) tipoFamiliaDAO.getEntityManager().merge(tipoFamilia);
						}			
						tipoFamiliaDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						tipoFamiliaDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	

	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "TipoFamiliar.buscarPorPatron", null);
		winTipoFamiliaEditar.detach();
	}

	public TipoFamiliar getTipoFamilia() {
		return tipoFamilia;
	}

	public void setTipoFamilia(TipoFamiliar tipoFamilia) {
		this.tipoFamilia = tipoFamilia;
	}

}
