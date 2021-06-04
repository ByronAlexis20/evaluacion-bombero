package com.bombero.control.evaluacion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.bombero.model.dao.CalificacionDAO;
import com.bombero.model.dao.MatriculaDAO;
import com.bombero.model.dao.ModuloAsignadoDAO;
import com.bombero.model.dao.ModuloDAO;
import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.dao.UsuarioDAO;
import com.bombero.model.entity.Aspirante;
import com.bombero.model.entity.Calificacion;
import com.bombero.model.entity.Matricula;
import com.bombero.model.entity.ModuloAsignado;
import com.bombero.model.entity.Periodo;
import com.bombero.model.entity.Usuario;
import com.bombero.util.Globals;
import com.bombero.util.SecurityUtil;

public class VerCalificacionC {
	@Wire Vlayout hlayoutCal;
	Periodo periodo;
	Aspirante aspirante;
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	PeriodoDAO periodoDAO = new PeriodoDAO();
	MatriculaDAO matriculaDAO = new MatriculaDAO();
	ModuloDAO moduloDAO = new ModuloDAO();
	ModuloAsignadoDAO moduloAsignadoDAO = new ModuloAsignadoDAO();
	CalificacionDAO calificacionDAO = new CalificacionDAO();
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		cargarPeriodoActivo();
		cargarNotas();
	}
	private void cargarPeriodoActivo() {
		List<Periodo> listaPeriodo = periodoDAO.buscarEnProceso();
		if(listaPeriodo.size() > 0)
			periodo = listaPeriodo.get(0);
	}
	public void cargarNotas() {
		try {
			if(periodo != null) {
				Usuario usuario = usuarioDAO.getUsuario(SecurityUtil.getUser().getUsername());
				if(usuario == null)
					return;
				aspirante = usuario.getAspirante();
				if(usuario.getPerfil().getIdPerfil() == Globals.CODIGO_USUARIO_ASPIRANTE) {
					List<Grid> listaGrid = new ArrayList<>();
					List<Matricula> matricula = matriculaDAO.obtenerPorAspiranteYPeriodo(usuario.getAspirante().getIdAspirante(), periodo.getIdPeriodo());
					if(matricula.size() > 0) {
						//si esta matriculado
						List<ModuloAsignado> listaModulosAsignados = moduloAsignadoDAO.obtenerAsignacionesPorPeriodo(periodo.getIdPeriodo());
						for(ModuloAsignado mod : listaModulosAsignados) {
							Grid grid = new Grid();
							grid.setHflex("1");
							//cabecera
							Auxhead auxHead = new Auxhead();
							Auxheader auxHeader = new Auxheader();
							Label label = new Label(mod.getModulo().getModulo());
							auxHeader.getChildren().add(label);
							auxHead.getChildren().add(auxHeader);
							//columnas
							Columns colums = new Columns();
							Column columNotaInstructor = new Column();
							columNotaInstructor.setLabel("Instructor");
							colums.getChildren().add(columNotaInstructor);
							Column columNota1 = new Column();
							columNota1.setLabel("Nota 1");
							colums.getChildren().add(columNota1);
							Column columNota2 = new Column();
							columNota2.setLabel("Nota 2");
							colums.getChildren().add(columNota2);
							Column columNota3 = new Column();
							columNota3.setLabel("Nota 3");
							colums.getChildren().add(columNota3);
							Column columNota4 = new Column();
							columNota4.setLabel("Nota 4");
							colums.getChildren().add(columNota4);
							Column columExamen = new Column();
							columExamen.setLabel("Examen");
							colums.getChildren().add(columExamen);
							Column columTotal = new Column();
							columTotal.setLabel("N. Final");
							colums.getChildren().add(columTotal);
							Column columEstado = new Column();
							columEstado.setLabel("Estado");
							colums.getChildren().add(columEstado);
							//consultar las calificaciones
							Rows rows = new Rows();
							Row row = new Row();
							Cell cellInstructor = new Cell();
							Label labelInstructor = new Label(mod.getInstructor().getNombre() + " " + mod.getInstructor().getApellido());
							cellInstructor.getChildren().add(labelInstructor);
							row.getChildren().add(cellInstructor);
							Cell cellNota1 = new Cell();
							Cell cellNota2 = new Cell();
							Cell cellNota3 = new Cell();
							Cell cellNota4 = new Cell();
							Cell cellExamen = new Cell();
							Cell cellTotal = new Cell();
							Cell cellEstado = new Cell();
							List<Calificacion> listaCalificacion = calificacionDAO.buscarPorMatriculaYModulo(matricula.get(0).getIdMatricula(), mod.getModulo().getIdModulo());
							if(listaCalificacion.size() > 0) {
								Label labelNota1 = new Label(String.valueOf(listaCalificacion.get(0).getNota1()));
								cellNota1.getChildren().add(labelNota1);
								row.getChildren().add(cellNota1);
								
								Label labelNota2 = new Label(String.valueOf(listaCalificacion.get(0).getNota2()));
								cellNota2.getChildren().add(labelNota2);
								row.getChildren().add(cellNota2);
								
								Label labelNota3 = new Label(String.valueOf(listaCalificacion.get(0).getNota3()));
								cellNota3.getChildren().add(labelNota3);
								row.getChildren().add(cellNota3);
								
								Label labelNota4 = new Label(String.valueOf(listaCalificacion.get(0).getNota4()));
								cellNota4.getChildren().add(labelNota4);
								row.getChildren().add(cellNota4);
								
								Label labelExamen = new Label(String.valueOf(listaCalificacion.get(0).getNota4()));
								cellExamen.getChildren().add(labelExamen);
								row.getChildren().add(cellExamen);
								
								Label labelTotal = new Label(String.valueOf(listaCalificacion.get(0).getNotaFinal()));
								cellTotal.getChildren().add(labelTotal);
								row.getChildren().add(cellTotal);
								//
								String estado = "REPROBADO";
								if(listaCalificacion.get(0).getNotaFinal() >= Globals.NOTA_MINIMA_APROBACION)
									estado = "APROBADO";
								Label labelEstado = new Label(estado);
								cellEstado.getChildren().add(labelEstado);
								row.getChildren().add(cellEstado);
							} else {
								Label labelNota1 = new Label("0");
								cellNota1.getChildren().add(labelNota1);
								row.getChildren().add(cellNota1);
								
								Label labelNota2 = new Label("0");
								cellNota2.getChildren().add(labelNota2);
								row.getChildren().add(cellNota2);
								
								Label labelNota3 = new Label("0");
								cellNota3.getChildren().add(labelNota3);
								row.getChildren().add(cellNota3);
								
								Label labelNota4 = new Label("0");
								cellNota4.getChildren().add(labelNota4);
								row.getChildren().add(cellNota4);
								
								Label labelExamen = new Label("0");
								cellExamen.getChildren().add(labelExamen);
								row.getChildren().add(cellExamen);
								
								Label labelTotal = new Label("0");
								cellTotal.getChildren().add(labelTotal);
								row.getChildren().add(cellTotal);
								
								Label labelEstado = new Label("REPROBADO");
								cellEstado.getChildren().add(labelEstado);
								row.getChildren().add(cellEstado);
							}
							rows.getChildren().add(row);
							//agregar los componentes principales
							grid.getChildren().add(auxHead);
							grid.getChildren().add(colums);
							grid.getChildren().add(rows);
							listaGrid.add(grid);
						}
					}
					System.out.println(listaGrid.size());
					hlayoutCal.getChildren().addAll(listaGrid);
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public Periodo getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	public Aspirante getAspirante() {
		return aspirante;
	}
	public void setAspirante(Aspirante aspirante) {
		this.aspirante = aspirante;
	}
}
