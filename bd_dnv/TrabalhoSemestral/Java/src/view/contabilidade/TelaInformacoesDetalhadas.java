package view.contabilidade;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Empresa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TelaInformacoesDetalhadas extends Application {

    private Empresa e;

    public void setEmpresa(Empresa empresa) {
        this.e = empresa;
    }

    @Override
    public void start(Stage stage) {
        ObservableList<Empresa> lista = FXCollections.observableArrayList(e);
        // T�tulo
        Label titulo = new Label(e.getNomeEmpresa());
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        HBox boxTitulo = new HBox(titulo);
        boxTitulo.setAlignment(Pos.CENTER);
        boxTitulo.setPadding(new Insets(10, 0, 10, 0));

        // Tabela Endere�o
        TableView<Empresa> tabelaEndereco = new TableView();
        tabelaEndereco.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Empresa, String> colCep = new TableColumn<>("CEP");
        colCep.setCellValueFactory(new PropertyValueFactory<>("CEP"));
        TableColumn<Empresa, String> colLog = new TableColumn<>("Logradouro");
        colLog.setCellValueFactory(new PropertyValueFactory<>("Logradouro"));
        TableColumn<Empresa, Integer> colNum = new TableColumn<>("Numero");
        colNum.setCellValueFactory(new PropertyValueFactory<>("numero"));
        TableColumn<Empresa, String> colCom = new TableColumn<>("Complemento");
        colCom.setCellValueFactory(new PropertyValueFactory<>("complemento"));
        TableColumn<Empresa, String> colCidade = new TableColumn<>("Cidade");
        colCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        TableColumn<Empresa, String> colBairro = new TableColumn<>("Bairro");
        colBairro.setCellValueFactory(new PropertyValueFactory<>("bairro"));
        tabelaEndereco.getColumns().addAll(
                colCep, colLog, colNum, colCom, colCidade, colBairro
        );
        tabelaEndereco.setItems(lista);

        VBox.setVgrow(tabelaEndereco, Priority.ALWAYS);

        // Tabela Email
        TableView<String> tabelaEmail = new TableView();
        tabelaEmail.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<String, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(cellData ->  Bindings.createStringBinding(() -> cellData.getValue()
        ));
        tabelaEmail.getColumns().add(colEmail);

        List<String> listaEmails = new ArrayList<>();
        listaEmails.add(e.getEmail()[0]);
        listaEmails.add(e.getEmail()[1]);
        listaEmails.add(e.getEmail()[2]);

        ObservableList<String> emails = FXCollections.observableArrayList(
                Arrays.stream(e.getEmail())
                        .filter(email -> email != null && !email.isBlank())
                        .toList()
        );

        tabelaEmail.setItems(emails);
        VBox.setVgrow(tabelaEmail, Priority.ALWAYS);

        // Tabela Telefone
        TableView<String> tabelaTelefone = new TableView();
        tabelaTelefone.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<String, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(cellData ->  Bindings.createStringBinding(() -> cellData.getValue()
        ));
        tabelaTelefone.getColumns().add(colTelefone);

        List<String> listaTelefone = new ArrayList<>();
        listaTelefone.add(e.getTelefone()[0]);
        listaTelefone.add(e.getTelefone()[1]);
        listaTelefone.add(e.getTelefone()[2]);

        ObservableList<String> telefones = FXCollections.observableArrayList(
                Arrays.stream(e.getTelefone())
                        .filter(telefone -> telefone != null && !telefone.isBlank())
                        .toList()
        );

        tabelaTelefone.setItems(telefones);

        VBox.setVgrow(tabelaTelefone, Priority.ALWAYS);

        // Bot�o Voltar
        Button btnVoltar = new Button("voltar");
        
        btnVoltar.setOnAction(e -> {
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            TelaVerificarEmpresas verificar = new TelaVerificarEmpresas();
            try {
                verificar.start(stageAtual); 
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        HBox boxBtn = new HBox(btnVoltar);
        boxBtn.setAlignment(Pos.BOTTOM_RIGHT);
        boxBtn.setPadding(new Insets(10, 0, 0, 0));

        // Layout principal responsivo
        VBox layoutCentral = new VBox(10,
                boxTitulo,
                tabelaEndereco,
                tabelaEmail,
                tabelaTelefone,
                boxBtn
        );
        layoutCentral.setPadding(new Insets(20));
        VBox.setVgrow(layoutCentral, Priority.ALWAYS);

        BorderPane root = new BorderPane(layoutCentral);
        Scene scene = new Scene(root, 950, 520);

        stage.setTitle("Informa��es Detalhadas");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
