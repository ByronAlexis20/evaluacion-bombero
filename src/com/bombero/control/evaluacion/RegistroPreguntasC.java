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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.ModuloAsignadoDAO;
import com.bombero.model.dao.ModuloDAO;
import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.dao.PreguntaDAO;
import com.bombero.model.dao.UsuarioDAO;
import com.bombero.model.entity.Modulo;
import com.bombero.model.entity.ModuloAsignado;
import com.bombero.model.entity.Periodo;
import com.bombero.model.entity.Pregunta;
import com.bombero.model.entity.Respuesta;
import com.bombero.model.entity.Usuario;
import com.bombero.util.Globals;
import com.bombero.util.SecurityUtil;

public class RegistroPreguntasC {
	Periodo periodoSeleccionado;
	Modulo moduloSeleccionado;
	PeriodoDAO periodoDAO = new PeriodoDAO();
	ModuloDAO moduloDAO = new ModuloDAO();
	PreguntaDAO preguntaDAO = new PreguntaDAO();
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	ModuloAsignadoDAO moduloAsignadoDAO = new ModuloAsignadoDAO();
	List<PreguntaPresentar> listaPregunta;
	PreguntaPresentar preguntaSeleccionada;
	List<Modulo> moduloLista;
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
		int cont = 1;
		for(Pregunta prg : lista) {
			validador = false;
			PreguntaPresentar obj = new PreguntaPresentar();
			obj.setPregunta(prg);
			for(Respuesta r : prg.getRespuestas() ) {
				if(r.getEstado().equals("A")) {
					if(r.getCorrecta().equals(Globals.RESPUESTA_CORRECTA)) {
						validador = true;
						obj.setRespuesta(r.getRespuesta());
					}
				}
			}
			if(validador == false) {
				obj.setRespuesta("No se ha registrado respuesta correcta!");
			}
			obj.setItem(cont);
			lstPres.add(obj);
			cont ++;
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
		if(preguntaSeleccionada == null) {
			Clients.showNotification("Pregunta no seleccionado.!!");
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Periodo", periodoSeleccionado);
		params.put("Modulo", moduloSeleccionado);
		params.put("Pregunta", preguntaSeleccionada.getPregunta());
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/evaluacion/preguntas/preguntaEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminarPregunta() {
		if(preguntaSeleccionada == null) {
			Clients.showNotification("Debe seleccionar Una pregunta");
			return;
		}
		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {	
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						Pregunta seleccionado = preguntaSeleccionada.getPregunta();
						seleccionado.setEstado("I");
						preguntaDAO.getEntityManager().getTransaction().begin();
						preguntaDAO.getEntityManager().merge(seleccionado);
						preguntaDAO.getEntityManager().getTransaction().commit();
						BindUtils.postGlobalCommand(null, null, "Pregunta.buscarPorPeriodoYModulo", null);
						Clients.showNotification("Pregunta eliminada");
					} catch (Exception e) {
						preguntaDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});	
	}
	@GlobalCommand({"Modulo.buscarPorPatron", "ModuloAsignado.buscarAsignacionesPorInstructorPeriodo"})
	@Command
	@NotifyChange({"moduloLista"})
	public void seleccionarPeriodo() {
		Usuario usuario = usuarioDAO.getUsuario(SecurityUtil.getUser().getUsername());
		if(usuario.getPerfil().getIdPerfil() == Globals.CODIGO_USUARIO_ADMINISTRADOR || usuario.getPerfil().getIdPerfil() == Globals.CODIGO_USUARIO_SISTEMAS) {
			moduloLista = moduloDAO.getModuloPorDescripcion("");
		}else {
			List<Modulo> listaMod = new ArrayList<>();
			List<ModuloAsignado> listModAsig = moduloAsignadoDAO.buscarAsignacionesPorInstructorPeriodo(usuario.getIdUsuario(), periodoSeleccionado.getIdPeriodo());
			for(ModuloAsignado md : listModAsig) {
				listaMod.add(md.getModulo());
			}
			moduloLista = listaMod;
		}
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
	public PreguntaPresentar getPreguntaSeleccionada() {
		return preguntaSeleccionada;
	}
	public void setPreguntaSeleccionada(PreguntaPresentar preguntaSeleccionada) {
		this.preguntaSeleccionada = preguntaSeleccionada;
	}
	public List<Modulo> getModuloLista() {
		return moduloLista;
	}
	public void setModuloLista(List<Modulo> moduloLista) {
		this.moduloLista = moduloLista;
	}
	public class PreguntaPresentar {
		private int item;
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
		}
		public int getItem() {
			return item;
		}
		public void setItem(int item) {
			this.item = item;
		};	
	}
}
