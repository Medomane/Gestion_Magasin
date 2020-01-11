package Controller;

import Controller.Classes.Categorie;
import Controller.Classes.Produit;
import Core._db;
import Core._func;
import Core._notify;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class _categorie {
    public static Categorie Get(long id){
        return new Model._categorie().get(id);
    }
    public static boolean Create(Categorie p) {
        return new Model._categorie().create(p);
    }
    public static boolean Delete(long id) {
        return new Model._categorie().delete(id);
    }
    public static boolean Update(Categorie p) {
        return new Model._categorie().update(p);
    }
    public static List<Categorie> Get() {
        return new Model._categorie().get();
    }
    public static List<Categorie> Get(String str) {
        return new Model._categorie().get(str);
    }
    public static List<Categorie> Get(ResultSet rs) {
        return new Model._categorie().get(rs);
    }
    public static double[] Sales(long id){
        double[] info = new double[2];
        double somme = 0,nbr = 0;
        try {
            ResultSet rs = _db.Where("categorie_id = "+id,"detail_v","*");
            while (rs.next()){
                nbr += rs.getLong("quantite");
                somme += rs.getDouble("prix") * rs.getLong("quantite");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        info[0] = somme;
        info[1] = nbr ;
        return info;
    }
    public static List<Produit> Products(long id){
        return _produit.Get(_db.Where("categorie_id = "+id,"produit","*"));
    }
    public static boolean isVerified(long id, TextField libelle){
        libelle.getStyleClass().removeAll("text-error");
        if(_func.isNull(libelle.getText())){
            libelle.requestFocus();
            libelle.getStyleClass().add("text-error");
            return false ;
        }
        else if(_categorie.Get(_db.Where("libelle = "+_db.Str(libelle.getText()) + ((id <=0) ?"":" AND id <> "+id),"categorie","*")) != null){
            _notify.Show("Erreur","Erreur  d'information","Catégorie déjà existe !!!", Alert.AlertType.ERROR);
            return false ;
        }
        return true ;
    }
}
