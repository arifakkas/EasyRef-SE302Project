package EasyRef;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;
import org.jbibtex.*;


public class Controller implements Initializable {

    @FXML private TableView<Reference> tableView;
    @FXML private TableColumn<Reference, String> typeColumn;
    @FXML private TableColumn<Reference, String> authorColumn;
    @FXML private TableColumn<Reference, String> titleColumn;
    @FXML private TableColumn<Reference, String> yearColumn;
    @FXML private TableColumn<Reference, String> monthColumn;
    @FXML private TableColumn<Reference, String> keyColumn;

    @FXML private TextField authorTextField;
    @FXML private TextField titleTextField;
    @FXML private TextField yearTextField;
    @FXML private TextField keyTextField;
    @FXML private TextField filterField;

    @FXML private ComboBox<String> typeField;
    ObservableList<String> typeFieldItems = FXCollections.observableArrayList();
    @FXML private ComboBox<String> monthField;
    ObservableList<String> monthFieldItems = FXCollections.observableArrayList();


    @FXML private Button addEntryButton;
    @FXML private Button deleteSelectedEntryButton;

    @FXML private MenuBar fileMenu;

    ObservableList<Reference> refs = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle rb){

        filterField.textProperty().addListener(new ChangeListener() {
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                filterTableView((String) oldValue, (String) newValue);

            }
        });

        typeColumn.setCellValueFactory(new PropertyValueFactory<Reference,String>("type"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<Reference,String>("author"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Reference,String>("title"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<Reference,String>("year"));
        monthColumn.setCellValueFactory(new PropertyValueFactory<Reference,String>("month"));
        keyColumn.setCellValueFactory(new PropertyValueFactory<Reference,String>("key"));


        authorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        yearColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        monthColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setItems(refs);

        typeFieldItems.addAll("Article","Book","Conference","Misc","MastersThesis","PhdThesis","Proceedings");//eklenecekler var
        typeField.setItems(typeFieldItems);

        monthFieldItems.addAll("January","February","March","April","May","June","July","August","September","October","November","December");
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

    public void deleteButtonPushed(ActionEvent event)
    {
        if(!refs.isEmpty()) {
            System.out.println("Delete button clicked");
            Alert deleteAlert = new Alert(Alert.AlertType.WARNING, "Confirm", ButtonType.OK, ButtonType.CANCEL);
            Window owner = ((Node) event.getTarget()).getScene().getWindow();
            deleteAlert.setContentText("Are you sure you want to delete selected items?\n\nTHIS CANNOT BE UNDONE.");
            deleteAlert.initModality(Modality.APPLICATION_MODAL);
            deleteAlert.initOwner(owner);
            deleteAlert.showAndWait();
            if(deleteAlert.getResult() == ButtonType.OK) {
                refs.removeAll(tableView.getSelectionModel().getSelectedItems());
                tableView.getSelectionModel().clearSelection();
            }
            else {
                deleteAlert.close();
            }
        }
    }

    public void ClearButtonPushed(ActionEvent event){
        keyTextField.clear();
        authorTextField.clear();
        yearTextField.clear();
        titleTextField.clear();
        typeField.getSelectionModel().clearSelection();
        monthField.getSelectionModel().clearSelection();
    }

    public void AddEntryButtonPushed(ActionEvent event)
    {
       if(isValidInput(event)){
        Reference newRef = new Reference(typeField.getValue(),
                keyTextField.getText(),
               titleTextField.getText(),authorTextField.getText(),yearTextField.getText(),monthField.getValue());
        refs.add(newRef);
        keyTextField.clear();
        authorTextField.clear();
        yearTextField.clear();
        titleTextField.clear();
        typeField.cancelEdit();
        monthField.cancelEdit();
       }

    }


    public void changeAuthorCellEvent(TableColumn.CellEditEvent edittedCell)
    {
        Reference refSelected =  tableView.getSelectionModel().getSelectedItem();
        refSelected.setAuthor(edittedCell.getNewValue().toString());
    }

    public void changeTitleCellEvent(TableColumn.CellEditEvent edittedCell)
    {
        Reference refSelected =  tableView.getSelectionModel().getSelectedItem();
        refSelected.setTitle(edittedCell.getNewValue().toString());
    }

    public void changeYearCellEvent(TableColumn.CellEditEvent edittedCell)
    {
        Reference refSelected =  tableView.getSelectionModel().getSelectedItem();
        refSelected.setYear(edittedCell.getNewValue().toString());
    }

    public void changeMonthCellEvent(TableColumn.CellEditEvent edittedCell)
    {
        Reference refSelected =  tableView.getSelectionModel().getSelectedItem();
        refSelected.setMonth(edittedCell.getNewValue().toString());
    }

    public void filterTableView(String oldValue, String newValue) {
        ObservableList<Reference> filteredList = FXCollections.observableArrayList();
        if(filterField == null || (newValue.length() < oldValue.length()) || newValue == null) {
            tableView.setItems(refs);
        }
        else {
            newValue = newValue.toUpperCase();
            for(Reference ref : tableView.getItems()) {
                String author = ref.getAuthor();
                String title = ref.getTitle();
                String year = ref.getYear();
                String key = ref.getKey();
                String month = ref.getMonth();
                String type = ref.getType();
                if(author.toUpperCase().contains(newValue) || title.toUpperCase().contains(newValue) || year.toUpperCase().contains(newValue) || type.toUpperCase().contains(newValue) || key.toUpperCase().contains(newValue) || month.toUpperCase().contains(newValue)) {
                    filteredList.add(ref);
                }
            }
            tableView.setItems(filteredList);
        }
    }

    private boolean isValidInput(ActionEvent event) {

        Boolean validInput = true;

        if(authorTextField == null || authorTextField.getText().trim().isEmpty()) {
            validInput = false;
            Alert alert = new Alert(Alert.AlertType.WARNING, "Warning", ButtonType.OK);
            Window owner = ((Node) event.getTarget()).getScene().getWindow();
            alert.setContentText("author is EMPTY");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(owner);
            alert.showAndWait();
            if(alert.getResult() == ButtonType.OK) {
                alert.close();
                authorTextField.requestFocus();
            }
        }
        if(titleTextField == null || titleTextField.getText().trim().isEmpty()) {
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
        if(yearTextField == null || yearTextField.getText().trim().isEmpty()) {
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
        if(keyTextField == null || keyTextField.getText().trim().isEmpty()) {
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

        if(typeField == null || typeField.getValue().isEmpty() ) {
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
        if(monthField == null || monthField.getValue().trim().isEmpty() ) {
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
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Bibtex Files", "*.bib")
        );

        if(refs.isEmpty()) {
            secondaryStage.initOwner(this.fileMenu.getScene().getWindow());
            Alert emptyTableAlert = new Alert(Alert.AlertType.ERROR, "EMPTY TABLE", ButtonType.OK);
            emptyTableAlert.setContentText("You have nothing to save");
            emptyTableAlert.initModality(Modality.APPLICATION_MODAL);
            emptyTableAlert.initOwner(this.fileMenu.getScene().getWindow());
            emptyTableAlert.showAndWait();
            if(emptyTableAlert.getResult() == ButtonType.OK) {
                emptyTableAlert.close();
            }
        }
        else {
            File file = fileChooser.showSaveDialog(this.fileMenu.getScene().getWindow());
            if(file != null) {
                saveFile(tableView.getItems(), file);
            }

        }
    }

    public void saveFile(ObservableList<Reference> references, File file) {
        try {
            BufferedWriter outWriter = new BufferedWriter(new FileWriter(file));
            // A new database is created
            org.jbibtex.BibTeXDatabase database = new BibTeXDatabase();
            // A formatter is created. This formatter will take the contents of the database and write them to a file.
            org.jbibtex.BibTeXFormatter formatter = new org.jbibtex.BibTeXFormatter();
            for(Reference reference : references) {
               switch (reference.getType().toLowerCase()){
                   case"article":{
                       Key type = BibTeXEntry.TYPE_ARTICLE;

                       // A unique key is generated for each entry
                       Key key = new Key(reference.getKey());

                       // A new entry is created with type and key but it has no fields inside
                       BibTeXEntry newEntry = new BibTeXEntry(type, key);

                       // Fields are being added to the new entry created
                       newEntry.addField(BibTeXEntry.KEY_TITLE, new StringValue(reference.getTitle(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_AUTHOR, new StringValue(reference.getAuthor(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_YEAR, new StringValue(reference.getYear(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_MONTH, new StringValue(reference.getMonth(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_VOLUME, new StringValue(((Article)reference).getVolume(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_NUMBER, new StringValue(((Article)reference).getNumber(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_PAGES, new StringValue(((Article)reference).getPages(), StringValue.Style.BRACED));

                       // Each entry is considered an object and they are added to the database
                       database.addObject(newEntry);
                       break;
                   }
                   case"book":{
                       Key type = BibTeXEntry.TYPE_BOOK;

                       // A unique key is generated for each entry
                       Key key = new Key(reference.getKey());

                       // A new entry is created with type and key but it has no fields inside
                       BibTeXEntry newEntry = new BibTeXEntry(type, key);

                       // Fields are being added to the new entry created
                       newEntry.addField(BibTeXEntry.KEY_TITLE, new StringValue(reference.getTitle(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_AUTHOR, new StringValue(reference.getAuthor(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_YEAR, new StringValue(reference.getYear(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_MONTH, new StringValue(reference.getMonth(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_VOLUME, new StringValue(((Book)reference).getVolume(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_PUBLISHER, new StringValue(((Book)reference).getPublisher(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_EDITION, new StringValue(((Book)reference).getEdition(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_SERIES, new StringValue(((Book)reference).getSeries(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_ADDRESS, new StringValue(((Book)reference).getAddress(), StringValue.Style.BRACED));


                       // Each entry is considered an object and they are added to the database
                       database.addObject(newEntry);
                       break;
                   }
                   case"booklet":{
                       Key type = BibTeXEntry.TYPE_BOOKLET;

                       // A unique key is generated for each entry
                       Key key = new Key(reference.getKey());

                       // A new entry is created with type and key but it has no fields inside
                       BibTeXEntry newEntry = new BibTeXEntry(type, key);

                       // Fields are being added to the new entry created
                       newEntry.addField(BibTeXEntry.KEY_TITLE, new StringValue(reference.getTitle(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_AUTHOR, new StringValue(reference.getAuthor(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_YEAR, new StringValue(reference.getYear(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_MONTH, new StringValue(reference.getMonth(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_HOWPUBLISHED, new StringValue(((Booklet)reference).getHowPublished(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_ADDRESS, new StringValue(((Booklet)reference).getAddress(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_NOTE, new StringValue(((Booklet)reference).getNote(), StringValue.Style.BRACED));



                       // Each entry is considered an object and they are added to the database
                       database.addObject(newEntry);
                       break;
                   }
                   case"conference":{
                       Key type = BibTeXEntry.TYPE_CONFERENCE;

                       // A unique key is generated for each entry
                       Key key = new Key(reference.getKey());

                       // A new entry is created with type and key but it has no fields inside
                       BibTeXEntry newEntry = new BibTeXEntry(type, key);

                       // Fields are being added to the new entry created
                       newEntry.addField(BibTeXEntry.KEY_TITLE, new StringValue(reference.getTitle(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_AUTHOR, new StringValue(reference.getAuthor(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_YEAR, new StringValue(reference.getYear(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_MONTH, new StringValue(reference.getMonth(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_PUBLISHER, new StringValue(((Conference)reference).getPublisher(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_VOLUME, new StringValue(((Conference)reference).getVolume(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_SERIES, new StringValue(((Conference)reference).getSeries(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_ADDRESS, new StringValue(((Conference)reference).getAddress(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_EDITION, new StringValue(((Conference)reference).getEdition(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_PAGES, new StringValue(((Conference)reference).getPages(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_BOOKTITLE, new StringValue(((Conference)reference).getBookTitle(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_ORGANIZATION, new StringValue(((Conference)reference).getOrganization(), StringValue.Style.BRACED));



                       // Each entry is considered an object and they are added to the database
                       database.addObject(newEntry);
                       break;
                   }
                   case"inbook":{
                       Key type = BibTeXEntry.TYPE_INBOOK;

                       // A unique key is generated for each entry
                       Key key = new Key(reference.getKey());

                       // A new entry is created with type and key but it has no fields inside
                       BibTeXEntry newEntry = new BibTeXEntry(type, key);

                       // Fields are being added to the new entry created
                       newEntry.addField(BibTeXEntry.KEY_TITLE, new StringValue(reference.getTitle(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_AUTHOR, new StringValue(reference.getAuthor(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_YEAR, new StringValue(reference.getYear(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_MONTH, new StringValue(reference.getMonth(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_PUBLISHER, new StringValue(((inBook)reference).getPublisher(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_VOLUME, new StringValue(((inBook)reference).getVolume(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_SERIES, new StringValue(((inBook)reference).getSeries(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_ADDRESS, new StringValue(((inBook)reference).getAddress(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_EDITION, new StringValue(((inBook)reference).getEdition(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_ORGANIZATION, new StringValue(((inBook)reference).getOrganization(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_CHAPTER, new StringValue(((inBook)reference).getChapter(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_NOTE, new StringValue(((inBook)reference).getNote(), StringValue.Style.BRACED));



                       // Each entry is considered an object and they are added to the database
                       database.addObject(newEntry);
                       break;
                   }
                   case"incollection":{
                       Key type = BibTeXEntry.TYPE_INCOLLECTION;

                       // A unique key is generated for each entry
                       Key key = new Key(reference.getKey());

                       // A new entry is created with type and key but it has no fields inside
                       BibTeXEntry newEntry = new BibTeXEntry(type, key);

                       // Fields are being added to the new entry created
                       newEntry.addField(BibTeXEntry.KEY_TITLE, new StringValue(reference.getTitle(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_AUTHOR, new StringValue(reference.getAuthor(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_YEAR, new StringValue(reference.getYear(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_MONTH, new StringValue(reference.getMonth(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_PUBLISHER, new StringValue(((inCollection)reference).getPublisher(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_VOLUME, new StringValue(((inCollection)reference).getVolume(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_SERIES, new StringValue(((inCollection)reference).getSeries(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_ADDRESS, new StringValue(((inCollection)reference).getAddress(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_EDITION, new StringValue(((inCollection)reference).getEdition(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_ORGANIZATION, new StringValue(((inCollection)reference).getOrganization(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_CHAPTER, new StringValue(((inCollection)reference).getChapter(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_BOOKTITLE, new StringValue(((inCollection)reference).getBookTitle(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_EDITOR, new StringValue(((inCollection)reference).getEditor(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_PAGES, new StringValue(((inCollection)reference).getPages(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_NOTE, new StringValue(((inCollection)reference).getNote(), StringValue.Style.BRACED));



                       // Each entry is considered an object and they are added to the database
                       database.addObject(newEntry);
                       break;
                   }
                   case"inproceedings":{
                       Key type = BibTeXEntry.TYPE_INPROCEEDINGS;

                       // A unique key is generated for each entry
                       Key key = new Key(reference.getKey());

                       // A new entry is created with type and key but it has no fields inside
                       BibTeXEntry newEntry = new BibTeXEntry(type, key);

                       // Fields are being added to the new entry created
                       newEntry.addField(BibTeXEntry.KEY_TITLE, new StringValue(reference.getTitle(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_AUTHOR, new StringValue(reference.getAuthor(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_YEAR, new StringValue(reference.getYear(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_MONTH, new StringValue(reference.getMonth(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_PUBLISHER, new StringValue(((inProceedings)reference).getPublisher(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_VOLUME, new StringValue(((inProceedings)reference).getVolume(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_SERIES, new StringValue(((inProceedings)reference).getSeries(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_ADDRESS, new StringValue(((inProceedings)reference).getAddress(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_EDITION, new StringValue(((inProceedings)reference).getEdition(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_ORGANIZATION, new StringValue(((inProceedings)reference).getOrganization(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_CHAPTER, new StringValue(((inProceedings)reference).getChapter(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_BOOKTITLE, new StringValue(((inProceedings)reference).getBookTitle(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_EDITOR, new StringValue(((inProceedings)reference).getEditor(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_PAGES, new StringValue(((inProceedings)reference).getPages(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_NOTE, new StringValue(((inProceedings)reference).getNote(), StringValue.Style.BRACED));



                       // Each entry is considered an object and they are added to the database
                       database.addObject(newEntry);
                       break;
                   }
                   case"manual":{
                       Key type = BibTeXEntry.TYPE_MANUAL;

                       // A unique key is generated for each entry
                       Key key = new Key(reference.getKey());

                       // A new entry is created with type and key but it has no fields inside
                       BibTeXEntry newEntry = new BibTeXEntry(type, key);

                       // Fields are being added to the new entry created
                       newEntry.addField(BibTeXEntry.KEY_TITLE, new StringValue(reference.getTitle(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_AUTHOR, new StringValue(reference.getAuthor(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_YEAR, new StringValue(reference.getYear(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_MONTH, new StringValue(reference.getMonth(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_ORGANIZATION, new StringValue(((Manual)reference).getOrganization(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_ADDRESS, new StringValue(((Manual)reference).getAddress(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_EDITION, new StringValue(((Manual)reference).getEdition(), StringValue.Style.BRACED));



                       // Each entry is considered an object and they are added to the database
                       database.addObject(newEntry);
                       break;
                   }
                   case"mastersthesis":{
                       Key type = BibTeXEntry.TYPE_MASTERSTHESIS;

                       // A unique key is generated for each entry
                       Key key = new Key(reference.getKey());

                       // A new entry is created with type and key but it has no fields inside
                       BibTeXEntry newEntry = new BibTeXEntry(type, key);

                       // Fields are being added to the new entry created
                       newEntry.addField(BibTeXEntry.KEY_TITLE, new StringValue(reference.getTitle(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_AUTHOR, new StringValue(reference.getAuthor(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_YEAR, new StringValue(reference.getYear(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_MONTH, new StringValue(reference.getMonth(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_SCHOOL, new StringValue(((MastersThesis)reference).getSchool(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_ADDRESS, new StringValue(((MastersThesis)reference).getAddress(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_TYPE, new StringValue(((MastersThesis)reference).getThesisType(), StringValue.Style.BRACED));



                       // Each entry is considered an object and they are added to the database
                       database.addObject(newEntry);
                       break;
                   }
                   case"misc":{
                       Key type = BibTeXEntry.TYPE_MISC;

                       // A unique key is generated for each entry
                       Key key = new Key(reference.getKey());

                       // A new entry is created with type and key but it has no fields inside
                       BibTeXEntry newEntry = new BibTeXEntry(type, key);

                       // Fields are being added to the new entry created
                       newEntry.addField(BibTeXEntry.KEY_TITLE, new StringValue(reference.getTitle(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_AUTHOR, new StringValue(reference.getAuthor(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_YEAR, new StringValue(reference.getYear(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_MONTH, new StringValue(reference.getMonth(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_HOWPUBLISHED, new StringValue(((Misc)reference).getHowPublished(), StringValue.Style.BRACED));




                       // Each entry is considered an object and they are added to the database
                       database.addObject(newEntry);
                       break;
                   }
                   case"phdthesis":{
                       Key type = BibTeXEntry.TYPE_PHDTHESIS;

                       // A unique key is generated for each entry
                       Key key = new Key(reference.getKey());

                       // A new entry is created with type and key but it has no fields inside
                       BibTeXEntry newEntry = new BibTeXEntry(type, key);

                       // Fields are being added to the new entry created
                       newEntry.addField(BibTeXEntry.KEY_TITLE, new StringValue(reference.getTitle(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_AUTHOR, new StringValue(reference.getAuthor(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_YEAR, new StringValue(reference.getYear(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_MONTH, new StringValue(reference.getMonth(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_SCHOOL, new StringValue(((PhdThesis)reference).getSchool(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_TYPE, new StringValue(((PhdThesis)reference).getThesisType(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_ADDRESS, new StringValue(((PhdThesis)reference).getAddress(), StringValue.Style.BRACED));





                       // Each entry is considered an object and they are added to the database
                       database.addObject(newEntry);
                       break;
                   }
                   case"proceedings":{
                       Key type = BibTeXEntry.TYPE_PROCEEDINGS;

                       // A unique key is generated for each entry
                       Key key = new Key(reference.getKey());

                       // A new entry is created with type and key but it has no fields inside
                       BibTeXEntry newEntry = new BibTeXEntry(type, key);

                       // Fields are being added to the new entry created
                       newEntry.addField(BibTeXEntry.KEY_TITLE, new StringValue(reference.getTitle(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_AUTHOR, new StringValue(reference.getAuthor(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_YEAR, new StringValue(reference.getYear(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_MONTH, new StringValue(reference.getMonth(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_PUBLISHER, new StringValue(((Proceedings)reference).getPublisher(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_VOLUME, new StringValue(((Proceedings)reference).getVolume(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_SERIES, new StringValue(((Proceedings)reference).getSeries(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_ADDRESS, new StringValue(((Proceedings)reference).getAddress(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_EDITOR, new StringValue(((Proceedings)reference).getEditor(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_ORGANIZATION, new StringValue(((Proceedings)reference).getOrganization(), StringValue.Style.BRACED));



                       // Each entry is considered an object and they are added to the database
                       database.addObject(newEntry);
                       break;
                   }
                   case"techreport":{
                       Key type = BibTeXEntry.TYPE_TECHREPORT;

                       // A unique key is generated for each entry
                       Key key = new Key(reference.getKey());

                       // A new entry is created with type and key but it has no fields inside
                       BibTeXEntry newEntry = new BibTeXEntry(type, key);

                       // Fields are being added to the new entry created
                       newEntry.addField(BibTeXEntry.KEY_TITLE, new StringValue(reference.getTitle(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_AUTHOR, new StringValue(reference.getAuthor(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_YEAR, new StringValue(reference.getYear(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_MONTH, new StringValue(reference.getMonth(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_INSTITUTION, new StringValue(((TechReport)reference).getInstitution(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_NUMBER, new StringValue(((TechReport)reference).getNumber(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_ADDRESS, new StringValue(((TechReport)reference).getAddress(), StringValue.Style.BRACED));

                       // Each entry is considered an object and they are added to the database
                       database.addObject(newEntry);
                       break;
                   }
                   case"unpublished":{
                       Key type = BibTeXEntry.TYPE_UNPUBLISHED;

                       // A unique key is generated for each entry
                       Key key = new Key(reference.getKey());

                       // A new entry is created with type and key but it has no fields inside
                       BibTeXEntry newEntry = new BibTeXEntry(type, key);

                       // Fields are being added to the new entry created
                       newEntry.addField(BibTeXEntry.KEY_TITLE, new StringValue(reference.getTitle(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_AUTHOR, new StringValue(reference.getAuthor(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_YEAR, new StringValue(reference.getYear(), StringValue.Style.BRACED));
                       newEntry.addField(BibTeXEntry.KEY_MONTH, new StringValue(reference.getMonth(), StringValue.Style.BRACED));

                       // Each entry is considered an object and they are added to the database
                       database.addObject(newEntry);
                       break;
                   }



               }
            }
            formatter.format(database, outWriter);
            outWriter.close();
        } catch (IOException e) {
            Alert ioAlert = new Alert(Alert.AlertType.ERROR, "OOPS!", ButtonType.OK);
            ioAlert.setContentText("Sorry. An error has occurred.");
            ioAlert.showAndWait();
            if(ioAlert.getResult() == ButtonType.OK) {
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

        if(exitAlert.getResult() == ButtonType.OK) {
            Platform.exit();
        }
        else {
            exitAlert.close();
        }
    }

    public ObservableList openBibFile(File file){

        ObservableList<Reference> refs = FXCollections.observableArrayList();
        try {


            // A reader created to read the contents of the bib file.
            BufferedReader reader = new BufferedReader(
                    // Just passing the file into the reader makes the reader have all the contents of the file
                    new FileReader(file)
            );

            // A BibTex Database is created here. org.jbibtex part can be omitted
            // This database is just a variable that stores all the values of the file
            org.jbibtex.BibTeXDatabase bibTeXDatabase;

            // BibTex Parser created here.
            org.jbibtex.BibTeXParser bibTeXParser = new org.jbibtex.BibTeXParser();
            //Database initialized to the parsed version of the reader/file by using the bibTexParser created above.
            bibTeXDatabase = bibTeXParser.parse(reader);

            // Each BibTex Entry can be mapped to their key with the code below.
            // Map<Key, BibTeXEntry> myMap = bibTeXDatabase.getEntries();

            // A BibTex Entry collection is created and it includes all the entries within that specific file
            Collection<BibTeXEntry> entries = bibTeXDatabase.getEntries().values();

            // This for loop loops through each entry in the bib file
            for (BibTeXEntry entry: entries) {

                switch (entry.getType().getValue()) {
                    case "article": {
                        Article ref = new Article("", "", "", "", "", "", "", "", "");

                        Map<Key, Value> allFields = entry.getFields();
                        ref.setType(entry.getType().getValue());
                        ref.setKey(entry.getKey().getValue());

                        allFields.forEach((key, value) -> {
                            switch (key.getValue()) {
                                case "title":
                                    ref.setTitle(value.toUserString());
                                    break;
                                case "author":
                                    ref.setAuthor(value.toUserString());
                                    break;
                                case "year":
                                    ref.setYear(value.toUserString());
                                    break;
                                case "month":
                                    ref.setMonth(value.toUserString());
                                    break;
                                case "volume":
                                    ref.setVolume(value.toUserString());
                                    break;
                                case "number":
                                    ref.setNumber(value.toUserString());
                                    break;
                                case "pages":
                                    ref.setPages(value.toUserString());

                                    break;
                            }
                        });
                        refs.add(ref);
                        break;
                    }
                    case "book": {
                        Book ref = new Book("", "", "", "", "", "", "", "", "", "", "");

                        Map<Key, Value> allFields = entry.getFields();
                        ref.setType(entry.getType().getValue());
                        ref.setKey(entry.getKey().getValue());

                        allFields.forEach((key, value) -> {
                            switch (key.getValue()) {
                                case "title":
                                    ref.setTitle(value.toUserString());
                                    break;
                                case "author":
                                    ref.setAuthor(value.toUserString());
                                    break;
                                case "year":
                                    ref.setYear(value.toUserString());
                                    break;
                                case "month":
                                    ref.setMonth(value.toUserString());
                                    break;
                                case "publisher":
                                    ref.setPublisher(value.toUserString());
                                    break;
                                case "volume":
                                    ref.setVolume(value.toUserString());
                                    break;
                                case "series":
                                    ref.setSeries(value.toUserString());
                                    break;
                                case "address":
                                    ref.setAddress(value.toUserString());
                                    break;
                                case "edition":
                                    ref.setEdition(value.toUserString());
                                    break;
                            }
                        });
                        refs.add(ref);
                        break;
                    }
                    case "conference": {
                        Conference ref = new Conference("", "", "", "", "", "", "", "", "", "", "", "", "", "");

                        Map<Key, Value> allFields = entry.getFields();
                        ref.setType(entry.getType().getValue());
                        ref.setKey(entry.getKey().getValue());

                        allFields.forEach((key, value) -> {
                            switch (key.getValue()) {
                                case "title":
                                    ref.setTitle(value.toUserString());
                                    break;
                                case "author":
                                    ref.setAuthor(value.toUserString());
                                    break;
                                case "year":
                                    ref.setYear(value.toUserString());
                                    break;
                                case "month":
                                    ref.setMonth(value.toUserString());
                                    break;
                                case "publisher":
                                    ref.setPublisher(value.toUserString());
                                    break;
                                case "volume":
                                    ref.setVolume(value.toUserString());
                                    break;
                                case "series":
                                    ref.setSeries(value.toUserString());
                                    break;
                                case "address":
                                    ref.setAddress(value.toUserString());
                                    break;
                                case "edition":
                                    ref.setEdition(value.toUserString());
                                    break;
                                case "pages":
                                    ref.setPages(value.toUserString());
                                    break;
                                case "booktitle":
                                    ref.setEdition(value.toUserString());
                                    break;
                                case "organization":
                                    ref.setOrganization(value.toUserString());
                                    break;
                            }

                        });
                        refs.add(ref);
                        break;
                    }
                    case "proceedings": {
                        Proceedings ref = new Proceedings("", "", "", "", "", "", "", "", "", "", "", "");

                        Map<Key, Value> allFields = entry.getFields();
                        ref.setType(entry.getType().getValue());
                        ref.setKey(entry.getKey().getValue());

                        allFields.forEach((key, value) -> {
                            switch (key.getValue()) {
                                case "title":
                                    ref.setTitle(value.toUserString());
                                    break;
                                case "author":
                                    ref.setAuthor(value.toUserString());
                                    break;
                                case "year":
                                    ref.setYear(value.toUserString());
                                    break;
                                case "month":
                                    ref.setMonth(value.toUserString());
                                    break;
                                case "publisher":
                                    ref.setPublisher(value.toUserString());
                                    break;
                                case "volume":
                                    ref.setVolume(value.toUserString());
                                    break;
                                case "series":
                                    ref.setSeries(value.toUserString());
                                    break;
                                case "address":
                                    ref.setAddress(value.toUserString());
                                    break;
                                case "editor":
                                    ref.setEditor(value.toUserString());
                                    break;
                                case "organization":
                                    ref.setOrganization(value.toUserString());
                                    break;
                            }

                        });
                        refs.add(ref);
                        break;
                    }
                    case "phdthesis": {
                        PhdThesis ref = new PhdThesis("", "", "", "", "", "", "", "", "");

                        Map<Key, Value> allFields = entry.getFields();
                        ref.setType(entry.getType().getValue());
                        ref.setKey(entry.getKey().getValue());

                        allFields.forEach((key, value) -> {
                            switch (key.getValue()) {
                                case "title":
                                    ref.setTitle(value.toUserString());
                                    break;
                                case "author":
                                    ref.setAuthor(value.toUserString());
                                    break;
                                case "year":
                                    ref.setYear(value.toUserString());
                                    break;
                                case "month":
                                    ref.setMonth(value.toUserString());
                                    break;
                                case "school":
                                    ref.setSchool(value.toUserString());
                                    break;
                                case "type":
                                    ref.setThesisType(value.toUserString());
                                    break;
                                case "address":
                                    ref.setAddress(value.toUserString());
                                    break;
                            }

                        });
                        refs.add(ref);
                        break;
                    }
                    case "mastersthesis": {
                        MastersThesis ref = new MastersThesis("", "", "", "", "", "", "", "", "");

                        Map<Key, Value> allFields = entry.getFields();
                        ref.setType(entry.getType().getValue());
                        ref.setKey(entry.getKey().getValue());

                        allFields.forEach((key, value) -> {
                            switch (key.getValue()) {
                                case "title":
                                    ref.setTitle(value.toUserString());
                                    break;
                                case "author":
                                    ref.setAuthor(value.toUserString());
                                    break;
                                case "year":
                                    ref.setYear(value.toUserString());
                                    break;
                                case "month":
                                    ref.setMonth(value.toUserString());
                                    break;
                                case "school":
                                    ref.setSchool(value.toUserString());
                                    break;
                                case "type":
                                    ref.setThesisType(value.toUserString());
                                    break;
                                case "address":
                                    ref.setAddress(value.toUserString());
                                    break;
                            }

                        });
                        refs.add(ref);
                        break;
                    }
                    case "misc": {
                        Misc ref = new Misc("", "", "", "", "", "", "");

                        Map<Key, Value> allFields = entry.getFields();
                        ref.setType(entry.getType().getValue());
                        ref.setKey(entry.getKey().getValue());

                        allFields.forEach((key, value) -> {
                            switch (key.getValue()) {
                                case "title":
                                    ref.setTitle(value.toUserString());
                                    break;
                                case "author":
                                    ref.setAuthor(value.toUserString());
                                    break;
                                case "year":
                                    ref.setYear(value.toUserString());
                                    break;
                                case "month":
                                    ref.setMonth(value.toUserString());
                                    break;
                                case "howpublished":
                                    ref.setHowPublished(value.toUserString());
                                    break;
                            }

                        });
                        refs.add(ref);
                        break;
                    }
                    case "techreport": {
                        TechReport ref = new TechReport("", "", "", "", "", "", "","","");

                        Map<Key, Value> allFields = entry.getFields();
                        ref.setType(entry.getType().getValue());
                        ref.setKey(entry.getKey().getValue());

                        allFields.forEach((key, value) -> {
                            switch (key.getValue()) {
                                case "title":
                                    ref.setTitle(value.toUserString());
                                    break;
                                case "author":
                                    ref.setAuthor(value.toUserString());
                                    break;
                                case "year":
                                    ref.setYear(value.toUserString());
                                    break;
                                case "month":
                                    ref.setMonth(value.toUserString());
                                    break;
                                case "address":
                                    ref.setAddress(value.toUserString());
                                    break;
                                case "number":
                                    ref.setNumber(value.toUserString());
                                    break;
                                case "institution":
                                    ref.setInstitution(value.toUserString());
                                    break;

                            }

                        });
                        refs.add(ref);
                        break;
                    }
                    case "unpublished": {
                        unPublished ref = new unPublished("", "", "", "", "", "");
                        Map<Key, Value> allFields = entry.getFields();
                        ref.setType(entry.getType().getValue());
                        ref.setKey(entry.getKey().getValue());

                        allFields.forEach((key, value) -> {
                            switch (key.getValue()) {
                                case "title":
                                    ref.setTitle(value.toUserString());
                                    break;
                                case "author":
                                    ref.setAuthor(value.toUserString());
                                    break;
                                case "year":
                                    ref.setYear(value.toUserString());
                                    break;
                                case "month":
                                    ref.setMonth(value.toUserString());
                                    break;
                            }

                        });
                        refs.add(ref);
                        break;
                    }
                    case "manual": {
                        Manual ref = new Manual("", "", "", "", "", "", "","","");

                        Map<Key, Value> allFields = entry.getFields();
                        ref.setType(entry.getType().getValue());
                        ref.setKey(entry.getKey().getValue());

                        allFields.forEach((key, value) -> {
                            switch (key.getValue()) {
                                case "title":
                                    ref.setTitle(value.toUserString());
                                    break;
                                case "author":
                                    ref.setAuthor(value.toUserString());
                                    break;
                                case "year":
                                    ref.setYear(value.toUserString());
                                    break;
                                case "month":
                                    ref.setMonth(value.toUserString());
                                    break;
                                case "address":
                                    ref.setAddress(value.toUserString());
                                    break;
                                case "organization":
                                    ref.setOrganization(value.toUserString());
                                    break;
                                case "edition":
                                    ref.setEdition(value.toUserString());
                                    break;
                            }

                        });
                        refs.add(ref);
                        break;
                    }
                    case "standard": {
                        Standard ref = new Standard("", "", "", "", "", "", "","","","","","","","");

                        Map<Key, Value> allFields = entry.getFields();
                        ref.setType(entry.getType().getValue());
                        ref.setKey(entry.getKey().getValue());

                        allFields.forEach((key, value) -> {
                            switch (key.getValue()) {
                                case "title":
                                    ref.setTitle(value.toUserString());
                                    break;
                                case "author":
                                    ref.setAuthor(value.toUserString());
                                    break;
                                case "year":
                                    ref.setYear(value.toUserString());
                                    break;
                                case "month":
                                    ref.setMonth(value.toUserString());
                                    break;
                                case "address":
                                    ref.setAddress(value.toUserString());
                                    break;
                                case "number":
                                    ref.setNumber(value.toUserString());
                                    break;
                                case "institution":
                                    ref.setInstitution(value.toUserString());
                                    break;
                                case "organization":
                                    ref.setOrganization(value.toUserString());
                                    break;
                                case "language":
                                    ref.setLanguage(value.toUserString());
                                    break;
                                case "howpublished":
                                    ref.setHowpublished(value.toUserString());
                                    break;
                                case "revision":
                                    ref.setRevision(value.toUserString());
                                    break;
                                case "url":
                                    ref.setUrl(value.toUserString());
                                    break;
                            }

                        });
                        refs.add(ref);
                        break;
                    }
                    case "inbook": {
                        inBook ref = new inBook("", "", "", "", "", "", "","","","","","","","");

                        Map<Key, Value> allFields = entry.getFields();
                        ref.setType(entry.getType().getValue());
                        ref.setKey(entry.getKey().getValue());

                        allFields.forEach((key, value) -> {
                            switch (key.getValue()) {
                                case "title":
                                    ref.setTitle(value.toUserString());
                                    break;
                                case "author":
                                    ref.setAuthor(value.toUserString());
                                    break;
                                case "year":
                                    ref.setYear(value.toUserString());
                                    break;
                                case "month":
                                    ref.setMonth(value.toUserString());
                                    break;
                                case "address":
                                    ref.setAddress(value.toUserString());
                                    break;
                                case "publisher":
                                    ref.setPublisher(value.toUserString());
                                    break;
                                case "volume":
                                    ref.setVolume(value.toUserString());
                                    break;
                                case "organization":
                                    ref.setOrganization(value.toUserString());
                                    break;
                                case "series":
                                    ref.setSeries(value.toUserString());
                                    break;
                                case "edition":
                                    ref.setEdition(value.toUserString());
                                    break;
                                case "chapter":
                                    ref.setChapter(value.toUserString());
                                    break;
                                case "note":
                                    ref.setNote(value.toUserString());
                                    break;
                            }

                        });
                        refs.add(ref);
                        break;
                    }
                    case "incollection": {
                        inCollection ref = new inCollection("", "", "", "", "", "", "","","","","","","","","","","");

                        Map<Key, Value> allFields = entry.getFields();
                        ref.setType(entry.getType().getValue());
                        ref.setKey(entry.getKey().getValue());

                        allFields.forEach((key, value) -> {
                            switch (key.getValue()) {
                                case "title":
                                    ref.setTitle(value.toUserString());
                                    break;
                                case "author":
                                    ref.setAuthor(value.toUserString());
                                    break;
                                case "year":
                                    ref.setYear(value.toUserString());
                                    break;
                                case "month":
                                    ref.setMonth(value.toUserString());
                                    break;
                                case "address":
                                    ref.setAddress(value.toUserString());
                                    break;
                                case "publisher":
                                    ref.setPublisher(value.toUserString());
                                    break;
                                case "volume":
                                    ref.setVolume(value.toUserString());
                                    break;
                                case "series":
                                    ref.setSeries(value.toUserString());
                                    break;
                                case "edition":
                                    ref.setEdition(value.toUserString());
                                    break;
                                case "pages":
                                    ref.setPages(value.toUserString());
                                    break;
                                case "booktitle":
                                    ref.setBookTitle(value.toUserString());
                                    break;
                                case "organization":
                                    ref.setOrganization(value.toUserString());
                                    break;
                                case "editor":
                                    ref.setEditor(value.toUserString());
                                    break;
                                case "chapter":
                                    ref.setChapter(value.toUserString());
                                    break;
                                case "note":
                                    ref.setNote(value.toUserString());
                                    break;
                            }

                        });
                        refs.add(ref);
                        break;
                    }
                    case "inproceedings": {
                        inProceedings ref = new inProceedings("", "", "", "", "", "", "","","","","","","","","","","");

                        Map<Key, Value> allFields = entry.getFields();
                        ref.setType(entry.getType().getValue());
                        ref.setKey(entry.getKey().getValue());

                        allFields.forEach((key, value) -> {
                            switch (key.getValue()) {
                                case "title":
                                    ref.setTitle(value.toUserString());
                                    break;
                                case "author":
                                    ref.setAuthor(value.toUserString());
                                    break;
                                case "year":
                                    ref.setYear(value.toUserString());
                                    break;
                                case "month":
                                    ref.setMonth(value.toUserString());
                                    break;
                                case "address":
                                    ref.setAddress(value.toUserString());
                                    break;
                                case "publisher":
                                    ref.setPublisher(value.toUserString());
                                    break;
                                case "volume":
                                    ref.setVolume(value.toUserString());
                                    break;
                                case "series":
                                    ref.setSeries(value.toUserString());
                                    break;
                                case "edition":
                                    ref.setEdition(value.toUserString());
                                    break;
                                case "pages":
                                    ref.setPages(value.toUserString());
                                    break;
                                case "booktitle":
                                    ref.setBookTitle(value.toUserString());
                                    break;
                                case "organization":
                                    ref.setOrganization(value.toUserString());
                                    break;
                                case "editor":
                                    ref.setEditor(value.toUserString());
                                    break;
                                case "chapter":
                                    ref.setChapter(value.toUserString());
                                    break;
                                case "note":
                                    ref.setNote(value.toUserString());
                                    break;
                            }

                        });
                        refs.add(ref);
                        break;
                    }
                    case "online": {
                        Online ref = new Online("", "", "", "", "", "", "");

                        Map<Key, Value> allFields = entry.getFields();
                        ref.setType(entry.getType().getValue());
                        ref.setKey(entry.getKey().getValue());

                        allFields.forEach((key, value) -> {
                            switch (key.getValue()) {
                                case "title":
                                    ref.setTitle(value.toUserString());
                                    break;
                                case "author":
                                    ref.setAuthor(value.toUserString());
                                    break;
                                case "year":
                                    ref.setYear(value.toUserString());
                                    break;
                                case "month":
                                    ref.setMonth(value.toUserString());
                                    break;
                                case "url":
                                    ref.setUrl(value.toUserString());
                                    break;

                            }

                        });
                        refs.add(ref);
                        break;
                    }
                    case "booklet": {
                        Booklet ref = new Booklet("", "", "", "", "", "", "","","");

                        Map<Key, Value> allFields = entry.getFields();
                        ref.setType(entry.getType().getValue());
                        ref.setKey(entry.getKey().getValue());

                        allFields.forEach((key, value) -> {
                            switch (key.getValue()) {
                                case "title":
                                    ref.setTitle(value.toUserString());
                                    break;
                                case "author":
                                    ref.setAuthor(value.toUserString());
                                    break;
                                case "year":
                                    ref.setYear(value.toUserString());
                                    break;
                                case "month":
                                    ref.setMonth(value.toUserString());
                                    break;
                                case "address":
                                    ref.setAddress(value.toUserString());
                                    break;
                                case "howpublished":
                                    ref.setHowPublished(value.toUserString());
                                    break;
                                case "note":
                                    ref.setNote(value.toUserString());
                                    break;
                            }

                        });
                        refs.add(ref);
                        break;
                    }
                }
            }

            reader.close();
            return refs;


        } catch (org.jbibtex.ParseException e) {
            System.err.println("The BibTex file format is not correct. Please check your file.");
        } catch (IOException e) {
            System.err.println("There is an error related to the bib file. Please check the location of the bib file");
        }
        return refs;
    }



    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

    public void openFileButtonPushed(ActionEvent event){
        Stage secondaryStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Reference Table");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("BibTex Files", "*.bib")
        );

        File file = fileChooser.showOpenDialog(this.fileMenu.getScene().getWindow());

        if(file != null){
        refs.clear();
        refs.addAll(openBibFile(file));
        }



    }

    public void AdvancedSearch(ActionEvent event)throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AdvancedSearch.fxml"));
        Parent parent = loader.load();

        Scene advancedSearchScene = new Scene(parent);

        //access the controller and call a method
        SearchController controller = loader.getController();
        controller.initList(refs);
        tableView.setItems(controller.listToFilter);
        // Parent tableViewParent = FXMLLoader.load(getClass().getResource("AdvancedSearch.fxml"));
       // Scene advancedSearchScene = new Scene(tableViewParent);

        //This line gets the Stage information
       Stage window = new Stage();

        window.setScene(advancedSearchScene);
        window.show();

    }



    public void ClearFilter(ActionEvent event){
        tableView.setItems(refs);
        filterField.clear();
    }











}
