package fr.diginamic.dao;

import fr.diginamic.bo.Article;

import java.util.List;

public interface ArticleDao extends InterfaceDao<Article> {

    void updatePrixWhereDesignationContain(String string) throws DaoException;
    float getAveragePrice() throws DaoException;
}
