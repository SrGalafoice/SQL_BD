package view.caixa;

import controller.ProdutoControl;
import controller.VendaControl;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import model.Carrinho;
import model.Empresa;
import Exception.*;
import model.Produto;
import model.Venda;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TelaCaixa extends Application {
        private Empresa empresa = new Empresa();
        private TextField tfEmpresa = new TextField();
        private TextField tfId = new TextField();
        private TextField tfQtd = new TextField();
        private ProdutoControl pControl = null;
        private VendaControl vControl = null;
        private List<Produto> lista = new ArrayList<>();
        private ObservableList<Produto> listando = FXCollections.observableArrayList();
        private Double valor = 0.0;
        private StringProperty cnpj = new SimpleStringProperty();
        private Produto p = new Produto();
        public void setEmpresa(Empresa e){
            this.empresa = e;
        }
    @Override
    public void start(Stage primaryStage) {

            try{
                pControl = new ProdutoControl();
            }catch (Exception ex){
                System.err.println("Erro ao inicicar control " + ex.getMessage());
            } catch (ProdutoException ex) {
                throw new RuntimeException(ex);
            }

            try{
                vControl = new VendaControl();
            } catch (VendaException ex){
                    System.err.println(ex.getMessage());
            }
            cnpj.set("X");
        tfEmpresa.textProperty().bind(cnpj);
        // Tabela
        TableView<Produto> tabela = new TableView<>();
        TableColumn<Produto, String> colId = new TableColumn<>("ID Produto");
        colId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<Produto, String> colNome = new TableColumn<>("Nome Produto");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Produto, String> colQtd = new TableColumn<>("Quantidade");
        colQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        TableColumn<Produto, String> colValor = new TableColumn<>("Valor");
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorUnitarioVenda"));
        tabela.getColumns().addAll(colId, colNome, colQtd, colValor);
        tabela.setPrefHeight(300);

        // Label valor total
        Label lblValorTotal = new Label("Valor Total:");
        Label lblValor = new Label("R$"+valor);
        HBox hboxValor = new HBox(10, lblValorTotal, lblValor);
        hboxValor.setAlignment(Pos.CENTER);

        // Bot�o apagar todos
        Button btnApagarTodos = new Button("Apagar todos os itens do carrinho");
        btnApagarTodos.setOnAction(e -> {
            lista.clear();
            listando.clear();
            lblValor.setText("R$" + 0);
        });

        // Layout inferior
        HBox hboxInferior = new HBox(20, btnApagarTodos, hboxValor);
        hboxInferior.setPadding(new Insets(10));
        hboxInferior.setAlignment(Pos.CENTER_LEFT);

        // Campos de produto (ID e Quantidade)
        Label lblId = new Label("ID Produto");

        Label lblQtd = new Label("Quantidade");

        // Bot�es produto
        Button btnRemover = new Button("Remover Produto");
        btnRemover.setOnAction(e -> {
            Produto selecionada = tabela.getSelectionModel().getSelectedItem();
            if (selecionada != null) {
                valor -= (selecionada.getValorUnitarioVenda() * selecionada.getQuantidade());
                lista.remove(selecionada);
                listando.remove(selecionada);
                lblValor.setText("R$" + valor);
            } else alert("Selecione um item para remover.");
        });
        Button btnFinalizar = new Button("Finalizar Compra");
        btnFinalizar.setOnAction(e -> {
            try {
                pControl.atualizarProduto(p);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (ProdutoException ex) {
                alert(ex.getMessage());
            }
            Carrinho c = new Carrinho();
            Venda v = new Venda();
            c.setValorTotal(valor);
            c.setProduto(listando);
            v.setCarrinho(c);
            v.setEmpresa(empresa);
            v.setData(LocalDate.now());
            vControl.adicionarVenda(v);
            alert("Compra Finalizada");
        });

        Button btnAdicionar = new Button("Adicionar Produto");
        btnAdicionar.setOnAction(e -> {
            if(!tfEmpresa.getText().contains("X") && tfEmpresa.getText() != null){
                try{
                    p = pControl.pesquisarProduto(tfId.getText());
                    p.setID(Integer.parseInt(tfId.getText()));
                    p.setQuantidade(Integer.parseInt(tfQtd.getText()));

                    listando.add(p);
                    tabela.setItems(listando);

                    valor += (p.getQuantidade() * p.getValorUnitarioVenda());
                    lblValor.setText("R$" + valor);
                }catch (ProdutoException ex){
                    alert("Erro ao adicionar produto: " + ex.getMessage());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else  alert("Selecione uma empresa antes de adicionar o produto.");

        });

        VBox vboxProduto = new VBox(10, lblId, tfId, lblQtd, tfQtd, btnRemover, btnAdicionar, btnFinalizar);
        vboxProduto.setPadding(new Insets(10));
        vboxProduto.setAlignment(Pos.TOP_LEFT);

        // Empresa
        Button btnAdicionarEmpresa = new Button("Adicionar Empresa");

        btnAdicionarEmpresa.setOnAction(e -> {
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            TelaConsultarEmpresas consulta = new TelaConsultarEmpresas();
            try {
                consulta.start(stageAtual); // Reutiliza o Stage atual
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        if (empresa != null){
            cnpj.set(empresa.getCnpj());
            tfEmpresa.textProperty().bind(cnpj);
        }
        Label lblEmpresa = new Label("Empresa:");


        VBox vboxEmpresa = new VBox(10, btnAdicionarEmpresa, lblEmpresa, tfEmpresa);
        vboxEmpresa.setPadding(new Insets(10));
        vboxEmpresa.setAlignment(Pos.TOP_LEFT);

        VBox vboxDireita = new VBox(20, vboxEmpresa, vboxProduto);
        vboxDireita.setPadding(new Insets(10));
        vboxDireita.setAlignment(Pos.TOP_CENTER);

        // Layout principal
        BorderPane layout = new BorderPane();
        layout.setCenter(tabela);
        layout.setBottom(hboxInferior);
        layout.setRight(vboxDireita);

        // Cena
        Scene scene = new Scene(layout, 900, 500);
        primaryStage.setTitle("Tela de Caixa");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void alert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
