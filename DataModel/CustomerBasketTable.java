package au.com.einsporn.DataModel;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CustomerBasketTable {
    private static int count;
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty description = new SimpleStringProperty();
    private SimpleStringProperty code = new SimpleStringProperty();
    private SimpleStringProperty size = new SimpleStringProperty();
    private SimpleStringProperty unit = new SimpleStringProperty();
    private SimpleStringProperty category = new SimpleStringProperty();
    private SimpleStringProperty quantity = new SimpleStringProperty();
    private SimpleStringProperty price = new SimpleStringProperty();

    public CustomerBasketTable(Integer id, String description, String code, String size, String unit, String category, String quantity, String price) {

        this.id.set(id);
        this.description.set(description);
        this.code.set(code);
        this.size.set(size);
        this.unit.set(unit);
        this.category.set(category);
        this.quantity.set(quantity);
        this.price.set(price);

    }



    public int getId() {
        return id.get();
    }



    public String getDescription() {
        return description.get();
    }


    public String getCode() {
        return code.get();
    }


    public String getSize() {
        return size.get();
    }


    public String getUnit() {
        return unit.get();
    }


    public String getCategory() {
        return category.get();
    }


    public String getQuantity() {
        return quantity.get();
    }


    public String getPrice() {
        return price.get();
    }

}