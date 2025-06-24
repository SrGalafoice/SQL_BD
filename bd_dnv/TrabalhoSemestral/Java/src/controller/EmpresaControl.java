package controller;

import DAO.EmpresaDAO;
import DAOImpl.EmpresaDAOImpl;
import Exception.EmpresaException;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Empresa;

import java.util.List;


public class EmpresaControl {
    private ObservableList<Empresa> lista =
            FXCollections.observableArrayList();

    private StringProperty cnpj = new SimpleStringProperty("");
    private StringProperty nome = new SimpleStringProperty("");
    private StringProperty rua = new SimpleStringProperty("");
    private StringProperty CEP = new SimpleStringProperty("");
    private IntegerProperty numero = new SimpleIntegerProperty(0);
    private StringProperty cidade = new SimpleStringProperty("");
    private StringProperty bairro = new SimpleStringProperty("");
    private StringProperty complemento = new SimpleStringProperty("");

    private EmpresaDAO eDAO = null;

    public EmpresaControl() throws EmpresaException {
        try {
            eDAO = new EmpresaDAOImpl();
        } catch(Exception e) {
            throw new EmpresaException(e);
        }

    }
    public void adicionarEmpresa(String[] email, String[] telefone){

        System.out.println("NOME: " + nome.get());
        System.out.println("CNPJ: " + cnpj.get());
        System.out.println("BAIRRO: " + bairro.get());
        System.out.println("NUMERO: " + numero.get());
        if (nome.get() == null || nome.get().isBlank()) {
            throw new IllegalArgumentException("Erro: nome da empresa não foi preenchido.");
        }
            Empresa e = new Empresa();
        e.setBairro(bairro.get());
        e.setCidade(cidade.get());
        e.setComplemento(complemento.get());
        e.setCEP(CEP.get());
        e.setLogradouro(rua.get());
        e.setNumero(numero.get());
        e.setNomeEmpresa(nome.get());
        e.setCnpj(cnpj.get());
        e.setEmail(email);
        e.setTelefone(telefone);

        eDAO.adicionar(e);
    }
    public void pesquisarEmpresas(){
        lista.clear();
        lista.addAll(eDAO.pesquisarEmpresas());
    }

    public StringProperty cnpjProperty(){
        return cnpj;
    }
    public StringProperty nomeProperty(){
        return nome;
    }
    public StringProperty ruaProperty(){
        return rua;
    }
    public StringProperty CEPProperty(){
        return CEP;
    }
    public IntegerProperty numeroProperty(){
        return numero;
    }
    public StringProperty cidadeProperty(){
        return cidade;
    }
    public StringProperty bairroProperty(){
        return bairro;
    }
    public StringProperty complementoProperty(){
        return complemento;
    }

    public ObservableList<Empresa> getLista(){
        return lista;
    }


}
