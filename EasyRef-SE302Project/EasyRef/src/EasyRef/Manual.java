package EasyRef;

import javafx.beans.property.SimpleStringProperty;

public class Manual extends Reference {
    private SimpleStringProperty organization, address, edition;

    public Manual(String type, String key, String title, String author, String year, String month, String organization, String address, String edition){
        super(type, key, title, author, year, month);
        this.address = new SimpleStringProperty(organization);
        this.edition = new SimpleStringProperty(address);
        this.organization = new SimpleStringProperty(edition);
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

    public String toString(){
        String printer = "Type: " + getType() + " | Author: " + getAuthor() + " | Title: " + getTitle() + " | Year: " + getYear() + " | Month: " + getMonth() + " | Key: " + getKey();
        if (!getAddress().equals("")){
            printer = printer + " | Organization: " + getOrganization();
        }
        if (!getEdition().equals("")){
            printer = printer + " | Address: " + getAddress();
        }
        if (!getOrganization().equals("")){
            printer = printer + " | Edition: " + getEdition();
        }
        return printer;
    }
}