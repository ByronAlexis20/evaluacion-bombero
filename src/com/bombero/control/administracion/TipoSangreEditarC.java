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

import com.bombero.model.dao.TipoSangreDAO;
import com.bombero.model.entity.TipoSangre;

public class TipoSangreEditarC {
	@Wire private Window winTipoSangreEditar;
	@Wire private Textbox txtCodigo;
	@Wire private Textbox txtTipoSangre;

	private TipoSangreDAO tipoSangreDAO = new TipoSangreDAO();
	private TipoSangre tipoSangre;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		tipoSangre = (TipoSangre) Executions.getCurrent().getArg().get("TipoSangre");
		if (tipoSangre == null) {
			tipoSangre = new TipoSangre();
			tipoSangre.setEstado("A");
		}
	}

	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(txtTipoSangre.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar el tipo de sangre","info",txtTipoSangre,"end_center",2000);
				txtTipoSangre.setFocus(true);
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
						
						tipoSangreDAO.getEntityManager().getTransaction().begin();
						if (tipoSangre.getIdTipoSangre() == null) {
							tipoSangreDAO.getEntityManager().persist(tipoSangre);
						}else{
							tipoSangre = (TipoSangre) tipoSangreDAO.getEntityManager().merge(tipoSangre);
						}			
						tipoSangreDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						tipoSangreDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	

	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "TipoSangre.buscarPorPatron", null);
		winTipoSangreEditar.detach();
	}

	public TipoSangre getTipoSangre() {
		return tipoSangre;
	}

	public void setTipoSangre(TipoSangre tipoSangre) {
		this.tipoSangre = tipoSangre;
	}
}
