package DAO;

import Exception.ProdutoException;
import model.Produto;

import java.sql.SQLException;
import java.util.List;

public interface ProdutoDAO {
    public void adicionarProduto(Produto p) throws ProdutoException;
    public List<Produto> pesquisarProdutos(String n) throws ProdutoException, SQLException;
    public Produto pesquisarProduto(int i) throws ProdutoException, SQLException;
    public void atualizarProduto(Produto p) throws ProdutoException, SQLException;
}
