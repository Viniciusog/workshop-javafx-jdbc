package gui;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.entities.Seller;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtBirthDate;

	@FXML
	private TextField txtBaseSalary;

	@FXML
	private TextField txtDepartmentId;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorBirthDate;

	@FXML
	private Label labelErrorBaseSalary;

	@FXML
	private Label labelErrorDepartmentId;

	private Seller entity;
	private SellerService sellerService;
	private DepartmentService departmentService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("entity was null");
		}
		if (sellerService == null) {
			throw new IllegalStateException("sellerService was null");
		}
		if (departmentService == null) {
			throw new IllegalStateException("departmentService was null");
		}
		try {
			entity = getFormData();
			sellerService.saveOrUpdate(entity); // Salva no banco de dados
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {

		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	// Método responsável por pegar os dados do Seller nos campos de texto e
	// instanciar um Seller
	private Seller getFormData() {
		Seller obj = new Seller();
		try {
			obj.setId(Utils.tryParseToInt(txtId.getText()));
			obj.setName(txtName.getText());
			obj.setEmail(txtEmail.getText());
			obj.setBirthDate(sdf.parse(txtBirthDate.getText()));
			obj.setBaseSalary(Double.parseDouble(txtBaseSalary.getText()));

			Department department = new Department();
			department = departmentService.findById(Utils.tryParseToInt(txtDepartmentId.getText()));
			obj.setDepartment(department);

		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return obj;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
		Constraints.setTextFieldMaxLength(txtEmail, 20);
		Constraints.setTextFieldMaxLength(txtBaseSalary, 20);
	}

	public void updateFormData() {

		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());

		if (entity.getBirthDate() != null) {
			txtBirthDate.setText(sdf.format(entity.getBirthDate()));
		}

		if (entity.getBaseSalary() != null) {
			txtBaseSalary.setText(String.valueOf(entity.getBaseSalary()));
		} else {
			txtBaseSalary.setText("");
		}
		if (entity.getDepartment() != null) {
			txtDepartmentId.setText(String.valueOf(entity.getDepartment().getId()));
		}
	}
}