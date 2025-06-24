package controller;


import DAO.PrateleiraDAO;
import Exception.PrateleiraException;

import DAOImpl.PrateleiraDAOImpl;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Prateleira;
import model.Produto;

import java.sql.SQLException;
import java.util.List;


public class PrateleiraControl {
    private ObservableList<Produto> lista =
            FXCollections.observableArrayList();
    private PrateleiraDAO pDAO = null;
    private IntegerProperty ID = new SimpleIntegerProperty(0);

    public PrateleiraControl() throws PrateleiraException {
        try {
            pDAO = new PrateleiraDAOImpl();
        } catch(PrateleiraException e){
            System.err.println("Erro ao iniciar a DAO" + e.getMessage());
        }

    }
    public List<Produto> consultarPrateleira() throws SQLException {
        Prateleira p = new Prateleira();
        p.setId(ID.get());
        System.out.println(ID.get());
        lista.clear();
       lista.addAll(pDAO.consultarPrateleira(p));
       if (lista.isEmpty()){
           throw new IllegalArgumentException("A prateleira está vazia");
       } else{
           return lista;
       }
    }

    public IntegerProperty IDProperty(){
        return ID;
    }

    public ObservableList<Produto> getLista() {
        return lista;
    }
}
