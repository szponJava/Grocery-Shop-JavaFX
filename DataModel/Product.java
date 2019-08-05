package au.com.einsporn.DataModel;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {


    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty description = new SimpleStringProperty();
    private SimpleStringProperty code = new SimpleStringProperty();
    private SimpleStringProperty category = new SimpleStringProperty();
    private SimpleStringProperty size = new SimpleStringProperty();
    private SimpleStringProperty unit = new SimpleStringProperty();
    private SimpleStringProperty stockLevel = new SimpleStringProperty();
    private SimpleStringProperty vendor = new SimpleStringProperty();
    private SimpleStringProperty mainSupplier = new SimpleStringProperty();
    private SimpleStringProperty price = new SimpleStringProperty();


    public Product(Integer id, String description, String code, String category, String size, String unit, String  stockLevel, String vendor,
                   String mainSupplier, String price) {
        this.id.set(id);
        this.description.set(description);
        this.code.set(code);
        this.category.set(category);
        this.size.set(size);
        this.unit.set(unit);
        this.stockLevel.set(stockLevel);
        this.vendor.set(vendor);
        this.mainSupplier.set(mainSupplier);
        this.price.set(price);
    }




    public Product(String description,String code, String size, String unit,String category, String stockLevel, String price) {

        this.description.set(description);
        this.code.set(code);
        this.size.set(size);
        this.unit.set(unit);
        this.category.set(category);
        this.stockLevel.set(stockLevel);
        this.price.set(price);
    }


    public Product(String description, String code, String size, String stockLevel, String vendor, String price) {
        this.description.set(description);
        this.code.set(code);
        this.size.set(size);
        this.stockLevel.set(stockLevel);
        this.vendor.set(vendor);
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

    public String getCategory() {
        return category.get();
    }

    public String getSize() {
        return size.get();
    }


    public String getUnit() {
        return unit.get();
    }


    public String getStockLevel() {
        return stockLevel.get();
    }


    public String getVendor() {
        return vendor.get();
    }


    public String getMainSupplier() {
        return mainSupplier.get();
    }

    public String getPrice() {
        return price.get();
    }

}



