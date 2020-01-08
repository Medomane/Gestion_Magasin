package View.Administrator;

import Controller.Classes.Categorie;
import Controller._categorie;
import Controller._client;
import Controller._produit;
import Core._controls;
import Core._func;
import Core._notify;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class Produit {
    private Stage window ;
    private BorderPane root ;
    public Tab tab ;
    private GridPane grid = new GridPane();
    Button[] buttons = new Button[4];

    private TextField designationTextField = new TextField();
    private TextField prixTextField = new TextField();
    private _controls.NumberTextField qteDispoTextField = new _controls.NumberTextField();
    private FileChooser fileChooser = new FileChooser();
    private ComboBox categorieComboBox ;

    private Label sales = new Label();

    private boolean flag = true ;

    private ImageView mainImage = _func.getImage("product",200,200);

    private TextField filterTextField = new TextField();

    private TableView<Controller.Classes.Produit> table = new TableView<>();

    public Produit(Stage window){
        this.window = window;
        root = new BorderPane();
        tab = new Tab("Produit",root);
    }
    public void Init(){
        initElements();
        Refresh();
        EventHandling();
        addToRoot();
    }
    public void initElements() {
        buttons[0] = new Button();buttons[1] = new Button();buttons[2] = new Button();buttons[3] = new Button();

        categorieComboBox = new ComboBox();
        var list = _categorie.Get();
        if(list != null){
            for(Categorie cat : list) categorieComboBox.getItems().add(cat);
            categorieComboBox.getSelectionModel().selectFirst();
        }

        // grid Elements
        grid.add(new Label("Désignation"), 0, 0);
        grid.add(new Label(":"), 1, 0);
        grid.add(designationTextField, 2, 0);

        grid.add(new Label("Prix"), 0, 1);
        grid.add(new Label(":"), 1, 1);
        grid.add(prixTextField, 2, 1);

        grid.add(new Label("Quantité en stocke"), 0, 2);
        grid.add(new Label(":"), 1, 2);
        grid.add(qteDispoTextField, 2, 2);

        grid.add(new Label("Catégorie"), 0, 3);
        grid.add(new Label(":"), 1, 3);
        grid.add(categorieComboBox, 2, 3);

        grid.add(new Label("Total"), 0, 4);
        grid.add(new Label(":"), 1, 4);
        grid.add(sales, 2, 4);

        grid.setAlignment(Pos.TOP_CENTER);

        //TableView
        TableColumn<Controller.Classes.Produit, Long> idColumn = new TableColumn<>("Id");
        idColumn.setMaxWidth(30);
        idColumn.setMinWidth(30);
        TableColumn<Controller.Classes.Produit, String> designationColumn = new TableColumn<>("Désignation");
        designationColumn.setMaxWidth(148);
        designationColumn.setMinWidth(148);
        TableColumn<Controller.Classes.Produit, String> prixColumn = new TableColumn<>("Prix");
        prixColumn.setMaxWidth(60);
        prixColumn.setMinWidth(60);
        TableColumn<Controller.Classes.Produit, String> qteDispoColumn = new TableColumn<>("Qte dispo");
        qteDispoColumn.setMaxWidth(60);
        qteDispoColumn.setMinWidth(60);
        TableColumn<Controller.Classes.Produit, String> imageColumn = new TableColumn<>("Image");
        imageColumn.setVisible(false);
        TableColumn<Controller.Classes.Produit, String> categorieColumn = new TableColumn<>("Catégorie");
        categorieColumn.setVisible(false);

        table.getColumns().addAll(idColumn, designationColumn,prixColumn,qteDispoColumn,imageColumn, categorieColumn);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        designationColumn.setCellValueFactory(new PropertyValueFactory<>("designation"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        qteDispoColumn.setCellValueFactory(new PropertyValueFactory<>("quantite_disponible"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("img"));
        categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie_id"));

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);
    }
    private boolean isVerified(){
        designationTextField.getStyleClass().removeAll("text-error");
        prixTextField.getStyleClass().removeAll("text-error");
        qteDispoTextField.getStyleClass().removeAll("text-error");
        categorieComboBox.getStyleClass().removeAll("text-error");

        if(_func.isNull(designationTextField.getText())){
            designationTextField.requestFocus();
            designationTextField.getStyleClass().add("text-error");
            return false ;
        }
        else if(_func.isNull(prixTextField.getText())){
            prixTextField.requestFocus();
            prixTextField.getStyleClass().add("text-error");
            return false ;
        }
        else if(!_func.isDouble(qteDispoTextField.getText())){
            qteDispoTextField.requestFocus();
            qteDispoTextField.getStyleClass().add("text-error");
            return false ;
        }
        else if(categorieComboBox.getValue() == null){
            categorieComboBox.requestFocus();
            categorieComboBox.getStyleClass().add("text-error");
            return false ;
        }
        return true ;
    }
    private String getExtension(){
        String fileName = new File(mainImage.getImage().getUrl().replace("file:/","")).getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) return fileName.substring(fileName.lastIndexOf(".")+1);
        return "";
    }
    public void EventHandling() {
        buttons[0].setOnAction(click -> {
            if(isVerified()){
                Controller.Classes.Produit p = new Controller.Classes.Produit(1,designationTextField.getText(),Double.parseDouble(prixTextField.getText()),Long.parseLong(qteDispoTextField.getText()),"product_",((Categorie)categorieComboBox.getValue()).getId());
                try {
                    File sf = new File(Path.of(URLDecoder.decode(Path.of(mainImage.getImage().getUrl().replace("file:/","")).toAbsolutePath().toString(), StandardCharsets.UTF_8)).toUri());
                    if(sf.exists()){
                        if(_produit.Create(p)){
                            p.setImg("product_"+p.getId()+"."+getExtension());
                            _produit.Update(p);
                            table.getItems().add(p);
                            File df = new File(Path.of(URLDecoder.decode(Path.of("src/tmp/Images/products/product_"+p.getId()+"."+getExtension()).toAbsolutePath().toString(), StandardCharsets.UTF_8)).toUri());
                            Files.copy(sf.toPath(),df.toPath(),StandardCopyOption.REPLACE_EXISTING);
                            _func.Translate(buttons[0].getGraphic(),-200,400);
                        }
                    }
                }
                catch (Exception e) {
                    _notify.Show("Erreur","Erreur d'image",e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
        buttons[1].setOnAction(click -> {
            Controller.Classes.Produit p = table.getSelectionModel().getSelectedItem();
            if(p == null) _notify.Show("Erreur", "Selection", "Vous devez selectionner un produit !!!", Alert.AlertType.ERROR);
            else if (isVerified()) {
                try {
                    File sf = new File(Path.of(URLDecoder.decode(Path.of(mainImage.getImage().getUrl().replace("file:/","")).toAbsolutePath().toString(), StandardCharsets.UTF_8)).toUri());
                    if(sf.exists()){
                        File df = new File(Path.of(URLDecoder.decode(Path.of("src/tmp/Images/products/product_"+p.getId()+"."+getExtension()).toAbsolutePath().toString(), StandardCharsets.UTF_8)).toUri());
                        Files.copy(sf.toPath(),df.toPath(),StandardCopyOption.REPLACE_EXISTING);
                        p.setDesignation(designationTextField.getText());
                        p.setPrix(Double.parseDouble(prixTextField.getText()));
                        p.setQuantite_disponible(Long.parseLong(qteDispoTextField.getText()));
                        p.setImg("product_"+p.getId()+"."+getExtension());
                        p.setCategorie_id(((Categorie)categorieComboBox.getValue()).getId());
                        _produit.Update(p);
                        table.getItems().set(table.getSelectionModel().getSelectedIndex(), p);
                        mainImage.resize(200,200);
                        _func.Fade(buttons[1].getGraphic(),400,1,0.1,2);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        buttons[2].setOnAction(click -> {
            Controller.Classes.Produit p = table.getSelectionModel().getSelectedItem();
            if(p != null) {
                if(_notify.Confirm("Confirmation",p.toString(),"Voulez-vous vraiment supprimer ce produit !!!")) {
                    _produit.Delete(p.getId());
                    table.getItems().remove(p);
                    table.getSelectionModel().clearSelection();
                    clearFields();
                }
            }
            else _notify.Show("Erreur","Selection", "Vous devez selectionner un client !!!", Alert.AlertType.ERROR);
        });
        buttons[3].setOnAction(click -> Refresh());
        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Controller.Classes.Produit> list = _produit.Get(newValue);
            if(list != null) {
                table.getItems().clear();
                table.getItems().addAll(list);
            }
        });
        mainImage.setOnMouseClicked(click -> {
            File file = fileChooser.showOpenDialog(this.window);
            if (file != null) {
                mainImage.setImage(new Image(file.toURI().toString()));
                mainImage.setSmooth(true);
                mainImage.setPreserveRatio(true);
                mainImage.setCache(true);
                mainImage.resize(200,200);
            }
        });
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if ((newSelection != null && oldSelection != null) || flag) {
                assert newSelection != null;
                designationTextField.setText(newSelection.getDesignation());
                prixTextField.setText(Double.toString(newSelection.getPrix()));
                qteDispoTextField.setText(Long.toString(newSelection.getQuantite_disponible()));
                categorieComboBox.getSelectionModel().select(_categorie.Get(newSelection.getCategorie_id()));
                sales.setText(Double.toString(_produit.Sales(newSelection.getId())));
                File f = new File(Path.of(URLDecoder.decode(Path.of("src/tmp/Images/products/"+newSelection.getImg()).toAbsolutePath().toString(), StandardCharsets.UTF_8)).toUri());
                if(f.exists()){
                    try{ mainImage.setImage(new Image(f.toURI().toString()));}
                    catch (Exception e){}
                    mainImage.resize(200,200);
                }
            }
            if(oldSelection == null) flag = false ;
        });
    }
    public void clearFields(){
        designationTextField.clear();
        prixTextField.clear();
        qteDispoTextField.clear();
        categorieComboBox.getSelectionModel().selectFirst();
        sales.setText("0");
    }
    public void addToRoot() {
        root.setCenter(_controls.CenterBox(mainImage,grid));
        root.setLeft(_controls.LeftBox(buttons));
        root.setRight(_controls.RightBox(new Node[]{filterTextField,table},300));
        root.setBottom(_controls.BottomBox("Gestion des produits"));
    }
    public void Refresh() {
        table.getItems().clear();
        categorieComboBox.getItems().clear();
        var rotate = _func.Rotate(buttons[3].getGraphic());
        buttons[3].setDisable(true);
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(400);
                List<Controller.Classes.Produit> t = _produit.Get();
                if (t != null) table.getItems().addAll(t);
                flag = true ;
                var list = _categorie.Get();
                if(list != null) categorieComboBox.getItems().addAll(list);
                return null ;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                rotate.stop();
                buttons[3].setDisable(false);
            }
        });
        new Thread(sleeper).start();
    }
}
