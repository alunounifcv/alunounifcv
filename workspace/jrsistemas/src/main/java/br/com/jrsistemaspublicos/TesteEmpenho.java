package br.com.jrsistemaspublicos;

import java.io.FileNotFoundException;

import br.com.jrsistemaspublicos.dao.EmpenhoDao;


public class TesteEmpenho {

	public static void main(String[] args) throws FileNotFoundException {
		 EmpenhoDao empenhoDao = new EmpenhoDao("cmarapongas");	
		 empenhoDao.getEmpenhos(1, 2021, 2, 8787);

		 //IntAcaoDao IntacaoDaoG = new IntAcaoDao("fpterraroxa");	
		 //IntacaoDaoG.getIntacao(1,2021,12634);
         
		 //IntAcaoDao IntacaoDaoT = new IntAcaoDao("fpterraroxa","integracao");	
		 //IntacaoDaoT.getIntacao(1,2021,326);

	}

}
