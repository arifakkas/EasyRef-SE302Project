package EasyRef;

import javafx.beans.property.SimpleStringProperty;

public class Proceedings extends Reference {
    private SimpleStringProperty publisher, volume, series,  address, editor, organization;

    public Proceedings(String type, String key, String title, String author, String year, String month, String publisher, String volume, String series, String address, String editor, String organization) {
        super(type, key, title, author, year, month);
        this.publisher = new SimpleStringProperty(publisher);
        this.volume = new SimpleStringProperty(volume);
        this.series = new SimpleStringProperty(series);
        this.address = new SimpleStringProperty(address);
        this.editor = new SimpleStringProperty(editor);
        this.organization = new SimpleStringProperty(organization);
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

    public String getEditor() {
        return editor.get();
    }

    public SimpleStringProperty editorProperty() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor.set(editor);
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

    public String toString(){

        String printer = "Type: " + getType() + " | Author: " + getAuthor() + " | Title: " + getTitle() + " | Year: " + getYear() + " | Month: " + getMonth() + " | Key: " + getKey();

        if (!getPublisher().equals("null")){

            printer = printer + " | Publisher: " + getVolume();
        }
        if (!getVolume().equals("null")){

            printer = printer + " | Volume: " + getVolume();
        }
        if (!getSeries().equals("null")){

            printer = printer + " | Series: " + getVolume();
        }

        return printer;
    }
}
