package Model;

import Controller.Classes.Client;
import Core.DAO;
import Core._db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class _client implements DAO<Client> {
    @Override
    public Client get(long id) {
        var list = get( _db.Where("id = "+id,"client","*"));
        if(list != null) return list.get(0);
        return null;
    }
    @Override
    public boolean delete(long id){
        return _db.Exec("DELETE FROM client where id = "+id) > 0;
    }
    @Override
    public boolean create(Client obj) {
        long inserted_id = _db.Exec("INSERT INTO client(nom,prenom,telephone,email,mot_de_passe) values("+ _db.Str(obj.getNom())+","+ _db.Str(obj.getPrenom())+","+_db.Str(obj.getTelephone())+","+_db.Str(obj.getEmail())+","+_db.Str(obj.getMot_de_passe())+")");
        if(inserted_id > 0){
            obj.setId(inserted_id);
            return true;
        }
        return false;
    }
    @Override
    public boolean update(Client obj) {
        return (_db.Exec("update client set nom = "+_db.Str(obj.getNom())+", prenom = "+_db.Str(obj.getPrenom())+",telephone= "+_db.Str(obj.getTelephone())+",email= "+_db.Str(obj.getEmail())+",mot_de_passe = "+_db.Str(obj.getMot_de_passe())+" where id = "+obj.getId()) > 0);
    }
    @Override
    public List<Client> get() {
        return get(_db.All("client"));
    }
    @Override
    public List<Client> get(String key) {
        key = _db.Search_Str(key);
        return get( _db.Where("telephone LIKE "+key+" OR id like "+key+" OR nom like "+key+" OR prenom like "+key+" OR email like "+key,"client","*"));
    }
    @Override
    public List<Client> get(ResultSet rs) {
        List<Client> list = new ArrayList<>();
        try {
            while (rs.next()) list.add(new Client(rs.getLong("id"),rs.getString("nom"),rs.getString("prenom"),rs.getString("telephone"),rs.getString("email"),rs.getString("mot_de_passe")));
            if(list.size() > 0) return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }
}
