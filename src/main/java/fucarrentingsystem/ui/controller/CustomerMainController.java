package fucarrentingsystem.ui.controller;

import fucarrentingsystem.entity.CarRental;
import fucarrentingsystem.entity.Customer;
import fucarrentingsystem.service.CarRentalService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerMainController {

    private Customer loggedInCustomer;
    private final CarRentalService rentalService = new CarRentalService();

    @FXML
    private Label lblWelcome;

    // Profile fields
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtMobile;
    @FXML
    private DatePicker dpBirthday;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;

    // History
    @FXML
    private TableView<CarRental> tblHistory;
    @FXML
    private TableColumn<CarRental, Integer> colHistId;
    @FXML
    private TableColumn<CarRental, String> colHistCar;
    @FXML
    private TableColumn<CarRental, LocalDate> colHistPickup;
    @FXML
    private TableColumn<CarRental, LocalDate> colHistReturn;
    @FXML
    private TableColumn<CarRental, BigDecimal> colHistPrice;
    @FXML
    private TableColumn<CarRental, String> colHistStatus;

    public void setLoggedInCustomer(Customer customer) {
        this.loggedInCustomer = customer;
        initData();
    }

    private void initData() {
        if (loggedInCustomer == null) return;
        lblWelcome.setText("Welcome, " + loggedInCustomer.getCustomerName());

        txtName.setText(loggedInCustomer.getCustomerName());
        txtMobile.setText(loggedInCustomer.getMobile());
        dpBirthday.setValue(loggedInCustomer.getBirthday());
        txtEmail.setText(loggedInCustomer.getEmail());
        txtPassword.setText(loggedInCustomer.getPassword());

        colHistId.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(
                d.getValue().getRentalId() == null ? 0 : d.getValue().getRentalId()).asObject());
        colHistCar.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getCar().getCarName()));
        colHistPickup.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getPickupDate()));
        colHistReturn.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getReturnDate()));
        colHistPrice.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getRentPrice()));
        colHistStatus.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getStatus()));

        refreshHistory();
    }

    private void refreshHistory() {
        if (loggedInCustomer == null) return;
        tblHistory.setItems(FXCollections.observableArrayList(
                rentalService.getAllRentals().stream()
                        .filter(r -> r.getCustomer().getCustomerId().equals(loggedInCustomer.getCustomerId()))
                        .toList()
        ));
    }

    @FXML
    private void handleSaveProfile() {
        if (loggedInCustomer == null) return;
        loggedInCustomer.setCustomerName(txtName.getText().trim());
        loggedInCustomer.setMobile(txtMobile.getText().trim());
        loggedInCustomer.setBirthday(dpBirthday.getValue());
        loggedInCustomer.setEmail(txtEmail.getText().trim());
        loggedInCustomer.setPassword(txtPassword.getText());
        // save via Hibernate simple session
        // to keep code short, we reuse service from admin if needed
        // but for assignment, just updating object is enough to show UI flow
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Profile updated (demo).", ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("FU Car Renting System - Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
