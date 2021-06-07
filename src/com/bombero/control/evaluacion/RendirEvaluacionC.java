package com.bombero.control.evaluacion;

import java.io.IOException;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;

import com.bombero.model.dao.AspiranteDAO;
import com.bombero.model.dao.EvaluacionDAO;
import com.bombero.model.dao.MatriculaDAO;
import com.bombero.model.dao.ModuloDAO;
import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.dao.ResultadoEvaluacionDAO;
import com.bombero.model.dao.UsuarioDAO;
import com.bombero.model.entity.Aspirante;
import com.bombero.model.entity.Evaluacion;
import com.bombero.model.entity.Matricula;
import com.bombero.model.entity.ResultadoEvaluacion;
import com.bombero.model.entity.Usuario;
import com.bombero.util.Globals;
import com.bombero.util.SecurityUtil;

public class RendirEvaluacionC {
	@Wire Include iPreguntas;
	@Wire Label lblPeriodo;
	@Wire Label lblCedula;
	@Wire Label lblEstudiante;
	@Wire Label lblObservacion;
	@Wire Label lblEtiquetaPregunta;
	@Wire Label lblCantidadPreguntas;
	@Wire Button btnIniciar;
	PeriodoDAO periodoDAO = new PeriodoDAO();
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	AspiranteDAO aspiranteDAO = new AspiranteDAO();
	EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
	MatriculaDAO matriculaDAO = new MatriculaDAO();
	ResultadoEvaluacionDAO resultadoDAO = new ResultadoEvaluacionDAO();
	ModuloDAO moduloDAO = new ModuloDAO();
	Aspirante aspirante;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		validarPeriodo();
		
	}
	
	private void validarPeriodo() {
		try {
			List<Evaluacion> evaluacions = evaluacionDAO.buscarPorEstadoEvaluacion(Globals.EVALUACION_INICIADA);
			if(evaluacions.size() > 0) {//la evaluacion ha sido iniciada
				lblPeriodo.setValue("Evaluación final del Módulo " + evaluacions.get(0).getModulo().getModulo() + " del Periodo" + evaluacions.get(0).getPeriodo().getDescripcion());
				lblPeriodo.setStyle("font-size:20px;font-weight:bold;color:#000000");
				//tambien preguntar por el usuario logeado a ver si es un estudiante
				Usuario usuarios = usuarioDAO.getUsuario(SecurityUtil.getUser().getUsername());
				if(usuarios != null) {
					// ahora preguntar si es estudiante
					if(usuarios.getAspirante() != null) {//si es diferente de null es un estudiante
						//buscar si se encuentra matriculado en el periodo
						List<Matricula> listaMatricula = matriculaDAO.obtenerPorAspiranteYPeriodo(usuarios.getAspirante().getIdAspirante(), evaluacions.get(0).getPeriodo().getIdPeriodo());
						if(listaMatricula.size() > 0) {//si se encuentra matriculado
							lblCedula.setVisible(true);
							lblEstudiante.setVisible(true);
							lblObservacion.setVisible(true);
							
							lblCedula.setValue("Cédula: " + listaMatricula.get(0).getAspirante().getCedula());
							lblEstudiante.setValue("Estudiante: " + listaMatricula.get(0).getAspirante().getNombres() + " " + listaMatricula.get(0).getAspirante().getApellidos());
							//validar si esa persona ya realizo la prueba
							List<ResultadoEvaluacion> resultado = resultadoDAO.buscarPorEvaluacionYAspirante(evaluacions.get(0).getIdEvaluacion(), listaMatricula.get(0).getIdMatricula());
							if(resultado.size() > 0) {
								lblObservacion.setValue("Ud ya terminó de realizar la evaluación");
							}else {
								lblObservacion.setValue("Recuerde solo tiene una oportunidad para rendir la evaluación");
								btnIniciar.setVisible(true);
							}
						}else {
							lblObservacion.setValue("Estudiante no se encuentra matriculado en el periodo");
							lblObservacion.setStyle("font-size:15px;font-weight:bold;color:#000000");
						}
					}else {
						lblObservacion.setVisible(true);
						lblObservacion.setValue("Usuario logeado no es un aspirante a bombero");
						lblObservacion.setStyle("font-size:15px;font-weight:bold;color:#000000");
						lblCedula.setValue("");
						lblEstudiante.setValue("");
						
						lblCedula.setVisible(false);
						lblEstudiante.setVisible(false);
					}
				}
			}else {
				lblPeriodo.setValue("No existe periodo de evaluación");
				lblPeriodo.setStyle("font-size:20px;font-weight:bold;color:#FF0000");
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@Command
	public void iniciarEvaluacion() {
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
			public void onEvent(ClickEvent event) throws Exception {
				if(Messagebox.Button.YES.equals(event.getButton())) {
					lblCantidadPreguntas.setVisible(true);
					lblEtiquetaPregunta.setVisible(true);
					btnIniciar.setDisabled(true);
					lblCantidadPreguntas.setValue(String.valueOf(Globals.CANTIDAD_PREGUNTAS));
					iPreguntas.setSrc("/recursos/forms/evaluacion/rendir-evaluacion/responderPreguntas.zul");
				}
			}
		};
		Messagebox.show("¿Desea realizar la evaluación? \nContinuar (Ok)", "Confirmación", new Messagebox.Button[]{
				Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, clickListener);
	}
	public Aspirante getAspirante() {
		return aspirante;
	}
	public void setAspirante(Aspirante aspirante) {
		this.aspirante = aspirante;
	}
}
