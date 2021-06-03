package fr.diginamic;

import fr.diginamic.bo.Article;
import fr.diginamic.bo.Fournisseur;
import fr.diginamic.dao.*;

public class Main {
    public static void main(String[] args) {
        FournisseurDao fournisseurDao = DaoFactory.getFournisseurDaoJdbc();
        ArticleDao articleDao = DaoFactory.getArticleDaoJdbc();

        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setNom("La Maison de la Peinture");

        Article article = new Article();
        article.setRef("tp06");
        article.setDesignation("Peinture blanche 1L");
        article.setPrix(12.5f);
        article.setFournisseur(fournisseur);

        Article article2 = new Article();
        article2.setRef("tp06");
        article2.setDesignation("Peinture rouge mate 1L");
        article2.setPrix(15.5f);
        article2.setFournisseur(fournisseur);

        Article article3 = new Article();
        article3.setRef("tp06");
        article3.setDesignation("Peinture noire laquée 1L");
        article3.setPrix(17.8f);
        article3.setFournisseur(fournisseur);

        Article article4 = new Article();
        article4.setRef("tp06");
        article4.setDesignation("Peinture bleue mate 1L");
        article4.setPrix(15.5f);
        article4.setFournisseur(fournisseur);

        try {
            fournisseurDao.insert(fournisseur);

            articleDao.insert(article);
            articleDao.insert(article2);
            articleDao.insert(article3);
            articleDao.insert(article4);

            articleDao.updatePrixWhereDesignationContain("mate");

        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
}
