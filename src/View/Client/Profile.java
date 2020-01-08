package View.Client;

import Controller.Classes.Client;
import Controller._client;
import Core._controls;
import Core._func;
import View.Administrator.Detail;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.sql.Ref;
import java.util.Date;
import java.util.List;

public class Profile {
    private Stage window ;
    private BorderPane root ;
    public Tab tab ;
    private Client client ;

    TextField filterField ;
    private GridPane grid;
    Label lblInfo ;
    private ImageView mainImage ;
    private Button refresh ;
    Button btnSubmit;

    private TextField nomTextField ;
    private TextField prenomTextField;
    private TextField telephoneTextField ;
    private TextField emailTextField ;
    private PasswordField mot_de_passeTextField ;

    HBox topBox;

    private TableView<Detail.info> table ;

    public Profile(Stage window,Client client){
        this.window = window;
        root = new BorderPane();
        tab = new Tab("Profile",root);
        this.client = client;
    }
    public void Init(){
        InitElements();
        addToRoot();
    }
    public void InitElements(){
        lblInfo = new Label();
        lblInfo.setTextAlignment(TextAlignment.CENTER);
        lblInfo.setAlignment(Pos.CENTER_LEFT);

        filterField = new TextField();

        refresh = new Button("Actualiser");
        refresh.setGraphic(_func.getImage("refresh",17,17));
        refresh.setCursor(Cursor.HAND);

        nomTextField = new TextField(client.getNom());
        prenomTextField = new TextField(client.getPrenom());
        telephoneTextField = new TextField(client.getTelephone());
        emailTextField =new TextField(client.getEmail());
        mot_de_passeTextField = new PasswordField();
        mot_de_passeTextField.setText(client.getMot_de_passe());

        topBox = new HBox();
        HBox topRightBox = new HBox();
        topRightBox.setMinWidth(100);
        topRightBox.setAlignment(Pos.CENTER_RIGHT);
        topRightBox.getChildren().add(refresh);

        HBox topLeftBox = new HBox();
        topLeftBox.setMinWidth(1055);
        topLeftBox.setAlignment(Pos.CENTER_LEFT);
        topLeftBox.getChildren().add(lblInfo);
        topLeftBox.setPadding(new Insets(0,0,0,5));

        topBox.getChildren().addAll(topLeftBox,topRightBox);
        topBox.setAlignment(Pos.CENTER_RIGHT);
        topBox.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));

        mainImage = _func.getImage("customer",200,200);

        grid =new GridPane();
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

        btnSubmit = new Button("Modifier");
        btnSubmit.setGraphic(_func.getImage("edit",20,20));

        grid.add(btnSubmit, 2, 5);


        table = new TableView<>();
        double width = 133;
        TableColumn<Detail.info,String> categorieColumn = new TableColumn<>("Catégorie");
        categorieColumn.setMinWidth(width);
        categorieColumn.setMaxWidth(width);
        TableColumn<Detail.info,String> designationColumn = new TableColumn<>("Désignation");
        designationColumn.setMinWidth(width);
        designationColumn.setMaxWidth(width);
        TableColumn<Detail.info,Long> qteColumn = new TableColumn<>("Quantité");
        qteColumn.setMinWidth(width);
        qteColumn.setMaxWidth(width);
        TableColumn<Detail.info,Long> stColumn = new TableColumn<>("Sous total");
        stColumn.setMinWidth(width);
        stColumn.setMaxWidth(width);
        TableColumn<Detail.info, Date> dateColumn = new TableColumn<>("Date");
        dateColumn.setMinWidth(width);
        dateColumn.setMaxWidth(width);
        TableColumn<Detail.info,String> userColumn = new TableColumn<>("Client");
        userColumn.setVisible(false);
        TableColumn<Detail.info,String> typeColumn = new TableColumn<>("Type de paiement");
        typeColumn.setMinWidth(width);
        typeColumn.setMaxWidth(width);

        table.getColumns().addAll(categorieColumn,designationColumn, qteColumn,stColumn,dateColumn,userColumn,typeColumn);

        categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        designationColumn.setCellValueFactory(new PropertyValueFactory<>("designation"));
        qteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        stColumn.setCellValueFactory(new PropertyValueFactory<>("sousTotal"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("client"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type_de_paiement"));

        Refresh();
        events();
    }
    public void addToRoot() {
        root.setTop(topBox);
        root.setCenter(_controls.CenterBox(mainImage,grid));
        root.setRight(_controls.RightBox(new Node[]{filterField,table},800));
        root.setBottom(_controls.BottomBox("Informations de votre profil"));
    }

    void Refresh(){
        refresh.setDisable(true);
        table.getItems().clear();
        var rotate = _func.Rotate(refresh.getGraphic());
        Task<String> sleeper = new Task<String>() {
            @Override
            protected String call() throws Exception {
                Thread.sleep(400);

                List<Detail.info> t = Detail.info.Get(client.getId());
                double total = 0;
                int nbr = 0;
                if (t != null){
                    table.getItems().addAll(t);
                    for(Detail.info tmp:t){
                        nbr++;
                        total += tmp.getSousTotal();
                    }
                }
                return "Nombre de commande est : "+nbr+" commande"+((nbr>1)?"s":"")+" avec un total de "+total+" MAD" ;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                lblInfo.setText(sleeper.getValue());
                rotate.stop();
                refresh.setDisable(false);
            }
        });
        new Thread(sleeper).start();
    }
    public void events(){
        btnSubmit.setOnAction(click ->{
            if(_client.isVerified(client.getId(),nomTextField,prenomTextField,telephoneTextField,emailTextField,mot_de_passeTextField)){
                client.setNom(nomTextField.getText());
                client.setPrenom(prenomTextField.getText());
                client.setTelephone(telephoneTextField.getText());
                client.setEmail(emailTextField.getText());
                client.setMot_de_passe(mot_de_passeTextField.getText());
                _client.Update( client );
                window.setTitle(client.getPrenom()+" "+client.getNom());
                _func.Fade(btnSubmit.getGraphic(),400,1,0.1,2);
            }
        });
        refresh.setOnAction(click ->{
            Refresh();
        });
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            table.getItems().clear();
            var rotate = _func.Rotate(refresh.getGraphic());
            Task<String> sleeper = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    List<Detail.info> t = Detail.info.Get(newValue,client.getId());
                    double total = 0;
                    int nbr = 0;
                    if (t != null){
                        table.getItems().addAll(t);
                        for(Detail.info tmp:t){
                            nbr++;
                            total += tmp.getSousTotal();
                        }
                    }
                    return "Nombre de commande est : "+nbr+" commande"+((nbr>1)?"s":"")+" avec un total de "+total+" MAD" ;
                }
            };
            sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    lblInfo.setText(sleeper.getValue());
                    rotate.stop();
                    refresh.setDisable(false);
                }
            });
            new Thread(sleeper).start();
        });
    }
}