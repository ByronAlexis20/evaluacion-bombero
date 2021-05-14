package com.bombero.control.seguridad;

import java.io.IOException;
import java.util.List;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.PerfilDAO;
import com.bombero.model.dao.UsuarioDAO;
import com.bombero.model.entity.Perfil;
import com.bombero.model.entity.Usuario;
import com.bombero.util.ControllerHelper;
import com.bombero.util.FileUtil;

public class UsuarioEditarC {
	@Wire private Window winUsuarioEditar;
	@Wire private Combobox cboPerfil;
	@Wire private Textbox txtNombres;
	@Wire private Textbox txtApellidos;
	@Wire private Textbox txtCedula;
	@Wire private Textbox txtTelefono;
	@Wire private Textbox txtUsuario;
	@Wire private Textbox txtDireccion;
	@Wire private Textbox txtClave;
	
	PerfilDAO perfilDAO = new PerfilDAO();
	
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	Usuario usuario;
	Perfil perfilSeleccionado;
	ControllerHelper helper = new ControllerHelper();
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		usuario = (Usuario) Executions.getCurrent().getArg().get("Usuario");
		if (usuario == null) {
			usuario = new Usuario();
			usuario.setEstado("A");
			perfilSeleccionado = null;
		}else {
			perfilSeleccionado = usuario.getPerfil();
			cboPerfil.setText(usuario.getPerfil().getPerfil());
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar(){
		if(validarDatos() == false) {
			return;
		}
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {		
					try {						
						usuario.setPerfil((Perfil)cboPerfil.getSelectedItem().getValue());
						usuario.setClave(helper.encriptar(txtClave.getText()));
						usuarioDAO.getEntityManager().getTransaction().begin();			
						if (usuario.getIdUsuario() == null) {
							usuarioDAO.getEntityManager().persist(usuario);
						}else{
							usuario = (Usuario) usuarioDAO.getEntityManager().merge(usuario);
						}
						usuarioDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						BindUtils.postGlobalCommand(null, null, "Usuario.buscarPorPatron", null);
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						usuarioDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	
	
	public boolean validarDatos() {
		if(txtCedula.getText().isEmpty()) {
			Clients.showNotification("Debe registrar el número de cédula","info",txtCedula,"end_center",2000);
			txtCedula.focus();
			return false;
		}
		if(!helper.validarDeCedula(txtCedula.getText())) {
			Clients.showNotification("Número de CÉDULA NO VÁLIDA!","info",txtCedula,"end_center",2000);
			txtCedula.focus();
			return false;
		}
		if(cboPerfil.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar el Perfil de usuario","info",cboPerfil,"end_center",2000);
			return false;
		}
		//luego preguntar si el numero de documento ya se encuentra sobre los registros
		if(validarUsuarioExistente() == true) {
			Clients.showNotification("Ya hay un Usuario con el número de documento " + txtCedula.getText() + "!","info",txtCedula,"end_center",2000);
			txtCedula.focus();
			return false;
		}
		if(txtNombres.getText().isEmpty()) {
			Clients.showNotification("Debe registrar nombre del usuario","info",txtNombres,"end_center",2000);
			txtNombres.setFocus(true);
			return false;
		}
		if(txtApellidos.getText().isEmpty()) {
			Clients.showNotification("Debe registrar apellidos del usuario","info",txtApellidos,"end_center",2000);
			txtApellidos.setFocus(true);
			return false;
		}
		if(txtTelefono.getText().isEmpty()) {
			Clients.showNotification("Debe registrar Telefono del usuario","info",txtTelefono,"end_center",2000);
			txtTelefono.setFocus(true);
			return false;
		}
		if(txtUsuario.getText().isEmpty()) {
			Clients.showNotification("Debe registrar Usuario del usuario","info",txtUsuario,"end_center",2000);
			txtUsuario.setFocus(true);
			return false;
		}
		int idUsuario;
		if(usuario.getIdUsuario() != null)
			idUsuario = usuario.getIdUsuario();
		else
			idUsuario = 0;
		if(usuarioDAO.getBuscarUsuario(txtUsuario.getText(),idUsuario).size() > 0) {
			Clients.showNotification("Usuario ya existe","info",txtUsuario,"end_center",2000);
			return false;
		}
		if(txtClave.getText().isEmpty()) {
			Clients.showNotification("Debe registrar Clave del usuario","info",txtClave,"end_center",2000);
			txtClave.setFocus(true);
			return false;
		}
		return true;
	}
	
	/** Validar si el usuario existe a traves de la cedula */
	private boolean validarUsuarioExistente() {
		try {
			boolean bandera = false;
			List<Usuario> listaUsuario;
			if (usuario.getIdUsuario() == null) {
				listaUsuario = usuarioDAO.getValidarUsuarioExistente(txtCedula.getText().toString());
			}else {
				listaUsuario = usuarioDAO.getValidarUsuarioExistenteDiferente(txtCedula.getText().toString(),usuario.getIdUsuario());
			}
			
			if(listaUsuario.size() != 0)
				bandera = true;
			else
				bandera = false;
			return bandera;
		}catch(Exception ex) {
			return false;
		}
	}
	
	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "Usuario.buscarPorPatron", null);
		winUsuarioEditar.detach();
	}
	@Command
	@NotifyChange("imagenUsuario")
	public void subir(@ContextParam(ContextType.BIND_CONTEXT) BindContext contexto) {
		String pathRetornado; 
		UploadEvent eventoCarga = (UploadEvent) contexto.getTriggerEvent();
		pathRetornado = FileUtil.cargaArchivo(eventoCarga.getMedia());
		usuario.setFoto(pathRetornado);
	}
	@Command
	public void descargar() {
		if (usuario.getFoto() != null) {
			FileUtil.descargaArchivo(usuario.getFoto());
		}
	}
	public List<Perfil> getPerfiles(){
		return perfilDAO.getPerfilesPorDescripcion("");
	}
	public AImage getImagenUsuario() {
		AImage retorno = null;
		if (usuario.getFoto() != null) {
			try {
				retorno = FileUtil.getImagenTamanoFijo(new AImage(usuario.getFoto()), 100, -1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return retorno; 
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Perfil getPerfilSeleccionado() {
		return perfilSeleccionado;
	}
	public void setPerfilSeleccionado(Perfil perfilSeleccionado) {
		this.perfilSeleccionado = perfilSeleccionado;
	}
}
