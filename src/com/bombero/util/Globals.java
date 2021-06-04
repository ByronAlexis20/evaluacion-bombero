package com.bombero.util;

import org.zkoss.zk.ui.Executions;

public class Globals {
	public static final String ESTADO_PERIODO_EN_PROCESO = "EN PROCESO";
	public static final String ESTADO_PERIODO_FINALIZADO = "FINALIZADO";
	
	public static final Integer CODIGO_ESTADO_CIVIL_CASADO = 2;
	public static final String NOMBRE_EXAMEN_COPROPARASITO = "Examen-coproparasito-";
	public static final String NOMBRE_ELECTROCARDIOGRAMA = "Electrocardiograma-";
	public static final String NOMBRE_EXAMEN_SANGRE = "Examen-sangre-";
	public static final String NOMBRE_EXAMEN_ORINA = "Examen-orina-";
	public static final String NOMBRE_FICHA_MEDICA = "Ficha-medica-";
	public static final String NOMBRE_RADIOGRAFIA_TORAX = "Radiografia-torax-";
	
	public static String PATH_SISTEMA = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "temp\\";
	public static String PATH_IMAGEN = "img\\";
	public static String PATH_REPORTE = "rpt";
	public static String PATH_ARCHIVO = "doc";
	
	public static String RESPUESTA_CORRECTA = "C";
	public static String RESPUESTA_INCORRECTA = "I";
	
	//codigos de usuarios
	public static Integer CODIGO_USUARIO_SISTEMAS = 1;
	public static Integer CODIGO_USUARIO_ADMINISTRADOR = 2;
	public static Integer CODIGO_USUARIO_ASPIRANTE = 3;
	public static Integer CODIGO_USUARIO_INSTRUCTOR = 4;
	
	public static Integer NOTA_MINIMA_APROBACION = 70;
}
