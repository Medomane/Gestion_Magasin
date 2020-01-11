package Core;

import java.sql.ResultSet;
import java.util.List;

public interface DAO<T> {
    boolean create(T p);
    T get(long id);
    boolean delete(long id);
    boolean update(T p);
    List<T> get();
    List<T> get(String des);
    List<T> get(ResultSet rs);
}