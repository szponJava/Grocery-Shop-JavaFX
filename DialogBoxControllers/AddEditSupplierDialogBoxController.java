package au.com.einsporn.DialogBoxControllers;

import au.com.einsporn.DataModel.Supplier;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddEditSupplierDialogBoxController {


    @FXML
    private TextField nameDialogBoxTextField;
    @FXML
    private TextField emailDialogBoxTextField;
    @FXML
    private TextField phoneDialogBoxTextField;
    @FXML
    private TextField addressDialogBoxTextField;
    @FXML
    private TextArea notesDialogBoxTextField;
    @FXML
    private Label newSupplierIDLabel;



    public Supplier getSupplierTextFields(){

        return new Supplier(nameDialogBoxTextField.getText(),emailDialogBoxTextField.getText(),phoneDialogBoxTextField.getText(), addressDialogBoxTextField.getText(),notesDialogBoxTextField.getText());

    }


    public void setNewSupplierIDLabel(Integer id){

        newSupplierIDLabel.setText(String.valueOf(id));

    }

    public void setSupplierTextFields(Supplier textFields){

        nameDialogBoxTextField.setText(textFields.getName());
        emailDialogBoxTextField.setText(textFields.getEmail());
        phoneDialogBoxTextField.setText(textFields.getPhone());
        addressDialogBoxTextField.setText(textFields.getAddress());
        notesDialogBoxTextField.setText(textFields.getNotes());


    }

}




