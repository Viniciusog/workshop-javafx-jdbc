package gui;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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
import model.exceptions.ValidationException;
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
			notifyDataChangeListeners();
			sellerService.saveOrUpdate(entity); // Salva no banco de dados	
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		} catch (ValidationException e) {
			setErrorMessage(e.getErrors());
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

			ValidationException exception = new ValidationException("Validation error");

			obj.setId(Utils.tryParseToInt(txtId.getText()));

			if (txtName.getText() == null || txtName.getText().trim().equals("")) {
				exception.addErrors("name", "Field can't be empty");
			} else if (!txtName.getText().matches("^([A-Z][a-z]+\\s?)+$")) {
				exception.addErrors("name", "Enter a proper name");
			} else {
				obj.setName(txtName.getText().trim());
			}

			if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
				exception.addErrors("email", "Field can't be empty");
			} else if (!txtEmail.getText().trim()
					.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")) {
				exception.addErrors("email", "Invalid email address");
			} else {
				obj.setEmail(txtEmail.getText());
			}

			if (txtBirthDate.getText() == null || txtBirthDate.getText().trim().equals("")) {
				exception.addErrors("birthDate", "Field can't be empty");
			} else if (!txtBirthDate.getText()
					.matches("^([0-2][0-9]|3[0-1])/(0[0-9]|1[0-2])/" + "(19[0-9]{2}|20[0-1][0-9])$")) {
				exception.addErrors("birthDate", "Format must be: dd/mm/yyyy");
			} else {
				obj.setBirthDate(sdf.parse(txtBirthDate.getText()));
			}

			if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
				exception.addErrors("baseSalary", "Field can't be empty");
			} else {
				obj.setBaseSalary(Double.parseDouble(txtBaseSalary.getText()));
			}

			if (txtDepartmentId.getText() == null || txtDepartmentId.getText().trim().equals("")) {
				exception.addErrors("departmentId", "Field can't be empty");
			} else {
				Department department = new Department();
				department = departmentService.findById(Utils.tryParseToInt(txtDepartmentId.getText()));
				if (department == null) {
					exception.addErrors("departmentId", "There's no department with this id");
				}
				obj.setDepartment(department);
			}

			if (exception.getErrors().size() > 0) {
				throw exception;
			}

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

	private void setErrorMessage(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
		if (fields.contains("email")) {
			labelErrorEmail.setText(errors.get("email"));
		}
		if (fields.contains("birthDate")) {
			labelErrorBirthDate.setText(errors.get("birthDate"));
		}
		if (fields.contains("baseSalary")) {
			labelErrorBaseSalary.setText(errors.get("baseSalary"));
		}
		if (fields.contains("departmentId")) {
			labelErrorDepartmentId.setText(errors.get("departmentId"));
		}
	}
}