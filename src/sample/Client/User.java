package sample.Client;

import Controller.Classes.Client;
import View.Client.Offres;
import View.Client.Profile;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class User {
    Stage window ;
    Client client ;
    public User(Stage window, Client client){
        this.window = window;
        this.client = client;
    }
    public void Init(){
        final Stage[] dialog = {new Stage()};
        dialog[0] = new Stage();

        BorderPane root = new BorderPane();

        Offres offres = new Offres(dialog[0],client);
        offres.Init();

        Profile profile = new Profile(dialog[0],client);
        profile.Init();

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(offres.tab,profile.tab);//categorieView.tab,produitView.tab,clientView.tab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        root.setCenter(tabPane);

        Scene dialogScene = new Scene(root,1155,700);
        dialogScene.getStylesheets().add("tmp/CSS/user.css");

        dialog[0].initModality(Modality.APPLICATION_MODAL);
        dialog[0].initOwner(window);
        dialog[0].getIcons().add(new Image("tmp/Images/logo.png"));
        dialog[0].setTitle(client.getPrenom() + " "+client.getNom());
        dialog[0].resizableProperty().setValue(Boolean.FALSE);
        dialog[0].setScene(dialogScene);
        dialog[0].show();
    }
}
