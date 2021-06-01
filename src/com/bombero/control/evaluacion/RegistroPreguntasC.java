package com.bombero.control.evaluacion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import com.bombero.model.dao.ModuloDAO;
import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.dao.PreguntaDAO;
import com.bombero.model.entity.Modulo;
import com.bombero.model.entity.Periodo;
import com.bombero.model.entity.Pregunta;
import com.bombero.model.entity.Respuesta;
import com.bombero.util.Globals;

public class RegistroPreguntasC {
	Periodo periodoSeleccionado;
	Modulo moduloSeleccionado;
	PeriodoDAO periodoDAO = new PeriodoDAO();
	ModuloDAO moduloDAO = new ModuloDAO();
	PreguntaDAO preguntaDAO = new PreguntaDAO();
	List<PreguntaPresentar> listaPregunta;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
	}
	@GlobalCommand("Pregunta.buscarPorPeriodoYModulo")
	@NotifyChange({"listaPregunta"})
	@Command
	public void cargarPreguntas() {
		if(periodoSeleccionado == null) {
			Clients.showNotification("Periodo no seleccionado.!!");
			return;
		}
		if(moduloSeleccionado == null) {
			Clients.showNotification("Módulo no seleccionado.!!");
			return;
		}
		List<Pregunta> lista = preguntaDAO.buscarPreguntaPorPeriodoYModulo(periodoSeleccionado.getIdPeriodo(), moduloSeleccionado.getIdModulo());
		List<PreguntaPresentar> lstPres = new ArrayList<>();
		boolean validador = false;
		for(Pregunta prg : lista) {
			validador = false;
			PreguntaPresentar obj = new PreguntaPresentar();
			obj.setPregunta(prg);
			for(Respuesta r : prg.getRespuestas() ) {
				if(r.getCorrecta().equals(Globals.RESPUESTA_CORRECTA)) {
					validador = true;
					obj.setRespuesta(r.getRespuesta());
				}
			}
			if(validador == false) {
				obj.setRespuesta("No se ha registrado respuesta correcta!");
			}
			lstPres.add(obj);
		}
		listaPregunta = lstPres;
	}
	@Command
	public void nuevaPregunta() {
		if(periodoSeleccionado == null) {
			Clients.showNotification("Periodo no seleccionado.!!");
			return;
		}
		if(moduloSeleccionado == null) {
			Clients.showNotification("Módulo no seleccionado.!!");
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Periodo", periodoSeleccionado);
		params.put("Modulo", moduloSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/evaluacion/preguntas/preguntaEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	@Command
	public void editarPregunta() {
		
	}
	@Command
	public void eliminarPregunta() {
		
	}
	public List<Periodo> getPeriodoLista() {
		return periodoDAO.buscarActivos();
	}
	public List<Modulo> getModuloLista(){
		return moduloDAO.getModuloPorDescripcion("");
	}
	public Periodo getPeriodoSeleccionado() {
		return periodoSeleccionado;
	}
	public void setPeriodoSeleccionado(Periodo periodoSeleccionado) {
		this.periodoSeleccionado = periodoSeleccionado;
	}
	public Modulo getModuloSeleccionado() {
		return moduloSeleccionado;
	}
	public void setModuloSeleccionado(Modulo moduloSeleccionado) {
		this.moduloSeleccionado = moduloSeleccionado;
	}
	public List<PreguntaPresentar> getListaPregunta() {
		return listaPregunta;
	}
	public void setListaPregunta(List<PreguntaPresentar> listaPregunta) {
		this.listaPregunta = listaPregunta;
	}
	public class PreguntaPresentar {
		private Pregunta pregunta;
		private String respuesta;
		public Pregunta getPregunta() {
			return pregunta;
		}
		public void setPregunta(Pregunta pregunta) {
			this.pregunta = pregunta;
		}
		public String getRespuesta() {
			return respuesta;
		}
		public void setRespuesta(String respuesta) {
			this.respuesta = respuesta;
		};	
	}
}
