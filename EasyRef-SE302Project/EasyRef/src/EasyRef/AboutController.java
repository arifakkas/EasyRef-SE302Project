package EasyRef;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

	@FXML
	private Button closeButton;


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {



	}
	public void closeAbout(ActionEvent event){
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();

	}


}
