package EasyRef ;
import javafx.beans.property.SimpleStringProperty;

import java.lang.ref.Reference;

public class Booklet extends Reference {
    private SimpleStringProperty howPublished, address;

    public Booklet(String key, String title, String author, int year, String month, String howPublished, String address){

        super(key, title, author, year, month);
        this.howPublished = new SimpleStringProperty(howPublished);
        this.address = new SimpleStringProperty(address);
    }

    public String getHowPublished() {

        return howPublished.get();
    }

    public SimpleStringProperty HowPublishedProperty() {

        return howPublished;
    }

    public void setHowPublished(String howPublished) {
        this.howPublished.set(howPublished);
    }
    public String getAddress() {

        return address.get();
    }

    public SimpleStringProperty AddressProperty() {

        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }




    public String toString() {

        String printer = "Note: " + getNote() + " | Author: " + getAuthor() + " | Title: " + getTitle() + " | Year: " + getYear() + " | Month: " + getMonth() + " | Key: " + getKey();

        if (!getHowPublished().equals("null")) {

            printer = printer + " | How Published: " + getHowPublished();
        }

        if (!getAddress().equals("null")){

            printer = printer + " | Address: " + getAddress();
        }
        return printer;

    }
}
