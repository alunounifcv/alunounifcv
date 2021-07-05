package br.com.jrsistemaspublicos;

import br.com.jrsistemaspublicos.dao.IntPlanoContaDao;

public class TesteIntPlanoConta {

	public static void main(String[] args) {
		
		
		
		//IntPlanoContaDao planoContaDaoA = new IntPlanoContaDao("cmarapongas");
		//planoContaDaoA.getPlanoConta(1, 2021, 8787);

		IntPlanoContaDao planoContaDaoG = new IntPlanoContaDao("cmguaira");
		planoContaDaoG.getPlanoConta(1, 2021, 12634);

		//IntPlanoContaDao planoContaDaoT = new IntPlanoContaDao("fpterraroxa");
		//planoContaDaoT.getPlanoConta(1, 2021, 326);

	}

}
