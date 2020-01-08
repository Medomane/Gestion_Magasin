package View.Administrator;

import Core._db;
import Core._func;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Detail {
    public static class info{
        public String categorie;
        public String designation;
        public long quantite;
        public double sousTotal;
        public Date date;
        public String client;
        public String type_de_paiement;
        public info(String categorie,String designation,long quantite,double sousTotal,Date date,String client,String type_de_paiement){
            this.categorie = categorie;
            this.designation = designation;
            this.quantite = quantite;
            this.sousTotal = sousTotal;
            this.date = date ;
            this.client = client;
            this.type_de_paiement = type_de_paiement;
        }
        public static List<info> Get(){
            List<info> list = new ArrayList<>();
            ResultSet rs = _db.All("detail_v");
            try{
                while (rs.next()) list.add(new info(rs.getString("libelle"),rs.getString("designation"),rs.getLong("quantite"),rs.getDouble("prix")*rs.getLong("quantite"),rs.getDate("date"),rs.getString("prenom")+" "+rs.getString("nom"),rs.getString("type_de_paiement")));
                if(list.size() > 0) return list ;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            return null ;
        }
        public static List<info> Get(long id){
            List<info> list = new ArrayList<>();
            ResultSet rs = _db.Where("client_id = "+id,"detail_v","*");
            try{
                while (rs.next()) list.add(new info(rs.getString("libelle"),rs.getString("designation"),rs.getLong("quantite"),rs.getDouble("prix")*rs.getLong("quantite"),rs.getDate("date"),rs.getString("prenom")+" "+rs.getString("nom"),rs.getString("type_de_paiement")));
                if(list.size() > 0) return list ;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            return null ;
        }
        public static List<info> Get(String req,long id){
            List<info> list = new ArrayList<>();
            req = _db.Search_Str(req);
            ResultSet rs = _db.Get("SELECT * FROM detail_v WHERE client_id = "+id+" AND (libelle like "+req+" OR designation like "+req+" OR quantite like "+req+" OR date like "+req+" OR type_de_paiement like "+req+")");
            //_func.Print("SELECT * FROM detail_v WHERE client_id = "+id+" AND (libelle like "+req+" OR designation like "+req+" OR quantite like "+req+" OR date like "+req+" OR type_de_paiement like "+req+")");
            try{
                while (rs.next()) list.add(new info(rs.getString("libelle"),rs.getString("designation"),rs.getLong("quantite"),rs.getDouble("prix")*rs.getLong("quantite"),rs.getDate("date"),rs.getString("prenom")+" "+rs.getString("nom"),rs.getString("type_de_paiement")));
                if(list.size() > 0) return list ;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            return null ;
        }

        public String getCategorie() {
            return categorie;
        }

        public void setCategorie(String categorie) {
            this.categorie = categorie;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public long getQuantite() {
            return quantite;
        }

        public void setQuantite(long quantite) {
            this.quantite = quantite;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public String getType_de_paiement() {
            return type_de_paiement;
        }

        public void setType_de_paiement(String type_de_paiement) {
            this.type_de_paiement = type_de_paiement;
        }

        public double getSousTotal() {
            return sousTotal;
        }

        public void setSousTotal(double sousTotal) {
            this.sousTotal = sousTotal;
        }
    }

    private BorderPane root ;
    public Tab tab ;
    TableView<info> table;
    Label lblInfo ;
    Button refresh ;
    public Detail(){
        root = new BorderPane();
        lblInfo = new Label();
        lblInfo.setTextAlignment(TextAlignment.CENTER);
        lblInfo.setAlignment(Pos.CENTER_LEFT);

        HBox topBox = new HBox();
        refresh = new Button("Actualiser");
        refresh.setGraphic(_func.getImage("refresh",17,17));
        refresh.setCursor(Cursor.HAND);

        refresh.setOnAction(click ->{
            Refresh();
        });
        HBox topRightBox = new HBox();
        topRightBox.setMinWidth(100);
        topRightBox.setAlignment(Pos.CENTER_RIGHT);
        topRightBox.getChildren().add(refresh);

        HBox topLeftBox = new HBox();
        topLeftBox.setMinWidth(700);
        topLeftBox.setAlignment(Pos.CENTER_LEFT);
        topLeftBox.getChildren().add(lblInfo);
        topLeftBox.setPadding(new Insets(0,0,0,5));

        topBox.getChildren().addAll(topLeftBox,topRightBox);
        topBox.setAlignment(Pos.CENTER_RIGHT);
        topBox.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));

        tab = new Tab("Détail des commandes",root);

        table = new TableView<>();
        table.setMinWidth(800);
        double width = (table.getMinWidth()/7)-1;
        TableColumn<info,String> categorieColumn = new TableColumn<>("Catégorie");
        categorieColumn.setMinWidth(width);
        categorieColumn.setMaxWidth(width);
        TableColumn<info,String> designationColumn = new TableColumn<>("Désignation");
        designationColumn.setMinWidth(width);
        designationColumn.setMaxWidth(width);
        TableColumn<info,Long> qteColumn = new TableColumn<>("Quantité");
        qteColumn.setMinWidth(width);
        qteColumn.setMaxWidth(width);
        TableColumn<info,Long> stColumn = new TableColumn<>("Sous total");
        stColumn.setMinWidth(width);
        stColumn.setMaxWidth(width);
        TableColumn<info,Date> dateColumn = new TableColumn<>("Date");
        dateColumn.setMinWidth(width);
        dateColumn.setMaxWidth(width);
        TableColumn<info,String> userColumn = new TableColumn<>("Client");
        userColumn.setMinWidth(width);
        userColumn.setMaxWidth(width);
        TableColumn<info,String> typeColumn = new TableColumn<>("Type de paiement");
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

        root.setTop(topBox);
        root.setCenter(table);
    }
    void Refresh(){
        refresh.setDisable(true);
        table.getItems().clear();
        var rotate = _func.Rotate(refresh.getGraphic());
        Task<String> sleeper = new Task<String>() {
            @Override
            protected String call() throws Exception {
                Thread.sleep(400);

                List<info> t = info.Get();
                double total = 0;
                int nbr = 0;
                if (t != null){
                    table.getItems().addAll(t);
                    for(info tmp:t){
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
}
