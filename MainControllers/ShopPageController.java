package au.com.einsporn.MainControllers;

import au.com.einsporn.DataModel.*;
import au.com.einsporn.DialogBoxControllers.AddCustomerDialogBoxController;
import au.com.einsporn.DialogBoxControllers.EditSelectedItemDiologBoxController;
import au.com.einsporn.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;




public class ShopPageController {



    @FXML
    private TextField productSearchTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private Label priceDisplayLabel;
    @FXML
    private Label totalPriceDisplayLabel;
    @FXML
    private Label numberOfItemsInCustomerBasketLabel;
    @FXML
    private Label customerNameLabel;
    @FXML
    private Label customerNumberLabel;
    @FXML
    private Label trolleyLabel;
    @FXML
    private RadioButton descriptionSearchRadioBtn,categorySearchRadioBtn;

    @FXML
    private TableView<Product> selectProductTable = new TableView<>();
    @FXML
    private TableView<CustomerBasketTable> customerBasketTable = new TableView<>();


    private ShopPageData shopPageData;
    private ProductPageData productPageData;
    private CustomerDialogBoxData customerDialogBoxData;
    private CustomerPageData customerPageData;
    private SalesPageData salesPageData;

    private static Integer count = 0;




    public ShopPageController()  {
    }


    public void initialize() throws SQLException, FileNotFoundException {

        shopPageData = new ShopPageData();
        productPageData = new ProductPageData();
        customerDialogBoxData = new CustomerDialogBoxData();
        customerPageData = new CustomerPageData();
        salesPageData = new SalesPageData();



        selectProductTable.setItems(shopPageData.getSelectProductTableList());
        customerBasketTable.setItems(shopPageData.getCustomerBasketTableList());

        shopPageData.getSelectProductTableList().clear();
        shopPageData.retriveSearchSelectProductTableItemsFromDB("","description");
        Collections.sort(shopPageData.getSelectProductTableList(),new SortingAlphabetically());

        shopPageData.retriveCustomerBasketItemsFromDB();
        customerDialogBoxData.retriveCustomersFromDB();

        totalPriseAndNoItemsLabelsHandler();
        setSelectedCutomerLabel();


        Image image = new Image(getClass().getResourceAsStream("../Resources/shoppingTrolleyBlue.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(55);
        imageView.setFitWidth(55);
        trolleyLabel.setGraphic(imageView);


        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(descriptionSearchRadioBtn,categorySearchRadioBtn);
        descriptionSearchRadioBtn.fire();


        // adding listener to the table so selected row can be extracted and passed to Basket Table
        selectProductTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {

                // displaying price of selected product in Price display label

                if (selectProductTable.getSelectionModel().getSelectedItem() != null) {
                    priceDisplayLabel.setText(selectProductTable.getSelectionModel().getSelectedItem().getPrice());
                }
            }
        });

        // adding listener to customer Basket Table so selected row can be edited or deleted from the table
        customerBasketTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CustomerBasketTable>() {
            @Override
            public void changed(ObservableValue<? extends CustomerBasketTable> observable, CustomerBasketTable oldValue, CustomerBasketTable newValue) {


            }
        });

