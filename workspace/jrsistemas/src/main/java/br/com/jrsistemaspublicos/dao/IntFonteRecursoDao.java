package br.com.jrsistemaspublicos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.jrsistemaspublicos.data.ConexaoElotech;
import br.com.jrsistemaspublicos.model.IntFonteRecurso;


public class IntFonteRecursoDao {
	private static Connection connection;

	public IntFonteRecursoDao(String dataBase) {
		if (dataBase == "fpterraroxa") {
			this.connection = ConexaoElotech.getConnection(dataBase);
		} else if(dataBase == "cmguaira") {			
			this.connection = ConexaoElotech.getConnection(dataBase);
		} else if(dataBase == "cmarapongas"){			
			this.connection = ConexaoElotech.getConnection(dataBase);
		}

	}

	public boolean getFonteRecurso(Integer cliente) {

		if (cliente != null) {
			String sqlFonteRecurso = "select "+cliente+" entidade,\r\n" + "       f.fonterecurso elofonterecurso,\r\n"
					+ "       f.descricao elodescricao,\r\n"
					+ "       cast(f.codigotce as numeric(8,0)) ipmvinculo,\r\n"
					+ "       f.descricao ipmdescricao \r\n" + "   from siscop.fonterecurso f\r\n"
					+ "    where not exists \r\n" + "      (select * from intfonterecurso fi\r\n"
					+ "         where f.fonterecurso = fi.elofonterecurso\r\n" + "        and fi.entidade = ?)";
			try {
				PreparedStatement psfr = connection.prepareStatement(sqlFonteRecurso);

				psfr.setInt(1, cliente);

				ResultSet rsfr = psfr.executeQuery();
				while (rsfr.next()) {
					IntFonteRecurso fonteRecurso = new IntFonteRecurso();
					fonteRecurso.setEntidade(cliente);
					fonteRecurso.setElofonterecurso(rsfr.getInt("elofonterecurso"));
					fonteRecurso.setElodescricao(rsfr.getString("elodescricao"));
					fonteRecurso.setIpmvinculo(rsfr.getInt("ipmvinculo"));
					fonteRecurso.setIpmdescricao(rsfr.getString("ipmdescricao"));

					String sqlfrsalvar = "INSERT INTO intfonterecurso(entidade,elofonterecurso,elodescricao,ipmvinculo,ipmdescricao) VALUES (?,?,?,?,?)";
					try {
						PreparedStatement psifr = connection.prepareStatement(sqlfrsalvar);

						psifr.setInt(1, cliente);
						psifr.setInt(2, rsfr.getInt("elofonterecurso"));
						psifr.setString(3, rsfr.getString("elodescricao"));
						psifr.setInt(4, rsfr.getInt("ipmvinculo"));
						psifr.setString(5, rsfr.getString("ipmdescricao"));
						psifr.execute();
						psifr.close();

					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return true;
	}

}
