package View.Client;

import Controller.Classes.*;
import Controller._categorie;
import Controller._commande;
import Controller._detail;
import Controller._produit;
import Core._controls;
import Core._db;
import Core._func;
import Core._notify;
import javafx.animation.*;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

public class Offres {
    public class TProduit{
        private long id ;
        private String designation ;
        private double montant;
        private long qteCommande;
        private String categorie;
        public TProduit(){}

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public double getMontant() {
            return montant;
        }

        public void setMontant(double montant) {
            this.montant = montant;
        }

        public long getQteCommande() {
            return qteCommande;
        }

        public void setQteCommande(long qteCommane) {
            this.qteCommande = qteCommane;
        }

        public String getCategorie() {
            return categorie;
        }

        public void setCategorie(String categorie) {
            this.categorie = categorie;
        }
    }

    private Stage window ;
    private BorderPane root ;
    public Tab tab ;
    private Client client ;

    //Top
    HBox topBox;
    Label topLabel ;

    //Right
    VBox rightBox ;
    ComboBox topRightCombo ;
    Label topRightLabel ;
    private TableView<TProduit> table ;
    HBox bottomRightBox ;
    Label bottomRightLabel;
    Button bottomRightButton ;

    //Bottom
    HBox bottomBox;
    HBox listCategoryBox;
    Button rBtn ;
    Button lBtn ;
    ScrollPane sp ;

    //Center
    ScrollPane scrollPaneCenter ;
    GridPane grid ;


    public Offres(Stage window, Client client){
        this.window = window;
        root = new BorderPane();
        tab = new Tab("Offres",root);
        this.client = client;

        topBox = new HBox();
        topLabel = new Label("Commander");

        rightBox = new VBox();
        topRightLabel = new Label("Panier");
        topRightCombo = new ComboBox();
        table = new TableView<>();
        bottomRightBox = new HBox();
        bottomRightButton = new Button("Commander");
        bottomRightLabel = new Label("0.0");

        bottomBox = new HBox();
        listCategoryBox = new HBox();
        sp = new ScrollPane();
        scrollPaneCenter = new ScrollPane();
        grid = new GridPane();
    }
    void initElements(){
        topBox.getChildren().add(new Label("Commander"));
        topBox.setPadding(new Insets(10));
        topBox.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        topBox.setAlignment(Pos.CENTER);
        root.setTop(topBox);

        bottomRightButton.setMinWidth(90);
        bottomRightButton.setCursor(Cursor.HAND);
        bottomRightLabel.setMinWidth(88);
        Label tmpLbl = new Label("Total : ");
        tmpLbl.setMinWidth(40);
        bottomRightBox.getChildren().add(tmpLbl);
        bottomRightBox.getChildren().add(bottomRightLabel);
        topRightCombo.getItems().addAll("Chèque","Effet","Espece","Virement bancaire");
        topRightCombo.getSelectionModel().selectFirst();
        topRightCombo.setMinWidth(70);
        bottomRightBox.getChildren().add(topRightCombo);
        bottomRightBox.getChildren().add(bottomRightButton);
        bottomRightBox.setPadding(new Insets(5));
        bottomRightBox.setStyle("-fx-background-color:orange;");
        bottomRightBox.setAlignment(Pos.CENTER);
        //TableView
        TableColumn<TProduit, Long> idColumn = new TableColumn<>("Id");
        idColumn.setVisible(false);
        TableColumn<TProduit, String> designationColumn = new TableColumn<>("Désignation");
        designationColumn.setMaxWidth(75);
        designationColumn.setMinWidth(75);
        TableColumn<TProduit, Double> montantColumn = new TableColumn<>("Montant");
        montantColumn.setMaxWidth(73);
        montantColumn.setMinWidth(73);
        TableColumn<TProduit, Long> qteCommandeColumn = new TableColumn<>("Quantité");
        qteCommandeColumn.setMaxWidth(75);
        qteCommandeColumn.setMinWidth(75);
        TableColumn<TProduit, String> categorieColumn = new TableColumn<>("Catégorie");
        categorieColumn.setMaxWidth(75);
        categorieColumn.setMinWidth(75);

        table.getColumns().addAll(idColumn, designationColumn,montantColumn,qteCommandeColumn, categorieColumn);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        designationColumn.setCellValueFactory(new PropertyValueFactory<>("designation"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        qteCommandeColumn.setCellValueFactory(new PropertyValueFactory<>("qteCommande"));
        categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));

        HBox rightTopBox = new HBox();
        rightTopBox.getChildren().add(new Label("Panier"));
        rightTopBox.setAlignment(Pos.CENTER);
        rightTopBox.setPadding(new Insets(5));
        root.setRight(_controls.RightBox(new Node[]{rightTopBox,table,bottomRightBox},300));


        rBtn = btnDirection("right");
        lBtn = btnDirection("left");

        sp.setPannable(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setContent(listCategoryBox);
        sp.setMinWidth(1065);

        listCategoryBox.setSpacing(14);
        listCategoryBox.setPadding(new Insets(5));
        listCategoryBox.setBackground(new Background(new BackgroundFill(Color.rgb(13, 1, 82,0.2), CornerRadii.EMPTY, Insets.EMPTY)));
        listCategoryBox.setAlignment(Pos.CENTER);
        listCategoryBox.setMinWidth(1065);

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5));

