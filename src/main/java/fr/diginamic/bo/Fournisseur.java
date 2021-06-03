package fr.diginamic.bo;

public class Fournisseur {

    private long id;
    private String nom;

    public Fournisseur() {
    }

    public Fournisseur(String nom) {
        this.nom = nom;
    }

    public Fournisseur(long id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Fournisseur [id=" + id + ", nom=" + nom + "]";
    }
}
