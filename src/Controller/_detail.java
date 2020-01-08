package Controller;


import Controller.Classes.Detail;

import java.sql.ResultSet;
import java.util.List;

public class _detail {
    public static Detail Get(long id){
        return new Model._detail().get(id);
    }
    public static boolean Create(Detail p) {
        return new Model._detail().create(p);
    }
    public static boolean Delete(long id) {
        return new Model._detail().delete(id);
    }
    public static boolean Update(Detail p) {
        return new Model._detail().update(p);
    }
    public static List<Detail> Get() {
        return new Model._detail().get();
    }
    public static List<Detail> Get(String str) {
        return new Model._detail().get(str);
    }
    public static List<Detail> Get(ResultSet rs) {
        return new Model._detail().get(rs);
    }
}
