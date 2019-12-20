package EasyRef;

import javafx.beans.property.SimpleStringProperty;

public class Manual extends Reference {
    private SimpleStringProperty organization, address, edition, month;

    public Manual(String type, String key, String title, String author, String year, String month, String organization, String address, String edition){
        super(type, key, title, author, year, month);
        this.address = new SimpleStringProperty(address);
        this.edition = new SimpleStringProperty(edition);
        this.month = new SimpleStringProperty(month);
        this.organization = new SimpleStringProperty(organization);
    }

    public String getOrganization() {
        return organization.get();
    }

    public SimpleStringProperty organizationProperty() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization.set(organization);
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

    public String getEdition() {
        return edition.get();
    }

    public SimpleStringProperty editionProperty() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition.set(edition);
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
        if (!getEdition().equals("null")){
            printer = printer + " | Edition: " + getEdition();
        }
        if (!getMonth().equals("null")){
            printer = printer + " | Month: " + getMonth();
        }
        if (!getOrganization().equals("null")){
            printer = printer + " | Organization: " + getOrganization();
        }
        return printer;
    }
}