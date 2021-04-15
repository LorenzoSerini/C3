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

import it.unicam.c3.Anagrafica.Corriere;
import it.unicam.c3.Consegne.Consegna;
import it.unicam.c3.Consegne.GestoreConsegne;
import it.unicam.c3.Consegne.StatoConsegna;
import it.unicam.c3.Persistence.DBCorriere;
import it.unicam.c3.Persistence.IDBCorriere;

import java.util.List;

public class ControllerCorriere {
    private Corriere corriere;
    private ControllerEmail infoEmail;
    private IDBCorriere dbCorriere;

    public ControllerCorriere(Corriere corriere) throws Exception {
        this.corriere=corriere;
        this.dbCorriere=new DBCorriere();
        this.infoEmail=new ControllerEmail();
    }


    public ControllerCorriere(Corriere corriere, IDBCorriere persistence) throws Exception {
        this.corriere=corriere;
        this.dbCorriere=persistence;
        this.infoEmail=new ControllerEmail();
    }

    /**
     *
     * @return lista delle consegne pronte per essere consegnate
     */
    public List<Consegna> getConsegneInAttesa(){
        return GestoreConsegne.getInstance().getConsegne(StatoConsegna.IN_ATTESA);
    }

    /**
     *
     * @return lista delle consegne prese in carico
     */
    public List<Consegna> getConsegneInCarico(){
        return GestoreConsegne.getInstance().getConsegnePreseInCaricoDa(this.corriere);
    }

    /**
     * Utilizzato per prendere in carico una consegna
     * @param consegna
     */
    public void prendiInCarico(Consegna consegna) throws Exception {
        GestoreConsegne.getInstance().prendiInCaricoConsegna(consegna,this.corriere);
        this.dbCorriere.updateConsegnaInCarico(consegna);
        this.dbCorriere.updateCorriere(consegna);
    }

    /**
     * Utilizzato per prendere in carico una consegna
     * @param indexConsegna
     */
    public void prendiInCarico(int indexConsegna) throws Exception {
       this.prendiInCarico(this.getConsegneInAttesa().get(indexConsegna));
    }

    /**
     * Utilizzato quando la consegna è stata portata a termine
     * @param consegna
     */
    public void effettuaConsegna(Consegna consegna) throws Exception {
        GestoreConsegne.getInstance().consegnaEffettuata(consegna,this.corriere);
        this.dbCorriere.updateConsegnaEffettuata(consegna);
        this.dbCorriere.updateDisponibilita(consegna.getPuntoRitiro());
        infoEmail.sendEmail(consegna.getOrdine().getCliente().getEmail(), "Consegna effettuata", "Si informa che la sua consegna \u00E8 stata effettuata presso un punto di ritiro.\nAppena l'ordine verr\u00E0 pagato presso il punto vendita"+
        ", sar\u00E0 disponibile per il ritiro.\nIl punto di ritiro e l'id di sblocco verranno resi disponibili " +
                "successivamente al pagamento dell'ordine\n\nPunto Vendita: "+consegna.getOrdine().getPuntoVendita().getNome()+" sito in "+consegna.getOrdine().getPuntoVendita().getPosizione()+"\n\nNB: Questa mail \u00E8 generata da un sistema automatico non presidiato pertanto si invita cortesemente a non rispondere - eventuali email ricevute rimarranno inevase.\n" +
                "\nGrazie e cordiali saluti.\nC3 Team System");
    }

    /**
     * Utilizzato quando la consegna è stata portata a termine
     * @param indexConsegna
     */
    public void effettuaConsegna(int indexConsegna) throws Exception {
        this.effettuaConsegna(this.getConsegneInCarico().get(indexConsegna));
    }

    /**
     * Utilizzato per annullare la presa in carico di una consegna, riportarla nello
     * stato IN_ATTESA e rimuovere il corriere.
     * La consegna potra nuovamente essere visualizzata e presa in carico da un Corriere.
     * @param consegna
     * @throws Exception
     */
    public void annullaPresaInCarico(Consegna consegna) throws Exception {
        GestoreConsegne.getInstance().annullaPresaInCarico(consegna,this.corriere);
        this.dbCorriere.updateConsegnaInAttesa(consegna);
        this.dbCorriere.updateCorriereNullo(consegna);
    }

    /**
     * Utilizzato per annullare la presa in carico di una consegna, riportarla nello
     * stato IN_ATTESA e rimuovere il corriere.
     * La consegna potra nuovamente essere visualizzata e presa in carico da un Corriere.
     * @param indexConsegna
     * @throws Exception
     */
    public void annullaPresaInCarico(int indexConsegna) throws Exception {
        this.annullaPresaInCarico(this.getConsegneInCarico().get(indexConsegna));
    }

}
