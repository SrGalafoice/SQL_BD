package model;

import java.time.LocalDate;
import java.util.List;

public class RelatorioEntrada {
    private List<Entrada> entradas;
    private Double valorTotal;
    private LocalDate dataRelatorio;

    public List<Entrada> getEntradas() {
        return entradas;
    }

    public void setEntradas(List<Entrada> entradas) {
        this.entradas = entradas;
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
        return "RelatorioEntrada{" +
                "entradas=" + entradas +
                ", valorTotal=" + valorTotal +
                ", dataRelatorio=" + dataRelatorio +
                '}';
    }
}
