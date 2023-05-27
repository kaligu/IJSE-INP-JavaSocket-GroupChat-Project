/*
  @ Author       : C.Kaligu Jayanath
  @ Prjoect Name : IJSE-INP-JavaSocket-GroupChat-Project
  @ Date         : 5/22/2023
  @ Time         : 11:19 PM
*/
package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import util.Client;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/22/2023
 * Time    : 11:19 PM
 */
public class ChatFormController extends Window implements Observer {

    public VBox vboxpane;
    public TextArea txtareamsg;
    public TextField txtfldmsg;
    public Text txtsendername;
    @FXML
    public AnchorPane paneLogin;
    @FXML
    public JFXTextField txtFldgetname;
    public AnchorPane mainpane;
    public ImageView btnlogout;
    public ImageView btnclose;
    public ImageView btnminimise;
    public AnchorPane chatpane;
    public JFXTextField txtFldmsg;
    public AnchorPane emojipane;
    public GridPane gridpaneemoji;
    Client client;
    private double x=0;
    private double y=0;

    public static void receivemsg(VBox vBox, String message) {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-left;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:   #2980b9;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }


    public static void receiveImg(VBox vBox, String message) {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-left;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");

        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:   #2980b9;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);


        Button button = new Button("Click here to Show! ");
        button.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("client_side/lk/playtech/resources/Shared-Img-Location"));

            File selectedFile = fileChooser.showOpenDialog(null);

            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

            if (selectedFile != null) {

                ImageView imageView = new ImageView();
                try {
                    imageView.setFitWidth(300);
                    imageView.setFitHeight(300);
                    BufferedImage bufferedImage = ImageIO.read(selectedFile);
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    imageView.setImage(image);
                } catch (IOException ex) {
//            Logger.getLogger(JavaFXPixel.class.getName()).log(Level.SEVERE, null, ex);
                }


                HBox hBox1 = new HBox();
                hBox1.getChildren().add(imageView);
                Platform.runLater(() -> {
                    vBox.getChildren().add(hBox1);
                });
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Image Download Warning");
                alert.setHeaderText("Select received image to download to Chat!");
                alert.setContentText("This image show in once.If you are not select this recived image you can't see in this chat history.Therefore please select and open it.");
                ButtonType okButton = new ButtonType("ReOpen", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(okButton, cancelButton);

                alert.show();
            }
        });
        hBox.getChildren().add(button);



//        System.out.println("controllerimage recived********************************************");
////                        String imgurl = dataInputStream.readUTF(0);
//
//
//
       ;


