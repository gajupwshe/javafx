/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package e1262;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import static javafx.scene.paint.Color.RED;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author hydro
 */
public class LoginScreenController implements Initializable {

    Thread Initialize_Initial;
    @FXML
    private AnchorPane AnchorPane;
//    private JFXTextField txtusername;
    @FXML
    private JFXButton btnLogin;
//    private JFXPasswordField txtPassword;
//    private HBox hboxMessage;
    private Text txtMessage;
//    private JFXButton btnMessage;
//    @FXML
//    private JFXPasswordField txtUser;
    @FXML
    private JFXPasswordField txtPass;
    @FXML
    private JFXButton brnExit;
    @FXML
    private FontAwesomeIconView fntuser;
    @FXML
    private FontAwesomeIconView fntlock;
    @FXML
    private FontAwesomeIconView fntunlock;
    @FXML
    private JFXTextField txtUser;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fntunlock.setVisible(false);
        Session.set("user_type", "operator");
//        ToolKit.madefadeinTrinsition(AnchorPane);
//        btnMessage.setVisible(false);
//        hboxMessage.setVisible(false);

        Initialize_Initial = new Thread(() -> {
            Background_Processes.Initialize_Initial_Screen();
            System.out.println("Initialized");
        }, "intialScreenInitializer");
        ;
        Initialize_Initial.setDaemon(true);
        Initialize_Initial.start();
    }

    DatabaseHandler dh = new DatabaseHandler();
    Connection connect = dh.MakeConnection();

    @FXML
    private void btnLoginAction(ActionEvent event) throws InterruptedException {
        login_check();
    }

    @FXML
    private void btnExitAction(ActionEvent event) {
        Session.distroyAll();
        ToolKit.unloadScreen(AnchorPane);
        System.exit(0);
    }

    private void btnMessageAction(ActionEvent event) {
//        hboxMessage.setVisible(false);
//        btnMessage.setVisible(false);
    }

    void txtPasswordAction(ActionEvent event) {
        login_check();
    }

    private void login_check() {
        String username = txtUser.getText();
        String password = txtPass.getText();

        String query = "SELECT * FROM User_data WHERE username = '" + username + "' AND password = '" + password + "';";
        System.out.println("query : " +query);
        try {
            ResultSet rs = dh.getData(query, connect);
            if (rs.next()) {
                System.out.println("query: " +query);
                fntlock.setVisible(false);
                fntunlock.setVisible(true);
//                long start = System.currentTimeMillis();
                Session.set("user_type", rs.getString("user_type"));
//                Session.set("name", rs.getString("name"));
//                Session.set("display_valve_data", "0");
                Platform.runLater(() -> {
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("InitialScreen.fxml"));
                        ToolKit.loadScreen(root);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                ToolKit.unloadScreen(AnchorPane);
//                long stop = System.currentTimeMillis();
//                System.out.println("Load Iniit "+(stop-start));
            } else {
                Timeline idlestage = new Timeline(new KeyFrame(Duration.seconds(1.5), (ActionEvent event) -> {
//                    hboxMessage.setVisible(false);
//                    btnMessage.setVisible(false);
                }));
                idlestage.setCycleCount(1);
                idlestage.play();
                fntlock.setFill(RED);
                fntuser.setFill(RED);
                txtPass.setFocusColor(RED);
                txtUser.setFocusColor(RED);
                txtUser.setUnFocusColor(RED);
                txtPass.setUnFocusColor(RED);
                fntlock.setVisible(true);
                fntunlock.setVisible(false);
//                hboxMessage.setVisible(true);
                txtMessage.setText("Invalid Username/Password!!!!");
//                btnMessage.setVisible(true);
            }
        } catch (Exception e) {
        }
    }

    @FXML
    private void txtPassAction(ActionEvent event) {
        login_check();
    }

    @FXML
    private void txtPassKeyRelease(KeyEvent event) {
        
    }
}
