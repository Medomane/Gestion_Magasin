package Controller.Classes;

import java.util.Date;

public class Commande {
    private long id ;
    private Date date ;
    private String type_de_paiement;
    private long client_id ;

    public Commande(){

    }

    public Commande(long id, Date date,String type_de_paiement,long client_id){
        this.id = id ;
        this.date = date ;
        this.type_de_paiement = type_de_paiement;
        this.client_id = client_id ;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getClient_id() {
        return client_id;
    }

    public void setClient_id(long client_id) {
        this.client_id = client_id;
    }

    public String getType_de_paiement() {
        return type_de_paiement;
    }

    public void setType_de_paiement(String type_de_paiement) {
        this.type_de_paiement = type_de_paiement;
    }
}
