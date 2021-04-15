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

import it.unicam.c3.Anagrafica.Commerciante;
import it.unicam.c3.Citta.CentroCittadino;
import it.unicam.c3.Citta.PuntoRitiro;
import it.unicam.c3.Commercio.IOfferta;
import it.unicam.c3.Commercio.Prodotto;
import it.unicam.c3.Commercio.PuntoVendita;
import it.unicam.c3.Consegne.Consegna;
import it.unicam.c3.Consegne.GestoreConsegne;
import it.unicam.c3.Consegne.StatoConsegna;
import it.unicam.c3.Ordini.GestoreOrdini;
import it.unicam.c3.Ordini.Ordine;
import it.unicam.c3.Ordini.StatoOrdine;
import it.unicam.c3.Persistence.DBCommerciante;
import it.unicam.c3.Persistence.IDBCommerciante;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerCommerciante {
    private Commerciante commerciante;
    private IDBCommerciante database;

    public ControllerCommerciante(Commerciante c, IDBCommerciante database) throws SQLException {
        this.commerciante = c;
        this.database=database;
    }

    public ControllerCommerciante(Commerciante c) throws SQLException {
        this(c,new DBCommerciante(c));
    }

    /**
     * Aggiunge un nuovo punto vendita
     * @param posizione
     * @param nome
     * @throws Exception
     */
    public void addPuntoVendita(String posizione, String nome) throws Exception {
        this.commerciante.addPuntoVendita(nome,posizione);
        this.database.savePuntoVendita(this.getPuntiVendita().get(this.getPuntiVendita().size()-1));
    }

    /**
     * Rimuove il punto vendita
     * @param i
     * @throws Exception
     */
    public void removePuntoVendita(int i) throws Exception {
        this.commerciante.getPuntiVendita().remove(i);
        this.database.removePuntoVendita(this.getPuntiVendita().get(i));
    }

    /**
     * Rimuove il punto vendita
     * @param pv
     * @throws Exception
     */
    public void removePuntoVendita(PuntoVendita pv) throws Exception {
        this.commerciante.getPuntiVendita().remove(pv);
        this.database.removePuntoVendita(pv);
    }

    /**
     * Aggiunge un nuovo prodotto
     * @param pv
     * @param descrizione
     * @param prezzo
     * @throws Exception
     */
    public void addProdotto(PuntoVendita pv, String descrizione, double prezzo) throws Exception {
        if(this.commerciante.getPuntiVendita().contains(pv)){
            pv.addProdotto(descrizione,prezzo);
            this.database.saveProdotto(pv,pv.getProdotti().get(pv.getProdotti().size()-1));
        } else throw new IllegalArgumentException();
    }

    /**
     * Aggiunge un nuovo prodotto
     * @param indexPv
     * @param descrizione
     * @param prezzo
     * @throws Exception
     */
    public void addProdotto(int indexPv, String descrizione, double prezzo) throws Exception {
       this.addProdotto(this.commerciante.getPuntiVendita().get(indexPv), descrizione, prezzo);
    }

    /**
     * Rimuove il prodotto
     * @param pv
     * @param prodotto
     * @throws Exception
     */
    public void removeProdotto(PuntoVendita pv, Prodotto prodotto) throws Exception {
        if(this.commerciante.getPuntiVendita().contains(pv)){
            pv.removeProdotto(prodotto);
            this.database.removeProdotto(prodotto);
        } else throw new Exception("Punto vendita non presente!");
    }

    /**
     * Rimuove il prodotto
     * @param indexPv
     * @param prodotto
     * @throws Exception
     */
    public void removeProdotto(int indexPv, Prodotto prodotto) throws Exception {
       this.removeProdotto(this.commerciante.getPuntiVendita().get(indexPv), prodotto);
    }

    /**
     * Rimuove il prodotto
     * @param pv
     * @param indexProdotto
     * @throws Exception
     */
    public void removeProdotto(PuntoVendita pv, int indexProdotto) throws Exception {
        this.removeProdotto(pv , pv.getProdotti().get(indexProdotto));
    }

    /**
     * Rimuove il prodotto
     * @param indexPv
     * @param indexProdotto
     * @throws Exception
     */
    public void removeProdotto(int indexPv, int indexProdotto) throws Exception {
        this.removeProdotto(this.commerciante.getPuntiVendita().get(indexPv) , this.commerciante.getPuntiVendita().get(indexPv).getProdotti().get(indexProdotto));
    }

    /**
     *
     * @return la lista dei punti vendita
     */
    public List<PuntoVendita> getPuntiVendita() {
        return this.commerciante.getPuntiVendita();
    }

    /**
     *
     * @return la lista degli ordini
     */
    public List<Ordine> getOrdini() {
        return GestoreOrdini.getInstance().getOrdini(this.commerciante);
    }

    /**
     *
     * @param stato
     * @return la lista degli ordini in un determinato stato
     */
    public List<Ordine> getOrdini(StatoOrdine stato) {
        return GestoreOrdini.getInstance().getOrdini(this.commerciante, stato);
    }

    /**
     * Usato dal commerciante per accettare un ordine
     * @param ordine
     * @param pv
     * @throws Exception
     */
    public void accettaOrdine(Ordine ordine, PuntoRitiro pv) throws Exception {
        GestoreConsegne.getInstance().addConsegna(ordine,this.commerciante,pv);
        GestoreOrdini.getInstance().setStato(ordine, StatoOrdine.ACCETTATO);
        this.database.updateStatoOrdine(ordine, StatoOrdine.ACCETTATO);
        this.database.saveConsegna(GestoreConsegne.getInstance().getConsegne().get(GestoreConsegne.getInstance().getConsegne().size()-1));
    }

    /**
     * Usato dal commerciante per rifiutare un ordine
     * @param ordine
     * @throws Exception
     */
    public void rifiutaOrdine(Ordine ordine) throws Exception {
        GestoreOrdini.getInstance().setStato(ordine, StatoOrdine.RIFIUTATO);
        this.database.updateStatoOrdine(ordine, StatoOrdine.RIFIUTATO);
    }

    /**
     *
     * @return la lista delle consegne
     */
    public List<Consegna> getConsegne() {
        return GestoreConsegne.getInstance().getConsegne(this.commerciante);
    }

    /**
     *
     * @param stato
     * @return la lista delle consegne in un determinato stato
     */
    public List<Consegna> getConsegne(StatoConsegna stato) {
        return GestoreConsegne.getInstance().getConsegne(this.commerciante, stato);
    }

    /**
     *
     * @return la lista delle consegne da abilitare al ritiro
     */
    public List<Consegna> getConsegneDaAbilitareAlRitiro(){
        List<Consegna> daAbilitare = new LinkedList<>();
        daAbilitare.addAll(this.getConsegne(StatoConsegna.PRESA_IN_CARICO));
        daAbilitare.addAll(this.getConsegne(StatoConsegna.EFFETTUATA));
        daAbilitare.removeIf(Consegna::isRitirabile);
        return daAbilitare;
    }

    /**
     * Aggiunge una nuova offerta
     * @param pv
     * @param descrizione
     * @param importo
     * @throws Exception
     */
    public void addOfferta(PuntoVendita pv, String descrizione, String importo) throws Exception {
        if (this.commerciante.getPuntiVendita().contains(pv)){
            pv.addOfferta(descrizione,importo);
            this.database.saveOfferta(pv,pv.getOfferte().get(pv.getOfferte().size()-1));
        } else throw new IllegalArgumentException();
    }

    /**
     * Aggiunge una nuova offerta
     * @param pv
     * @param descrizione
     * @param importo
     * @param date
     * @throws Exception
     */
    public void addOfferta(PuntoVendita pv, String descrizione, String importo, LocalDate date) throws Exception {
        if (this.commerciante.getPuntiVendita().contains(pv)){
            pv.addOfferta(descrizione,importo, date);
           this.database.saveOfferta(pv,pv.getOfferte().get(pv.getOfferte().size()-1),date);
        } else throw new IllegalArgumentException();
    }

    /**
     * Rimuove l'offerta
     * @param pv
     * @param offerta
     * @throws Exception
     */
    public void removeOfferta(PuntoVendita pv, IOfferta offerta) throws Exception {
        if(this.commerciante.getPuntiVendita().contains(pv) && pv.getOfferte().contains(offerta)){
            pv.getOfferte().remove(offerta);
            this.database.removeOfferta(offerta);
        } else throw new IllegalArgumentException();
    }

    /**
     * Abilita la consegna al ritiro
     * @param consegna
     * @throws Exception
     */
    public void abilitaRitiro(Consegna consegna) throws Exception {
        GestoreConsegne.getInstance().abilitaRitiro(consegna);
        this.database.updateConsegnaRitirabile(consegna);
    }

    /**
     * Rimuove l'offerta
     * @param indexPv
     * @param offerta
     * @throws Exception
     */
    public void removeOfferta(int indexPv, IOfferta offerta) throws Exception {
        this.removeOfferta(this.commerciante.getPuntiVendita().get(indexPv), offerta);
    }

    /**
     * Rimuove l'offerta
     * @param pv
     * @param indexOfferta
     * @throws Exception
     */
    public void removeOfferta(PuntoVendita pv, int indexOfferta) throws Exception {
        this.removeOfferta(pv, pv.getOfferte().get(indexOfferta));
    }

    /**
     * Rimuove l'offerta
     * @param indexPv
     * @param indexOfferta
     * @throws Exception
     */
    public void removeOfferta(int indexPv, int indexOfferta) throws Exception {
        this.removeOfferta(this.commerciante.getPuntiVendita().get(indexPv), this.commerciante.getPuntiVendita().get(indexPv).getOfferte().get(indexOfferta));
    }

    /**
     *
     * @return la lista dei punti di ritiro disponibili
     */
    public List<PuntoRitiro> getPuntiRitiroDisponibili(){
        return CentroCittadino.getInstance().getPuntiRitiro().stream()
                .filter(pr->pr.getSlotDisponibili()>0)
                .collect(Collectors.toList());
    }

    /**
     * Cambia la disponibilità del prodotto
     * @param p
     * @param disponibilie
     * @throws Exception
     */
    public void cambiaDisponibilitaProdotto(Prodotto p, boolean disponibilie) throws Exception {
        p.setDisponibilita(disponibilie);
        this.database.updateDisponibilitaProdotto(p);
    }

}
