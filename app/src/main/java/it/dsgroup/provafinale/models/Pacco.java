package it.dsgroup.provafinale.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by utente9.academy on 14/12/2017.
 */

public class Pacco implements Serializable{

    private String idPacco; // scelto come stringa per eventuale id alfanumerico
    private String deposito;
    private String destinatario;
    private String stato;
    private Date dataConsegna;
    private String indDep;
    private String indCons;
    private String corriere;

    public String getCorriere() {
        return corriere;
    }

    public void setCorriere(String corriere) {
        this.corriere = corriere;
    }



    public String getDimensione() {
        return dimensione;
    }

    public void setDimensione(String dimensione) {
        this.dimensione = dimensione;
    }

    private String dimensione;

    public Pacco() {
    }

    public String getIdPacco() {
        return idPacco;
    }

    public void setIdPacco(String idPacco) {
        this.idPacco = idPacco;
    }

    public String getDeposito() {
        return deposito;
    }

    public void setDeposito(String deposito) {
        this.deposito = deposito;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public Date getDataConsegna() {
        return dataConsegna;
    }

    public void setDataConsegna(Date dataConsegna) {
        this.dataConsegna = dataConsegna;
    }

    public String getIndDep() {
        return indDep;
    }

    public void setIndDep(String indDep) {
        this.indDep = indDep;
    }

    public String getIndCons() {
        return indCons;
    }

    public void setIndCons(String indCons) {
        this.indCons = indCons;
    }
}
