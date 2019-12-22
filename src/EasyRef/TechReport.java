package EasyRef;

import javafx.beans.property.SimpleStringProperty;

public class TechReport extends Reference{

    private SimpleStringProperty institution, number, address;

    public TechReport(String type, String key, String title, String author, String year, String month, String address, String institution, String number){
        super(type, key, title, author, year, month);
        this.address = new SimpleStringProperty(address);
        this.institution = new SimpleStringProperty(institution);
        this.number = new SimpleStringProperty(number);
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

    public String toString(){
        String printer = "Type: " + getType() + " | Author: " + getAuthor() + " | Title: " + getTitle() + " | Year: " + getYear() + " | Month: " + getMonth() + " | Key: " + getKey();

        if (!getAddress().equals("")){
            printer = printer + " | Address: " + getAddress();
        }
        if (!getInstitution().equals("")){
            printer = printer + " | Institution: " + getInstitution();
        }
        if (!getNumber().equals("")){
            printer = printer + " | number: " + getNumber();
        }
        return printer;
    }
}