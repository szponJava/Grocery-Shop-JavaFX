package au.com.einsporn.MainControllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import au.com.einsporn.DataModel.CustomerPageData;
import au.com.einsporn.DataModel.Customer;
import au.com.einsporn.DialogBoxControllers.AddNewEditCustomerDialogBoxController;

import java.sql.SQLException;
import java.util.Optional;


public class CustomerPageController {

    @FXML
    public TableView<Customer> customersTable = new TableView<>();
    @FXML
    private TextField productSearchTextField;
    @FXML
    private RadioButton firstNameSearchRadioBtn;
    @FXML
    private RadioButton lastNameSearchRadioBtn;
    @FXML
    private RadioButton addressSearchRadioBtn;
    @FXML
    private Label customerLabel;

    private ToggleGroup toggleGroup;
    private CustomerPageData customerPageData;


    public void initialize() {

        customerPageData = new CustomerPageData();
        customerPageData.retriveCustomersFromDB();
        customersTable.setItems(customerPageData.getCustomersDataList());
        firstNameSearchRadioBtn.setDisable(false);

        // setting up Radio buttons
        toggleGroup = new ToggleGroup();
        firstNameSearchRadioBtn.setToggleGroup(toggleGroup);
        lastNameSearchRadioBtn.setToggleGroup(toggleGroup);
        addressSearchRadioBtn.setToggleGroup(toggleGroup);
        firstNameSearchRadioBtn.fire();

        // setting up image
        Image image = new Image(getClass().getResourceAsStream("../Resources/customers.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(110);
        imageView.setFitWidth(80);
        customerLabel.setGraphic(imageView);





        customersTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.getButton().equals(MouseButton.PRIMARY)) {

                    if (event.getClickCount() == 2) {

                        try {
                            editCustomerBtnHandler();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if(event.getButton().equals(MouseButton.SECONDARY)){

                    if(event.getClickCount()==1){
                        ContextMenu contextMenu = new ContextMenu();
                        MenuItem add = new MenuItem("Add");
                        MenuItem update = new MenuItem("Update");
                        MenuItem delete = new MenuItem("Delete");
                        contextMenu.getItems().addAll(add,update,delete);
                        customersTable.setContextMenu(contextMenu);

                        add.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                try {
                                    addCustomerBtnHandler();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        update.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {

                                try {
                                    editCustomerBtnHandler();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                        delete.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {

                                try {
                                    removeCustomerBtnHandler();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                }else {

                }
            }
        });


        customersTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if(event.getCode().equals(KeyCode.DELETE)){

                    try {
                        removeCustomerBtnHandler();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                if(event.getCode().equals(KeyCode.ENTER)){

                    try {
                        editCustomerBtnHandler();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
        });


    }

        @FXML
        private void addCustomerBtnHandler () throws Exception {

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add New Customer");

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../FXMLviewes/AddNewCustomerDialogBox.fxml"));
            dialog.getDialogPane().setContent(fxmlLoader.load());


            AddNewEditCustomerDialogBoxController addNewCustomerDialogBoxController = fxmlLoader.getController();

            // retrieving  Last Customer Number from the DB
            Integer lastCustomerNumber = customerPageData.retriveLastCustomerNumberFromDB();

            // setting new Customer number to the label in the dialog Box
            addNewCustomerDialogBoxController.setCustomerNumber(String.valueOf(lastCustomerNumber + 1));


            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();


            if (result.isPresent() && result.get() == ButtonType.OK) {

                // retrieving entered New Customer data from the DialogBox
                Customer enteredNewCustomer = addNewCustomerDialogBoxController.getCutomerDialogBoxData();

                //Updating DB with new Customer details
                try {
                    customerPageData.addEditCustomerToDB(lastCustomerNumber + 1, enteredNewCustomer,"add");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                customerPageData.getCustomersDataList().clear();
                customerPageData.retriveCustomersFromDB();
            }
        }


        @FXML
        private void editCustomerBtnHandler () throws Exception {

            // retrieving selected customer from the table
            Customer customers = customersTable.getSelectionModel().getSelectedItem();

            if (customers != null) {

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("../FXMLviewes/EditCustomerDialogBox.fxml"));

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setTitle("Edit Customer");
                dialog.getDialogPane().setContent(fxmlLoader.load());
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

                // retrieving Customer Number from selected customer in the table
                Integer customerNumber = customers.getCustomerNumber();

                //  setting selected customer data in the Dialog box
                AddNewEditCustomerDialogBoxController addNewEditCustomerDialogBoxController = fxmlLoader.getController();
                addNewEditCustomerDialogBoxController.setCustomerDialogBox(customers, String.valueOf(customerNumber));


                Optional<ButtonType> result = dialog.showAndWait();

                if (result.isPresent() && result.get().equals(ButtonType.OK)) {

                    // retrieving edited Customer data from the DialogBox
                    Customer editedCustomer = addNewEditCustomerDialogBoxController.getCutomerDialogBoxData();

                    customerPageData.addEditCustomerToDB(customerNumber, editedCustomer,"edit");
                    customerPageData.getCustomersDataList().clear();
                    customerPageData.retriveCustomersFromDB();

                }

            }
            else {


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("\"Edit Customer\"");
                alert.setContentText("Select customer from the table");
                alert.showAndWait();

            }
        }

        @FXML
        private void removeCustomerBtnHandler () throws Exception {

            Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();

            if (selectedCustomer != null) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Remove Customer");
                alert.setContentText(" Do you want to delete selected customer?");
                alert.setHeaderText("Customer Number: " + selectedCustomer.getCustomerNumber() + "\nFirst Name " + selectedCustomer.getFirstName() + ",  Last Name: " + selectedCustomer.getLastName());

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                    customerPageData.deleteCustomerFromDB(selectedCustomer.getCustomerNumber());
                    customerPageData.getCustomersDataList().clear();
                    customerPageData.retriveCustomersFromDB();

                }

            } else {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("\"Remove Customer\"");
                alert.setContentText("Select customer from the table");
                alert.showAndWait();

            }
        }

        @FXML
        private void customerSearchBtnHandler () throws SQLException {


            if (firstNameSearchRadioBtn.isSelected()) {
                customerPageData.getCustomersDataList().clear();
                String text = productSearchTextField.getText();
                customerPageData.retriveSearchSaleProductTableItemsFromDB(text, "firstName");
            } else if(lastNameSearchRadioBtn.isSelected()) {
                customerPageData.getCustomersDataList().clear();
                String text = productSearchTextField.getText();
                customerPageData.retriveSearchSaleProductTableItemsFromDB(text, "lastName");
            } else {

                customerPageData.getCustomersDataList().clear();
                String text = productSearchTextField.getText();
                customerPageData.retriveSearchSaleProductTableItemsFromDB(text, "address");

            }

        }


    }
