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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import util.ClientConnection;
import util.impl.ClientConnectionImpl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * @author : H.C.Kaligu Jayanath
 * Date    : 5/22/2023
 * Time    : 11:19 PM
 */
public class ChatFormController extends Window {
    @FXML
    public VBox vboxpane;
    @FXML
    public Text txtsendername;
    @FXML
    public AnchorPane paneLogin;
    @FXML
    public JFXTextField txtFldgetname;
    @FXML
    public AnchorPane mainpane;
    @FXML
    public ImageView btnlogout;
    @FXML
    public ImageView btnclose;
    @FXML
    public ImageView btnminimise;
    @FXML
    public AnchorPane chatpane;
    @FXML
    public JFXTextField txtFldmsg;
    @FXML
    public AnchorPane emojipane;
    @FXML
    public GridPane gridpaneemoji;

    //ClientConnection referrence
    ClientConnection clientConnection;

    //mouse location x,y reference
    private double x=0;
    private double y=0;

    //server shared image location path
    private static final String cashedimgloaction = "client_side/lk/playtech/resources/Shared-Img-Location";

    //host
    String host = "localhost";

    //port
    int port =5000;

    //static method for client class's access and update UI when recieving msg
    public static void receivemsg(VBox vBox, String message) {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-left;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:   #2980b9;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl); //label add to hbox
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox); //message added hbox add to vbox
            }
        });
    }

    //static method for client class's access and update UI when recieving img
    public static void receiveImg(VBox vBox, String message) {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-left;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:   #2980b9;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");


        Button button = new Button("Click here to Show! ");
        //button action
        button.setOnAction(e -> {

            //create filechooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(cashedimgloaction));  //server shared images saved path in client side
            File selectedFile = fileChooser.showOpenDialog(null);
            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
            if (selectedFile != null) {  //if image selected
                button.setVisible(false);
                ImageView imageView = new ImageView(); //create image
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
                    vBox.getChildren().add(hBox1); //selected image added hbox add to vbox
                });
            }else{  //if image not selected
                //show alert for select image
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Image Download Warning");
                alert.setHeaderText("Select received image to download to Chat!");
                alert.setContentText("This image show in once.Next time show in this file path next recieved photo.If you are not select this recived image you can't see in this chat history.Therefore please select and open it.");
                alert.show();
            }
        });

        hBox.getChildren().add(messageLbl); //label add to hbox
        hBox.getChildren().add(button); //button add to hbox

        Platform.runLater(() -> {
            vBox.getChildren().add(hBox);  //button,label added hbox add to vbox
        });

    }


    public void initialize(){
        setEmojiesToPane(); //set emojies to ui grid pane
    }

    private void setEmojiesToPane() {
        int codePoint = 0x1F600;   //emoji character code starting point
        for (int j = 0; j < 10; j++) {   //for add emojies to row wise
            for (int i = 0; i < 8; i++) {  //for add emojies to column wise
                Label text = new Label(new String(Character.toChars(codePoint)));   //Character code convert to string to show emoji
                text.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        txtFldmsg.appendText(text.getText());   //add selected emoji to textarea at ui
                    }
                });
                text.setStyle("-fx-font-size: 35px;" +
                        " -fx-font-family: 'Noto Emoji';" +
                        "-fx-text-alignment: center;");
                gridpaneemoji.add(text,i,j); //add emojies to grid pane
                codePoint++;
            }
        }
    }

    //if msg sent update at ui
    private void msgSendedUpdateatUI(String message) {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-right;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:  #27ae60;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl); //add hbox to label
        vboxpane.getChildren().add(hBox); //add hob to vbox at FXML
    }

    public void btnLoginToCHatOnAction(ActionEvent actionEvent) { //login button click action
        txtsendername.setText(txtFldgetname.getText()); //update sender name in UI
        if(!txtFldgetname.getText().equals("")){
            try {
                clientConnection = new ClientConnectionImpl(new Socket(host,port),txtFldgetname.getText());  //create connection to server
                txtFldgetname.clear();
                paneLogin.setVisible(false);
                chatpane.setVisible(true);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR , "Serever not connected.Try Again!");
                alert.show();
            }
            clientConnection.listentoServerMsgsImgs(vboxpane); //start listen to server and send controller's UI to update
            clientConnection.clientSendMsgtoServer("");  //send msg to server
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR , "Enter your name to join the chat.Try Again!");
            alert.show();
        }
    }


    public void OnActionMouseDraggedwindowupbar(MouseEvent mouseEvent) { //mouse move with move pane when dragging upper menu
        Stage primaryStage = (Stage) mainpane.getScene().getWindow();
        primaryStage.setX(mouseEvent.getScreenX() - x);
        primaryStage.setY(mouseEvent.getScreenY() - y);
    }

    public void OnActionMousePressedwindowupbar(MouseEvent mouseEvent) { //get mouse location
        x = mouseEvent.getSceneX();
        y = mouseEvent.getSceneY();
    }

    public void btncloseMouseClickedAction(MouseEvent mouseEvent) {
        System.exit(0);  //close programme
    }

    public void btncloseMouseEnterdAction(MouseEvent mouseEvent) {
        btnclose.setImage(new Image("images/redclose-50.png"));
    }

    public void btncloseMouseExitedAction(MouseEvent mouseEvent) {
        btnclose.setImage(new Image("images/whiteclose.png"));
    }

    public void btnminimizeMouseClickedAction(MouseEvent mouseEvent) {  //minimize window
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
        String message = txtFldmsg.getText();  //get text senders text field

        if (!message.isEmpty()) {
            msgSendedUpdateatUI(message);  //update ui
            clientConnection.clientSendMsgtoServer(message); //send msg to server
            txtFldmsg.clear();
        }
    }

    public void btnaddemojiestoChatOnActionClicked(MouseEvent mouseEvent) {
        emojipane.setVisible(true);
    }

    public void btnaddphototoChatOnActionClicked(MouseEvent mouseEvent) {  //image adding action
        //create file chooser
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
            imageView.setImage(image); //selected image added to image view in hbox
        } catch (IOException ex) {
//            Logger.getLogger(JavaFXPixel.class.getName()).log(Level.SEVERE, null, ex);
        }
        clientConnection.clientSendImgtoServer(file);  //send image to send server

        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-right;-fx-fill-height: true;-fx-padding: 10");
        hBox.getChildren().add(imageView); //add hobx to imageview

        vboxpane.getChildren().add(hBox); //add hbox to vbox at ui
    }

    public void msgtypefieldclickonAction(MouseEvent mouseEvent) {
        emojipane.setVisible(false);
    }

    public void chatpaneclickonAction(MouseEvent mouseEvent) {
        emojipane.setVisible(false);
    }
}
