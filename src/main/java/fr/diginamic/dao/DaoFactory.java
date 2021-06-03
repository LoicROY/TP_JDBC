package fr.diginamic.dao;

public final class DaoFactory {

    private DaoFactory() {
    }

    public static ArticleDao getArticleDaoJdbc(){
        return new ArticleDaoJdbc();
    }

    public static FournisseurDao getFournisseurDaoJdbc(){
        return new FournisseurDaoJdbc();
    }
}
