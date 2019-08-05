package au.com.einsporn.DataModel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customer {

    private SimpleIntegerProperty customerNumber = new SimpleIntegerProperty();
    private SimpleStringProperty firstName = new SimpleStringProperty();
    private SimpleStringProperty lastName = new SimpleStringProperty();
    private SimpleStringProperty address = new SimpleStringProperty();
    private SimpleStringProperty suburb = new SimpleStringProperty();
    private SimpleStringProperty state = new SimpleStringProperty();
    private SimpleStringProperty postCode = new SimpleStringProperty();


    public Customer(Integer  cutomerNumber, String firstName, String lastName, String address, String suburb, String state, String postCode) {
        this.customerNumber.set(cutomerNumber);
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.address.set(address);
        this.suburb.set(suburb);
        this.state.set(state);
        this.postCode.set(postCode);
    }

    public Customer(String firstName, String lastName, String address, String suburb, String state, String postCode) {
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.address.set(address);
        this.suburb.set(suburb);
        this.state.set(state);
        this.postCode.set(postCode);
    }


    public Customer(Integer  customerNumber, String firstName, String lastName) {
        this.customerNumber.set(customerNumber);
        this.firstName.set(firstName);
        this.lastName.set(lastName);
    }


    public Customer(String firstName, String lastName) {
        this.firstName.set(firstName);
        this.lastName.set(lastName);
    }


    public int getCustomerNumber() {
        return customerNumber.get();
    }


    public String getFirstName() {
        return firstName.get();
    }



    public String getLastName() {
        return lastName.get();
    }



    public String getAddress() {
        return address.get();
    }



    public String getSuburb() {
        return suburb.get();
    }



    public String getState() {
        return state.get();
    }


    public String getPostCode() {
        return postCode.get();
    }


}
