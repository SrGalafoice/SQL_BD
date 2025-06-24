package view.contabilidade;

import Exception.VendaException;
import controller.VendaControl;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Exception.VendaException;
import model.Entrada;
import model.Produto;
import model.Venda;

public class TelaRelatorioVendas extends Application {
    private DatePicker datePicker = new DatePicker();
    private VendaControl control = null;
    private List<Venda> lista =  new ArrayList<>();

    @Override
    public void start(Stage stage) {

        try{
            control = new VendaControl();
        } catch (VendaException e){
            System.err.println(e.getMessage());
        }
        // T�tulo e Data
        Label titulo = new Label("Gerar Relatorios de Vendas");
        Label lbldata = new Label("Data:");
        HBox topo = new HBox(20, titulo, lbldata);
        topo.setPadding(new Insets(10));
        topo.setAlignment(Pos.CENTER_LEFT);
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Tabela
        TableView<Venda> tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Venda, String> colProduto = new TableColumn<>("IDProduto e Nome");
        colProduto.setCellValueFactory(new PropertyValueFactory<>("produto"));
        TableColumn<Venda, Integer> colQtd = new TableColumn<>("Quantidade");
        colQtd.setCellValueFactory(cellData -> {
                    return new SimpleIntegerProperty(cellData.getValue().getProduto().getQuantidade()).asObject();
                });
        TableColumn<Venda, Double> colUnit = new TableColumn<>("ValorUnitario");
        colUnit.setCellValueFactory(cellData -> {
            return new SimpleDoubleProperty(cellData.getValue().getProduto().getValorUnitarioVenda()).asObject();
        });
        TableColumn<Venda, Double> colTotal = new TableColumn<>("Valor Total");
        colTotal.setCellValueFactory(cellData -> {
            return new SimpleDoubleProperty(cellData.getValue().getCarrinho().getValorTotal()).asObject();
        });
        TableColumn<Venda, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(new PropertyValueFactory<>("empresa"));

        tabela.getColumns().addAll(colProduto, colQtd, colUnit, colTotal, colCliente);
        VBox.setVgrow(tabela, Priority.ALWAYS);

        // Valor Total
        Label lblValorTotal = new Label("Valor Total :");
        HBox valorBox = new HBox(lblValorTotal);
        valorBox.setPadding(new Insets(5));
        valorBox.setAlignment(Pos.CENTER_LEFT);

        // Calend�rio (DatePicker)

        VBox calendarioBox = new VBox(datePicker);
        calendarioBox.setPadding(new Insets(10));
        calendarioBox.setAlignment(Pos.TOP_CENTER);

        // Bot�es
        Button btnBaixar = new Button("Baixar Relat�rio");
        Button btnGerar = new Button("Gerar Relatorio deste dia");
        Button btnVoltar = new Button("Voltar");
        
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
                            control.pesquisarVendas();
                            tabela.setItems(control.getLista());
                           lblValorTotal.setText("Valor Total: R$" + (calcularValorTotal(lista = control.getLista())));
                           lbldata.setText("Data: " + datePicker.getValue() );
                        } catch(Exception err) {
                            alert("Erro ao pesquisar: " + err.getMessage());
                        }
                    }
                });
        btnBaixar.setOnAction(e -> {
            exportTableViewToCSV(tabela);
        });
        HBox botoes = new HBox(10, btnBaixar, btnGerar, btnVoltar);
        botoes.setAlignment(Pos.BOTTOM_RIGHT);
        botoes.setPadding(new Insets(10));

        // Painel esquerdo (Tabela + total + bot�es)
        VBox esquerda = new VBox(10, topo, tabela, valorBox, botoes);
        esquerda.setPadding(new Insets(10));
        esquerda.setPrefWidth(750);
        VBox.setVgrow(tabela, Priority.ALWAYS);

        // Layout principal
        HBox layout = new HBox(10, esquerda, calendarioBox);

        Scene scene = new Scene(layout, 1010, 550);
        stage.setTitle("Relat�rio de Vendas");
        stage.setScene(scene);
        stage.show();
    }

    public void generateBindings(){
        LocalDate data = datePicker.getValue();
        System.out.println(data);
        control.dataProperty().set(data);
    }
    public static void alert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }
    public double calcularValorTotal(List<Venda> e){
        int quant = 0;
        double valorUnit = 0;
        for (Venda lulu : e){
            quant += lulu.getProduto().getQuantidade();
            valorUnit += lulu.getProduto().getValorUnitarioVenda();
        }
        return quant * valorUnit;
    }

    public void exportTableViewToCSV(TableView<Venda> tabela) {
        String caminhoArquivo = "C:/relatorios/relatorioVenda/Venda_" + datePicker.getValue() + ".csv";
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
            writer.write("IdProduto e Nome; Quantidade; ValorUnitario ;Valor Total; Cliente");
            writer.newLine();

            // Para cada item na tabela, escreve uma linha CSV
            for (Venda v : tabela.getItems()) {
                String linha = String.format("%s; %d;  %.2f;  %.2f; %s",
                        v.getProduto(),                   // ou outro método que retorna String
                        v.getProduto().getQuantidade(),
                        v.getProduto().getValorUnitarioVenda(),
                        v.getCarrinho().getValorTotal(),
                        v.getEmpresa()
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
