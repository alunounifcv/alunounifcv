package br.com.jrsistemaspublicos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.jrsistemaspublicos.data.ConexaoElotech;
import br.com.jrsistemaspublicos.model.IntPlanoConta;


public class IntPlanoContaDao {
	
	private static Connection connection;

	public IntPlanoContaDao(String dataBase) {
		if (dataBase == "fpterraroxa") {
			this.connection = ConexaoElotech.getConnection(dataBase);
		} else if (dataBase == "cmguaira") {
			this.connection = ConexaoElotech.getConnection(dataBase);
		} else if (dataBase == "cmarapongas") {
			this.connection = ConexaoElotech.getConnection(dataBase);
		}

	}

	public boolean getPlanoConta(Integer entidade, Integer exercicio, Integer cliente) {
		if (entidade != null && exercicio != null && cliente != null) {
		String sqlInsereTab = "CREATE TABLE if not exists intplanocontas(\r\n" + "  entidade integer,\r\n"
				+ "  eloexercicio double precision,\r\n" + "  eloconta character varying(35),\r\n"
				+ "  elodescricao character varying(250),\r\n" + "  elocontacorrente numeric(10,0),\r\n"
				+ "  ipmconta character varying(35),\r\n" + "  ipmdescricao character varying(250),\r\n"
				+ "  ipmvinculo character varying(8),\r\n" + "  constraint fk_IntPlanoContas primary key \r\n"
				+ "    (entidade, eloexercicio, eloconta,elocontacorrente))\r\n" + "WITH ( OIDS=FALSE);\r\n" + "\r\n"
				+ "ALTER TABLE public.intplanocontas\r\n" + "  OWNER TO postgres;";

		try {
			PreparedStatement ent = connection.prepareStatement(sqlInsereTab);
			ent.execute();
			ent.close();

			String sqlIntacao = "select distinct " + cliente + " entidade,        elc.exercicio eloexercicio,\r\n"
					+ "					       elc.conta eloconta,        (select distinct descricao from siscop.plano p \r\n"
					+ "					           where p.entidade = elc.entidade \r\n"
					+ "					             and p.exercicio = elc.exercicio \r\n"
					+ "					             and p.conta = elc.conta) elodescricao, \r\n"
					+ "					       elc.idcontacorrente as elocontacorrente, \r\n"
					+ "					       rpad(elc.conta,20,'0') ipmconta, \r\n"
					+ "					       (select distinct descricao from siscop.plano p \r\n"
					+ "					           where p.entidade = elc.entidade \r\n"
					+ "					             and p.exercicio = elc.exercicio \r\n"
					+ "					             and p.conta = elc.conta) ipmdescricao,\r\n"
					+ "					       lpad(cast(case when (elc.conta like '1111%' or elc.conta like '1141%') then \r\n"
					+ "					            (select (select cb.fonterecurso from siscop.contabancaria cb\r\n"
					+ "					                       where cb.entidade = cbv.entidade \r\n"
					+ "					                         and cb.reduzido = cbv.reduzido ) \r\n"
					+ "					               from siscop.contabancariavinculo cbv\r\n"
					+ "					               inner join siscop.contacorrente cc \r\n"
					+ "					                       on cbv.entidade = cc.entidade \r\n"
					+ "					                      and cbv.id = cc.idcontabancariavinculo\r\n"
					+ "					               inner join siscop.planocontacorrente pcc\r\n"
					+ "					                       on pcc.entidade = cbv.entidade\r\n"
					+ "					                      and pcc.idcontacorrente = cc.idcontacorrente\r\n"
					+ "					                where pcc.entidade = elc.entidade\r\n"
					+ "					                  and pcc.exercicio = elc.exercicio\r\n"
					+ "					                  and pcc.conta = elc.conta\r\n"
					+ "					                  and pcc.idcontacorrente = elc.idcontacorrente) else 0 end as varchar(8)),8,'0') ipmvinculo\r\n"
					+ "					    from siscop.eventoslancadosconta elc   inner join siscop.eventoslancados el \r\n"
					+ "					          on elc.entidade = el.entidade         and elc.exercicio = el.exercicio \r\n"
					+ "					         and elc.tipoevento = el.tipoevento \r\n"
					+ "					         and elc.grupoevento = el.grupoevento         and elc.evento = el.evento\r\n"
					+ "					         and elc.nrolancamento = el.nrolancamento  where elc.entidade = ? \r\n"
					+ "					    and elc.exercicio = ?  ";

			try {
				PreparedStatement psa = connection.prepareStatement(sqlIntacao);

				psa.setInt(1, entidade);
				psa.setInt(2, exercicio);

				ResultSet rsa = psa.executeQuery();

				while (rsa.next()) {
					IntPlanoConta planoconta = new IntPlanoConta();

					planoconta.setEntidade(cliente);
					planoconta.setEloexercicio(rsa.getInt("eloexercicio"));
					planoconta.setEloconta(rsa.getString("eloconta"));
					planoconta.setElodescricao(rsa.getString("elodescricao"));
					planoconta.setElocontacorrente(rsa.getInt("elocontacorrente"));
					planoconta.setIpmconta(rsa.getString("ipmconta"));
					planoconta.setIpmdescricao(rsa.getString("ipmdescricao"));
					planoconta.setIpmvinculo(rsa.getString("ipmvinculo"));

					String sqlValida = "select count(*) as quantidade from intPlanoContas   \r\n"
							+ "  where entidade = ? \r\n" + "    and eloexercicio = ? \r\n"
							+ "    and eloconta = ? \r\n" + "    and elocontacorrente = ? ";

					PreparedStatement vrs = connection.prepareStatement(sqlValida);
					vrs.setInt(1, cliente);
					vrs.setInt(2, rsa.getInt("eloexercicio"));
					vrs.setString(3, rsa.getString("eloconta"));
					vrs.setInt(4, rsa.getInt("elocontacorrente"));

					ResultSet validaEntidade = vrs.executeQuery();

					validaEntidade.next();

					int quantidade = validaEntidade.getInt("quantidade");

					if (quantidade == 0) {
						String sqlarsalvar = "insert into intplanocontas(\r\n"
								+ "            entidade, eloexercicio, eloconta, elodescricao, elocontacorrente, \r\n"
								+ "            ipmconta, ipmdescricao, ipmvinculo)\r\n"
								+ "    VALUES (?, ?, ?, ?, ?, \r\n"
								+ "            ?, ?, ?)";
						try {
							PreparedStatement psipl = connection.prepareStatement(sqlarsalvar);

							psipl.setInt(1, cliente);
							psipl.setInt(2, rsa.getInt("eloexercicio"));
							psipl.setString(3, rsa.getString("eloconta"));
							psipl.setString(4, rsa.getString("elodescricao"));
							psipl.setInt(5, rsa.getInt("elocontacorrente"));
							psipl.setString(6, rsa.getString("ipmconta"));
							psipl.setString(7, rsa.getString("ipmdescricao"));
							psipl.setString(8, rsa.getString("ipmvinculo"));
							psipl.execute();
							psipl.close();
							System.out.println("Cadastrado com sucesso!!");

						} catch (SQLException e) {
							e.printStackTrace();

						}

					} else {
						System.out.println("Já possuí informações na Tabela para entidade-> " + cliente);

					}

				}
			

			
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
		}
		
		System.out.println("não possui Conta");
		return true;

	}

}
