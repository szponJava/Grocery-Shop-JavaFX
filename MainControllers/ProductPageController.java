package au.com.einsporn.MainControllers;

import au.com.einsporn.DialogBoxControllers.CategoryDialogBoxController;
import au.com.einsporn.DialogBoxControllers.UnitsDialogBoxController;
import au.com.einsporn.DataModel.CategoryDialogBoxData;
import au.com.einsporn.DataModel.Product;
import au.com.einsporn.DataModel.ProductPageData;
import au.com.einsporn.DataModel.UnitsDialogBoxData;
import au.com.einsporn.DialogBoxControllers.AddNewProductEditDialogBoxController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;


public class ProductPageController {


    @FXML
    private RadioButton descriptionSearchRadioBtn;
    @FXML
    private RadioButton categorySearchRadioBtn;
    @FXML
    private RadioButton vendorSearchRadioBtn;
    @FXML
    private RadioButton supplierSearchRadioBtn;
    @FXML
    private RadioButton stockLevelSearchRadioBtn;
    @FXML
    private TextField productSearchTextField, stockLevelTextField;
    @FXML
    private Button addProductBtn;
    @FXML
    private Button editProductBtn;
    @FXML
    TableView<Product> productsTable = new TableView();
    @FXML
    private Label productsLabel;


    private ProductPageData productPageData;
    private AddNewProductEditDialogBoxController addNewProductEditDialogBoxController;
    private CategoryDialogBoxData categoryDialogBoxData;
    private UnitsDialogBoxData unitsDialogBoxData;



    public void initialize() {


        productPageData = new ProductPageData();
        addNewProductEditDialogBoxController = new AddNewProductEditDialogBoxController();
        categoryDialogBoxData = new CategoryDialogBoxData();
        unitsDialogBoxData = new UnitsDialogBoxData();


        productPageData.retriveProductsFromDB();


        //sorting ProductID in ascending order
        Collections.sort(productPageData.getProductDataList(), new SortingByProductID());

        productsTable.setItems(productPageData.getProductDataList());

        productPageData.retriveCategoryComboBoxListFromDB();
        productPageData.retriveSupplierComboBoxListFromDB();
        productPageData.retriveUnitsComboBoxListFromDB();

        stockLevelTextField.setEditable(true);
        stockLevelTextField.setDisable(true);

        // setting up radio Buttons
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(descriptionSearchRadioBtn, categorySearchRadioBtn, vendorSearchRadioBtn, supplierSearchRadioBtn, stockLevelSearchRadioBtn);
        descriptionSearchRadioBtn.fire();


        // setting up image
        Image image = new Image(getClass().getResourceAsStream("../Resources/products.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(96);
        imageView.setFitWidth(96);
        productsLabel.setGraphic(imageView);


        stockLevelSearchRadioBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stockLevelTextField.setDisable(false);
            }
        });

        descriptionSearchRadioBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stockLevelTextField.setDisable(true);
            }
        });


        categorySearchRadioBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stockLevelTextField.setDisable(true);
            }
        });

        vendorSearchRadioBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stockLevelTextField.setDisable(true);
            }
        });

        supplierSearchRadioBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stockLevelTextField.setDisable(true);

            }
        });


        // setting up mouse keys action on productsTable
        productsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                //setting action on primary mouse button key (double click).
                if (event.getButton().equals(MouseButton.PRIMARY)) {

                    if (event.getClickCount() == 2) {


                        try {
                            addEditProductBtnsHandler(new Event(editProductBtn, null, null));// passing editProductBtn as an event to addEditProductBtnHandler method so edit dialog is initiated by double clicking primary mouse key
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                // setting action - context Menu on secondary mouse button key click

                if (event.getButton().equals(MouseButton.SECONDARY)) {

                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem add = new MenuItem("Add");
                    MenuItem edit = new MenuItem("Edit");
                    MenuItem delete = new MenuItem("Delete");
                    contextMenu.getItems().addAll(add, edit, delete);
                    productsTable.setContextMenu(contextMenu);


                    add.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            try {
                                addEditProductBtnsHandler(new Event(addProductBtn, null, null));// passing addProductBtn as an event to addEditProductBtnHandler method so edit dialog is initiated
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


                    edit.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {

                            try {
                                addEditProductBtnsHandler(new Event(editProductBtn, null, null));// passing editProductBtn as an event to addEditProductBtnHandler method so edit dialog is initiated
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    delete.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            try {
                                removeProductBtnHandler();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            }
        });


    }


    @FXML
    private void addEditProductBtnsHandler(Event event) throws Exception {


        //Note: "Add Product Button" and "Edit Product Button" are connected to this method.
        // Event is used to find out which button was clicked and appropriate action is triggered

        Dialog<ButtonType> dialog = new Dialog<>();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../FXMLviewes/AddNewProductEditDialogBox.fxml"));
        dialog.getDialogPane().setContent(fxmlLoader.load());


        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);


        AddNewProductEditDialogBoxController addNewProductEditDialogBoxController = fxmlLoader.getController();

        //Populates Category ComboBox in "Add Product" button - Dialog Box
        Collections.sort(productPageData.getComboBoxListCategory());// sorting ComboBox values alphabetically
        addNewProductEditDialogBoxController.setCategorycomboBoxList(productPageData.getComboBoxListCategory());


        //Populates Supplier ComboBox in "Add Product" - Dialog Box
        Collections.sort(productPageData.getComboBoxListSupplier());// sorting ComboBox values alphabetically
        addNewProductEditDialogBoxController.setSupplierComboBoxList(productPageData.getComboBoxListSupplier());

        //Populates Units ComboBox in "Add Product" - Dialog Box
        addNewProductEditDialogBoxController.setUnitscomboBoxList(productPageData.getComboBoxListUnits());

        Product items = productsTable.getSelectionModel().getSelectedItem();

        Boolean addProductBtnClicked = false;
        Boolean editProductBtnClicked = false;
        Integer selectedProductNumber = null;

        if (event.getSource().equals(addProductBtn)) {
            addProductBtnClicked = true;
            dialog.setTitle("Add New Product");

            //populates next available product number and sets it to the Label in "Add Product" button - dialog box
            String newProductNumber = String.valueOf(productPageData.retriveLastProductIDFromDB() + 1);
            addNewProductEditDialogBoxController.setNewProductIDLabel(newProductNumber);


        } else if ((event.getSource().equals(editProductBtn) && items != null)) {
            editProductBtnClicked = true;
            dialog.setTitle("Edit Product");

            //setting selected category value in comboBoxList
            addNewProductEditDialogBoxController.getCategorycomboBoxList().getSelectionModel().select(items.getCategory());

            //setting selected unit value in comboBoxList
            addNewProductEditDialogBoxController.getUnitsComboBoxList().getSelectionModel().select(items.getUnit());
            //setting selected supplier value in comboBoxList
            addNewProductEditDialogBoxController.getSupplierComboBoxList().getSelectionModel().select(items.getMainSupplier());

            //setting remaining text fields in the Edit Dialog with selected items for editting
            addNewProductEditDialogBoxController.setEditDialogBoxTextFields(items);

            selectedProductNumber = items.getId();
            addNewProductEditDialogBoxController.setNewProductIDLabel(String.valueOf(items.getId()));

        } else {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("\"Edit Product\"");
            alert.setContentText("Select product from the table");
            alert.showAndWait();

            return;
        }


        Boolean a = true;

        while (a) {

            String sizeNotaDigit = "";
            String displayText = "";
            String displayText1 = "";

            Optional<ButtonType> result = dialog.showAndWait();

            //Getting all entered/selected information from the text fields in Dialog Box
            Product item = addNewProductEditDialogBoxController.getNewProductDiologBoxTextFields();

            Integer newProductNumberInt = productPageData.retriveLastProductIDFromDB() + 1;


            String description = item.getDescription();
            String code = item.getCode();
            String category = addNewProductEditDialogBoxController.getCategorycomboBoxList().getSelectionModel().getSelectedItem();
            if (category == null) {
                category = "";
            }

            String size = item.getSize();
            String units = addNewProductEditDialogBoxController.getUnitsComboBoxList().getSelectionModel().getSelectedItem();
            if (units == null) {
                units = "";
            }
            String stockLevel = item.getStockLevel();
            String vendor = item.getVendor();
            String supplier = addNewProductEditDialogBoxController.getSupplierComboBoxList().getSelectionModel().getSelectedItem();
            if (supplier == null) {
                supplier = "";
            }
            String price = item.getPrice();
            String addNewProduct = "addNewProduct";
            String editNewProduct = "editProduct";

            if (result.isPresent() && result.get().equals(ButtonType.OK)) {

                ArrayList<String> emptyItemsStorage = new ArrayList<>();// storage for empty items(empty text fields) in the dialog box)
                String itemsArray[][] = {{"Price", price}, {"Supplier", supplier}, {"Vendor", vendor}, {"Stock Level", stockLevel}, {"Units", units}, {"Size", size}, {"Category", category}, {"Code", code}, {"Description", description}};

                //iterates through itemsArray Array and save empty fields to the Arraylist emptyItemsStorage.
                for (int i = 0; i < itemsArray.length; i++) {

                    if (itemsArray[i][1].isEmpty()) {

                        emptyItemsStorage.add(itemsArray[i][0]);// empty items are saved in the ArrayList

                    } else ;
                }

                // iterates through ArrayList emptyItemsStorage and concatenates items into one string to be then displayed in the Alert box

                for (int i = 0; i < emptyItemsStorage.size(); i++) {

                    displayText = emptyItemsStorage.get(i) + ", " + displayText;
                }


                if (!emptyItemsStorage.isEmpty()) {

                    displayText1 = "1:" + "  " + "\"" + displayText + "\"" + " fields cannot be empty";
                    sizeNotaDigit = "2:" + "  " + " field(s) " + " can only contain digits and single full stop";

                    if ((size.isEmpty() || isDigit(size)) && (price.isEmpty() || isDigit(price)) && (stockLevel.isEmpty() || isDigit(stockLevel))) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        //alert.setHeaderText("Fill in empty fields.\nThe following fields are empty:");
                        alert.setContentText("");
                        alert.setContentText(displayText1);
                        alert.showAndWait();

                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText(displayText1 + "\n++++++++++++++++++++++++++++++++++++++++" + "\n" + sizeNotaDigit);
                        alert.showAndWait();

                    }

                } else if (!isDigit(size) || !isDigit(stockLevel) || !isDigit(price)) {

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("field(s) can only contain digits and single full stop");
                    alert.showAndWait();


                } else {

                    if (addProductBtnClicked) {
                        //Adding new Product to the DB
                        productPageData.addEditProductToDB(new Product(newProductNumberInt, description, code, category, size, units, stockLevel, vendor, supplier, price), addNewProduct);
                        productPageData.getProductDataList().clear();
                        productPageData.retriveProductsFromDB();
                        //sorting ProductID column in the Table in ascending order
                        Collections.sort(productPageData.getProductDataList(), new SortingByProductID());


                        // Selects and scrolls to added row
                        productsTable.getSelectionModel().select(newProductNumberInt);
                        productsTable.scrollTo(newProductNumberInt);


                        a = false;

                    } else if (editProductBtnClicked) {
                        //Editing Product in the DB
                        productPageData.addEditProductToDB(new Product(selectedProductNumber, description, code, category, size, units, stockLevel, vendor, supplier, price), editNewProduct);
                        productPageData.getProductDataList().clear();
                        productPageData.retriveProductsFromDB();
                        //sorting ProductID column in the Table in ascending order
                        Collections.sort(productPageData.getProductDataList(), new SortingByProductID());

                        a = false;
                    }
                }
            } else
                a = false;
        }
    }


    @FXML
    private void removeProductBtnHandler() throws Exception {

        Product selectedItem = productsTable.getSelectionModel().getSelectedItem();


        if (selectedItem != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Product");
            alert.setHeaderText("Product: " + selectedItem.getDescription());
            alert.setContentText("Do you want to delete selected product?");


            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get().equals(ButtonType.OK)) {

                productPageData.deleteProductFromDB(selectedItem.getId());
                productPageData.getProductDataList().clear();
                productPageData.retriveProductsFromDB();

            } else {

            }
        } else {


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("\"Remove Product\"");
            alert.setContentText("Select product from the table");
            alert.showAndWait();


        }
    }

    @FXML
    private void productSearchBtnHandler() throws SQLException {

        String description = "description";
        String category = "category";
        String vendor = "vendor";
        String supplier = "supplier";
        String stockLevel = "stockLevel";
        String searchWord = productSearchTextField.getText();
        String enteredStockLevel = stockLevelTextField.getText();

        if (descriptionSearchRadioBtn.isSelected()) {
            productPageData.getProductDataList().clear();
            productPageData.retriveSearchProductFromDB(searchWord, description);

        } else if (categorySearchRadioBtn.isSelected()) {
            productPageData.getProductDataList().clear();
            productPageData.retriveSearchProductFromDB(searchWord, category);

        } else if (vendorSearchRadioBtn.isSelected()) {
            productPageData.getProductDataList().clear();
            productPageData.retriveSearchProductFromDB(searchWord, vendor);
        } else if (supplierSearchRadioBtn.isSelected()) {
            productPageData.getProductDataList().clear();
            productPageData.retriveSearchProductFromDB(searchWord, supplier);
        } else if (stockLevelSearchRadioBtn.isSelected() && !enteredStockLevel.isEmpty()) {

            if (isDigitNoDot(enteredStockLevel)) {
                productPageData.getProductDataList().clear();
                productPageData.retriveSearchProductFromDB(enteredStockLevel, stockLevel);

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("field can only contain digit(s)");
                alert.showAndWait();

            }

        }

    }


    @FXML
    private void categoryBtnHandler() throws Exception {

        categoryDialogBoxData.getAddNewCategoryList().clear();
        categoryDialogBoxData.retriveCategoryFromDB();


        Dialog<ButtonType> dialog = new Dialog<>();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../FXMLviewes/CategoryDialogBox.fxml"));

        dialog.getDialogPane().setContent(fxmlLoader.load());
        dialog.setTitle("Add/Delete Catogory ");


        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

        CategoryDialogBoxController categoryDialogBoxController = fxmlLoader.getController();

        Collections.sort(categoryDialogBoxData.getAddNewCategoryList());
        categoryDialogBoxController.setAvailableCategoryListView(categoryDialogBoxData.getAddNewCategoryList());

        //Adding new Category
        categoryDialogBoxController.getAddNewCategoryBtnHandler().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String newCategory = categoryDialogBoxController.getNewCategoryTextField();

                if (!newCategory.isEmpty()) {

                    try {
                        categoryDialogBoxData.addCategoryToDB(newCategory);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    categoryDialogBoxData.getAddNewCategoryList().clear();
                    categoryDialogBoxData.retriveCategoryFromDB();
                    categoryDialogBoxController.setText("");
                    categoryDialogBoxController.getAvailableCategoryListView().getSelectionModel().select(newCategory);
                    categoryDialogBoxController.getAvailableCategoryListView().scrollTo(newCategory);

                    //updates Category ComboBox with a new category
                    productPageData.retriveCategoryComboBoxListFromDB();


                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("\"Enter new category\" field is empty");
                    alert.setContentText("Enter Category");
                    alert.showAndWait();

                }
            }
        });


        //Deleting Selected Category

        categoryDialogBoxController.getDeleteSelectedCategoryBtnHandler().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String category = categoryDialogBoxController.getAvailableCategoryListView().selectionModelProperty().getValue().getSelectedItem();

                if (category != null) {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Selected Category: " + "\"" + category + "\"");
                    alert.setContentText("Do You Want To Delete Selected Category?");

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get().equals(ButtonType.OK)) {

                        try {
                            categoryDialogBoxData.deleteCategoryFromDB(category);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        categoryDialogBoxData.getAddNewCategoryList().clear();
                        categoryDialogBoxData.retriveCategoryFromDB();

                        //updates ComboBox to remove deleted category
                        productPageData.getComboBoxListCategory().clear();
                        productPageData.retriveCategoryComboBoxListFromDB();

                    } else {

                        return;
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Select category to delete");
                    alert.showAndWait();

                }
            }
        });
        Optional<ButtonType> result = dialog.showAndWait();


        if (result.isPresent() && result.get().equals(ButtonType.OK)) {

        }

    }

    @FXML
    private void unitsBtnHandler() throws IOException {

        unitsDialogBoxData.getAddNewUnitsListView().clear();
        unitsDialogBoxData.retriveUnitsFromDB();

        Dialog<ButtonType> dialog = new Dialog<>();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../FXMLviewes/UnitsDialogBox.fxml"));
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

        dialog.getDialogPane().setContent(fxmlLoader.load());
        dialog.setTitle("Add/Delete Units ");


        UnitsDialogBoxController unitsDialogBoxController = fxmlLoader.getController();
        unitsDialogBoxController.setUnitsListView(unitsDialogBoxData.getAddNewUnitsListView());


        //  Adding new Units

        unitsDialogBoxController.getUnitsBtnHandler().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String newUnits = unitsDialogBoxController.getNewUnitsTextField();

                if (!newUnits.isEmpty()) {


                    try {
                        unitsDialogBoxData.addUnitsToDB(newUnits);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    unitsDialogBoxData.getAddNewUnitsListView().clear();
                    unitsDialogBoxData.retriveUnitsFromDB();


                    unitsDialogBoxController.setText("");
                    unitsDialogBoxController.getUnitsListView().getSelectionModel().select(newUnits);
                    unitsDialogBoxController.getUnitsListView().scrollTo(newUnits);

                    //updates ComboBox with new added units
                    productPageData.retriveUnitsComboBoxListFromDB();


                } else {

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("\"Enter new units\" field is empty");
                    alert.setContentText("Enter Units");
                    alert.showAndWait();


                }
            }
        });


        //  Deleteing new Units

        unitsDialogBoxController.getDeleteSelectedUnitsBtnHandler().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {


                String selectedUnits = unitsDialogBoxController.getUnitsListView().selectionModelProperty().getValue().getSelectedItem();


                if (selectedUnits != null) {


                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Selected Units: " + "\"" + selectedUnits + "\"");
                    alert.setContentText("Do You Want To Delete Selected Units?");

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get().equals(ButtonType.OK)) {

                        try {
                            unitsDialogBoxData.deleteUnitsFromDB(selectedUnits);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        unitsDialogBoxData.getAddNewUnitsListView().clear();
                        unitsDialogBoxData.retriveUnitsFromDB();

                        //updates ComboBox to remove deleted units
                        productPageData.getComboBoxListUnits().clear();
                        productPageData.retriveUnitsComboBoxListFromDB();

                    } else {

                        return;
                    }

                } else {

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Select Units to Delete");
                    alert.showAndWait();

                }
            }
        });


        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get().equals(ButtonType.CANCEL)) {

            return;
        }

    }


    // method to check if supplied text consists of digits and no more than one dot.
    private boolean isDigit(String text) {

        int a = 0;
        String xxx = "";
        StringBuilder concChar = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {

            boolean xx = Character.isDigit(text.charAt(i));
            if (!xx) {
                a = i;
                if ((text.charAt(a) == '.')) {
                    concChar.append(text.charAt(a));
                    xxx = concChar.toString() + xxx;
                    a = 0;
                    if (xxx.equals("...")) {
                        a = a + i;
                        break;
                    } else ;
                } else ;

            } else ;
        }
        return Character.isDigit(text.charAt(a));
    }


    // method to check if supplied text consists of digits only.
    private boolean isDigitNoDot(String text) {

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


    // class sorting items numerically
    class SortingByProductID implements Comparator<Product> {

        @Override
        public int compare(Product o1, Product o2) {
            if (o1.getId() > o2.getId()) {

                return 1;
            } else if (o1.getId() < o2.getId()) {

                return -1;
            } else

                return 0;
        }
    }


}
