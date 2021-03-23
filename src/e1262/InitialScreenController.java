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
import de.re.easymodbus.modbusclient.ModbusClient;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Section;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Separator;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.RED;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import static javax.management.remote.JMXConnectorFactory.connect;

/**
 * FXML Controller class
 *
 * @author snehal
 */
public class InitialScreenController implements Initializable {

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
    private JFXButton btnReport;
    @FXML
    private JFXButton btnSystemCheck;
    @FXML
    private JFXButton btnAdmin;
    @FXML
    private JFXComboBox<String> cmbValveType;
    @FXML
    private JFXComboBox<String> cmbValveClass;
    @FXML
    private JFXComboBox<String> cmbValveSize;
    @FXML
    private JFXComboBox<String> cmbTypeOfSealing;
    @FXML
    private JFXComboBox<String> cmbTestStandards;
    @FXML
    private JFXTextField txtStabilizationTime;
    @FXML
    private JFXTextField txtHoldingTime;
    @FXML
    private JFXTextField txtDrainTime;
    @FXML
    private JFXTextField txtHydroSetPressure;
    @FXML
    private JFXTextField txtHydraulicSetPressure;
    @FXML
    private JFXRadioButton radioBar;
    @FXML
    private JFXRadioButton radioPsi;
    @FXML
    private JFXTextField txtValveSerialNo;
    @FXML
    private JFXTextField txtValveTagNo;
    @FXML
    private JFXTextField txtIMIRNo;
    @FXML
    private JFXTextField txtWaterTemperture;
    @FXML
    private JFXComboBox<String> cmbProject;
    @FXML
    private JFXComboBox<String> cmbCustomer;
    @FXML
    private JFXComboBox<String> cmbManufacturer;
    @FXML
    private JFXComboBox<String> cmbOperatorName;
    @FXML
    private JFXComboBox<String> cmbShift;
    private JFXRadioButton radioAuto;
    private JFXRadioButton radioManual;
    public static Stage catStage;
    private Text textHydro;
    @FXML
    private VBox vboxTxt;
    @FXML
    private JFXTextField txtCycleStatus;
    @FXML
    private JFXTextField txtOverAllTime;
    @FXML
    private HBox hboxTestScreen;

    final ToggleGroup group = new ToggleGroup();
    Color colorDefault = Color.web("0x0099FF");

    final ToggleGroup group_Am = new ToggleGroup();

    @FXML
    private JFXComboBox<String> cmbTestType;
    DatabaseHandler dh = new DatabaseHandler();
    Connection connect = dh.MakeConnection();
    @FXML
    private JFXComboBox<String> cmbValveStd;

    Thread machine_mode, insert_data, cycleStatus, modScan;
    Boolean stop_insert = false, stop_mode = false, stop_pressure_get = true;

//    String current_machine_mode, previous_hydro_set_pressure, current_offline_mode, pu;
    String current_machine_mode, previous_hydro_set_pressure, current_offline_mode, pu, current_cycle_status, current_stabilization_timer, current_holding_timer, current_drain_delay, current_drain_timer, current_over_all_timer, oat, al, start_pressure_a, start_pressure_b, start_pressure_c, start_pressure_d, start_pressure_e, stop_pressure_a, stop_pressure_b, stop_pressure_c, stop_pressure_d, stop_pressure_e;

    String mac_mode = "4545", off_mode = "4545", cycl_status = "4545", s_time = "0", h_time = "0", d_d = "0", d_time = "0", over_all_time = "0", clampingActualPressure = "0", pressure_a = "0", pressure_b = "0", popUps = "0", result = "4545", bubble_counter = "0";
    DoubleProperty ClampingAct;
    DoubleProperty HydroActA;
    DoubleProperty HydroActB;

    Boolean first_pop_lock = false, second_pop_lock = false, third_pop_lock = false, you_can_change = true;
    @FXML
    private Text txtOffline;
//    private Gauge // GaugeHydraulic;
    @FXML
    private VBox vboxGauge1;
    @FXML
    private Gauge GaugeActualHydraulic;
    @FXML
    private Text textHydro1;
//    private Gauge // GaugeHydro;
    @FXML
    private VBox vboxGauge11;
    @FXML
    private Gauge GaugeActualHydro;
    @FXML
    private Text textHydro11;
    @FXML
    private JFXTextField txtHoldingTimer;
    @FXML
    private JFXTextField txtResult;
    @FXML
    private JFXTextField txtStabilizationTimer;

    private int gauge_clamping_red, gauge_hydro_red = 70;

    Colors colors = new Colors();
    @FXML
    private JFXRadioButton radioKgcm;
    @FXML
    private JFXComboBox<String> cmbValveLeack;

    volatile boolean stopCycleStatusThread;
    String pressure_a_side = "0";
    String pressure_b_side = "0";
    int test_result_by_type_check = 0, delete_count = 0, test_result_count = 0, count_result = 0;
    volatile boolean stopModScanThread;
    volatile float weighingScaleReading = 0;
//    ArrayList<Float> avgWeighingScaleReading = new ArrayList<>();
    // For ConcurrentModificationException
    List<Float> avgWeighingScaleReading = new CopyOnWriteArrayList<>();
    float avgActualReading = 0;
    DecimalFormat decimalformat = new DecimalFormat();
    @FXML
    private JFXTextField txtLeak;
    @FXML
    private VBox vboxGauge111;
    @FXML
    private Text textHydro111;
    @FXML
    private Gauge GaugeActualHydro_B;
    @FXML
    private Separator saperator1;
    @FXML
    private Separator saperator2;
    @FXML
    private JFXComboBox<String> cmbGaugeHydro;
    @FXML
    private JFXTextField dateGaugeHydro;
    @FXML
    private JFXTextField duedateGaugeHydro;

    //creating object of  Background_Process
//     Background_Processes  bg_process;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {

            hboxTestScreen.setVisible(false);
            radioBar.setToggleGroup(group);
            radioBar.setSelected(true);
            radioPsi.setToggleGroup(group);
            radioKgcm.setSelected(true);
            radioKgcm.setToggleGroup(group);

//            radioAuto.setToggleGroup(group_Am);
//            radioAuto.setSelected(true);
//            radioManual.setToggleGroup(group_Am);
            if (Session.get("user_type").equals("admin")) {
                btnAdmin.setVisible(true);
//            btnSystemCheck.setVisible(true);
                Session.set("catAccess", "granted");
            } else {
                Session.set("catAccess", "not");
                btnAdmin.setVisible(false);
//            btnSystemCheck.setVisible(false);
            }

            imgAuto.setVisible(false);
            imgManual.setVisible(false);
            imgEmergency.setVisible(false);
            txtStabilizationTime.setDisable(true);
            txtDrainTime.setDisable(true);
//        txtDrainDelay.setDisable(true);
            final String DEGREE = "\u00b0";
            txtWaterTemperture.setPromptText("Water Temperature(" + DEGREE + "C)");
            radioBar.setSelectedColor(colorDefault);
            radioPsi.setSelectedColor(colorDefault);
            radioKgcm.setSelectedColor(colorDefault);
            cmbShift.getItems().add(0, "A");
            cmbShift.getItems().add(1, "B");
            cmbShift.getItems().add(1, "C");
            Session.set("its_valve_standards", "no");

            //date method
            date_time();
            //Initial data
            InitialDataLoad();
            
