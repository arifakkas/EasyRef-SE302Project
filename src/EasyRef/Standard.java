package EasyRef;

import javafx.beans.property.SimpleStringProperty;

public class Standard extends Reference {

    private SimpleStringProperty organization, institution, language, howpublished, type, number, revision, address, month, url;

    public Standard(String type, String key, String title, String author, String year, String month, String organization, String institution, String language, String howpublished, String number, String revision, String address, String url) {
        super(type, key, title, author, year, month);
        this.organization = new SimpleStringProperty(organization);
        this.institution = new SimpleStringProperty(institution);
        this.language = new SimpleStringProperty(language);
        this.howpublished = new SimpleStringProperty(howpublished);
        this.number = new SimpleStringProperty(number);
        this.revision = new SimpleStringProperty(revision);
        this.address = new SimpleStringProperty(address);
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

        if (!getOrganization().equals("")) {
            printer = printer + " | Organization: " + getOrganization();
        }
        if (!getAddress().equals("")) {
            printer = printer + " | Address: " + getAddress();
        }
        if (!getHowpublished().equals("")) {
            printer = printer + " | HowPublished: " + getHowpublished();
        }
        if (!getInstitution().equals("")) {
            printer = printer + " | Institution: " + getInstitution();
        }
        if (!getLanguage().equals("")) {
            printer = printer + " | Language: " + getLanguage();
        }
        if (!getNumber().equals("")) {
            printer = printer + " | Number: " + getNumber();
        }
        if (!getRevision().equals("")) {
            printer = printer + " | Revision: " + getRevision();
        }
        if (!getUrl().equals("")) {
            printer = printer + " | Url: " + getUrl();
        }
        return printer;
    }
}