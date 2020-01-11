package sample.Administrator;

import Config.Conf;
import View.Administrator.Categorie;
import View.Administrator.Client;
import View.Administrator.Detail;
import View.Administrator.Produit;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Administrateur {
    Stage window ;
    public Administrateur(Stage window){
        this.window = window;
    }
    public void Init(){
        final Stage[] dialog = {new Stage()};
        dialog[0] = new Stage();

        BorderPane root = new BorderPane();

        Client clientView = new Client();
        clientView.Init();
        Produit produitView = new Produit(dialog[0]);
        produitView.Init();
        Categorie categorieView = new Categorie(dialog[0]);
        categorieView.Init();
        Detail detailView = new Detail();

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(categorieView.tab,produitView.tab,clientView.tab,detailView.tab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        root.setCenter(tabPane);

        Scene dialogScene = new Scene(root, 800, 500);
        dialogScene.getStylesheets().add("tmp/CSS/views.css");

        dialog[0].initModality(Modality.APPLICATION_MODAL);
        dialog[0].initOwner(window);
        dialog[0].getIcons().add(new Image("tmp/Images/logo.png"));
        dialog[0].setTitle(Conf.AppName);
        dialog[0].resizableProperty().setValue(Boolean.FALSE);
        dialog[0].setScene(dialogScene);
        dialog[0].show();
    }
}
