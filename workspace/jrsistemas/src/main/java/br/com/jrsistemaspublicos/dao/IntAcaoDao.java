package br.com.jrsistemaspublicos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.jrsistemaspublicos.data.ConexaoElotech;
import br.com.jrsistemaspublicos.model.Intacao;


public class IntAcaoDao {

	private static Connection connection;
	private static Connection connIntegracao;
	

	public IntAcaoDao(String dataBase) {
		if (dataBase == "fpterraroxa") {
		    this.connection = ConexaoElotech.getConnection(dataBase);
		} else if (dataBase == "cmarapongas") {
			this.connection = ConexaoElotech.getConnection(dataBase);
		} else if (dataBase == "cmguaira") {
			this.connection = ConexaoElotech.getConnection(dataBase);
		} else {

		}
	}

	public boolean getIntacao(Integer entidade, Integer exercicio, Integer cliente) {

		if (entidade != null && exercicio != null && cliente != null) {

			String sqlInsereTab = "\r\n" + "CREATE TABLE if not exists intacao\r\n" + "(\r\n"
					+ "  entidade numeric(10,0) NOT NULL,\r\n"
					+ "  eloprojetoatividade character varying(4) NOT NULL,\r\n"
					+ "  elodescricao character varying(250),\r\n" + "  ipmtipo character varying(2) NOT NULL,\r\n"
					+ "  CONSTRAINT fk_intacao PRIMARY KEY (entidade, eloprojetoatividade)\r\n" + ")\r\n" + "WITH (\r\n"
					+ "  OIDS=FALSE\r\n" + ");\r\n" + "ALTER TABLE public.intacao\r\n" + "  OWNER TO postgres;";

			try {
				PreparedStatement ent = connection.prepareStatement(sqlInsereTab);
				ent.execute();
				ent.close();

				String sqlIntacao = "select distinct entidade, \r\n"
						+ "       substring(programatica,15,4) eloprojetoatividade,  \r\n"
						+ "       case when substring(programatica,15,1) = '2' then 'Atividade'\r\n"
						+ "            when substring(programatica,15,1) = '1' then 'Projeto'\r\n"
						+ "       else '00' end elodescricao,\r\n"
						+ "       case when substring(programatica,15,1) = '2' then '02'\r\n"
						+ "            when substring(programatica,15,1) = '1' then '01'\r\n"
						+ "       else '00'end ipmtipo \r\n" + "   from siscop.despesa\r\n" + "  where entidade = ?\r\n"
						+ "    and exercicio = ?\r\n" + "    and movsn = 'S'";
				try {
					PreparedStatement psa = connection.prepareStatement(sqlIntacao);

					psa.setInt(1, entidade);
					psa.setInt(2, exercicio);

					ResultSet rsa = psa.executeQuery();

					while (rsa.next()) {
						Intacao intAcao = new Intacao();
						intAcao.setEntidade(cliente);
						intAcao.setEloprojetoatividade(rsa.getString("eloprojetoatividade"));
						intAcao.setElodescricao(rsa.getString("elodescricao"));
						intAcao.setIpmtipo(rsa.getString("ipmtipo"));

						String sqlValida = "select count(*) as quantidade from intacao \r\n"
								+ "   where entidade = ? \r\n" + "     and eloprojetoatividade = ?";

						PreparedStatement vrs = connection.prepareStatement(sqlValida);
						vrs.setInt(1, cliente);
						vrs.setString(2, rsa.getString("eloprojetoatividade"));

						ResultSet validaEntidade = vrs.executeQuery();

						validaEntidade.next();
						
						int quantidade = validaEntidade.getInt("quantidade");

						if (quantidade == 0) {
							String sqlarsalvar = "INSERT INTO intacao(entidade,eloprojetoatividade,elodescricao,ipmtipo) VALUES (?,?,?,?)";
							try {
								PreparedStatement psia = connection.prepareStatement(sqlarsalvar);

								psia.setInt(1, cliente);
								psia.setString(2, rsa.getString("eloprojetoatividade"));
								psia.setString(3, rsa.getString("elodescricao"));
								psia.setString(4, rsa.getString("ipmtipo"));

								psia.execute();
								System.out.println("Cadastro efetuado com sucesso!!");
								psia.close();
								

							} catch (SQLException e) {
								e.printStackTrace();

							}

						} else {
							System.out.println("Já possuí informações na Tabela para entidade-> "+cliente );

						}

					}

					return true;

				} catch (SQLException e) {
					e.printStackTrace();

				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			return false;

		}

		return true;
	}

}