        //setting up left mouse click on selectProduct Table
        selectProductTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.getButton().equals(MouseButton.PRIMARY)) {

                    if (event.getClickCount() == 2) {
                        try {
                            AddToCustomerBasketBtnHandler();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });


        //setting up left and right mouse click on customer Basket Table
        customerBasketTable.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {


                //setting up left mouse click
                if (event.getButton().equals(MouseButton.PRIMARY)) {

                    if (event.getClickCount() == 2) {

                        try {
                            RemoveSelectedItemBtnHandler();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {

                    }

                }

                //setting up right mouse click
                if (event.getButton().equals(MouseButton.SECONDARY)) {

                    if (event.getClickCount() == 1) {

                        ContextMenu menu = new ContextMenu();
                        MenuItem delete = new MenuItem("Delete");
                        MenuItem update = new MenuItem("Update");
                        menu.getItems().addAll(delete, update);
                        customerBasketTable.setContextMenu(menu);

                        delete.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                try {
                                    RemoveSelectedItemBtnHandler();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        update.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                try {
                                    EditSelectedItemBtnHandler();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }

                }


            }
        });

        //setting up Del and Enter key button action when item is selected on customer BasketTable
        customerBasketTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if (event.getCode().equals(KeyCode.DELETE)) {

                    try {
                        RemoveSelectedItemBtnHandler();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                if (event.getCode().equals(KeyCode.ENTER)) {

                    try {
                        EditSelectedItemBtnHandler();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }


    @FXML
    private void AddToCustomerBasketBtnHandler() throws SQLException {

        Product product = selectProductTable.getSelectionModel().getSelectedItem();
        DecimalFormat df = new DecimalFormat("0.00");

        count++;

        if (product != null) {

            Integer stockLevel = Integer.parseInt(product.getStockLevel());
            String description = selectProductTable.getSelectionModel().getSelectedItem().getDescription();
            String code = selectProductTable.getSelectionModel().getSelectedItem().getCode();
            String size = selectProductTable.getSelectionModel().getSelectedItem().getSize();
            String unit = selectProductTable.getSelectionModel().getSelectedItem().getUnit();
            String category = selectProductTable.getSelectionModel().getSelectedItem().getCategory();
            String price = selectProductTable.getSelectionModel().getSelectedItem().getPrice();

            String enteredQuantity = quantityTextField.getText();


            if (stockLevel > 0) {

                // adding selected row (item) from SelectedProductTable to CustomerBasketTable
                if (enteredQuantity.isEmpty()) {

                    shopPageData.addCustomerBasketTableList(new CustomerBasketTable(count, description, code, size, unit, category, "1", price));// adding selected row from selectedCustomerTable selected row into a CustomerBasketTable List

                    try {
                        shopPageData.addCustomerBasketItemToDB(count, description, code, size, unit, category, "1", price);// adding selected row from selectedCustomerTable selected row into a DataBAse

                        // Update StockLevel in SelectProductTable
                        productPageData.updateProductStockLevelInDB(code, String.valueOf(stockLevel - 1));
                        shopPageData.getSelectProductTableList().clear();
                        shopPageData.retriveSearchSelectProductTableItemsFromDB("","description");
                        Collections.sort(shopPageData.getSelectProductTableList(),new SortingAlphabetically());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    totalPriseAndNoItemsLabelsHandler();// method to display total price and number of items of the products listed in CustomerBasketTable


                } else {
                    // check if quantity value is numeric value
                    if (!isDigit(enteredQuantity)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Quantity field must only contain numeric values");
                        alert.setHeaderText("Quantity: " + enteredQuantity);

                        alert.showAndWait();

                    } else if (Integer.parseInt(quantityTextField.getText()) > 0 && stockLevel >= Integer.parseInt(enteredQuantity)) {


                        Integer quantity = Integer.parseInt(quantityTextField.getText());
                        Integer newStockLevel = stockLevel - quantity;
                        String totalProductPrice = String.valueOf(df.format(Float.parseFloat(price) * quantity));

                        shopPageData.addCustomerBasketTableList(new CustomerBasketTable(count, description, code, size, unit, category, String.valueOf(quantity),totalProductPrice));// adding selected row from selectedCustomerTable into a CustomerBasketTable List
                        try {

                            shopPageData.addCustomerBasketItemToDB(count, description, code, size, unit, category, String.valueOf(quantity), totalProductPrice);// adding selected row from selectedCustomerTable selected row into a DataBAse

                            productPageData.updateProductStockLevelInDB(code, String.valueOf(newStockLevel));
                            shopPageData.getSelectProductTableList().clear();
                            shopPageData.retriveSearchSelectProductTableItemsFromDB("","description");


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        totalPriseAndNoItemsLabelsHandler();// method to display total price of the products listed in CustomerBasketTable
                        quantityTextField.clear();

                    } else {

                        if (stockLevel <= Integer.parseInt(enteredQuantity)) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Selected quantity is greater than stock level");
                            alert.showAndWait();
                        } else {

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Quantity needs to be greater than 0");
                            alert.showAndWait();

                        }

                    }
                }


            } else {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Stock Level: 0");
                alert.showAndWait();
            }


        } else {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Select product from the Product Table");
            alert.showAndWait();

        }
    }

    @FXML
    private void productSearchBtnHandler() throws SQLException {


        if (descriptionSearchRadioBtn.isSelected()) {

            shopPageData.getSelectProductTableList().clear();
            priceDisplayLabel.setText(null);
            shopPageData.retriveSearchSelectProductTableItemsFromDB(productSearchTextField.getText(),"description");
            Collections.sort(shopPageData.getSelectProductTableList(),new SortingAlphabetically());

        } else if (categorySearchRadioBtn.isSelected()){

            shopPageData.getSelectProductTableList().clear();
            priceDisplayLabel.setText(null);
            shopPageData.retriveSearchSelectProductTableItemsFromDB(productSearchTextField.getText(),"category");
            Collections.sort(shopPageData.getSelectProductTableList(),new SortingAlphabetically());

        }

    }


    @FXML
    private void AddCustomerBtnHandler() throws Exception {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Customer Dialog Box");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../FXMLviewes/AddCustomerDialogBox.fxml"));
        dialog.getDialogPane().setContent(fxmlLoader.load());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.APPLY);


        AddCustomerDialogBoxController addCustomerDialogBoxController = fxmlLoader.getController();
        addCustomerDialogBoxController.getSelectCustomerTableView().setItems(customerDialogBoxData.getCustomerDialogBoxDataTableView());


        //adding listener to SelectCustomerTableview in Dialog box
        addCustomerDialogBoxController.getSelectCustomerTableView().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {
            @Override
            public void changed(ObservableValue<? extends Customer> observable, Customer oldValue, Customer newValue) {

                //Getting selected Customer info

                Integer selectedCustomerNumber = addCustomerDialogBoxController.getSelectCustomerTableView().getSelectionModel().getSelectedItem().getCustomerNumber();
                String selectedCustomerFirstName = addCustomerDialogBoxController.getSelectCustomerTableView().getSelectionModel().getSelectedItem().getFirstName();
                String selectedCustomerLastName = addCustomerDialogBoxController.getSelectCustomerTableView().getSelectionModel().getSelectedItem().getLastName();


                //setting up Dialog Box CustomerNo and Customer Name labels

                addCustomerDialogBoxController.setCustomerNoDialogBoxLabel(String.valueOf(selectedCustomerNumber));
                addCustomerDialogBoxController.setCustomerNameDialogBoxLabel(selectedCustomerFirstName + " " + selectedCustomerLastName);


                //settting up OnMouseClick on SelectedCustomerTable View
                addCustomerDialogBoxController.getSelectCustomerTableView().setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton().equals(MouseButton.PRIMARY)) {


                            if (event.getClickCount() == 2) {

                                // updating DB with selected Customer
                                try {
                                    customerPageData.updateSelectedCutomerInDB(selectedCustomerNumber, selectedCustomerFirstName, selectedCustomerLastName);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //setting up Customer label with selected customer
                                try {
                                    setSelectedCutomerLabel();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                dialog.close();
                            }
                        }
                    }
                });

            }
        });


        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get().equals(ButtonType.APPLY)) {

            //Getting selected Customer info

            Integer selectedCustomerNumber = addCustomerDialogBoxController.getSelectCustomerTableView().getSelectionModel().getSelectedItem().getCustomerNumber();
            String selectedCustomerFirstName = addCustomerDialogBoxController.getSelectCustomerTableView().getSelectionModel().getSelectedItem().getFirstName();
            String selectedCustomerLastName = addCustomerDialogBoxController.getSelectCustomerTableView().getSelectionModel().getSelectedItem().getLastName();

            // updating DB with selected Customer
            customerPageData.updateSelectedCutomerInDB(selectedCustomerNumber, selectedCustomerFirstName, selectedCustomerLastName);
            //setting up Customer label with selected customer
            setSelectedCutomerLabel();

        }

    }

    @FXML
    private void RemoveCustomerBtnHandler() {

        customerNameLabel.setText("");
        customerNumberLabel.setText("");

    }


    @FXML
    private void EditSelectedItemBtnHandler() throws Exception {


        CustomerBasketTable customerBasketTable = this.customerBasketTable.getSelectionModel().getSelectedItem();

        if (customerBasketTable != null) {


            Integer indexofSelectedItem = this.customerBasketTable.getSelectionModel().getSelectedIndex();
            Integer id = this.customerBasketTable.getSelectionModel().getSelectedItem().getId();


            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(Main.getRoot().getScene().getWindow());
            dialog.setTitle("Edit Selected Item");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../FXMLviewes/EditSelectedItemDiologBox.fxml"));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // setting up all labeles in the Dialog Box
            EditSelectedItemDiologBoxController editSelectedItemDiologBoxController = fxmlLoader.getController();
            editSelectedItemDiologBoxController.setProductDialogBoxLabel(customerBasketTable);


            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();


            String price = customerBasketTable.getPrice();
            String description = customerBasketTable.getDescription();
            String size = customerBasketTable.getSize();
            String unit = customerBasketTable.getUnit();
            String code = customerBasketTable.getCode();
            String category = customerBasketTable.getCategory();


            if (result.isPresent() && result.get() == ButtonType.OK) {

                Integer currentQuantity = Integer.parseInt(customerBasketTable.getQuantity());
                String enteredNewQuantity = editSelectedItemDiologBoxController.getProductDialogQuantity();
                Integer stockLevel = Integer.parseInt(productPageData.retriveProductStockLevelFromDB(code));

                if (!enteredNewQuantity.isEmpty()) {

                    if (!isDigit(enteredNewQuantity)) {

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Quantity field must only contain numeric values");
                        alert.setHeaderText("Quantity: " + enteredNewQuantity);
                        alert.showAndWait();

                    } else {


                        Integer newQuantity = Integer.parseInt(enteredNewQuantity);

                        if ((newQuantity)>currentQuantity) {

                            if ((newQuantity<=stockLevel+currentQuantity)) {
                                Float totalProductPrice = Float.parseFloat(price) / currentQuantity * newQuantity;

                                //updating  selected row ( quantity)  using index value
                                shopPageData.getCustomerBasketTableList().set(indexofSelectedItem, new CustomerBasketTable(indexofSelectedItem, description, code, size, unit, category, String.valueOf(newQuantity), String.valueOf(totalProductPrice)));

                                //updating DB using id value
                                try {
                                    shopPageData.editSelectedCustomerBasketIteminInDB(id, newQuantity, totalProductPrice);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                    Integer newStockLevel = stockLevel - (newQuantity-currentQuantity);

                                    // Update StockLevel in SelectProductTable
                                    productPageData.updateProductStockLevelInDB(code, String.valueOf(newStockLevel));
                                    shopPageData.getSelectProductTableList().clear();
                                    shopPageData.retriveSearchSelectProductTableItemsFromDB("","description");
                                    Collections.sort(shopPageData.getSelectProductTableList(),new SortingAlphabetically());

                                    totalPriseAndNoItemsLabelsHandler();

                            } else {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setContentText("Item: " + description + "\nAvailable stock: " + stockLevel + "\n" + "\nSelected quantity: " + newQuantity);
                                alert.setHeaderText("Selected quantity exceeds available stock");
                                alert.showAndWait();
                            }
                        } else {

                            Float totalProductPrice = Float.parseFloat(price) / currentQuantity * newQuantity;
                            //updating  selected row ( quantity)  using index value
                            shopPageData.getCustomerBasketTableList().set(indexofSelectedItem, new CustomerBasketTable(indexofSelectedItem, description, code, size, unit, category, String.valueOf(newQuantity), String.valueOf(totalProductPrice)));


                            Integer newStockLevel = stockLevel + (currentQuantity-newQuantity);

                            productPageData.updateProductStockLevelInDB(code, String.valueOf(newStockLevel));
                            shopPageData.getSelectProductTableList().clear();
                            shopPageData.retriveSearchSelectProductTableItemsFromDB("","description");
                            Collections.sort(shopPageData.getSelectProductTableList(),new SortingAlphabetically());
                            totalPriseAndNoItemsLabelsHandler();
                        }
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("");
                    alert.setHeaderText("Quantity field cannot be empty. Enter a numeric value");
                    alert.showAndWait();

                }
            }

        } else {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("\"Edit Selected Item\"");
            alert.setContentText("Select item from the table");
            alert.showAndWait();
        }
    }


    @FXML
    private void RemoveSelectedItemBtnHandler() throws Exception {


        CustomerBasketTable customerBasketTable = this.customerBasketTable.getSelectionModel().getSelectedItem();

        if (customerBasketTable != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setContentText("Do you want to delete selected Item?");
            alert.setTitle("Remove Selected Item");
            alert.setHeaderText(customerBasketTable.getDescription());

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                customerBasketTable = this.customerBasketTable.getSelectionModel().getSelectedItem();
                shopPageData.removeProductFromCustomerBasketTableList(customerBasketTable);
                shopPageData.removeSelectedCustomerBasketItemFromDB(customerBasketTable.getId());

                String productCode = customerBasketTable.getCode();
                Integer currentStockLevel = Integer.parseInt(productPageData.retriveProductStockLevelFromDB(productCode));

                Integer quantity = Integer.parseInt(customerBasketTable.getQuantity());
                Integer newStockLevel = currentStockLevel + quantity;

                //updates Product in DB with new stock level
                productPageData.updateProductStockLevelInDB(productCode, String.valueOf(newStockLevel));

                shopPageData.getSelectProductTableList().clear();
                shopPageData.retriveSearchSelectProductTableItemsFromDB("","description");
                Collections.sort(shopPageData.getSelectProductTableList(),new SortingAlphabetically());

                totalPriseAndNoItemsLabelsHandler();

            }
        }else {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("\"Remove Selected Item\"");
            alert.setContentText("Select Item from the table");
            alert.showAndWait();


        }

    }


    @FXML
    private void SaveBtnHandler() throws Exception {

        DecimalFormat df = new DecimalFormat("0.00");

        Integer saleNumber = salesPageData.retriveLastSaleNumberFromDB()+1;

        for(int i = 0; i<shopPageData.getCustomerBasketTableList().size(); i++){


            String customerFirstName = customerPageData.getSelectedCustomerList().get(0).getFirstName();
            String customerLastName = customerPageData.getSelectedCustomerList().get(0).getLastName();
            String customerName = customerFirstName+" "+customerLastName;

            String product = shopPageData.getCustomerBasketTableList().get(i).getDescription();
            String code = shopPageData.getCustomerBasketTableList().get(i).getCode();
            String size = shopPageData.getCustomerBasketTableList().get(i).getSize();
            String units = shopPageData.getCustomerBasketTableList().get(i).getUnit();
            String category = shopPageData.getCustomerBasketTableList().get(i).getCategory();
            String quantity = shopPageData.getCustomerBasketTableList().get(i).getQuantity();

            Float unitPriceFloat = Float.parseFloat(shopPageData.getCustomerBasketTableList().get(i).getPrice())/Integer.parseInt(quantity);
            String salePrice =  shopPageData.getCustomerBasketTableList().get(i).getPrice();


            salesPageData.addSaleToDB(new Sales(saleNumber,getCurrentDate(),getCurrentTime(),customerName,product,code,size,units,category,quantity,df.format(unitPriceFloat),salePrice));

            shopPageData.clearCustomerBasketItemsFromDB();
        }
        shopPageData.getCustomerBasketTableList().clear();
        totalPriceDisplayLabel.setText("");
        numberOfItemsInCustomerBasketLabel.setText("");

    }


    @FXML
    private void emptyCustomerBasketBtnHandler() throws Exception {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do you want to delete All items from Customer Basket?");
        alert.setTitle("Empty Basket");
        alert.setHeaderText("");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get().equals(ButtonType.OK)) {


            //updates Product DB with a new stock level for each product
            for (int i = 0; i < customerBasketTable.getItems().size(); i++) {
                String productCode = customerBasketTable.getItems().get(i).getCode();
                Integer quantity = Integer.parseInt(customerBasketTable.getItems().get(i).getQuantity());
                Integer currentStockLevel = Integer.parseInt(productPageData.retriveProductStockLevelFromDB(productCode));
                Integer newStockLevel = currentStockLevel + quantity;
                productPageData.updateProductStockLevelInDB(productCode, String.valueOf(newStockLevel));

            }
            // update/refresh SelectProductTable
            shopPageData.getSelectProductTableList().clear();
            shopPageData.retriveSearchSelectProductTableItemsFromDB("","description");
            Collections.sort(shopPageData.getSelectProductTableList(),new SortingAlphabetically());

            // removing products from CutomerBasketTable
            shopPageData.getCustomerBasketTableList().remove(0, shopPageData.getCustomerBasketTableList().size());
            shopPageData.clearCustomerBasketItemsFromDB();

            totalPriceDisplayLabel.setText("");
            numberOfItemsInCustomerBasketLabel.setText("");
        } else {

        }

    }

    // Methods checks for digits in the supplied string value
    private boolean isDigit(String text) {

        int a = 0;

        for (int i = 0; i < text.length(); i++) {

            Boolean xx = Character.isDigit(text.charAt(i));

            if (!xx && !xx.equals(",")) {
                a = i;
                break;
            }
        }

        return Character.isDigit(text.charAt(a));
    }


    // method calculates a total price and number of items from Customer Basket Table and display values in the Labels
    private void totalPriseAndNoItemsLabelsHandler() {

        ObservableList<CustomerBasketTable> list = shopPageData.getCustomerBasketTableList();

        float a = 0;
        int b=0;

        for (int i = 0; i < list.size(); i++) {

            a+= Float.parseFloat(list.get(i).getPrice());
            b+=Integer.parseInt(list.get(i).getQuantity());
        }

        DecimalFormat df =new DecimalFormat("0.00");

        totalPriceDisplayLabel.setText(String.valueOf(df.format(a)));

        numberOfItemsInCustomerBasketLabel.setText(String.valueOf(b));
    }


    private void setSelectedCutomerLabel() throws SQLException {

        customerPageData.getSelectedCustomerList().clear();
        customerPageData.retriveSelectedCustomer();
        Integer customerNumber = customerPageData.getSelectedCustomerList().get(0).getCustomerNumber();
        String customerFirstName = customerPageData.getSelectedCustomerList().get(0).getFirstName();
        String customerLastName = customerPageData.getSelectedCustomerList().get(0).getLastName();

        customerNameLabel.setText(customerFirstName + " " + customerLastName);
        customerNumberLabel.setText(String.valueOf(customerNumber));


    }


    private String getCurrentDate() {

        Date now = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyy");

        return dateFormatter.format(now);
    }

    private String getCurrentTime() {

        Date now = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("H:mm:ss");

        return dateFormatter.format(now);
    }


   /* class to sort Description column alphabetically */
    class SortingAlphabetically implements Comparator<Product>{

        @Override
        public int compare(Product o1, Product o2) {
            return o1.getDescription().compareTo(o2.getDescription());
        }
    }

}



