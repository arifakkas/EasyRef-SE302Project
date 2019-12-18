package EasyRef;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Controller implements Initializable {

	@FXML
	private TableView<Reference> tableView;
	@FXML
	private TableColumn<Reference, String> typeColumn;
	@FXML
	private TableColumn<Reference, String> authorColumn;
	@FXML
	private TableColumn<Reference, String> titleColumn;
	@FXML
	private TableColumn<Reference, String> yearColumn;
	@FXML
	private TableColumn<Reference, String> monthColumn;
	@FXML
	private TableColumn<Reference, String> keyColumn;

	@FXML
	private TextField authorTextField;
	@FXML
	private TextField titleTextField;
	@FXML
	private TextField yearTextField;
	@FXML
	private TextField keyTextField;
	@FXML
	private TextField filterField;
	@FXML
	private TextField opt1;
	@FXML
	private TextField opt2;
	@FXML
	private TextField opt3;
	@FXML
	private TextField opt4;
	@FXML
	private TextField opt5;
	@FXML
	private TextField opt6;
	@FXML
	private TextField opt7;
	@FXML
	private TextField opt8;


	@FXML
	private ComboBox<String> typeField;
	ObservableList<String> typeFieldItems = FXCollections.observableArrayList();
	@FXML
	private ComboBox<String> monthField;
	ObservableList<String> monthFieldItems = FXCollections.observableArrayList();


	@FXML
	private Button addEntryButton;
	@FXML
	private Button deleteSelectedEntryButton;
	@FXML
	private Button importButton;

	@FXML
	private MenuBar fileMenu;

	ObservableList<Reference> refs = FXCollections.observableArrayList();


	@Override
	public void initialize(URL url, ResourceBundle rb) {

		filterField.textProperty().addListener(new ChangeListener() {
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				filterTableView((String) oldValue, (String) newValue);

			}
		});

		typeColumn.setCellValueFactory(new PropertyValueFactory<Reference, String>("type"));
		authorColumn.setCellValueFactory(new PropertyValueFactory<Reference, String>("author"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<Reference, String>("title"));
		yearColumn.setCellValueFactory(new PropertyValueFactory<Reference, String>("year"));
		monthColumn.setCellValueFactory(new PropertyValueFactory<Reference, String>("month"));
		keyColumn.setCellValueFactory(new PropertyValueFactory<Reference, String>("key"));


		authorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		yearColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		monthColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableView.setItems(refs);

		typeFieldItems.addAll("Article", "Book", "Conference", "Misc", "MastersThesis", "PhdThesis", "Proceedings");
		typeField.setItems(typeFieldItems);

		monthFieldItems.addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
		monthField.setItems(monthFieldItems);


		deleteSelectedEntryButton.setDisable(true);
		addEntryButton.setDisable(true);
		authorTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (authorTextField.isFocused()) {
				addEntryButton.setDisable(false);
			}
		});
		tableView.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (tableView.isFocused()) {
				deleteSelectedEntryButton.setDisable(false);
			}
		});

	}

	public void deleteButtonPushed(ActionEvent event) {
		if (!refs.isEmpty()) {
			System.out.println("Delete button clicked");
			Alert deleteAlert = new Alert(Alert.AlertType.WARNING, "Confirm", ButtonType.OK, ButtonType.CANCEL);
			Window owner = ((Node) event.getTarget()).getScene().getWindow();
			deleteAlert.setContentText("Are you sure you want to delete selected items?\n\nTHIS CANNOT BE UNDONE.");
			deleteAlert.initModality(Modality.APPLICATION_MODAL);
			deleteAlert.initOwner(owner);
			deleteAlert.showAndWait();
			if (deleteAlert.getResult() == ButtonType.OK) {
				refs.removeAll(tableView.getSelectionModel().getSelectedItems());
				tableView.getSelectionModel().clearSelection();
			} else {
				deleteAlert.close();
			}
		}
	}

	public void importRowsAsBibFile() {

		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save reference Table");
		fileChooser.getExtensionFilters().addAll(
			   new FileChooser.ExtensionFilter("Bib Files", "*.bib")
		);
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

		ObservableList<Reference> selectedItems = tableView.getSelectionModel().getSelectedItems();



		int counter = 0;

		for (Reference row : selectedItems) {

			for (Reference ref : refs) {

				if (ref.getKey().equals(row.getKey()) && ref.getAuthor().equals(row.getAuthor()) &&
					   ref.getMonth().equals(row.getMonth()) && ref.getTitle().equals(row.getTitle()) &&
					   ref.getType().equals(row.getType())) {

					counter++;
				}
			}
		}

		if (counter == 0) {

			Alert ioAlert = new Alert(Alert.AlertType.ERROR, "ERROR!", ButtonType.OK);
			ioAlert.setContentText("You did not select any file, please select file(s) before importing");
			ioAlert.showAndWait();
			if (ioAlert.getResult() == ButtonType.OK) {
				ioAlert.close();
			}

		} else {

			File file = fileChooser.showSaveDialog(stage);

			for (Reference row : selectedItems) {

				for (Reference ref : refs) {


					if (ref.getKey().equals(row.getKey()) && ref.getAuthor().equals(row.getAuthor()) &&
						   ref.getMonth().equals(row.getMonth()) && ref.getTitle().equals(row.getTitle()) &&
						   ref.getType().equals(row.getType())) {

						String temp = "";

						if (row.getType().equals("Article")) {

							Article articleRow = (Article) row;

							if (!articleRow.getKey().equals("null")) {

								temp = "@" + "article" + "{" + articleRow.getKey() + "," + "\n";
							}
							if (!articleRow.getTitle().equals("null")) {
								temp = temp + "title={" + articleRow.getTitle() + "}," + "\n";
							}
							if (!articleRow.getAuthor().equals("null")) {
								temp = temp + "author={" + articleRow.getAuthor() + "}," + "\n";
							}
							if (!articleRow.getMonth().equals("null")) {
								temp = temp + "month={" + articleRow.getMonth() + "}," + "\n";
							}
							if (!articleRow.getYear().equals("null")) {
								temp = temp + "year={" + articleRow.getYear() + "}," + "\n";
							}
							if (!articleRow.getVolume().equals("null")) {
								temp = temp + "volume={" + articleRow.getVolume() + "}" + "," + "\n";
							}
							if (!articleRow.getNumber().equals("null")) {
								temp = temp + "number={" + articleRow.getNumber() + "}" + "," + "\n";
							}
							if (!articleRow.getPages().equals("null")) {
								temp = temp + "pages={" + articleRow.getPages() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + ",";
						} else if (row.getType().equals("Book")) {

							Book bookRow = (Book) row;
							if (!bookRow.getKey().equals("null")) {
								temp = "@" + "book" + "{" + bookRow.getKey() + "," + "\n";
							}
							if (!bookRow.getTitle().equals("null")) {
								temp = temp + "title={" + bookRow.getTitle() + "}," + "\n";
							}
							if (!bookRow.getAuthor().equals("null")) {
								temp = temp + "author={" + bookRow.getAuthor() + "}," + "\n";
							}
							if (!bookRow.getMonth().equals("null")) {
								temp = temp + "month={" + bookRow.getMonth() + "}," + "\n";
							}
							if (!bookRow.getYear().equals("null")) {
								temp = temp + "year={" + bookRow.getYear() + "}," + "\n";
							}
							if (!bookRow.getPublisher().equals("null")) {
								temp = temp + "publisher={" + bookRow.getVolume() + "}" + "," + "\n";
							}
							if (!bookRow.getVolume().equals("null")) {
								temp = temp + "volume={" + bookRow.getVolume() + "}" + "," + "\n";
							}
							if (!bookRow.getSeries().equals("null")) {
								temp = temp + "series={" + bookRow.getSeries() + "}" + "," + "\n";
							}
							if (!bookRow.getAddress().equals("null")) {
								temp = temp + "address={" + bookRow.getAddress() + "}" + "," + "\n";
							}
							if (!bookRow.getEdition().equals("null")) {
								temp = temp + "edition={" + bookRow.getEdition() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + ",";
						} else if (row.getType().equals("Conference")) {

							Conference conferenceRow = (Conference) row;
							if (!conferenceRow.getKey().equals("null")) {
								temp = "@" + "conference" + "{" + conferenceRow.getKey() + "," + "\n";
							}
							if (!conferenceRow.getTitle().equals("null")) {
								temp = temp + "title={" + conferenceRow.getTitle() + "}," + "\n";
							}
							if (!conferenceRow.getAuthor().equals("null")) {
								temp = temp + "author={" + conferenceRow.getAuthor() + "}," + "\n";
							}
							if (!conferenceRow.getMonth().equals("null")) {
								temp = temp + "month={" + conferenceRow.getMonth() + "}," + "\n";
							}
							if (!conferenceRow.getYear().equals("null")) {
								temp = temp + "year={" + conferenceRow.getYear() + "}," + "\n";
							}
							if (!conferenceRow.getPublisher().equals("null")) {
								temp = temp + "publisher={" + conferenceRow.getVolume() + "}" + "," + "\n";
							}
							if (!conferenceRow.getVolume().equals("null")) {
								temp = temp + "volume={" + conferenceRow.getVolume() + "}" + "," + "\n";
							}
							if (!conferenceRow.getSeries().equals("null")) {
								temp = temp + "series={" + conferenceRow.getSeries() + "}" + "," + "\n";
							}
							if (!conferenceRow.getAddress().equals("null")) {
								temp = temp + "address={" + conferenceRow.getAddress() + "}" + "," + "\n";
							}
							if (!conferenceRow.getEdition().equals("null")) {
								temp = temp + "edition={" + conferenceRow.getEdition() + "}" + "," + "\n";
							}
							if (!conferenceRow.getPages().equals("null")) {
								temp = temp + "pages={" + conferenceRow.getPages() + "}" + "," + "\n";
							}
							if (!conferenceRow.getBookTitle().equals("null")) {
								temp = temp + "booktitle={" + conferenceRow.getBookTitle() + "}" + "," + "\n";
							}
							if (!conferenceRow.getOrganization().equals("null")) {
								temp = temp + "organization={" + conferenceRow.getOrganization() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + ",";
						} else if (row.getType().equals("MastersThesis")) {

							MastersThesis mastersThesisRow = (MastersThesis) row;
							if (!mastersThesisRow.getKey().equals("null")) {
								temp = "@" + "mastersthesis" + "{" + mastersThesisRow.getKey() + "," + "\n";
							}
							if (!mastersThesisRow.getTitle().equals("null")) {
								temp = temp + "title={" + mastersThesisRow.getTitle() + "}," + "\n";
							}
							if (!mastersThesisRow.getAuthor().equals("null")) {
								temp = temp + "author={" + mastersThesisRow.getAuthor() + "}," + "\n";
							}
							if (!mastersThesisRow.getMonth().equals("null")) {
								temp = temp + "month={" + mastersThesisRow.getMonth() + "}," + "\n";
							}
							if (!mastersThesisRow.getYear().equals("null")) {
								temp = temp + "year={" + mastersThesisRow.getYear() + "}," + "\n";
							}
							if (!mastersThesisRow.getSchool().equals("null")) {
								temp = temp + "school={" + mastersThesisRow.getSchool() + "}" + "," + "\n";
							}
							if (!mastersThesisRow.getAddress().equals("null")) {
								temp = temp + "address={" + mastersThesisRow.getAddress() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + ",";
						} else if (row.getType().equals("Misc")) {

							Misc miscRow = (Misc) row;
							if (!miscRow.getKey().equals("null")) {
								temp = "@" + "misc" + "{" + miscRow.getKey() + "," + "\n";
							}
							if (!miscRow.getTitle().equals("null")) {
								temp = temp + "title={" + miscRow.getTitle() + "}," + "\n";
							}
							if (!miscRow.getAuthor().equals("null")) {
								temp = temp + "author={" + miscRow.getAuthor() + "}," + "\n";
							}
							if (!miscRow.getMonth().equals("null")) {
								temp = temp + "month={" + miscRow.getMonth() + "}," + "\n";
							}
							if (!miscRow.getYear().equals("null")) {
								temp = temp + "year={" + miscRow.getYear() + "}," + "\n";
							}
							if (!miscRow.getHowPublished().equals("null")) {
								temp = temp + "howpublished={" + miscRow.getHowPublished() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + ",";
						} else if (row.getType().equals("PhdThesis")) {

							PhdThesis phdThesisRow = (PhdThesis) row;
							if (!phdThesisRow.getKey().equals("null")) {
								temp = "@" + "phdthesis" + "{" + phdThesisRow.getKey() + "," + "\n";
							}
							if (!phdThesisRow.getTitle().equals("null")) {
								temp = temp + "title={" + phdThesisRow.getTitle() + "}," + "\n";
							}
							if (!phdThesisRow.getAuthor().equals("null")) {
								temp = temp + "author={" + phdThesisRow.getAuthor() + "}," + "\n";
							}
							if (!phdThesisRow.getMonth().equals("null")) {
								temp = temp + "month={" + phdThesisRow.getMonth() + "}," + "\n";
							}
							if (!phdThesisRow.getYear().equals("null")) {
								temp = temp + "year={" + phdThesisRow.getYear() + "}," + "\n";
							}
							if (!phdThesisRow.getSchool().equals("null")) {
								temp = temp + "school={" + phdThesisRow.getSchool() + "}" + "," + "\n";
							}
							if (!phdThesisRow.getAddress().equals("null")) {
								temp = temp + "address={" + phdThesisRow.getAddress() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + ",";
						} else if (row.getType().equals("Proceedings")) {

							Proceedings proceedingsRow = (Proceedings) row;
							if (!proceedingsRow.getKey().equals("null")) {
								temp = "@" + "proceedings" + "{" + proceedingsRow.getKey() + "," + "\n";
							}
							if (!proceedingsRow.getTitle().equals("null")) {
								temp = temp + "title={" + proceedingsRow.getTitle() + "}," + "\n";
							}
							if (!proceedingsRow.getAuthor().equals("null")) {
								temp = temp + "author={" + proceedingsRow.getAuthor() + "}," + "\n";
							}
							if (!proceedingsRow.getMonth().equals("null")) {
								temp = temp + "month={" + proceedingsRow.getMonth() + "}," + "\n";
							}
							if (!proceedingsRow.getYear().equals("null")) {
								temp = temp + "year={" + proceedingsRow.getYear() + "}," + "\n";
							}
							if (!proceedingsRow.getPublisher().equals("null")) {
								temp = temp + "publisher={" + proceedingsRow.getPublisher() + "}" + "," + "\n";
							}
							if (!proceedingsRow.getVolume().equals("null")) {
								temp = temp + "volume={" + proceedingsRow.getVolume() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + ",";
						}
						counter--;
						if (counter == 0) {
							temp = temp.substring(0, temp.length() - 1);
						}

						if (file != null) {
							importASbib(temp, file);
						}

					}
				}
			}

		}


	}

	public void ClearButtonPushed(ActionEvent event) {
		keyTextField.clear();
		authorTextField.clear();
		yearTextField.clear();
		titleTextField.clear();
		typeField.setValue("Type*");
		monthField.setValue("Month*");
		opt1.clear();
		opt2.clear();
		opt3.clear();
		opt4.clear();
		opt5.clear();
		opt6.clear();
		opt7.clear();
		opt8.clear();

	}

	public void AddEntryButtonPushed(ActionEvent event) {
		if (isValidInput(event)) {

			if (typeField.getValue().equals("Article")) {
				Article newRef = new Article(typeField.getValue(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText());
				refs.add(newRef);

			} else if (typeField.getValue().equals("Book")) {
				Book newRef = new Book(typeField.getValue(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText(), opt4.getText(), opt5.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("Conference")) {
				Conference newRef = new Conference(typeField.getValue(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText(), opt4.getText(), opt5.getText(), opt6.getText(), opt7.getText(),
					   opt8.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("MastersThesis")) {
				MastersThesis newRef = new MastersThesis(typeField.getValue(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("Misc")) {
				Misc newRef = new Misc(typeField.getValue(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("PhdThesis")) {
				PhdThesis newRef = new PhdThesis(typeField.getValue(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("Proceedings")) {
				Proceedings newRef = new Proceedings(typeField.getValue(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText(), opt4.getText(), opt5.getText(), opt6.getText());
				refs.add(newRef);
			}

			keyTextField.clear();
			authorTextField.clear();
			yearTextField.clear();
			titleTextField.clear();
			typeField.cancelEdit();
			monthField.cancelEdit();
			opt1.clear();
			opt2.clear();
			opt3.clear();
			opt4.clear();
			opt5.clear();
			opt6.clear();
			opt7.clear();
			opt8.clear();
		}


	}

	public void changeType(ActionEvent event) {


		String typeName = typeField.getValue();
		if (typeName.equals("Article")) {
			opt1.setPromptText("Volume");
			opt2.setPromptText("Number");
			opt3.setPromptText("Pages");
			opt4.setVisible(false);
			opt5.setVisible(false);
			opt6.setVisible(false);
			opt7.setVisible(false);
			opt8.setVisible(false);
			opt1.setVisible(true);
			opt2.setVisible(true);
			opt3.setVisible(true);
		} else if (typeName.equals("Book")) {
			opt1.setPromptText("Publisher");
			opt2.setPromptText("Volume");
			opt3.setPromptText("Series");
			opt4.setPromptText("Address");
			opt5.setPromptText("Edition");
			opt6.setVisible(false);
			opt7.setVisible(false);
			opt8.setVisible(false);
			opt1.setVisible(true);
			opt2.setVisible(true);
			opt3.setVisible(true);
			opt4.setVisible(true);
			opt5.setVisible(true);
		} else if (typeName.equals("Conference")) {
			opt1.setPromptText("Publisher");
			opt2.setPromptText("Volume");
			opt3.setPromptText("Series");
			opt4.setPromptText("Address");
			opt5.setPromptText("Edition");
			opt6.setPromptText("Pages");
			opt7.setPromptText("Book Title");
			opt8.setPromptText("Organization");
			opt1.setVisible(true);
			opt2.setVisible(true);
			opt3.setVisible(true);
			opt4.setVisible(true);
			opt5.setVisible(true);
			opt6.setVisible(true);
			opt7.setVisible(true);
			opt8.setVisible(true);
		} else if (typeName.equals("MastersThesis")) {
			opt1.setPromptText("School");
			opt2.setPromptText("Thesis Type");
			opt3.setPromptText("Address");
			opt4.setVisible(false);
			opt5.setVisible(false);
			opt6.setVisible(false);
			opt7.setVisible(false);
			opt8.setVisible(false);
			opt1.setVisible(true);
			opt2.setVisible(true);
			opt3.setVisible(true);
		} else if (typeName.equals("Misc")) {
			opt1.setPromptText("How Published");
			opt2.setVisible(false);
			opt3.setVisible(false);
			opt4.setVisible(false);
			opt5.setVisible(false);
			opt6.setVisible(false);
			opt7.setVisible(false);
			opt8.setVisible(false);
			opt1.setVisible(true);
		} else if (typeName.equals("PhdThesis")) {
			opt1.setPromptText("School");
			opt2.setPromptText("Thesis Type");
			opt3.setPromptText("Address");
			opt4.setVisible(false);
			opt5.setVisible(false);
			opt6.setVisible(false);
			opt7.setVisible(false);
			opt8.setVisible(false);
			opt1.setVisible(true);
			opt2.setVisible(true);
			opt3.setVisible(true);
		} else if (typeName.equals("Proceedings")) {
			opt1.setPromptText("Publisher");
			opt2.setPromptText("Volume");
			opt3.setPromptText("Series");
			opt4.setPromptText("Address");
			opt5.setPromptText("Editor");
			opt6.setPromptText("Organization");
			opt7.setVisible(false);
			opt8.setVisible(false);
			opt1.setVisible(true);
			opt2.setVisible(true);
			opt3.setVisible(true);
			opt4.setVisible(true);
			opt5.setVisible(true);
		}

	}


	public void changeAuthorCellEvent(TableColumn.CellEditEvent edittedCell) {
		Reference refSelected = tableView.getSelectionModel().getSelectedItem();
		refSelected.setAuthor(edittedCell.getNewValue().toString());
	}

	public void changeTitleCellEvent(TableColumn.CellEditEvent edittedCell) {
		Reference refSelected = tableView.getSelectionModel().getSelectedItem();
		refSelected.setTitle(edittedCell.getNewValue().toString());
	}

	public void changeYearCellEvent(TableColumn.CellEditEvent edittedCell) {
		Reference refSelected = tableView.getSelectionModel().getSelectedItem();
		refSelected.setYear(edittedCell.getNewValue().toString());
	}

	public void changeMonthCellEvent(TableColumn.CellEditEvent edittedCell) {
		Reference refSelected = tableView.getSelectionModel().getSelectedItem();
		refSelected.setMonth(edittedCell.getNewValue().toString());
	}

	public void filterTableView(String oldValue, String newValue) {
		ObservableList<Reference> filteredList = FXCollections.observableArrayList();
		if (filterField == null || (newValue.length() < oldValue.length()) || newValue == null) {
			tableView.setItems(refs);
		} else {
			newValue = newValue.toUpperCase();
			for (Reference ref : tableView.getItems()) {
				String author = ref.getAuthor();
				String title = ref.getTitle();
				String year = ref.getYear();
				String key = ref.getKey();
				String month = ref.getMonth();
				String type = ref.getType();
				if (author.toUpperCase().contains(newValue) || title.toUpperCase().contains(newValue) || year.toUpperCase().contains(newValue) || type.toUpperCase().contains(newValue) || key.toUpperCase().contains(newValue) || month.toUpperCase().contains(newValue)) {
					filteredList.add(ref);
				}
			}
			tableView.setItems(filteredList);
		}
	}

	private boolean isValidInput(ActionEvent event) {

		boolean validInput = true;

		if (authorTextField == null || authorTextField.getText().trim().isEmpty()) {
			validInput = false;
			Alert alert = new Alert(Alert.AlertType.WARNING, "Warning", ButtonType.OK);
			Window owner = ((Node) event.getTarget()).getScene().getWindow();
			alert.setContentText("author is EMPTY");
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner(owner);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
				authorTextField.requestFocus();
			}
		}
		if (titleTextField == null || titleTextField.getText().trim().isEmpty()) {
			validInput = false;
			Alert alert = new Alert(Alert.AlertType.WARNING, "Warning", ButtonType.OK);
			Window owner = ((Node) event.getTarget()).getScene().getWindow();
			alert.setContentText("title field is EMPTY");
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner(owner);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
				titleTextField.requestFocus();
			}
		}
		if (yearTextField == null || yearTextField.getText().trim().isEmpty()) {
			validInput = false;
			Alert alert = new Alert(Alert.AlertType.WARNING, "Warning", ButtonType.OK);
			Window owner = ((Node) event.getTarget()).getScene().getWindow();
			alert.setContentText("year field is EMPTY");
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner(owner);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
				yearTextField.requestFocus();
			}
		}
		if (keyTextField == null || keyTextField.getText().trim().isEmpty()) {
			validInput = false;
			Alert alert = new Alert(Alert.AlertType.WARNING, "Warning", ButtonType.OK);
			Window owner = ((Node) event.getTarget()).getScene().getWindow();
			alert.setContentText("key field is EMPTY");
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner(owner);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
				keyTextField.requestFocus();
			}
		}

		if (typeField == null || typeField.getValue().isEmpty()) {
			validInput = false;
			Alert alert = new Alert(Alert.AlertType.WARNING, "Warning", ButtonType.OK);
			Window owner = ((Node) event.getTarget()).getScene().getWindow();
			alert.setContentText("type is not chosen");
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner(owner);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
				typeField.requestFocus();
			}
		}
		if (monthField == null || monthField.getValue().trim().isEmpty()) {
			validInput = false;
			Alert emptyMonth = new Alert(Alert.AlertType.WARNING, "Warning", ButtonType.OK);
			Window owner = ((Node) event.getTarget()).getScene().getWindow();
			emptyMonth.setContentText("month is not chosen");
			emptyMonth.initModality(Modality.APPLICATION_MODAL);
			emptyMonth.initOwner(owner);
			emptyMonth.showAndWait();
			if (emptyMonth.getResult() == ButtonType.OK) {
				emptyMonth.close();
				monthField.requestFocus();
			}
		}
		return validInput;


	}

	public void handleSave(ActionEvent event) {
		Stage secondaryStage = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save reference Table");
		fileChooser.getExtensionFilters().addAll(
			   new FileChooser.ExtensionFilter("Bib Files", "*.bib"),
			   new FileChooser.ExtensionFilter("Txt Files", "*.txt")
		);
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		if (refs.isEmpty()) {
			secondaryStage.initOwner(this.fileMenu.getScene().getWindow());
			Alert emptyTableAlert = new Alert(Alert.AlertType.ERROR, "EMPTY TABLE", ButtonType.OK);
			emptyTableAlert.setContentText("You have nothing to save");
			emptyTableAlert.initModality(Modality.APPLICATION_MODAL);
			emptyTableAlert.initOwner(this.fileMenu.getScene().getWindow());
			emptyTableAlert.showAndWait();
			if (emptyTableAlert.getResult() == ButtonType.OK) {
				emptyTableAlert.close();
			}
		} else {
			File file = fileChooser.showSaveDialog(secondaryStage);

			if (file != null) {
				saveFile(tableView.getItems(), file);
			}
		}
	}

	public void importASbib(String temporary, File file) {
		try {
			BufferedWriter outWriter = new BufferedWriter(new FileWriter(file));

			outWriter.write(temporary);
			outWriter.newLine();

			System.out.println(temporary);
			outWriter.close();
		} catch (IOException e) {
			Alert ioAlert = new Alert(Alert.AlertType.ERROR, "OOPS!", ButtonType.OK);
			ioAlert.setContentText("Sorry. An error has occurred.");
			ioAlert.showAndWait();
			if (ioAlert.getResult() == ButtonType.OK) {
				ioAlert.close();
			}
		}
	}

	public void saveFile(ObservableList<Reference> references, File file) {
		try {
			BufferedWriter outWriter = new BufferedWriter(new FileWriter(file));

			for (Reference reference : references) {
				outWriter.write(reference.toString());
				outWriter.newLine();
			}
			System.out.println(references.toString());
			outWriter.close();
		} catch (IOException e) {
			Alert ioAlert = new Alert(Alert.AlertType.ERROR, "OOPS!", ButtonType.OK);
			ioAlert.setContentText("Sorry. An error has occurred.");
			ioAlert.showAndWait();
			if (ioAlert.getResult() == ButtonType.OK) {
				ioAlert.close();
			}
		}
	}

	public void closeApp(ActionEvent event) {
		Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Confirm", ButtonType.OK, ButtonType.CANCEL);
		Stage stage = (Stage) fileMenu.getScene().getWindow();
		exitAlert.setContentText("Are you sure you want to exit?");
		exitAlert.initModality(Modality.APPLICATION_MODAL);
		exitAlert.initOwner(stage);
		exitAlert.showAndWait();

		if (exitAlert.getResult() == ButtonType.OK) {
			Platform.exit();
		} else {
			exitAlert.close();
		}
	}


}
