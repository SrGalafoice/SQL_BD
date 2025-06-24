package DAOImpl;

import DAO.ProdutoDAO;
import model.Prateleira;
import model.Produto;
import Exception.ProdutoException;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class ProdutoDAOImpl implements ProdutoDAO {
    private DBConnection db = null;

    public ProdutoDAOImpl() throws ProdutoException {
        try {
            db = DBConnection.getInstance();
        } catch (Exception e) {
            throw new ProdutoException( e );
        }
    }


    @Override
    public void adicionarProduto(Produto p) throws ProdutoException {
        String sql = """
                INSERT INTO Produto(ID, valorUnitario, nome) VALUES (?, ?, ?)
            """;
        String asc = """
                INSERT INTO Produto_Prateleira(quantidade, ProdutoID, PrateleiraID) VALUES (?, ?, ?)
            """;

        try {
            Connection con = db.getConnection();
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setDouble(1, p.getID());
            pstm.setDouble(2, p.getValorUnitarioVenda());
            pstm.setString(3, p.getNome());
            pstm.executeUpdate();
            System.out.println("produto...");



            PreparedStatement pstm2 = con.prepareStatement(asc);
            pstm2.setInt(1, p.getQuantidade());
            pstm2.setInt(2, p.getID());
            pstm2.setInt(3, p.getPrateleira().getId());
            pstm2.executeUpdate();
            System.out.println("associativa...");
            con.close();

            System.out.println("PASSOU PELA DAO DE PRODUTO"); //VE ISSO AQUI TBM
        } catch (Exception e) {
            throw new ProdutoException( e );
        }
    }

    @Override
    public List<Produto> pesquisarProdutos(String n) throws SQLException {
        String sql = "";
        ResultSet rs;
        List<Produto> lista = new ArrayList<>();
        try {
            Connection con = db.getConnection();
            if(n == null || n.isBlank() ){
                sql = "SELECT p.nome FROM Produto_Prateleira pp INNER JOIN Produto p ON pp.ProdutoID = p.ID INNER JOIN Prateleira pra ON pp.PrateleiraID = pra.ID";
                PreparedStatement pstm = con.prepareStatement(sql);
                rs = pstm.executeQuery();

                while (rs.next()){
                    Produto p = new Produto();
                    p.setNome(rs.getString("nome"));
                    lista.add(p);
                }
                return lista;
            }else if (n.matches(".*\\d.*")){
                sql = "SELECT pp.quantidade, pp.PrateleiraID FROM Produto_Prateleira pp INNER JOIN Produto p ON pp.ProdutoID = p.ID INNER JOIN Prateleira pra ON pp.PrateleiraID = pra.ID WHERE p.ID = ? ";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setInt(1, parseInt(n));
                rs = pstm.executeQuery();
        } else {
                sql = "SELECT pp.quantidade, pp.PrateleiraID FROM Produto_Prateleira pp INNER JOIN Produto p ON pp.ProdutoID = p.ID INNER JOIN Prateleira pra ON pp.PrateleiraID = pra.ID WHERE p.nome LIKE ? ";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, n);
                rs = pstm.executeQuery();
            }

            while (rs.next()) {
                Produto p = new Produto();
                Prateleira pra = new Prateleira();
                pra.setId(rs.getInt("PrateleiraID"));
                p.setQuantidade(rs.getInt("quantidade"));
                p.setPrateleira(pra);
                lista.add(p);
            }
            con.close();
    } catch ( SQLException e) {
        System.err.println("Erro ao procurar produto " + e.getMessage());
        }
        if (lista.isEmpty()){
            view.estoque.TelaConsultarProduto.alert("O produto não existe em nenhuma prateleira. Confira se o ID está correto.");
        }
        return lista;
    }

    @Override
    public Produto pesquisarProduto(int i) throws ProdutoException, SQLException {
        ResultSet rs;
        Connection con = db.getConnection();
        String sql = "SELECT p.nome, pp.quantidade, p.valorUnitario FROM Produto_Prateleira pp INNER JOIN Produto p ON pp.ProdutoID = p.ID INNER JOIN Prateleira pra ON pp.PrateleiraID = pra.ID WHERE p.ID = ? ";
        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setInt(1, i);
        rs = pstm.executeQuery();
        Produto p = new Produto();
        if(rs.next()) {
            p.setNome(rs.getString("nome"));
            p.setQuantidade(rs.getInt("quantidade"));
            p.setValorUnitarioVenda(rs.getDouble("valorUnitario"));
        }
        con.close();
        return p;
    }

    @Override
    public void atualizarProduto(Produto p) throws ProdutoException, SQLException {
        Produto estoque = new Produto();
        estoque = pesquisarProduto(p.getID());
        estoque.setID(p.getID());
        System.out.println("ID ESTOQUE: " + estoque.getID() + "Produto ID: " + p.getID() + "Qauntidade retirada: " + (estoque.getQuantidade() - p.getQuantidade()));
        Connection con = db.getConnection();
        if (p.getQuantidade() < estoque.getQuantidade()){
            String sql = "UPDATE Produto_Prateleira SET quantidade = ? WHERE ProdutoID = ? ";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setInt(1, (estoque.getQuantidade() - p.getQuantidade()));
            pstm.setInt(2, p.getID());
            pstm.executeUpdate();
        } else throw new IllegalArgumentException("Quantidade de produto maior que estoque");
        con.close();
    }


}



