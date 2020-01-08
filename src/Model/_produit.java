package Model;

import Controller.Classes.Produit;
import Core.DAO;
import Core._db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class _produit implements DAO<Produit> {
    @Override
    public Produit get(long id) {
        var list = get(_db.Where("id = "+id,"produit_v","*"));
        if(list != null) return list.get(0);
        return null;
    }
    @Override
    public boolean delete(long id){
        return _db.Exec("DELETE FROM produit where id = "+id) > 0 ;
    }
    @Override
    public boolean create(Produit prod) {
        long inserted_id = _db.Exec("INSERT INTO produit(designation,prix,quantite_disponible,img,categorie_id) values("+ _db.Str(prod.getDesignation())+","+prod.getPrix()+","+prod.getQuantite_disponible()+",'"+prod.getImg()+"',"+prod.getCategorie_id()+")");
        if(inserted_id > 0){
            prod.setId(inserted_id);
            return true;
        }
        return false;
    }
    @Override
    public boolean update(Produit prod) {
        return _db.Exec("update produit set designation = "+ _db.Str(prod.getDesignation())+", prix = "+prod.getPrix()+", quantite_disponible = "+prod.getQuantite_disponible()+", img = '"+prod.getImg()+"', categorie_id = "+prod.getCategorie_id()+" where id = "+prod.getId()) > 0;
    }
    @Override
    public List<Produit> get() {
        return get(_db.All("produit_v"));
    }
    @Override
    public List<Produit> get(String key) {
        key = _db.Search_Str(key);
        return get(_db.Where("designation LIKE "+key+" OR libelle like " + key + " OR prix like "+key,"produit_v","*"));
    }
    @Override
    public List<Produit> get(ResultSet rs) {
        List<Produit> list = new ArrayList<>();
        try {
            while (rs.next()) list.add(new Produit(rs.getLong("id"),rs.getString("designation"),rs.getDouble("prix"),rs.getLong("quantite_disponible"),rs.getString("img"),rs.getLong("categorie_id")));
            if(list.size() > 0) return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }
}
