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

import it.unicam.c3.Anagrafica.Cliente;
import it.unicam.c3.Citta.CentroCittadino;
import it.unicam.c3.Commercio.IOfferta;
import it.unicam.c3.Commercio.Prodotto;
import it.unicam.c3.Commercio.PuntoVendita;
import it.unicam.c3.Consegne.Consegna;
import it.unicam.c3.Consegne.GestoreConsegne;
import it.unicam.c3.Consegne.StatoConsegna;
import it.unicam.c3.Ordini.GestoreOrdini;
import it.unicam.c3.Ordini.Ordine;
import it.unicam.c3.Ordini.StatoOrdine;
import it.unicam.c3.Persistence.DBCliente;
import it.unicam.c3.Persistence.IDBCliente;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class ControllerCliente {
    private Cliente cliente;
    private Map<PuntoVendita, List<Prodotto>> carrello;
    private IDBCliente database;

    public ControllerCliente(Cliente cliente) throws SQLException {
       this(cliente, new DBCliente());
    }

    public ControllerCliente(Cliente cliente, IDBCliente persistence){
        this.cliente = cliente;
        this.carrello = new HashMap<>();
        this.database=persistence;
    }

    /**
     * @return lista dei punti vendita disponibili
     */
    public List<PuntoVendita> getPuntiVendita() {
        return CentroCittadino.getInstance().getPuntiVendita();
    }

    /**
     * Aggiunge al carrello il Prodotto prodotto facente parte del PuntoVendita pv
     *
     * @param pv
     * @param prodotto
     */
    public void addInCarrello(PuntoVendita pv, Prodotto prodotto) {
        if (carrello.containsKey(pv)) {
            carrello.get(pv).add(prodotto);
        } else {
            LinkedList<Prodotto> p = new LinkedList<>();
            p.add(prodotto);
            carrello.put(pv, p);
        }
    }

    /**
     * Rimuove dal carrello il Prodotto prodotto facente parte del PuntoVendita pv
     *
     * @param pv
     * @param prodotto
     */
    public void removeFromCarrello(PuntoVendita pv, Prodotto prodotto) {
        this.carrello.get(pv).remove(prodotto);
        controlCarrello();
    }

    /**
     * Rimuove dal carrello il prodotto indexProdotto facente parte del punto vendita indexPuntoVendita
     *
     * @param indexPuntoVendita
     * @param indexProdotto
     */
    public void removeFromCarrello(int indexPuntoVendita, int indexProdotto) {
        PuntoVendita puntov = null;
        int count = 0;
        for (PuntoVendita pv : this.carrello.keySet()) {
            if (count == indexPuntoVendita) {
                puntov = pv;
            }
            count++;
        }
        this.carrello.get(puntov).remove(indexProdotto);
        controlCarrello();
    }

    /**
     * Se l'ordine non contiene prodotti, lo elimina dal carrello
     */
    private void controlCarrello() {
        for (PuntoVendita pv : this.carrello.keySet()) {
            if (this.carrello.get(pv).isEmpty()) this.carrello.remove(pv);
        }
    }

    /**
     * @return carrello
     */
    public Map<PuntoVendita, List<Prodotto>> getCarrello() {
        return this.carrello;
    }

    /**
     * Elimina tutti i prodotti dal carrello
     */
    public void clearCarrello() {
        this.carrello.clear();
    }

    /**
     * Crea un ordine con i prodotti presenti nel carrello
     */
    public void ordinaProdotti() throws Exception {
        for (PuntoVendita pv : carrello.keySet()) {
            GestoreOrdini.getInstance().addOrdine(this.cliente, pv, carrello.get(pv));
            database.saveOrdine(GestoreOrdini.getInstance().getOrdini(this.cliente).get(GestoreOrdini.getInstance().getOrdini(this.cliente).size()-1));
        }
        carrello.clear();
    }

    /**
     * Segnala che la consegna è stata ritirata
     *
     * @param consegna
     */
    public void setConsegnaRitirata(Consegna consegna) throws Exception {
        GestoreConsegne.getInstance().consegnaRitirata(consegna, this.cliente);
        this.database.updateConsegnaRitirata(consegna);
        this.database.updateDisponibilita(consegna.getPuntoRitiro());
    }

    /**
     * @return lista delle consegne
     */
    public List<Consegna> getConsegne() {
        return GestoreConsegne.getInstance().getConsegne().stream()
                .filter(c -> c.getOrdine().getCliente().equals(this.cliente))
                .collect(Collectors.toList());
    }

    /**
     * @param stato
     * @return lista delle consegne con stato
     */
    public List<Consegna> getConsegne(StatoConsegna stato) {
        return GestoreConsegne.getInstance().getConsegne().stream()
                .filter(c -> c.getOrdine().getCliente().equals(this.cliente))
                .filter(c -> c.getStato().equals(stato))
                .collect(Collectors.toList());
    }

    /**
     * Se la consegna è disponibile al ritiro, ritorna un array di due posizioni,
     * Nella prima posizione troviamo l'Id della consegna.
     * Nella seconda posizione troviamo l'indirizzo del punto di ritiro.
     *
     * @param consegna
     * @return String[2]
     */
    public String[] getDettagliRitiro(Consegna consegna) {
        String[] dettagli = new String[2];
        if (consegna.isRitirabile()) {
            dettagli[0] = consegna.getId();
            dettagli[1] = consegna.getPuntoRitiro().getIndirizzo();
        } else {
            dettagli[0] = "NON ABILITATO AL RITIRO";
            dettagli[1] = "NON ABILITATO AL RITIRO";
        }
        return dettagli;
    }

    /**
     * @return lista degli ordini effettuati
     */
    public List<Ordine> getOrdini() {
        return GestoreOrdini.getInstance().getOrdini(this.cliente);
    }

    /**
     * @param stato
     * @return lista degli ordini in un determinato stato
     */
    public List<Ordine> getOrdini(StatoOrdine stato) {
        return GestoreOrdini.getInstance().getOrdini(this.cliente, stato);
    }

    /**
     *
     * @return le offerte pubblicate da tutti i punti vendita disponibili
     */
    public Map<PuntoVendita,List<IOfferta>> getOfferte() {
        Map<PuntoVendita, List<IOfferta>> offerteA = new HashMap<>();
        for (PuntoVendita pv : CentroCittadino.getInstance().getPuntiVendita()) {
            if(!pv.getOfferte().isEmpty()) offerteA.put(pv,pv.getOfferte());
        }
        return offerteA;
    }

    /**
     *
     * @param pv
     * @return le offerte di un singolo punto vendita
     */
    public List<IOfferta> getOfferte(PuntoVendita pv) {
        LinkedList<IOfferta> offerte = new LinkedList<>();
            for (IOfferta offerta : pv.getOfferte()) {
                offerte.add(offerta);
            }
        return offerte;
    }



}
