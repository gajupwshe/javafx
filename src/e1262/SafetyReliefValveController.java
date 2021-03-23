/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package e1262;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author snehal
 */
public class SafetyReliefValveController implements Initializable {

    @FXML
    private HBox sectionHeader1;
    @FXML
    private Text txtMode;
    @FXML
    private ImageView imgEmergency;
    @FXML
    private ImageView imgAuto;
    @FXML
    private ImageView imgManual;
    @FXML
    private Text txtDate;
    @FXML
    private HBox sectionHeader;
    @FXML
    private JFXButton btnHome;
    @FXML
    private JFXButton btnInitial;
    @FXML
    private JFXButton btnTestScreen;
    @FXML
    private JFXButton btnReport;
    @FXML
    private JFXButton btnSystemCheck;
    @FXML
    private JFXButton btnAdmin;
    @FXML
    private JFXButton btnHelp;
    @FXML
    private JFXTextField txtHydroSetPressure;
    @FXML
    private VBox vboxGauge;
    @FXML
    private VBox vboxtrend;
    @FXML
    private Text textTrend;
    @FXML
    private HBox hboxtrend;
    @FXML
    private JFXDrawer drawer1;
    @FXML
    private VBox vboxTxt;
    @FXML
    private JFXTextField txtValveSize;
    @FXML
    private JFXTextField txtValveClass1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnHomeAction(ActionEvent event) {
    }

    @FXML
    private void btnTestScreenAction(ActionEvent event) {
    }

    @FXML
    private void btnReportAction(ActionEvent event) {
    }

    @FXML
    private void btnSystemCheckAction(ActionEvent event) {
    }

    @FXML
    private void btnAdminAction(ActionEvent event) {
    }

    @FXML
    private void btnHelpAction(ActionEvent event) {
    }


    @FXML
    private void txtHydroSetPressureAction(ActionEvent event) {
    }

    @FXML
    private void txtHydroPressureKeyRelease(KeyEvent event) {
    }

    @FXML
    private void txtStabilizationTimeKeyRelease(KeyEvent event) {
    }

    
}
