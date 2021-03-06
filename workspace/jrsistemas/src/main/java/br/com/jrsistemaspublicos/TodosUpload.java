package br.com.jrsistemaspublicos;

import java.io.FileNotFoundException;

import br.com.jrsistemaspublicos.dao.IntDotacaoDao;
import br.com.jrsistemaspublicos.dao.IntPlanoContaDao;
import br.com.jrsistemaspublicos.upload.UploadTodos;


public class TodosUpload {
	public static void main(String[] args) {
      
			//integracao(1, 2021, 04, 8787,"cmarapongas");  //CMArapongas
			integracao(1, 2021, 06, 12634,"cmguaira");  //CMGua?ra
			// integracao(1, 2021, 05, 326,"fpterraroxa");  // Fundo de Terra Roxa
		
	}
	
	public static void integracao(Integer entidade,Integer exercicio, Integer mes, Integer cliente,String baseDados) {
		if(cliente == 12634) {
			UploadTodos vUpload = new UploadTodos(baseDados); //CMGuaira
			try {
				IntDotacaoDao intDotacaoA = new IntDotacaoDao(baseDados);
				intDotacaoA.getIntDotacao(entidade, exercicio,cliente);  
				IntPlanoContaDao intPlanoContaDao = new IntPlanoContaDao(baseDados);
				intPlanoContaDao.getPlanoConta(entidade, exercicio, cliente);
				vUpload.getTodosUpload(entidade, exercicio, mes, cliente);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			
		}else if(cliente == 8787) {
			UploadTodos vUpload = new UploadTodos(baseDados); //CMArapongas
			try {
				IntDotacaoDao intDotacaoA = new IntDotacaoDao(baseDados);
				intDotacaoA.getIntDotacao(entidade, exercicio,cliente);  
				IntPlanoContaDao intPlanoContaDao = new IntPlanoContaDao(baseDados);
				intPlanoContaDao.getPlanoConta(entidade, exercicio, cliente);
				vUpload.getTodosUpload(entidade, exercicio, mes, cliente);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			
		}else if(cliente == 326) {
			UploadTodos vUpload = new UploadTodos(baseDados); //CMArapongas
			try {
				IntDotacaoDao intDotacaoA = new IntDotacaoDao(baseDados);
				intDotacaoA.getIntDotacao(entidade, exercicio,cliente);  
				IntPlanoContaDao intPlanoContaDao = new IntPlanoContaDao(baseDados);
				intPlanoContaDao.getPlanoConta(entidade, exercicio, cliente);
				vUpload.getTodosUpload(entidade, exercicio, mes, cliente);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			
		}
		
	}

}
