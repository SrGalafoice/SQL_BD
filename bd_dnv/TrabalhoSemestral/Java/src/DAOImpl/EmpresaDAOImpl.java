package DAOImpl;

import DAO.EmpresaDAO;
import Exception.EmpresaException;
import model.Empresa;
import model.Entrada;
import model.Produto;
import utils.DBConnection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class EmpresaDAOImpl implements EmpresaDAO {
        private DBConnection db = null;

    public EmpresaDAOImpl() throws EmpresaException {
        try {
            db = DBConnection.getInstance();
        } catch (Exception e) {
            throw new EmpresaException( e );
        }
    }
    @Override
    public void adicionar(Empresa e) throws EmpresaException {
        System.out.println(e.getNomeEmpresa());
        try {
            Connection con = db.getConnection();
            String sql = """
                INSERT INTO Empresa(CNPJ, nomeEmpresa, CEP, Logradouro, NumeroLogradouro, ComplementoLogradouro, CidadeLogradouro, BairroLogradouro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, e.getCnpj());
            pstm.setString(2, e.getNomeEmpresa());
            pstm.setString(3, e.getCEP());
            pstm.setString(4, e.getLogradouro());
            pstm.setInt(5, e.getNumero());
            pstm.setString(6, e.getComplemento());
            pstm.setString(7, e.getCidade());
            pstm.setString(8, e.getBairro());
            pstm.executeUpdate();

            System.out.println("Empresa adicionada");

            String sqlEmail = """
                INSERT INTO Email(Endereco_email, EmpresaCNPJ) VALUES (?, ?)
            """;
            pstm = con.prepareStatement(sqlEmail);
            for (String email : e.getEmail()) {
                if (email != null && !email.trim().isEmpty()) {
                    pstm.setString(1, email.trim());
                    pstm.setString(2, e.getCnpj());
                    pstm.executeUpdate();
                }
            }

            String sqlTel = """
                INSERT INTO telefone(Numero_telefone, EmpresaCNPJ) VALUES (?, ?)
            """;
            pstm = con.prepareStatement(sqlTel);
            for (String tel : e.getTelefone()) {
                if (tel != null && !tel.trim().isEmpty()) {
                    pstm.setString(1, tel.trim());
                    pstm.setString(2, e.getCnpj());
                    pstm.executeUpdate();
                }
            }
            con.close();

        } catch (Exception ex) {
            throw new EmpresaException( ex );
        }
    }

    @Override
    public List<Empresa> pesquisarEmpresas() throws EmpresaException {
        List<Empresa> lista = new ArrayList<>();
        Map<String, Empresa> empresasMap = new HashMap<>();
        ResultSet rs;
        try {
            Connection con = db.getConnection();
            String sql = "SELECT * FROM Empresa e INNER JOIN Email ON e.CNPJ = Email.EmpresaCNPJ INNER JOIN Telefone t ON t.EmpresaCNPJ = e.CNPJ WHERE nomeEmpresa NOT LIKE '%CPF%'";
            PreparedStatement pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                String cnpj = rs.getString("CNPJ");
                Empresa e = empresasMap.get(cnpj);
                if (e == null) {
                    e = new Empresa();
                    e.setNomeEmpresa(rs.getString("nomeEmpresa"));
                    e.setCnpj(rs.getString("CNPJ"));
                    e.setCEP(rs.getString("CEP"));
                    e.setBairro(rs.getString("BairroLogradouro"));
                    e.setLogradouro(rs.getString("Logradouro"));
                    e.setNumero(rs.getInt("NumeroLogradouro"));
                    e.setComplemento(rs.getString("ComplementoLogradouro"));
                    e.setCidade(rs.getString("CidadeLogradouro"));
                    e.setBairro(rs.getString("BairroLogradouro"));

                }
                String email = rs.getString("Endereco_email");

                if (email != null) {
                    String[] emails = e.getEmail();
                    for (int i = 0; i < emails.length; i++) {
                        if (emails[i] == null) {
                            emails[i] = email;
                            break;
                        }
                    }


                    e.setEmail(emails);
                }
                String telefone = rs.getString("Numero_telefone");
                if (telefone != null) {
                    String[] telefones = e.getTelefone();
                    for (int i = 0; i < telefones.length; i++) {
                        if (telefones[i] == null) {
                            telefones[i] = telefone;
                            break;
                        }
                    }
                    e.setTelefone(telefones);
                }

                empresasMap.put(cnpj, e);

            }
            lista = new ArrayList<>(empresasMap.values());
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    @Override
    public Empresa pesquisarPorCNPJ(String CNPJ) throws EmpresaException {
        Empresa e = new Empresa();
        ResultSet rs;
        try {
            Connection con = db.getConnection();
            String sql = "SELECT * FROM Empresa e";
            PreparedStatement pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery(sql);
            if(rs.next()) {
                e.setNomeEmpresa(rs.getString("nomeEmpresa"));
                e.setCnpj(rs.getString("CNPJ"));
                e.setCEP(rs.getString("CEP"));
                e.setLogradouro(rs.getString("Logradouro"));
                e.setNumero(rs.getInt("NumeroLogradouro"));
                e.setComplemento(rs.getString("ComplementoLogradouro"));
                e.setCidade(rs.getString("CidadeLogradouro"));
                e.setBairro(rs.getString("BairroLogradouro"));
            }
            sql = "SELECT Email.Endereco_email FROM Email INNER JOIN Empresa ON Empresa.CNPJ = Email.EmpresaCNPJ WHERE Empresa.CNPJ LIKE ?";
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery(sql);
            int i = 0;
            String[] v = new String[3];
            while (rs.next() && i < e.getEmail().length){
                v[i] = rs.getString("Endereco_email");
            }
            e.setEmail(v);

            sql = "SELECT t.Numero_telefone FROM Telefone t INNER JOIN Empresa ON Empresa.CNPJ = t.EmpresaCNPJ WHERE Empresa.CNPJ LIKE ?";
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery(sql);
            i = 0;
            v = new String[3];
            while (rs.next() && i < e.getTelefone().length){
                v[i] = rs.getString("Numero_telefone");
            }
            e.setTelefone(v);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return e;
    }
}
