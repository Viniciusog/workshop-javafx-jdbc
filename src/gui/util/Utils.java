package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	//Class 254
	//Acessar o Stage onde o controller que recebeu o evento (event) está
	//Por exemplo se clicar no botão, vamos receber o stage daquele botão
	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

}
