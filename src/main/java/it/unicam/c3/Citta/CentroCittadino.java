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


package it.unicam.c3.Citta;

import it.unicam.c3.Anagrafica.Cliente;
import it.unicam.c3.Anagrafica.Commerciante;
import it.unicam.c3.Anagrafica.Corriere;
import it.unicam.c3.Commercio.PuntoVendita;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe singleton che gestisce liste di tipo Commerciante, Cliente, Corriere, PuntiRitiro.
 */
public class CentroCittadino {
    private static CentroCittadino instance;
    private List<Commerciante> commercianti;
    private List<Cliente> clienti;
    private List<Corriere> corrieri;
    private List<PuntoRitiro> puntiRitiro;

    private CentroCittadino(){
        commercianti = new LinkedList<>();
        clienti = new LinkedList<>();
        corrieri = new LinkedList<>();
        puntiRitiro = new LinkedList<>();
    }

    private CentroCittadino(List<Commerciante> commercianti, List<Cliente> clienti, List<Corriere> corrieri, List<PuntoRitiro> puntiRitiro){
        this.commercianti=commercianti;
        this.clienti=clienti;
        this.corrieri=corrieri;
        this.puntiRitiro=puntiRitiro;
    }

    public static CentroCittadino getInstance() {
        if (instance == null) {
            instance = new CentroCittadino();
        }
        return instance;
    }

    public static CentroCittadino getInstance(List<Commerciante> commercianti, List<Cliente> clienti, List<Corriere> corrieri, List<PuntoRitiro> puntiRitiro) {
        if (instance == null) {
            instance = new CentroCittadino(commercianti,clienti,corrieri,puntiRitiro);
        }
        return instance;
    }

    /**
     *
     * @return tutti i punti vendita disponibili
     */
    public List<PuntoVendita> getPuntiVendita() {
        List<PuntoVendita> puntiVendita = new LinkedList<>();
        for (Commerciante c:commercianti) {
            puntiVendita.addAll(c.getPuntiVendita());
        }
        return puntiVendita;
    }

    /**
     *
     * @return tutti i commercianti
     */
    public List<Commerciante> getCommercianti(){
        return this.commercianti;
    }

    /**
     * Aggiunge il commerciante alla lista dei commercianti
     * @param commerciante
     */
    public void addCommerciante(Commerciante commerciante){
        this.commercianti.add(commerciante);
    }

    /**
     * rimuove il commerciante dalla lista dei commercianti
     * @param index
     */
    public void removeCommerciante(int index){
        this.commercianti.remove(index);
    }

    /**
     * rimuove il commerciante dalla lista dei commercianti
     * @param commerciante
     */
    public void removeCommerciante(Commerciante commerciante){
        this.commercianti.remove(commerciante);
    }

    /**
     *
     * @return tutti i punti di ritiro
     */
    public List<PuntoRitiro> getPuntiRitiro(){
        return  this.puntiRitiro;
    }

    /**
     * Aggiunge il punto di ritiro
     * @param indirizzo
     * @param capienza
     */
    public void addPuntoRitiro(String indirizzo, int capienza){
        this.puntiRitiro.add(new PuntoRitiro(indirizzo,capienza));
    }

    /**
     * Rimuove il punto di ritiro
     * @param index
     */
    public void removePuntoRitiro(int index){
        this.puntiRitiro.remove(index);
    }

    /**
     * Rimuove il punto di ritiro
     * @param pr
     */
    public void removePuntoRitiro(PuntoRitiro pr){
        this.puntiRitiro.remove(pr);
    }

    /**
     * Aggiunge il cliente alla lista
     * @param cliente
     */
    public void addCliente(Cliente cliente){
        this.clienti.add(cliente);
    }

    /**
     *
     * @return la lista di tutti i clienti
     */
    public List<Cliente> getClienti(){
        return this.clienti;
    }

    /**
     * Rimuove il cliente dalla lista
     * @param index
     */
    public void removeCliente(int index){
        this.clienti.remove(index);
    }

    /**
     * Rimuove il cliente dalla lista
     * @param cliente
     */
    public void removeCliente(Cliente cliente){
        this.clienti.remove(cliente);
    }

    /**
     *
     * @return la lista di tutti i corrieri
     */
    public List<Corriere> getCorrieri(){
        return this.corrieri;
    }

    /**
     * Aggiunge il corriere alla lista
     * @param corriere
     */
    public void addCorriere(Corriere corriere){
        this.corrieri.add(corriere);
    }

    /**
     * Rimuove il corriere dalla lista
     * @param index
     */
    public void removeCorriere(int index){
        this.corrieri.remove(index);
    }

    /**
     * Rimuove il corriere dalla lista
     * @param corriere
     */
    public void removeCorriere(Corriere corriere){
        this.corrieri.remove(corriere);
    }

}
