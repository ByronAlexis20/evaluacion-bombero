package com.bombero.control.registros;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.MatriculaDAO;
import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.entity.Matricula;
import com.bombero.model.entity.Periodo;
import com.bombero.util.Globals;

public class AspiranteListaC {
	PeriodoDAO periodoDAO = new PeriodoDAO();
	List<Periodo> periodoLista;
	Periodo periodoSeleccionado;
	@Wire private Listbox lstPeriodos;
	MatriculaDAO matriculaDAO = new MatriculaDAO();
	List<MatriculaMostrar> matriculaLista;
	@Wire private Listbox lstAspirantes;
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	//botones
	@Wire Button btnNuevoAspirante;
	@Wire Button btnEditarAspirante;
	@Wire Button btnEliminarAspirante;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		buscarPeriodo();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("Periodo.buscarPorPatron")
	@Command
	@NotifyChange({"periodoLista"})
	public void buscarPeriodo(){
		if (periodoLista != null) {
			periodoLista = null; 
		}
		periodoLista = periodoDAO.buscarActivos();
		lstPeriodos.setModel(new ListModelList(periodoLista));
		if(periodoLista.size() == 0) 
			Clients.showNotification("No hay Periodos para mostrar.!!");
	}
	
	@Command
	public void seleccionarPeriodo() {
		if(matriculaLista != null)
			matriculaLista = null;
		cargarAspirantes();
		if(periodoSeleccionado.getEstadoPeriodo().equals(Globals.ESTADO_PERIODO_FINALIZADO)) {
			btnNuevoAspirante.setDisabled(true);
			btnEditarAspirante.setDisabled(true);
			btnEliminarAspirante.setDisabled(true);
		} else {
			btnNuevoAspirante.setDisabled(false);
			btnEditarAspirante.setDisabled(false);
			btnEliminarAspirante.setDisabled(false);
		}
	}
	
	@GlobalCommand("Matricula.buscarAspirantePorPeriodo")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@NotifyChange({"matriculaLista"})
	public void cargarAspirantes() {
		List<Matricula> lista = matriculaDAO.obtenerAspirantesPorIdPeriodo(periodoSeleccionado.getIdPeriodo());
		matriculaLista = new ArrayList<>();
		for(Matricula mat : lista) {
			MatriculaMostrar m = new MatriculaMostrar();
			m.setCedula(mat.getAspirante().getCedula());
			m.setFechaNacimiento(formateador.format(mat.getAspirante().getFechaNacimiento()));
			m.setGenero(mat.getAspirante().getGenero().getGenero());
			m.setMatricula(mat);
			m.setNombres(mat.getAspirante().getNombres() + " " + mat.getAspirante().getApellidos());
			m.setProfesion(mat.getAspirante().getProfesion().getProfesion());
			m.setTipoSangre(mat.getAspirante().getTipoSangre().getTipoSangre());
			matriculaLista.add(m);
		}
		lstAspirantes.setModel(new ListModelList(matriculaLista));
	}
	
	@Command
	public void nuevoAspirante() {
		try {
			if(periodoSeleccionado == null) {
				Clients.showNotification("Debe Seleccionar un periodo");
				return;
			}
			if(periodoSeleccionado.getEstadoPeriodo().equals(Globals.ESTADO_PERIODO_FINALIZADO)) {
				Clients.showNotification("Debe Seleccionar un periodo");
				return;
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("Periodo", periodoSeleccionado);
			Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/registros/aspirantes/aspiranteEditar.zul", null, params);
			ventanaCargar.doModal();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@Command
	public void editarAspirante(){
		if(lstAspirantes.getSelectedItem() == null) {
			Clients.showNotification("Debe seleccionar un registro");
			return;
		}
		MatriculaMostrar matriculaSel = (MatriculaMostrar)lstAspirantes.getSelectedItem().getValue();
		matriculaDAO.getEntityManager().refresh(matriculaSel.getMatricula());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Matricula", matriculaSel.getMatricula());
		params.put("Periodo", periodoSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/registros/aspirantes/aspiranteEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminarAspirante(){
		if(lstAspirantes.getSelectedItem() == null) {
			Clients.showNotification("Debe seleccionar un registro");
			return;
		}
		MatriculaMostrar matriculaSel = (MatriculaMostrar)lstAspirantes.getSelectedItem().getValue();
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						Matricula mat = matriculaSel.getMatricula();
						matriculaDAO.getEntityManager().getTransaction().begin();
						mat.setEstado("I");
						matriculaDAO.getEntityManager().merge(mat);
						matriculaDAO.getEntityManager().getTransaction().commit();;
						cargarAspirantes();
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						matriculaDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}
//	@Command
//	public void documentos() {
//		if(lstAspirantes.getSelectedItem() == null) {
//			Clients.showNotification("Debe seleccionar un registro");
//			return;
//		}
//		MatriculaMostrar matriculaSel = (MatriculaMostrar)lstAspirantes.getSelectedItem().getValue();
//		matriculaDAO.getEntityManager().refresh(matriculaSel.getMatricula());
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("Matricula", matriculaSel.getMatricula());
//		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/registros/aspirantes/documentoLista.zul", null, params);
//		ventanaCargar.doModal();
//	}
//	@Command
//	public void fichaMedica() {
//		if(lstAspirantes.getSelectedItem() == null) {
//			Clients.showNotification("Debe seleccionar un registro");
//			return;
//		}
//		MatriculaMostrar matriculaSel = (MatriculaMostrar)lstAspirantes.getSelectedItem().getValue();
//		matriculaDAO.getEntityManager().refresh(matriculaSel.getMatricula());
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("Aspirante", matriculaSel.getMatricula().getAspirante());
//		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/registros/aspirantes/fichaMedica.zul", null, params);
//		ventanaCargar.doModal();
//	}
	public List<Periodo> getPeriodoLista() {
		return periodoLista;
	}
	public void setPeriodoLista(List<Periodo> periodoLista) {
		this.periodoLista = periodoLista;
	}
	public Periodo getPeriodoSeleccionado() {
		return periodoSeleccionado;
	}
	public void setPeriodoSeleccionado(Periodo periodoSeleccionado) {
		this.periodoSeleccionado = periodoSeleccionado;
	}
	public List<MatriculaMostrar> getMatriculaLista() {
		return matriculaLista;
	}
	public void setMatriculaLista(List<MatriculaMostrar> matriculaLista) {
		this.matriculaLista = matriculaLista;
	}
	
	public class MatriculaMostrar {
		private Matricula matricula;
		private String cedula;
		private String nombres;
		private String tipoSangre;
		private String profesion;
		private String genero;
		private String fechaNacimiento;
		public Matricula getMatricula() {
			return matricula;
		}
		public void setMatricula(Matricula matricula) {
			this.matricula = matricula;
		}
		public String getCedula() {
			return cedula;
		}
		public void setCedula(String cedula) {
			this.cedula = cedula;
		}
		public String getNombres() {
			return nombres;
		}
		public void setNombres(String nombres) {
			this.nombres = nombres;
		}
		public String getTipoSangre() {
			return tipoSangre;
		}
		public void setTipoSangre(String tipoSangre) {
			this.tipoSangre = tipoSangre;
		}
		public String getProfesion() {
			return profesion;
		}
		public void setProfesion(String profesion) {
			this.profesion = profesion;
		}
		public String getGenero() {
			return genero;
		}
		public void setGenero(String genero) {
			this.genero = genero;
		}
		public String getFechaNacimiento() {
			return fechaNacimiento;
		}
		public void setFechaNacimiento(String fechaNacimiento) {
			this.fechaNacimiento = fechaNacimiento;
		}
	}
}
