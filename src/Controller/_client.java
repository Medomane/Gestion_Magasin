package Controller;

import Controller.Classes.Client;
import Core._db;
import Core._func;
import Core._notify;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class _client {
    public static Client Get(long id){
        return new Model._client().get(id);
    }
    public static boolean Create(Client p) {
        return new Model._client().create(p);
    }
    public static boolean Delete(long id) {
        return new Model._client().delete(id);
    }
    public static boolean Update(Client p) {
        return new Model._client().update(p);
    }
    public static List<Client> Get() {
        return new Model._client().get();
    }
    public static List<Client> Get(String str) {
        return new Model._client().get(str);
    }
    public static List<Client> Get(ResultSet rs) {
        return new Model._client().get(rs);
    }
    public static double[] purchases(long id){
        double[] info = new double[2];
        double somme = 0,nbr = 0;
        try {
            ResultSet rs = _db.Where("client_id = "+id,"detail_v","*");
            while (rs.next()){
                nbr += 1;
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
    public static boolean isVerified(long id, TextField nomTextField, TextField prenomTextField, TextField telephoneTextField, TextField emailTextField, TextField mot_de_passeTextField){
        nomTextField.getStyleClass().removeAll("text-error");
        prenomTextField.getStyleClass().removeAll("text-error");
        telephoneTextField.getStyleClass().removeAll("text-error");
        emailTextField.getStyleClass().removeAll("text-error");
        mot_de_passeTextField.getStyleClass().removeAll("text-error");
        if(_func.isNull(nomTextField.getText())){
            nomTextField.requestFocus();
            nomTextField.getStyleClass().add("text-error");
            return false ;
        }
        else if(_func.isNull(prenomTextField.getText())){
            prenomTextField.requestFocus();
            prenomTextField.getStyleClass().add("text-error");
            return false ;
        }
        else if(!_func.isValid("phone",telephoneTextField.getText())){
            telephoneTextField.requestFocus();
            telephoneTextField.getStyleClass().add("text-error");
            return false ;
        }
        else if(!_func.isValid("email",emailTextField.getText())){
            emailTextField.requestFocus();
            emailTextField.getStyleClass().add("text-error");
            return false ;
        }
        else if(_func.isNull(mot_de_passeTextField.getText())){
            mot_de_passeTextField.requestFocus();
            mot_de_passeTextField.getStyleClass().add("text-error");
            return false ;
        }
        else if(_client.Get(_db.Get("SELECT * FROM client WHERE email = "+_db.Str(emailTextField.getText()) + ((id == -1)?"":" AND id <> "+id))) != null){
            _notify.Show("Erreur","email","Email déjà existe !!!", Alert.AlertType.ERROR);
            return false ;
        }
        return true ;
    }
}
