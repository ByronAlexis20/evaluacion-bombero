package com.bombero.control.evaluacion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import com.bombero.model.dao.CalificacionDAO;
import com.bombero.model.dao.MatriculaDAO;
import com.bombero.model.dao.ModuloDAO;
import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.dao.UsuarioDAO;
import com.bombero.model.entity.Calificacion;
import com.bombero.model.entity.Matricula;
import com.bombero.model.entity.Modulo;
import com.bombero.model.entity.Periodo;
import com.bombero.model.entity.Usuario;
import com.bombero.util.Globals;
import com.bombero.util.SecurityUtil;

public class SeleccionarPeriodoC {
	@Wire Window winSeleccionarPeriodo;
	@Wire Window winRegistrarNotas;
	Periodo periodoSeleccionado;
	Modulo moduloSeleccionado;
	PeriodoDAO periodoDAO = new PeriodoDAO();
	ModuloDAO moduloDAO = new ModuloDAO();
	MatriculaDAO matriculaDAO = new MatriculaDAO();
	CalificacionDAO calificacionDAO = new CalificacionDAO();
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	
	List<Calificacion> listaCalificaciones;
	List<CalificacionAspirantes> listaCalificacionAspirantes;
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		winSeleccionarPeriodo.setMaximized(true);
		winRegistrarNotas.setMinimized(true);
	}
	@Command
	public void registrarNota() {
		if(periodoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar el PERIODO");
			return;
		}
		if(moduloSeleccionado == null) {
			Clients.showNotification("Debe seleccionar el MÓDULO");
			return;
		}
		winSeleccionarPeriodo.setVisible(false);
		winSeleccionarPeriodo.setMinimized(true);
		winRegistrarNotas.setVisible(true);
		winRegistrarNotas.setMaximized(true);
		BindUtils.postGlobalCommand(null, null, "Matricula.buscarAspirantePorPeriodo", null);
	}
	@GlobalCommand("Matricula.buscarAspirantePorPeriodo")
	@Command
	@NotifyChange({"listaCalificacionAspirantes"})
	public void cargarCalificaciones() {
		List<CalificacionAspirantes> lista = new ArrayList<>();
		List<Matricula> listaAspirantes = matriculaDAO.obtenerAspirantesPorIdPeriodo(periodoSeleccionado.getIdPeriodo());
		for(Matricula mat : listaAspirantes) {
			CalificacionAspirantes obj = new CalificacionAspirantes();
			Calificacion cal = calificacionDAO.obtenerCalificacionPorMatriculaYModulo(moduloSeleccionado.getIdModulo(), mat.getIdMatricula());
			obj.setMatricula(mat);
			if(cal != null) {
				obj.setCalificacion(cal);
			}else {
				//se graba la calificacion
				Calificacion calificacion = new Calificacion();
				calificacion.setEstado("A");
				calificacion.setIdCalificacion(null);
				calificacion.setModulo(moduloSeleccionado);
				calificacion.setMatricula(mat);
				calificacion.setNota1(0);
				calificacion.setExamen(0);
				calificacion.setNota2(0);
				calificacion.setNota3(0);
				calificacion.setNota4(0);
				calificacion.setNotaFinal(0);
				calificacionDAO.getEntityManager().getTransaction().begin();
				calificacionDAO.getEntityManager().persist(calificacion);
				calificacionDAO.getEntityManager().getTransaction().commit();
				obj.setCalificacion(calificacion);
			}
			lista.add(obj);
		}
		listaCalificacionAspirantes = lista;
	}
	@Command
	public void calificar(@BindingParam("calificacion") CalificacionAspirantes calSelecionado){
		if(calSelecionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Calificacion", calSelecionado.getCalificacion());
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/evaluacion/calificaciones/calificar.zul", null, params);
		ventanaCargar.doModal();
	}
	@Command
	public void regresar() {
		winRegistrarNotas.setMinimized(true);
		winSeleccionarPeriodo.setVisible(true);
		winSeleccionarPeriodo.setMaximized(true);
		winRegistrarNotas.setVisible(false);
	}
	public List<Periodo> getListaPeriodo(){
		return periodoDAO.getPeriodoPorDescripcion("");
	}
	public List<Modulo> getListaModulo() {
		//cuando no es administrador, solo se cargan los que tiene registrado
		Usuario usuario = usuarioDAO.getUsuario(SecurityUtil.getUser().getUsername().trim()); 
		if(usuario.getPerfil().getIdPerfil() == Globals.CODIGO_USUARIO_ADMINISTRADOR || usuario.getPerfil().getIdPerfil() == Globals.CODIGO_USUARIO_SISTEMAS)
			return moduloDAO.getModuloPorDescripcion("");
		else {
			if(usuario.getPerfil().getIdPerfil() == Globals.CODIGO_USUARIO_INSTRUCTOR)
				return moduloDAO.buscarPorInstructor(usuario.getInstructor().getIdInstructor());
			else
				return null;
		}
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
	public List<Calificacion> getListaCalificaciones() {
		return listaCalificaciones;
	}
	public void setListaCalificaciones(List<Calificacion> listaCalificaciones) {
		this.listaCalificaciones = listaCalificaciones;
	}
	public List<CalificacionAspirantes> getListaCalificacionAspirantes() {
		return listaCalificacionAspirantes;
	}
	public void setListaCalificacionAspirantes(List<CalificacionAspirantes> listaCalificacionAspirantes) {
		this.listaCalificacionAspirantes = listaCalificacionAspirantes;
	}
	public class CalificacionAspirantes {
		private Matricula matricula;
		private Calificacion calificacion;
		private float total;
		public Matricula getMatricula() {
			return matricula;
		}
		public void setMatricula(Matricula matricula) {
			this.matricula = matricula;
		}
		public Calificacion getCalificacion() {
			return calificacion;
		}
		public void setCalificacion(Calificacion calificacion) {
			this.calificacion = calificacion;
		}
		public float getTotal() {
			return total;
		}
		public void setTotal(float total) {
			this.total = total;
		}
	}
}