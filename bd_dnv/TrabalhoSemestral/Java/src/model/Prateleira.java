package model;

import java.util.List;

public class Prateleira {
    private int id;
    private List<model.Produto> produtos;
    private int corredor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCorredor() {
        return corredor;
    }

    public void setCorredor(int corredor) {
        this.corredor = corredor;
    }

    public List<model.Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<model.Produto> produto) {
        produtos = produto;
    }

    @Override
    public String toString() {
        return "Prateleira{" +
                "id='" + id + '\'' +
                ", produtos=" + produtos +
                ", corredor=" + corredor +
                '}';
    }
}
