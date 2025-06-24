package view.principal;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import view.caixa.TelaCaixa;
import view.caixa.TelaConsultarEmpresas;
import view.contabilidade.TelaContabilidadeHub;
import view.estoque.TelaEstoqueHub;

public class Inicio extends Application {

    @Override
    public void start(Stage stage) {
        // Criando os botoes diretamente
        Button btnCaixa = new Button("Caixa");
        Button btnContabilidade = new Button("Sistema Contabilidade");
        Button btnEstoque = new Button("Sistema Estoque");
        
     // Estilo opcional para simular os blocos maiores
        btnCaixa.setPrefSize(180, 100);
        btnContabilidade.setPrefSize(180, 100);
        btnEstoque.setPrefSize(180, 100);

        // acoes dos botoes
        btnCaixa.setOnAction(e -> {
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            TelaCaixa caixa = new TelaCaixa();
            try {
                caixa.start(stageAtual); // Reutiliza o Stage atual
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        btnContabilidade.setOnAction(e -> {
        	 Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
             TelaContabilidadeHub contabilidade = new TelaContabilidadeHub();
             try {
                 contabilidade.start(stageAtual); // Reutiliza o Stage atual
             } catch (Exception ex) {
                 ex.printStackTrace();
            }
        });
        
        btnEstoque.setOnAction(e -> {
        	Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            TelaEstoqueHub estoque = new TelaEstoqueHub();
            try {
                estoque.start(stageAtual); // Abre a nova tela
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Layout dos botões
        HBox hbox = new HBox(50,btnCaixa,btnContabilidade ,btnEstoque );
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(50));

        // Layout principal (root)
        StackPane root = new StackPane(hbox);
        Scene scene = new Scene(root, 800, 400);

        stage.setTitle("Contabilidade Hub");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
