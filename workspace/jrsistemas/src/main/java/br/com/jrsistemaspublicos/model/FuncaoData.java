package br.com.jrsistemaspublicos.model;

import java.text.SimpleDateFormat;

public class FuncaoData {

	public static void main(String[] args) {
		String data = "dd-MM-yyyy";
		String hora = "hh mm";
		String data1, hora1;

			java.util.Date agora = new java.util.Date();;
			SimpleDateFormat formata = new SimpleDateFormat(data);
			data1 = formata.format(agora);
			formata = new SimpleDateFormat(hora);
			hora1 = formata.format(agora);
			
			System.out.print("Data: " + data1);
			System.out.print("Hora: " + hora1);
		//System.out.print(hora1+"hora: " + hora);
	}

}
