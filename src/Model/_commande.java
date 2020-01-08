package Model;

import Controller.Classes.Commande;
import Core.DAO;
import Core._db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class _commande implements DAO<Commande> {
    @Override
    public Commande get(long id) {
        var list = get(_db.Where("id = "+id,"commande_v","*"));
        if(list != null) return list.get(0);
        return null;
    }
    @Override
    public boolean delete(long id){
        return _db.Exec("DELETE FROM commande where id = "+id) > 0 ;
    }
    @Override
    public boolean create(Commande cmd) {
        long inserted_id = _db.Exec("INSERT INTO commande(type_de_paiement,client_id) values("+_db.Str(cmd.getType_de_paiement())+","+cmd.getClient_id()+")");
        if(inserted_id > 0){
            cmd.setId(inserted_id);
            return true;
        }
        return false;
    }
    @Override
    public boolean update(Commande cmd) {
        return _db.Exec("update commande set type_de_paiement = "+_db.Str(cmd.getType_de_paiement())+" WHERE id = "+cmd.getId()) > 0;
    }
    @Override
    public List<Commande> get() {
        return get( _db.All("commande_v"));
    }
    @Override
    public List<Commande> get(String key) {
        key = _db.Search_Str(key);
        return get(_db.Where("telephone LIKE "+key+" OR id like "+key+" OR type_de_paiement like "+key+" OR nom like "+key+" OR date like "+key+" OR prenom like "+key+" OR email like "+key,"commande_v","*"));
    }
    @Override
    public List<Commande> get(ResultSet rs) {
        List<Commande> list = new ArrayList<>();
        try {
            while (rs.next()) list.add(new Commande(rs.getLong("id"),rs.getDate("date"),rs.getString("type_de_paiement"),rs.getLong("client_id")));
            if(list.size() > 0) return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }
}
