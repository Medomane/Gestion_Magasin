package View.Administrator;

import Controller._categorie;
import Core._controls;
import Core._db;
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

public class Categorie {
    private Stage window ;
    private BorderPane root ;
    public Tab tab ;
    private GridPane grid = new GridPane();
    Button[] buttons = new Button[4];

    private FileChooser fileChooser = new FileChooser();
    private TextField libelleTextField = new TextField();

    private Label nbrProducts = new Label();
    private Label sales = new Label();
    private Label qteSold = new Label();

    private boolean flag = true ;

    private ImageView mainImage = _func.getImage("category",200,200);

    private TextField filterTextField = new TextField();

    private TableView<Controller.Classes.Categorie> table = new TableView<>();

    public Categorie(Stage window){
        this.window = window;
        root = new BorderPane();
        tab = new Tab("Catégorie",root);
    }
    public void Init(){
        initElements();
        Refresh();
        EventHandling();
        addToRoot();
    }
    public void initElements() {
        buttons[0] = new Button();buttons[1] = new Button();buttons[2] = new Button();buttons[3] = new Button();

        // grid Elements
        grid.add(new Label("Libellé"), 0, 0);
        grid.add(new Label(":"), 1, 0);
        grid.add(libelleTextField, 2, 0);

        grid.add(new Label("Nombre de produits"), 0, 1);
        grid.add(new Label(":"), 1, 1);
        grid.add(nbrProducts, 2, 1);

        grid.add(new Label("Nombre de produits vendus"), 0, 2);
        grid.add(new Label(":"), 1, 2);
        grid.add(qteSold, 2, 2);

        grid.add(new Label("Total"), 0, 3);
        grid.add(new Label(":"), 1, 3);
        grid.add(sales, 2, 3);

        grid.setAlignment(Pos.TOP_CENTER);

        //TableView
        TableColumn<Controller.Classes.Categorie, Long> idColumn = new TableColumn<>("Id");
        idColumn.setMaxWidth(32);
        idColumn.setMinWidth(32);
        TableColumn<Controller.Classes.Categorie, String> libelleColumn = new TableColumn<>("Libellé");
        libelleColumn.setMaxWidth(150);
        libelleColumn.setMinWidth(150);
        TableColumn<Controller.Classes.Categorie, String> avatarColumn = new TableColumn<>("Avatar");
        avatarColumn.setVisible(false);
        table.getColumns().addAll(idColumn,libelleColumn, avatarColumn);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        libelleColumn.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        avatarColumn.setCellValueFactory(new PropertyValueFactory<>("avatar"));

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);
    }
    private String getExtension(){
        String fileName = new File(mainImage.getImage().getUrl().replace("file:/","")).getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) return fileName.substring(fileName.lastIndexOf(".")+1);
        return "";
    }
    public void EventHandling() {
        buttons[0].setOnAction(click -> {
            if(_categorie.isVerified(-1,libelleTextField)){
                Controller.Classes.Categorie c = new Controller.Classes.Categorie(1,libelleTextField.getText(),"category_");
                try {
                    File sf = new File(Path.of(URLDecoder.decode(Path.of(mainImage.getImage().getUrl().replace("file:/","")).toAbsolutePath().toString(), StandardCharsets.UTF_8)).toUri());
                    if(sf.exists()){
                        if(_categorie.Get(_db.Where("libelle = "+_db.Str(c.getLibelle()),"categorie","*")) == null){
                            if(_categorie.Create(c)){
                                c.setAvatar("category_"+c.getId()+"."+getExtension());
                                _categorie.Update(c);
                                table.getItems().add(c);
                                File df = new File(Path.of(URLDecoder.decode(Path.of("src/tmp/Images/categories/category_"+c.getId()+"."+getExtension()).toAbsolutePath().toString(), StandardCharsets.UTF_8)).toUri());
                                Files.copy(sf.toPath(),df.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                sales.setText("0");
                                qteSold.setText("0");
                                sales.setText("0");
                                _func.Translate(buttons[0].getGraphic(),-200,400);
                            }
                        }
                    }
                } catch (Exception e) {
                    _notify.Show("Erreur","Erreur d'image",e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
        buttons[1].setOnAction(click -> {
            Controller.Classes.Categorie c = table.getSelectionModel().getSelectedItem();
            if(c == null) _notify.Show("Erreur","Selection", "Vous devez selectionner une catégorie !!!", Alert.AlertType.ERROR);
            else if (_categorie.isVerified(c.getId(),libelleTextField)) {
                try {
                    File sf = new File(Path.of(URLDecoder.decode(Path.of(mainImage.getImage().getUrl().replace("file:/","")).toAbsolutePath().toString(), StandardCharsets.UTF_8)).toUri());
                    if(sf.exists()){
                        c.setLibelle(libelleTextField.getText());
                        c.setAvatar("category_"+c.getId()+"."+getExtension());
                        if(_categorie.Get(_db.Where("id <> "+c.getId()+" AND libelle = "+_db.Str(c.getLibelle()),"categorie","*")) == null){
                            File df = new File(Path.of(URLDecoder.decode(Path.of("src/tmp/Images/categories/category_"+c.getId()+"."+getExtension()).toAbsolutePath().toString(), StandardCharsets.UTF_8)).toUri());
                            Files.copy(sf.toPath(),df.toPath(),StandardCopyOption.REPLACE_EXISTING);
                            _categorie.Update(c);
                            table.getItems().set(table.getSelectionModel().getSelectedIndex(), c);
                            mainImage.resize(200,200);
                            sales.setText(Double.toString(_categorie.Sales(c.getId())[0]));
                            qteSold.setText(Integer.toString((int)_categorie.Sales(c.getId())[1]));
                            var lst = _categorie.Products(c.getId());
                            nbrProducts.setText( (lst == null) ? "0": Integer.toString(lst.size()) );
                            _func.Fade(buttons[1].getGraphic(),400,1,0.1,2);
                        }
                        else _notify.Show("Erreur","Erreur d'information","Catégorie déjà existe !!!", Alert.AlertType.ERROR);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        buttons[2].setOnAction(click -> {
            Controller.Classes.Categorie c = table.getSelectionModel().getSelectedItem();
            if(c != null) {
                if(_notify.Confirm("Confirmation",c.toString(),"Voulez-vous vraiment supprimer cette catégorie !!!")) {
                    _categorie.Delete(c.getId());
                    table.getItems().remove(c);
                    table.getSelectionModel().clearSelection();
                    clearFields();
                }
            }
            else _notify.Show("Erreur","Selection", "Vous devez selectionner une catégorie !!!", Alert.AlertType.ERROR);
        });
        buttons[3].setOnAction(click -> Refresh());
        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Controller.Classes.Categorie> list = _categorie.Get(newValue);
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
                libelleTextField.setText(newSelection.getLibelle());
                sales.setText(Double.toString(_categorie.Sales(newSelection.getId())[0]));
                qteSold.setText(Integer.toString((int)_categorie.Sales(newSelection.getId())[1]));
                var lst = _categorie.Products(newSelection.getId());
                nbrProducts.setText( (lst == null) ? "0": Integer.toString(lst.size()) );
                File f = new File(Path.of(URLDecoder.decode(Path.of("src/tmp/Images/categories/"+newSelection.getAvatar()).toAbsolutePath().toString(), StandardCharsets.UTF_8)).toUri());
                if(f.exists()){
                    try{ mainImage.setImage(new Image(f.toURI().toString()));}
                    catch (Exception ignored){}
                    mainImage.resize(200,200);
                }
            }
            if(oldSelection == null) flag = false ;
        });
    }
    public void clearFields(){
        libelleTextField.clear();
        sales.setText("0");
        qteSold.setText("0");
        nbrProducts.setText("0");
    }
    public void addToRoot() {
        root.setCenter(_controls.CenterBox(mainImage,grid));
        root.setLeft(_controls.LeftBox(buttons));
        root.setRight(_controls.RightBox(new Node[]{filterTextField,table},185));
        root.setBottom(_controls.BottomBox("Gestion des catégories"));
    }
    public void Refresh() {
        var rotate = _func.Rotate(buttons[3].getGraphic());
        buttons[3].setDisable(true);
        if(!table.getItems().isEmpty()) table.getItems().clear();
        Task<Void> sleeper = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(400);
                List<Controller.Classes.Categorie> t = _categorie.Get();
                if (t != null) table.getItems().addAll(t);
                flag = true;
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> {
            rotate.stop();
            buttons[3].setDisable(false);
        });
        new Thread(sleeper).start();
    }
}
