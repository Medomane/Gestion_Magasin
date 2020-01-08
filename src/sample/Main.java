package sample;

import Config.Conf;
import Core._func;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    BorderPane root = new BorderPane();

    HBox centerHBox = new HBox();
    HBox TopHBox = new HBox();
    Button btnUser = new Button();
    Button btnAdmin = new Button();
    Button btnClose = new Button();

    public void initEvent(Stage window){
        //Close btn
        btnClose.setOnAction(click -> {
            window.close();
        });
        //---------------
        //admin btn
        btnAdmin.setOnAction(click -> {
            sample.Administrator.Authentification auth = new sample.Administrator.Authentification(window);
            auth.Init();
        });
        //---------------
        //users btn
        btnUser.setOnAction(click -> {
            sample.Client.Authentification auth = new sample.Client.Authentification(window);
            auth.Init();
        });
        //---------------
    }
    public void initElements(){
        //Buttons home
        btnAdmin.setGraphic(_func.getImage("admin",200,200));
        btnAdmin.getStyleClass().add("btn-home");
        btnAdmin.setContentDisplay(ContentDisplay.TOP);
        btnAdmin.setText("Administrateur");
        btnUser.setGraphic(_func.getImage("commander",200,200));
        btnUser.getStyleClass().add("btn-home");
        btnUser.setContentDisplay(ContentDisplay.TOP);
        btnUser.setText("Commander");
        //---------------
        //Center HBox
        centerHBox.getChildren().add(btnAdmin);
        centerHBox.getChildren().add(btnUser);
        centerHBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        centerHBox.setSpacing(40);
        centerHBox.setAlignment(Pos.CENTER);
        //---------------
        //Close button
        btnClose.getStyleClass().add("close-btn");
        btnClose.setText("X");
        btnClose.setCancelButton(true);
        btnClose.setAlignment(Pos.TOP_RIGHT);
        //---------------
        //TopHBox
        TopHBox.getChildren().add(btnClose);
        Label lbl = new Label(Conf.AppName);
        lbl.getStyleClass().add("top-title");
        TopHBox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        HBox rightButtons = new HBox(btnClose);
        rightButtons.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(rightButtons, Priority.ALWAYS);
        TopHBox.getChildren().addAll(lbl,rightButtons);
        TopHBox.setPadding(new Insets(2));
        TopHBox.setAlignment(Pos.CENTER);
    }
    public void addToRoot(){
        root.setCenter(centerHBox);
        root.setTop(TopHBox);
    }

    @Override
    public void start(Stage window) throws Exception{
        initElements();
        addToRoot();
        initEvent(window);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("tmp/CSS/home.css");

        window.getIcons().add(new Image("tmp/Images/logo.png"));
        window.setWidth(1000);
        window.setHeight(700);
        window.initStyle(StageStyle.UNDECORATED);
        window.setTitle(Conf.AppName);
        window.setScene(scene);
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
