package DAOImpl;

import DAO.PrateleiraDAO;
import model.Prateleira;
import model.Produto;
import Exception.PrateleiraException;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrateleiraDAOImpl implements PrateleiraDAO {

    private DBConnection db = null;

    public PrateleiraDAOImpl() throws PrateleiraException {
        try {
            db = DBConnection.getInstance();
        } catch (Exception e) {
            throw new PrateleiraException( e );
        }
    }

    @Override
    public List<Produto> consultarPrateleira(Prateleira p) throws PrateleiraException {
        List<Produto> lista = new ArrayList<Produto>();
        try {
            Connection con = db.getConnection();
            String sql = """
                        SELECT p.ID, p.nome, pp.quantidade FROM Produto_Prateleira pp INNER JOIN Produto p 
                        ON p.ID = pp.ProdutoID INNER JOIN Prateleira
                        ON Prateleira.ID = pp.PrateleiraID 
                        WHERE PrateleiraID LIKE ?
                    """;
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setInt(1, p.getId());
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Produto pp = new Produto();
                pp.setID(rs.getInt("ID"));
                pp.setQuantidade(rs.getInt("quantidade"));
                pp.setNome(rs.getString("nome"));
                lista.add(pp);
            }
            con.close();
        } catch (Exception e) {
            System.err.println("Erro na DAO" + e.getMessage());
        }
        return lista;
    }
}