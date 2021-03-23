package com.bombero.control.seguridad;

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
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.bombero.model.dao.EmpresaDAO;
import com.bombero.model.entity.Empresa;
import com.bombero.util.ControllerHelper;

public class EmpresaC {
	@Wire Textbox txtRuc;
	@Wire Textbox txtNombre;
	@Wire Textbox txtRepresentante;
	@Wire Textbox txtDireccion;
	@Wire Textbox txtTelefono;
	@Wire Textbox txtEmail;
	
	Empresa empresa;
	EmpresaDAO empresaDAO = new EmpresaDAO();
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);	 
		recuperarEmpresa();
	}
	
	@GlobalCommand("Empresa.findAll")
	@Command
	@NotifyChange({"empresa"})
	private void recuperarEmpresa() {
		List<Empresa> lista = empresaDAO.obtenerListaEmpresa();
		if(lista.size() > 0) {
			empresa = lista.get(0);
		}else {
			empresa = new Empresa();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar() {
		try {
			if(validarDatos() == false) {
				return;
			}
			Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getName().equals("onYes")) {		
						try {		
							empresaDAO.getEntityManager().getTransaction().begin();
							if (empresa.getIdEmpresa() == null) {
								empresaDAO.getEntityManager().persist(empresa);
							}else{
								empresa = (Empresa) empresaDAO.getEntityManager().merge(empresa);
							}			
							empresaDAO.getEntityManager().getTransaction().commit();
							Clients.showNotification("Proceso Ejecutado con exito.");
							BindUtils.postGlobalCommand(null, null, "Empresa.findAll", null);
						} catch (Exception e) {
							e.printStackTrace();
							empresaDAO.getEntityManager().getTransaction().rollback();
						}
					}
				}
			});
		}catch(Exception ex) {
			
		}
	}
	
	public boolean validarDatos() {
		if(empresa.getRuc().length() < 13) {
			Clients.showNotification("Cantidad de números incorrecto para el ruc","info",txtRuc,"end_center",2000);
			txtRuc.setFocus(true);
			return false;
		}
		if(!txtEmail.getText().isEmpty()) {
			if(!ControllerHelper.validarEmail(txtEmail.getText())) {
				Clients.showNotification("El correo ingresado no es valido","info",txtEmail,"end_center",2000);
				txtEmail.setFocus(true);
				return false;
			}
		}
		return true;
	}
	public Empresa getEmpresa() {
		return empresa;
	}
	
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}	
}