        Platform.runLater(() -> {
            vBox.getChildren().add(hBox);
        });
    }


    public void initialize(){
        setEmojiesToPane(); //set emojies to ui grid pane
    }

    private void setEmojiesToPane() {
//        int EMOJI_INDEX = 0;
//        for (int j = 0; j < 3; j++) {
//            for (int i = 0; i < 5;  i++) {
//                Label text = new Label(new String(Character.toChars(emojis[EMOJI_INDEX++])));
//                text.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent event) {
////                        messageTextField.appendText(text.getText());
//
//                    }
//                });
//                text.setStyle("-fx-font-size: 25px;" +
//                        " -fx-font-family: 'Noto Emoji';" +
//                        "-fx-text-alignment: center;");
//                gridpaneemoji.add(text, i, j);
//            }
//        }

        int codePoint = 0x1F600;
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 8; i++) {
                Label text = new Label(new String(Character.toChars(codePoint)));
                text.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        txtFldmsg.appendText(text.getText());
                    }
                });
                text.setStyle("-fx-font-size: 35px;" +
                        " -fx-font-family: 'Noto Emoji';" +
                        "-fx-text-alignment: center;");
                gridpaneemoji.add(text,i,j);
                codePoint++;
            }
        }

    }

    public void txtfldmsgOnAction(ActionEvent actionEvent) {

    }

    private void sendMessage(String message) {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-right;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:  #27ae60;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        vboxpane.getChildren().add(hBox);

    }

    public void receiveMessage(String message, VBox vBox){
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-left; -fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:   #2980b9;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }


    @Override
    public void update(Observable o, Object arg) {

    }

    public void btnLoginToCHatOnAction(ActionEvent actionEvent) {
        txtsendername.setText(txtFldgetname.getText());
        if(!txtFldgetname.getText().equals("")){
            try {
                client = new Client(new Socket("localhost",5000),txtFldgetname.getText());
                txtFldgetname.clear();
                paneLogin.setVisible(false);
                chatpane.setVisible(true);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR , "Serever not connected.Try Again!");
                alert.show();
            }
            client.listenForMessage(vboxpane);
            client.clientSendMessage("");
        }

    }


    public void OnActionMouseDraggedwindowupbar(MouseEvent mouseEvent) {
        Stage primaryStage = (Stage) mainpane.getScene().getWindow();
        primaryStage.setX(mouseEvent.getScreenX() - x);
        primaryStage.setY(mouseEvent.getScreenY() - y);
    }

    public void OnActionMousePressedwindowupbar(MouseEvent mouseEvent) {
        x = mouseEvent.getSceneX();
        y = mouseEvent.getSceneY();
    }

    public void btnlogoutMouseClickedAction(MouseEvent mouseEvent) {

        chatpane.setVisible(false);
        paneLogin.setVisible(true);
    }

    public void btnlogoutMouseEnteredAction(MouseEvent mouseEvent) {
        btnlogout.setImage(new Image("images/redlogout.png"));
    }

    public void btnlogoutMouseExitedAction(MouseEvent mouseEvent) {
        btnlogout.setImage(new Image("images/iwhitelogout.png"));
    }

    public void btncloseMouseClickedAction(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void btncloseMouseEnterdAction(MouseEvent mouseEvent) {
        btnclose.setImage(new Image("images/redclose-50.png"));
    }

    public void btncloseMouseExitedAction(MouseEvent mouseEvent) {
        btnclose.setImage(new Image("images/whiteclose.png"));
    }

    public void btnminimizeMouseClickedAction(MouseEvent mouseEvent) {
        Stage primaryStage = (Stage) mainpane.getScene().getWindow();
        primaryStage.setIconified(true); // Minimize the stage
    }

    public void btnminimizeMouseEnteredAction(MouseEvent mouseEvent) {
        btnminimise.setImage(new Image("images/minred.png"));
    }

    public void btnminimizeMouseExitedAction(MouseEvent mouseEvent) {
        btnminimise.setImage(new Image("images/minwhite.png"));
    }

    public void txtfldMsgtypeOnAction(ActionEvent actionEvent) {
        String message = txtFldmsg.getText();  //set text

        if (!message.isEmpty()) {
            sendMessage(message);
            client.clientSendMessage(message);
            txtFldmsg.clear();
        }
    }

    public void btnaddemojiestoChatOnActionClicked(MouseEvent mouseEvent) {
        emojipane.setVisible(true);
    }

    public void btnaddphototoChatOnActionClicked(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        ImageView imageView = new ImageView();
        try {
            imageView.setFitWidth(300);
            imageView.setFitHeight(300);
            BufferedImage bufferedImage = ImageIO.read(file);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(image);
        } catch (IOException ex) {
//            Logger.getLogger(JavaFXPixel.class.getName()).log(Level.SEVERE, null, ex);
        }
        client.clientSendImage(file);

        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-right;-fx-fill-height: true;-fx-padding: 10");
        hBox.getChildren().add(imageView);

        vboxpane.getChildren().add(hBox);
    }


    public void msgtypefieldclickonAction(MouseEvent mouseEvent) {
        emojipane.setVisible(false);
    }

    public void chatpaneclickonAction(MouseEvent mouseEvent) {
        emojipane.setVisible(false);
    }
}
