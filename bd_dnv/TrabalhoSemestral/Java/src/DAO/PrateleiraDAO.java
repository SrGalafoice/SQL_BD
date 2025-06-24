package DAO;

import model.Prateleira;
import Exception.PrateleiraException;
import model.Produto;

import java.sql.SQLException;
import java.util.List;

public interface PrateleiraDAO {
    public List<Produto> consultarPrateleira(Prateleira p) throws PrateleiraException, SQLException;
}
