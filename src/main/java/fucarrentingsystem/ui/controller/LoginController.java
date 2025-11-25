package fucarrentingsystem.ui.controller;

import fucarrentingsystem.entity.Customer;
import fucarrentingsystem.config.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;

public class LoginController {

    @FXML
    private TextField txtUsername; // admin: 'admin', customer: email

    @FXML
    private PasswordField txtPassword;

    @FXML
    private void handleLogin(ActionEvent event) {
        String user = txtUsername.getText().trim();
        String pass = txtPassword.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            showError("Please enter username and password");
            return;
        }

        try {
            if (user.equals("admin") && pass.equals("admin")) {
                // open admin view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin-main.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("FU Car Renting System - Admin");
                stage.show();
                return;
            }

            // customer login by email + password
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Customer customer = session.createQuery(
                                "FROM Customer c WHERE c.email = :email AND c.password = :pwd",
                                Customer.class)
                        .setParameter("email", user)
                        .setParameter("pwd", pass)
                        .uniqueResult();

                if (customer == null) {
                    showError("Invalid credentials");
                    return;
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customer-main.fxml"));
                Parent root = loader.load();
                CustomerMainController controller = loader.getController();
                controller.setLoggedInCustomer(customer);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("FU Car Renting System - Customer");
                stage.show();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            showError("System error: " + ex.getMessage());
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
