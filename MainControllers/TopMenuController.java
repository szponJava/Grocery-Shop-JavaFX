package au.com.einsporn.MainControllers;

import au.com.einsporn.DataModel.*;
import au.com.einsporn.Main;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.stage.WindowEvent;


import java.io.*;


import static java.sql.Types.NULL;

public class TopMenuController {

    @FXML
    MenuBar menuBar;
    @FXML
    Menu menuFile,menuAbout;
    @FXML
    MenuItem saveCustomersTableAsMenuItem;

    private CustomerPageData customerPageData;
    private ProductPageData productPageData;
    private SupplierPageData supplierPageData;
    private SalesPageData salesPageData;
    private ShopPageData shopPageData;

    private FileWriter localFile = null;


    public TopMenuController() {

        customerPageData = new CustomerPageData();
        productPageData = new ProductPageData();
        supplierPageData = new SupplierPageData();
        salesPageData = new SalesPageData();
        shopPageData = new ShopPageData();

    }


    @FXML
    private void saveCustomersTableAsMenuItem() throws IOException {

        //saving Customers details in the text file(*.txt).Columns are separated by semicolon.

        customerPageData.retriveCustomersFromDB();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Dialog");
        fileChooser.setInitialFileName("Customers");

        fileChooser.getInitialFileName();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("text File","*.txt"));

        File file = fileChooser.showSaveDialog(Main.getRoot().getScene().getWindow());


        if(file!=null){

            localFile = new FileWriter(file.getAbsolutePath());

            for(int i=0; i<customerPageData.getCustomersDataList().size();i++){

                String a = String.valueOf(customerPageData.getCustomersDataList().get(i).getCustomerNumber());
                String b = customerPageData.getCustomersDataList().get(i).getFirstName();
                String c = customerPageData.getCustomersDataList().get(i).getLastName();
                String d = customerPageData.getCustomersDataList().get(i).getAddress();
                String e = customerPageData.getCustomersDataList().get(i).getSuburb();
                String f = customerPageData.getCustomersDataList().get(i).getState();
                String g = customerPageData.getCustomersDataList().get(i).getPostCode();

                localFile.write(a+"; "+b+"; "+c+"; "+d+"; "+e+"; "+f+"; "+g+"; " + "\n");

            }
            localFile.close();
        }
    }


    @FXML
    private void saveProductsTableMenuItem() throws IOException {

        //saving Products details in the text file(*.txt).Columns are separated by semicolon.

        productPageData.retriveProductsFromDB();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Dialog");
        fileChooser.setInitialFileName("Products");
        fileChooser.getInitialFileName();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("text File","*.txt"));

        File file = fileChooser.showSaveDialog(Main.getRoot().getScene().getWindow());



        if(file!=null) {

            localFile = new FileWriter(file.getAbsolutePath());

            for (int i = 0; i < productPageData.getProductDataList().size(); i++) {

                String a = String.valueOf(productPageData.getProductDataList().get(i).getId());
                String b = productPageData.getProductDataList().get(i).getDescription();
                String c = productPageData.getProductDataList().get(i).getCode();
                String d = productPageData.getProductDataList().get(i).getCategory();
                String e = productPageData.getProductDataList().get(i).getSize();
                String f = productPageData.getProductDataList().get(i).getUnit();
                String g = productPageData.getProductDataList().get(i).getStockLevel();
                String h = productPageData.getProductDataList().get(i).getVendor();
                String ii = productPageData.getProductDataList().get(i).getMainSupplier();
                String j = productPageData.getProductDataList().get(i).getPrice();


                localFile.write(a + "; " + b + "; " + c + "; " + d + "; " + e + "; " + f + "; " + g + "; " + h + "; " + ii + "; " + j+"; " + "\n");

            }
            localFile.close();
        }
    }


    public void saveSuppliersTableMenuItem() throws IOException {

        //saving Suppliers in the text file(*.txt).Columns are separated by semicolon.

        supplierPageData.retriveSupplierFromDB();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Dialog");
        fileChooser.setInitialFileName("Suppliers");
        fileChooser.getInitialFileName();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("text File","*.txt"));

        File file = fileChooser.showSaveDialog(Main.getRoot().getScene().getWindow());

        if(file!=null){
        localFile = new FileWriter(file.getAbsolutePath());

        for(int i = 0; i<supplierPageData.getSuppliersDataList().size(); i++){

            String a = String.valueOf(supplierPageData.getSuppliersDataList().get(i).getSupplierId());
            String b = supplierPageData.getSuppliersDataList().get(i).getName();
            String c = supplierPageData.getSuppliersDataList().get(i).getEmail();
            String d = supplierPageData.getSuppliersDataList().get(i).getPhone();
            String e = supplierPageData.getSuppliersDataList().get(i).getAddress();

            localFile.write(a+"; "+b+"; "+c+"; "+d+"; "+e+"; "+ "\n");

        }
        localFile.close();

        }
    }




    public void saveSalesTableMenuItem() throws IOException {

        //saving Sales in the text file(*.txt).Columns are separated by semicolon.

        salesPageData.retriveSalesFromDB();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Dialog");
        fileChooser.setInitialFileName("Sales");
        fileChooser.getInitialFileName();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("text File","*.txt"));

        File file = fileChooser.showSaveDialog(Main.getRoot().getScene().getWindow());

        if(file!=null) {

            localFile = new FileWriter(file.getAbsolutePath());

            for (int i = 0; i < salesPageData.getSalesDataList().size(); i++) {

                String a = String.valueOf(salesPageData.getSalesDataList().get(i).getSaleNumber());
                String b = salesPageData.getSalesDataList().get(i).getDate();
                String c = salesPageData.getSalesDataList().get(i).getTime();
                String d = salesPageData.getSalesDataList().get(i).getCustomer();
                String e = salesPageData.getSalesDataList().get(i).getProduct();
                String f = salesPageData.getSalesDataList().get(i).getCode();
                String g = salesPageData.getSalesDataList().get(i).getSize();
                String h = salesPageData.getSalesDataList().get(i).getUnits();
                String ii = salesPageData.getSalesDataList().get(i).getCategory();
                String j = salesPageData.getSalesDataList().get(i).getQuantity();
                String k = salesPageData.getSalesDataList().get(i).getItemPrice();
                String l = salesPageData.getSalesDataList().get(i).getSalePrice();


                localFile.write(a + "; " + b + "; " + c + "; " + d + "; " + e + "; " + f + "; " + g + "; " + h + "; " + ii + "; " + j + "; " + k + "; " + l +"; "+ "\n");

            }
            localFile.close();
        }
    }



    public void exitMenuItem() throws Exception {


        shopPageData.clearCustomerBasketItemsFromDB();
        customerPageData.updateSelectedCutomerInDB(NULL, "Not Selected", "");

        Main.getStage().close();


    }


    public void aboutMenuItem() throws IOException {


        Dialog<ButtonType> dialog = new Dialog<>();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../FXMLviewes/aboutDialogBox.fxml"));
        dialog.getDialogPane().setContent(fxmlLoader.load());
        dialog.setTitle("About");
        Window window = dialog.getDialogPane().getScene().getWindow();

        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                dialog.close();
            }
        });

        dialog.showAndWait();


    }




}
