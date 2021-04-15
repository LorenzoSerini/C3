/*******************************************************************************
 * MIT License

 * Copyright (c) 2021 Lorenzo Serini and Alessandro Pecugi
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
/**
 *
 */

package it.unicam.c3.Controller;

import it.unicam.c3.Citta.CentroCittadino;
import it.unicam.c3.Citta.PuntoRitiro;
import it.unicam.c3.Consegne.GestoreConsegne;
import it.unicam.c3.Ordini.GestoreOrdini;
import it.unicam.c3.Persistence.*;

import java.util.List;

public class ControllerGestore {
    private static String password = "admin";
    private boolean autorizzato = false;
    private IDBPuntiRitiro dbPuntiRitiro;

    public ControllerGestore(IDBAccounts dbAccounts, IDBPuntiRitiro dbPuntiRitiro, IDBOrdini dbOrdini, IDBConsegne dbConsegne ) throws Exception {
        this.dbPuntiRitiro=dbPuntiRitiro;
        CentroCittadino.getInstance(dbAccounts.getCommercianti(), dbAccounts.getClienti(), dbAccounts.getCorrieri(), dbPuntiRitiro.getPuntiRitiro());
        GestoreOrdini.getInstance(dbOrdini.getOrdini());
        GestoreConsegne.getInstance(dbConsegne.getConsegne());
    }

    public ControllerGestore() throws Exception {
        this(new DBAccounts(), new DBPuntiRitiro(), new DBOrdini(), new DBConsegne());
    }

    public boolean autorizza(String password) {
        this.autorizzato = ControllerGestore.password.equals(password);
        return this.autorizzato;
    }

    public boolean isAutorizzato() {
        return autorizzato;
    }

    public List<PuntoRitiro> getPuntiRitiro() {
        if (!autorizzato) throw new RuntimeException("Utente non autorizzato");
        return CentroCittadino.getInstance().getPuntiRitiro();
    }

    public void addPuntoRitiro(String indirizzo, int capienza) throws Exception {
        if (!autorizzato) throw new RuntimeException("Utente non autorizzato");
        CentroCittadino.getInstance().addPuntoRitiro(indirizzo, capienza);
        dbPuntiRitiro.savePuntoRitiro(CentroCittadino.getInstance().getPuntiRitiro().get(CentroCittadino.getInstance().getPuntiRitiro().size()-1));
    }

    public void removePuntoRitiro(PuntoRitiro pr) throws Exception {
        if (!autorizzato) throw new RuntimeException("Utente non autorizzato");
        CentroCittadino.getInstance().getPuntiRitiro().remove(pr);
        this.dbPuntiRitiro.removePuntoRitiro(pr);
    }

    public void removePuntoRitiro(int indexPr) throws Exception {
        if (!autorizzato) throw new RuntimeException("Utente non autorizzato");
        this.dbPuntiRitiro.removePuntoRitiro(CentroCittadino.getInstance().getPuntiRitiro().get(indexPr));
        this.removePuntoRitiro(CentroCittadino.getInstance().getPuntiRitiro().get(indexPr));
    }


}
