package br.com.jrsistemaspublicos;

import br.com.jrsistemaspublicos.dao.IntFonteRecursoDao;

public class TesteFonteRecurso {

	public static void main(String[] args) {
		
		IntFonteRecursoDao fonteRecursoDao = new IntFonteRecursoDao("cmarapongas");			
		fonteRecursoDao.getFonteRecurso(8787);
		
		//IntFonteRecursoDao fonteRecursoDao = new IntFonteRecursoDao("cmguaira");			
		//fonteRecursoDao.getFonteRecurso(12634);
		
		//IntFonteRecursoDao fonteRecursoDao = new IntFonteRecursoDao("fpterraroxa");			
		//fonteRecursoDao.getFonteRecurso(326);
		
		
		System.out.println("Gerado Com Sucesso");
	
		

	}

}
