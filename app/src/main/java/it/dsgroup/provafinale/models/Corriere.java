package it.dsgroup.provafinale.models;

import java.io.Serializable;

/**
 * Created by utente9.academy on 14/12/2017.
 */

public class Corriere implements Serializable {

    private String userCorriere;
    private String passCorriere;
    private String idPacco;

    public Corriere() {
    }

    public String getUserCorriere() {
        return userCorriere;
    }

    public void setUserCorriere(String userCorriere) {
        this.userCorriere = userCorriere;
    }

    public String getPassCorriere() {
        return passCorriere;
    }

    public void setPassCorriere(String passCorriere) {
        this.passCorriere = passCorriere;
    }

    public String getIdPacco() {
        return idPacco;
    }

    public void setIdPacco(String idPacco) {
        this.idPacco = idPacco;
    }
}
