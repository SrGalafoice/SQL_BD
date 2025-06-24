package DAO;

import model.Produto;
import model.Venda;
import Exception.VendaException;

import java.time.LocalDate;
import java.util.List;

public interface VendaDAO {
    public void adicionarVenda(Venda v) throws VendaException;
    public List<Venda> pesquisarVendas(LocalDate d) throws VendaException;
}
