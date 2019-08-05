package au.com.einsporn.MainControllers;

import au.com.einsporn.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;


import java.io.IOException;

public class LeftMenuController {

    @FXML
    private Button shopLeftMenuBtn, customerLeftMenuBtn, productLeftMenuBtn,supplierLeftMenuBtn, salesLeftMenuBtn;



    @FXML
    private void shopPageBtnHandle() throws IOException {

        GridPane customerPage = FXMLLoader.load(getClass().getResource("../FXMLviewes/ShopPage.fxml"));

        BorderPane customerPage1 = Main.getRoot();

        customerPage1.setCenter(customerPage);
        customerLeftMenuBtn.setStyle(null);
        productLeftMenuBtn.setStyle(null);
        supplierLeftMenuBtn.setStyle(null);
        shopLeftMenuBtn.setStyle("-fx-background-color:#aad1ff; -fx-border-color:green");
        salesLeftMenuBtn.setStyle(null);

    }


    @FXML
    private void CustomerPageBtnHandle() throws IOException {


        /* GridPane customerPage = FXMLLoader.load(getClass().getResource("FXMLviewes/CustomerPage.fxml"));*/


        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../FXMLviewes/CustomerPage.fxml"));
        GridPane customerPage = fxmlLoader.load();

        BorderPane customerPage1 = Main.getRoot();
        customerPage1.setCenter(customerPage);

        shopLeftMenuBtn.setStyle(null);
        productLeftMenuBtn.setStyle(null);
        supplierLeftMenuBtn.setStyle(null);
        customerLeftMenuBtn.setStyle("-fx-background-color:#aad1ff; -fx-border-color:green");
        salesLeftMenuBtn.setStyle(null);

    }


    @FXML
    private void productPageBtnHandler() throws IOException {

        GridPane customerPage = FXMLLoader.load(getClass().getResource("../FXMLviewes/ProductPage.fxml"));

        BorderPane customerPage1 = Main.getRoot();

        customerPage1.setCenter(customerPage);

        shopLeftMenuBtn.setStyle(null);
        productLeftMenuBtn.setStyle("-fx-background-color:#aad1ff; -fx-border-color:green");
        supplierLeftMenuBtn.setStyle(null);
        customerLeftMenuBtn.setStyle(null);
        salesLeftMenuBtn.setStyle(null);

    }


    @FXML
    private void supplierPageBtnHandler() throws IOException {

        GridPane customerPage = FXMLLoader.load(getClass().getResource("../FXMLviewes/SupplierPage.fxml"));

        BorderPane customerPage1 = Main.getRoot();

        customerPage1.setCenter(customerPage);

        shopLeftMenuBtn.setStyle(null);
        productLeftMenuBtn.setStyle(null);
        customerLeftMenuBtn.setStyle(null);
        supplierLeftMenuBtn.setStyle("-fx-background-color:#aad1ff; -fx-border-color:green");
        salesLeftMenuBtn.setStyle(null);

    }


    @FXML
    private void salesPageBtnHandler() throws IOException {

        GridPane customerPage = FXMLLoader.load(getClass().getResource("../FXMLviewes/SalesPage.fxml"));

        BorderPane customerPage1 = Main.getRoot();

        customerPage1.setCenter(customerPage);

        shopLeftMenuBtn.setStyle(null);
        productLeftMenuBtn.setStyle(null);
        customerLeftMenuBtn.setStyle(null);
        supplierLeftMenuBtn.setStyle(null);
        salesLeftMenuBtn.setStyle("-fx-background-color:#aad1ff; -fx-border-color:green");


    }

}
