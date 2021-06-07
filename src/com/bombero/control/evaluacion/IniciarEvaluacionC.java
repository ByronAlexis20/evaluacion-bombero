package com.bombero.control.evaluacion;

import java.io.IOException;
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
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

import com.bombero.model.dao.EvaluacionDAO;
import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.dao.PreguntaDAO;
import com.bombero.model.entity.Evaluacion;
import com.bombero.model.entity.Periodo;
import com.bombero.model.entity.Pregunta;
import com.bombero.util.Globals;

public class IniciarEvaluacionC {
	Periodo periodoSeleccionado;
	PeriodoDAO periodoDAO = new PeriodoDAO();
	PreguntaDAO preguntaDAO = new PreguntaDAO();
	EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
	Evaluacion evaluacionSeleccionado;
	List<Evaluacion> listaEvaluacion;
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
	}
	@GlobalCommand({"Evaluacion.buscarPorPeriodo"})
	@Command
	@NotifyChange({"listaEvaluacion"})
	public void seleccionarPeriodo() {
		listaEvaluacion = evaluacionDAO.recuperarPorPeriodo(periodoSeleccionado.getIdPeriodo());
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void iniciarEvaluacion() {
		if(evaluacionSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un registro");
			return;
		}
		if(verificarMinimoPreguntas() == true) {
			Clients.showNotification("No se puede iniciar, por motivos de cantidad de prguntas estan debajo del mínimo");
			return;
		}
		if(evaluacionSeleccionado.getEstadoEvaluacion().equals(Globals.EVALUACION_INICIADA)) {
			Clients.showNotification("La evaluación ya ha sido INICIADA!");
			return;
		}
		if(evaluacionSeleccionado.getEstadoEvaluacion().equals(Globals.EVALUACION_TERMINADA)) {
			Clients.showNotification("La evaluación ya ha sido TERMINADA!");
			return;
		}
		//validar que solo una evaluacion este iniciada
		boolean band = false;
		for(Evaluacion ev : listaEvaluacion) {
			if(ev.getEstadoEvaluacion().equals(Globals.EVALUACION_INICIADA))
				band = true;
		}
		if(band == true) {
			Clients.showNotification("Ya existe una evaluación INICIADA");
			return;
		}
		Messagebox.show("¿Desea iniciar la evaluación?", "Iniciar Evaluación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {	
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						evaluacionDAO.getEntityManager().getTransaction().begin();
						evaluacionSeleccionado.setEstadoEvaluacion(Globals.EVALUACION_INICIADA);
						evaluacionDAO.getEntityManager().merge(evaluacionSeleccionado);
						evaluacionDAO.getEntityManager().getTransaction().commit();
						BindUtils.postGlobalCommand(null, null, "Evaluacion.buscarPorPeriodo", null);
						Clients.showNotification("Evaluación iniciada!");
					} catch (Exception e) {
						evaluacionDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});	
	}
	private boolean verificarMinimoPreguntas() {
		List<Pregunta> listaPreguntas = preguntaDAO.buscarPorEvaluacion(evaluacionSeleccionado.getIdEvaluacion());
		if(listaPreguntas.size() < Globals.CANTIDAD_PREGUNTAS)
			return true;
		else
			return false;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void terminarEvaluacion() {
		if(evaluacionSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un registro");
			return;
		}
		if(!evaluacionSeleccionado.getEstadoEvaluacion().equals(Globals.EVALUACION_INICIADA)) {
			Clients.showNotification("La evaluación debe estar INICIADA");
			return;
		}
		Messagebox.show("¿Desea iniciar la evaluación?", "Iniciar Evaluación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {	
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						evaluacionDAO.getEntityManager().getTransaction().begin();
						evaluacionSeleccionado.setEstadoEvaluacion(Globals.EVALUACION_TERMINADA);
						evaluacionDAO.getEntityManager().merge(evaluacionSeleccionado);
						evaluacionDAO.getEntityManager().getTransaction().commit();
						BindUtils.postGlobalCommand(null, null, "Evaluacion.buscarPorPeriodo", null);
						Clients.showNotification("Evaluación iniciada!");
					} catch (Exception e) {
						evaluacionDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}
	public List<Periodo> getPeriodoLista() {
		return periodoDAO.buscarActivos();
	}
	public Periodo getPeriodoSeleccionado() {
		return periodoSeleccionado;
	}
	public void setPeriodoSeleccionado(Periodo periodoSeleccionado) {
		this.periodoSeleccionado = periodoSeleccionado;
	}
	public Evaluacion getEvaluacionSeleccionado() {
		return evaluacionSeleccionado;
	}
	public void setEvaluacionSeleccionado(Evaluacion evaluacionSeleccionado) {
		this.evaluacionSeleccionado = evaluacionSeleccionado;
	}
	public List<Evaluacion> getListaEvaluacion() {
		return listaEvaluacion;
	}
	public void setListaEvaluacion(List<Evaluacion> listaEvaluacion) {
		this.listaEvaluacion = listaEvaluacion;
	}
}