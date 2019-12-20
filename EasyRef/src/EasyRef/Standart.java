package EasyRef;

import javafx.beans.property.SimpleStringProperty;

public class Standart extends Reference {

    private SimpleStringProperty organization, institution, language, howpublished, type, number, revision, address, month, url;

    public Standart(String key, String title, String author, String year, String organization, String institution, String language, String howpublished, String type, String number, String revision, String address, String month, String url) {
        super(type, key, title, author, year, month);
        this.organization = new SimpleStringProperty(organization);
        this.institution = new SimpleStringProperty(institution);
        this.language = new SimpleStringProperty(language);
        this.howpublished = new SimpleStringProperty(howpublished);
        this.type = new SimpleStringProperty(type);
        this.number = new SimpleStringProperty(number);
        this.revision = new SimpleStringProperty(revision);
        this.address = new SimpleStringProperty(address);
        this.month = new SimpleStringProperty(month);
        this.url = new SimpleStringProperty(url);
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

    public String getInstitution() {
        return institution.get();
    }

    public SimpleStringProperty institutionProperty() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution.set(institution);
    }

    public String getLanguage() {
        return language.get();
    }

    public SimpleStringProperty languageProperty() {
        return language;
    }

    public void setLanguage(String language) {
        this.language.set(language);
    }

    public String getHowpublished() {
        return howpublished.get();
    }

    public SimpleStringProperty howpublishedProperty() {
        return howpublished;
    }

    public void setHowpublished(String howpublished) {
        this.howpublished.set(howpublished);
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

    public String getRevision() {
        return revision.get();
    }

    public SimpleStringProperty revisionProperty() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision.set(revision);
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

    public String getUrl() {
        return url.get();
    }

    public SimpleStringProperty urlProperty() {
        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public String toSring() {
        String printer = "Type: " + getType() + " | Author: " + getAuthor() + " | Title: " + getTitle() + " | Year: " + getYear() + " | Month: " + getMonth() + " | Key: " + getKey();

        if (!getOrganization().equals("null")) {
            printer = printer + " | Organization: " + getOrganization();
        }
        if (!getAddress().equals("null")) {
            printer = printer + " | Address: " + getAddress();
        }
        if (!getHowpublished().equals("null")) {
            printer = printer + " | HowPublished: " + getHowpublished();
        }
        if (!getInstitution().equals("null")) {
            printer = printer + " | Institution: " + getInstitution();
        }
        if (!getLanguage().equals("null")) {
            printer = printer + " | Language: " + getLanguage();
        }
        if (!getMonth().equals("null")) {
            printer = printer + " | Month: " + getMonth();
        }
        if (!getNumber().equals("null")) {
            printer = printer + " | Number: " + getNumber();
        }
        if (!getRevision().equals("null")) {
            printer = printer + " | Revision: " + getRevision();
        }
        if (!getType().equals("null")) {
            printer = printer + " | Type: " + getType();
        }
        if (!getUrl().equals("null")) {
            printer = printer + " | Url: " + getUrl();
        }
        return printer;
    }
}