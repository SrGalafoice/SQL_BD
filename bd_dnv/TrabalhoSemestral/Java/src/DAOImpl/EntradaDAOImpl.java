package DAOImpl;

import DAO.EntradaDAO;
import Exception.EntradaException;
import model.Entrada;
import model.Produto;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EntradaDAOImpl implements EntradaDAO {
    private DBConnection db = null;

    public EntradaDAOImpl() throws EntradaException {
        try {
            db = DBConnection.getInstance();
        } catch (Exception e) {
            throw new EntradaException( e );
        }
    }

    @Override
    public void adicionar(Entrada en) throws EntradaException {

        try {
            java.sql.Date data = java.sql.Date.valueOf(en.getData());
            Connection con = db.getConnection();
            String sql = """
                INSERT INTO Entrada(ID, valorTotal, data, quantidade, valorUnitarioEntrada) VALUES (?, ?, ?, ?, ?)
            """;
            PreparedStatement pstm = con.prepareStatement(sql);
            int ID = (int)(Math.random() * 1000) + 1;
            pstm.setDouble(1, ID);
            pstm.setDouble(2, en.getValorTotal());
            pstm.setDate(3, data);
            pstm.setInt(4, en.getQuantidadeEntrada());
            pstm.setDouble(5, en.getValorUnitarioEntrada());
            pstm.executeUpdate();

            sql = "INSERT INTO Produto_entrada(ProdutoID, entradaID) VALUES(?,?)";
            pstm = con.prepareStatement(sql);
            pstm.setInt(1, en.getProduto().getID());
            pstm.setInt(2, ID);
            pstm.executeUpdate();

            con.close();

            System.out.println("ENTRADA DAO PASSOU");
        } catch (Exception e) {
            throw new EntradaException( e );
        }
    }

    @Override
    public List<Entrada> pesquisarTodasEntradas(LocalDate d) throws EntradaException {
        List<Entrada> lista = new ArrayList<>();
        java.sql.Date data = java.sql.Date.valueOf(d);
        ResultSet rs;
        try {
            Connection con = db.getConnection();
            String sql = "SELECT p.ID, p.nome, e.valorUnitarioEntrada, e.quantidade FROM Entrada e INNER JOIN Produto_Entrada pe ON pe.EntradaID = e.ID INNER JOIN Produto p ON p.ID = pe.ProdutoID WHERE e.data = ? ";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setDate(1, data);
            rs = pstm.executeQuery();

            while(rs.next()){
                Entrada e = new Entrada();
                Produto p = new Produto();
                p.setID(rs.getInt("ID"));
                p.setNome(rs.getString("nome"));
                e.setQuantidadeEntrada(rs.getInt("quantidade"));
                e.setValorUnitarioEntrada(rs.getDouble("valorUnitarioEntrada"));
                e.setProduto(p);
                lista.add(e);
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

}





