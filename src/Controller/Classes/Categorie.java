package Controller.Classes;

public class Categorie {
    private long id;
    private String libelle;
    private String avatar ;

    public Categorie(long id, String libelle,String avatar) {
        this.id = id;
        this.libelle = libelle;
        this.avatar = avatar;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    @Override
    public String toString() {
        return libelle;
    }
}
