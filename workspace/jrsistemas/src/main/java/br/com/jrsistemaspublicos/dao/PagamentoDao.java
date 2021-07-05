package br.com.jrsistemaspublicos.dao;

import java.io.FileNotFoundException;
import java.sql.Connection;

import br.com.jrsistemaspublicos.data.ConexaoElotech;


public class PagamentoDao {
	
	private static Connection connection; 
	

	public PagamentoDao(String dataBase) {
		if (dataBase == "fpterraroxa") {
		    this.connection = ConexaoElotech.getConnection(dataBase);
		}else if(dataBase == "cmguaira") {
			this.connection = ConexaoElotech.getConnection(dataBase);
		}else if(dataBase == "cmarapongasjaneiro") {

			this.connection = ConexaoElotech.getConnection(dataBase);
		}		
		
		
	}
	
	
	public  boolean getTodosUpload(Integer entidade, Integer exercicio, Integer dMes, Integer cliente) throws FileNotFoundException {
		
	
		return false;
	}
}
