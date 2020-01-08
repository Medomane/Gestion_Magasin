package Controller;


import Controller.Classes.Produit;
import Core._db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class _produit {
    public static Produit Get(long id){
        return new Model._produit().get(id);
    }
    public static boolean Create(Produit p) {
        return new Model._produit().create(p);
    }
    public static boolean Delete(long id) {
        return new Model._produit().delete(id);
    }
    public static boolean Update(Produit p) {
        return new Model._produit().update(p);
    }
    public static List<Produit> Get() {
        return new Model._produit().get();
    }
    public static List<Produit> Get(String str) {
        return new Model._produit().get(str);
    }
    public static List<Produit> Get(ResultSet rs) {
        return new Model._produit().get(rs);
    }
    public static double Sales(long id){
        double somme = 0 ;
        try{
            ResultSet rs = _db.Where("produit_id = "+id,"detail_v","*");
            while (rs.next()) somme += rs.getLong("quantite") * rs.getDouble("prix");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return somme ;
    }
}
