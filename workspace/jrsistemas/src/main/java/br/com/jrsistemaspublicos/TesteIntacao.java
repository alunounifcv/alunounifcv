package br.com.jrsistemaspublicos;

import br.com.jrsistemaspublicos.dao.IntAcaoDao;

public class TesteIntacao {

	public static void main(String[] args) {
		 //IntAcaoDao IntacaoDaoA = new IntAcaoDao("cmarapongas");	
		 //IntacaoDaoA.getIntacao(1,2021,8787);

		 IntAcaoDao IntacaoDaoG = new IntAcaoDao("cmguaira");	
		 IntacaoDaoG.getIntacao(1,2021,12634);
         
		 //IntAcaoDao IntacaoDaoT = new IntAcaoDao("fpterraroxa","integracao");	
		 //IntacaoDaoT.getIntacao(1,2021,326);

	}

}
