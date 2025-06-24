package controller;

import DAO.ProdutoDAO;
import DAOImpl.ProdutoDAOImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Produto;
import Exception.ProdutoException;

import java.sql.SQLException;
import java.util.Objects;

public class ProdutoControl {
    private ProdutoDAO pDAO = null;
    private ObservableList<Produto> lista =
            FXCollections.observableArrayList();
    private StringProperty ID = new SimpleStringProperty("");

    public ProdutoControl() throws ProdutoException {
        try {
            pDAO = new ProdutoDAOImpl();
        } catch (ProdutoException e) {
            System.err.println("Erro na DAO " + e.getMessage());
        }
    }

    public void pesquisarProdutos() throws ProdutoException, SQLException {
        lista.clear();
        lista.addAll(pDAO.pesquisarProdutos(ID.get()));

    }
public Produto pesquisarProduto(String id) throws ProdutoException, SQLException {
    return pDAO.pesquisarProduto(Integer.parseInt(id));
}

public void atualizarProduto(Produto p) throws SQLException, ProdutoException {
        pDAO.atualizarProduto(p);
}
    public StringProperty IDProperty(){
        return ID;
    }



    public ObservableList<Produto> getLista() {
        return lista;
    }
}
