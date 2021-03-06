package br.com.jrsistemaspublicos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.jrsistemaspublicos.data.ConexaoElotech;
import br.com.jrsistemaspublicos.model.IntDotacao;

public class IntDotacaoDao {

	private static Connection connection;

	public IntDotacaoDao(String dataBase) {
		if (dataBase == "fpterraroxa") {
			this.connection = ConexaoElotech.getConnection(dataBase);
		} else if (dataBase == "cmguaira") {

			this.connection = ConexaoElotech.getConnection(dataBase);
		} else if (dataBase == "cmarapongas") {

			this.connection = ConexaoElotech.getConnection(dataBase);
		}
	}

	public boolean getIntDotacao(Integer entidade, Integer exercicio, Integer cliente) {
		if (entidade != null && exercicio != null && cliente != null) {
			String sqlInsereTab = "CREATE TABLE if not exists intdotacao\r\n" + "(\r\n"
					+ "  entidade double precision NOT NULL,\r\n" + "  eloexercicio double precision NOT NULL,\r\n"
					+ "  eloprogramatica character varying(35) NOT NULL,\r\n"
					+ "  elofonterecurso character varying(10) NOT NULL,\r\n"
					+ "  elodespesa character varying(6) NOT NULL,\r\n"
					+ "  elodesdobramento character varying(2) NOT NULL,\r\n"
					+ "  elosubdesdobramento character varying(2) NOT NULL,\r\n"
					+ "  elodescricao character varying(250),\r\n" + "  ipmelemento character varying(20),\r\n"
					+ "  ipmdescricao character varying(250),\r\n" + "  ipmvinculo character varying(8),\r\n"
					+ "  ipmtipoacao character varying(2),\r\n"
					+ "  CONSTRAINT fk_intdotacao PRIMARY KEY (entidade, eloexercicio, eloprogramatica, elofonterecurso, \r\n"
					+ "                                        elodespesa, elodesdobramento, elosubdesdobramento)\r\n"
					+ ")\r\n" + "WITH (\r\n" + "  OIDS=FALSE\r\n" + ");\r\n" + "ALTER TABLE public.intdotacao\r\n"
					+ "  OWNER TO postgres;";			
			
			try {
				PreparedStatement ent = connection.prepareStatement(sqlInsereTab);
				ent.execute();
				ent.close();

				String sqlIntdotacao = "select " + cliente + " entidade,\r\n"
						+ "				       d.exercicio eloexercicio,\r\n"
						+ "					   d.programatica eloprogramatica,\r\n"
						+ "					   d.fonterecurso elofonterecurso,\r\n"
						+ "					   dd.despesa elodespesa,\r\n"
						+ "				       dd.desdobramento elodesdobramento,\r\n"
						+ "					   dd.subdesdobramento elosubdesdobramento,\r\n"
						+ "					   dd.descricao elodescricao,\r\n"
						+ "					   dd.despesa||dd.desdobramento||dd.subdesdobramento ipmelemento,\r\n"
						+ "					   dd.descricao ipmdescricao,\r\n"
						+ "					   d.fonterecurso ipmvinculo,\r\n"
						+ "					   (select ipmtipo from intacao ia\r\n"
						+ "					      where ia.entidade = " + cliente + "\r\n"
						+ "						    and ia.eloprojetoatividade = substring(d.programatica,15,4))  ipmtipoacao\r\n"
						+ "				   from siscop.despesa d\r\n"
						+ "				      inner join siscop.desdobradesp dd\r\n"
						+ "					          on d.entidade = dd.entidade\r\n"
						+ "						     and d.exercicio = dd.exercicio\r\n"
						+ "						     and substring(d.programatica,19,6) = dd.despesa\r\n"
						+ "							 and dd.movsn = 'S'			 \r\n"
						+ "				  where d.entidade = ? \r\n" + "				    and d.exercicio = ? \r\n"
						+ "					and d.movsn = 'S'";
				try {
					PreparedStatement psa = connection.prepareStatement(sqlIntdotacao);

					psa.setInt(1, entidade);
					psa.setInt(2, exercicio);

					ResultSet rsa = psa.executeQuery();
					while (rsa.next()) {
						IntDotacao intDotacao = new IntDotacao();
						intDotacao.setEntidade(cliente);
						intDotacao.setEloexercicio(rsa.getInt("eloexercicio"));
						intDotacao.setEloprogramatica(rsa.getString("eloprogramatica"));
						intDotacao.setElofonterecurso(rsa.getInt("elofonterecurso"));
						intDotacao.setElodespesa(rsa.getString("elodespesa"));
						intDotacao.setElodesdobramento(rsa.getString("elodesdobramento"));
						intDotacao.setElosubdesdobramento(rsa.getString("elosubdesdobramento"));
						intDotacao.setElodescricao(rsa.getString("elodescricao"));
						intDotacao.setIpmelemento(rsa.getString("ipmelemento"));
						intDotacao.setIpmdescricao(rsa.getString("ipmdescricao"));
						intDotacao.setIpmvinculo(rsa.getString("ipmvinculo"));
						intDotacao.setIpmtipoacao(rsa.getString("ipmtipoacao"));

						String sqlValida = "select count(*) as quantidade from intdotacao \r\n"
								+ "  where entidade = ? \r\n" + "    and eloexercicio = ? \r\n"
								+ "    and eloprogramatica = ? \r\n" + "    and elofonterecurso = ? \r\n"
								+ "    and elodespesa = ? \r\n" + "    and elodesdobramento = ? \r\n"
								+ "    and elosubdesdobramento = ? ";

						PreparedStatement vrs = connection.prepareStatement(sqlValida);
						vrs.setInt(1, cliente);
						vrs.setInt(2, rsa.getInt("eloexercicio"));
						vrs.setString(3, rsa.getString("eloprogramatica"));
						vrs.setInt(4, rsa.getInt("elofonterecurso"));
						vrs.setString(5, rsa.getString("elodespesa"));
						vrs.setString(6, rsa.getString("elodesdobramento"));
						vrs.setString(7, rsa.getString("elosubdesdobramento"));

						ResultSet validaEntidade = vrs.executeQuery();

						validaEntidade.next();

						int quantidade = validaEntidade.getInt("quantidade");

						if (quantidade == 0) {
							String sqlarsalvar = "insert into intdotacao(\r\n"
									+ "            entidade, eloexercicio, eloprogramatica, elofonterecurso, elodespesa, \r\n"
									+ "            elodesdobramento, elosubdesdobramento, elodescricao, ipmelemento, \r\n"
									+ "            ipmdescricao, ipmvinculo, ipmtipoacao)\r\n"
									+ "    VALUES (?, ?, ?, ?, ?, \r\n" + "            ?, ?, ?, ?, \r\n"
									+ "            ?, ?, ?)";
							try {
								PreparedStatement psid = connection.prepareStatement(sqlarsalvar);

								psid.setInt(1, cliente);
								psid.setInt(2, rsa.getInt("eloexercicio"));
								psid.setString(3, rsa.getString("eloprogramatica"));
								psid.setInt(4, rsa.getInt("elofonterecurso"));
								psid.setString(5, rsa.getString("elodespesa"));
								psid.setString(6, rsa.getString("elodesdobramento"));
								psid.setString(7, rsa.getString("elosubdesdobramento"));
								psid.setString(8, rsa.getString("elodescricao"));
								psid.setString(9, rsa.getString("ipmelemento"));
								psid.setString(10, rsa.getString("ipmdescricao"));
								psid.setString(11, rsa.getString("ipmvinculo"));
								psid.setString(12, rsa.getString("ipmtipoacao"));

								psid.execute();
								psid.close();

							} catch (SQLException e) {
								e.printStackTrace();
							}
						} else {
							System.out.println("J? possu? informa??es na Tabela para entidade-> " + cliente);

						}

					}

				} catch (SQLException e) {
					e.printStackTrace();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;

		} else {
			return false;
		}

	}
}