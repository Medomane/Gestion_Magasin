package Model;

import Controller.Classes.Detail;
import Core.DAO;
import Core._db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class _detail implements DAO<Detail> {
    @Override
    public boolean create(Detail d) {
        long inserted_id = _db.Exec("INSERT INTO detail(quantite,produit_id,commande_id) values("+ d.getQuantite()+","+d.getProduit_id()+","+d.getCommande_id()+")");
        if(inserted_id > 0){
            d.setId(inserted_id);
            return true;
        }
        return false;
    }
    @Override
    public Detail get(long id) {
        var list = get(_db.Where("id = "+id,"detail_v","*"));
        if(list != null) return list.get(0);
        return null;
    }
    @Override
    public boolean delete(long id) {
        return _db.Exec("DELETE FROM detail where id = "+id) > 0 ;
    }
    @Override
    public boolean update(Detail d) {
        return true;
    }
    @Override
    public List<Detail> get() {
        return get(_db.All("detail_v"));
    }
    @Override
    public List<Detail> get(String des) {
        return null;
    }
    @Override
    public List<Detail> get(ResultSet rs) {
        List<Detail> list = new ArrayList<>();
        try {
            while (rs.next()) list.add(new Detail(rs.getLong("id"),rs.getLong("quantite"),rs.getLong("produit_id"),rs.getLong("commande_id")));
            if(list.size() > 0) return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }
}
