package Controller.Classes;

public class Produit {
    private long id;
    private String designation;
    private double prix;
    private long quantite_disponible ;
    private String img ;
    private long categorie_id;

    public Produit(long id, String designation, double prix,long quantite_disponible,String img,long categorie_id) {
        this.id = id;
        this.designation = designation;
        this.prix = prix;
        this.quantite_disponible = quantite_disponible;
        this.categorie_id = categorie_id;
        this.img = img;
    }

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

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public long getCategorie_id() {
        return categorie_id;
    }

    public void setCategorie_id(long categorie_id) {
        this.categorie_id = categorie_id;
    }

    public long getQuantite_disponible() {
        return quantite_disponible;
    }

    public void setQuantite_disponible(long quantite_disponible) {
        this.quantite_disponible = quantite_disponible;
    }

    @Override
    public String toString(){
        return "Désignation : "+getDesignation()+", Catégorie : "+getCategorie_id();
    }
}
