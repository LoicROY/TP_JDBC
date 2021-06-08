package fr.diginamic.dao;

import fr.diginamic.bo.Article;
import fr.diginamic.bo.Fournisseur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ArticleDaoJdbc implements ArticleDao {

    private static final String ID_COL = "ID";
    private static final String REF_COL = "REF";
    private static final String DESIGNATION_COL = "DESIGNATION";
    private static final String PRIX_COL = "PRIX";
    private static final String ID_FOURNISSEUR_COL = "ID_FOU";
    private static final String ALIAS_AVG_PRICE = "AVERAGE_PRICE";

    private static final String TABLE_NAME = "article";

    private static final String SELECT = String.format(
            "SELECT * FROM %s;", TABLE_NAME);

    private static final String SELECT_BY_ID = String.format(
            "SELECT * FROM %s WHERE %s=?;", TABLE_NAME, ID_COL);

    private static final String INSERT = String.format(
            "INSERT INTO %s VALUES (null, ?,?,?,?);", TABLE_NAME);

    private static final String UPDATE = String.format(
            "UPDATE %s SET %s=?, %s=?, %s=?, %s=? WHERE %s=?;",
            TABLE_NAME, REF_COL, DESIGNATION_COL, PRIX_COL, ID_FOURNISSEUR_COL, ID_COL);

    private static final String DELETE = String.format(
            "DELETE FROM %s WHERE id=?;", TABLE_NAME);

    private static final String SELECT_BY_DESIGNATION = String.format(
            "SELECT * FROM %s WHERE %s LIKE ?;", TABLE_NAME, DESIGNATION_COL);

    private static final String AVG_PRICE = String.format(
            "SELECT AVG(%s) %s FROM %s;", PRIX_COL, ALIAS_AVG_PRICE, TABLE_NAME);

    private FournisseurDao fournisseurDao = DaoFactory.getFournisseurDaoJdbc();

    @Override
    public List<Article> select() throws DaoException {
        Connection connection = ConnectionService.getInstance().getConnection();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SELECT);

            List<Article> articles = new ArrayList<>();
            while (rs.next()) {
                articles.add(new Article(
                        rs.getInt(ID_COL),
                        rs.getString(REF_COL),
                        rs.getString(DESIGNATION_COL),
                        rs.getFloat(PRIX_COL),
                        fournisseurDao.selectById(rs.getLong(ID_FOURNISSEUR_COL))
                ));
            }
            return articles;

        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Article selectById(long id) throws DaoException {
        Connection connection = ConnectionService.getInstance().getConnection();
        try (PreparedStatement pst = connection.prepareStatement(SELECT_BY_ID)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();;

            Article article = new Article();
            while (rs.next()) {
                article.setId(rs.getInt(ID_COL));
                article.setRef(rs.getString(REF_COL));
                article.setDesignation(rs.getString(DESIGNATION_COL));
                article.setPrix(rs.getFloat(PRIX_COL));
                article.setFournisseur(fournisseurDao.selectById(rs.getLong(ID_FOURNISSEUR_COL)));
            }
            return article;

        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Article insert(Article article) throws DaoException {
        Connection connection = ConnectionService.getInstance().getConnection();
        try (PreparedStatement pst = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, article.getRef());
            pst.setString(2, article.getDesignation());
            pst.setFloat(3, article.getPrix());
            pst.setLong(4, article.getFournisseur().getId());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                article.setId(rs.getInt(1));
            }
            return article;

        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Article update(Article article) throws DaoException {
        Connection connection = ConnectionService.getInstance().getConnection();
        try (PreparedStatement pst = connection.prepareStatement(UPDATE)) {
            pst.setString(1, article.getRef());
            pst.setString(2, article.getDesignation());
            System.out.println(article.getPrix());
            pst.setFloat(3, article.getPrix());
            pst.setLong(4, article.getFournisseur().getId());
            pst.setLong(5, article.getId());
            pst.executeUpdate();

            return article;

        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public boolean delete(Article article) throws DaoException {
        Connection connection = ConnectionService.getInstance().getConnection();
        try (PreparedStatement pst = connection.prepareStatement(DELETE)) {
            pst.setLong(1, article.getId());
            pst.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void updatePrixWhereDesignationContain(String string) throws DaoException {
        //diminuer prix de 25% pour les designation qui contiennent "mates"
        List<Article> articlesToUpdate = this.selectByDesignation(string);
        for (Article article : articlesToUpdate) {
            System.out.println(article.getPrix());
            System.out.println(article.getPrix() - article.getPrix() * 25/100);

            article.setPrix(article.getPrix() - article.getPrix() * 25/100);
            this.update(article);
        }
    }

    @Override
    public List<Article> selectByDesignation(String string) throws DaoException {
        Connection connection = ConnectionService.getInstance().getConnection();
        try (PreparedStatement pst = connection.prepareStatement(SELECT_BY_DESIGNATION)) {
            pst.setString(1, "%" + string + "%");
            ResultSet rs = pst.executeQuery();;

            List<Article> articles = new ArrayList<>();

            while (rs.next()) {
                Article article = new Article();
                article.setId(rs.getInt(ID_COL));
                article.setRef(rs.getString(REF_COL));
                article.setDesignation(rs.getString(DESIGNATION_COL));
                article.setPrix(rs.getFloat(PRIX_COL));
                article.setFournisseur(fournisseurDao.selectById(rs.getLong(ID_FOURNISSEUR_COL)));

                articles.add(article);
            }
            return articles;

        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public float getAveragePrice() throws DaoException {
        Connection connection = ConnectionService.getInstance().getConnection();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(AVG_PRICE);;

            float averagePrice = 0f;
            if (rs.next()) {
                averagePrice = rs.getFloat(ALIAS_AVG_PRICE);
            }
            return averagePrice;

        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }
}
