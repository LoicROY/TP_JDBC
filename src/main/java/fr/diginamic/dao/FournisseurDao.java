package fr.diginamic.dao;

import fr.diginamic.bo.Fournisseur;

public interface FournisseurDao extends InterfaceDao<Fournisseur> {

    public Fournisseur selectByExactDesignation(String string) throws DaoException;
}
