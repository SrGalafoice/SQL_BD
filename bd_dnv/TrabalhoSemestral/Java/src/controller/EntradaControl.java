package controller;

import DAO.ProdutoDAO;
import DAOImpl.ProdutoDAOImpl;
import Exception.ProdutoException;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Entrada;
import model.Prateleira;
import model.Produto;

import Exception.*;

import DAO.EntradaDAO;

import DAOImpl.*;

import java.time.LocalDate;




public class EntradaControl {
    private DoubleProperty valorUnitarioEntrada = new SimpleDoubleProperty(0);
    private IntegerProperty quantidade = new SimpleIntegerProperty(0);

    private StringProperty nome = new SimpleStringProperty("");
    private DoubleProperty valorUnitarioVenda = new SimpleDoubleProperty(0);
    private IntegerProperty prateleiraID = new SimpleIntegerProperty(0);
    private IntegerProperty pID = new SimpleIntegerProperty(0);

    private ObjectProperty<LocalDate> data = new SimpleObjectProperty(LocalDate.now());
    private ObservableList<Entrada> lista = FXCollections.observableArrayList();
    private EntradaDAO eDAO = null;
    private ProdutoDAO pDAO = null;

    public EntradaControl() throws EntradaException, ProdutoException{
        try {
            eDAO = new EntradaDAOImpl();
            pDAO = new ProdutoDAOImpl();
        } catch(EntradaException | ProdutoException e){
            System.err.println("Erro ao iniciar a DAO" + e.getMessage());
        }
    }

    public void adicionarEntrada(int quant) throws EntradaException, ProdutoException {
        Produto p = new Produto();
        Prateleira prateleira = new Prateleira();
        prateleira.setId(prateleiraID.get());

        p.setNome(nome.get());
        p.setQuantidade(quantidade.get());
        p.setValorUnitarioVenda(valorUnitarioVenda.get());
        p.setID(pID.get());
        p.setPrateleira(prateleira);
        System.out.println(quant);
        if (quantidade.get() >= (100 - quant)) {
            throw new IllegalArgumentException("Quantidade deve ser menor que os espaços vazios.");
        } else {

        System.out.println(p.getPrateleira().getId());
        Entrada e = new Entrada();
        e.setQuantidadeEntrada(quantidade.get());
        e.setData(LocalDate.now());
        e.setProduto(p);
        e.setValorUnitarioEntrada(valorUnitarioEntrada.get());
        e.setValorTotal(valorUnitarioEntrada.get() * quantidade.get());
        pDAO.adicionarProduto(p);
        eDAO.adicionar(e);

        System.out.println("Passou pela controler suave" + "\n" + e + "\n" + p); //OLHA ESSA PORRA KRL
        }
    }

    public void pesquisarTodasEntradas() throws EntradaException {
        lista.clear();
        lista.addAll(eDAO.pesquisarTodasEntradas(data.get()));

    }

    public DoubleProperty valorUnitarioEntradaProperty() {
        return valorUnitarioEntrada;
    }

    public IntegerProperty quantidadeProperty() {
        return quantidade;
    }
    public StringProperty nomeProperty() {
        return nome;
    }
    public DoubleProperty valorUnitarioVendaProperty() {
        return valorUnitarioVenda;
    }
    public IntegerProperty prateleiraIDProperty() {
        return prateleiraID;
    }
    public IntegerProperty pIDProperty() {
        return pID;
    }

    public ObjectProperty<LocalDate> dataProperty() {
        return data;
    }

    public ObservableList<Entrada> getLista(){
        return lista;
    }
}