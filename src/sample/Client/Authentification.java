package sample.Client;

import Controller.Classes.Client;
import Controller._client;
import Core._db;
import Core._func;
import Core._notify;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class Authentification {
    Stage window ;
    public Authentification(Stage window){
        this.window = window;
    }

    public void Init(){
        final Stage[] dialog = {new Stage()};


        TextField email = new TextField();
        PasswordField password = new PasswordField();

        Button btnConne = new Button("Connexion");
        Button btnRegister = new Button("Inscription");

        btnConne.setOnAction(click -> {
            email.getStyleClass().removeAll("text-error");
            password.getStyleClass().removeAll("text-error");
            if(_func.isNull(email.getText())){
                email.requestFocus();
                email.getStyleClass().add("text-error");
            }
            else if(_func.isNull(password.getText())){
                password.requestFocus();
                password.getStyleClass().add("text-error");
            }
            else{
                List<Client> clt = _client.Get(_db.Where("email = "+_db.Str(email.getText())+" AND mot_de_passe = "+_db.Str(password.getText()),"client","*"));
                if(clt != null){
                    dialog[0].close();
                    User user = new User(window,clt.get(0));
                    user.Init();
                }
                else {
                    email.requestFocus();
                    _notify.Show("Erreur","Erruer d'information","Ce client n'existe pas !!!", Alert.AlertType.ERROR);
                }
            }
        });
        btnRegister.setOnAction(click ->{
            dialog[0].close();
            Register register = new Register(window);
            register.Init();
        });

        dialog[0] = new Stage();
        VBox dialogVbox = new VBox(20);
        HBox btnsBox = new HBox(50);
        btnsBox.getChildren().addAll(btnConne,btnRegister);
        btnsBox.setAlignment(Pos.CENTER);

        dialogVbox.getChildren().add(_func.getImage("user",100,100));
        dialogVbox.getChildren().add(new Label("Email"));
        dialogVbox.getChildren().add(email);
        dialogVbox.getChildren().add(new Label("Mot de passe"));
        dialogVbox.getChildren().add(password);
        dialogVbox.getChildren().add(btnsBox);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setPadding(new Insets(30));
        dialogVbox.setSpacing(10);

        Scene dialogScene = new Scene(dialogVbox, 300, 300);
        dialogScene.getStylesheets().add("tmp/CSS/auth.css");

        dialog[0].initModality(Modality.APPLICATION_MODAL);
        dialog[0].initOwner(window);
        dialog[0].getIcons().add(new Image("tmp/Images/auth.png"));
        dialog[0].setTitle("Se connecter");
        dialog[0].resizableProperty().setValue(Boolean.FALSE);
        dialog[0].setScene(dialogScene);
        dialog[0].show();
    }
}
