package view.contabilidade;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import Exception.EntradaException;
import Exception.ProdutoException;
import model.Entrada;
import model.Produto;
import controller.EntradaControl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TelaRelatorioCompras extends Application {
    private DatePicker datePicker = new DatePicker(LocalDate.now());
    private VBox vboxCalendario = new VBox(10, datePicker);
    private EntradaControl control = null;
    private List<Entrada> lista = new ArrayList<>();

    @Override
    public void start(Stage stage) {

        try{
            control = new EntradaControl();
        } catch (EntradaException | ProdutoException e){
            System.err.println("Erro ao iniciar a control");
        }
        // T�tulo e Data
        Label lblTitulo = new Label("Gerar Relatorios de compra");
        Label lblData = new Label("Data :");

        HBox hboxTitulo = new HBox(20, lblTitulo, lblData);
        hboxTitulo.setPadding(new Insets(10));
        hboxTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Tabela (sem tipo gen�rico para simplificar)
        TableView<Entrada> tabela = new TableView<>();
        tabela.setPrefHeight(300);

        TableColumn<Entrada, String> colDescricao = new TableColumn<>("ID e Nome");
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("produto"));
        TableColumn<Entrada, String> colValor = new TableColumn<>("Valor");
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorUnitarioEntrada"));
        TableColumn<Entrada, String> colQuantidade = new TableColumn<>("Quantidade");
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidadeEntrada"));

        tabela.getColumns().addAll(colDescricao, colValor, colQuantidade);

        // Faz a tabela crescer dentro do VBox
        VBox.setVgrow(tabela, Priority.ALWAYS);

        // Label de valor total
        Label lblValorTotal = new Label("Valor Total :");
        lblValorTotal.setStyle("-fx-font-size: 14px;");
        HBox hboxValorTotal = new HBox(lblValorTotal);
        hboxValorTotal.setPadding(new Insets(10));

        // DatePicker
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setShowWeekNumbers(false);


        vboxCalendario.setPadding(new Insets(10));
        vboxCalendario.setStyle("-fx-background-color: #e0e0e0;");
        vboxCalendario.setPrefWidth(200);

        // Bot�es inferiores
        Button btnBaixar = new Button("Baixar Relatorio");
        Button btnGerar = new Button("Gerar Relatorio deste dia");
        Button btnVoltar = new Button("Voltar");

        btnBaixar.setOnAction(e -> {
            exportTableViewToCSV(tabela);
        });

        btnVoltar.setOnAction(e -> {
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            TelaContabilidadeHub hub = new TelaContabilidadeHub();
            try {
                hub.start(stageAtual);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnGerar.addEventFilter(ActionEvent.ANY,
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        try{
                            generateBindings();
                            control.pesquisarTodasEntradas();
                            tabela.setItems(control.getLista());
                            lblValorTotal.setText("Valor Total: R$" + (calcularValorTotal(lista = control.getLista())));
                            lblData.setText("Data: " + datePicker.getValue());
                        } catch(Exception | EntradaException err) {
                            alert("Erro ao pesquisar: " + err.getMessage());
                        }
                    }
                });


        HBox hboxBotoes = new HBox(10, btnBaixar, btnGerar, btnVoltar);
        hboxBotoes.setPadding(new Insets(10));
        hboxBotoes.setAlignment(Pos.CENTER_RIGHT);

        // Painel esquerdo (tabela + t�tulo + valor total + bot�es)
        VBox painelEsquerda = new VBox(10, hboxTitulo, tabela, hboxValorTotal, hboxBotoes);
        painelEsquerda.setPadding(new Insets(10));
        // Removido setPrefWidth para permitir crescimento autom�tico
        // painelEsquerda.setPrefWidth(700);

        // Faz o painel esquerdo crescer horizontalmente
        HBox.setHgrow(painelEsquerda, Priority.ALWAYS);
        // Faz o painel esquerdo crescer verticalmente se necess�rio
        VBox.setVgrow(painelEsquerda, Priority.ALWAYS);

        // Layout principal
        HBox layout = new HBox(painelEsquerda, vboxCalendario);
        layout.setPrefSize(950, 500);
        layout.setHgrow(painelEsquerda, Priority.ALWAYS);

        Scene scene = new Scene(layout);
        stage.setTitle("Relat�rio de Compras");
        stage.setScene(scene);
        stage.show();
    }

    public void generateBindings(){
       LocalDate data = datePicker.getValue();
        System.out.println(data);
       control.dataProperty().set(data);

    }

    public double calcularValorTotal(List<Entrada> e){
        int quant = 0;
        double valorUnit = 0;
        for (Entrada lulu : e){
            quant += lulu.getQuantidadeEntrada();
            valorUnit += lulu.getValorUnitarioEntrada();
        }

        return quant * valorUnit;
    }

    public static void alert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }

    public void exportTableViewToCSV(TableView<Entrada> tabela) {
        String caminhoArquivo = "C:/relatorios/relatorioEntrada/Entrada_" + datePicker.getValue() + ".csv";
        File arquivo = new File(caminhoArquivo);
        File pasta = arquivo.getParentFile();

        if (pasta != null && !pasta.exists()) {
            boolean criada = pasta.mkdirs(); // cria todas as pastas do caminho
            if (!criada) {
                System.out.println("Não foi possível criar a pasta " + pasta.getAbsolutePath());
                return; // ou trate o erro como quiser
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            // Escreve o cabeçalho
            writer.write("IdProduto e Nome; Valor ;Quantidade");
            writer.newLine();

            // Para cada item na tabela, escreve uma linha CSV
            for (Entrada entrada : tabela.getItems()) {
                String linha = String.format("%s; %.2f; %d",
                        entrada.getProduto(),                   // ou outro método que retorna String
                        entrada.getValorUnitarioEntrada(),
                        entrada.getQuantidadeEntrada()
                );
                writer.write(linha);
                writer.newLine();
            }
            alert("Relatório exportado com sucesso para:\n" + caminhoArquivo);
        } catch (IOException e) {
            alert("Erro ao exportar relatório: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
