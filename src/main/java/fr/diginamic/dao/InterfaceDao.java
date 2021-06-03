package fr.diginamic.dao;

import java.util.List;

public interface InterfaceDao<T> {
    List<T> select() throws DaoException;

    T selectById(long id) throws DaoException;

    T insert(T object) throws DaoException;

    T update(T object) throws DaoException;

    boolean delete(T object) throws DaoException;
}