        scrollPaneCenter.setPannable(true);
        scrollPaneCenter.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneCenter.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }
    public void setEvents(int nbr){
        rBtn.setOnAction(click -> {
            scroll("right",nbr);
        });
        lBtn.setOnAction(click -> {
            scroll("left",nbr);
        });
        bottomRightButton.setOnAction(click -> {
            double total = Double.parseDouble(bottomRightLabel.getText());
            if(total > 0){
                Commande cmd = new Commande();
                cmd.setType_de_paiement(topRightCombo.getValue().toString());
                cmd.setClient_id(client.getId());
                if(_commande.Create(cmd)){
                    List<TProduit> listPurchases = table.getItems();
                    for(TProduit prt : listPurchases){
                        Detail detail = new Detail(1,prt.getQteCommande(),prt.getId(),cmd.getId());
                        _detail.Create(detail);
                    }
                    table.getItems().clear();
                    _notify.Show("Information","Succès","Votre commande a été effectuée avec succès", Alert.AlertType.INFORMATION);
                    showProducts((Integer.parseInt( ((Label)topBox.getChildren().get(0)).getText().split("-")[0])) );
                    //window.close();
                }
                else _notify.Show("Erreur","Information","Une erreur esr survenue lors de la passation de votr commande !!!", Alert.AlertType.WARNING);
            }
            else _notify.Show("Information","Panier","Votre panier est vide !!!", Alert.AlertType.WARNING);
        });
    }
    public void Init(){
        List<Categorie> list = _categorie.Get();
        if(list != null){
            initElements();
            bottomBox.getChildren().add(lBtn);

            ((Label)topBox.getChildren().get(0)).setText(list.get(0).getId()+"-"+list.get(0).getLibelle());
            showProducts(list.get(0).getId());
            for(Categorie cat:list){
                Button btn = new Button(cat.getLibelle());
                btn.setGraphic(_func.getImage("categories/"+cat.getAvatar(),140,140));
                btn.getStyleClass().add("btn-category");
                btn.setContentDisplay(ContentDisplay.TOP);
                btn.setCursor(Cursor.HAND);
                btn.setOnAction(e -> {
                    ((Label)topBox.getChildren().get(0)).setText(cat.getId()+"-"+cat.getLibelle());
                    showProducts(cat.getId());
                });
                listCategoryBox.getChildren().add(btn);
            }
            bottomBox.getChildren().add(sp);
            bottomBox.getChildren().add(rBtn);

            root.setBottom(bottomBox);
            setEvents(list.size());
        }
        else root.setCenter(new Label("Pas d'offres pour le moment !!!"));
    }
    Button btnDirection(String type){
        Button btn = new Button();
        btn.setCursor(Cursor.HAND);
        btn.setStyle("-fx-background-radius: 0;");
        btn.setMinHeight(181);
        btn.setGraphic(_func.getImage(type+"_arrow",30,50));
        btn.setAlignment(Pos.CENTER);
        btn.setTextAlignment(TextAlignment.CENTER);
        return btn ;
    }
    void scroll(String type,int nbrElement){
        int nbrCat = 7 ;
        double uWidth = ((((listCategoryBox.getPrefWidth()/nbrElement)*nbrCat)*100)/listCategoryBox.getPrefWidth())/100;
        final Timeline timeline = new Timeline();
        final KeyValue kv = new KeyValue(sp.hvalueProperty(), (type.equals("right")?sp.hvalueProperty().getValue()+uWidth:sp.hvalueProperty().getValue()-uWidth), Interpolator.EASE_BOTH);
        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }
    int productIndex(long id){
        int index = -1 ;
        boolean exist = false;
        for(TProduit _tp:table.getItems()){
            index++;
            if(id == _tp.getId()){
                exist = true ;
                break ;
            }
        }
        return exist ? index:-1;
    }
    void updateProducts(CheckBox cb,_controls.NumberTextField nbr,long id){
        int index = productIndex(id);
        if(index > -1) {
            cb.setSelected(true);
            nbr.setNumber(BigDecimal.valueOf(table.getItems().get(index).getQteCommande()));
        }
    }
    void showProducts(long categorie_id){
        ProgressIndicator progressBar = new ProgressBar();
        root.setCenter(progressBar);
        List<Produit> list = _produit.Get(_db.Where("categorie_id = "+categorie_id,"produit","*"));
        if(list != null){
            Task<Boolean> sleeper = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    int cols = (int)Math.ceil(scrollPaneCenter.getWidth()/200),i=0,j=0;
                    grid= new GridPane();
                    grid.setAlignment(Pos.CENTER);
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.setPadding(new Insets(5));
                    boolean exist = false ;
                    for(Produit tpo:list){
                        if(tpo.getQuantite_disponible()>0){
                            exist = true ;
                            CheckBox cb = new CheckBox();

                            //Creation de l'objet TProduit
                            TProduit p = new TProduit();
                            p.setId(tpo.getId());
                            p.setDesignation(tpo.getDesignation());
                            p.setCategorie(_categorie.Get(tpo.getCategorie_id()).getLibelle());
                            p.setMontant(0);
                            p.setQteCommande(0);
                            //-----------------------------
                            //Btn du poduit
                            Button btn = new Button(p.getDesignation());
                            btn.setId("btn_"+Long.toString(p.getId()));
                            //btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            //-----------------------------
                            //Box qui contient checkbox et l'image de produit
                            VBox vc = new VBox();
                            vc.setAlignment(Pos.CENTER_RIGHT);
                            vc.getChildren().add(cb);


                            File df = new File(Path.of(URLDecoder.decode(Path.of("src/tmp/Images/products/"+tpo.getImg()).toAbsolutePath().toString(), StandardCharsets.UTF_8)).toUri());

                            vc.getChildren().add(_func.getImage("products/"+tpo.getImg(),120,120));
                            //-----------------------------
                            //Field qui contient la quantité
                            _controls.NumberTextField qteField = _controls.NumberField("qte_"+p.getId());
                            qteField.setNumber(BigDecimal.ONE);
                            qteField.textProperty().addListener((observable, oldValue, newValue) -> {
                                if(cb.isSelected()){
                                    if(_func.isLong(newValue) && Long.parseLong(newValue) <= tpo.getQuantite_disponible() && Long.parseLong(newValue) > 0){
                                        int index = productIndex(p.getId());
                                        if(index > -1) {
                                            bottomRightLabel.setText(Double.toString(Double.parseDouble(bottomRightLabel.getText())-table.getItems().get(index).getMontant()));
                                            TProduit mtp = new TProduit();
                                            mtp.setCategorie(p.getCategorie());
                                            mtp.setDesignation(p.getDesignation());
                                            mtp.setId(p.getId());
                                            mtp.setQteCommande(Long.parseLong(newValue));
                                            mtp.setMontant(tpo.getPrix()*Long.parseLong(newValue));
                                            //_func.Print(p.getDesignation());
                                            bottomRightLabel.setText(Double.toString(Double.parseDouble(bottomRightLabel.getText())+mtp.getMontant()));
                                            table.getItems().set(index,mtp);
                                        }
                                    }
                                }
                            });
                            qteField.prefWidthProperty().bind(btn.widthProperty());
                            //-----------------------------
                            //Selectionner les produit qui existent dans le panier
                            updateProducts(cb,qteField,p.getId());
                            //-----------------------------

                            btn.setGraphic(vc);
                            btn.getStyleClass().add("btn-product");
                            btn.setContentDisplay(ContentDisplay.TOP);
                            btn.setCursor(Cursor.HAND);
                            btn.setOnAction(click ->{
                                cb.setSelected(!cb.isSelected());
                                int index = productIndex(p.getId());
                                if(cb.isSelected() && index == -1){
                                    if(qteField.getNumber().doubleValue() <= 0 ||qteField.getNumber().doubleValue() > tpo.getQuantite_disponible()) cb.setSelected(false);
                                    else{
                                        p.setQteCommande(Long.parseLong(qteField.getNumber().toString()));
                                        p.setMontant(tpo.getPrix()*Long.parseLong(qteField.getNumber().toString()));
                                        bottomRightLabel.setText(Double.toString(Double.parseDouble(bottomRightLabel.getText()) + p.getMontant()));
                                        table.getItems().add(p);
                                    }
                                }
                                if(!cb.isSelected() && index > -1) {
                                    bottomRightLabel.setText(Double.toString(Double.parseDouble(bottomRightLabel.getText()) - table.getItems().get(index).getMontant()));
                                    table.getItems().remove(table.getItems().get(index));
                                }
                            });
                            btn.setMaxWidth(130);
                            btn.setTooltip(new Tooltip(p.designation));

                            Label infoLabel =new Label((long)(_produit.Sales(p.getId())/tpo.getPrix())+" vendus\n"+tpo.getQuantite_disponible()+" disponibles");
                            Label priceLabel =new Label(tpo.getPrix()+" MAD");
                            priceLabel.setTextAlignment(TextAlignment.CENTER);
                            priceLabel.setBackground(new Background(new BackgroundFill(Color.rgb(201, 178, 0,0.3), new CornerRadii(4), Insets.EMPTY)));
                            priceLabel.setAlignment(Pos.CENTER);
                            infoLabel.setPadding(new Insets(5));
                            infoLabel.setAlignment(Pos.CENTER);
                            infoLabel.setTextAlignment(TextAlignment.CENTER);
                            infoLabel.setFont(Font.font(11));
                            infoLabel.prefWidthProperty().bind(btn.widthProperty());
                            priceLabel.prefWidthProperty().bind(btn.widthProperty());

                            VBox productCard = new VBox();
                            productCard.getChildren().add(btn);
                            productCard.getChildren().add(qteField);
                            productCard.getChildren().add(infoLabel);
                            productCard.getChildren().add(priceLabel);
                            productCard.setStyle("-fx-padding: 10;" +
                                    "-fx-border-style: solid inside;" +
                                    "-fx-border-width: 1;" +
                                    "-fx-border-insets: 0;" +
                                    "-fx-border-radius: 10;" +
                                    "-fx-border-color: blue;" );
                            productCard.setBackground(new Background(new BackgroundFill(Color.rgb(81, 66, 245,0.15), new CornerRadii(10), Insets.EMPTY)));
                            productCard.setOnMouseEntered(e->{
                                productCard.setBackground(new Background(new BackgroundFill(Color.rgb(81, 66, 245,0.2), new CornerRadii(10), Insets.EMPTY)));
                            });
                            productCard.setOnMouseExited(e->{
                                productCard.setBackground(new Background(new BackgroundFill(Color.rgb(81, 66, 245,0.15), new CornerRadii(10), Insets.EMPTY)));
                            });
                            productCard.setOnMouseClicked(e ->{
                                if(e.getButton().name().equals("PRIMARY")){
                                    ProductDetail pd = new ProductDetail(window,p);
                                    pd.Init();
                                }
                            });
                            grid.add(productCard, j, i);
                            j++;
                            if(j==cols){
                                i++;
                                j=0;
                            }
                        }
                    }
                    return exist;
                }
            };
            sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    if(sleeper.getValue()){
                        scrollPaneCenter.setContent(grid);
                        root.setCenter(scrollPaneCenter);
                    }
                    else root.setCenter(new Label("Pas de produits"));
                }
            });
            new Thread(sleeper).start();
        }
        else root.setCenter(new Label("Pas de produits"));
    }
}