            //Adding dropdown data from Master list START
        try {

            ResultSet rsC = dh.getData_sp("select_customer_sp()", connect);
            ResultSet rsM = dh.getData_sp("select_manufacturer_sp()", connect);
            ResultSet rsP = dh.getData_sp("select_project_sp()", connect);
            while (rsC.next()) {
                cmbCustomer.getItems().add(rsC.getString("customer_name"));
            }
            while (rsM.next()) {
                cmbManufacturer.getItems().add(rsM.getString("manufacturer_name"));
            }
            while (rsP.next()) {
                cmbProject.getItems().add(rsP.getString("project_name"));
            }

//        check_empty_fields(txtError_Leakage_type, cmbLeakageType.getSelectionModel().getSelectedItem());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

            //insert plc tag value to databse
            insert_plc_data();

            //call machine mode method
            machine_mode();
        } catch (SQLException ex) {
            Logger.getLogger(InitialScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void InitialDataLoad() throws SQLException {
        //test type 
        String query = "SELECT test_type,write_value FROM test_type GROUP BY write_value";
        cmbTestType.getSelectionModel().clearSelection();
        cmbTestType.getItems().clear();
        try {
            int count = 0;
            ResultSet rs = dh.getData(query, connect);
            while (rs.next()) {
//                int index = Integer.parseInt(rs.getString("write_value"));
                cmbTestType.getItems().add(count, rs.getString("test_type"));
                count++;
            }

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }

        String valve_s = "";
        String q2 = "SELECT valve_standards FROM Initial_init ORDER BY ID DESC LIMIT 1;";
        ResultSet rs0;

        rs0 = dh.getData(q2, connect);
        if (rs0.next()) {
            String q1 = "SELECT valve_standards FROM valve_standards WHERE write_value = " + rs0.getString("valve_standards") + ";";
            ResultSet rs1 = dh.getData(q1, connect);
            if (rs1.next()) {
                valve_s = rs1.getString("valve_standards");
            }
        }

        Initial_Dropdowns_Data();
        //VALVE CLASS SIZE
        valve_class_size(valve_s);

        //if previous data exist
        //setting data
        String query1 = "SELECT * FROM Initial_init ORDER BY ID DESC LIMIT 1;";

        try {
            ResultSet rs1 = dh.getData(query1, connect);

            while (rs1.next()) {
                cmbTestType.getSelectionModel().select(Integer.parseInt(rs1.getString("test_type")));
//                cmbLeakageType.getSelectionModel().select(Integer.parseInt(rs1.getString("leakage_type")));
                String vc_query = "SELECT valve_class FROM valve_class WHERE write_value = '" + rs1.getString("valve_class") + "';";
                ResultSet vc_rs = dh.getData(vc_query, connect);
                if (vc_rs.next()) {
                    cmbValveClass.getSelectionModel().select(vc_rs.getString("valve_class"));
                }
                String vs_query = "SELECT valve_size FROM valve_size WHERE write_value = '" + rs1.getString("valve_size") + "';";
                ResultSet vs_rs = dh.getData(vs_query, connect);
                if (vs_rs.next()) {
                    cmbValveSize.getSelectionModel().select(vs_rs.getString("valve_size"));
                }
//                cmbValveClass.getSelectionModel().select(Integer.parseInt(rs.getString("valve_class")));
//                cmbValveSize.getSelectionModel().select(Integer.parseInt(rs.getString("valve_size")));
                cmbValveType.getSelectionModel().select(Integer.parseInt(rs1.getString("valve_type")));
                cmbTypeOfSealing.getSelectionModel().select(Integer.parseInt(rs1.getString("type_of_sealing")));
                cmbTestStandards.getSelectionModel().select(Integer.parseInt(rs1.getString("test_standards")));
                cmbValveStd.getSelectionModel().select(Integer.parseInt(rs1.getString("valve_standards")));
                txtStabilizationTime.setText(rs1.getString("stabilization_time"));
                txtHoldingTime.setText(rs1.getString("holding_time"));
//                txtDrainDelay.setText(rs.getString("drain_delay"));
                txtDrainTime.setText(rs1.getString("drain_time"));
                txtHydroSetPressure.setText(rs1.getString("hydro_set_pressure"));
                txtHydraulicSetPressure.setText(rs1.getString("hydraulic_set_pressure"));
//                txtAllowableLeakage.setText(rs.getString("allowable_leakage"));
//                previous_hydro_set_pressure = rs.getString("hydro_set_pressure");
//                if (rs.getString("wrong_selection").equals("1")) {
//                    txtWrongSelection.setVisible(true);
//                } else {
//                    txtWrongSelection.setVisible(false);
//                }
                mode(rs1.getString("machine_mode"));
                bar_psi_kg(rs1.getString("bar_psi_kgcm"));
                offline_online(rs1.getString("offline_online"));
            }
            leakage_type();
            if (cmbTestType.getSelectionModel().getSelectedItem().equals("PNEUMATIC SEAT A SIDE") || cmbTestType.getSelectionModel().getSelectedItem().equals("PNEUMATIC SEAT B SIDE")) {
                Gauge_details("Air Guage");
            } else {
                Gauge_details(cmbValveClass.getSelectionModel().getSelectedItem());
            }

            //display value is checked for set automatic data when comming to initial screen from test screen
            try {
                if (Session.get("display_valve_data").equals("1")) {
                    String q = "SELECT * FROM valve_data ORDER BY valve_data_id DESC LIMIT 1";
                    ResultSet rs_1 = null;
                    try {
                        rs1 = dh.getData(q, connect);
                        if (rs1.next()) {
                            txtValveSerialNo.setText(rs_1.getString("valve_serial_no"));
                            txtValveTagNo.setText(rs_1.getString("valve_tag_no"));
                            txtIMIRNo.setText(rs_1.getString("imir_no"));
                            txtWaterTemperture.setText(rs_1.getString("water_temperature"));
//                        txtIMIRNo.setText(rs.getString("imir_no"));
//                        txtProject.setText(rs1.getString("project"));
//                        txtCustomer.setText(rs1.getString("customer"));
                            cmbShift.getSelectionModel().select(rs_1.getString("shift"));
                            cmbOperatorName.getSelectionModel().select(rs_1.getString("operator"));
                            cmbCustomer.getSelectionModel().select(rs_1.getString("customer"));
                            cmbManufacturer.getSelectionModel().select(rs_1.getString("manufacturer"));
                            cmbProject.getSelectionModel().select(rs_1.getString("project"));
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        Logger.getLogger(InitialScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } catch (Exception e) {
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
    public void Gauge_details(String description) {
//        String valve_class = cmbValveClass.getSelectionModel().getSelectedItem();
//        cmbGaugeClamping.getSelectionModel().clearSelection();
        cmbGaugeHydro.getSelectionModel().clearSelection();
        cmbGaugeHydro.getItems().clear();
//        cmbGaugeClamping.getItems().clear();
        String q = "SELECT * FROM gauge_data WHERE description = '" + description + "';";
//        String q1 = "SELECT * FROM gauge_data WHERE description = 'Clamping Gauge';";
        try {
            ResultSet rs = dh.getData(q, connect);
//            ResultSet rs1 = dh.getData(q1, connect);
            int index = 0;
//            int index1 = 0;
            while (rs.next()) {
                cmbGaugeHydro.getItems().add(index, rs.getString("serial"));
                if (index == 0) {
                    dateGaugeHydro.setText(rs.getString("c_date"));
                    duedateGaugeHydro.setText(rs.getString("c_due_date"));
                }
                index++;
            }
//            while (rs1.next()) {
//                cmbGaugeClamping.getItems().add(index1, rs1.getString("serial"));
//                if (index1 == 0) {
//                    dateGaugeClamping.setText(rs1.getString("c_date"));
//                    duedateGaugeClamping.setText(rs1.getString("c_due_date"));
//                }
//                index1++;
//            }
            cmbGaugeHydro.getSelectionModel().select(0);
//            cmbGaugeClamping.getSelectionModel().select(0);

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }

    }

    private void valve_class_size(String Valve_Standard) throws SQLException {
        cmbValveClass.getSelectionModel().clearSelection();
        cmbValveSize.getSelectionModel().clearSelection();
        cmbValveClass.getItems().clear();
        cmbValveSize.getItems().clear();
        //Valve_class
        String vc = "SELECT vc.valve_class FROM valve_class vc WHERE vc.valve_standards = '" + Valve_Standard + "';";
        System.out.println(vc);
        ResultSet rs_vc = dh.getData(vc, connect);
        while (rs_vc.next()) {
            if (rs_vc.getString("valve_class") == null || rs_vc.getString("valve_class").equals("")) {
            } else {
                cmbValveClass.getItems().add(rs_vc.getString("valve_class"));
            }
        }
        //Valve_size
        String vs = "SELECT vs.valve_size FROM valve_size vs WHERE vs.valve_standards = '" + Valve_Standard + "';";
        System.out.println(vs);
        ResultSet rs_vs = dh.getData(vs, connect);
        while (rs_vs.next()) {
            if (rs_vs.getString("valve_size") == null || rs_vs.getString("valve_size").equals("")) {
            } else {

                cmbValveSize.getItems().add(rs_vs.getString("valve_size"));

            }
        }
    }

    private void Initial_Dropdowns_Data() {
        String q = "SELECT vt.valve_type,vt.write_value AS type_write,tos.type_of_sealing,tos.write_value AS sealing_write,ts.test_standards,ts.write_value AS standard_write,vst.valve_standards,vst.write_value AS valve_standard_write FROM valve_type vt LEFT JOIN type_of_sealing tos ON tos.type_of_sealing_id = vt.valve_type_id LEFT JOIN test_standards ts ON ts.test_standards_id = vt.valve_type_id LEFT JOIN valve_standards vst ON vst.valve_standards_id = vt.valve_type_id;";
        try {
            ResultSet rs = dh.getData(q, connect);
            while (rs.next()) {
                if (rs.getString("valve_type") == null || rs.getString("valve_type").equals("")) {
                } else {
                    cmbValveType.getItems().add(Integer.parseInt(rs.getString("type_write")), rs.getString("valve_type"));
                }
                if (rs.getString("type_of_sealing") == null || rs.getString("type_of_sealing").equals("")) {
                } else {
                    cmbTypeOfSealing.getItems().add(Integer.parseInt(rs.getString("sealing_write")), rs.getString("type_of_sealing"));
                }
                if (rs.getString("test_standards") == null || rs.getString("test_standards").equals("")) {
                } else {
                    cmbTestStandards.getItems().add(Integer.parseInt(rs.getString("standard_write")), rs.getString("test_standards"));
                }
                if (rs.getString("valve_standards") == null || rs.getString("valve_standards").equals("")) {
                } else {
                    cmbValveStd.getItems().add(Integer.parseInt(rs.getString("valve_standard_write")), rs.getString("valve_standards"));
                }
            }
            String query1 = "SELECT name,qualification FROM User_data WHERE user_type = 'operator';";
            ResultSet rs1 = dh.getData(query1, connect);
            while (rs1.next()) {
                cmbOperatorName.getItems().add(rs1.getString("name"));
            }
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }

    }
    boolean Initail_leakage_type_flag = true;

    private void leakage_type() {
        String tst_type = cmbTestType.getSelectionModel().getSelectedItem();
        cmbValveLeack.getSelectionModel().clearSelection();
        cmbValveLeack.getItems().clear();
//        txtActualUnit.setVisible(false);
//        txtAllowableLeakage.setVisible(false);
        String q = "SELECT series,leakage_type,write_value_test_type FROM leakage_type WHERE test_type = '" + tst_type + "' GROUP BY series;";
        System.out.println("q: " + q);
        try {
            ResultSet rs = dh.getData(q, connect);
            String write_value = "0";
            while (rs.next()) {
                write_value = rs.getString("write_value_test_type");
                int index = Integer.parseInt(rs.getString("series"));
//                cmbValveLeack.getItems().add(index, rs.getString("leakage_type"));
                cmbValveLeack.getItems().add(rs.getString("leakage_type"));
            }
            if (Initail_leakage_type_flag) {
//                cmbLeakageType.getSelectionModel().select(0);
                ToolKit.tagWrite("N7:28", "0");
                Initail_leakage_type_flag = false;
            } else {
//                cmbLeakageType.getSelectionModel().select(0);

            }
//            cmbLeakageType.getSelectionModel().select(0);
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }

    }

    String py_insert_cmd = "python /media/nsp/AA88D95288D91D9F/hydro_project_window/pro/E1262/E1262/python_plc/insert_init_tags.py " + DatabaseHandler.DB_HOST + " " + DatabaseHandler.DB_USER + " " + DatabaseHandler.DB_PASS + " " + DatabaseHandler.DB_NAME + " truncate_init_tags insert_init_tags_sp";

    //insert plc data to database
    private void insert_plc_data() {
        insert_data = new Thread(() -> {
            while (true) {
                try {
                    if (stop_insert) {
                        insert_data.stop();
                        break;
                    }

                    Process child = Runtime.getRuntime().exec(py_insert_cmd);

                    child.waitFor();
                    insert_data.sleep(100);

                } catch (IOException ex) {
                    Logger.getLogger(InitialScreenController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(InitialScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        });
        insert_data.start();

    }

    //machine mode 
    private void machine_mode() {
//        String display = "SELECT machine_mode,offline_online FROM init_tags ORDER BY ID DESC LIMIT 1";
        machine_mode = new Thread(() -> {
            String display = "SELECT * FROM test_tags ORDER BY test_tags_id DESC LIMIT 1";
            ResultSet rs;
            try {
                rs = dh.getData(display, connect);
                if (rs.next()) {
                    if (ToolKit.isNull(rs.getString("over_all_time"))) {
                        System.out.println("NULL over_all_time");
                    } else {
                        current_over_all_timer = rs.getString("over_all_time");
                    }
                    txtOverAllTime.setText("");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger(TestScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }

            while (true) {
                try {
                    long start = System.currentTimeMillis();
                    //Sleeping thread for 250 miliseconds: Starts
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException intex) {
                        intex.printStackTrace();
                        System.err.println("Interupt in mode thread : " + intex);
                    }
                    //Sleeping thread for 250 miliseconds: End

                    if (stop_mode) {
                        break;
                    }
//                    System.out.println("This is select  query  : " + display);

                    rs = dh.getData(display, connect);
                    if (rs.next()) {
                        //Storing Value's of Machine Parameters: Start
                        if (ToolKit.isNull(rs.getString("machine_mode"))) {
                            System.out.println("NULL machine_mode");
                        } else {

//                                mac_mode=rs.getString("ma")
                            mac_mode = rs.getString("machine_mode");
                        }
                        if (ToolKit.isNull(rs.getString("offline_online"))) {
                            System.out.println("NULL offline_online");
                        } else {
                            off_mode = rs.getString("offline_online");
                        }
                        if (ToolKit.isNull(rs.getString("cycle_status"))) {
                            System.out.println("NULL cycle_status");
                        } else {
                            cycl_status = rs.getString("cycle_status");
                        }
                        if (ToolKit.isNull(rs.getString("stabilization_timer"))) {
                            System.out.println("NULL stabilization_timer");
                        } else {
                            s_time = rs.getString("stabilization_timer");
                        }
                        if (ToolKit.isNull(rs.getString("holding_timer"))) {
                            System.out.println("NULL holding_timer");
                        } else {
                            h_time = rs.getString("holding_timer");
                        }
                        if (ToolKit.isNull(rs.getString("drain_delay"))) {
                            System.out.println("NULL drain_delay");
                        } else {
                            d_d = rs.getString("drain_delay");
                        }
                        if (ToolKit.isNull(rs.getString("drain_timer"))) {
                            System.out.println("NULL drain_timer");
                        } else {
                            d_time = rs.getString("drain_timer");
                        }
                        if (ToolKit.isNull(rs.getString("over_all_time"))) {
                            System.out.println("NULL over_all_time");
                        } else {
                            over_all_time = rs.getString("over_all_time");
                        }
                        if (ToolKit.isNull(rs.getString("hydraulic_actual_pressure"))) {
                            System.out.println("NULL hydraulic_actual_pressure");
                        } else {
                            clampingActualPressure = rs.getString("hydraulic_actual_pressure");
                        }
                        if (ToolKit.isNull(rs.getString("hydro_actual_a_pressure"))) {
                            System.out.println("NULL hydro_actual_a_pressure");
                        } else {
                            pressure_a = rs.getString("hydro_actual_a_pressure");
                        }
                        if (ToolKit.isNull(rs.getString("hydro_actual_b_pressure"))) {
                            System.out.println("NULL hydro_actual_b_pressure");
                        } else {
                            pressure_b = rs.getString("hydro_actual_b_pressure");
                        }
                        if (ToolKit.isNull(rs.getString("pop_ups"))) {
                            System.out.println("NULL pop_ups");
                        } else {
                            popUps = rs.getString("pop_ups");
                        }
                        if (ToolKit.isNull(rs.getString("result"))) {
                            System.out.println("NULL result");
                        } else {
                            result = rs.getString("result");
                        }
                        if (ToolKit.isNull(rs.getString("bubble_counter"))) {
                            System.out.println("NULL bubble_counter");
                        } else {
                            bubble_counter = rs.getString("bubble_counter");
                        }

                        //Storing Value's of Machine Parameters: End
                        try {

                            //Updating Gauge's Value: Start
                            double clampingActual = Double.parseDouble(clampingActualPressure);
                            double hydroA = Double.parseDouble(pressure_a);
                            double hydroB = Double.parseDouble(pressure_b);
                            ClampingAct = new SimpleDoubleProperty(clampingActual);
                            HydroActA = new SimpleDoubleProperty(hydroA);
                            HydroActB = new SimpleDoubleProperty(hydroB);

                            Platform.runLater(() -> {
                                GaugeActualHydraulic.valueProperty().bind(ClampingAct);
                                GaugeActualHydro.valueProperty().bind(HydroActA);
                                GaugeActualHydro_B.valueProperty().bind(HydroActB);

                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("EXCEPTION IN SETTING VALUE OF GAUGES IN DATA_UPDATE_THREAD : " + e.getMessage());
//                            GaugeActualHydraulic.setValue(hydroB);
                        }

//                        System.out.println(clampingActual + ": " + hydroA + ": " + hydroB);
                        //Updating Gauge's Valu: End
                        try {
                            try {
                                
                                Platform.runLater(() -> {
                                    if (ToolKit.isNull(over_all_time)) {
                                    } else {

                                        if (over_all_time.equals(current_over_all_timer)) {
                                        } else {
                                            txtOverAllTime.setText(over_all_time);
                                            current_over_all_timer = over_all_time;
                                        }
                                    }

                                });

                                //Updating Overall Time Value: End
                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();

                                System.out.println("EXCEPTION IN UPDATE OVERALL TIME DATA_UPDATE_THREAD : " + e.getMessage());
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            try {
                                //Updating Off_Online Mode Value: Start
                                if (off_mode.equals(current_offline_mode)) {
                                } else {
                                    offline_online(off_mode);
                                }
                                //Updating Off_Online Mode Value: End
                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                                System.out.println("EXCEPTION IN Updating Off_Online Mode DATA_UPDATE_THREAD : " + e.getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //break loop and stoping process: Start
                        if (stop_mode) {
                            break;
                        }
                        //break loop and stoping process: Start

                        try {
                            try {
                                //Updating Operating Mode Value: Start
                                if (mac_mode.equals(current_machine_mode)) {
                                } else {
                                    mode(mac_mode);
                                }
                                //Updating Operating Mode Value: End
                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                                System.out.println("EXCEPTION IN Updating Operating Mode DATA_UPDATE_THREAD : " + e.getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            //getting first pop up start the cycle: Start
                            switch (popUps) {
                                case "0":
                                    first_pop_lock = false;
                                    second_pop_lock = false;
                                    third_pop_lock = false;
                                    break;
                                case "1":
                                    if (first_pop_lock) {
                                    } else {

                                        first_pop_lock = true;
                                        you_can_change = false;
//                                        txtStabilizationTimer.setText();
                                        txtStabilizationTimer.setText("");
                                        txtHoldingTimer.setText("");
//                                        txtDrainDelay.setText("");
//                                        txtDrainTimer.setText("");
                                        txtOverAllTime.setText("");
                                        txtResult.setText("");
                                        pop_up_start("Please Confirm Valve Type, Valve Class and Valve Size.", 450, "0", "4", "N7:9");
                                    }
                                    break;
                                case "2":
                                    if (second_pop_lock) {
                                    } else {

                                        pop_up_timer("Start Hodling Timer", 300, "0", "N7:9");
                                        second_pop_lock = true;
//                                        start_pressure_a = pressure_a;
//                                        start_pressure_b = pressure_b;
                                    }
                                    break;
                                case "3":
                                    if (third_pop_lock) {
                                    } else {

                                        pop_up_timer("Start Drain Timer", 300, "0", "N7:9");
                                        third_pop_lock = true;
                                    }
                                    break;
                                default:
                                    break;
                            }
                            //getting first pop up start the cycle: Start
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("EXCEPTION IN Pop up Logic DATA_UPDATE_THREAD : " + e.getMessage());
                        }

                    }
                    long stop = System.currentTimeMillis();
                    long res = stop - start;
//                    System.out.println("mode offline pop cycle combine time : " + res);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Exception In Mode Thread : " + e);
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException intex) {
                        System.err.println("Interupt in mode thread : " + intex);
                    }
                }
                if (stop_mode) {
                    machine_mode.stop();
                    break;
                }
            }
        });
        machine_mode.start();

    }

    private void mode(String mode) {
        switch (mode) {
            case "0":
                Platform.runLater(() -> {
                    txtMode.setText("Auto Mode");
                });
                current_machine_mode = "0";
                txtDate.setFill(Color.web("#0099FF"));
                imgEmergency.setVisible(false);
                imgManual.setVisible(false);
                imgAuto.setVisible(true);
                txtHoldingTime.setDisable(true);
                txtHydroSetPressure.setDisable(true);
                txtHydraulicSetPressure.setDisable(true);
                break;
            case "1":
                Platform.runLater(() -> {
                    txtMode.setText("Manual Mode");
                });
                current_machine_mode = "1";
                txtDate.setFill(Color.web("#0099FF"));
                imgEmergency.setVisible(false);
                imgManual.setVisible(true);
                imgAuto.setVisible(false);
                txtHoldingTime.setDisable(false);
                txtHydroSetPressure.setDisable(false);
                txtHydraulicSetPressure.setDisable(false);
                break;
            case "2":
                Platform.runLater(() -> {
                    txtMode.setText("Emergency Mode");
                });
                current_machine_mode = "2";
                txtDate.setFill(Color.web("#C32420"));
                imgEmergency.setVisible(true);
                imgManual.setVisible(false);
                imgAuto.setVisible(false);
                txtHoldingTime.setDisable(true);
                break;
            default:
                Platform.runLater(() -> {
                    txtMode.setText("Something wrong");
                });
                current_machine_mode = "0";
                txtDate.setFill(Color.web("#C32420"));
                imgEmergency.setVisible(true);
                imgManual.setVisible(false);
                imgAuto.setVisible(false);
                txtHoldingTime.setDisable(true);
                break;
        }
    }

    /**
     * pressure unit mode selector
     */
    private void bar_psi_kg(String bar_psi_kg) {
        switch (bar_psi_kg) {
            case "0":
                radioBar.setSelected(true);
                pu = "bar";
                setPressureUnit("bar");
                break;
            case "1":
                radioPsi.setSelected(true);
                pu = "psi";
                setPressureUnit("psi");
                break;
//            case "2":
//                radioKgcm.setSelected(true);
//                pu = "kg/sqcm";
//                setPressureUnit("kg/sqcm");
//                break;
            default:
                radioBar.setSelected(true);
                pu = "bar";
                break;
        }
    }

    private void setPressureUnit(String unit) {
        txtHydroSetPressure.setPromptText("Hydro Set Pressure(" + unit + ")");
        txtHydraulicSetPressure.setPromptText("Hydraulic Set Pressure(" + unit + ")");
    }

    /**
     * offline online mode selector
     */
    private void offline_online(String off_on) {
        switch (off_on) {
            case "0":
                Platform.runLater(() -> {
                    txtOffline.setText("Online");
                });
                current_offline_mode = "0";
                break;
            case "1":
                Platform.runLater(() -> {
                    txtOffline.setText("Offline");
                });
                current_offline_mode = "1";
                break;
            default:
                Platform.runLater(() -> {
                    txtOffline.setText("Something went wrong");
                });
                current_offline_mode = "0";
                break;
        }
    }

    private void pop_up_start(String message, int width, String yes, String no, String tag) {
//        String[] split = ToolKit.readpy("python /media/nsp/AA88D95288D91D9F/hydro_project_window/pro/E1262/E1262/python_plc/gauge_setup_hydro.py").split("/");
        String[] split = ToolKit.readpy("python /media/nsp/AA88D95288D91D9F/hydro_project_window/pro/E1262/E1262/python_plc/test.py").split("/");
        if (split[3].equals("False")) {
            String tt = cmbTestType.getSelectionModel().getSelectedItem();
            String lt = cmbValveLeack.getSelectionModel().getSelectedItem();
            String vt = cmbValveType.getSelectionModel().getSelectedItem();
            String vc = cmbValveClass.getSelectionModel().getSelectedItem();
            String vs = cmbValveSize.getSelectionModel().getSelectedItem();
            String tos = cmbTypeOfSealing.getSelectionModel().getSelectedItem();
            String ts = cmbTestStandards.getSelectionModel().getSelectedItem();
            String operator_name = cmbOperatorName.getSelectionModel().getSelectedItem();
            String shift = cmbShift.getSelectionModel().getSelectedItem();
            String st = txtStabilizationTime.getText();
            String ht = txtHoldingTime.getText();
//                String dd = txtDrainDelay.getText();
            String dt = txtDrainTime.getText();
            String hsp = txtHydroSetPressure.getText();
            String csp = txtHydraulicSetPressure.getText();
            String vsn = txtValveSerialNo.getText();
            String vtn = txtValveTagNo.getText();
            String imir = txtIMIRNo.getText();
            String mnfr = cmbManufacturer.getSelectionModel().getSelectedItem();
            String wtr = txtWaterTemperture.getText();
//                String allowableLeakage = txtAllowableLeakage.getText();
            String pro = cmbProject.getSelectionModel().getSelectedItem();
//                String cus = txtCustomer.getText();
            String cus = cmbCustomer.getSelectionModel().getSelectedItem();
            String vst = cmbValveStd.getSelectionModel().getSelectedItem();
//                String gauge_serial = cmbGaugeHydro.getSelectionModel().getSelectedItem();
//                String gauge_calibration_date = dateGaugeHydro.getText();

            if (ToolKit.isNull(tt)
                    || ToolKit.isNull(lt)
                    || ToolKit.isNull(vt)
                    || ToolKit.isNull(vc)
                    || ToolKit.isNull(vs)
                    || ToolKit.isNull(tos)
                    || ToolKit.isNull(ts)
                    || ToolKit.isNull(shift)
                    || ToolKit.isNull(operator_name)
                    || ToolKit.isNull(st)
                    || ToolKit.isNull(ht)
                    //                    || ToolKit.isNull(dd)
                    || ToolKit.isNull(dt)
                    || ToolKit.isNull(hsp)
                    || ToolKit.isNull(csp)
                    || ToolKit.isNull(vsn)
                    || ToolKit.isNull(vtn)
                    || ToolKit.isNull(mnfr)
                    || ToolKit.isNull(wtr)
                    || ToolKit.isNull(pro)
                    || ToolKit.isNull(cus)
                    || ToolKit.isNull(vst)
                    || ToolKit.isNull(imir)) {
//                insert_plc_data("python /media/nsp/AA88D95288D91D9F/hydro_project_window/pro/E1262/E1262/python_plc/insert_init_tags.py", false, true);
                Dialog.showForSometime("", "Please provide appropriate data", "Alert", 450, 10);
                check_text_empty_fields(txtHydraulicSetPressure, csp);
                check_combo_empty_fields(cmbCustomer, cus);
                check_text_empty_fields(txtHoldingTime, ht);
                check_text_empty_fields(txtHydroSetPressure, hsp);
                check_combo_empty_fields(cmbValveLeack, lt);
                check_combo_empty_fields(cmbManufacturer, mnfr);
                check_combo_empty_fields(cmbOperatorName, operator_name);
                check_combo_empty_fields(cmbProject, pro);
                check_combo_empty_fields(cmbShift, shift);
                check_combo_empty_fields(cmbTestStandards, ts);
                check_combo_empty_fields(cmbTestType, tt);
                check_combo_empty_fields(cmbTypeOfSealing, tos);
                check_combo_empty_fields(cmbValveClass, vc);
                check_combo_empty_fields(cmbValveSize, vs);
                check_combo_empty_fields(cmbValveStd, vst);
                check_text_empty_fields(txtValveTagNo, vtn);
                check_combo_empty_fields(cmbValveType, vt);
                check_text_empty_fields(txtWaterTemperture, wtr);
                check_text_empty_fields(txtValveSerialNo, vsn);
                check_text_empty_fields(txtIMIRNo, imir);

                //write 4 to pop up tag
                ToolKit.tagWrite("N7:9", no);

            } else {
                // insert query
                    
                //pop_window display
                Platform.runLater(() -> {
                    Optional<ButtonType> option = Dialog.ConfirmationDialog("CONFIRMATION", message, width);
                    if (option.get() == ButtonType.YES) {
                        try {
                            System.out.println("YES PRESSED");
//                    write_node_value(yes, tag);
                            ToolKit.tagWrite("N7:9", yes);
                            Thread.sleep(50);
                            delete_count = 0;
                            test_result_count = 0;
                            stop_pressure_get = true;
                            test_result_by_type_check++;
//                            start_trend = true;
//                            imgOverAll.setVisible(true);
//                            start_trend();
//                            Session.set("Trend_Status", "Running");
                            String gauge_unit = "";
                            if (radioBar.isSelected()) {
                                gauge_unit = "bar";
                            } else if (radioPsi.isSelected()) {
                                gauge_unit = "psi";
                            } else if (radioKgcm.isSelected()) {
                                gauge_unit = "kg/cm3";
                            } else {

                            }
                            gauge_initalize(Integer.parseInt(split[1]), Integer.parseInt(split[1]), gauge_unit);
                            cycleStatusThread();

                        } catch (InterruptedException ex) {
                            Logger.getLogger(TestScreenController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        hboxTestScreen.setVisible(true);
                        if (cmbTestType.getSelectionModel().getSelectedItem().equals("HYDROSTATIC SEAT A SIDE") || cmbTestType.getSelectionModel().getSelectedItem().equals("PNEUMATIC SEAT A SIDE")) {
                            GaugeActualHydro_B.setVisible(false);
                        } else if (cmbTestType.getSelectionModel().getSelectedItem().equals("HYDROSTATIC SEAT B SIDE") || cmbTestType.getSelectionModel().getSelectedItem().equals("PNEUMATIC SEAT B SIDE")) {
                            GaugeActualHydro.setVisible(false);
                        } else {

                        }
                    } else if (option.get() == ButtonType.NO) {
                        try {
                            System.out.println("NO PRESSED");
                            you_can_change = true;
                            ToolKit.tagWrite("N7:9", no);
//                    write_node_value(no, tag);
                            Thread.sleep(50);
                            Session.set("Trend_Status", "Stopped");
                        } catch (InterruptedException ex) {
                            Logger.getLogger(TestScreenController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

            }

        } else {
            ToolKit.tagWrite("N7:9", no);
            Dialog.showForSometime("!!!!WRONG SELECTION!!!!", "Please Refer Clamping Chart", "", 350, 5);
        }

    }

    private void pop_up_timer(String message, int width, String ok, String tag) {
        Platform.runLater(() -> {
            Optional<ButtonType> option = Dialog.ConfirmationDialog_Single_button("CONFIRMATION", message, width);
            if (option.get() == ButtonType.OK) {
                try {
                    System.out.println("OK PRESSED");
                    ToolKit.tagWrite("N7:9", ok);
                    Thread.sleep(50);
                    if (message.equals("Start Hodling Timer")) {

                        if (Session.get("Test_Type").equals("HYDROSTATIC SEAT C SIDE")) {
                            start_pressure_a = pressure_b;
                            start_pressure_b = pressure_b;
//                            start_pressure_c = pressure_a;
//                            start_pressure_d = pressure_b;
//                            start_pressure_e = pressure_b;
                        } else if (Session.get("Test_Type").equals("HYDROSTATIC SEAT D SIDE")) {
                            start_pressure_a = pressure_a;
                            start_pressure_b = pressure_a;
//                            start_pressure_c = pressure_a;
//                            start_pressure_d = pressure_b;
//                            start_pressure_e = pressure_a;
                        } else if (Session.get("Test_Type").equals("HYDROSTATIC SEAT E SIDE")) {
                            start_pressure_a = pressure_b;
                            start_pressure_b = pressure_b;
//                            start_pressure_c = pressure_b;
//                            start_pressure_d = pressure_b;
//                            start_pressure_e = pressure_a;
                        } else {

                            start_pressure_a = pressure_a;
                            start_pressure_b = pressure_b;
//                            start_pressure_c = "0";
//                            start_pressure_d = "0";
//                            start_pressure_e = "0";
//                        leakageCheck();
                        }
                    }
                    Session.set("Trend_Status", "Running");
                } catch (InterruptedException ex) {
                    System.err.println("Exception in Holding time pop up respond" + ex.getMessage());
                }
            }
        });
    }

    private void gauge_initalize(int green, int max, String bpk) {


        /*
        *Hydro gauge Properties start
         */
//        try {
//            // GaugeHydro.clearAreas();
//            // GaugeHydro.clearTickMarkSections();
//            // GaugeHydro.clearTickLabelSections();
//            // GaugeHydro.clearSections();
//            // GaugeHydro.clearCustomTickLabels();
//            textHydro.setVisible(true);
//            // GaugeHydro.setVisible(true);
        GaugeActualHydro.clearAreas();
        GaugeActualHydro.clearTickMarkSections();
        GaugeActualHydro.clearTickLabelSections();
        GaugeActualHydro.clearSections();
        GaugeActualHydro.clearCustomTickLabels();
//            textHydro.setVisible(true);
        GaugeActualHydro.setVisible(true);

        //Hydro gauge visible property
        // GaugeHydro.sectionsVisibleProperty().set(true);
        GaugeActualHydro.sectionsVisibleProperty().set(true);
//            //GaugeActualHydro_B.sectionsVisibleProperty().set(true);

        //Hydro gauge max value
        // GaugeHydro.setMaxValue(max);
        GaugeActualHydro.setMaxValue(max);
//            GaugeActualHydro.setMaxValue(max);

        GaugeActualHydro_B.clearAreas();
        GaugeActualHydro_B.clearTickMarkSections();
        GaugeActualHydro_B.clearTickLabelSections();
        GaugeActualHydro_B.clearSections();
        GaugeActualHydro_B.clearCustomTickLabels();
//            textHydro.setVisible(true);
        GaugeActualHydro_B.setVisible(true);

        //Hydro gauge visible property
        // GaugeHydro.sectionsVisibleProperty().set(true);
        GaugeActualHydro_B.sectionsVisibleProperty().set(true);
//            //GaugeActualHydro_B.sectionsVisibleProperty().set(true);

        //Hydro gauge max value
        // GaugeHydro.setMaxValue(max);
        GaugeActualHydro_B.setMaxValue(max);
//            GaugeActualHydro.setMaxValue(max);

        //Hydro gauge major tick space
        if (max < 100) {
            System.out.println("in gauge initialization");
            if (max % 10 == 0) {
                // GaugeHydro.setMajorTickSpace(10);
                GaugeActualHydro_B.setMajorTickSpace(10);
                GaugeActualHydro.setMajorTickSpace(10);
                // GaugeHydro.setMinorTickSpace(5);
                GaugeActualHydro_B.setMinorTickSpace(5);
                GaugeActualHydro.setMinorTickSpace(5);
            } else {
                if (max < 10) {
                } else {
                    int space = max / 10;
                    // GaugeHydro.setMajorTickSpace(max / space);
                    GaugeActualHydro_B.setMajorTickSpace(max / space);
                    GaugeActualHydro.setMajorTickSpace(max / space);
                    // GaugeHydro.setMinorTickSpace(1);
                    GaugeActualHydro_B.setMinorTickSpace(1);
                    GaugeActualHydro.setMinorTickSpace(1);
                }

            }
        } else if (max > 1500) {
            // GaugeHydro.setMajorTickSpace(max / 1000);
            GaugeActualHydro_B.setMajorTickSpace(max / 1000);
            GaugeActualHydro.setMajorTickSpace(max / 1000);
            // GaugeHydro.setMinorTickSpace(250);
            //GaugeActualHydro_B.setMinorTickSpace(250);
            GaugeActualHydro.setMinorTickSpace(250);
        } else if (max > 1000 && max < 1500) {
//                 GaugeHydro.setMajorTickSpace(max / 200);
            GaugeActualHydro_B.setMajorTickSpace(max / 200);
            GaugeActualHydro.setMajorTickSpace(max / 200);
            // GaugeHydro.setMinorTickSpace(50);
            GaugeActualHydro_B.setMinorTickSpace(50);
            GaugeActualHydro.setMinorTickSpace(50);
        } else if (max > 500 && max < 1000) {
            // GaugeHydro.setMajorTickSpace(max / 100);
            GaugeActualHydro_B.setMajorTickSpace(max / 100);
            GaugeActualHydro.setMajorTickSpace(max / 100);
            // GaugeHydro.setMinorTickSpace(25);
            GaugeActualHydro_B.setMinorTickSpace(25);
            GaugeActualHydro.setMinorTickSpace(25);
        } else if (max < 500 && max > 100) {
            // GaugeHydro.setMajorTickSpace(max / 50);
            GaugeActualHydro_B.setMajorTickSpace(max / 50);
            GaugeActualHydro.setMajorTickSpace(max / 50);
            // GaugeHydro.setMinorTickSpace(15);
            GaugeActualHydro_B.setMinorTickSpace(15);
            GaugeActualHydro.setMinorTickSpace(15);
        }

        //Hydro gauge green zone range
        // GaugeHydro.addSection(new Section(0, green, Colors.GaugeGreen));
        GaugeActualHydro_B.addSection(new Section(0, green, Colors.GaugeGreen));
        GaugeActualHydro.addSection(new Section(0, green + 1, Colors.GaugeGreen));

        //Hydro gauge red zone range
        // GaugeHydro.addSection(new Section(green + 1, max, Colors.GaugeRed));
        GaugeActualHydro.addSection(new Section(green + 1, max, Colors.GaugeRed));
        GaugeActualHydro_B.addSection(new Section(green + 1, max, Colors.GaugeRed));

        //Hydro gauge needle Color
        // GaugeHydro.needleColorProperty().setValue(Colors.black);
        GaugeActualHydro.needleColorProperty().setValue(Colors.black);
        GaugeActualHydro_B.needleColorProperty().setValue(Colors.black);

        /*
         *Hydro gauge Properties stop
         */
 /*
         *Clamping gauge Properties start
         */
        switch ("bar") {
            case "bar":
                gauge_clamping_red = 420;
                bar_psi(420, 600);
                break;
            case "psi":
                gauge_clamping_red = 6090;
                bar_psi(6090, 8700);
                break;
            case "kg/sqcm":
                gauge_clamping_red = 425;
                bar_psi(425, 620);
                break;
            default:
                break;
        }
//        } catch (Exception e) {
//            System.out.println("Error in gauge initialization " + e.getMessage());
//        }
        /*
         *Clamping gauge Properties stop
         */

    }

    public void bar_psi(int green, int max) {

        GaugeActualHydraulic.setMaxValue(max);
        // GaugeHydraulic.setMaxValue(max);
        if (max < 100) {
            if (max % 10 == 0) {
                Platform.runLater(() -> {
                    GaugeActualHydraulic.setMajorTickSpace(10);
                    // GaugeHydraulic.setMajorTickSpace(10);
                    GaugeActualHydraulic.setMinorTickSpace(5);
                    // GaugeHydraulic.setMinorTickSpace(5);
                });
            } else {
                int space = max / 10;
                Platform.runLater(() -> {
                    GaugeActualHydraulic.setMajorTickSpace(max / space);
                    // GaugeHydraulic.setMajorTickSpace(max / space);
                });
            }
        } else if (max > 1000) {
            Platform.runLater(() -> {
                GaugeActualHydraulic.setMajorTickSpace(max / 1000);
                // GaugeHydraulic.setMajorTickSpace(max / 1000);
                GaugeActualHydraulic.setMinorTickSpace(250);
                // GaugeHydraulic.setMinorTickSpace(250);
            });
        } else {
            Platform.runLater(() -> {
                GaugeActualHydraulic.setMajorTickSpace(max / 100);
                // GaugeHydraulic.setMajorTickSpace(max / 100);
                GaugeActualHydraulic.setMinorTickSpace(50);
                // GaugeHydraulic.setMinorTickSpace(50);
            });
        }

        //Clamping gauge visible properties
        GaugeActualHydraulic.sectionsVisibleProperty().set(true);
        // GaugeHydraulic.sectionsVisibleProperty().set(true);
        //Clamping gauge green zone range
        Platform.runLater(() -> {
            GaugeActualHydraulic.addSection(new Section(0, green, colors.GaugeGreen));
            // GaugeHydraulic.addSection(new Section(0, green, colors.GaugeGreen));
            //Clamping gauge red zone range
            GaugeActualHydraulic.addSection(new Section(green + 1, max, colors.GaugeRed));
            // GaugeHydraulic.addSection(new Section(green + 1, max, colors.GaugeRed));
            //Clamping gauge needle color
            // GaugeHydraulic.needleColorProperty().setValue(colors.black);
            GaugeActualHydraulic.needleColorProperty().setValue(colors.black);
        });
    }

    //required filed error_color display
    private void check_text_empty_fields(JFXTextField field, String value) {
        if (value != null && !value.isEmpty()) {
            if (value.equals("null") || value.equals("")) {
                field.setUnFocusColor(RED);
                field.setFocusColor(RED);
            } else {
//                field.setFocusColor(false);
            }
        } else {
            field.setUnFocusColor(RED);
            field.setFocusColor(RED);
        }
    }

    private void check_combo_empty_fields(JFXComboBox field, String value) {
        if (value != null && !value.isEmpty()) {
            if (value.equals("null") || value.equals("")) {
                field.setUnFocusColor(RED);
                field.setFocusColor(RED);
            } else {
//                field.setVisible(false);
            }
        } else {
            field.setUnFocusColor(RED);
            field.setFocusColor(RED);
        }
    }

    /*
        cycle status method use to check running cycle and insert final result
     */
    private void cycleStatusThread() {

        stopCycleStatusThread = false;
        cycleStatus = new Thread(() -> {

            while (true) {
//                 Runtime.getRuntime().gc();
                try {
                    cycleStatus.sleep(250);
                    try {
                        try {
                            //cycle status update
                            if (cycl_status.equals(current_cycle_status)) {
                            } else {
                                try {
                                    cycle_status(cycl_status);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            if (stopCycleStatusThread) {
                                cycleStatus.stop();
                                break;
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();

                            //Thread.interrupted();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("EXCEPTION IN CYCLE STATUS UPDATE cycleStatusThread : " + e.getLocalizedMessage());
                    }

                    try {
                        //getting All timers
                        switch (cycl_status) {
                            case "2":
                                try {
                                    if (s_time.equals(current_stabilization_timer)) {
                                    } else {
                                        try {
                                            Platform.runLater(() -> {
                                                txtStabilizationTimer.setText(s_time);
                                            });
                                            current_stabilization_timer = s_time;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "3":
                                try {
                                    if (h_time.equals(current_holding_timer)) {
                                    } else {
                                        try {
                                            txtHoldingTimer.setText(h_time);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            pressure_a_side = new DecimalFormat("#").format(Double.parseDouble(pressure_a));
                                            pressure_b_side = new DecimalFormat("#").format(Double.parseDouble(pressure_b));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //Test_result_by_test_type

                                        if (test_result_by_type_check == 1) {
                                            String query = "INSERT INTO test_result_by_type (`valve_serial_no`,`test_no`,`test_type`,`hydro_pressure_a_side`,`hydro_pressure_b_side`,`date_time`,`test_count`) VALUES('" + Session.get("Valve_Serial_No") + "','" + Session.get("test_no") + "','" + Session.get("Test_Type") + "','" + pressure_a_side + "','" + pressure_b_side + "',NOW(),'" + test_result_by_type_check + "')";
                                            try {
                                                dh.execute(query, connect);
                                            } catch (SQLException e) {
                                                System.out.println(String.valueOf("Test result by type:  " + e.getMessage()));
                                            }
                                        } else {
                                            if (delete_count == 0) {
                                                String delete_query = "DELETE FROM test_result_by_type WHERE valve_serial_no = '" + Session.get("Valve_Serial_No") + "' AND test_type = '" + Session.get("Test_Type") + "' AND test_no = '" + Session.get("test_no") + "';";
                                                try {
                                                    dh.execute(delete_query, connect);
                                                    delete_count++;
                                                } catch (SQLException e) {
                                                    System.out.println(String.valueOf("Test result by type:  " + e.getMessage()));
                                                }
                                            }
                                            String query = "INSERT INTO test_result_by_type (`valve_serial_no`,`test_no`,`test_type`,`hydro_pressure_a_side`,`hydro_pressure_b_side`,`date_time`,`test_count`) VALUES('" + Session.get("Valve_Serial_No") + "','" + Session.get("test_no") + "','" + Session.get("Test_Type") + "','" + pressure_a_side + "','" + pressure_b_side + "',NOW(),'" + test_result_by_type_check + "')";
                                            try {
                                                dh.execute(query, connect);
                                            } catch (SQLException e) {
                                                System.out.println(String.valueOf("Test result by type:  " + e.getMessage()));
                                            }
                                        }
                                        current_holding_timer = h_time;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "4":
                                try {
                                    stopModScanThread = true;
                                    if (count_result <= 10) {
                                        if (count_result <= 2) {
                                            stopModScanThread = true;
                                            if (cmbValveLeack.getSelectionModel().getSelectedItem().equals("NONE")) {

                                            } else if (cmbValveLeack.getSelectionModel().getSelectedItem().equals("WATER FLOW METER")) {
                                                //Logic for Actual leakage
                                                int arrLenth = avgWeighingScaleReading.size();
                                                float sum = 0;
                                                for (int i = 0; i < arrLenth; i++) {
                                                    sum += avgWeighingScaleReading.get(i);
                                                }
                                                avgActualReading = sum / arrLenth;
                                                decimalformat.setMaximumFractionDigits(1);
                                                String avg = decimalformat.format(avgActualReading);
                                                System.out.println("AVERAGE READING OF WEIGHING SCALE FROM MODBUS THTHTHTHHTHTHTHTHTHTHTHHTHTHTHHTHTHTHTHHTHTHTHHTHTHT:   " + avg);
                                                try {
                                                    Session.set("write_type", "2");
                                                    Session.set("tag", "F8:83");
                                                    Session.set("write_value", "" + avg);
                                                    Thread.sleep(30);
//                                                    Background_Processes.pause_plc_read();
//                                                    pause_plc_read();

                                                } catch (InterruptedException | NumberFormatException e) {
                                                }
                                            } else {
                                                //bubble counter logic avg actual reading = bubble_counter
                                            }
                                            count_result += 2;
                                        }
                                        count_result++;
                                    }

                                    if (d_d.equals("4") || d_d.equals("5") || d_d.equals("6")) {
                                        try {
                                            get_result(result);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    if (stop_pressure_get) {
                                        if (Session.get("Test_Type").equals("HYDROSTATIC SEAT C SIDE")) {
                                            stop_pressure_a = pressure_b;
                                            stop_pressure_b = pressure_b;
                                            stop_pressure_c = pressure_a;
                                            stop_pressure_d = pressure_b;
                                            stop_pressure_e = pressure_b;
                                        } else if (Session.get("Test_Type").equals("HYDROSTATIC SEAT D SIDE")) {
                                            stop_pressure_a = pressure_a;
                                            stop_pressure_b = pressure_a;
                                            stop_pressure_c = pressure_a;
                                            stop_pressure_d = pressure_b;
                                            stop_pressure_e = pressure_a;
                                        } else if (Session.get("Test_Type").equals("HYDROSTATIC SEAT E SIDE")) {
                                            stop_pressure_a = pressure_b;
                                            stop_pressure_b = pressure_b;
                                            stop_pressure_c = pressure_b;
                                            stop_pressure_d = pressure_b;
                                            stop_pressure_e = pressure_a;
                                        } else {

                                            stop_pressure_a = pressure_a;
                                            stop_pressure_b = pressure_b;
                                            stop_pressure_c = "0";
                                            stop_pressure_d = "0";
                                            stop_pressure_e = "0";
                                            stop_pressure_get = false;
                                        }
                                    }
                                    if (d_d.equals(current_drain_delay)) {
                                    } else {
                                        try {
                                            Platform.runLater(() -> {
//                                            txtDrainDelay.setText(d_d);
                                            });
                                            current_drain_delay = d_d;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                            case "5":
                                try {
                                    if (d_time.equals(current_drain_timer)) {
                                    } else {
                                        try {
                                            Platform.runLater(() -> {
//                                                txtDrainTimer.setText(d_time);
                                            });
                                            current_drain_timer = d_time;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                            case "6":
                                try {
                                    count_result = 0;
                                    String lt = cmbValveLeack.getSelectionModel().getSelectedItem();
                                    String vt = cmbValveType.getSelectionModel().getSelectedItem();
                                    String vc = cmbValveClass.getSelectionModel().getSelectedItem();
                                    String vs = cmbValveSize.getSelectionModel().getSelectedItem();
                                    String hsp = txtHydroSetPressure.getText();
                                    String psu = GaugeActualHydro.getUnit();
                                    if (test_result_count == 0) {
                                        String check_data = "SELECT test_result_id,test_no FROM test_result WHERE test_no = '" + Session.get("test_no") + "';";
                                        ResultSet check = dh.getData(check_data, connect);
                                        if (check.next()) {
                                            String delete_query = "DELETE FROM test_result WHERE test_no = '" + Session.get("test_no") + "';";
                                            try {
                                                dh.execute(delete_query, connect);
                                                delete_count++;
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                                System.out.println(String.valueOf(e.getMessage()));
                                            }
                                        }
                                        Thread.sleep(15);

                                        String start_pressure_a_side = new DecimalFormat("#").format(Double.parseDouble(start_pressure_a));
                                        String start_pressure_b_side = new DecimalFormat("#").format(Double.parseDouble(start_pressure_b));
                                        String stop_pressure_a_side = new DecimalFormat("#").format(Double.parseDouble(stop_pressure_a));
                                        String stop_pressure_b_side = new DecimalFormat("#").format(Double.parseDouble(stop_pressure_b));
                                        // for dbb block
                                        String start_pressure_c_side = new DecimalFormat("#").format(Double.parseDouble(start_pressure_c));
                                        String start_pressure_d_side = new DecimalFormat("#").format(Double.parseDouble(start_pressure_d));
                                        String start_pressure_e_side = new DecimalFormat("#").format(Double.parseDouble(start_pressure_e));
                                        String stop_pressure_c_side = new DecimalFormat("#").format(Double.parseDouble(stop_pressure_c));
                                        String stop_pressure_d_side = new DecimalFormat("#").format(Double.parseDouble(stop_pressure_d));
                                        String stop_pressure_e_side = new DecimalFormat("#").format(Double.parseDouble(stop_pressure_e));
                                        String actLeak = " ";
                                        if (cmbValveLeack.getSelectionModel().getSelectedItem().equals("BUBBLE COUNTER")) {
                                            actLeak = bubble_counter;
                                        } else {
//                                            actLeak = decimalformat.format(avgActualReading);
                                        }

                                        String query = "INSERT INTO test_result (`valve_serial_no`, `test_no`, `test_type`, `leakage_type`, `valve_type`,`valve_size`, `valve_class`, `actual_leakage`, `holding_time`,`over_all_time`, `hydro_set_pressure`,`start_pressure_a`,`start_pressure_b`,`stop_pressure_a`,`stop_pressure_b`, `pressure_unit`, `gauge_serial_no`, `guage_calibration_date`, `test_result`, `date_time`) VALUES('" + Session.get("Valve_Serial_No") + "','" + Session.get("test_no") + "','" + Session.get("Test_Type") + "','" + lt + "','" + vt + "','" + vs + "','" + vc + "','" + actLeak + "','" + Session.get("holding_time") + "','" + oat + "','" + hsp + "','" + start_pressure_a_side + "','" + start_pressure_b_side + "','" + stop_pressure_a_side + "','" + stop_pressure_b_side + "','" + psu + "','" + Session.get("gauge_serial") + "','" + Session.get("gauge_calibration_date") + "','" + result + "',NOW())";
                                        try {
                                            dh.execute(query, connect);
                                            test_result_count++;
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                            System.out.println(String.valueOf("TEST RESULT : " + e.getMessage()));
                                        }

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
//                                dropbox("TestScreen.fxml", false);
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("EXCEPTION IN getting All timers cycleStatusThread : " + e.getLocalizedMessage());
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException intex) {
                            System.err.println("Interupt in cycleStatusThread Exception : " + intex);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Exception in cycleStatusThread: " + e.getMessage());
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException intex) {
                        System.err.println("Interupt in cycleStatusThread Exception : " + intex);
                    }
                }

                //to clear garbage data
            }

        }, "cycleStatusThread");
        cycleStatus.start();

    }

    private void cycle_status(String status) {
        switch (status) {
            case "0":
                Platform.runLater(() -> {
                    txtCycleStatus.setText("");
                });
//                txtCycleStatus.setText("");
                current_cycle_status = "0";
                break;
            case "1":
                Platform.runLater(() -> {
                    txtCycleStatus.setText("CYCLE RUNNING");
                });
                current_cycle_status = "1";
                break;
            case "2":
                Platform.runLater(() -> {
                    txtCycleStatus.setText("STABILIZATION TIMER RUNNING");
                });

                current_cycle_status = "2";
                break;
            case "3":
                Platform.runLater(() -> {
                    txtCycleStatus.setText("HOLDING TIMER  RUNNING");
                });

                current_cycle_status = "3";
                break;
            case "4":
                Platform.runLater(() -> {
                    txtCycleStatus.setText("DRAIN DEALY TIMER RUNNING");
                });

                current_cycle_status = "4";
                break;
            case "5":
                Platform.runLater(() -> {
                    txtCycleStatus.setText("DRAIN TIMER RUNNING");
                });

                current_cycle_status = "5";
                break;
            case "6":
                Platform.runLater(() -> {
                    txtCycleStatus.setText("CYCLE COMPLETE");
                });
                you_can_change = true;
                oat = txtOverAllTime.getText();
//                al = txtActualLeakage.getText();
                current_cycle_status = "6";
                empty_timers();
                txtResult.setStyle("-fx-background-color: derive(#FFFFFF,100%); -fx-text-inner-color:black; -fx-font-size: 20px;");

//                ();
                System.out.println("start_trend LOW");

                break;
            case "7":
                count_result = 0;
                Platform.runLater(() -> {
                    txtCycleStatus.setText("FORCE STOP");
                });
                empty_timers();
                txtResult.setStyle("-fx-background-color: derive(#FFFFFF,100%); -fx-text-inner-color:black; -fx-font-size: 20px;");

                you_can_change = true;
                System.out.println("start_trend LOW");
                current_cycle_status = "7";
                break;
            case "8":
                count_result = 0;
                Platform.runLater(() -> {
                    txtCycleStatus.setText("EMERGENCY");
                });
                you_can_change = true;
                empty_timers();
                txtResult.setStyle("-fx-background-color: derive(#FFFFFF,100%); -fx-text-inner-color:black; -fx-font-size: 20px;");

//                stop_read();
                System.out.println("start_trend LOW");
                current_cycle_status = "8";
                break;
            default:
                txtCycleStatus.setText("");
                you_can_change = true;
                current_cycle_status = "0";
                break;
        }
    }

    private void empty_timers() {
        txtStabilizationTimer.setText("");
        txtHoldingTimer.setText("");
//        txtDrainDelay.setText("");
//        txtDrainTimer.setText("");
        txtOverAllTime.setText("");
        txtResult.setText("");
//        txtActualLeakage.setText("");
    }

    @FXML
    private void btnHomeAction(ActionEvent event) {
    }

    @FXML
    private void btnReportAction(ActionEvent event) {
    }

    @FXML
    private void btnSystemCheckAction(ActionEvent event) {
    }

    @FXML
    private void btnAdminAction(ActionEvent event) throws URISyntaxException, IOException {
        insert_data.stop();
        machine_mode.stop();
//        cycleStatus.stop();

    }

    @FXML
    private void cmbValveTypeAction(ActionEvent event) {
    }

    @FXML
    private void cmbValveClassAction(ActionEvent event) {
//        
//        gauge_initalize(19, 49, "70");

        int index = cmbValveClass.getSelectionModel().getSelectedIndex();
        ToolKit.tagWrite("N7:2", Integer.toString(index));
        read_plc_values();
        
//        System.out.println("sdfsdjfhuygfw64ey825i");
        Gauge_details(cmbValveClass.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void cmbValveSizeAction(ActionEvent event) {
        int index = cmbValveSize.getSelectionModel().getSelectedIndex();
        ToolKit.tagWrite("N7:3", Integer.toString(index));
        read_plc_values();

    }

    @FXML
    private void cmbTypeOfSealingAction(ActionEvent event) {
        int index = cmbTypeOfSealing.getSelectionModel().getSelectedIndex();
        ToolKit.tagWrite("N7:5", Integer.toString(index));
        read_plc_values();

    }

    @FXML
    private void cmbTestStandardsAction(ActionEvent event) {
        int index = cmbTestStandards.getSelectionModel().getSelectedIndex();
        ToolKit.tagWrite("N7:6", Integer.toString(index));

    }

    @FXML
    private void txtStabilizationTimeAction(ActionEvent event) {

    }

    @FXML
    private void txtStabilizationTimeKeyRelease(KeyEvent event) {
        String stabi = txtStabilizationTime.getText();
        ToolKit.tagWrite("N7:24", stabi);
    }

    @FXML
    private void txtHoldingTimeAction(ActionEvent event) {
    }

    @FXML
    private void txtHoldingTimeKeyRelease(KeyEvent event) {
        String hold = txtHoldingTime.getText();
        ToolKit.tagWrite("N7:25", hold);
    }

    @FXML
    private void txtDrainTimeAction(ActionEvent event) {
    }

    @FXML
    private void txtDrainTimeKeyRelease(KeyEvent event) {
        String drain = txtDrainTime.getText();
        ToolKit.tagWrite("N7:26", drain);
    }

    @FXML
    private void txtHydroSetPressureAction(ActionEvent event) {
        String hydro = txtHydroSetPressure.getText();
        ToolKit.tagWrite("N7:30", hydro);
    }

    @FXML
    private void txtHydroPressureKeyRelease(KeyEvent event) {
    }

    @FXML
    private void txtHydraulicSetPressureAction(ActionEvent event) {
        String hydrolic = txtHydraulicSetPressure.getText();
        ToolKit.tagWrite("N7:31", hydrolic);
    }

    @FXML
    private void txtClampingPressureKeyRelease(KeyEvent event) {

    }

    @FXML
    private void radioBarAction(ActionEvent event) {
        ToolKit.tagWrite("N7:20", "0");
    }

    @FXML
    private void radioPsiAction(ActionEvent event) {
        ToolKit.tagWrite("N7:20", "1");
    }

    @FXML
    private void radioradioKgcmAction(ActionEvent event) {
        ToolKit.tagWrite("N7:20", "2");
    }

    @FXML
    private void txtValveSerialNoAction(ActionEvent event) {
        try {
            String valve_s_no = txtValveSerialNo.getText();
            String valve_data_query = "SELECT * FROM valve_data WHERE `valve_serial_no` = '" + valve_s_no + "' ORDER BY `valve_data_id` DESC LIMIT 1;";
            ResultSet rs = dh.getData(valve_data_query, connect);
            if (rs.next()) {
                txtValveTagNo.setText(rs.getString("valve_tag_no"));
                txtIMIRNo.setText(rs.getString("imir_no"));
//                txtManufacturer.setText(rs.getString("manufacturer"));
                cmbCustomer.getSelectionModel().select(rs.getString("customer"));
                cmbManufacturer.getSelectionModel().select(rs.getString("manufacturer"));
                cmbProject.getSelectionModel().select(rs.getString("project"));
//                txtWaterTemperture.setText(rs.getString("water_temperature"));
//                txtProject.setText(rs.getString("project"));
            } else {
                txtValveTagNo.setText("");
                cmbCustomer.getSelectionModel().clearSelection();
                cmbManufacturer.getSelectionModel().clearSelection();
                cmbProject.getSelectionModel().clearSelection();
                txtIMIRNo.setText("");
//                txtManufacturer.setText("");
//                txtWaterTemperture.setText("");
//                txtProject.setText("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void txtWaterTempertureKeyRelease(KeyEvent event) {
    }

    @FXML
    private void cmbProjectAction(ActionEvent event) {
    }

    @FXML
    private void cmbCustomerAction(ActionEvent event) {
    }

    @FXML
    private void cmbManufacturerAction(ActionEvent event) {
    }

    @FXML
    private void cmbOperatorNameAction(ActionEvent event) {
    }

    @FXML
    private void cmbShiftAction(ActionEvent event) {
    }

    @FXML
    private void cmbValveStdAction(ActionEvent event) {
        try {
            String valve_std = cmbValveStd.getSelectionModel().getSelectedItem();
            String Query = "SELECT valve_class From valve_class WHERE valve_standards='" + valve_std + "' ORDER BY valve_class_id ASC";
            ResultSet rs_vstd = dh.getData(Query, connect);
            cmbValveClass.getItems().clear();

            while (rs_vstd.next()) {
                cmbValveClass.getItems().add(rs_vstd.getString("valve_class"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(InitialScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            //Valve_size
            String Valve_Standard = cmbValveStd.getSelectionModel().getSelectedItem();
            String vs = "SELECT vs.valve_size FROM valve_size vs WHERE vs.valve_standards = '" + Valve_Standard + "';";
            System.out.println(vs);
            ResultSet rs_vs = dh.getData(vs, connect);
            cmbValveSize.getItems().clear();
            while (rs_vs.next()) {
                if (rs_vs.getString("valve_size") == null || rs_vs.getString("valve_size").equals("")) {
                } else {
                    cmbValveSize.getItems().add(rs_vs.getString("valve_size"));
                }
            }

            int index = cmbValveStd.getSelectionModel().getSelectedIndex();
            ToolKit.tagWrite("N7:7", Integer.toString(index));

        } catch (SQLException ex) {
            Logger.getLogger(InitialScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void cmbValveLeackAction(ActionEvent event) throws SQLException {

        String write_val = "";
        String leak = cmbValveLeack.getSelectionModel().getSelectedItem();
        String qry = "SELECT write_value_leakage_type FROM leakage_type WHERE test_type = '" + leak + "'";
        ResultSet rs_l = dh.getData(qry, connect);
        if (rs_l.next()) {
            write_val = rs_l.getString("write_value_leakage_type");
        }
        ToolKit.tagWrite("N7:28", write_val);
    }

    //read plc_value
    private void read_plc_values() {
        String[] split_values = ToolKit.readpy("python /media/nsp/AA88D95288D91D9F/hydro_project_window/pro/E1262/E1262/python_plc/read_plc_5.py.py").split("/");
        txtStabilizationTime.setText(split_values[1]);
        txtHoldingTime.setText(split_values[2]);
        txtDrainTime.setText(split_values[3]);
        txtHydroSetPressure.setText(split_values[4]);
        txtHydraulicSetPressure.setText(split_values[5]);
    }

    private void get_result(String result) {
        if (result.equals("1")) {
            txtResult.setStyle("-fx-background-color: derive(#388e3c,50%); -fx-text-inner-color:black; -fx-font-size: 20px;");
            txtResult.setText("TEST OK");
        } else {
            txtResult.setStyle("-fx-background-color: derive(#ac0800,50%); -fx-text-inner-color:white; -fx-font-size: 20px;");
            txtResult.setText("TEST NOT OK");
        }
        try {
            stopCycleStatusThread = false;
            cycleStatus.stop();
        } catch (Exception e) {
        }

    }

    void leakageCheck() throws IOException {
        avgWeighingScaleReading.add(avgActualReading);

        if (cmbValveLeack.getSelectionModel().getSelectedItem().equals("NONE")) {
            System.out.println("Leakage Type NONE DETECTED");
        } else if (cmbValveLeack.getSelectionModel().getSelectedItem().equals("BUBBLE COUNTER")) {
            System.out.println("leakageCheck Bubble Counter in");

            stopModScanThread = false;
            Runnable r = () -> {
//
                while (true) {
                    try {
                        Thread.sleep(500);
                        if (stopModScanThread) {
                            break;
                        }

                        if (ToolKit.isNull(bubble_counter)) {
                            //do nothing
                            System.out.println("NULL in bubble_coounter in leakageCheck");
                        } else {
                            Platform.runLater(() -> {
                                txtLeak.setText(bubble_counter);
                            });
                        }
//                    System.out.println("Float Value : " + floaat);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Exception in catch" + e);
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException intex) {
                            System.err.println("Interupt in leakageCheck Exception : " + intex);
                        }

                    }
                    //to clear garbage data
//                Runtime.getRuntime().gc();
                }

            };
            modScan = new Thread(r, "leakageCheckThread");
//        modScan.setDaemon(true);
            modScan.start();

            avgWeighingScaleReading = null;
            txtLeak.setText(null);

        } else {
            System.out.println("leakageCheck Modbus in");
            stopModScanThread = false;
            ModbusClient modbusClient = new ModbusClient();
            modbusClient.Connect("192.168.0.236", 502);
            Runnable r = () -> {

                while (true) {
                    try {

                        int[] inputRegisters = modbusClient.ReadHoldingRegisters(0, 3);

                        Thread.sleep(500);

                        try {
                            for (int i = 0; i < inputRegisters.length; i++) {
                                System.out.println("for " + i + ":" + inputRegisters[i]);
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                        if (stopModScanThread) {
                            break;
                        }
                        try {
                            int multiplier = inputRegisters[1] * 65528;
                            if (multiplier == 0) {

                                if (inputRegisters[0] == 1) {

                                    //Code for Tag writing in plc 
//                              int a=inputRegisters[0];
//                              string weighing = Integer.toHexString(a);
                                    //System.out.println("::" +weighing );
                                    //setting value to tag in session 
                                    Session.set("weighing", "0");
                                    Session.set("tag_w", "F8:83");
//                               Background_Processes.Write_weighing(); 
                                    Write_weighing();

                                }
                                if (inputRegisters[2] < 0) {
                                    float value = 32767 + inputRegisters[2];
                                    weighingScaleReading = (value + 32768) / 1000;
                                    avgWeighingScaleReading.add(weighingScaleReading);
                                    decimalformat.setMaximumFractionDigits(1);
                                    String f = decimalformat.format(weighingScaleReading);
                                    System.out.println("ACTUAL VALUE 1ST IF : " + weighingScaleReading);
                                    Platform.runLater(() -> {
                                        txtLeak.setText("" + f);
                                    });
                                } else {
                                    float value = inputRegisters[2];
                                    weighingScaleReading = value / 1000;
                                    avgWeighingScaleReading.add(weighingScaleReading);
                                    decimalformat.setMaximumFractionDigits(1);
                                    String f = decimalformat.format(weighingScaleReading);
                                    System.out.println("ACTUAL VALUE 1ST ESLE: " + weighingScaleReading);
                                    Platform.runLater(() -> {
                                        txtLeak.setText("" + f);
                                    });
                                }
                            } else {
                                if (inputRegisters[2] < 0) {
                                    float negativeCorrection = 32767 + inputRegisters[1];
                                    float unsignedValue = negativeCorrection + 32768;
                                    float modbusOneCorrection = 65535 + unsignedValue;
                                    float value = multiplier + unsignedValue;
                                    weighingScaleReading = value / 1000;
                                    avgWeighingScaleReading.add(weighingScaleReading);
                                    decimalformat.setMaximumFractionDigits(1);
                                    String f = decimalformat.format(weighingScaleReading);
                                    System.out.println("ACTUAL VALUE 2ND IF : " + weighingScaleReading);
                                    Platform.runLater(() -> {
                                        txtLeak.setText("" + f);
                                    });
                                } else {
                                    float value = multiplier + inputRegisters[2];
                                    weighingScaleReading = value / 1000;
                                    avgWeighingScaleReading.add(weighingScaleReading);
                                    decimalformat.setMaximumFractionDigits(1);
                                    String f = decimalformat.format(weighingScaleReading);
                                    System.out.println("ACTUAL VALUE 2ND ELSE : " + weighingScaleReading);
                                    Platform.runLater(() -> {
                                        txtLeak.setText("" + f);
                                    });
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                        }

//                    System.out.println("Float Value : " + floaat);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Exception in catch" + e);
                        try {
                            modbusClient.Connect("192.168.0.236", 502);
                        } catch (IOException ex) {
                            Logger.getLogger(TestScreenController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
                //to clear garbage data
//                Runtime.getRuntime().gc();
            };
            modScan = new Thread(r, "leakageCheckThread");
//        modScan.setDaemon(true);
            modScan.start();
            avgWeighingScaleReading = null;
            txtLeak.setText(null);
        }
    }

    public void Write_weighing() {
        try {
            System.out.println("Thread started insert : ");
            //call tagwrite function for plc 
            ToolKit.tagWrite(Session.get("tag_w"), Session.get("weighing"));

        } catch (Exception e) {
        }

    }

    @FXML
    private void cmbTestTypeAction(ActionEvent event) {
        int index = cmbTestType.getSelectionModel().getSelectedIndex();
        ToolKit.tagWrite("N7:1", Integer.toString(index));
        leakage_type();
    }

    private void date_time() {
        time.scheduleAtFixedRate(date, 0, 1000);
    }

    Timer time = new Timer();

    TimerTask date = new TimerTask() {
        @Override
        public void run() {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            txtDate.setText("Date:" + dtf.format(now));

        }
    };

    @FXML
    private void cmbGaugeHydroAction(ActionEvent event) {
    }

}
