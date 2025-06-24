package view.contabilidade;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import view.caixa.TelaConsultarEmpresas;

public class TelaContabilidadeHub extends Application {

    @Override
    public void start(Stage stage) {
        // Criando os botoes diretamente
        Button btnRelatorioCompras = new Button("Relatorio Compras");
        Button btnVerificarEmpresas = new Button("Verificar Empresas");
        Button btnRelatorioVendas = new Button("Relatorio Vendas");
        
     // Estilo opcional para simular os blocos maiores
        btnRelatorioCompras.setPrefSize(180, 100);
        btnVerificarEmpresas.setPrefSize(180, 100);
        btnRelatorioVendas.setPrefSize(180, 100);

        // acoes dos botoes
        btnRelatorioCompras.setOnAction(e -> {
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            TelaRelatorioCompras compras = new TelaRelatorioCompras();
            try {
                compras.start(stageAtual); // Reutiliza o Stage atual
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        btnVerificarEmpresas.setOnAction(e -> {
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            TelaVerificarEmpresas vendas = new TelaVerificarEmpresas();
            try {
                vendas.start(stageAtual); // Abre a nova tela
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        btnRelatorioVendas.setOnAction(e -> {
            Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
            TelaRelatorioVendas consulta = new TelaRelatorioVendas();
            try {
                consulta.start(stageAtual); // Reutiliza o Stage atual
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Layout dos botões
        HBox hbox = new HBox(50, btnRelatorioCompras, btnVerificarEmpresas, btnRelatorioVendas);
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
