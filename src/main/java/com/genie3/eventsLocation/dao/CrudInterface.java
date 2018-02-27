package com.genie3.eventsLocation.dao;

import java.util.List;
import java.util.Map;

public interface CrudInterface<T> {

    T create(T t);
    List<T> read(String[] fields, String whereClause,String orderBy,Integer limit);
    T get(int id);
    T update(T t);
    Boolean delete(int id);
}
