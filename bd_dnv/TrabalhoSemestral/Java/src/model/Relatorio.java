package model;

import java.time.LocalDate;
import java.util.List;

public class Relatorio {
    private List<Venda> vendas;
    private Double valorTotal;
    private LocalDate dataRelatorio;

    public List<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDate getDataRelatorio() {
        return dataRelatorio;
    }

    public void setDataRelatorio(LocalDate dataRelatorio) {
        this.dataRelatorio = dataRelatorio;
    }

    @Override
    public String toString() {
        return "Relatorio{" +
                "vendas=" + vendas +
                ", valorTotal=" + valorTotal +
                ", dataRelatorio=" + dataRelatorio +
                '}';
    }
}
