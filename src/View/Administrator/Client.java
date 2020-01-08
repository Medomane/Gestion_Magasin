package View.Administrator;

import Controller._client;
import Core._controls;
import Core._db;
import Core._func;
import Core._notify;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.List;

public class Client {
    private BorderPane root ;
    public Tab tab ;
    private GridPane grid = new GridPane();
    Button[] buttons = new Button[4];

    private TextField nomTextField = new TextField();
    private TextField prenomTextField = new TextField();
    private TextField telephoneTextField = new TextField();
    private TextField emailTextField = new TextField();
    private PasswordField mot_de_passeTextField = new PasswordField();

    private Label nbrCommands = new Label();
    private Label purchases = new Label();

    private ImageView mainImage = _func.getImage("customer",200,200);

    private TextField filterTextField = new TextField();

    private TableView<Controller.Classes.Client> table = new TableView<>();

    public Client(){
        root = new BorderPane();
        tab = new Tab("Client",root);
    }
    public void Init(){
        initElements();
        Refresh();
        EventHandling();
        addToRoot();
    }
    public void initElements() {
        buttons[0] = new Button();buttons[1] = new Button();buttons[2] = new Button();buttons[3] = new Button();
        // GridPane
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

        grid.add(new Label("Nombre de commande"), 0, 5);
        grid.add(new Label(":"), 1, 5);
        grid.add(nbrCommands, 2, 5);

        grid.add(new Label("Total"), 0, 6);
        grid.add(new Label(":"), 1, 6);
        grid.add(purchases, 2, 6);

        //TableView
        TableColumn<Controller.Classes.Client, Long> idColumn = new TableColumn<>("Id");
        idColumn.setMaxWidth(32);
        idColumn.setMinWidth(32);
        TableColumn<Controller.Classes.Client, String> nomColumn = new TableColumn<>("Nom");
        nomColumn.setMaxWidth(66);
        nomColumn.setMinWidth(66);
        TableColumn<Controller.Classes.Client, String> prenomColumn = new TableColumn<>("Prénom");
        prenomColumn.setMaxWidth(66);
        prenomColumn.setMinWidth(66);
        TableColumn<Controller.Classes.Client, String> telephoneColumn = new TableColumn<>("Téléphone");
        telephoneColumn.setMaxWidth(66);
        telephoneColumn.setMinWidth(66);
        TableColumn<Controller.Classes.Client, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setMaxWidth(67);
        emailColumn.setMinWidth(67);
        TableColumn<Controller.Classes.Client, String> mot_de_passeColumn = new TableColumn<>("Mot de passe");
        mot_de_passeColumn.setVisible(false);
        table.getColumns().addAll(idColumn,nomColumn, prenomColumn,telephoneColumn,emailColumn,mot_de_passeColumn);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        mot_de_passeColumn.setCellValueFactory(new PropertyValueFactory<>("mot_de_passe"));

    }
    public void EventHandling() {
        buttons[0].setOnAction(click -> {
            if(_client.isVerified(-1,nomTextField,prenomTextField,telephoneTextField,emailTextField,mot_de_passeTextField)){
                Controller.Classes.Client p = new Controller.Classes.Client(1,nomTextField.getText(),prenomTextField.getText(),telephoneTextField.getText(),emailTextField.getText(),mot_de_passeTextField.getText());
                if(_client.Get(_db.Where("email = "+_db.Str(p.getEmail()),"client","*")) == null){
                    if(_client.Create(p)){
                        table.getItems().add(p);
                        purchases.setText("0");
                        nbrCommands.setText("0");
                        _func.Translate(buttons[0].getGraphic(),-200,400);
                    }
                }
                else _notify.Show("Erreur","Erreur  d'information","Email déjà existe !!!", Alert.AlertType.ERROR);
            }
        });
        buttons[1].setOnAction(click -> {
            Controller.Classes.Client p = table.getSelectionModel().getSelectedItem();
            if(p == null) _notify.Show("Erreur","Selection", "Vous devez selectionner un client !!!", Alert.AlertType.ERROR);
            else if (_client.isVerified(p.getId(),nomTextField,prenomTextField,telephoneTextField,emailTextField,mot_de_passeTextField)) {
                p.setNom(nomTextField.getText());
                p.setPrenom(prenomTextField.getText());
                p.setTelephone(telephoneTextField.getText());
                p.setEmail(emailTextField.getText());
                p.setMot_de_passe(mot_de_passeTextField.getText());
                _client.Update(p);
                table.getItems().set(table.getSelectionModel().getSelectedIndex(), p);
                var res = _client.purchases(p.getId());
                purchases.setText(Double.toString(res[0]));
                nbrCommands.setText(Integer.toString((int)res[1]));
                _func.Fade(buttons[1].getGraphic(),400,1,0.1,2);
            }
        });
        buttons[2].setOnAction(click -> {
            Controller.Classes.Client p = table.getSelectionModel().getSelectedItem();
            if(p != null) {
                if(_notify.Confirm("Confirmation",p.toString(),"Voulez-vous vraiment supprimer ce client !!!")) {
                    _client.Delete(p.getId());
                    table.getItems().remove(p);
                    table.getSelectionModel().clearSelection();
                    clearFields();
                }
            }
            else _notify.Show("Erreur","Selection", "Vous devez selectionner un client !!!", Alert.AlertType.ERROR);
        });
        buttons[3].setOnAction(click -> Refresh());
        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Controller.Classes.Client> list = _client.Get(newValue);
            if(list != null) {
                table.getItems().clear();
                table.getItems().addAll(list);
            }
        });
        table.getSelectionModel().selectedItemProperty().addListener((observableValue, client, t1) -> {
            Controller.Classes.Client p = table.getSelectionModel().getSelectedItem();
            if (p != null) {
                nomTextField.setText(p.getNom());
                prenomTextField.setText(p.getPrenom());
                emailTextField.setText(p.getEmail());
                telephoneTextField.setText(p.getTelephone());
                mot_de_passeTextField.setText(p.getTelephone());
                var res = _client.purchases(p.getId());
                purchases.setText(Double.toString(res[0]));
                nbrCommands.setText(Integer.toString((int)res[1]));
            }
        });
    }
    public void clearFields(){
        nomTextField.clear();
        prenomTextField.clear();
        emailTextField.clear();
        telephoneTextField.clear();
        mot_de_passeTextField.clear();
        purchases.setText("0");
        nbrCommands.setText("0");
    }
    public void addToRoot() {
        root.setCenter(_controls.CenterBox(mainImage,grid));
        root.setLeft(_controls.LeftBox(buttons));
        root.setRight(_controls.RightBox(new Node[]{filterTextField,table},300));
        root.setBottom(_controls.BottomBox("Gestion des clients"));
    }
    public void Refresh() {
        var rotate = _func.Rotate(buttons[3].getGraphic());
        buttons[3].setDisable(true);
        if(!table.getItems().isEmpty()) table.getItems().clear();
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(400);
                List<Controller.Classes.Client> t = _client.Get();
                if (t != null) table.getItems().addAll(t);
                _func.Print(t.size());
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
