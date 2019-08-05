package au.com.einsporn.DataModel;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Sales {

    private SimpleIntegerProperty saleNumber = new SimpleIntegerProperty();
    private SimpleStringProperty date = new SimpleStringProperty();
    private SimpleStringProperty time = new SimpleStringProperty();
    private SimpleStringProperty customer = new SimpleStringProperty();
    private SimpleStringProperty product = new SimpleStringProperty();
    private SimpleStringProperty code = new SimpleStringProperty();
    private SimpleStringProperty size = new SimpleStringProperty();
    private SimpleStringProperty units = new SimpleStringProperty();
    private SimpleStringProperty category = new SimpleStringProperty();
    private SimpleStringProperty quantity = new SimpleStringProperty();
    private SimpleStringProperty itemPrice = new SimpleStringProperty();
    private SimpleStringProperty salePrice = new SimpleStringProperty();

    public Sales(Integer saleNumber, String date, String time, String customer, String product,
                 String code, String size, String unit, String categoty,
                 String quantity, String itemPrice, String salePrice) {

        this.saleNumber.set(saleNumber);
        this.date.set(date);
        this.time.set(time);
        this.customer.set(customer);
        this.product.set(product);
        this.code.set(code);
        this.size.set(size);
        this.units.set(unit);
        this.category.set(categoty);
        this.quantity.set(quantity);
        this.itemPrice.set(itemPrice);
        this.salePrice.set(salePrice);
    }


    public int getSaleNumber() {
        return saleNumber.get();
    }



    public String getDate() {
        return date.get();
    }



    public String getTime() {
        return time.get();
    }



    public String getCustomer() {
        return customer.get();
    }



    public String getProduct() {
        return product.get();
    }



    public String getCode() {
        return code.get();
    }


    public String getSize() {
        return size.get();
    }

    public SimpleStringProperty sizeProperty() {
        return size;
    }

    public String getUnits() {
        return units.get();
    }



    public String getCategory() {
        return category.get();
    }


    public String getQuantity() {
        return quantity.get();
    }



    public String getItemPrice() {
        return itemPrice.get();
    }



    public String getSalePrice() {
        return salePrice.get();
    }


}