package view.caixa;

//Arquivo: TelaCadastroEmpresa.java

import controller.EmpresaControl;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import Exception.EmpresaException;
import model.Empresa;

public class TelaConsultarEmpresas extends Application {
    private EmpresaControl control = null;
    private TextField tfnomeEmpresa = new TextField();
    private TextField tfCnpj = new TextField();

 @Override
 public void start(Stage primaryStage) {

     try{
         control = new EmpresaControl();
     }catch (EmpresaException e){
        System.err.println("Erro ao inciar a control");
     }
     // Tabela de empresas
     TableView<Empresa> tabela = new TableView<>();
     TableColumn<Empresa, String> colEmpresa = new TableColumn<>("Empresa");
     colEmpresa.setCellValueFactory(new PropertyValueFactory<>("nomeEmpresa"));
     TableColumn<Empresa, String> colCNPJ = new TableColumn<>("CNPJ");
     colCNPJ.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
     TableColumn<Empresa, String> colCEP = new TableColumn<>("Cep");
     colCEP.setCellValueFactory(new PropertyValueFactory<>("CEP"));
     tabela.getColumns().addAll(colEmpresa, colCNPJ, colCEP);

     control.pesquisarEmpresas();
     tabela.setItems(control.getLista());
     tabela.setPrefWidth(600);

     tabela.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
         if (newSelection != null) {
             tfnomeEmpresa.setText(newSelection.getNomeEmpresa());
             tfCnpj.setText(newSelection.getCnpj());
         }
     });
     // �rea lateral com campos e bot�es
     VBox painelLateral = new VBox(10);
     painelLateral.setPrefWidth(300);


     Label labelEmpresa = new Label("Empresa");
     tfnomeEmpresa.setPromptText("*nome");

     Label labelCnpj = new Label("CNPJ");

     tfCnpj.setPromptText("*cnpj");

     Button btnCadastrar = new Button("Cadastrar Alguma Empresa");
     btnCadastrar.setOnAction(e -> {
    	 Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
         TelaCadastrarEmpresas cadastrar = new TelaCadastrarEmpresas();
         try {
             cadastrar.start(stageAtual);
         } catch (Exception ex) {
             ex.printStackTrace();
         }
     });
     Button btnAdicionarAoCarrinho = new Button("Adicionar empresa no carrinho");
     btnAdicionarAoCarrinho.setOnAction(e -> {
         Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
         Empresa empresa = new Empresa();
         empresa.setCnpj(tfCnpj.getText());
         try{
             TelaCaixa caixa = new TelaCaixa();
             caixa.setEmpresa(empresa);
             caixa.start(stageAtual);
         } catch (EmpresaException ex){
             alert("Erro ao adicionar empresa ao caixa" + ex.getMessage());
         }
     });
     Button btnVoltar = new Button("Voltar");
     btnVoltar.setOnAction(e -> {
         Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
         TelaCaixa telaCaixa = new TelaCaixa();
         try {
             telaCaixa.start(stageAtual); 
         } catch (Exception ex) {
             ex.printStackTrace();
         }
     });


     painelLateral.getChildren().addAll(
             labelEmpresa, tfnomeEmpresa,
             labelCnpj, tfCnpj,
             btnCadastrar,
             btnAdicionarAoCarrinho,
             btnVoltar
     );

     // Layout principal
     HBox layoutPrincipal = new HBox(10);
     layoutPrincipal.getChildren().addAll(tabela, painelLateral);

     Scene scene = new Scene(layoutPrincipal, 900, 500);
     primaryStage.setTitle("Cadastro de Empresa");
     primaryStage.setScene(scene);
     primaryStage.show();
 }
    public static void alert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }

}

