package com.bombero.control.evaluacion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.EvaluacionDAO;
import com.bombero.model.dao.ModuloDAO;
import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.dao.PreguntaDAO;
import com.bombero.model.dao.RespuestaDAO;
import com.bombero.model.entity.Evaluacion;
import com.bombero.model.entity.Modulo;
import com.bombero.model.entity.Periodo;
import com.bombero.model.entity.Pregunta;
import com.bombero.model.entity.Respuesta;

public class PreguntaEditarC {
	@Wire Listbox lstRespuestas;
	@Wire Textbox txtPregunta;
	@Wire Window winPregunta;
	@Wire Button btnEditar;
	@Wire Button btnGrabarPregunta;
	@Wire Button btnAgregar;
	
	Respuesta respuestaSeleccionada;
	List<Respuesta> listaRespuestas;
	Periodo periodo;
	Modulo modulo;
	Pregunta pregunta;
	Evaluacion evaluacion;
	
	PreguntaDAO preguntaDAO = new PreguntaDAO();
	PeriodoDAO periodoDAO = new PeriodoDAO();
	ModuloDAO moduloDAO = new ModuloDAO();
	RespuestaDAO respuestaDAO = new RespuestaDAO();
	EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		if (Executions.getCurrent().getArg().get("Periodo") != null) {
			periodo = (Periodo) Executions.getCurrent().getArg().get("Periodo");
		}
		if (Executions.getCurrent().getArg().get("Modulo") != null) {
			modulo = (Modulo) Executions.getCurrent().getArg().get("Modulo");
		}
		if (Executions.getCurrent().getArg().get("Pregunta") != null) {
			pregunta = (Pregunta) Executions.getCurrent().getArg().get("Pregunta");
		}
		if(pregunta != null) {
			txtPregunta.setText(pregunta.getPregunta());
			btnEditar.setDisabled(false);
			txtPregunta.setDisabled(true);//se bloquea el cuadro de texto de la pregunta para cuando presione el boton editar se desbloquee
			recuperarRespuestas();
		}else {
			pregunta = new Pregunta();
			pregunta.setIdPregunta(null);
			btnEditar.setDisabled(true);
			txtPregunta.setDisabled(false);
			btnGrabarPregunta.setDisabled(false);
			btnAgregar.setDisabled(true);
		}
	}
	@Command
	public void editar() {
		btnEditar.setDisabled(true);
		txtPregunta.setDisabled(false);
		btnGrabarPregunta.setDisabled(false);
	}
	@GlobalCommand("Respuesta.buscarPorPregunta")
	@NotifyChange({"listaRespuestas"})
	@Command
	public void recuperarRespuestas() {
		try {
			listaRespuestas = respuestaDAO.buscarRespuestaPorPregunta(pregunta.getIdPregunta());
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@Command
	public void grabarPregunta() {
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
			public void onEvent(ClickEvent event) throws Exception {
				if(Messagebox.Button.YES.equals(event.getButton())) {
					if(pregunta.getIdPregunta() != null) {//si es diferente de null, es una pregunta recuperada, solo se modifica
						pregunta.setPregunta(txtPregunta.getText());
						preguntaDAO.getEntityManager().getTransaction().begin();
						preguntaDAO.getEntityManager().merge(pregunta);
						preguntaDAO.getEntityManager().getTransaction().commit();
					}else {//caso contrario es una pregunta nueva
						//cuando es una pregunta nueva hay que preguntar primero si tiene una evaluacion
						List<Evaluacion> listaEv = evaluacionDAO.buscarPorModuloYPeriodo(periodo.getIdPeriodo(), modulo.getIdModulo());
						if(listaEv.size() > 0) {
							evaluacion = listaEv.get(0);
							//si tiene evaluacion hay que preguntar si tiene preguntas 
							pregunta.setEstado("A");
							pregunta.setIdPregunta(null);
							pregunta.setPregunta(txtPregunta.getText());
							pregunta.setEvaluacion(evaluacion);
							if(evaluacion.getPreguntas().size() > 0) {//si es mayor a cero tiene preguntas entre sus registros
								evaluacion.addPregunta(pregunta);
							}else {//no tiene preguntas y se crea una lista
								List<Pregunta> listaPreg = new ArrayList<>();
								listaPreg.add(pregunta);
								evaluacion.setPreguntas(listaPreg);
							}
							evaluacionDAO.getEntityManager().getTransaction().begin();
							evaluacionDAO.getEntityManager().merge(evaluacion);
							evaluacionDAO.getEntityManager().getTransaction().commit();
							evaluacion = evaluacionDAO.buscarEvaluacionPorId(evaluacion.getIdEvaluacion());
							pregunta = preguntaDAO.buscarPreguntaPorId(pregunta.getIdPregunta());
						}else {//no tiene evaluacion
							evaluacion = new Evaluacion();
							evaluacion.setEstado("A");
							evaluacion.setDescripcion("Evaluacion del periodo " + periodo.getDescripcion() + " Modulo " + modulo.getModulo());
							evaluacion.setModulo(modulo);
							evaluacion.setPeriodo(periodo);
							evaluacion.setIdEvaluacion(null);
							
							pregunta.setEstado("A");
							pregunta.setIdPregunta(null);
							pregunta.setPregunta(txtPregunta.getText());
							pregunta.setEvaluacion(evaluacion);
							
							List<Pregunta> listaPreg = new ArrayList<>();
							listaPreg.add(pregunta);
							evaluacion.setPreguntas(listaPreg);
							
							evaluacionDAO.getEntityManager().getTransaction().begin();
							evaluacionDAO.getEntityManager().persist(evaluacion);
							evaluacionDAO.getEntityManager().getTransaction().commit();
							List<Evaluacion> listaEvaluacion = evaluacionDAO.recuperarUltimaEvaluacion();
							if(listaEvaluacion.size() > 0) {
								evaluacion = listaEvaluacion.get(0);
							}
							List<Pregunta> listaPregunta = preguntaDAO.getPreguntaUltima();
							if(listaPregunta.size() > 0) {
								pregunta = listaPregunta.get(0);
							}
						}
					}
					Clients.showNotification("Pregunta registrada correctamente!");
					btnEditar.setDisabled(false);
					btnGrabarPregunta.setDisabled(true);
					txtPregunta.setDisabled(true);		
					btnAgregar.setDisabled(false);
				}
			}
		};
		Messagebox.show("¿Desea Grabar los Datos?", "Confirmación", new Messagebox.Button[]{
				Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, clickListener);	
	}
	@Command
	public void agregarRespuesta(){
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("Pregunta", pregunta);
			Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/evaluacion/preguntas/respuesta.zul", null, params);
			ventanaCargar.doModal();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@Command
	public void quitarRespuesta() {
		try {
			if(respuestaSeleccionada == null) {
				Messagebox.show("Debe seleccionar una respuesta a eliminar");
				return;
			}
			RespuestaDAO respuestaDAO = new RespuestaDAO();
			for(Respuesta item : listaRespuestas) {
				System.out.println(item.toString());
			}
			System.out.println("Seleccionada"); System.out.println("selecionada a: " + respuestaSeleccionada.toString());
			EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
				public void onEvent(ClickEvent event) throws Exception {
					if(Messagebox.Button.YES.equals(event.getButton())) {
						if(respuestaSeleccionada.getIdRespuesta() != null) {
							respuestaSeleccionada.setEstado("I");
							respuestaDAO.getEntityManager().getTransaction().begin();
							respuestaDAO.getEntityManager().merge(respuestaSeleccionada);
							respuestaDAO.getEntityManager().getTransaction().commit();
						}
						respuestaSeleccionada = null;
						BindUtils.postGlobalCommand(null, null, "Respuesta.buscarPorPregunta", null);
						Clients.showNotification("Transaccion ejecutada con exito.");
					}
				}
			};
			Messagebox.show("¿Seguro desea quitar la respuesta?", "Confirmación", new Messagebox.Button[]{
					Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, clickListener);
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@Command
	public void salir() {
		winPregunta.detach();
	}
	public Respuesta getRespuestaSeleccionada() {
		return respuestaSeleccionada;
	}
	public void setRespuestaSeleccionada(Respuesta respuestaSeleccionada) {
		this.respuestaSeleccionada = respuestaSeleccionada;
	}
	public List<Respuesta> getListaRespuestas() {
		return listaRespuestas;
	}
	public void setListaRespuestas(List<Respuesta> listaRespuestas) {
		this.listaRespuestas = listaRespuestas;
	}
	public Periodo getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	public Modulo getModulo() {
		return modulo;
	}
	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}
	public Pregunta getPregunta() {
		return pregunta;
	}
	public void setPregunta(Pregunta pregunta) {
		this.pregunta = pregunta;
	}
	public Evaluacion getEvaluacion() {
		return evaluacion;
	}
	public void setEvaluacion(Evaluacion evaluacion) {
		this.evaluacion = evaluacion;
	}
}