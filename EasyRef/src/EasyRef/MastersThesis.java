package EasyRef;

import javafx.beans.property.SimpleStringProperty;

public class MastersThesis extends Reference {
    private SimpleStringProperty school, thesisType, address;
    public MastersThesis(String type, String key, String title, String author, String year, String month, String school, String thesisType, String address) {
        super(type, key, title, author, year, month);
        this.school = new SimpleStringProperty(school);
        this.thesisType = new SimpleStringProperty(thesisType);
        this.address = new SimpleStringProperty(address);
    }

    public String getSchool() {
        return school.get();
    }

    public SimpleStringProperty schoolProperty() {
        return school;
    }

    public void setSchool(String school) {
        this.school.set(school);
    }

    public SimpleStringProperty getThesisType() {
        return thesisType;
    }

    public void setThesisType(String thesisType) {
        this.type.set(thesisType);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String toString(){

        String printer = "Type: " + getType() + " | Author: " + getAuthor() + " | Title: " + getTitle() + " | Year: " + getYear() + " | Month: " + getMonth() + " | Key: " + getKey();

        if (!getSchool().equals("null")){

            printer = printer + " | School: " + getSchool();
        }
        if (!getAddress().equals("null")){

            printer = printer + " | Address: " + getAddress();
        }

        return printer;
    }
}
