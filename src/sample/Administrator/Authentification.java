package sample.Administrator;

import Config.Hook;
import Core._func;
import Core._notify;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Authentification {
    Stage window ;
    public Authentification(Stage window){
        this.window = window;
    }

    public void Init(){
        final Stage[] dialog = {new Stage()};

        TextField username = new TextField();
        PasswordField password = new PasswordField();

        Button btnConne = new Button("Connexion");
        btnConne.setOnAction(click -> {
            username.getStyleClass().removeAll("text-error");
            password.getStyleClass().removeAll("text-error");
            if(_func.isNull(username.getText())){
                username.requestFocus();
                username.getStyleClass().add("text-error");
            }
            else if(_func.isNull(password.getText())){
                password.requestFocus();
                password.getStyleClass().add("text-error");
            }
            else{
                if(username.getText().equals(Hook.username) && password.getText().equals(Hook.password)){
                    dialog[0].close();
                    Administrateur admin = new Administrateur(window);
                    admin.Init();
                }
                else {
                    username.requestFocus();
                    _notify.Show("Erreur","Erruer d'information","Cet administrateur n'existe pas !!!", Alert.AlertType.ERROR);
                }
            }
        });

        dialog[0] = new Stage();
        VBox dialogVbox = new VBox(20);

        dialogVbox.getChildren().add(_func.getImage("img_auth",100,100));
        dialogVbox.getChildren().add(new Label("Nom d'utilisateur"));
        dialogVbox.getChildren().add(username);
        dialogVbox.getChildren().add(new Label("Mot de passe"));
        dialogVbox.getChildren().add(password);
        dialogVbox.getChildren().add(btnConne);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setPadding(new Insets(30));
        dialogVbox.setSpacing(10);

        Scene dialogScene = new Scene(dialogVbox, 300, 300);
        dialogScene.getStylesheets().add("tmp/CSS/auth.css");

        dialog[0].initModality(Modality.APPLICATION_MODAL);
        dialog[0].initOwner(window);
        dialog[0].getIcons().add(new Image("tmp/Images/auth.png"));
        dialog[0].setTitle("Authentification");
        dialog[0].resizableProperty().setValue(Boolean.FALSE);
        dialog[0].setScene(dialogScene);
        dialog[0].show();
    }
}
