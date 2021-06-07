package com.bombero.control.evaluacion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Vlayout;

import com.bombero.model.dao.AspiranteDAO;
import com.bombero.model.dao.CalificacionDAO;
import com.bombero.model.dao.EvaluacionDAO;
import com.bombero.model.dao.MatriculaDAO;
import com.bombero.model.dao.ModuloDAO;
import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.dao.PreguntaDAO;
import com.bombero.model.dao.UsuarioDAO;
import com.bombero.model.entity.Calificacion;
import com.bombero.model.entity.Evaluacion;
import com.bombero.model.entity.Matricula;
import com.bombero.model.entity.Pregunta;
import com.bombero.model.entity.Respuesta;
import com.bombero.model.entity.RespuestaSeleccionada;
import com.bombero.model.entity.ResultadoEvaluacion;
import com.bombero.model.entity.Usuario;
import com.bombero.util.Globals;
import com.bombero.util.SecurityUtil;

public class ResponderPreguntasC {
	@Wire Vlayout vlPreguntas;
	@Wire Label lblRealizado;
	@Wire Vlayout vlRealizado; 
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	PeriodoDAO periodoDAO = new PeriodoDAO();
	AspiranteDAO aspiranteDAO = new AspiranteDAO();
	ModuloDAO moduloDAO = new ModuloDAO();
	EvaluacionDAO evaluacionDAO = new EvaluacionDAO();
	PreguntaDAO preguntaDAO = new PreguntaDAO();
	MatriculaDAO matriculaDAO = new MatriculaDAO();
	CalificacionDAO calificacionDAO = new CalificacionDAO();

	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		//agregar el componente pregunta
		cargarPreguntas();
	}
	private void cargarPreguntas() {
		List<Evaluacion> evaluacions = evaluacionDAO.buscarPorEstadoEvaluacion(Globals.EVALUACION_INICIADA);
		if(evaluacions.size() > 0) {
			Usuario usuarios = usuarioDAO.getUsuario(SecurityUtil.getUser().getUsername());
			if(usuarios != null) {
				if(usuarios.getAspirante() != null) {//si es diferente de null es un estudiante
					//buscar si se encuentra matriculado en el periodo
					List<Matricula> listaMatricula = matriculaDAO.obtenerPorAspiranteYPeriodo(usuarios.getAspirante().getIdAspirante(), evaluacions.get(0).getPeriodo().getIdPeriodo());
					if(listaMatricula.size() > 0) {//la persona si pertenece al periodo en estado de evaluacion
						List<Pregunta> preguntasPresentar = new ArrayList<>();
						Evaluacion evaluacion = evaluacions.get(0);
						List<Pregunta> preguntasModulo = preguntaDAO.buscarPorEvaluacion(evaluacion.getIdEvaluacion());
						if(preguntasModulo.size() < Globals.CANTIDAD_PREGUNTAS) {
							Clients.showNotification("NO hay preguntas suficientes");
						}else {
							List<Integer> itemsPreguntas = new ArrayList<>();//declaro un arreglo del item de la pregunta a grabar
							boolean bandera = false;
							Integer cantidadDisponible = preguntasModulo.size();//esta es la cantidad de preguntas disponibles
							Integer i = 0;//para recorrer y q se incremente cada vez que elije una pregunta, sirve para romper el ciclo
							while(true) {//hago un ciclo infinitp, se rompe cuando ya se seleccionan las cantidades de preguntas permitidas
								bandera = false;
								Random r = new Random();
								int numero = r.nextInt(cantidadDisponible);
								for(Integer num : itemsPreguntas) {
									if(num == numero)
										bandera = true;
								}
								if(bandera == false) {//si la bandera sigue siendo falso.. es xq no se encuentra en el listado
									itemsPreguntas.add(numero);
									i = i + 1;//incremento el contador
								}
								if(i == Globals.CANTIDAD_PREGUNTAS) {
									break;//si ya se ha alcanzdo el numero de preguntas.. se rompe el ciclo
								}
							}
							//cuando se rompe el ciclo infinito, ya tenemos los items de las preguntas seleccionadas al random
							//recorremoe la lista de items
							for(Integer item : itemsPreguntas) {
								preguntasPresentar.add(preguntasModulo.get(item));//agrego a la lista el item perteneciente a la pregunta
							}

						}
						//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
						//cuando ya se han seleccionado todas las preguntas, lo siguiente es presentar las preguntas por pantalla
						Integer i = 1;
						for(Pregunta pre : preguntasPresentar) {
							establecerPregunta(String.valueOf(pre.getIdPregunta()),i,pre.getPregunta(),pre.getRespuestas());
							i += 1;
						}
					}
				}
			}
		}
	}
	private void establecerPregunta(String idPregunta,Integer item, String pregunta, List<Respuesta> respuestas) {
		Groupbox grupoPregunta = new Groupbox();
		Radiogroup grupoRespuestas = new Radiogroup();
		grupoRespuestas.setId("grupo" + item);
		grupoRespuestas.setOrient("vertical");
		grupoRespuestas.setVisible(false);

		Vbox vRespuestas = new Vbox();
		vRespuestas.getChildren().add(grupoRespuestas);
		//numero de pregunta
		Label lblNoPregunta = new Label();
		lblNoPregunta.setValue("PREGUNTA NO " + item);
		vRespuestas.getChildren().add(lblNoPregunta);
		//pregunta
		Label lblPregunta = new Label();
		lblPregunta.setValue(pregunta);
		lblPregunta.setId(idPregunta);
		vRespuestas.getChildren().add(lblPregunta);
		//respuestas
		for(Respuesta res : respuestas) {
			if(res.getEstado().equals("A")) {
				Radio rRes = new Radio();
				rRes.setRadiogroup("grupo" + item);
				rRes.setId(String.valueOf(res.getIdRespuesta()));
				rRes.setLabel(res.getRespuesta());
				vRespuestas.getChildren().add(rRes);
			}
		}
		grupoPregunta.getChildren().add(vRespuestas);
		vlPreguntas.getChildren().add(grupoPregunta);
	}

	@Command
	public void enviarEvaluacion() {
		try {
			EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
				public void onEvent(ClickEvent event) throws Exception {
					if(Messagebox.Button.YES.equals(event.getButton())) {
						List<PreguntaRespuesta> listaRespuestas = new ArrayList<>();
						List<Integer> listaEvaluacion = new ArrayList<>();//para grabar el listado de las evaluaciones
						boolean bandera = false;//para validar si la evaluacion ya ha sido ingresado a la lista
						for(int i = 0 ; i < vlPreguntas.getChildren().size() ; i ++) {
							Groupbox grupo = (Groupbox) vlPreguntas.getChildren().get(i);
							PreguntaRespuesta respuestaSeleccionada = new PreguntaRespuesta();
							for(int j = 0 ; j < grupo.getChildren().size() ; j ++) {
								Vbox vbox = (Vbox) grupo.getChildren().get(j);
								for(int h = 0 ; h < vbox.getChildren().size() ; h ++) {
									//la primera posicion es un Radiogroup
									if(h == 0) {
									}else {
										//la segunda posicion es Label del No pregunta
										if(h == 1) {
										}else {
											//la tercera es Label de la pregunta
											if(h == 2) {
												Label pregunta = (Label) vbox.getChildren().get(h);
												respuestaSeleccionada.setIdPregunta(Integer.parseInt(pregunta.getId()));
												Pregunta lstPregunta = preguntaDAO.buscarPreguntaPorId(Integer.parseInt(pregunta.getId()));
												if(lstPregunta != null) {
													bandera = false;
													respuestaSeleccionada.setIdEvaluacion(lstPregunta.getEvaluacion().getIdEvaluacion());
													for(Integer ev : listaEvaluacion) {
														if(ev == lstPregunta.getEvaluacion().getIdEvaluacion())
															bandera = true;
													}
													if(bandera == false)//si la bandera sigue siendo falso es xq no la encontro en el listado, entonces se agrega
														listaEvaluacion.add(lstPregunta.getEvaluacion().getIdEvaluacion());
												}
											}else {
												//las demas posiciones son Radio de respuestas
												Radio respuesta = (Radio) vbox.getChildren().get(h);
												System.out.print("Id Respuesta: " + respuesta.getId() + " Respuesta: " + respuesta.getLabel());
												if(respuesta.isSelected()) {
													System.out.print(" <-- Seleccionada");
													respuestaSeleccionada.setIdSelecionada(Integer.parseInt(respuesta.getId()));
												}
												System.out.println("");
											}
										}
									}
								}
								listaRespuestas.add(respuestaSeleccionada);
							}
						}
						Integer total = 0;
						for(Integer ev : listaEvaluacion) {
							Evaluacion evaluacion = evaluacionDAO.buscarEvaluacionPorId(ev);
							if(evaluacion != null) {
								ResultadoEvaluacion resultados = new ResultadoEvaluacion();
								resultados.setEstado("A");
								//proceso para calcular las respuestas
								Integer suma = 0;
								for(PreguntaRespuesta res : listaRespuestas) {
									if(res.getIdEvaluacion() == ev) {
										Integer califica = calificacion(res.getIdPregunta(),res.getIdSelecionada());
										suma = suma + califica;
										total = total + califica;
									}
								}
								resultados.setCalificacion(suma);
								resultados.setEvaluacion(evaluacion);
								resultados.setIdResultadoEvaluacion(null);
								//la persona es la que se ecuentra loggeada
								Usuario usuarios = usuarioDAO.getUsuario(SecurityUtil.getUser().getUsername());
								Matricula matric = null;
								if(usuarios != null) {
									List<Matricula> mat = matriculaDAO.obtenerPorAspiranteYPeriodo(usuarios.getAspirante().getIdAspirante(), evaluacion.getPeriodo().getIdPeriodo());
									if(mat.size() > 0){
										resultados.setMatricula(mat.get(0));
										matric = mat.get(0);
									}
								}
								//luego se pone los reultados dependiendo de las preguntas
								List<RespuestaSeleccionada> seleccionados = new ArrayList<>();
								for(PreguntaRespuesta res : listaRespuestas) {
									if(res.getIdEvaluacion() == ev) {
										RespuestaSeleccionada sel = new RespuestaSeleccionada();//graba las respuestas seleccionadas
										sel.setEstado("A");
										sel.setIdPregunta(res.getIdPregunta());
										sel.setIdRespuesta(res.getIdSelecionada());
										sel.setIdRespuestaSeleccionada(null);
										sel.setResultadoEvaluacion(resultados);
										seleccionados.add(sel);
									}
								}
								resultados.setRespuestaSeleccionadas(seleccionados);
								//tambien se actualiza la calificacion en la seccion de examen
								Calificacion calificacion = calificacionDAO.obtenerCalificacionPorMatriculaYModulo(evaluacion.getIdEvaluacion(), matric.getIdMatricula());
								evaluacionDAO.getEntityManager().getTransaction().begin();
								evaluacionDAO.getEntityManager().persist(resultados);
								if(calificacion != null) {
									calificacion.setExamen(total);
									evaluacionDAO.getEntityManager().merge(calificacion);
								}
								evaluacionDAO.getEntityManager().getTransaction().commit();
							}
						}
					}
				}
			};
			Messagebox.show("¿Desea Grabar los Datos?", "Confirmación", new Messagebox.Button[]{
					Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, clickListener);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private Integer calificacion(Integer idPregunta, Integer idRespuesta) {
		try {
			Integer cali = 0;
			Pregunta preg = preguntaDAO.buscarPreguntaPorId(idPregunta);
			if(preg != null) {
				for(Respuesta res : preg.getRespuestas()) {
					if(res.getEstado().equals("A")) {
						if(res.getCorrecta().equals(Globals.RESPUESTA_CORRECTA)) {
							if(idRespuesta.equals(res.getIdRespuesta()))
								cali = 1;
						}
					}
				}
			}
			return cali;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return 0;
		}
	}
	public class PreguntaRespuesta {
		private Integer idEvaluacion;
		private Integer idPregunta;
		private Integer idSelecionada;
		public Integer getIdPregunta() {
			return idPregunta;
		}
		public void setIdPregunta(Integer idPregunta) {
			this.idPregunta = idPregunta;
		}
		public Integer getIdSelecionada() {
			return idSelecionada;
		}
		public void setIdSelecionada(Integer idSelecionada) {
			this.idSelecionada = idSelecionada;
		}
		public Integer getIdEvaluacion() {
			return idEvaluacion;
		}
		public void setIdEvaluacion(Integer idEvaluacion) {
			this.idEvaluacion = idEvaluacion;
		}
	}
}
