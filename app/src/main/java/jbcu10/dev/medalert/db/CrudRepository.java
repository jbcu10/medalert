package jbcu10.dev.medalert.db;

import java.util.List;

/**
 * Created by dev on 10/1/17.
 */

public interface CrudRepository<T>  {

    List<T>getAll();
    T  getById(int id);
    T  getByUuid(String uuid);
    boolean create(T t);
    boolean update(T t);
    boolean deleteById(int id);
}
