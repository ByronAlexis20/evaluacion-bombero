package com.bombero.util;

import org.zkoss.zk.ui.Executions;

public class Globals {
	public static final String ESTADO_PERIODO_EN_PROCESO = "EN PROCESO";
	public static final String ESTADO_PERIODO_FINALIZADO = "FINALIZADO";
	
	public static final Integer CODIGO_ESTADO_CIVIL_CASADO = 2;
	
	public static final String RUTA_PROYECTO = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
}
