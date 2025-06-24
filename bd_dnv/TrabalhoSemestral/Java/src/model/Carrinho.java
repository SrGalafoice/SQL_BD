package model;

import java.util.Arrays;
import java.util.List;

public class Carrinho {
    private List<Produto> Produto;
    private Double valorTotal;
    private Empresa cliente;

    public Carrinho() {
    }

    public List<Produto> getProduto() {
        return Produto;
    }

    public void setProduto(List<Produto> produto) {
        Produto = produto;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Empresa getCliente() {
        return cliente;
    }

    public void setCliente(Empresa cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Carrinho{" +
                "Produto=" + Produto +
                ", valorTotal=" + valorTotal +
                ", cliente=" + cliente +
                '}';
    }
}
