package EasyRef;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {

	Controller controller = new Controller();


	@Override
	public void start(Stage primaryStage) throws Exception{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("sample.fxml"));
		Parent root = loader.load();
		primaryStage.setResizable(false);
		primaryStage.setTitle("EasyRef");
		primaryStage.setScene(new Scene(root, 748, 600));
		primaryStage.show();
		controller = loader.getController();

		Platform.setImplicitExit(false);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {

				Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Confirm", ButtonType.OK, ButtonType.CANCEL);
				Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION, "Confirm", ButtonType.YES, ButtonType.NO,ButtonType.CANCEL);
				exitAlert.setContentText("Are you sure you want to exit?");
				exitAlert.initModality(Modality.APPLICATION_MODAL);
				exitAlert.initOwner(primaryStage);

				saveAlert.setContentText("There are unsaved changes, Do you want to save them before exiting?");
				saveAlert.initModality(Modality.APPLICATION_MODAL);
				saveAlert.initOwner(primaryStage);
				boolean flag = controller.getFlag();

				if (flag){
					saveAlert.showAndWait();
					if (saveAlert.getResult() == ButtonType.YES) {
						controller.SaveOnMain();
						Platform.exit();
					} else if (saveAlert.getResult() == ButtonType.NO){
						Platform.exit();

					}else{
						we.consume();
						saveAlert.close();}
				}else {
					exitAlert.showAndWait();
					if (exitAlert.getResult() == ButtonType.OK) {
						Platform.exit();
					} else {
						we.consume();

						exitAlert.close();
					}
				}
			}

	   });
	}


public static void main(String[] args) {
	   launch(args);
	   }
	   }
