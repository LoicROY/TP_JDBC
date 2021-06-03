package fr.diginamic.dao;

public class DaoException extends Exception {

    private static final long serialVersionUID = 1L;

    DaoException(){
        super();
    }

    DaoException(String message){
        super(message);
    }

    @Override
    public String getMessage(){
        return "DAO - " + super.getMessage();
    }
}
