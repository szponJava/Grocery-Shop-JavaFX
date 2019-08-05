package au.com.einsporn.DataModel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Supplier {

    private SimpleIntegerProperty supplierId = new SimpleIntegerProperty();
    private  SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty email = new SimpleStringProperty();
    private SimpleStringProperty phone = new SimpleStringProperty();
    private  SimpleStringProperty address = new SimpleStringProperty();
    private  SimpleStringProperty notes = new SimpleStringProperty();

    public Supplier(Integer supplierId, String name, String email, String phone, String address, String notes) {
        this.supplierId.set(supplierId);
        this.name.set(name);
        this.email.set(email);
        this.phone.set(phone);
        this.address.set(address);
        this.notes.set(notes);

    }

    public Supplier(String name, String email, String phone, String address, String notes) {

        this.name.set(name);
        this.email.set(email);
        this.phone.set(phone);
        this.address.set(address);
        this.notes.set(notes);

    }




    public int getSupplierId() {
        return supplierId.get();
    }



    public String getName() {
        return name.get();
    }



    public String getEmail() {
        return email.get();
    }



    public String getPhone() {
        return phone.get();
    }



    public String getAddress() {
        return address.get();
    }



    public String getNotes() {
        return notes.get();
    }


}
