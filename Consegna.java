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

package it.unicam.c3.Consegne;

import it.unicam.c3.Anagrafica.Commerciante;
import it.unicam.c3.Anagrafica.Corriere;
import it.unicam.c3.Citta.PuntoRitiro;
import it.unicam.c3.Ordini.Ordine;

import java.util.UUID;

public class Consegna {
    private StatoConsegna stato;
    private Ordine ordine;
    private Corriere corriere;
    private Commerciante commerciante;
    private PuntoRitiro puntoRitiro;
    private String id;
    private boolean ritirabile;

    public Consegna(Commerciante commerciante, Ordine ordine, PuntoRitiro puntoRitiro, String id){
        this.commerciante=commerciante;
        this.ordine=ordine;
        this.puntoRitiro=puntoRitiro;
        this.stato=StatoConsegna.IN_ATTESA;
       if(id!=null) {
           this.id=id;
       } else this.id = UUID.randomUUID().toString();
        ritirabile=false;
    }

    public Consegna(Commerciante commerciante, Ordine ordine, PuntoRitiro puntoRitiro) {
        this(commerciante, ordine, puntoRitiro, null);
    }

    /**
     *
     * @return id
     */
    public String getId(){
        return this.id.toString();
    }

    /**
     * set id
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * set state
     * @param stato
     */
    public void setStato(StatoConsegna stato){
        this.stato=stato;
    }

    /**
     *
     * @return state
     */
    public StatoConsegna getStato(){
        return this.stato;
    }

    /**
     * set Ordine associato
     * @param ordine
     */
    public void setOrdine(Ordine ordine){
        this.ordine=ordine;
    }

    /**
     *
     * @return Ordine
     */
    public Ordine getOrdine(){
        return this.ordine;
    }

    /**
     * set Corriere associato
     * @param corriere
     */
    public void setCorriere(Corriere corriere){
        this.corriere=corriere;
    }

    /**
     *
     * @return Corriere associato
     */
    public Corriere getCorriere(){
        return this.corriere;
    }

    /**
     *
     * @return Commerciante associato
     */
    public Commerciante getCommerciante(){
        return this.commerciante;
    }

    /**
     * set PuntoRitiro associato
     * @param puntoRitiro
     */
    public void setPuntoRitiro(PuntoRitiro puntoRitiro){
        this.puntoRitiro=puntoRitiro;
    }

    /**
     *
     * @return PuntoRitiro associato
     */
    public PuntoRitiro getPuntoRitiro(){
        return this.puntoRitiro;
    }

    /**
     * return se la consegna può essere ritirata dal punto di ritiro
     * @return boolean
     */
    public boolean isRitirabile(){
        return ritirabile;
    }

    /**
     * set la ritirabilità della consegna
     * @param bool
     */
    public void setRitirabile(boolean bool){
         this.ritirabile=bool;
    }

    public String toString(){
        if(corriere!=null) {
            return "Commerciante: [" + commerciante.toString() + "] Ordine: [" + ordine.toString() + "] Stato: [" + stato.toString()+"] Corriere: ["+this.corriere.toString()+"] Punto Ritiro: [" + puntoRitiro.getIndirizzo() + "] ";
        } else return "Commerciante: [" + commerciante.toString() + "] Ordine: [" + ordine.toString() + "] Stato: [" + stato.toString() +"] Corriere: [NULL] Punto Ritiro: [" + puntoRitiro.getIndirizzo() + "] ";

    }
}
