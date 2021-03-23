/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package e1262;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author hydro
 */
public class AlarmScreenController implements Initializable {

    Thread mode, read, insert_alarm;
    private Service<Void> TimeBackground_alarm;
    String current_machine_mode, previous_hydro_set_pressure, current_offline_mode, current_oil_level, current_air_inlet, current_temperature, current_5hp, current_2hp, current_m3, current_m4, current_m5;
    DatabaseHandler dh = new DatabaseHandler();
    Connection connect = dh.MakeConnection();

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private HBox sectionHeader1;
    @FXML
    private Text txtMode;
    @FXML
    private Text txtDate;
    @FXML
    private ImageView imgEmergency;
    @FXML
    private Text txtOffline;
    @FXML
    private HBox sectionHeader;
    @FXML
    private JFXButton btnHome;
    @FXML
    private JFXButton btnInitial;
    @FXML
    private JFXButton btnReport;
    @FXML
    private JFXButton btnAdmin;
    @FXML
    private JFXButton btnOilLevel;
    @FXML
    private JFXButton btnAirInlet;
    @FXML
    private JFXButton btnTemperature;
    @FXML
    private JFXButton btnHM5Hp;
    @FXML
    private JFXButton btnHM2Hp;
    @FXML
    private JFXButton btnMotor3;
    @FXML
    private JFXButton btnMotor4;
    @FXML
    private JFXButton btnMotor5;
    @FXML
    private HBox sectionFooter;
    private JFXButton btnAlarm;

    public static volatile boolean stop_mode = false;
    @FXML
    private ImageView imgAuto;
    @FXML
    private ImageView imgManual;
    @FXML
    private JFXButton btnHelp;
    @FXML
    private JFXButton btnSystemCheck;

