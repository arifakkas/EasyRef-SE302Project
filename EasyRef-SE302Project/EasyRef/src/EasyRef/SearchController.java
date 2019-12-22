package EasyRef;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.stage.Stage;
import org.apache.commons.text.similarity.LevenshteinDistance;
import java.util.*;



public class SearchController implements Initializable {
    LevenshteinDistance ld = new LevenshteinDistance();
    @FXML private TextField authorField;
    @FXML private TextField titleField;
    @FXML private TextField minYearField;
    @FXML private TextField maxYearField;
    @FXML private ComboBox<String> typeField;
    ObservableList<String> typeFieldItems = FXCollections.observableArrayList();
    ObservableList<Reference> listToFilter = FXCollections.observableArrayList();
    @FXML private Button FilterBtn;


    public void initList(ObservableList<Reference> refs){
        listToFilter.addAll(refs);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        typeFieldItems.addAll("Article","Book","Conference","Misc","MastersThesis","PhdThesis","Proceedings");//ekleneckler var
        typeField.setItems(typeFieldItems);


    }

    public void Filter(ActionEvent event){
            ObservableList<Reference> listToPass = listToFilter;
        if(!authorField.getText().trim().isEmpty()){


            listToPass = AdvancedNameSearch(listToPass, authorField.getText().trim().replaceAll(" +", " "));
        }

        if(!titleField.getText().trim().isEmpty()){
            for(int i = 0; i < listToPass.size(); i++) {
                if(!listToPass.get(i).getTitle().contains(" ")) {
                    if(!(ld.apply(authorField.getText().trim().replaceAll(" +"," "), listToPass.get(i).getTitle()) < listToPass.get(i).getTitle().length() * 0.25)) {
                        listToPass.remove(i);
                    }
                }

                else {
                    if(!(listToPass.get(i).getTitle().contains(authorField.getText().trim().replaceAll(" +"," "))
                            || ld.apply(authorField.getText().trim().replaceAll(" +"," "), listToPass.get(i).getTitle()) < listToPass.get(i).getTitle().length() * 0.4)) {
                        listToPass.remove(i);
                    }
                }
                if(listToPass.isEmpty())
                    break;
            }
        }
        if(!typeField.getSelectionModel().isEmpty()){
            for(Reference ref: listToPass){
                if(!typeField.getValue().equals(ref.getType())){
                    listToPass.remove(ref);
                }
                if(listToPass.isEmpty())
                    break;
            }
        }
        if(!minYearField.getText().trim().isEmpty()){
            for(Reference ref: listToPass){
                if(Integer.parseInt(minYearField.getText())>Integer.parseInt(ref.getYear())){
                    listToPass.remove(ref);
                }
                if(listToPass.isEmpty())
                    break;
            }
        }
        if(!maxYearField.getText().trim().isEmpty()){
            for(Reference ref: listToPass){
                if(Integer.parseInt(maxYearField.getText())<Integer.parseInt(ref.getYear())){
                    listToPass.remove(ref);
                }
                if(listToPass.isEmpty())
                    break;
            }
        }
        listToFilter.clear();
        listToFilter.addAll(listToPass);

        Stage stage = (Stage) FilterBtn.getScene().getWindow();
        stage.close();
    }

    public ObservableList<Reference> AdvancedNameSearch(ObservableList<Reference> list, String searchName){
        ObservableList<Reference> listToReturn = FXCollections.observableArrayList();
        String searchedName;
        searchName = searchName.toLowerCase().trim().replaceAll(" +", " ");



        if(!searchName.contains(" ")) {
            for(Reference ref : list) {
                searchedName = ref.getAuthor().toLowerCase();
                if(!searchedName.contains(" ")) {
                    if(searchName.equals(searchedName)) {
                        listToReturn.add(ref);
                    }

                    else if(ld.apply(searchName, searchedName) < searchedName.length() * 0.35) {
                        listToReturn.add(ref);
                    }

                }

                else {
                    for(int j = 0; j < searchedName.split(" ").length; j++) {
                        if(searchName.equals(searchedName.split(" ")[j])){
                            listToReturn.add(ref);
                        }

                        else if(ld.apply(searchName, searchedName.split(" ")[j]) < searchedName.split(" ")[j].length() * 0.35) {
                            listToReturn.add(ref);
                        }
                    }
                }
            }
        }

        else {
            List<String> foundStrings = new ArrayList<String>();
            boolean loopBreak, initialsSame;
            for(int i = 0; i < list.size(); i++) {
                foundStrings.clear();
                loopBreak = false; initialsSame = true;
                searchedName = list.get(i).getAuthor().toLowerCase();

                outerLoop:
                for(int l = 0; l < searchedName.split(" ").length; l++) {
                    for(int m = 0; m < searchedName.split(" ").length; m++) {
                        if(searchedName.split(" ")[l].charAt(0) == searchedName.split(" ")[m].charAt(0) && m != l) {
                            initialsSame = true;
                            break outerLoop;
                        }

                        else if(l == searchedName.split(" ").length - 1 && m == searchedName.split(" ").length - 1){
                            initialsSame = false;
                        }
                    }
                }


                if(searchName.split(" ").length <= searchedName.split(" ").length) {
                    if(searchName.equals(searchedName)) {
                        listToReturn.add(list.get(i));
                    }

                    else if(searchName.contains(".")){
                        for(int j = 0; j < searchName.split(" ").length && !loopBreak; j++) {
                            for(int k = 0; k < searchedName.split(" ").length; k++) {
                                if((!(searchName.split(" ")[j].equals(searchedName.split(" ")[k])
                                        || ld.apply(searchName.split(" ")[j], searchedName.split(" ")[k]) < searchedName.split(" ")[k].length() * 0.35)
                                        && !searchName.split(" ")[j].contains("."))
                                        || (!(searchName.split(" ")[j].charAt(0) == searchedName.split(" ")[k].charAt(0))
                                        && searchName.split(" ")[j].contains("."))) {


                                    continue;
                                }

                                else if((!foundStrings.contains(searchedName.split(" ")[k]))
                                        || (foundStrings.contains(searchedName.split(" ")[k])
                                        && (searchName.split(" ")[j].equals(searchedName.split(" ")[k])
                                        || ld.apply(searchName.split(" ")[j], searchedName.split(" ")[k]) < searchedName.split(" ")[k].length() * 0.35)
                                        && initialsSame)) {

                                    foundStrings.add(searchedName.split(" ")[k]);


                                    if(j != searchName.split(" ").length - 1) {
                                        break;
                                    }
                                }

                                else if(k == searchedName.split(" ").length) {
                                    loopBreak = true;
                                    break;
                                }

                                if(j == searchName.split(" ").length - 1 && foundStrings.size() != searchName.split(" ").length) {
                                    break;
                                }

                                else if(j == searchName.split(" ").length - 1) {
                                    listToReturn.add(list.get(i));

                                }
                            }
                        }
                    }

                    else if(ld.apply(searchName, searchedName) < searchedName.length() * 0.35) {
                        listToReturn.add(list.get(i));
                    }


                }
            }
        }


        return listToReturn;

    }


}
