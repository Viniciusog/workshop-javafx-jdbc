package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SellerFormController implements Initializable{
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
	private Label labelErrorDepartmentName;
	
	@FXML
	public void onBtSaveAction() {
		System.out.println("onBtSaveAction");
		
	}
	
	@FXML
	public void onBtCancelAction() {
		System.out.println("onBtCancelAction");
		
	}
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
		Constraints.setTextFieldMaxLength(txtBirthDate, 30);
		Constraints.setTextFieldMaxLength(txtEmail, 20);
		Constraints.setTextFieldMaxLength(txtBaseSalary, 20);
	}

}
