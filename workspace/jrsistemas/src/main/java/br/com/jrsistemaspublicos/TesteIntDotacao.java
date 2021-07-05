package br.com.jrsistemaspublicos;

import br.com.jrsistemaspublicos.dao.IntDotacaoDao;

public class TesteIntDotacao {

	public static void main(String[] args) {
		IntDotacaoDao intDotacaoA = new IntDotacaoDao("cmarapongas");
		intDotacaoA.getIntDotacao(1, 2021, 8787);      
		
		//IntDotacaoDao intDotacaoG = new IntDotacaoDao("cmguaira");
		//intDotacaoG.getIntDotacao(1, 2021, 12634);      
		
		//IntDotacaoDao intDotacaoT = new IntDotacaoDao("fpterraroxa");
		//intDotacaoT.getIntDotacao(1, 2021, 326);      
		

	}

}
