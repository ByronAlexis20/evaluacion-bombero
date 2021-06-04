package com.bombero.control.evaluacion;

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

import com.bombero.model.dao.CalificacionDAO;
import com.bombero.model.entity.Calificacion;

public class CalificacarC {
	@Wire private Window winCalificar;
	@Wire private Textbox txtNota1;
	@Wire private Textbox txtNota2;
	@Wire private Textbox txtNota3;
	@Wire private Textbox txtNota4;
	
	Calificacion calificacion;
	CalificacionDAO calificacionDAO = new CalificacionDAO();
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		calificacion = (Calificacion) Executions.getCurrent().getArg().get("Calificacion");
		recuperarCalificaciones();
	}
	private void recuperarCalificaciones() {
		txtNota1.setText(String.valueOf(calificacion.getNota1()));
		txtNota2.setText(String.valueOf(calificacion.getNota2()));
		txtNota3.setText(String.valueOf(calificacion.getNota3()));
		txtNota4.setText(String.valueOf(calificacion.getNota4()));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar() {
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {		
					try {
						calificacion.setNota1(Float.parseFloat(txtNota1.getText()));
						calificacion.setNota2(Float.parseFloat(txtNota2.getText()));
						calificacion.setNota3(Float.parseFloat(txtNota3.getText()));
						calificacion.setNota4(Float.parseFloat(txtNota4.getText()));
						calificacionDAO.getEntityManager().getTransaction().begin();
						calificacionDAO.getEntityManager().merge(calificacion);
						calificacionDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						calificacionDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}
	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "Matricula.buscarAspirantePorPeriodo", null);
		winCalificar.detach();
	}
	public Calificacion getCalificacion() {
		return calificacion;
	}
	public void setCalificacion(Calificacion calificacion) {
		this.calificacion = calificacion;
	}
}