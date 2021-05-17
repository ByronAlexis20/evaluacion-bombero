package com.bombero.control.registros;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.CirugiaDAO;
import com.bombero.model.dao.FamiliarDAO;
import com.bombero.model.dao.FichaMedicaDAO;
import com.bombero.model.entity.Aspirante;
import com.bombero.model.entity.Cirugia;
import com.bombero.model.entity.Familiar;
import com.bombero.model.entity.FichaMedica;
import com.bombero.util.Globals;

public class FichaMedicaC {
	@Wire private Window winFichaMedica;
	@Wire private Textbox txtVacunas;
	@Wire private Textbox txtMedicinas;
	@Wire private Textbox txtAlimentos;
	@Wire private Textbox txtNoPartos;
	@Wire private Textbox txtNoAbortos;
	@Wire private Textbox txtNoCesareas;
	@Wire private Textbox txtNoHijos;
	@Wire private Textbox txtExamenSangre;
	@Wire private Button btnUploadExamenSangre;
	@Wire private Textbox txtExamenOrina;
	@Wire private Button btnUploadExamenOrina;
	@Wire private Textbox txtExamenCoproparasitario;
	@Wire private Button btnUploadExamenCoproparasitario;
	@Wire private Textbox txtRadiografiaTorax;
	@Wire private Button btnUploadRadiografiaTorax;
	@Wire private Textbox txtElectrocardiograma;
	@Wire private Button btnUploadElectrocardiograma;
	@Wire private Textbox txtFichaMedica;
	@Wire private Button btnUploadFichaMedica;
	@Wire private Button btnNuevoFamiliar;
	@Wire private Button btnEliminarFamiliar;
	@Wire Listbox lstFamiliares;
	@Wire private Button btnNuevaCirugia;
	@Wire private Button btnEliminarCirugia;
	@Wire private Listbox lstCirugias;
	Media mediaExamenSangre;
	Media mediaExamenOrina;
	Media mediaExamenCoproparasito;
	Media mediaRadiografiaTorax;
	Media mediaElectrocardiograma;
	Media mediaFichaMedica;
	Aspirante aspirante;
	FichaMedica fichaMedica;
	FamiliarDAO familiarDAO = new FamiliarDAO();
	CirugiaDAO cirugiaDAO = new CirugiaDAO();
	FichaMedicaDAO fichaMedicaDAO = new FichaMedicaDAO();
	List<Familiar> listaFamiliar;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		aspirante = (Aspirante) Executions.getCurrent().getArg().get("Aspirante");
		if(aspirante != null) {
			List<FichaMedica> fm = fichaMedicaDAO.buscarPorAspirante(aspirante.getIdAspirante());
			if(fm.size() > 0) {
				fichaMedica = fm.get(0);
				recuperarDatos();
			} else {
				fichaMedica = new FichaMedica();
			}
		}
	}
	private void recuperarDatos() {
		txtVacunas.setText(fichaMedica.getNombreVacunas());
		txtMedicinas.setText(fichaMedica.getNombreMedicinasAlergia());
		txtAlimentos.setText(fichaMedica.getNombreAlimentosAlergia());
		txtNoPartos.setText(String.valueOf(fichaMedica.getNoPartos()));
		txtNoAbortos.setText(String.valueOf(fichaMedica.getNoAbortos()));
		txtNoCesareas.setText(String.valueOf(fichaMedica.getNoCesarea()));
		txtNoHijos.setText(String.valueOf(fichaMedica.getNoHijos()));
		txtExamenSangre.setText(fichaMedica.getNombreExamenSangre());
		txtExamenOrina.setText(fichaMedica.getNombreExamenOrina());
		txtExamenCoproparasitario.setText(fichaMedica.getNombreCoproparasitario());
		txtRadiografiaTorax.setText(fichaMedica.getNombreRadiografiaTorax());
		txtElectrocardiograma.setText(fichaMedica.getNombreElectrocardiograma());
		txtFichaMedica.setText(fichaMedica.getNombreFichaMedica());
		recuperarFamiliares(fichaMedica.getIdFichaMedica());
		recuperarCirugias(fichaMedica.getIdFichaMedica());
	}
	private void recuperarFamiliares(Integer id) {
		listaFamiliar = familiarDAO.buscarPorFichaMedica(id);
		//lstFamiliares.setModel(new ListModelList(lista));
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void recuperarCirugias(Integer id) {
		List<Cirugia> lista = cirugiaDAO.buscarPorFichaMedica(id);
		lstCirugias.setModel(new ListModelList(lista));
	}
	@Command
	public void subirExamenSangre(@ContextParam(ContextType.BIND_CONTEXT) BindContext contexto) {
		UploadEvent eventoCarga = (UploadEvent) contexto.getTriggerEvent();
		mediaExamenSangre = eventoCarga.getMedia();
		txtExamenSangre.setText(mediaExamenSangre.getName());
	}
	@Command
	public void subirExamenOrina(@ContextParam(ContextType.BIND_CONTEXT) BindContext contexto) {
		UploadEvent eventoCarga = (UploadEvent) contexto.getTriggerEvent();
		mediaExamenOrina = eventoCarga.getMedia();
		txtExamenOrina.setText(mediaExamenOrina.getName());
	}
	@Command
	public void subirExamenCoproparasitario(@ContextParam(ContextType.BIND_CONTEXT) BindContext contexto) {
		UploadEvent eventoCarga = (UploadEvent) contexto.getTriggerEvent();
		mediaExamenCoproparasito = eventoCarga.getMedia();
		txtExamenCoproparasitario.setText(mediaExamenCoproparasito.getName());
	}
	@Command
	public void subirRadiografiaTorax(@ContextParam(ContextType.BIND_CONTEXT) BindContext contexto) {
		UploadEvent eventoCarga = (UploadEvent) contexto.getTriggerEvent();
		mediaRadiografiaTorax = eventoCarga.getMedia();
		txtRadiografiaTorax.setText(mediaRadiografiaTorax.getName());
	}
	@Command
	public void subirElectrocardiograma(@ContextParam(ContextType.BIND_CONTEXT) BindContext contexto) {
		UploadEvent eventoCarga = (UploadEvent) contexto.getTriggerEvent();
		mediaElectrocardiograma = eventoCarga.getMedia();
		txtElectrocardiograma.setText(mediaElectrocardiograma.getName());
	}
	@Command
	public void subirFichaMedica(@ContextParam(ContextType.BIND_CONTEXT) BindContext contexto) {
		UploadEvent eventoCarga = (UploadEvent) contexto.getTriggerEvent();
		mediaFichaMedica = eventoCarga.getMedia();
		txtFichaMedica.setText(mediaFichaMedica.getName());
	}
	@Command
	public void nuevoFamiliar() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("FichaMedica", fichaMedica);
		params.put("Ventana", this);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/registros/aspirantes/familiares.zul", winFichaMedica, params);
		ventanaCargar.doModal();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@NotifyChange({"listaFamiliar"})
	@Command
	public void agregarListaFamiliar(Familiar familiar) {
		List<Familiar> lista = new ArrayList<>();
		for(Familiar f : listaFamiliar) {
			lista.add(f);
		}
		lista.add(familiar);
		listaFamiliar = lista;
		lstFamiliares.setModel(new ListModelList(listaFamiliar));
	}
	@Command
	public void eliminarFamiliar() {
		
	}
	@Command
	public void nuevaCirugia() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("FichaMedica", null);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/registros/aspirantes/cirugia.zul", null, params);
		ventanaCargar.doModal();
	}
	@Command
	public void eliminarCirugia() {
		
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar() {
		try {
			Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getName().equals("onYes")) {		
						try {
							llenarDatos();
							if(fichaMedica.getAspirante() == null)
								fichaMedica.setAspirante(aspirante);
							fichaMedicaDAO.getEntityManager().getTransaction().begin();
							if(fichaMedica.getIdFichaMedica() == null) {
								fichaMedicaDAO.getEntityManager().persist(fichaMedica);
							}else {
								fichaMedicaDAO.getEntityManager().merge(fichaMedica);
							}
							fichaMedicaDAO.getEntityManager().getTransaction().commit();
							Clients.showNotification("Proceso Ejecutado con exito.");
							salir();
						} catch (Exception e) {
							e.printStackTrace();
							fichaMedicaDAO.getEntityManager().getTransaction().rollback();
						}
					}
				}
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void llenarDatos() throws IOException {
		fichaMedica.setEstado("A");
		fichaMedica.setNoAbortos(Integer.parseInt(txtNoAbortos.getText()));
		fichaMedica.setNoCesarea(Integer.parseInt(txtNoCesareas.getText()));
		fichaMedica.setNoHijos(Integer.parseInt(txtNoHijos.getText()));
		fichaMedica.setNombreAlimentosAlergia(txtAlimentos.getText());
		fichaMedica.setNombreCoproparasitario(txtExamenCoproparasitario.getText());
		fichaMedica.setNombreElectrocardiograma(txtElectrocardiograma.getText());
		fichaMedica.setNombreExamenOrina(txtExamenOrina.getText());
		fichaMedica.setNombreExamenSangre(txtExamenSangre.getText());
		fichaMedica.setNombreFichaMedica(txtFichaMedica.getText());
		fichaMedica.setNombreMedicinasAlergia(txtMedicinas.getText());
		fichaMedica.setNombreRadiografiaTorax(txtRadiografiaTorax.getText());
		fichaMedica.setNombreVacunas(txtVacunas.getText());
		fichaMedica.setNoPartos(Integer.parseInt(txtNoPartos.getText()));
		String ruta = Globals.PATH_SISTEMA + Globals.PATH_ARCHIVO;
		File folder = new File(ruta);
		if (folder.exists()) {
		}else {
			folder.mkdir();
		}
		if(mediaExamenCoproparasito != null) {
			String examen = ruta + "\\" + Globals.NOMBRE_EXAMEN_COPROPARASITO + aspirante.getCedula() + ".pdf";
			fichaMedica.setRutaCoproparasitario(examen);
			Files.copy(new File(examen),mediaExamenCoproparasito.getStreamData());
		}
		if(mediaElectrocardiograma != null) {
			String examen = ruta + "\\" + Globals.NOMBRE_ELECTROCARDIOGRAMA + aspirante.getCedula() + ".pdf";
			fichaMedica.setRutaElectrocardiograma(examen);
			Files.copy(new File(examen),mediaElectrocardiograma.getStreamData());
		}
		if(mediaExamenOrina != null) {
			String examen = ruta + "\\" + Globals.NOMBRE_EXAMEN_ORINA + aspirante.getCedula() + ".pdf";
			fichaMedica.setRutaExamenOrina(examen);
			Files.copy(new File(examen),mediaExamenOrina.getStreamData());
		}
		if(mediaExamenSangre != null) {
			String examen = ruta + "\\" + Globals.NOMBRE_EXAMEN_SANGRE + aspirante.getCedula() + ".pdf";
			fichaMedica.setRutaExamenSangre(examen);
			Files.copy(new File(examen),mediaExamenSangre.getStreamData());
		}
		if(mediaFichaMedica != null) {
			String examen = ruta + "\\" + Globals.NOMBRE_FICHA_MEDICA + aspirante.getCedula() + ".pdf";
			fichaMedica.setRutaFichaMedica(examen);
			Files.copy(new File(examen),mediaFichaMedica.getStreamData());
		}
		if(mediaRadiografiaTorax != null) {
			String examen = ruta + "\\" + Globals.NOMBRE_RADIOGRAFIA_TORAX + aspirante.getCedula() + ".pdf";
			fichaMedica.setRutaRadiografiaTorax(examen);
			Files.copy(new File(examen),mediaRadiografiaTorax.getStreamData());
		}
	}
	@Command
	public void salir() {
		winFichaMedica.detach();
	}
	public Aspirante getAspirante() {
		return aspirante;
	}
	public void setAspirante(Aspirante aspirante) {
		this.aspirante = aspirante;
	}
	public FichaMedica getFichaMedica() {
		return fichaMedica;
	}
	public void setFichaMedica(FichaMedica fichaMedica) {
		this.fichaMedica = fichaMedica;
	}
	public List<Familiar> getListaFamiliar() {
		return listaFamiliar;
	}
	public void setListaFamiliar(List<Familiar> listaFamiliar) {
		this.listaFamiliar = listaFamiliar;
	}
}
