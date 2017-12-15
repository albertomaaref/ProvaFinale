package it.dsgroup.provafinale.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by utente9.academy on 14/12/2017.
 */

public class Cliente implements Serializable {

    private String userCliente;
    private String passCliente;
    private ArrayList<Pacco> pacchiCliente;
    private ArrayList<Corriere> corrieriDisp;

    public Cliente() {
        pacchiCliente = new ArrayList<>();
        corrieriDisp = new ArrayList<>();
    }

    public String getUserCliente() {
        return userCliente;
    }

    public void setUserCliente(String userCliente) {
        this.userCliente = userCliente;
    }

    public String getPassCliente() {
        return passCliente;
    }

    public void setPassCliente(String passCliente) {
        this.passCliente = passCliente;
    }

    public ArrayList<Pacco> getPacchiCliente() {
        return pacchiCliente;
    }

    public void setPacchiCliente(ArrayList<Pacco> pacchiCliente) {
        this.pacchiCliente = pacchiCliente;
    }

    public ArrayList<Corriere> getCorrieriDisp() {
        return corrieriDisp;
    }

    public void setCorrieriDisp(ArrayList<Corriere> corrieriDisp) {
        this.corrieriDisp = corrieriDisp;
    }
}
