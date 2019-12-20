package EasyRef;

import javafx.beans.property.SimpleStringProperty;

public class TechReport extends Reference{

    private SimpleStringProperty institution, type, number, address, month;

    public TechReport(String type, String key, String title, String author, String year, String month){
        super(type, key, title, author, year, month);
        this.address = new SimpleStringProperty(address);
        this.institution = new SimpleStringProperty(institution);
        this.month = new SimpleStringProperty(month);
        this.number = new SimpleStringProperty(number);
        this.type = new SimpleStringProperty(type);
    }

    public String getInstitution() {
        return institution.get();
    }

    public SimpleStringProperty institutionProperty() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution.set(institution);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getNumber() {
        return number.get();
    }

    public SimpleStringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
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

    public String getMonth() {
        return month.get();
    }

    public SimpleStringProperty monthProperty() {
        return month;
    }

    public void setMonth(String month) {
        this.month.set(month);
    }
    public String toString(){
        String printer = "Type: " + getType() + " | Author: " + getAuthor() + " | Title: " + getTitle() + " | Year: " + getYear() + " | Month: " + getMonth() + " | Key: " + getKey();

        if (!getAddress().equals("null")){
            printer = printer + " | Address: " + getAddress();
        }
        if (!getInstitution().equals("null")){
            printer = printer + " | Institution: " + getInstitution();
        }
        if (!getMonth().equals("null")){
            printer = printer + " | Month: " + getMonth();
        }
        if (!getNumber().equals("null")){
            printer = printer + " | number: " + getNumber();
        }
        if (!getType().equals("null")){
            printer = printer + " | Type: " + getType();
        }
        return printer;
    }
}