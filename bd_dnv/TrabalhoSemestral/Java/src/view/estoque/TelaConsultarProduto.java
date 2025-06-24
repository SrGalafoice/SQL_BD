package view.estoque;

import controller.PrateleiraControl;
import controller.ProdutoControl;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import model.Prateleira;
import model.Produto;
import Exception.ProdutoException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TelaConsultarProduto extends Application {
	private List<Produto> lista = new ArrayList<>();
	private ProdutoControl control = null;
	private ComboBox<String> cbProdutos = new ComboBox<>();
	private TextField tfIdProduto = new TextField();

	@Override
	public void start(Stage stage) {

		try {
			control = new ProdutoControl();
		} catch (ProdutoException e) {
			System.err.println("erro ao iniciar a control" + e.getMessage());
		}


		// TableView com 10 colunas (prateleiras)
		TableView<Produto> tableView = new TableView<>();
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // colunas se ajustam ao espa�o
		List<TableColumn<Produto, String>> colunas = new ArrayList<>();

		for (int i = 1; i <= 10; i++) {
			 final int index = i;
			TableColumn<Produto, String> prateleira = new TableColumn<>("Prateleira " + index);
			prateleira.setCellValueFactory(cellData -> {
						Produto produto = cellData.getValue();
				if (produto.getPrateleira() != null && produto.getPrateleira().getId() == index) {
					return new SimpleStringProperty(String.valueOf(produto.getQuantidade()));
				} else {
					return new SimpleStringProperty(""); // célula vazia se não está nessa prateleira
				}
			});
			tableView.getColumns().add(prateleira);
		}

		// Campos de pesquisa
		tfIdProduto.setPromptText("id produto");
		tfIdProduto.textProperty().addListener((obs, oldVal, newVal) -> {
			control.IDProperty().set(newVal);
		});

        try {
            control.pesquisarProdutos();
			lista = control.getLista();
        } catch (ProdutoException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

			for (Produto produto : lista){
				cbProdutos.getItems().addAll(produto.getNome());


			}
		cbProdutos.getItems().add("ID");
		cbProdutos.setPromptText("Produto"); // ser� preenchido com foreach externamente

		Button btnPesquisar = new Button("Pesquisar Produto");
		Button btnVoltar = new Button("Voltar");

		btnVoltar.setOnAction(e -> {
			Stage stageAtual = (Stage) ((Node) e.getSource()).getScene().getWindow();
			view.estoque.TelaEstoqueHub hub = new view.estoque.TelaEstoqueHub();
			try {
				hub.start(stageAtual);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		btnPesquisar.addEventFilter(ActionEvent.ANY,
				new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						try{
							generateBindings();
							control.pesquisarProdutos();

							tableView.setItems(control.getLista());
						} catch(ProdutoException | SQLException err) {
							alert("Erro ao pesquisar" + err.getMessage());
						}
					}
				});

		// Layout inferior
		HBox hboxInferior = new HBox(10);
		hboxInferior.getChildren().addAll(btnVoltar, tfIdProduto, new Label("OU"), cbProdutos, btnPesquisar);
		hboxInferior.setPadding(new Insets(10));

		// Layout geral
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(10));
		layout.getChildren().addAll(tableView, hboxInferior);
		VBox.setVgrow(tableView, Priority.ALWAYS); // TableView cresce com a tela


		// Cena
		Scene scene = new Scene(layout, 1000, 600);
		stage.setTitle("Tela Consultar Produto");
		stage.setScene(scene);
		stage.show();
	}

	public void generateBindings(){
		String produto = cbProdutos.getValue();
		if(produto != null && !produto.contains("ID")){
			control.IDProperty().set(produto);
		} else {
			Bindings.bindBidirectional(tfIdProduto.textProperty(), control.IDProperty());
		}


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