    private void machine_mode() {
        stop_mode = false;
        mode = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(150);
                    if (stop_mode) {
                        break;
                    }
                    String display = "SELECT * FROM alarm_tags ORDER BY alarm_tags_id DESC LIMIT 1";
                    ResultSet rs = dh.getData(display, connect);
                    if (rs.next()) {
                        if (rs.getString("offline_online").equals(current_offline_mode)) {
                        } else {
                            offline_online(rs.getString("offline_online"));
                        }
                        if (stop_mode) {
                            break;
                        }
                        if (rs.getString("machine_mode").equals(current_machine_mode)) {
                        } else {
                            mode(rs.getString("machine_mode"));
                        }
                        if (stop_mode) {
                            break;
                        }
                        if (rs.getString("oil_level").equals(current_oil_level)) {
                        } else {
                            String value = rs.getString("oil_level");
                            Platform.runLater(() -> {
                                oil_level(value);
                            });
                        }
                        if (stop_mode) {
                            break;
                        }
                        if (rs.getString("air_inlet").equals(current_air_inlet)) {
                        } else {
                            String value = rs.getString("air_inlet");
                            Platform.runLater(() -> {
                                air_inlet(value);
                            });
                        }
                        if (stop_mode) {
                            break;
                        }
                        if (rs.getString("temperature").equals(current_temperature)) {
                        } else {
                            String value = rs.getString("temperature");
                            Platform.runLater(() -> {
                                temperature(value);
                            });
                        }
                        //MOTOR PART
                        if (stop_mode) {
                            break;
                        }
                        if (rs.getString("hydraulic_motor_5").equals(current_5hp)) {
                        } else {
                            String value = rs.getString("hydraulic_motor_5");
                            Platform.runLater(() -> {
                                motor_status(value, btnHM5Hp, current_5hp);
                            });
                        }
                        if (stop_mode) {
                            break;
                        }
                        if (rs.getString("hydraulic_motor_2").equals(current_2hp)) {
                        } else {
                            String value = rs.getString("hydraulic_motor_2");
                            Platform.runLater(() -> {
                                motor_status(value, btnHM2Hp, current_2hp);
                            });
                        }
                        if (stop_mode) {
                            break;
                        }
                        if (rs.getString("motor_3").equals(current_m3)) {
                        } else {
                            String value = rs.getString("motor_3");
                            Platform.runLater(() -> {
                                motor_status(value, btnMotor3, current_m3);
                            });
                        }
                        if (stop_mode) {
                            break;
                        }
                        if (rs.getString("motor_4").equals(current_m4)) {
                        } else {
                            String value = rs.getString("motor_4");
                            Platform.runLater(() -> {
                                motor_status(value, btnMotor4, current_m4);
                            });
                        }
                        if (stop_mode) {
                            break;
                        }
                        if (rs.getString("motor_5").equals(current_m5)) {
                        } else {
                            String value = rs.getString("motor_5");
                            Platform.runLater(() -> {
                                motor_status(value, btnMotor5, current_m5);
                            });
                        }
                    }

                } catch (InterruptedException | SQLException e) {

                }
                if (stop_mode) {
                    break;
                }
            }
        }, "machineModeThreadAlarmScreen");
        mode.setDaemon(true);
        mode.start();

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Initialize intial screen
        Thread Initialize_Initial = new Thread(() -> {
            Background_Processes.Initialize_Initial_Screen();
            System.out.println("Initialized");
        }, "initializeIntialScreenThread");
        Initialize_Initial.setDaemon(true);
        Initialize_Initial.start();
        // TODO
        if (Session.get("user_type").equals("admin")) {
            btnAdmin.setVisible(true);
        } else {
            btnAdmin.setVisible(false);
        }
        imgEmergency.setVisible(false);
        imgManual.setVisible(false);
        imgAuto.setVisible(false);

        try {
            current_2hp = "2131";
            current_5hp = "2121";
            current_air_inlet = "2121";
            current_m3 = "2121";
            current_m4 = "2121";
            current_m5 = "2121";
            current_machine_mode = "2121";
            current_offline_mode = "2121";
            current_oil_level = "2121";
            current_temperature = "2121";
            btnHome.setFocusTraversable(false);
            btnAdmin.setFocusTraversable(false);
            btnInitial.setFocusTraversable(false);
            btnReport.setFocusTraversable(false);

            Background_Processes.insert_plc_data("python include/insert_alarm_tags.py " + DatabaseHandler.DB_HOST + " " + DatabaseHandler.DB_USER + " " + DatabaseHandler.DB_PASS + " " + DatabaseHandler.DB_NAME + " truncate_alarm_sp insert_alarm_sp", false, true);
            machine_mode();
            Background_Processes.date_time(txtDate, false, false,"alarmScreenDateTimeThread");
        } catch (Exception e) {
        }

    }

    @FXML
    private void btnHomeAction(ActionEvent event) throws IOException {
        dropbox("LoginScreen.fxml", false);
    }

    @FXML
    private void btnReportAction(ActionEvent event) throws IOException {
        dropbox("Report.fxml", false);
    }

    @FXML
    private void btnAdminAction(ActionEvent event) throws IOException {
        dropbox("AdminScreen.fxml", false);
    }

    private void btnInitialAction(ActionEvent event) throws IOException {
        dropbox("InitialScreen.fxml", true);
    }

    /**
     * Oil Level selector
     */
    private void oil_level(String olevel) {

        switch (olevel) {
            case "0":
                btnOilLevel.setText("Low");
                btnOilLevel.setStyle("-fx-background-color:#ac0800;");
                current_oil_level = "0";
                break;
            case "1":
                btnOilLevel.setText("Medium");
                btnOilLevel.setStyle("-fx-background-color:#4b636e;");
                current_oil_level = "1";
                break;
            case "2":
                btnOilLevel.setText("High");
                btnOilLevel.setStyle("-fx-background-color:#388e3c;");
                current_oil_level = "2";
                break;
            default:
                current_oil_level = "0";
                break;
        }
    }

    /**
     * Oil Level selector
     */
    private void air_inlet(String alevel) {
        switch (alevel) {
            case "0":
                btnAirInlet.setText("Low");
                btnAirInlet.setStyle("-fx-background-color:#ac0800;");
                current_air_inlet = "0";
                break;
                //as per requirement
                 //if required uncomment below code to indicate medium air range
//            case "1":
//                btnAirInlet.setText("Medium");
//                btnAirInlet.setStyle("-fx-background-color:#4b636e;");
//                current_air_inlet = "1";
//                break;
            case "1":
                btnAirInlet.setText("High");
                btnAirInlet.setStyle("-fx-background-color:#388e3c");
                current_air_inlet = "1";
                break;
            default:
                current_air_inlet = "0";
                break;
        }
    }

    /**
     * Temperature selector
     */
    private void temperature(String Tlevel) {
        switch (Tlevel) {
            case "0":
                btnTemperature.setText("Low");
                btnTemperature.setStyle("-fx-background-color:#388e3c;");
                current_temperature = "0";
                break;
            case "1":
                btnTemperature.setText("Medium");
                btnTemperature.setStyle("-fx-background-color:#4b636e;");
                current_temperature = "1";
                break;
            case "2":
                btnTemperature.setText("High");
                btnTemperature.setStyle("-fx-background-color:#ac0800;");
                current_temperature = "2";
                break;
            default:
                current_temperature = "0";
                break;
        }
    }

    /**
     * Motor status
     */
    private void motor_status(String status, JFXButton button, String value_changes) {
        switch (status) {
            case "0":
                button.setText("OFF");
                button.setStyle("-fx-background-color:#4b636e;");
                value_changes = "0";
                break;
            case "1":
                button.setText("ON");
                button.setStyle("-fx-background-color:#388e3c;");
                value_changes = "1";
                break;
            case "2":
                button.setText("TRIP");
                button.setStyle("-fx-background-color:#ac0800;");
                value_changes = "2";
                break;
            default:
                value_changes = "0";
                break;
        }
    }

    /**
     * offline online mode selector
     */
    private void offline_online(String off_on) {
        switch (off_on) {
            case "0":
                txtOffline.setText("Online");
                current_offline_mode = "0";
                break;
            case "1":
                txtOffline.setText("Offline");
                current_offline_mode = "1";
                break;
            default:
                txtOffline.setText("Something went wrong");
                current_offline_mode = "0";
                break;
        }
    }

    /**
     * machine mode selector
     */
    private void mode(String mode) {
        switch (mode) {
            case "0":
                txtMode.setText("Auto Mode");
                current_machine_mode = "0";
                txtDate.setFill(Color.web("#0099FF"));
                imgEmergency.setVisible(false);
                imgManual.setVisible(false);
                imgAuto.setVisible(true);
                break;
            case "1":
                txtMode.setText("Manual Mode");
                current_machine_mode = "1";
                txtDate.setFill(Color.web("#0099FF"));
                imgEmergency.setVisible(false);
                imgManual.setVisible(true);
                imgAuto.setVisible(false);
                break;
            case "2":
                txtMode.setText("Emergency Mode");
                current_machine_mode = "2";
                txtDate.setFill(Color.web("#C32420"));
                imgEmergency.setVisible(true);
                imgManual.setVisible(false);
                imgAuto.setVisible(false);
                break;
            default:
                txtMode.setText("Something wrong");
                current_machine_mode = "0";
                txtDate.setFill(Color.web("#C32420"));
                imgEmergency.setVisible(true);
                imgManual.setVisible(false);
                imgAuto.setVisible(false);
                break;
        }
    }

    /**
     * change screen
     *
     * @param a
     * @param initial_check
     * @throws java.io.IOException
     */
    public void dropbox(String a, boolean initial_check) throws IOException {
        try {
            Background_Processes.stop_plc_read();
        } catch (Exception e) {
            System.out.println("Exception while stoping insert_plc_thread_Alram_Screen : "+ e.getMessage());
        }
        try {
            Background_Processes.stop_date_time();
        } catch (Exception e) {
            System.out.println("Exception while stoping date_time_thread_Alram_Screen : "+ e.getMessage());
        }
        //Stoping machine mode thread
        stop_mode = true;
//        if (initial_check) {
//            Background_Processes.Initialize_Initial_Screen();
//        }
        Platform.runLater(() -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource(a));
                ToolKit.loadScreen(root);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        ToolKit.unloadScreen(btnAlarm);

    }

   @FXML
    private void btnHelpAction(ActionEvent event) {
        ToolKit.excecuteScript("xdg-open /opt/380mtManual.pdf");
    }

    @FXML
    private void btnSystemCheckAction(ActionEvent event) {
    }

}
