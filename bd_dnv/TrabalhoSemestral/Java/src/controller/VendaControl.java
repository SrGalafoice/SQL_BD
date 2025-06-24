package controller;

import DAO.VendaDAO;
import DAOImpl.VendaDAOImpl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Venda;
import Exception.VendaException;

import java.time.LocalDate;

public class VendaControl {
    private ObjectProperty<LocalDate> data = new SimpleObjectProperty<>(LocalDate.now());
    private ObservableList<Venda> lista = FXCollections.observableArrayList();
    private VendaDAO vDAO = null;
    public VendaControl(){
        try{
            vDAO = new VendaDAOImpl();
        }catch (VendaException e){
            throw new RuntimeException(e);
        }
    }
    public void adicionarVenda(Venda v){
        vDAO.adicionarVenda(v);
    }
    public void pesquisarVendas(){
        lista.clear();
        lista.addAll(vDAO.pesquisarVendas(data.get()));
    }
    public ObservableList<Venda> getLista(){
        return lista;
    }
    public ObjectProperty<LocalDate> dataProperty(){
        return data;
    }
}
