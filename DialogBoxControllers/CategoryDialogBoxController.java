package au.com.einsporn.DialogBoxControllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;


public class CategoryDialogBoxController {


    @FXML
    private ListView<String> availableCategoryListView = new ListView<>();
    @FXML
    private TextField newCategoryTextField;
    @FXML
    private Button addNewCategoryBtnHandler;
    @FXML
    private Button deleteSelectedCategoryBtnHandler;


    public ListView<String> getAvailableCategoryListView() {

        return availableCategoryListView;

    }

    public void setAvailableCategoryListView(ObservableList<String> add) {
        availableCategoryListView.setItems(add);
    }


    public String getNewCategoryTextField() {

     return newCategoryTextField.getText();
    }


    public Button getAddNewCategoryBtnHandler(){

        return addNewCategoryBtnHandler;


    }

    public void setText(String text){

        newCategoryTextField.setText(text);

    }


    public Button getDeleteSelectedCategoryBtnHandler(){

        return deleteSelectedCategoryBtnHandler;

    }



}
