package view.caixa;

//Arquivo: TelaAdicionarEmpresa.java

import Exception.EmpresaException;
import controller.EmpresaControl;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
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
import Exception.EmpresaException;

import java.sql.SQLException;

public class TelaCadastrarEmpresas extends Application {
    private TextField tfNomeFantasia = new TextField();
    private TextField tfCNPJ = new TextField();
    private TextField tfLogradouro = new TextField();
    private TextField tfCEP = new TextField();
    private TextField tfNumero = new TextField();
    private TextField tfCidade = new TextField();
    private TextField tfBairro = new TextField();
    private TextField tfComplemento = new TextField();
    private TextField tfEmail1 = new TextField();
    private TextField tfTelefone1 = new TextField();
    private TextField tfEmail2 = new TextField();
    private TextField tfTelefone2 = new TextField();
    private TextField tfEmail3 = new TextField();
    private TextField tfTelefone3 = new TextField();

    private EmpresaControl control = null;
    private String[] email = new String[3];
    private String[] telefone = new String[3];


 @Override
 public void start(Stage primaryStage) {

     try{
         control = new EmpresaControl();
     } catch (EmpresaException e) {
         System.err.println("Erro ao inciar a control "+ e.getMessage());
     }

     // Coluna da esquerda
     GridPane gridEsquerda = new GridPane();
     gridEsquerda.setVgap(10);
     gridEsquerda.setHgap(10);

     gridEsquerda.addRow(0, new Label("Nome Fantasia"), tfNomeFantasia);
     gridEsquerda.addRow(1, new Label("CNPJ"), tfCNPJ);
     gridEsquerda.addRow(2, new Label("Logradouro"), tfLogradouro);
     gridEsquerda.addRow(3, new Label("CEP"), tfCEP);
     gridEsquerda.addRow(4, new Label("Numero"), tfNumero);
     gridEsquerda.addRow(5, new Label("Cidade"), tfCidade);
     gridEsquerda.addRow(6, new Label("Bairro"), tfBairro);
     gridEsquerda.addRow(7, new Label("Complemento"), tfComplemento);

     // Coluna da direita
     GridPane gridDireita = new GridPane();
     gridDireita.setVgap(10);
     gridDireita.setHgap(10);

     tfEmail1.setPromptText("campo obrigat�rio");
     tfTelefone1.setPromptText("campo obrigat�rio");
     tfEmail2.setPromptText("campo opcional");
     tfTelefone2.setPromptText("campo opcional");
     tfEmail3.setPromptText("campo opcional");
     tfTelefone3.setPromptText("campo opcional");

     gridDireita.addRow(0, new Label("Email 1"), tfEmail1, new Label("Telefone 1"), tfTelefone1);
     gridDireita.addRow(1, new Label("Email 2"), tfEmail2, new Label("Telefone 2"), tfTelefone2);
     gridDireita.addRow(2, new Label("Email 3"), tfEmail3, new Label("Telefone 3"), tfTelefone3);

     // Bot�es inferiores
     HBox botoes = new HBox(10);
     botoes.setAlignment(Pos.CENTER);
     Button btnCadastrar = new Button("Cadastrar empresa");
     Button btnVoltar = new Button("Voltar");

     btnCadastrar.addEventFilter(ActionEvent.ANY, new EventHandler<ActionEvent>(){
         @Override
         public void handle(ActionEvent e) {
             try {
                 generateBindings();
                 control.adicionarEmpresa(email, telefone);
                 alert("Empresa cadastrada!");
                 clear();
             } catch (IllegalArgumentException ex) {
                 alert("Erro: " + ex.getMessage());
             } catch (EmpresaException ex) {
                 throw new RuntimeException(ex);
             }
         }
     });

     btnVoltar.setOnAction(e -> {
         Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
         TelaConsultarEmpresas telaConsulta = new TelaConsultarEmpresas();
         try {
             telaConsulta.start(stageAtual);
         } catch (Exception ex) {
             ex.printStackTrace();
         }
     });
     botoes.getChildren().addAll(btnCadastrar, btnVoltar);

     VBox painelEsquerdo = new VBox(10, new Label("Adicionar Empresa"), gridEsquerda);
     VBox painelDireito = new VBox(10, gridDireita, botoes);

     HBox layoutPrincipal = new HBox(30, painelEsquerdo, painelDireito);
     layoutPrincipal.setPadding(new Insets(20));

     Scene scene = new Scene(layoutPrincipal, 900, 450);
     primaryStage.setTitle("Adicionar Empresa");
     primaryStage.setScene(scene);
     primaryStage.show();

     generateBindings();
 }
    public void generateBindings() {
        email[0] = tfEmail1.getText();
        email[1] = tfEmail2.getText();
        email[2] = tfEmail3.getText();

        telefone[0] = tfTelefone1.getText();
        telefone[1] = tfTelefone2.getText();
        telefone[2] = tfTelefone3.getText();

        StringConverter<Number> converter = new NumberStringConverter();
        Bindings.bindBidirectional(tfCNPJ.textProperty(), control.cnpjProperty());
        Bindings.bindBidirectional(tfNomeFantasia.textProperty(), control.nomeProperty());
        Bindings.bindBidirectional(tfLogradouro.textProperty(), control.ruaProperty());
        Bindings.bindBidirectional(tfNumero.textProperty(), control.numeroProperty(), converter);
        Bindings.bindBidirectional(tfCEP.textProperty(), control.CEPProperty());
        Bindings.bindBidirectional(tfCidade.textProperty(), control.cidadeProperty());
        Bindings.bindBidirectional(tfBairro.textProperty(), control.bairroProperty());
        Bindings.bindBidirectional(tfComplemento.textProperty(), control.complementoProperty());

    }

    public void alert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }

    public void clear(){
        tfNomeFantasia.setText("");
        tfCNPJ.setText("");
        tfLogradouro.setText("");
        tfCEP.setText("");
        tfNumero.setText("");
        tfCidade.setText("");
        tfBairro.setText("");
        tfComplemento.setText("");
        tfEmail1.setText("");
        tfTelefone1.setText("");
        tfEmail2.setText("");
        tfTelefone2.setText("");
        tfEmail3.setText("");
        tfTelefone3.setText("");
    }
}

