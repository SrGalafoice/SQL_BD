package DAO;

import Exception.EntradaException;
import model.Entrada;

import java.time.LocalDate;
import java.util.List;

public interface EntradaDAO  {
    public void adicionar(Entrada e) throws EntradaException;
    public List<Entrada> pesquisarTodasEntradas(LocalDate d) throws EntradaException;
}
