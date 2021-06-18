package com.bombero.control.seguridad;

import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.bombero.model.dao.ConfiguracionDAO;
import com.bombero.model.entity.Configuracion;
import com.bombero.util.Globals;

public class ConfiguracionC {
	@Wire Textbox txtNombreJefe;
	@Wire Textbox txtCargoJefe;
	@Wire Textbox txtNombreSegundoJefe;
	@Wire Textbox txtCargoSegundoJefe;
	@Wire Textbox txtNombreCapitan;
	@Wire Textbox txtCargoCapitan;
	@Wire Textbox txtCantPreguntas;
	
	Configuracion configuracion;
	ConfiguracionDAO configuracionDAO = new ConfiguracionDAO();
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);	 
		recuperarCOnfiguracion();
	}
	@GlobalCommand("Configuracion.findAll")
	@Command
	@NotifyChange({"configuracion"})
	private void recuperarCOnfiguracion() {
		List<Configuracion> lista = configuracionDAO.buscarConfiguracionActiva();
		if(lista.size() > 0) {
			configuracion = lista.get(0);
		}else {
			configuracion = new Configuracion();
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar() {
		try {
			Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getName().equals("onYes")) {		
						try {		
							configuracionDAO.getEntityManager().getTransaction().begin();
							if(configuracion.getCantidadPreguntas() != null) {
								float puntaje = Globals.PUNTAJE_EXAMEN / configuracion.getCantidadPreguntas();
								configuracion.setPuntaje(puntaje);
							}
							if (configuracion.getIdConfiguracion() == null) {
								configuracionDAO.getEntityManager().persist(configuracion);
							}else{
								configuracion = (Configuracion) configuracionDAO.getEntityManager().merge(configuracion);
							}			
							configuracionDAO.getEntityManager().getTransaction().commit();
							Clients.showNotification("Proceso Ejecutado con exito.");
							BindUtils.postGlobalCommand(null, null, "Empresa.findAll", null);
							Globals.CANTIDAD_PREGUNTAS = configuracion.getCantidadPreguntas();
							Globals.PUNTAJE_POR_PREGUNTA = configuracion.getPuntaje();
						} catch (Exception e) {
							e.printStackTrace();
							configuracionDAO.getEntityManager().getTransaction().rollback();
						}
					}
				}
			});
		}catch(Exception ex) {
			
		}
	}
	public Configuracion getConfiguracion() {
		return configuracion;
	}
	public void setConfiguracion(Configuracion configuracion) {
		this.configuracion = configuracion;
	}
}