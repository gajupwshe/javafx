package e1262;

import java.util.Calendar;
import java.util.Optional;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author dsp
 */
public class Dialog {

    public static void showAndGetResponse(String title, String headerText, String contextText) {
        Platform.runLater(() -> {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(headerText);
            alert.setContentText(contextText);

            alert.showAndWait();
        });

    }

    public static void showAndWait(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("CAutomate");
            alert.setHeaderText("");

            alert.setContentText(message);

            alert.showAndWait();
        });
    }

    public static void showForSometime(String title, String message, String headerText, double width, int time) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.getDialogPane().setMinWidth(width);
            alert.setContentText(message);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);
            stage.toFront();
            System.out.println("timeline Started -" + Calendar.getInstance().getTime());
            Timeline idlestage = new Timeline(new KeyFrame(Duration.seconds(time), (ActionEvent event) -> {
                alert.setResult(ButtonType.CANCEL);
                alert.hide();
            }));
            idlestage.setCycleCount(1);
            idlestage.play();

            alert.showAndWait();
        });
    }

    public static void showDialog(String title, String alertText) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(alertText);

            alert.showAndWait();
        });
    }

    public static Optional<String> showInputDialog(String message) {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("CAutomate");
        dialog.setHeaderText(message);

        Optional<String> result = dialog.showAndWait();

        return result;
    }

    public static Alert showConfirmationDialog(String messege) {
        Alert alert = new Alert(AlertType.CONFIRMATION, messege, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        return alert;
    }

    public static Alert showConfirmationDialogWithCustomButtons(String messege, ButtonType buttonType1, ButtonType buttonType2) {
        Alert alert = new Alert(AlertType.CONFIRMATION, messege, buttonType1, buttonType2, ButtonType.CANCEL);
        alert.showAndWait();

        return alert;
    }

    public static Optional<ButtonType> ConfirmationDialog(String title, String message, double width) {

        Alert alert = new Alert(AlertType.NONE, message, ButtonType.YES, ButtonType.NO);
        alert.getDialogPane().setMinWidth(width);
//        alert.setTitle(title);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.toFront();
        Optional<ButtonType> result = alert.showAndWait();

        return result;
    }

    public static Optional<ButtonType> ConfirmationDialog_Single_button(String title, String message, double width) {

        Alert alert = new Alert(AlertType.NONE, message, ButtonType.OK);
        alert.getDialogPane().setMinWidth(width);
//        alert.setTitle(title);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.toFront();
        Optional<ButtonType> result = alert.showAndWait();

        return result;
    }
}
