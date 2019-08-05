package au.com.einsporn.MainControllers;

import au.com.einsporn.DataModel.Supplier;
import au.com.einsporn.DataModel.SupplierPageData;
import au.com.einsporn.DialogBoxControllers.AddEditSupplierDialogBoxController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;


public class SupplierPageController {

    @FXML
    TableView<Supplier> supplierTable = new TableView<>();
    @FXML
    private TextField supplierSearchTextField;
    @FXML
    private TextArea notesTextArea;
    @FXML
    private RadioButton supplierIDSearchRadioBtn;
    @FXML
    private RadioButton nameSearchRadioBtn;
    @FXML
    RadioButton addressSearchRadioBtn;
    @FXML
    private Button addSupplierBtn;
    @FXML
    private Button editSupplierBtn;
    @FXML
    private Label supplierLabel;

    private SupplierPageData supplierPageData;


    public void initialize() {

        supplierPageData = new SupplierPageData();

        supplierPageData.retriveSupplierFromDB();
        supplierTable.setItems(supplierPageData.getSuppliersDataList());

        // setting up radio Buttons
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(supplierIDSearchRadioBtn, nameSearchRadioBtn, addressSearchRadioBtn);
        nameSearchRadioBtn.fire();

        //setting up image
        Image image = new Image(getClass().getResourceAsStream("../Resources/suppliers.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(120);
        imageView.setFitWidth(120);
        supplierLabel.setGraphic(imageView);



        // setting up listener on supplier Table
        supplierTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Supplier>() {
            @Override
            public void changed(ObservableValue<? extends Supplier> observable, Supplier oldValue, Supplier newValue) {

                Supplier selectedRow = supplierTable.getSelectionModel().getSelectedItem();

                if (selectedRow != null) {
                    notesTextArea.setText(selectedRow.getNotes());
                }

            }
        });


        supplierTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.getButton().equals(MouseButton.PRIMARY)) {

                    if (event.getClickCount() == 2)
                        try {
                            addEditSupplierBtnsHandler(new Event(editSupplierBtn, null, null));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }else if(event.getButton().equals(MouseButton.SECONDARY)){


                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem add = new MenuItem("Add");
                    MenuItem edit = new MenuItem("Edit");
                    MenuItem delete = new MenuItem("Delete");
                    contextMenu.getItems().addAll(add,edit,delete);
                    supplierTable.setContextMenu(contextMenu);


                    add.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {

                            try {
                                addEditSupplierBtnsHandler(new Event(addSupplierBtn,null,null));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    edit.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {

                            try {
                                addEditSupplierBtnsHandler(new Event(editSupplierBtn,null,null));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    delete.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {

                            try {
                                removeSupplierBtnHandler();
                            } catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    });
                }
            }
        });


    }


    @FXML
    private void supplierSearchBtnHandler() throws SQLException {

        String searchWord = supplierSearchTextField.getText();

        if (!searchWord.isEmpty() && supplierIDSearchRadioBtn.isSelected()) {

            String radioBtnSelected = "searchByID";
            notesTextArea.clear();
            supplierPageData.getSuppliersDataList().clear();
            supplierPageData.retriveSearchSupplierFromDB(searchWord, radioBtnSelected);

        } else if (!searchWord.isEmpty() && nameSearchRadioBtn.isSelected()) {

            String radioBtnSelected = "searchByName";
            notesTextArea.clear();
            supplierPageData.getSuppliersDataList().clear();
            supplierPageData.retriveSearchSupplierFromDB(searchWord, radioBtnSelected);

        } else if (!searchWord.isEmpty() && addressSearchRadioBtn.isSelected()) {

            String radioBtnSelected = "searchByAddress";
            notesTextArea.clear();
            supplierPageData.getSuppliersDataList().clear();
            supplierPageData.retriveSearchSupplierFromDB(searchWord, radioBtnSelected);

        } else {
            notesTextArea.clear();
            supplierPageData.getSuppliersDataList().clear();
            supplierPageData.retriveSupplierFromDB();

        }

    }


    @FXML
    private void addEditSupplierBtnsHandler(Event event) throws Exception {

        //Note: "Add Supplier Button" and "Edit Supplier Button" are both connected to the same method.
        // Event is used to find out which button was clicked and then appropriate action is triggered

        Dialog<ButtonType> dialog = new Dialog<>();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../FXMLviewes/AddEditSupplierDialogBox.fxml"));
        dialog.getDialogPane().setContent(fxmlLoader.load());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

        AddEditSupplierDialogBoxController addEditSupplierDialogBoxController = fxmlLoader.getController();
        Supplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();

        Integer newSupplierId = null;
        Integer selectedSupplierID = null;

        boolean addSupplierBtnClicked = false;
        boolean editSupplierBtnClicked = false;

        if (event.getSource().equals(addSupplierBtn)) {

            addSupplierBtnClicked = true;
            dialog.setTitle("Add Supplier");

            // populates next available Supplier number based on the info from the DB and sets it to the Label in "Add Supplier" button - dialog box
            newSupplierId = supplierPageData.retriveLastSupplierNumberFromDB() + 1;
            addEditSupplierDialogBoxController.setNewSupplierIDLabel(newSupplierId);


        } else if (event.getSource().equals(editSupplierBtn) && selectedSupplier != null) {
            dialog.setTitle("Edit Supplier");

            editSupplierBtnClicked = true;

            selectedSupplierID = selectedSupplier.getSupplierId();
            addEditSupplierDialogBoxController.setNewSupplierIDLabel(selectedSupplierID);

            //setting up textfields in the Dialog Box from the selected Supplier
            addEditSupplierDialogBoxController.setSupplierTextFields(selectedSupplier);


        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("\"Edit Supplier\"");
            alert.setContentText("Select supplier from the table");
            alert.showAndWait();
            return;
        }


        boolean a = true;

        while (a) {

            String onlyDigits = "";
            String displayText = "";
            String displayText1 = "";

            Optional<ButtonType> result = dialog.showAndWait();

            // getting info from supplier text fields in addEditSupplierDialogBox
            Supplier items = addEditSupplierDialogBoxController.getSupplierTextFields();

            String name = items.getName();
            String email = items.getEmail();
            String phone = items.getPhone();
            String address = items.getAddress();
            String notes = items.getNotes();
            String addSupplier = "addSupplier";
            String editSupplier = "editSupplier";


            if (result.isPresent() && result.get().equals(ButtonType.OK)) {

                ArrayList<String> emptyItemsStorage = new ArrayList<>();// storage for empty items(empty text fields) from the dialog box)
                String itemsArray[][] = {{"Name", name}, {"Email", email}, {"Phone", phone}, {"Address", address}};


                //iterates through itemsArray and save empty items to the Arraylist emptyItemsStorage.
                for (int i = 0; i < itemsArray.length; i++) {

                    if (itemsArray[i][1].isEmpty()) {

                        emptyItemsStorage.add(itemsArray[i][0]);// empty items are being saved in the ArrayList

                    } else ;
                }

                // iterates through ArrayList "emptyItemsStorage" and concatenates empty text fields into one string to be then displayed in the Alert box

                for (int i = 0; i < emptyItemsStorage.size(); i++) {

                    displayText = emptyItemsStorage.get(i) + ", " + displayText;
                }


                if (!emptyItemsStorage.isEmpty()) {

                    displayText1 = "1:" + "  " + "\"" + displayText + "\"" + " fields cannot be empty";
                    onlyDigits = "2:" + "  " + " Phone field " + " can only contain digits";


                    if ((phone.isEmpty() || isDigit(phone))) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        //alert.setHeaderText("Fill in empty fields.\nThe following fields are empty:");
                        alert.setContentText("");
                        alert.setContentText(displayText1);
                        alert.showAndWait();

                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText(displayText1 + "\n++++++++++++++++++++++++++++++++++++++++" + "\n" + onlyDigits);
                        alert.showAndWait();

                    }

                } else if (!isDigit(phone)) {

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Phone field can only contain digits");
                    alert.showAndWait();

                } else {

                    if (addSupplierBtnClicked) {

                        //adding new supplier to the DB
                        supplierPageData.addEditSupplierToDB(new Supplier(newSupplierId, name, email, phone, address, notes), addSupplier);
                        supplierPageData.getSuppliersDataList().clear();
                        supplierPageData.retriveSupplierFromDB();
                        a = false;

                    } else if (editSupplierBtnClicked) {

                        //editing existing supplier in the DB
                        Integer selectedRowId = supplierTable.getSelectionModel().getSelectedIndex();
                        supplierPageData.addEditSupplierToDB(new Supplier(selectedSupplierID, name, email, phone, address, notes), editSupplier);
                        supplierPageData.getSuppliersDataList().clear();
                        supplierPageData.retriveSupplierFromDB();

                        //row remains selected after editing is completed
                        supplierTable.getSelectionModel().select(selectedRowId);
                        //retrieving notes and display in the textArea
                        notesTextArea.setText(supplierTable.getSelectionModel().getSelectedItem().getNotes());

                        a = false;
                    }
                }

            } else
                a = false;
        }

    }


    @FXML
    private void removeSupplierBtnHandler() throws Exception {


        Supplier selectedItem = supplierTable.getSelectionModel().getSelectedItem();


        if (selectedItem != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Supplier");
            alert.setHeaderText("Supplier: " + selectedItem.getName());
            alert.setContentText("Do you want to delete selected supplier?");


            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get().equals(ButtonType.OK)) {

                supplierPageData.deleteSupplierFromDB(selectedItem.getSupplierId());
                supplierPageData.getSuppliersDataList().clear();
                supplierPageData.retriveSupplierFromDB();

            } else {

            }
        } else {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("\"Remove Supplier\"");
            alert.setContentText("Select supplier from the table");
            alert.showAndWait();

        }

    }

    // method to check if supplied text consists of digits only.
    private boolean isDigit(String text) {

        int a = 0;

        for (int i = 0; i < text.length(); i++) {

            boolean xx = Character.isDigit(text.charAt(i));
            if (!xx) {
                a = i;
                break;
            } else ;
        }
        return Character.isDigit(text.charAt(a));
    }


}
