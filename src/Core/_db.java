package Core;

import Config.Conf;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class _db {
    public static Connection GetConnection(){
        try{
            Class.forName (Conf.Driver);
            String url = "jdbc:mysql://"+Conf.Host+"/"+Conf.DbName;
            return (Connection) DriverManager.getConnection(url, Conf.Auth[0], Conf.Auth[1]);
        }
        catch (Exception exp) {
            exp.printStackTrace();
        }
        return null ;
    }
    public static ResultSet Get(String sql) {
        try {
            Statement statement = (Statement) Objects.requireNonNull(GetConnection()).createStatement();
            return (ResultSet)statement.executeQuery(sql) ;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultSet All(String tbl) {
        return Get("SELECT * FROM "+tbl);
    }

    public static ResultSet Where(String cond,String tbl,String fields) {
        return Get("SELECT "+fields+" FROM "+tbl+" WHERE "+cond);
    }

    public static long Exec(String sql) {
        try {
            if(sql.toLowerCase().contains("insert")){
                PreparedStatement ps = Objects.requireNonNull(GetConnection()).prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                if(ps.executeUpdate() > 0) {
                    ResultSet rs = ps.getGeneratedKeys();
                    int generatedKey = 0;
                    if (rs.next()) generatedKey = rs.getInt(1);
                    return generatedKey;
                }
            }
            else {
                return ((Statement)(Objects.requireNonNull(GetConnection()).createStatement())).execute(sql) ?1:0;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static int Count(ResultSet rs) {
        try{
            int c = 0;
            while (rs.next()) c++;
            return c ;
        }
        catch (SQLException e){return 0;}
    }
    public static String Str(String str){
        return "'"+str.replace("'","''")+"'";
    }
    public static String Search_Str(String str){
        return "'%"+str.replace("'","''")+"%'";
    }

}
