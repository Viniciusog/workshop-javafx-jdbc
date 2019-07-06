package gui;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable {

	private SellerService service;

	@FXML
	TableView<Seller> tableViewSeller;

	@FXML
	private TableColumn<Seller, Integer> tableColumnId;

	@FXML
	private TableColumn<Seller, String> tableColumnName;

	@FXML
	private TableColumn<Seller, String> tableColumnEmail;

	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;

	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;

	@FXML
	private TableColumn<Seller, Department> tableColumnDepartmentName;
	
	@FXML
	private Button btNew;

	private ObservableList<Seller> obsList;

	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller obj = new Seller();
		createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
	}

	public void setSellerService(SellerService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		initDepartmentNameColumn();
		initBirthDateColumn();

		Stage stage = (Stage) Main.getMainScene().getWindow();

		// Table view Acompanhar a altura da janela
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("service was null");
		}

		List<Seller> list = service.findAll();
		obsList = FXCollections.observableList(list);
		tableViewSeller.setItems(obsList);
	}

	private void initDepartmentNameColumn() {
		tableColumnDepartmentName.setCellValueFactory(new PropertyValueFactory<>("department"));
		tableColumnDepartmentName.setCellFactory(param -> new TableCell<>() {

			@Override
			protected void updateItem(Department department, boolean b) {
				super.updateItem(department, b);
				if (department == null) {
					setText(null);
				} else {
					//Formatando o valor do department para aparecer apenas o nome
					setText(department.getName());
				}
			}
		});
	}

	private void initBirthDateColumn() {
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		tableColumnBirthDate.setCellFactory(param -> new TableCell<>() {

			private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");

			@Override
			protected void updateItem(Date date, boolean b) {
				if (date == null) {
					setText(null);
				} else {
					//Formatando a data em Dia, Mês e Ano
					setText(sdf.format(date));
				}
			}
		});
	}
	
	private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			SellerFormController controller = loader.getController();
			controller.setSeller(obj);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Seller Data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
		} catch(IOException e ) {
			Alerts.showAlert("IO Exception", null, e.getMessage(), AlertType.ERROR);
			
		}
	}
}