package EasyRef;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
	private TextField opt9;
	@FXML
	private TextField opt10;
	@FXML
	private TextField opt11;

	@FXML
	private ComboBox<String> typeField;
	ObservableList<String> typeFieldItems = FXCollections.observableArrayList();
	@FXML
	private ComboBox<String> monthField;
	ObservableList<String> monthFieldItems = FXCollections.observableArrayList();
	boolean saveFlag = false;


	@FXML
	private Button addEntryButton;
	@FXML
	private Button deleteSelectedEntryButton;
	@FXML
	private Button exportButton;
	@FXML
	private Button AdvancedSearch;

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

		typeFieldItems.addAll("Article", "Book","Booklet", "Conference","inBook","inCollection",
			   "inProceedings", "Manual", "Misc", "MastersThesis", "Online", "PhdThesis", "Proceedings",
			   "Standard", "TechReport", "unPublished");
		typeField.setItems(typeFieldItems);

		monthFieldItems.addAll("January", "February", "March", "April", "May", "June", "July", "August",
			   "September", "October", "November", "December");
		monthField.setItems(monthFieldItems);


		deleteSelectedEntryButton.setDisable(true);
		addEntryButton.setDisable(true);
		authorTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (authorTextField.isFocused()) {
				addEntryButton.setDisable(false);
			}
		});
		tableView.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (tableView.getSelectionModel().getSelectedItems().size() != 0) {
				deleteSelectedEntryButton.setDisable(false);
			}
			else
				deleteSelectedEntryButton.setDisable(true);
		});
		exportButton.setDisable(true);
		tableView.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (tableView.getSelectionModel().getSelectedItems().size() != 0) {
				exportButton.setDisable(false);
			}
			else
				exportButton.setDisable(true);
		});

		AdvancedSearch.disableProperty().bind(Bindings.size(refs).isEqualTo(0));
		filterField.disableProperty().bind(Bindings.size(refs).isEqualTo(0));




		keyTextField.setVisible(false);
		authorTextField.setVisible(false);
		titleTextField.setVisible(false);
		yearTextField.setVisible(false);
		monthField.setVisible(false);
		opt1.setVisible(false);
		opt2.setVisible(false);
		opt3.setVisible(false);
		opt4.setVisible(false);
		opt5.setVisible(false);
		opt6.setVisible(false);
		opt7.setVisible(false);
		opt8.setVisible(false);
		opt9.setVisible(false);
		opt10.setVisible(false);
		opt11.setVisible(false);


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

	public void exportRowsAsBibFile() {

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

			String temp = "";

			File file = fileChooser.showSaveDialog(stage);

			for (Reference row : selectedItems) {

				for (Reference ref : refs) {


					if (ref.getKey().equals(row.getKey()) && ref.getAuthor().equals(row.getAuthor()) &&
						   ref.getMonth().equals(row.getMonth()) && ref.getTitle().equals(row.getTitle()) &&
						   ref.getType().equals(row.getType())) {


						if (row.getType().equals("article")) {

							Article articleRow = (Article) row;

							if (!articleRow.getKey().equals("")) {

								temp = temp + "@" + "article" + "{" + articleRow.getKey() + "," + "\n";
							}
							if (!articleRow.getTitle().equals("")) {
								temp = temp + "title={" + articleRow.getTitle() + "}," + "\n";
							}
							if (!articleRow.getAuthor().equals("")) {
								temp = temp + "author={" + articleRow.getAuthor() + "}," + "\n";
							}
							if (!articleRow.getMonth().equals("")) {
								temp = temp + "month={" + articleRow.getMonth() + "}," + "\n";
							}
							if (!articleRow.getYear().equals("")) {
								temp = temp + "year={" + articleRow.getYear() + "}," + "\n";
							}
							if (!articleRow.getVolume().equals("")) {
								temp = temp + "volume={" + articleRow.getVolume() + "}" + "," + "\n";
							}
							if (!articleRow.getNumber().equals("")) {
								temp = temp + "number={" + articleRow.getNumber() + "}" + "," + "\n";
							}
							if (!articleRow.getPages().equals("")) {
								temp = temp + "pages={" + articleRow.getPages() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + "," + "\n";
						}if (row.getType().equals("book")) {

							Book bookRow = (Book) row;
							if (!bookRow.getKey().equals("")) {
								temp = temp + "@" + "book" + "{" + bookRow.getKey() + "," + "\n";
							}
							if (!bookRow.getTitle().equals("")) {
								temp = temp + "title={" + bookRow.getTitle() + "}," + "\n";
							}
							if (!bookRow.getAuthor().equals("")) {
								temp = temp + "author={" + bookRow.getAuthor() + "}," + "\n";
							}
							if (!bookRow.getMonth().equals("")) {
								temp = temp + "month={" + bookRow.getMonth() + "}," + "\n";
							}
							if (!bookRow.getYear().equals("")) {
								temp = temp + "year={" + bookRow.getYear() + "}," + "\n";
							}
							if (!bookRow.getPublisher().equals("")) {
								temp = temp + "publisher={" + bookRow.getVolume() + "}" + "," + "\n";
							}
							if (!bookRow.getVolume().equals("")) {
								temp = temp + "volume={" + bookRow.getVolume() + "}" + "," + "\n";
							}
							if (!bookRow.getSeries().equals("")) {
								temp = temp + "series={" + bookRow.getSeries() + "}" + "," + "\n";
							}
							if (!bookRow.getAddress().equals("")) {
								temp = temp + "address={" + bookRow.getAddress() + "}" + "," + "\n";
							}
							if (!bookRow.getEdition().equals("")) {
								temp = temp + "edition={" + bookRow.getEdition() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + "," + "\n";
						}if (row.getType().equals("booklet")) {

							Booklet bookletRow = (Booklet) row;
							if (!bookletRow.getKey().equals("")) {
								temp = temp + "@" + "booklet" + "{" + bookletRow.getKey() + "," + "\n";
							}
							if (!bookletRow.getTitle().equals("")) {
								temp = temp + "title={" + bookletRow.getTitle() + "}," + "\n";
							}
							if (!bookletRow.getAuthor().equals("")) {
								temp = temp + "author={" + bookletRow.getAuthor() + "}," + "\n";
							}
							if (!bookletRow.getMonth().equals("")) {
								temp = temp + "month={" + bookletRow.getMonth() + "}," + "\n";
							}
							if (!bookletRow.getYear().equals("")) {
								temp = temp + "year={" + bookletRow.getYear() + "}," + "\n";
							}
							if (!bookletRow.getHowPublished().equals("")) {
								temp = temp + "howpublished={" + bookletRow.getHowPublished() + "}" + "," + "\n";
							}
							if (!bookletRow.getAddress().equals("")) {
								temp = temp + "address={" + bookletRow.getAddress() + "}" + "," + "\n";
							}
							if (!bookletRow.getNote().equals("")) {
								temp = temp + "note={" + bookletRow.getNote() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + "," + "\n";
						}if (row.getType().equals("conference")) {

							Conference conferenceRow = (Conference) row;
							if (!conferenceRow.getKey().equals("")) {
								temp = temp + "@" + "conference" + "{" + conferenceRow.getKey() + "," + "\n";
							}
							if (!conferenceRow.getTitle().equals("")) {
								temp = temp + "title={" + conferenceRow.getTitle() + "}," + "\n";
							}
							if (!conferenceRow.getAuthor().equals("")) {
								temp = temp + "author={" + conferenceRow.getAuthor() + "}," + "\n";
							}
							if (!conferenceRow.getMonth().equals("")) {
								temp = temp + "month={" + conferenceRow.getMonth() + "}," + "\n";
							}
							if (!conferenceRow.getYear().equals("")) {
								temp = temp + "year={" + conferenceRow.getYear() + "}," + "\n";
							}
							if (!conferenceRow.getPublisher().equals("")) {
								temp = temp + "publisher={" + conferenceRow.getVolume() + "}" + "," + "\n";
							}
							if (!conferenceRow.getVolume().equals("")) {
								temp = temp + "volume={" + conferenceRow.getVolume() + "}" + "," + "\n";
							}
							if (!conferenceRow.getSeries().equals("")) {
								temp = temp + "series={" + conferenceRow.getSeries() + "}" + "," + "\n";
							}
							if (!conferenceRow.getAddress().equals("")) {
								temp = temp + "address={" + conferenceRow.getAddress() + "}" + "," + "\n";
							}
							if (!conferenceRow.getEdition().equals("")) {
								temp = temp + "edition={" + conferenceRow.getEdition() + "}" + "," + "\n";
							}
							if (!conferenceRow.getPages().equals("")) {
								temp = temp + "pages={" + conferenceRow.getPages() + "}" + "," + "\n";
							}
							if (!conferenceRow.getBookTitle().equals("")) {
								temp = temp + "booktitle={" + conferenceRow.getBookTitle() + "}" + "," + "\n";
							}
							if (!conferenceRow.getOrganization().equals("")) {
								temp = temp + "organization={" + conferenceRow.getOrganization() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + "," + "\n";
						}if (row.getType().equals("inbook")) {

							inBook inbookRow = (inBook) row;
							if (!inbookRow.getKey().equals("")) {
								temp = temp + "@" + "inbook" + "{" + inbookRow.getKey() + "," + "\n";
							}
							if (!inbookRow.getTitle().equals("")) {
								temp = temp + "title={" + inbookRow.getTitle() + "}," + "\n";
							}
							if (!inbookRow.getAuthor().equals("")) {
								temp = temp + "author={" + inbookRow.getAuthor() + "}," + "\n";
							}
							if (!inbookRow.getMonth().equals("")) {
								temp = temp + "month={" + inbookRow.getMonth() + "}," + "\n";
							}
							if (!inbookRow.getYear().equals("")) {
								temp = temp + "year={" + inbookRow.getYear() + "}," + "\n";
							}
							if (!inbookRow.getPublisher().equals("")) {
								temp = temp + "publisher={" + inbookRow.getVolume() + "}" + "," + "\n";
							}
							if (!inbookRow.getVolume().equals("")) {
								temp = temp + "volume={" + inbookRow.getVolume() + "}" + "," + "\n";
							}
							if (!inbookRow.getSeries().equals("")) {
								temp = temp + "series={" + inbookRow.getSeries() + "}" + "," + "\n";
							}
							if (!inbookRow.getAddress().equals("")) {
								temp = temp + "address={" + inbookRow.getAddress() + "}" + "," + "\n";
							}
							if (!inbookRow.getEdition().equals("")) {
								temp = temp + "edition={" + inbookRow.getEdition() + "}" + "," + "\n";
							}
							if (!inbookRow.getOrganization().equals("")) {
								temp = temp + "organization={" + inbookRow.getOrganization() + "}" + "," + "\n";
							}
							if (!inbookRow.getChapter().equals("")) {
								temp = temp + "chapter={" + inbookRow.getChapter() + "}" + "," + "\n";
							}
							if (!inbookRow.getNote().equals("")) {
								temp = temp + "note={" + inbookRow.getNote() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + "," + "\n";
						}if (row.getType().equals("incollection")) {

							inCollection incollectionRow = (inCollection) row;
							if (!incollectionRow.getKey().equals("")) {
								temp = temp + "@" + "incollection" + "{" + incollectionRow.getKey() + "," + "\n";
							}
							if (!incollectionRow.getTitle().equals("")) {
								temp = temp + "title={" + incollectionRow.getTitle() + "}," + "\n";
							}
							if (!incollectionRow.getAuthor().equals("")) {
								temp = temp + "author={" + incollectionRow.getAuthor() + "}," + "\n";
							}
							if (!incollectionRow.getMonth().equals("")) {
								temp = temp + "month={" + incollectionRow.getMonth() + "}," + "\n";
							}
							if (!incollectionRow.getYear().equals("")) {
								temp = temp + "year={" + incollectionRow.getYear() + "}," + "\n";
							}
							if (!incollectionRow.getPublisher().equals("")) {
								temp = temp + "publisher={" + incollectionRow.getVolume() + "}" + "," + "\n";
							}
							if (!incollectionRow.getVolume().equals("")) {
								temp = temp + "volume={" + incollectionRow.getVolume() + "}" + "," + "\n";
							}
							if (!incollectionRow.getSeries().equals("")) {
								temp = temp + "series={" + incollectionRow.getSeries() + "}" + "," + "\n";
							}
							if (!incollectionRow.getAddress().equals("")) {
								temp = temp + "address={" + incollectionRow.getAddress() + "}" + "," + "\n";
							}
							if (!incollectionRow.getEdition().equals("")) {
								temp = temp + "edition={" + incollectionRow.getEdition() + "}" + "," + "\n";
							}
							if (!incollectionRow.getPages().equals("")) {
								temp = temp + "pages={" + incollectionRow.getPages() + "}" + "," + "\n";
							}
							if (!incollectionRow.getBookTitle().equals("")) {
								temp = temp + "booktitle={" + incollectionRow.getBookTitle() + "}" + "," + "\n";
							}
							if (!incollectionRow.getOrganization().equals("")) {
								temp = temp + "organization={" + incollectionRow.getOrganization() + "}" + "," + "\n";
							}
							if (!incollectionRow.getOrganization().equals("")) {
								temp = temp + "editor={" + incollectionRow.getEditor() + "}" + "," + "\n";
							}
							if (!incollectionRow.getOrganization().equals("")) {
								temp = temp + "chapter={" + incollectionRow.getChapter() + "}" + "," + "\n";
							}
							if (!incollectionRow.getOrganization().equals("")) {
								temp = temp + "note={" + incollectionRow.getNote() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + "," + "\n";
						}if (row.getType().equals("inproceedings")) {

							inProceedings inproceedingRow = (inProceedings) row;
							if (!inproceedingRow.getKey().equals("")) {
								temp = temp + "@" + "inproceedings" + "{" + inproceedingRow.getKey() + "," + "\n";
							}
							if (!inproceedingRow.getTitle().equals("")) {
								temp = temp + "title={" + inproceedingRow.getTitle() + "}," + "\n";
							}
							if (!inproceedingRow.getAuthor().equals("")) {
								temp = temp + "author={" + inproceedingRow.getAuthor() + "}," + "\n";
							}
							if (!inproceedingRow.getMonth().equals("")) {
								temp = temp + "month={" + inproceedingRow.getMonth() + "}," + "\n";
							}
							if (!inproceedingRow.getYear().equals("")) {
								temp = temp + "year={" + inproceedingRow.getYear() + "}," + "\n";
							}
							if (!inproceedingRow.getPublisher().equals("")) {
								temp = temp + "publisher={" + inproceedingRow.getVolume() + "}" + "," + "\n";
							}
							if (!inproceedingRow.getVolume().equals("")) {
								temp = temp + "volume={" + inproceedingRow.getVolume() + "}" + "," + "\n";
							}
							if (!inproceedingRow.getSeries().equals("")) {
								temp = temp + "series={" + inproceedingRow.getSeries() + "}" + "," + "\n";
							}
							if (!inproceedingRow.getAddress().equals("")) {
								temp = temp + "address={" + inproceedingRow.getAddress() + "}" + "," + "\n";
							}
							if (!inproceedingRow.getEdition().equals("")) {
								temp = temp + "edition={" + inproceedingRow.getEdition() + "}" + "," + "\n";
							}
							if (!inproceedingRow.getPages().equals("")) {
								temp = temp + "pages={" + inproceedingRow.getPages() + "}" + "," + "\n";
							}
							if (!inproceedingRow.getBookTitle().equals("")) {
								temp = temp + "booktitle={" + inproceedingRow.getBookTitle() + "}" + "," + "\n";
							}
							if (!inproceedingRow.getOrganization().equals("")) {
								temp = temp + "organization={" + inproceedingRow.getOrganization() + "}" + "," + "\n";
							}
							if (!inproceedingRow.getOrganization().equals("")) {
								temp = temp + "editor={" + inproceedingRow.getEditor() + "}" + "," + "\n";
							}
							if (!inproceedingRow.getOrganization().equals("")) {
								temp = temp + "chapter={" + inproceedingRow.getChapter() + "}" + "," + "\n";
							}
							if (!inproceedingRow.getOrganization().equals("")) {
								temp = temp + "note={" + inproceedingRow.getNote() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + "," + "\n";
						}if (row.getType().equals("manual")) {

							Manual manualRow = (Manual) row;
							if (!manualRow.getKey().equals("")) {
								temp = temp + "@" + "manual" + "{" + manualRow.getKey() + "," + "\n";
							}
							if (!manualRow.getTitle().equals("")) {
								temp = temp + "title={" + manualRow.getTitle() + "}," + "\n";
							}
							if (!manualRow.getAuthor().equals("")) {
								temp = temp + "author={" + manualRow.getAuthor() + "}," + "\n";
							}
							if (!manualRow.getMonth().equals("")) {
								temp = temp + "month={" + manualRow.getMonth() + "}," + "\n";
							}
							if (!manualRow.getYear().equals("")) {
								temp = temp + "year={" + manualRow.getYear() + "}," + "\n";
							}
							if (!manualRow.getAddress().equals("")) {
								temp = temp + "address={" + manualRow.getAddress() + "}" + "," + "\n";
							}
							if (!manualRow.getEdition().equals("")) {
								temp = temp + "edition={" + manualRow.getEdition() + "}" + "," + "\n";
							}
							if (!manualRow.getOrganization().equals("")) {
								temp = temp + "organization={" + manualRow.getOrganization() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + "," + "\n";
						}if (row.getType().equals("mastersthesis")) {

							MastersThesis mastersThesisRow = (MastersThesis) row;
							if (!mastersThesisRow.getKey().equals("")) {
								temp = temp + "@" + "mastersthesis" + "{" + mastersThesisRow.getKey() + "," + "\n";
							}
							if (!mastersThesisRow.getTitle().equals("")) {
								temp = temp + "title={" + mastersThesisRow.getTitle() + "}," + "\n";
							}
							if (!mastersThesisRow.getAuthor().equals("")) {
								temp = temp + "author={" + mastersThesisRow.getAuthor() + "}," + "\n";
							}
							if (!mastersThesisRow.getMonth().equals("")) {
								temp = temp + "month={" + mastersThesisRow.getMonth() + "}," + "\n";
							}
							if (!mastersThesisRow.getYear().equals("")) {
								temp = temp + "year={" + mastersThesisRow.getYear() + "}," + "\n";
							}
							if (!mastersThesisRow.getSchool().equals("")) {
								temp = temp + "school={" + mastersThesisRow.getSchool() + "}" + "," + "\n";
							}
							if (!mastersThesisRow.getAddress().equals("")) {
								temp = temp + "address={" + mastersThesisRow.getAddress() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + "," + "\n";
						}if (row.getType().equals("misc")) {

							Misc miscRow = (Misc) row;
							if (!miscRow.getKey().equals("")) {
								temp = temp + "@" + "misc" + "{" + miscRow.getKey() + "," + "\n";
							}
							if (!miscRow.getTitle().equals("")) {
								temp = temp + "title={" + miscRow.getTitle() + "}," + "\n";
							}
							if (!miscRow.getAuthor().equals("")) {
								temp = temp + "author={" + miscRow.getAuthor() + "}," + "\n";
							}
							if (!miscRow.getMonth().equals("")) {
								temp = temp + "month={" + miscRow.getMonth() + "}," + "\n";
							}
							if (!miscRow.getYear().equals("")) {
								temp = temp + "year={" + miscRow.getYear() + "}," + "\n";
							}
							if (!miscRow.getHowPublished().equals("")) {
								temp = temp + "howpublished={" + miscRow.getHowPublished() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + "," + "\n";
						}if (row.getType().equals("online")) {

							Online onlineRow = (Online) row;
							if (!onlineRow.getKey().equals("")) {
								temp = temp + "@" + "online" + "{" + onlineRow.getKey() + "," + "\n";
							}
							if (!onlineRow.getTitle().equals("")) {
								temp = temp + "title={" + onlineRow.getTitle() + "}," + "\n";
							}
							if (!onlineRow.getAuthor().equals("")) {
								temp = temp + "author={" + onlineRow.getAuthor() + "}," + "\n";
							}
							if (!onlineRow.getMonth().equals("")) {
								temp = temp + "month={" + onlineRow.getMonth() + "}," + "\n";
							}
							if (!onlineRow.getYear().equals("")) {
								temp = temp + "year={" + onlineRow.getYear() + "}," + "\n";
							}
							if (!onlineRow.getUrl().equals("")) {
								temp = temp + "url={" + onlineRow.getUrl() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + "," + "\n";
						}if (row.getType().equals("phdthesis")) {

							PhdThesis phdThesisRow = (PhdThesis) row;
							if (!phdThesisRow.getKey().equals("")) {
								temp = temp + "@" + "phdthesis" + "{" + phdThesisRow.getKey() + "," + "\n";
							}
							if (!phdThesisRow.getTitle().equals("")) {
								temp = temp + "title={" + phdThesisRow.getTitle() + "}," + "\n";
							}
							if (!phdThesisRow.getAuthor().equals("")) {
								temp = temp + "author={" + phdThesisRow.getAuthor() + "}," + "\n";
							}
							if (!phdThesisRow.getMonth().equals("")) {
								temp = temp + "month={" + phdThesisRow.getMonth() + "}," + "\n";
							}
							if (!phdThesisRow.getYear().equals("")) {
								temp = temp + "year={" + phdThesisRow.getYear() + "}," + "\n";
							}
							if (!phdThesisRow.getSchool().equals("")) {
								temp = temp + "school={" + phdThesisRow.getSchool() + "}" + "," + "\n";
							}
							if (!phdThesisRow.getAddress().equals("")) {
								temp = temp + "address={" + phdThesisRow.getAddress() + "}" + "," + "\n";
							}
							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + "," + "\n";
						}if (row.getType().equals("proceedings")) {

							Proceedings proceedingsRow = (Proceedings) row;
							if (!proceedingsRow.getKey().equals("")) {
								temp = temp + "@" + "proceedings" + "{" + proceedingsRow.getKey() + "," + "\n";
							}
							if (!proceedingsRow.getTitle().equals("")) {
								temp = temp + "title={" + proceedingsRow.getTitle() + "}," + "\n";
							}
							if (!proceedingsRow.getAuthor().equals("")) {
								temp = temp + "author={" + proceedingsRow.getAuthor() + "}," + "\n";
							}
							if (!proceedingsRow.getMonth().equals("")) {
								temp = temp + "month={" + proceedingsRow.getMonth() + "}," + "\n";
							}
							if (!proceedingsRow.getYear().equals("")) {
								temp = temp + "year={" + proceedingsRow.getYear() + "}," + "\n";
							}
							if (!proceedingsRow.getPublisher().equals("")) {
								temp = temp + "publisher={" + proceedingsRow.getPublisher() + "}" + "," + "\n";
							}
							if (!proceedingsRow.getVolume().equals("")) {
								temp = temp + "volume={" + proceedingsRow.getVolume() + "}" + "," + "\n";
							}

							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + "," + "\n";
						}if (row.getType().equals("standard")) {

							Standard standardRow = (Standard) row;
							if (!standardRow.getKey().equals("")) {
								temp = temp + "@" + "standard" + "{" + standardRow.getKey() + "," + "\n";
							}
							if (!standardRow.getTitle().equals("")) {
								temp = temp + "title={" + standardRow.getTitle() + "}," + "\n";
							}
							if (!standardRow.getAuthor().equals("")) {
								temp = temp + "author={" + standardRow.getAuthor() + "}," + "\n";
							}
							if (!standardRow.getMonth().equals("")) {
								temp = temp + "month={" + standardRow.getMonth() + "}," + "\n";
							}
							if (!standardRow.getYear().equals("")) {
								temp = temp + "year={" + standardRow.getYear() + "}," + "\n";
							}
							if (!standardRow.getOrganization().equals("")) {
								temp = temp + "organization={" + standardRow.getOrganization() + "}" + "," + "\n";
							}
							if (!standardRow.getInstitution().equals("")) {
								temp = temp + "institution={" + standardRow.getInstitution() + "}" + "," + "\n";
							}
							if (!standardRow.getLanguage().equals("")) {
								temp = temp + "language={" + standardRow.getLanguage() + "}" + "," + "\n";
							}
							if (!standardRow.getHowpublished().equals("")) {
								temp = temp + "howpublished={" + standardRow.getHowpublished() + "}" + "," + "\n";
							}
							if (!standardRow.getNumber().equals("")) {
								temp = temp + "number={" + standardRow.getNumber() + "}" + "," + "\n";
							}
							if (!standardRow.getRevision().equals("")) {
								temp = temp + "revision={" + standardRow.getRevision() + "}" + "," + "\n";
							}
							if (!standardRow.getAddress().equals("")) {
								temp = temp + "address={" + standardRow.getAddress() + "}" + "," + "\n";
							}
							if (!standardRow.getUrl().equals("")) {
								temp = temp + "url={" + standardRow.getUrl() + "}" + "," + "\n";
							}

							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + "," + "\n";
						}if (row.getType().equals("techreport")) {

							TechReport techreportRow = (TechReport) row;
							if (!techreportRow.getKey().equals("")) {
								temp = temp + "@" + "techreport" + "{" + techreportRow.getKey() + "," + "\n";
							}
							if (!techreportRow.getTitle().equals("")) {
								temp = temp + "title={" + techreportRow.getTitle() + "}," + "\n";
							}
							if (!techreportRow.getAuthor().equals("")) {
								temp = temp + "author={" + techreportRow.getAuthor() + "}," + "\n";
							}
							if (!techreportRow.getMonth().equals("")) {
								temp = temp + "month={" + techreportRow.getMonth() + "}," + "\n";
							}
							if (!techreportRow.getYear().equals("")) {
								temp = temp + "year={" + techreportRow.getYear() + "}," + "\n";
							}
							if (!techreportRow.getAddress().equals("")) {
								temp = temp + "address={" + techreportRow.getAddress() + "}" + "," + "\n";
							}
							if (!techreportRow.getInstitution().equals("")) {
								temp = temp + "institution={" + techreportRow.getInstitution() + "}" + "," + "\n";
							}
							if (!techreportRow.getNumber().equals("")) {
								temp = temp + "number={" + techreportRow.getNumber() + "}" + "," + "\n";
							}

							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + "," + "\n";
						}if (row.getType().equals("unpublished")) {

							unPublished unpublishedRow = (unPublished) row;
							if (!unpublishedRow.getKey().equals("")) {
								temp = temp + "@" + "unpublished" + "{" + unpublishedRow.getKey() + "," + "\n";
							}
							if (!unpublishedRow.getTitle().equals("")) {
								temp = temp + "title={" + unpublishedRow.getTitle() + "}," + "\n";
							}
							if (!unpublishedRow.getAuthor().equals("")) {
								temp = temp + "author={" + unpublishedRow.getAuthor() + "}," + "\n";
							}
							if (!unpublishedRow.getMonth().equals("")) {
								temp = temp + "month={" + unpublishedRow.getMonth() + "}," + "\n";
							}
							if (!unpublishedRow.getYear().equals("")) {
								temp = temp + "year={" + unpublishedRow.getYear() + "}," + "\n";
							}

							temp = temp.substring(0, temp.length() - 2);
							temp = temp + "\n" + "}" + "," + "\n";
						}
						counter--;
						if (counter == 0) {
							temp = temp.substring(0, temp.length() - 2);
						}

						if (file != null) {
							exportAsBib(temp, file);
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
		opt9.clear();
		opt10.clear();
		opt11.clear();

	}

	public void AddEntryButtonPushed(ActionEvent event) {
		if (isValidInput(event)) {

			if (typeField.getValue().equals("Article")) {
				Article newRef = new Article(typeField.getValue().toLowerCase(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText());
				refs.add(newRef);

			} else if (typeField.getValue().equals("Book")) {
				Book newRef = new Book(typeField.getValue().toLowerCase(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText(), opt4.getText(), opt5.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("Booklet")) {
				Booklet newRef = new Booklet(typeField.getValue().toLowerCase(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("Conference")) {
				Conference newRef = new Conference(typeField.getValue().toLowerCase(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText(), opt4.getText(), opt5.getText(), opt6.getText(), opt7.getText(),
					   opt8.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("inBook")) {
				Proceedings newRef = new Proceedings(typeField.getValue().toLowerCase(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText(), opt4.getText(), opt5.getText(), opt6.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("inCollection")) {
				inCollection newRef = new inCollection(typeField.getValue().toLowerCase(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText(), opt4.getText(), opt5.getText(), opt6.getText(), opt7.getText(),
					   opt8.getText(), opt9.getText(),opt10.getText(), opt11.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("inProceedings")) {
				inProceedings newRef = new inProceedings(typeField.getValue().toLowerCase(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText(), opt4.getText(), opt5.getText(), opt6.getText(), opt7.getText(),
					   opt8.getText(), opt9.getText(),opt10.getText(), opt11.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("Manual")) {
				Manual newRef = new Manual(typeField.getValue().toLowerCase(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("MastersThesis")) {
				MastersThesis newRef = new MastersThesis(typeField.getValue().toLowerCase(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("Misc")) {
				Misc newRef = new Misc(typeField.getValue().toLowerCase(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("Online")) {
				Online newRef = new Online(typeField.getValue().toLowerCase(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("PhdThesis")) {
				PhdThesis newRef = new PhdThesis(typeField.getValue().toLowerCase(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("Proceedings")) {
				Proceedings newRef = new Proceedings(typeField.getValue().toLowerCase(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText(), opt4.getText(), opt5.getText(), opt6.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("Standard")) {
				Standard newRef = new Standard(typeField.getValue().toLowerCase(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText(), opt4.getText(), opt5.getText(), opt6.getText(), opt7.getText(), opt8.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("TechReport")) {
				TechReport newRef = new TechReport(typeField.getValue().toLowerCase(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue(), opt1.getText(),
					   opt2.getText(), opt3.getText());
				refs.add(newRef);
			} else if (typeField.getValue().equals("unPublished")) {
				unPublished newRef = new unPublished(typeField.getValue().toLowerCase(), titleTextField.getText(), authorTextField.getText(),
					   authorTextField.getText(), yearTextField.getText(), monthField.getValue());
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
			opt9.setVisible(false);
			opt10.setVisible(false);
			opt11.setVisible(false);
			opt1.setVisible(true);
			opt2.setVisible(true);
			opt3.setVisible(true);
			keyTextField.setVisible(true);
			authorTextField.setVisible(true);
			titleTextField.setVisible(true);
			yearTextField.setVisible(true);
			monthField.setVisible(true);
		}else if (typeName.equals("Book")) {
			opt1.setPromptText("Publisher");
			opt2.setPromptText("Volume");
			opt3.setPromptText("Series");
			opt4.setPromptText("Address");
			opt5.setPromptText("Edition");
			opt6.setVisible(false);
			opt7.setVisible(false);
			opt8.setVisible(false);
			opt9.setVisible(false);
			opt10.setVisible(false);
			opt11.setVisible(false);
			opt1.setVisible(true);
			opt2.setVisible(true);
			opt3.setVisible(true);
			opt4.setVisible(true);
			opt5.setVisible(true);
			keyTextField.setVisible(true);
			authorTextField.setVisible(true);
			titleTextField.setVisible(true);
			yearTextField.setVisible(true);
			monthField.setVisible(true);
		}else if (typeName.equals("Booklet")){
			opt1.setPromptText("How Published");
			opt2.setPromptText("Address");
			opt3.setPromptText("Note");
			opt4.setVisible(false);
			opt5.setVisible(false);
			opt6.setVisible(false);
			opt7.setVisible(false);
			opt8.setVisible(false);
			opt9.setVisible(false);
			opt10.setVisible(false);
			opt11.setVisible(false);
			opt1.setVisible(true);
			opt2.setVisible(true);
			opt3.setVisible(true);
			keyTextField.setVisible(true);
			authorTextField.setVisible(true);
			titleTextField.setVisible(true);
			yearTextField.setVisible(true);
			monthField.setVisible(true);
		}else if (typeName.equals("Conference")) {
			opt1.setPromptText("Publisher");
			opt2.setPromptText("Volume");
			opt3.setPromptText("Series");
			opt4.setPromptText("Address");
			opt5.setPromptText("Edition");
			opt6.setPromptText("Pages");
			opt7.setPromptText("Book Title");
			opt8.setPromptText("Organization");
			opt9.setVisible(false);
			opt10.setVisible(false);
			opt11.setVisible(false);
			opt1.setVisible(true);
			opt2.setVisible(true);
			opt3.setVisible(true);
			opt4.setVisible(true);
			opt5.setVisible(true);
			opt6.setVisible(true);
			opt7.setVisible(true);
			opt8.setVisible(true);
			keyTextField.setVisible(true);
			authorTextField.setVisible(true);
			titleTextField.setVisible(true);
			yearTextField.setVisible(true);
			monthField.setVisible(true);
		}else if (typeName.equals("inBook")){
			opt1.setPromptText("Publisher");
			opt2.setPromptText("Volume");
			opt3.setPromptText("Series");
			opt4.setPromptText("Address");
			opt5.setPromptText("Edition");
			opt6.setPromptText("Organization");
			opt7.setPromptText("Chapter");
			opt8.setPromptText("Note");
			opt9.setVisible(false);
			opt10.setVisible(false);
			opt11.setVisible(false);
			opt1.setVisible(true);
			opt2.setVisible(true);
			opt3.setVisible(true);
			opt4.setVisible(true);
			opt5.setVisible(true);
			opt6.setVisible(true);
			opt7.setVisible(true);
			opt8.setVisible(true);
			keyTextField.setVisible(true);
			authorTextField.setVisible(true);
			titleTextField.setVisible(true);
			yearTextField.setVisible(true);
			monthField.setVisible(true);
		}else if (typeName.equals("inCollection")){
			opt1.setPromptText("Publisher");
			opt2.setPromptText("Volume");
			opt3.setPromptText("Series");
			opt4.setPromptText("Address");
			opt5.setPromptText("Edition");
			opt6.setPromptText("Pages");
			opt7.setPromptText("Book Title");
			opt8.setPromptText("Organization");
			opt9.setPromptText("Editor");
			opt10.setPromptText("Chapter");
			opt11.setPromptText("Note");
			opt1.setVisible(true);
			opt2.setVisible(true);
			opt3.setVisible(true);
			opt4.setVisible(true);
			opt5.setVisible(true);
			opt6.setVisible(true);
			opt7.setVisible(true);
			opt8.setVisible(true);
			opt9.setVisible(true);
			opt10.setVisible(true);
			opt11.setVisible(true);
			keyTextField.setVisible(true);
			authorTextField.setVisible(true);
			titleTextField.setVisible(true);
			yearTextField.setVisible(true);
			monthField.setVisible(true);
		}else if (typeName.equals("inProceedings")){
			opt1.setPromptText("Publisher");
			opt2.setPromptText("Volume");
			opt3.setPromptText("Series");
			opt4.setPromptText("Address");
			opt5.setPromptText("Edition");
			opt6.setPromptText("Pages");
			opt7.setPromptText("Book Title");
			opt8.setPromptText("Organization");
			opt9.setPromptText("Editor");
			opt10.setPromptText("Chapter");
			opt11.setPromptText("Note");
			opt1.setVisible(true);
			opt2.setVisible(true);
			opt3.setVisible(true);
			opt4.setVisible(true);
			opt5.setVisible(true);
			opt6.setVisible(true);
			opt7.setVisible(true);
			opt8.setVisible(true);
			opt9.setVisible(true);
			opt10.setVisible(true);
			opt11.setVisible(true);
			keyTextField.setVisible(true);
			authorTextField.setVisible(true);
			titleTextField.setVisible(true);
			yearTextField.setVisible(true);
			monthField.setVisible(true);
		}else if (typeName.equals("MastersThesis")) {
			opt1.setPromptText("School");
			opt2.setPromptText("Address");
			opt3.setVisible(false);
			opt4.setVisible(false);
			opt5.setVisible(false);
			opt6.setVisible(false);
			opt7.setVisible(false);
			opt8.setVisible(false);
			opt9.setVisible(false);
			opt10.setVisible(false);
			opt11.setVisible(false);
			opt1.setVisible(true);
			opt2.setVisible(true);
			keyTextField.setVisible(true);
			authorTextField.setVisible(true);
			titleTextField.setVisible(true);
			yearTextField.setVisible(true);
			monthField.setVisible(true);
		}else if (typeName.equals("Manual")){
			opt1.setPromptText("Address");
			opt2.setPromptText("Edition");
			opt3.setPromptText("Organization");
			opt4.setVisible(false);
			opt5.setVisible(false);
			opt6.setVisible(false);
			opt7.setVisible(false);
			opt8.setVisible(false);
			opt9.setVisible(false);
			opt10.setVisible(false);
			opt11.setVisible(false);
			opt1.setVisible(true);
			opt2.setVisible(true);
			opt3.setVisible(true);
			keyTextField.setVisible(true);
			authorTextField.setVisible(true);
			titleTextField.setVisible(true);
			yearTextField.setVisible(true);
			monthField.setVisible(true);
		}else if (typeName.equals("Misc")) {
			opt1.setPromptText("How Published");
			opt2.setVisible(false);
			opt3.setVisible(false);
			opt4.setVisible(false);
			opt5.setVisible(false);
			opt6.setVisible(false);
			opt7.setVisible(false);
			opt8.setVisible(false);
			opt9.setVisible(false);
			opt10.setVisible(false);
			opt11.setVisible(false);
			opt1.setVisible(true);
			keyTextField.setVisible(true);
			authorTextField.setVisible(true);
			titleTextField.setVisible(true);
			yearTextField.setVisible(true);
			monthField.setVisible(true);
		}else if (typeName.equals("Online")) {
			opt1.setPromptText("URL");
			opt2.setVisible(false);
			opt3.setVisible(false);
			opt4.setVisible(false);
			opt5.setVisible(false);
			opt6.setVisible(false);
			opt7.setVisible(false);
			opt8.setVisible(false);
			opt9.setVisible(false);
			opt10.setVisible(false);
			opt11.setVisible(false);
			opt1.setVisible(true);
			keyTextField.setVisible(true);
			authorTextField.setVisible(true);
			titleTextField.setVisible(true);
			yearTextField.setVisible(true);
			monthField.setVisible(true);
		} else if (typeName.equals("PhdThesis")) {
			opt1.setPromptText("School");
			opt2.setPromptText("Address");
			opt3.setVisible(false);
			opt4.setVisible(false);
			opt5.setVisible(false);
			opt6.setVisible(false);
			opt7.setVisible(false);
			opt8.setVisible(false);
			opt9.setVisible(false);
			opt10.setVisible(false);
			opt11.setVisible(false);
			opt1.setVisible(true);
			opt2.setVisible(true);
			keyTextField.setVisible(true);
			authorTextField.setVisible(true);
			titleTextField.setVisible(true);
			yearTextField.setVisible(true);
			monthField.setVisible(true);
		}else if (typeName.equals("Proceedings")) {
			opt1.setPromptText("Publisher");
			opt2.setPromptText("Volume");
			opt3.setPromptText("Series");
			opt4.setPromptText("Address");
			opt5.setPromptText("Editor");
			opt6.setPromptText("Organization");
			opt7.setVisible(false);
			opt8.setVisible(false);
			opt9.setVisible(false);
			opt10.setVisible(false);
			opt11.setVisible(false);
			opt1.setVisible(true);
			opt2.setVisible(true);
			opt3.setVisible(true);
			opt4.setVisible(true);
			opt5.setVisible(true);
			keyTextField.setVisible(true);
			authorTextField.setVisible(true);
			titleTextField.setVisible(true);
			yearTextField.setVisible(true);
			monthField.setVisible(true);
		}else if (typeName.equals("Standard")){
			opt1.setPromptText("Organization");
			opt2.setPromptText("Institution");
			opt3.setPromptText("Language");
			opt4.setPromptText("How Published");
			opt5.setPromptText("Number");
			opt6.setPromptText("Revision");
			opt7.setPromptText("Address");
			opt8.setPromptText("URL");
			opt9.setVisible(false);
			opt10.setVisible(false);
			opt11.setVisible(false);
			opt1.setVisible(true);
			opt2.setVisible(true);
			opt3.setVisible(true);
			opt4.setVisible(true);
			opt5.setVisible(true);
			opt6.setVisible(true);
			opt7.setVisible(true);
			opt8.setVisible(true);
			keyTextField.setVisible(true);
			authorTextField.setVisible(true);
			titleTextField.setVisible(true);
			yearTextField.setVisible(true);
			monthField.setVisible(true);
		}else if (typeName.equals("TechReport")){
			opt1.setPromptText("Address");
			opt2.setPromptText("Edition");
			opt3.setPromptText("Organization");
			opt4.setVisible(false);
			opt5.setVisible(false);
			opt6.setVisible(false);
			opt7.setVisible(false);
			opt8.setVisible(false);
			opt9.setVisible(false);
			opt10.setVisible(false);
			opt11.setVisible(false);
			opt1.setVisible(true);
			opt2.setVisible(true);
			opt3.setVisible(true);
			keyTextField.setVisible(true);
			authorTextField.setVisible(true);
			titleTextField.setVisible(true);
			yearTextField.setVisible(true);
			monthField.setVisible(true);
		}else if (typeName.equals("unPublished")){
			opt1.setVisible(false);
			opt2.setVisible(false);
			opt3.setVisible(false);
			opt4.setVisible(false);
			opt5.setVisible(false);
			opt6.setVisible(false);
			opt7.setVisible(false);
			opt8.setVisible(false);
			opt9.setVisible(false);
			opt10.setVisible(false);
			opt11.setVisible(false);
			keyTextField.setVisible(true);
			authorTextField.setVisible(true);
			titleTextField.setVisible(true);
			yearTextField.setVisible(true);
			monthField.setVisible(true);
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

		if (typeField == null || typeField.getSelectionModel().isEmpty()) {
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
		if (monthField == null || monthField.getSelectionModel().isEmpty()) {
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

	public void exportAsBib(String temporary, File file) {
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
	public Boolean getFlag(){
		return saveFlag;
	}




	public void closeApp(ActionEvent event) {
		Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Confirm", ButtonType.OK, ButtonType.CANCEL);
		Stage stage1 = (Stage) fileMenu.getScene().getWindow();
		exitAlert.setContentText("Are you sure you want to exit?");
		exitAlert.initModality(Modality.APPLICATION_MODAL);
		exitAlert.initOwner(stage1);

		Alert saveAlert = new Alert(Alert.AlertType.CONFIRMATION, "Confirm", ButtonType.OK, ButtonType.CANCEL);
		Stage stage2 = (Stage) fileMenu.getScene().getWindow();
		saveAlert.setContentText("Do you want to exit without saving?");
		saveAlert.initModality(Modality.APPLICATION_MODAL);
		saveAlert.initOwner(stage2);


		if (saveFlag = true){
			saveAlert.showAndWait();
			if (saveAlert.getResult() == ButtonType.OK) {
				Platform.exit();
			} else {
				exitAlert.close();
			}
		}else {
			exitAlert.showAndWait();
			if (exitAlert.getResult() == ButtonType.OK) {
				Platform.exit();
			} else {
				exitAlert.close();
			}
		}
	}


}
