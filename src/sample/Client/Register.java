package sample.Client;

import Controller._client;
import Core._controls;
import Core._db;
import Core._func;
import Core._notify;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Register {
    Stage window ;

    TextField nomTextField = new TextField();
    TextField prenomTextField = new TextField();
    TextField telephoneTextField = new TextField();
    TextField emailTextField = new TextField();
    PasswordField mot_de_passeTextField = new PasswordField();
    public Register(Stage window){
        this.window = window;
    }
    public void Init(){
        final Stage[] dialog = {new Stage()};

        GridPane grid = new GridPane();
        grid.add(new Label("Nom"), 0, 0);
        grid.add(new Label(":"), 1, 0);
        grid.add(nomTextField, 2, 0);

        grid.add(new Label("Prénom"), 0, 1);
        grid.add(new Label(":"), 1, 1);
        grid.add(prenomTextField, 2, 1);

        grid.add(new Label("Téléphone"), 0, 2);
        grid.add(new Label(":"), 1, 2);
        grid.add(telephoneTextField, 2, 2);

        grid.add(new Label("Email"), 0, 3);
        grid.add(new Label(":"), 1, 3);
        grid.add(emailTextField, 2, 3);

        grid.add(new Label("Mot de passe"), 0, 4);
        grid.add(new Label(":"), 1, 4);
        grid.add(mot_de_passeTextField, 2, 4);
        grid.setMinWidth(200);


        dialog[0] = new Stage();
        Button btnConne = new Button("Connexion");
        btnConne.setOnAction(click -> {
            dialog[0].close();
            Authentification user = new Authentification(window);
            user.Init();
        });
        Button btnRegister = new Button("Inscription");
        btnRegister.setOnAction(click ->{
            if(_client.isVerified(-1,nomTextField,prenomTextField,telephoneTextField,emailTextField,mot_de_passeTextField)){
                Controller.Classes.Client p = new Controller.Classes.Client(1,nomTextField.getText(),prenomTextField.getText(),telephoneTextField.getText(),emailTextField.getText(),mot_de_passeTextField.getText());
                if(_client.Get(_db.Where("email = "+_db.Str(p.getEmail()),"client","*")) == null){
                    if(_client.Create(p)){
                        _notify.Show("Succès","Inscription, vous pouvez vous connectez maintenant.","L'inscription a été effectuée avec succès !!!", Alert.AlertType.INFORMATION);
                        dialog[0].close();
                        Authentification user = new Authentification(window);
                        user.Init();
                    }
                }
                else _notify.Show("Erreur","Erreur  d'information","Email déjà existe !!!", Alert.AlertType.ERROR);
            }
        });

        VBox dialogVbox = new VBox(10);

        HBox btnsBox = new HBox(20);
        btnsBox.getChildren().addAll(btnConne,btnRegister);
        btnsBox.setAlignment(Pos.CENTER);

        //dialogVbox.getChildren().add(_func.getImage("user",100,100));
        dialogVbox.getChildren().add(_controls.CenterBox(_func.getImage("new_user",100,100),grid));
        dialogVbox.getChildren().add(btnsBox);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setPadding(new Insets(30));
        dialogVbox.setSpacing(10);

        Scene dialogScene = new Scene(dialogVbox);
        dialogScene.getStylesheets().add("tmp/CSS/auth.css");

        dialog[0].initModality(Modality.APPLICATION_MODAL);
        dialog[0].initOwner(window);
        dialog[0].getIcons().add(new Image("tmp/Images/auth.png"));
        dialog[0].setTitle("Inscription");
        dialog[0].resizableProperty().setValue(Boolean.FALSE);
        dialog[0].setScene(dialogScene);
        dialog[0].show();
    }

}
