package view.contabilidade;

import controller.EmpresaControl;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import Exception.EmpresaException;
import model.Empresa;
import view.caixa.TelaConsultarEmpresas;

import java.util.ArrayList;
import java.util.List;

public class TelaVerificarEmpresas extends Application {
    private EmpresaControl control = null;
    private List<Empresa> lista = new ArrayList<>();

    @Override
    public void start(Stage stage) {

        try{
            control = new EmpresaControl();
        }catch (EmpresaException e){
            System.err.println("Erro ao inciar a control");
        }
        // Tabela com colunas vazias (sem dados, sem funcionalidade)
        TableView<Empresa> tabela = new TableView();
        tabela.setPrefHeight(400);

        TableColumn colEmpresa = new TableColumn<>("Empresa");
        colEmpresa.setCellValueFactory(new PropertyValueFactory<>("nomeEmpresa"));
        TableColumn colCnpj = new TableColumn<>("CNPJ");
        colCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        TableColumn colCep = new TableColumn<>("CEP");
        colCep.setCellValueFactory(new PropertyValueFactory<>("CEP"));


        tabela.getColumns().addAll(colEmpresa, colCnpj, colCep);

        control.pesquisarEmpresas();
        lista = control.getLista();
        tabela.setItems(control.getLista());

        // Botões
        Button btnDetalhes = new Button("Ver informacoes\nDetalhadas");
        
        btnDetalhes.setOnAction(e -> {
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Empresa empresaSelecionada = tabela.getSelectionModel().getSelectedItem();
            if (empresaSelecionada != null){
                try {
                    TelaInformacoesDetalhadas info = new TelaInformacoesDetalhadas();
                    info.setEmpresa(empresaSelecionada);
                    info.start(stageAtual); // Reutiliza o Stage atual
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else alert("Selecione uma empresa antes de clicar no botão.");
        });
        
        
        Button btnVoltar = new Button("voltar");
        btnVoltar.setOnAction(e -> {
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            TelaContabilidadeHub hub = new TelaContabilidadeHub();
            try {
                hub.start(stageAtual); 
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox vboxBotoes = new VBox(20, btnDetalhes, btnVoltar);
        vboxBotoes.setAlignment(Pos.CENTER);
        vboxBotoes.setPadding(new Insets(10));

        // Layout principal
        BorderPane layout = new BorderPane();
        layout.setCenter(tabela);
        layout.setRight(vboxBotoes);

        Scene scene = new Scene(layout, 950, 500);
        stage.setTitle("Verificar Empresas");
        stage.setScene(scene);
        stage.show();
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

