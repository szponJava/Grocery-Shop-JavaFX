package au.com.einsporn.MainControllers;

import au.com.einsporn.DataModel.Sales;
import au.com.einsporn.DataModel.SalesPageData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;


public class SalesPageController {


    @FXML
    private TableView<Sales> salesTable = new TableView<>();
    @FXML
    private RadioButton saleNumberRadioBtn,dateRadioBtn;
    @FXML
    private TextField saleNoFromTextField,saleNoToTextField,selectFilterTextField;
    @FXML
    private DatePicker dateFromDatePicker,dateToDatePicker;
    @FXML
    private TextField totalPriceTextField;
    @FXML
    private Label salesNumberLabel;
    @FXML
    private ComboBox<String> selectFilterComboBox;
    @FXML
    private Label salesLabel;

    private ObservableList<String> selectFilterOptions;
    private SalesPageData salesPageData;

    private String selectedDateFrom;
    private String selectedDateTo;

    public SalesPageController() {

    }

    public void initialize() throws Exception {


        salesPageData = new SalesPageData();



        salesTable.setItems(salesPageData.getSalesDataList());
        salesPageData.retriveSalesFromDB();
        //sorts SalesDataList by Sale ID
        Collections.sort(salesPageData.getSalesDataList(),new SortingBySaleID());


        totalSalePriceAndNumberCalculation();
        totalPriceTextField.setEditable(false);

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(saleNumberRadioBtn,dateRadioBtn);

        //setting combo box with filter options
        selectFilterOptions = FXCollections.observableArrayList();
        selectFilterComboBox.setItems(salesPageData.getSelectFilterOptionsList());

        saleNoFromTextField.setDisable(true);
        saleNoToTextField.setDisable(true);
        selectFilterTextField.setDisable(true);
        dateFromDatePicker.setDisable(true);
        dateToDatePicker.setDisable(true);



        Image image = new Image(getClass().getResourceAsStream("../Resources/sales.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        salesLabel.setGraphic(imageView);





        String pattern="dd-MM-yyy";

        // changing date display format of date picker to dd-MM-yyy
        dateFromDatePicker.setConverter(new StringConverter<LocalDate>() {

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {

                if (date!= null) {
                    return dateTimeFormatter.format(date);

                } else return "";

            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateTimeFormatter);
                } else {
                    return null;
                }}
        });



        // changing date display format of date picker to dd-MM-yyy
        dateToDatePicker.setConverter(new StringConverter<LocalDate>() {

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {

                if (date!= null) {
                    return dateTimeFormatter.format(date);

                } else return "";

            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateTimeFormatter);
                } else {
                    return null;
                }}
        });



        saleNumberRadioBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {


                saleNoFromTextField.setDisable(false);
                saleNoToTextField.setDisable(false);

                selectFilterComboBox.getSelectionModel().clearSelection();
                selectFilterTextField.setDisable(true);
                selectFilterTextField.setDisable(true);
                dateFromDatePicker.setDisable(true);
                dateToDatePicker.setDisable(true);
            }
        });


        dateRadioBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                selectFilterComboBox.getSelectionModel().clearSelection();
                saleNoFromTextField.setDisable(true);
                saleNoToTextField.setDisable(true);
                selectFilterTextField.setDisable(true);
                selectFilterTextField.setDisable(true);

                dateFromDatePicker.setDisable(false);
                dateToDatePicker.setDisable(false);

            }
        });


        selectFilterComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {


                selectFilterTextField.setDisable(false);

                saleNoFromTextField.setDisable(true);
                saleNoToTextField.setDisable(true);
                dateFromDatePicker.setDisable(true);
                dateToDatePicker.setDisable(true);

                saleNumberRadioBtn.setSelected(false);
                dateRadioBtn.setSelected(false);


            }
        });



        dateFromDatePicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (dateFromDatePicker.getValue()!= null) {

                    selectedDateFrom = dateFromDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MMM-yyy"));

                }

            }
        });



        dateToDatePicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (dateToDatePicker.getValue() != null) {
                    selectedDateTo = dateToDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MMM-yyy"));
                }
            }
        });

    }


    @FXML
    private   void filterBtnHandler() throws SQLException {


        String saleNumberFrom = saleNoFromTextField.getText();
        String saleNumberTo = saleNoToTextField.getText();


        if(saleNumberRadioBtn.isSelected()) {

            if (!saleNumberFrom.isEmpty() && !saleNumberTo.isEmpty()) {
                salesPageData.getSalesDataList().clear();

                //retriving filtered rows from the DB
                salesPageData.retriveSearchSaleDataFromDB(saleNumberFrom,saleNumberTo,"SaleNumber");
                //sorts SalesDataList by Sale ID
                Collections.sort(salesPageData.getSalesDataList(),new SortingBySaleID());


                // calculating and displaying Total price for filtered rows
                totalSalePriceAndNumberCalculation();
            }

        } else if (dateRadioBtn.isSelected()){


            if (dateFromDatePicker.getValue()!=null && dateToDatePicker.getValue()!= null) {
                salesPageData.getSalesDataList().clear();
                salesPageData.retriveSearchSaleDataFromDB(selectedDateFrom,selectedDateTo,"dateSelected");
                //sorts SalesDataList by Sale ID
                Collections.sort(salesPageData.getSalesDataList(),new SortingBySaleID());

                // calculating and displaying Total price for filtered rows
                totalSalePriceAndNumberCalculation();

            }else {


            }


        } else if(!selectFilterComboBox.getSelectionModel().isEmpty()) {

            String selectedItem = selectFilterComboBox.getSelectionModel().getSelectedItem();


            if(selectedItem.equals("Customer Name")) {

                salesPageData.getSalesDataList().clear();
                salesPageData.retriveSearchSaleDataFromDB(selectFilterTextField.getText(),"","customerName");
                //sorts SalesDataList by Sale ID
                Collections.sort(salesPageData.getSalesDataList(),new SortingBySaleID());
                // calculating and displaying Total price for filtered rows
                totalSalePriceAndNumberCalculation();

            } else if(selectedItem.equals("Product Name")){

                salesPageData.getSalesDataList().clear();
                salesPageData.retriveSearchSaleDataFromDB(selectFilterTextField.getText(),"","productName");
                //sorts SalesDataList by Sale ID
                Collections.sort(salesPageData.getSalesDataList(),new SortingBySaleID());
                // calculating and displaying Total price for filtered rows
                totalSalePriceAndNumberCalculation();

            }

            else if(selectedItem.equals("Category")){

                salesPageData.getSalesDataList().clear();
                salesPageData.retriveSearchSaleDataFromDB(selectFilterTextField.getText(),"","category");
                //sorts SalesDataList by Sale ID
                Collections.sort(salesPageData.getSalesDataList(),new SortingBySaleID());
                // calculating and displaying Total price for filtered rows
                totalSalePriceAndNumberCalculation();

            }

        }

    }

    @FXML
    private void clearFilterBtnHandler(){


        saleNoFromTextField.clear();
        saleNoToTextField.clear();
        saleNoFromTextField.setDisable(true);
        saleNoToTextField.setDisable(true);


        dateFromDatePicker.setValue(null);
        dateToDatePicker.setValue(null);
        dateFromDatePicker.setDisable(true);
        dateToDatePicker.setDisable(true);

        selectFilterComboBox.getSelectionModel().clearSelection();
        selectFilterTextField.setDisable(true);

        saleNumberRadioBtn.setSelected(false);
        dateRadioBtn.setSelected(false);


        selectFilterTextField.clear();
        salesPageData.getSalesDataList().clear();
        salesPageData.retriveSalesFromDB();

        totalSalePriceAndNumberCalculation();



}

private void totalSalePriceAndNumberCalculation(){

    DecimalFormat df = new DecimalFormat("0.00");


        Float totalPrice =0.0f;
        Integer saleNumber = 0;

    for (int i = 0; i < salesPageData.getSalesDataList().size(); i++) {

        totalPrice+=Float.parseFloat(salesPageData.getSalesDataList().get(i).getSalePrice());
        saleNumber+=Integer.parseInt(salesPageData.getSalesDataList().get(i).getQuantity());


    }
    totalPriceTextField.clear();
    totalPriceTextField.setText(String.valueOf(df.format(totalPrice)));
    salesNumberLabel.setText(String.valueOf(saleNumber));

}

    // class sorting Sale ID column numerically
    class SortingBySaleID implements Comparator<Sales> {

        @Override
        public int compare(Sales o1, Sales o2) {
            if (o1.getSaleNumber() > o2.getSaleNumber()) {

                return 1;
            } else if (o1.getSaleNumber() < o2.getSaleNumber()) {

                return -1;
            } else

                return 0;
        }
    }



}


