package Controller.Classes;

public class Detail {
    private long id ;
    private long quantite;
    private long produit_id;
    private long commande_id ;
    public Detail(long id,long quantite,long produit_id,long commande_id){
        this.id = id ;
        this.quantite = quantite;
        this.produit_id = produit_id;
        this.commande_id = commande_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuantite() {
        return quantite;
    }

    public void setQuantite(long quantite) {
        this.quantite = quantite;
    }

    public long getProduit_id() {
        return produit_id;
    }

    public void setProduit_id(long produit_id) {
        this.produit_id = produit_id;
    }

    public long getCommande_id() {
        return commande_id;
    }

    public void setCommande_id(long commande_id) {
        this.commande_id = commande_id;
    }
}
