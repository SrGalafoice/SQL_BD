package model;

public class Produto {
    private int ID;
    private Double valorUnitarioVenda;
    private String nome;
    private int quantidade;
    private Prateleira prateleira;

    public int getID(){
        return ID;
    }
    public void setID(int ID){
        this.ID = ID;
    }
    public Double getValorUnitarioVenda() {
        return valorUnitarioVenda;
    }

    public void setValorUnitarioVenda(Double valorUnitarioVenda) {
        this.valorUnitarioVenda = valorUnitarioVenda;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Prateleira getPrateleira() {
        return prateleira;
    }

    public void setPrateleira(Prateleira prateleira) {
        this.prateleira = prateleira;
    }

    @Override
    public String toString() {
        return ID + " " + nome;
    }
}
