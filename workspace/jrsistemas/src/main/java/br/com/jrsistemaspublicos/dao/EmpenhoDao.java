package br.com.jrsistemaspublicos.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.jrsistemaspublicos.data.ConexaoElotech;


public class EmpenhoDao {

	private static Connection connection;

	public EmpenhoDao(String dataBase) {
		super();
		dataBase = ("cmarapongas");
		if (dataBase == "fpterraroxa") {
			this.connection = ConexaoElotech.getConnection(dataBase);
		} else if (dataBase == "cmguaira") {
			this.connection = ConexaoElotech.getConnection(dataBase);
		} else if (dataBase == "cmarapongas") {

			this.connection = ConexaoElotech.getConnection(dataBase);
		}

	}

	public static void getEmpenhos(Integer entidade, Integer exercicio, Integer dMes, Integer cliente)
			throws FileNotFoundException {

		if (entidade != null && exercicio != null && dMes != null) {

			if (entidade == 326) {
				cliente = 326;
				entidade = 1;// Fundo de Terra Roxa - Previsterra
			} else if (entidade == 12634) {
				cliente = 12634;// Câmara de Guaíra
				entidade = 1;
			} else if (entidade == 8787) {
				cliente = 8787;// Câmara de Arapongas
				entidade = 1;
			}

			Integer mesLoa = dMes;
			Integer anoLoa = exercicio;

			String ano = null;
			String mes = null;
			String meses = null;

			if (dMes == 1) {
				mes = "01";
				meses = "Janeiro";
			} else if (dMes == 2) {
				mes = "02";
				meses = "Fevereiro";
			} else if (dMes == 3) {
				mes = "03";
				meses = "Março";
			} else if (dMes == 4) {
				mes = "04";
				meses = "Abril";
			} else if (dMes == 5) {
				mes = "05";
				meses = "Maio";
			} else if (mesLoa == 6) {
				mes = "06";
				meses = "Junho";
			} else if (mesLoa == 7) {
				mes = "07";
				meses = "Julho";
			} else if (mesLoa == 8) {
				mes = "08";
				meses = "Agosto";
			} else if (mesLoa == 9) {
				mes = "09";
				meses = "Setembro";
			} else if (mesLoa == 10) {
				mes = "10";
				meses = "Outubro";
			} else if (mesLoa == 11) {
				mes = "11";
				meses = "Novembro";
			} else if (mesLoa == 12) {
				mes = "12";
				meses = "Dezembro";
			}
			String merda = mes;

			mes = merda;

			File fileRaiz = new File("c:\\integracao");
			File fileAno = new File("c:\\integracao\\" + anoLoa);
			File fileMes = new File("c:\\integracao\\" + anoLoa + "\\" + mes);

			File conFileRaiz[] = fileRaiz.listFiles();

			if (conFileRaiz == null) {
				File cFile = new File("c:\\integracao");
				cFile.mkdir();
			}
			File conFileAno[] = fileAno.listFiles();
			if (conFileAno == null) {
				File cFile = new File("c:\\integracao\\" + anoLoa);
				cFile.mkdir();
			}
			File conFileMes[] = fileMes.listFiles();
			if (conFileMes == null) {
				File cFile = new File("c:\\integracao\\" + anoLoa + "\\" + mes);
				cFile.mkdir();
			}
			String sql = "select lpad('" + cliente + "',6,'0') clicodigo,  \r\n"
					+ "				       exercicio loaano,  \r\n"
					+ "				       lpad(cast(empenho as varchar(8)),8,'0') empnro, \r\n"
					+ "				       lpad('0',3,'0')  empsub,  \r\n"
					+ "				       tipo empespecie,  \r\n"
					+ "				       lpad('0',4,'0') empanoinscresto, \r\n"
					+ "				       lpad('0',4,'0') empanobaixaresto,  \r\n"
					+ "				       cast(data as varchar(10)) empdataemissao, \r\n"
					+ "				       cast(data as varchar(10)) empdatavencimento,  \r\n"
					+ "				       lpad(cast(cast(valor as numeric(17,2))as varchar(17)),17,'0') empvalor, \r\n"
					+ "				       substring(regexp_replace(historiconad, E'[\n\r]+', ' - ', 'g' ),1,500) emphistorico ,  \r\n"
					+ "				       rpad((select case when cnpj = '58460160904000' then '07890935000130' else cnpj end cnpj from siscop.fornecedor where fornecedor = siscop.empenho.fornecedor),14,' ') unicpfcnpj, \r\n"
					+ "				       rpad((select nome from siscop.fornecedor where fornecedor = siscop.empenho.fornecedor),200,' ') uninomerazao, \r\n"
					+ "				       2 plntipoplano, \r\n"
					+ "				       rpad((select 3||d.ipmelemento from intdotacao d\r\n"
					+ "					      where siscop.empenho.exercicio = d.eloexercicio \r\n"
					+ "					        and siscop.empenho.programatica = d.eloprogramatica\r\n"
					+ "					        and siscop.empenho.fonterecurso = cast(d.elofonterecurso as double precision) \r\n"
					+ "					        and siscop.empenho.despesa = d.elodespesa \r\n"
					+ "					        and siscop.empenho.desdobradesp = d.elodesdobramento\r\n"
					+ "					        and siscop.empenho.subdesdobramento = d.elosubdesdobramento),20,'0')  plncodigo,  \r\n"
					+ "				       lpad(substring(programatica,1,2),2,'0') orgcodigo, \r\n"
					+ "				       substring(programatica,3,3) undcodigo, \r\n"
					+ "				       lpad(substring(programatica,6,2),4,'0') tfccodigo, \r\n"
					+ "				       lpad(substring(programatica,8,3),4,'0') tsfcodigo, \r\n"
					+ "				       substring(programatica,11,4) pgrcodigo,   \r\n"
					+ "				       lpad((select d.ipmtipoacao from intdotacao d\r\n"
					+ "					      where siscop.empenho.exercicio = cast(d.eloexercicio as double precision) \r\n"
					+ "					        and siscop.empenho.programatica = d.eloprogramatica\r\n"
					+ "					        and siscop.empenho.fonterecurso = cast(d.elofonterecurso as double precision)\r\n"
					+ "					        and siscop.empenho.despesa = d.elodespesa \r\n"
					+ "					        and siscop.empenho.desdobradesp = d.elodesdobramento\r\n"
					+ "					        and siscop.empenho.subdesdobramento = d.elosubdesdobramento),2,'0') acotipo, \r\n"
					+ "				       substring(programatica,15,4)acocodigo, \r\n"
					+ "				       lpad((select distinct cast(ipmvinculo as varchar(8)) from intfonterecurso ifr where ifr.elofonterecurso =  siscop.empenho.fonterecurso ),8,'0') vincodigo, \r\n"
					+ "				       rpad('3'||despesa,20,'0') plncodigodotacao,  \r\n"
					+ "				       lpad(cast(idlancamento as varchar(10)),10,'0') ctlsequencia,despesa||desdobradesp||subdesdobramento despesa \r\n"
					+ "				   from siscop.empenho  \r\n" + "				     where entidade = ?  \r\n"
					+ "				    and exercicio = ? \r\n"
					+ "					and cast(substring(cast(data as varchar(10)),6,2)as numeric(10,0)) = ? \r\n"
					+ "					 order by empenho";

			try {
				PreparedStatement ps = connection.prepareStatement(sql);

				ps.setInt(1, entidade);
				ps.setInt(2, exercicio);
				ps.setInt(3, dMes);

				ResultSet rs = ps.executeQuery();

				File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\Empenho.txt");

				PrintWriter pw = new PrintWriter(file);

				while (rs.next()) {

					String fimLinha = "\r\n";

					String cnpj = rs.getString("unicpfcnpj");
					String elemento = rs.getString("plncodigo");
					pw.printf(
							"%6s%4s%8s%3s%1s%4s%4s%10s%10s%17s%-500s%-14s%-200s%1s%20s%2s%3s%4s%4s%4s%2s%4s%8s%-20s%10s%4s",
							rs.getString("clicodigo"), rs.getString("loaano"), rs.getString("empnro"),
							rs.getString("empsub"), rs.getString("empespecie"), rs.getString("empanoinscresto"),
							rs.getString("empanobaixaresto"), rs.getString("empdataemissao"),
							rs.getString("empdatavencimento"), rs.getString("empvalor"), rs.getString("emphistorico"),
							cnpj, rs.getString("uninomerazao"), rs.getString("plntipoplano"), elemento,
							rs.getString("orgcodigo"), rs.getString("undcodigo"), rs.getString("tfccodigo"),
							rs.getString("tsfcodigo"), rs.getString("pgrcodigo"), rs.getString("acotipo"),
							rs.getString("acocodigo"), rs.getString("vincodigo"), rs.getString("plncodigodotacao"),
							rs.getString("ctlsequencia"), fimLinha);
				}
				pw.flush();
				pw.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
			// Fim Empenho

		}
	}

}
