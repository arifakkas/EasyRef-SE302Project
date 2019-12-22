package EasyRef ;
import javafx.beans.property.SimpleStringProperty;

public class Booklet extends Reference {
    private SimpleStringProperty howPublished, address, note;

    public Booklet(String type, String key, String title, String author, String year, String month, String howPublished, String address, String note){

        super(type, key, title, author, year, month);
        this.howPublished = new SimpleStringProperty(howPublished);
        this.address = new SimpleStringProperty(address);
        this.note = new SimpleStringProperty(note);
    }

    public String getNote() {
        return note.get();
    }

    public SimpleStringProperty noteProperty() {
        return note;
    }

    public void setNote(String note) {
        this.note.set(note);
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

        String printer = "Type: " + getType() + " | Author: " + getAuthor() + " | Title: " + getTitle() + " | Year: " + getYear() + " | Month: " + getMonth() + " | Key: " + getKey();

        if (!getHowPublished().equals("")) {

            printer = printer + " | How Published: " + getHowPublished();
        }

        if (!getAddress().equals("")){

            printer = printer + " | Address: " + getAddress();
        }
        if (!getNote().equals("")){

            printer = printer + " | Note: " + getNote();
        }
        return printer;

    }
}
