package EasyRef;

import javafx.beans.property.SimpleStringProperty;

public class Misc extends Reference {
    private SimpleStringProperty howPublished;

    public Misc(String type, String key, String title, String author, String year, String month, String howPublished) {
        super(type, key, title, author, year, month);
        this.howPublished = new SimpleStringProperty(howPublished);
    }

    public String getHowPublished() {
        return howPublished.get();
    }

    public SimpleStringProperty howPublishedProperty() {
        return howPublished;
    }

    public void setHowPublished(String howPublished) {
        this.howPublished.set(howPublished);
    }

    public String toString(){

        String printer = "Type: " + getType() + " | Author: " + getAuthor() + " | Title: " + getTitle() + " | Year: " + getYear() + " | Month: " + getMonth() + " | Key: " + getKey();

        if (!getHowPublished().equals("")){

            printer = printer + " | HowPublished: " + getHowPublished();
        }

        return printer;
    }
}
