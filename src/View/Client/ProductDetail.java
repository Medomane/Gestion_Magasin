package View.Client;

import Controller._produit;
import Core._func;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProductDetail {
    private Stage window ;
    private Offres.TProduit product ;
    public ProductDetail(Stage window, Offres.TProduit product){
        this.window = window ;
        this.product = product;
    }
    public void Init(){
        final Stage[] dialog = {new Stage()};
        dialog[0] = new Stage();

        BorderPane root = new BorderPane();
        root.setCenter(_func.getImage("products/"+_produit.Get(product.getId()).getImg(),600,600));

        Scene dialogScene = new Scene(root, 600, 600);
        dialog[0].initModality(Modality.APPLICATION_MODAL);
        dialog[0].initOwner(window);
        dialog[0].getIcons().add(new Image("tmp/Images/product.png"));
        dialog[0].setTitle(product.getDesignation());
        dialog[0].resizableProperty().setValue(Boolean.FALSE);
        dialog[0].setScene(dialogScene);
        dialog[0].show();
    }
}
