package au.com.einsporn;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import au.com.einsporn.DataModel.CustomerPageData;
import au.com.einsporn.DataModel.ShopPageData;

import static java.sql.Types.NULL;

public class Main extends Application {

    private static Stage windows = new Stage();
    private final ShopPageData shopPageData = new ShopPageData();
    private final CustomerPageData customerPageData = new CustomerPageData();

    private static BorderPane root = new BorderPane();

    public static BorderPane getRoot() {

        return root;
    }

    public static Stage getStage(){
        return windows;

    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        windows = primaryStage;


        BorderPane top = FXMLLoader.load(getClass().getResource("FXMLviewes/TopMenu.fxml"));
        BorderPane left = FXMLLoader.load(getClass().getResource("FXMLviewes/LeftMenu.fxml"));
        GridPane center = FXMLLoader.load(getClass().getResource("FXMLviewes/ShopPage.fxml"));

        BorderPane.setAlignment(left, Pos.TOP_RIGHT);
        BorderPane.setMargin(left, new Insets(20, 5, 5, 5));

        BorderPane.setAlignment(center, Pos.TOP_LEFT);


        root.setLeft(left);
        root.setTop(top);
        root.setCenter(center);


        windows.setTitle("Grocery Shop");
        windows.setScene(new Scene(root, 1000, 600));

        windows.show();



        windows.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                try {
                    shopPageData.clearCustomerBasketItemsFromDB();
                    customerPageData.updateSelectedCutomerInDB(NULL,"Not Selected","");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }



    public static void main(String[] args) {
        launch(args);
    }
}
