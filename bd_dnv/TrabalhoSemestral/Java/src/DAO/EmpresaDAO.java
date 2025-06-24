package DAO;

import model.Empresa;
import Exception.EmpresaException;

import java.util.List;

public interface EmpresaDAO {
    public void adicionar(Empresa e) throws EmpresaException;
    public List<Empresa> pesquisarEmpresas() throws EmpresaException;
    public Empresa pesquisarPorCNPJ(String n) throws EmpresaException;
}
