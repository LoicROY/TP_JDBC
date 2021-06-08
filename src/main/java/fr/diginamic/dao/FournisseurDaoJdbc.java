package fr.diginamic.dao;

import fr.diginamic.bo.Article;
import fr.diginamic.bo.Fournisseur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FournisseurDaoJdbc implements FournisseurDao {

    private static final String ID_COL = "ID";
    private static final String NOM_COL = "NOM";

    private static final String TABLE_NAME = "fournisseur";

    private static final String SELECT = String.format(
            "SELECT * FROM %s;", TABLE_NAME);

    private static final String SELECT_BY_ID = String.format(
            "SELECT * FROM %s WHERE %s=?;", TABLE_NAME, ID_COL);

    private static final String INSERT = String.format(
            "INSERT INTO %s VALUES (null, ?);", TABLE_NAME);

    private static final String UPDATE = String.format(
            "UPDATE %s SET %s=? WHERE %s=?;",
            TABLE_NAME, NOM_COL, ID_COL);

    private static final String DELETE = String.format(
            "DELETE FROM %s WHERE id=?;", TABLE_NAME);

    private static final String SELECT_BY_DESIGNATION = String.format(
            "SELECT * FROM %s WHERE %s LIKE ?;", TABLE_NAME, NOM_COL);

    @Override
    public List<Fournisseur> select() throws DaoException {
        Connection connection = ConnectionService.getInstance().getConnection();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SELECT);

            List<Fournisseur> fournisseurs = new ArrayList<>();
            while (rs.next()) {
                fournisseurs.add(new Fournisseur(rs.getInt(ID_COL), rs.getString(NOM_COL)));
            }
            return fournisseurs;

        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    public Fournisseur selectById(long id) throws DaoException {
        Connection connection = ConnectionService.getInstance().getConnection();
        try (PreparedStatement pst = connection.prepareStatement(SELECT_BY_ID)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();;

            Fournisseur fournisseur = new Fournisseur();
            while (rs.next()) {
                fournisseur.setId(rs.getInt(ID_COL));
                fournisseur.setNom(rs.getString(NOM_COL));
            }
            return fournisseur;

        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Fournisseur insert(Fournisseur fournisseur) throws DaoException {
        Connection connection = ConnectionService.getInstance().getConnection();
        try (PreparedStatement pst = connection.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, fournisseur.getNom());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();

            if (rs.next()) {
                fournisseur.setId(rs.getInt(1));
            }
            return fournisseur;

        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Fournisseur update(Fournisseur fournisseur) throws DaoException {
        Connection connection = ConnectionService.getInstance().getConnection();
        try (PreparedStatement pst = connection.prepareStatement(UPDATE)) {
            pst.setString(1, fournisseur.getNom());
            pst.setLong(2, fournisseur.getId());
            pst.executeUpdate();

            return fournisseur;

        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public boolean delete(Fournisseur fournisseur) throws DaoException {
        Connection connection = ConnectionService.getInstance().getConnection();
        try (PreparedStatement pst = connection.prepareStatement(DELETE)) {
            pst.setLong(1, fournisseur.getId());
            pst.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Fournisseur> selectByDesignation(String string) throws DaoException {
        Connection connection = ConnectionService.getInstance().getConnection();
        try (PreparedStatement pst = connection.prepareStatement(SELECT_BY_DESIGNATION)) {
            pst.setString(1, "%" + string + "%");
            ResultSet rs = pst.executeQuery();

            List<Fournisseur> fournisseurs = new ArrayList<>();

            while (rs.next()) {
                Fournisseur fournisseur = new Fournisseur();
                fournisseur.setId(rs.getInt(ID_COL));
                fournisseur.setNom(rs.getString(NOM_COL));

                fournisseurs.add(fournisseur);
            }
            return fournisseurs;

        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Fournisseur selectByExactDesignation(String string) throws DaoException {
        Connection connection = ConnectionService.getInstance().getConnection();
        try (PreparedStatement pst = connection.prepareStatement(SELECT_BY_DESIGNATION)) {
            pst.setString(1, string);
            ResultSet rs = pst.executeQuery();

            Fournisseur fournisseur = new Fournisseur();

            if (rs.next()) {
                fournisseur.setId(rs.getInt(ID_COL));
                fournisseur.setNom(rs.getString(NOM_COL));
            }
            return fournisseur;

        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

}
