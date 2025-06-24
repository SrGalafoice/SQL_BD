package view.estoque;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TelaEstoqueHub extends Application {

    @Override
    public void start(Stage stage) {
        // Bot�es
        Button btnAdicionar = new Button("Adicionar Produto");
        Button btnConsultarEstoque = new Button("Consultar Prateleira");
        Button btnConsultarProduto = new Button("Consultar Produto");
        
        btnAdicionar.setOnAction(e -> {
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            TelaAdicionarProdutos add = new TelaAdicionarProdutos();
            try {
                add.start(stageAtual); // Reutiliza o Stage atual
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        btnConsultarEstoque.setOnAction(e -> {
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            TelaConsultarPrateleira consultaEst = new TelaConsultarPrateleira();
            try {
                consultaEst.start(stageAtual); // Reutiliza o Stage atual
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        btnConsultarProduto.setOnAction(e -> {
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            TelaConsultarProduto consultaProd = new TelaConsultarProduto();
            try {
                consultaProd.start(stageAtual); // Reutiliza o Stage atual
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        // Estilo opcional para simular os blocos maiores
        btnAdicionar.setPrefSize(180, 100);
        btnConsultarEstoque.setPrefSize(180, 100);
        btnConsultarProduto.setPrefSize(180, 100);

        // Layout
        HBox botoesBox = new HBox(30, btnAdicionar, btnConsultarEstoque, btnConsultarProduto);
        botoesBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(botoesBox);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);

        // Cena e palco
        Scene scene = new Scene(layout, 800, 400);
        stage.setTitle("Estoque - Hub");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
