package Controller;


import Controller.Classes.Commande;

import java.sql.ResultSet;
import java.util.List;

public class _commande {
    public static Commande Get(long id){
        return new Model._commande().get(id);
    }
    public static boolean Create(Commande p) {
        return new Model._commande().create(p);
    }
    public static boolean Delete(long id) {
        return new Model._commande().delete(id);
    }
    public static boolean Update(Commande p) {
        return new Model._commande().update(p);
    }
    public static List<Commande> Get() {
        return new Model._commande().get();
    }
    public static List<Commande> Get(String str) {
        return new Model._commande().get(str);
    }
    public static List<Commande> Get(ResultSet rs) {
        return new Model._commande().get(rs);
    }
}
