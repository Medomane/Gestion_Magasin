package Core;

import java.sql.ResultSet;
import java.util.List;

public interface DAO<T> {
    public boolean create(T p);
    public T get(long id);
    public boolean delete(long id);
    public boolean update(T p);
    public List<T> get();
    public List<T> get(String des);
    public List<T> get(ResultSet rs);
}