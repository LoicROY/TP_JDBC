package fr.diginamic;

import fr.diginamic.bo.Article;
import fr.diginamic.bo.Fournisseur;
import fr.diginamic.dao.*;

import java.util.List;

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
            //Insere dans la BDD un nouveau Fournisseur
            fournisseurDao.insert(fournisseur);

            //Insere dans la BDD 4 nouveau Articles
            articleDao.insert(article);
            articleDao.insert(article2);
            articleDao.insert(article3);
            articleDao.insert(article4);

            //Modifie les prix des peintures mate de -25%
            articleDao.updatePrixWhereDesignationContain("mate");

            //Affiche tous les articles
            articleDao.select();

            //Utilisez la DAO pour exécuter une requête qui extrait la moyenne des prix des
            //articles et affiche cette moyenne. Attention la moyenne est effectuée par la base et
            //non en Java !!!
            System.out.println(articleDao.getAveragePrice());

            //supprimer tous les articles dont le nom contient « Peinture » de la base de
            //données.
            List<Article> articles = articleDao.selectByDesignation("Peinture");
            for (Article a: articles) {
                articleDao.delete(a);
            }

            //supprimer le fournisseur « La Maison de la Peinture »
            //(Je part du principe que le nom fournisseur est unique)
            Fournisseur fournisseurADelete = fournisseurDao.selectByExactDesignation("La maison de la Peinture");
            fournisseurDao.delete(fournisseurADelete);

        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
}
