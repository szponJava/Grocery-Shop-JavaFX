package au.com.einsporn.DialogBoxControllers;

import au.com.einsporn.DataModel.Product;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddNewProductEditDialogBoxController {


    @FXML
    ComboBox<String> categorycomboBoxList,supplierComboBoxList,unitsComboBoxList;
    @FXML
    private TextField descriptionDialogBoxTextField;
    @FXML
    private TextField codeDialogBoxTextField;
    @FXML
    private TextField sizeDialogBoxTextField;
    @FXML
    private TextField stockLevelDialogBoxTextField;
    @FXML
    private TextField vendorDialogBoxTextField;
    @FXML
    private TextField priceDialogBoxTextField;
    @FXML
    private Label newProductIDLabel;



public void  setCategorycomboBoxList(ObservableList<String> stringComboBox){

        categorycomboBoxList.setItems(stringComboBox);
}

    public void  setSupplierComboBoxList(ObservableList<String> stringComboBox){

        supplierComboBoxList.setItems(stringComboBox);
    }

    public void  setUnitscomboBoxList(ObservableList<String> stringComboBox){

        unitsComboBoxList.setItems(stringComboBox);
    }



    public ComboBox<String> getCategorycomboBoxList() {
        return categorycomboBoxList;
    }

    public ComboBox<String> getSupplierComboBoxList() {
        return supplierComboBoxList;
    }

    public ComboBox<String> getUnitsComboBoxList() {
        return unitsComboBoxList;
    }


    public Product getNewProductDiologBoxTextFields(){

    String description = descriptionDialogBoxTextField.getText();
    String code = codeDialogBoxTextField.getText();
    String size = sizeDialogBoxTextField.getText();
    String stockLevel = stockLevelDialogBoxTextField.getText();
    String vendor = vendorDialogBoxTextField.getText();
    String  price = priceDialogBoxTextField.getText();


    return new Product(description,code,size,stockLevel,vendor,price);
}


    public void setEditDialogBoxTextFields(Product text){

    descriptionDialogBoxTextField.setText(text.getDescription());
    codeDialogBoxTextField.setText(text.getCode());
    sizeDialogBoxTextField.setText(text.getSize());
    stockLevelDialogBoxTextField.setText(text.getStockLevel());
    vendorDialogBoxTextField.setText(text.getVendor());
    priceDialogBoxTextField.setText(text.getPrice());
    }



    public void setNewProductIDLabel(String text){

    this.newProductIDLabel.setText(text);

    }


}
