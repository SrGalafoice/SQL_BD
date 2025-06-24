package DAOImpl;

import DAO.VendaDAO;
import Exception.VendaException;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import Exception.VendaException;
import utils.DBConnection;

public class VendaDAOImpl implements VendaDAO {
    private DBConnection db = null;

    public VendaDAOImpl() throws VendaException {
        try {
            db = DBConnection.getInstance();
        } catch (Exception e) {
            throw new VendaException( e );
        }
    }
    @Override
    public void adicionarVenda(Venda v) throws VendaException {
        try {
            java.sql.Date data = java.sql.Date.valueOf(v.getData());
            Connection con = db.getConnection();
            for (Produto produto : v.getCarrinho().getProduto()){
                String sql = """
                INSERT INTO Venda(ID, data, quantidadeProduto, valorUnitarioSaida, ProdutoID, EmpresaCNPJ) VALUES (?, ?, ?, ?, ?, ?)
            """;

                PreparedStatement pstm = con.prepareStatement(sql);
                int ID = (int)(Math.random() * 1000) + 1;
                pstm.setInt(1, ID);
                pstm.setDate(2, data);
                pstm.setInt(3, produto.getQuantidade()); //lista de carrinho
                pstm.setDouble(4, produto.getValorUnitarioVenda()); //lista de carrinho
                pstm.setInt(5, produto.getID()); //lista de carrinho
                pstm.setString(6, v.getEmpresa().getCnpj());
                pstm.executeUpdate();
                System.out.println("UMa venda");
                sql = "INSERT INTO Produto_Venda(ProdutoID, VendaID) VALUES(?,?)";
                pstm = con.prepareStatement(sql);
                pstm.setInt(1, produto.getID());
                pstm.setInt(2, ID);
                pstm.executeUpdate();
                System.out.println("Mais uma associacao");
            }
            con.close();

        } catch (Exception e) {
            throw new VendaException( e );
        }
    }

    @Override
    public List<Venda> pesquisarVendas(LocalDate d) throws VendaException {
        List<Venda> lista = new ArrayList<>();
        java.sql.Date data = java.sql.Date.valueOf(d);
        ResultSet rs;
        try {
            Connection con = db.getConnection();
            String sql = "SELECT p.ID, p.nome, v.quantidadeProduto, v.valorUnitarioSaida, e.CNPJ, e.nomeEmpresa FROM Empresa e INNER JOIN Venda v ON e.CNPJ = v.EmpresaCNPJ INNER JOIN Produto_Venda pv ON v.ID = pv.VendaID INNER JOIN Produto p ON p.ID = pv.ProdutoID WHERE v.data = ? ";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setDate(1, data);
            rs = pstm.executeQuery();

            while(rs.next()){
                Venda v = new Venda();
                Produto p = new Produto();
                Empresa e = new Empresa();
                Carrinho c = new Carrinho();
                p.setID(rs.getInt("ID"));
                p.setNome(rs.getString("nome"));
                p.setQuantidade(rs.getInt("quantidadeProduto"));
                p.setValorUnitarioVenda(rs.getDouble("valorUnitarioSaida"));
                e.setCnpj(rs.getString("CNPJ"));
                e.setNomeEmpresa(rs.getString("nomeEmpresa"));
                c.setValorTotal((p.getValorUnitarioVenda() * p.getQuantidade()));
                v.setProduto(p);
                v.setEmpresa(e);
                v.setCarrinho(c);
                lista.add(v);
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }
}
