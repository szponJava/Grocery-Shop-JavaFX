package au.com.einsporn.DialogBoxControllers;

import au.com.einsporn.DataModel.CustomerBasketTable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditSelectedItemDiologBoxController {

    @FXML
    private Label productDialogBoxLabel;
    @FXML
    private Label codeDialogBoxLabel;
    @FXML
    private Label sizeDialogBoxLabel;
    @FXML
    private Label unitDialogBoxLabel;
    @FXML
    private Label categoryDialogBoxLabel;
    @FXML
    private TextField quantityDialogBoxTextField;
    @FXML
    private Label priseDialogBoxLabel;



    public void setProductDialogBoxLabel(CustomerBasketTable customerBasketTable){

        productDialogBoxLabel.setText(customerBasketTable.getDescription());
        codeDialogBoxLabel.setText(customerBasketTable.getCode());
        sizeDialogBoxLabel.setText(String.valueOf(customerBasketTable.getSize()));
        unitDialogBoxLabel.setText(customerBasketTable.getUnit());
        categoryDialogBoxLabel.setText(customerBasketTable.getCategory());
        quantityDialogBoxTextField.setText(String.valueOf(customerBasketTable.getQuantity()));
        priseDialogBoxLabel.setText(String.valueOf(customerBasketTable.getPrice()));

    }


    public String getProductDialogQuantity(){

        return  quantityDialogBoxTextField.getText();

    }



}
