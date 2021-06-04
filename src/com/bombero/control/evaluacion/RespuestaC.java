package com.bombero.control.evaluacion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.PreguntaDAO;
import com.bombero.model.dao.RespuestaDAO;
import com.bombero.model.entity.Pregunta;
import com.bombero.model.entity.Respuesta;
import com.bombero.util.Globals;

public class RespuestaC {
	@Wire Window winRespuestas;
	@Wire Textbox txtRespuesta;
	@Wire Checkbox chkCorrecta;
	Pregunta pregunta;
	RespuestaDAO respuestaDAO = new RespuestaDAO();
	PreguntaDAO preguntaDAO = new PreguntaDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		pregunta = (Pregunta)Executions.getCurrent().getArg().get("Pregunta");
	}
	@Command
	public void agregarRespuesta(){
		try {
			if(txtRespuesta.getText().equals("")) {
				Clients.showNotification("Debe registrar la respuesta","info",txtRespuesta,"end_center",2000);
				return;
			}
			pregunta = preguntaDAO.buscarPreguntaPorId(pregunta.getIdPregunta());
			//verificar si ya hau una respuesta correcta
			boolean respuestaCorrecta = false;
			String correcta = Globals.RESPUESTA_INCORRECTA;
			if(chkCorrecta.isChecked())
				correcta = Globals.RESPUESTA_CORRECTA;
			if(pregunta.getRespuestas().size() > 0) {
				for(Respuesta res : pregunta.getRespuestas()) {
					if(res.getEstado().equals("A")) {
						if(res.getCorrecta().equals(Globals.RESPUESTA_CORRECTA)){
							respuestaCorrecta = true;
						}
					}
				}
			}
			if(chkCorrecta.isChecked()) {
				if(respuestaCorrecta == true) {
					Clients.showNotification("Ya existe una respuesta correcta");
					return;
				}
			}
			Respuesta respuestaGrabar = new Respuesta();
			respuestaGrabar.setCorrecta(correcta);
			respuestaGrabar.setEstado("A");
			respuestaGrabar.setIdRespuesta(null);
			respuestaGrabar.setRespuesta(txtRespuesta.getText());
			respuestaGrabar.setPregunta(pregunta);
			preguntaDAO.getEntityManager().getTransaction().begin();
			if(pregunta.getRespuestas().size() == 0) {
				List<Respuesta> listaR = new ArrayList<>();
				listaR.add(respuestaGrabar);
				pregunta.setRespuestas(listaR);
				preguntaDAO.getEntityManager().merge(pregunta);
			} else {
				preguntaDAO.getEntityManager().merge(respuestaGrabar);
			}
			preguntaDAO.getEntityManager().getTransaction().commit();
			Clients.showNotification("Grabado correctamente");
			salir();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "Respuesta.buscarPorPregunta", null);
		winRespuestas.detach();
	}
}
