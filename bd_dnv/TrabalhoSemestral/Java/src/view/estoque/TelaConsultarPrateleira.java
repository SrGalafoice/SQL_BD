package view.estoque;

import controller.PrateleiraControl;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import model.Produto;

import java.util.ArrayList;
import java.util.List;

public class TelaConsultarPrateleira extends Application {
	private PrateleiraControl control = null;
	private ComboBox<String> cbPrateleira = new ComboBox<>();
	private TableView<Produto> tableView = new TableView<>();
	private int espacos = 0;
	private List<Produto> lista = new ArrayList<>();
	@Override
	public void start(Stage stage) {
		// TableView com 3 colunas
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<Produto, Integer> colIdProduto = new TableColumn<>("ID produto");
		colIdProduto.setCellValueFactory( new PropertyValueFactory<>("ID"));
		TableColumn<Produto, String> colNomeProduto = new TableColumn<>("Nome Produto");
		colNomeProduto.setCellValueFactory( new PropertyValueFactory<>("nome"));
		TableColumn<Produto, Integer> colQuantidade = new TableColumn<>("Quantidade");
		colQuantidade.setCellValueFactory( new PropertyValueFactory<>("quantidade"));

		tableView.setItems(control.getLista());
		tableView.getColumns().addAll(colIdProduto, colNomeProduto, colQuantidade);

		// Parte inferior da tela

		cbPrateleira.setPromptText("Selecione Prateleira");
		for (int i = 1; i <= 10; i++) {
			cbPrateleira.getItems().add("Prateleira " + i);
		}
		lista = control.getLista();
		espacos = lista.stream().mapToInt(Produto::getQuantidade).sum() - 100;
		Label lblCorredor = new Label("Corredor: 1");
		Label lblEspacos = new Label("espa�os vazios: " + espacos);

		Button btnInspecionar = new Button("Inspecionar produtos");

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

		btnInspecionar.addEventFilter(ActionEvent.ANY,
				new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						try{
							generateBindings();
							control.consultarPrateleira();
							tableView.setItems(control.getLista());
							lblEspacos.setText("espacos vazios: " + (100 - (lista.stream().mapToInt(Produto::getQuantidade).sum())));
						} catch(Exception err) {
							alert("Erro ao pesquisar: " + err.getMessage());
						}
					}
				});

		// Layout inferior
		HBox hboxInferior = new HBox(15);
		hboxInferior.setAlignment(Pos.CENTER_LEFT);
		hboxInferior.setPadding(new Insets(10));
		Region espacoMeio = new Region();
		HBox.setHgrow(espacoMeio, Priority.ALWAYS);
		hboxInferior.getChildren().addAll(btnVoltar, cbPrateleira, lblCorredor, espacoMeio, lblEspacos, btnInspecionar);

		// Layout principal
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(10));
		layout.getChildren().addAll(tableView, hboxInferior);
		VBox.setVgrow(tableView, Priority.ALWAYS);

		Scene scene = new Scene(layout, 1000, 600);
		stage.setTitle("Tela Consultar Prateleira");
		stage.setScene(scene);
		stage.show();


	}

	public TelaConsultarPrateleira(){
		try{
			control = new PrateleiraControl();
		} catch (Exception e){
			System.err.println("erro ao iniciar a control" + e.getMessage());
		}
	}

	public static void alert(String msg) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.show();
	}

	public void generateBindings() {
		String prateleira = cbPrateleira.getValue();
		String numero = prateleira.replace("Prateleira ", "");
		control.IDProperty().set(Integer.parseInt(numero));

	}


	public static void main(String[] args) {
		launch(args);
	}
}
