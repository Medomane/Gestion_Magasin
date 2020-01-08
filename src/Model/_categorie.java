package Model;

import Controller.Classes.Categorie;
import Core.DAO;
import Core._db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class _categorie implements DAO<Categorie> {
    @Override
    public boolean create(Categorie p) {
        String sql = "INSERT INTO categorie (libelle,avatar) VALUES ("+ _db.Str(p.getLibelle()) +","+ _db.Str(p.getAvatar()) +")";
        try{
            long inserted_id = _db.Exec(sql);
            if(inserted_id > 0){
                p.setId(inserted_id);
                return true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public Categorie get(long id) {
        var list = get(_db.Where("id = "+id,"categorie","*"));
        if(list != null) return list.get(0);
        return null;
    }
    @Override
    public boolean delete(long id) {
        return _db.Exec("DELETE FROM categorie WHERE id = "+id) > 0;
    }
    @Override
    public boolean update(Categorie p) {
        return (_db.Exec("UPDATE categorie SET libelle = "+ _db.Str(p.getLibelle()) +",avatar = "+ _db.Str(p.getAvatar()) +" where id = "+ p.getId()) > 0) ;
    }
    @Override
    public List<Categorie> get() {
        return get(_db.All("categorie"));
    }
    @Override
    public List<Categorie> get(String str) {
        return get(_db.Where("libelle like "+_db.Search_Str(str),"categorie","*"));
    }
    @Override
    public List<Categorie> get(ResultSet rs) {
        List<Categorie> list = new ArrayList<>();
        try {
            while (rs.next()) list.add(new Categorie(rs.getLong("id"),rs.getString("libelle"),rs.getString("avatar")));
            if(list.size() > 0) return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }
}
