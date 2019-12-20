package EasyRef ;
import javafx.beans.property.SimpleStringProperty;

import java.lang.ref.Reference;

public class inBook extends Reference {
    private SimpleStringProperty publisher, volume, series, address, edition, chapter, type, organization;

    public inBook(String type, String key, String title, String author, int year, String publisher, String volume, String series, String address, String edition, String month, String chapter, String note, String organization) {
        super(note, key, title, author, year, month);
        this.publisher = new SimpleStringProperty(publisher);
        this.volume = new SimpleStringProperty(volume);
        this.series = new SimpleStringProperty(series);
        this.address = new SimpleStringProperty(address);
        this.edition = new SimpleStringProperty(edition);
        this.organization = new SimpleStringProperty(organization);
        this.chapter = new SimpleStringProperty(chapter);
        this.type = new SimpleStringProperty(type);
    }

    public String getPublisher() {

        return publisher.get();
    }

    public SimpleStringProperty publisherProperty() {

        return publisher;
    }

    public void setPublisher(String publisher) {

        this.publisher.set(publisher);
    }

    public String getVolume() {

        return volume.get();
    }

    public SimpleStringProperty volumeProperty() {

        return volume;
    }

    public void setVolume(String volume) {

        this.volume.set(volume);
    }

    public String getSeries() {

        return series.get();
    }

    public SimpleStringProperty seriesProperty() {

        return series;
    }

    public void setSeries(String series) {

        this.series.set(series);
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

    public String getType() {

        return type.get();
    }

    public SimpleStringProperty typeProperty() {

        return type;
    }

    public void setType(String type) {

        this.type.set(type);
    }

    public void setChapter(String chapter) {

        this.chapter.set(chapter);
    }

    public String getChapter() {

        return chapter.get();
    }

    public SimpleStringProperty chapterProperty() {

        return chapter;
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

    public String toString() {

        String printer = "Note: " + getNote() + " | Author: " + getAuthor() + " | Title: " + getTitle() + " | Year: " + getYear() + " | Month: " + getMonth() + " | Key: " + getKey();

        if (!getPublisher().equals("null")) {

            printer = printer + " | Publisher: " + getPublisher();
        }
        if (!getVolume().equals("null")) {

            printer = printer + " | Volume: " + getVolume();
        }
        if (!getSeries().equals("null")) {

            printer = printer + " | Series: " + getSeries();
        }
        if (!getAddress().equals("null")) {

            printer = printer + " | Address: " + getAddress();
        }
        if (!getEdition().equals("null")) {

            printer = printer + " | Edition: " + getEdition();
        }
        if (!getType().equals("null")) {

            printer = printer + " | Type: " + getType();
        }
        if (!getChapter().equals("null")) {

            printer = printer + " | Chapter:  " + getChapter();
        }
        if (!getOrganization().equals("null")) {

            printer = printer + " | Organization: " + getOrganization();
        }
        return printer;
    }
}
