package model;

import java.time.LocalDate;

public class Entrada {
    private Produto produto;
    private int quantidadeEntrada;
    private LocalDate data;
    private Double valorUnitarioEntrada;
    private Double valorTotal;

    public model.Produto getProduto() {
        return produto;
    }

    public void setProduto(model.Produto produto) {
        this.produto = produto;
    }

    public int getQuantidadeEntrada() {
        return quantidadeEntrada;
    }

    public void setQuantidadeEntrada(int quantidadeEntrada) {
        this.quantidadeEntrada = quantidadeEntrada;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Double getValorUnitarioEntrada() {
        return valorUnitarioEntrada;
    }

    public void setValorUnitarioEntrada(Double valorUnitarioEntrada) {
        this.valorUnitarioEntrada = valorUnitarioEntrada;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }


}
