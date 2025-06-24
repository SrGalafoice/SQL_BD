package view.estoque;

import controller.EntradaControl;
import Exception.*;
import controller.PrateleiraControl;
import controller.ProdutoControl;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import Exception.ProdutoException;
import model.Produto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TelaAdicionarProdutos extends Application {
    private EntradaControl Econtrol = null;
    private PrateleiraControl Pcontrol = null;
    private ProdutoControl PROcontrol = null;
    private TextField tfNome = new TextField();
    private TextField tfValorUnitario = new TextField();
    private TextField tfValorVenda = new TextField();
    private TextField tfQuantidade = new TextField();
    private TextField tfIdProduto = new TextField();
    private ComboBox<String> cbPrateleira = new ComboBox<>();
    private int espacos = 0;
    private int total = 0;
    private List<Produto> lista = new ArrayList<>();
    private String numero = "";
    private StringProperty espacosRestantes = new SimpleStringProperty();
    @Override
    public void start(Stage stage) {
        try {
            Pcontrol = new PrateleiraControl();
        } catch (PrateleiraException e) {
            System.err.println("Erro ao iniciar a Control" + e.getMessage());
        }

        try {
            PROcontrol = new ProdutoControl();
        } catch (ProdutoException e) {
            throw new RuntimeException(e);
        }


        TextField tfEspacosVazios = new TextField();
        tfEspacosVazios.textProperty().bind(espacosRestantes);

        try {
            Econtrol = new EntradaControl();
        } catch (EntradaException | ProdutoException e) {
            System.err.println("Erro ao iniciar a Control" + e.getMessage());
        }

        cbPrateleira.setPromptText("Selecione Prateleira");
        for (int i = 1; i <= 10; i++) {
            cbPrateleira.getItems().add("Prateleira " + i);
        }


        // Bot�es
        Button btnProcurar = new Button("Procurar Produto");
        Button btnAtualizar = new Button("Atualizar prateleira");
        Button btnAdicionar = new Button("Adicionar Produto");
        Button btnVoltar = new Button("Voltar");

        btnAtualizar.setOnAction(e ->{
    try {
            generateBindings();
            total = espacosUsados();

            espacosRestantes.set("espaços vazios: " + (100 - total));
        } catch (Exception ex) {
            espacosRestantes.set("Erro ao atualizar prateleira.");
        }});

        btnProcurar.addEventFilter(ActionEvent.ANY, new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e) {
                Produto p = new Produto();
                try {
                    if (tfIdProduto.getText().matches(".*[a-zA-Z].*")){
                        alert("O ID não contém letras.");
                    } else
                        p = PROcontrol.pesquisarProduto(tfIdProduto.getText());
                    tfNome.setText(p.getNome());
                    tfValorVenda.setText(String.valueOf(p.getValorUnitarioVenda()));
                } catch (IllegalArgumentException ex) {
                    alert("Erro: " + ex.getMessage());
                } catch (SQLException | ProdutoException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        btnVoltar.setOnAction(e -> {
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            view.estoque.TelaEstoqueHub hub = new view.estoque.TelaEstoqueHub();
            try {
                hub.start(stageAtual);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        btnAdicionar.addEventFilter(ActionEvent.ANY, new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e) {
                try {
                    generateBindings();
                    int usados = espacosUsados();
                    Econtrol.adicionarEntrada(usados);
                    alert("Produto adicionado e Entrada criada com sucesso!");

                    espacosRestantes.set("espaços vazios: " + (100 - (usados + Econtrol.quantidadeProperty().get())));
                    clear();
                } catch (IllegalArgumentException ex) {
                    alert("Erro: " + ex.getMessage());
                } catch (EntradaException | ProdutoException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        // Layouts organizados por linha
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(20);
        grid.setVgap(15);

        // Tornar colunas responsivas
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(33);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(33);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(33);
        grid.getColumnConstraints().addAll(col1, col2, col3);

        grid.add(new Label("Nome Produto"), 0, 0);
        grid.add(tfNome, 0, 1);

        grid.add(new Label("Valor Unit�rio"), 1, 0);
        grid.add(tfValorUnitario, 1, 1);

        grid.add(new Label("Prateleira"), 2, 0);
        HBox hbPrateleira = new HBox(10, cbPrateleira, tfEspacosVazios);
        HBox hbPrateleira2 = new HBox(10,btnAtualizar);
        hbPrateleira2.setAlignment(Pos.CENTER_LEFT);
        hbPrateleira.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(tfEspacosVazios, Priority.ALWAYS);
        grid.add(hbPrateleira, 2, 1);
        grid.add(hbPrateleira2,2, 2);

        grid.add(new Label("Valor Venda"), 0, 2);
        grid.add(tfValorVenda, 0, 3);

        grid.add(new Label("Quantidade"), 1, 2);
        grid.add(tfQuantidade, 1, 3);

        grid.add(new Label("ID Produto"), 0, 4);
        grid.add(tfIdProduto, 0, 5);
        grid.add(btnProcurar, 1, 5);

        HBox hbButtons = new HBox(20, btnVoltar, btnAdicionar);
        hbButtons.setAlignment(Pos.CENTER_RIGHT);
        hbButtons.setPadding(new Insets(10));

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setFillWidth(true);
        layout.getChildren().addAll(new Label("Adicionar produto"), grid, hbButtons);
        VBox.setVgrow(grid, Priority.ALWAYS);

        // Cena redimension�vel
        Scene scene = new Scene(layout, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("Tela Adicionar Produtos");
        stage.show();

    }

    public void generateBindings() {
        String prateleira = cbPrateleira.getValue();
        numero = prateleira.replace("Prateleira ", "");
        Econtrol.prateleiraIDProperty().set(Integer.parseInt(numero));
        Pcontrol.IDProperty().set(Integer.parseInt(numero));

        StringConverter<Number> converter = new NumberStringConverter();
        Bindings.bindBidirectional(tfIdProduto.textProperty(), Econtrol.pIDProperty(), converter);
        Bindings.bindBidirectional(tfNome.textProperty(), Econtrol.nomeProperty());
        Bindings.bindBidirectional(tfQuantidade.textProperty(), Econtrol.quantidadeProperty(), converter);
        Bindings.bindBidirectional(tfValorUnitario.textProperty(), Econtrol.valorUnitarioEntradaProperty(), converter);
        Bindings.bindBidirectional(tfValorVenda.textProperty(), Econtrol.valorUnitarioVendaProperty(), converter);
    }

    public void alert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }

    public void clear(){
        tfIdProduto.setText("");
        tfNome.setText("");
        tfQuantidade.setText("");
        tfValorVenda.setText("");
        tfValorUnitario.setText("");
    }

    public int espacosUsados() throws SQLException {
        lista = Pcontrol.consultarPrateleira();
        return lista.stream()
                .filter(p -> p != null && p.getQuantidade() != 0)
                .mapToInt(Produto::getQuantidade)
                .sum();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
