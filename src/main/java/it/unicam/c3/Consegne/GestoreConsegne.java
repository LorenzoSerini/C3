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

import it.unicam.c3.Anagrafica.Cliente;
import it.unicam.c3.Anagrafica.Commerciante;
import it.unicam.c3.Anagrafica.Corriere;
import it.unicam.c3.Citta.PuntoRitiro;
import it.unicam.c3.Ordini.Ordine;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GestoreConsegne implements IGestoreConsegna{
   private static GestoreConsegne instance;
   private List<Consegna> consegne;

    private GestoreConsegne(){
       this.consegne= new LinkedList<>();
   }

   private GestoreConsegne(List<Consegna> consegne){
       this.consegne=consegne;
   }

    public static GestoreConsegne getInstance() {
        if (instance == null) {
            instance = new GestoreConsegne();
        }
        return instance;
    }

    public static GestoreConsegne getInstance(List<Consegna> consegne) {
        if (instance == null) {
            instance = new GestoreConsegne(consegne);
        }
        return instance;
    }

    @Override
    public void addConsegna(Ordine ordine, Commerciante commerciante, PuntoRitiro puntoRitiro) {
        this.consegne.add(new Consegna(commerciante,ordine,puntoRitiro));
    }

    @Override
    public List<Consegna> getConsegnePreseInCaricoDa(Corriere corriere) {
        return this.consegne.stream()
                .filter(c->c.getCorriere()!=null)
                .filter(c->c.getCorriere().equals(corriere))
                .filter(c->c.getStato().equals(StatoConsegna.PRESA_IN_CARICO))
               .collect(Collectors.toList());
    }

    @Override
    public List<Consegna> getConsegne(){
       return this.consegne;
    }

    @Override
    public List<Consegna> getConsegne(StatoConsegna stato) {
        return  this.consegne.stream()
                .filter(c->c.getStato()!=null)
                .filter(c->c.getStato().equals(stato))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consegna> getConsegne(Commerciante commerciante) {
        return this.consegne.stream()
                .filter(consegna -> consegna.getCommerciante().equals(commerciante))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consegna> getConsegne(Commerciante commerciante, StatoConsegna stato) {
        return this.consegne.stream()
                .filter(consegna -> consegna.getCommerciante().equals(commerciante))
                .filter(consegna -> consegna.getStato() == stato)
                .collect(Collectors.toList());
    }

    private void setConsegna(Consegna consegna, Corriere corriere, StatoConsegna stato) throws Exception {
        if(stato.equals(StatoConsegna.EFFETTUATA) || stato.equals(StatoConsegna.PRESA_IN_CARICO)) {
            if(this.consegne.contains(consegna)) {
                consegna.setCorriere(corriere);
                consegna.setStato(stato);
            }else throw new Exception("Consegna non trovata!");
        } else throw new Exception("Stato consegna errato!");
    }

    @Override
    public void prendiInCaricoConsegna(Consegna consegna, Corriere corriere) throws Exception {
           if(corriere!=null){
               setConsegna(consegna,corriere,StatoConsegna.PRESA_IN_CARICO);
           }else throw new IllegalArgumentException();
    }

    @Override
    public void prendiInCaricoConsegna(int index, Corriere corriere) throws Exception {
        prendiInCaricoConsegna(this.consegne.get(index), corriere);
    }

    @Override
    public void consegnaEffettuata(Consegna consegna, Corriere corriere) throws Exception {
        if(consegna.getCorriere().equals(corriere)&&consegna.getStato().equals(StatoConsegna.PRESA_IN_CARICO)){
            setConsegna(consegna,corriere,StatoConsegna.EFFETTUATA);
            consegna.getPuntoRitiro().incrementOccupati(1);
        }else throw new IllegalArgumentException();
    }

    @Override
    public void consegnaEffettuata(int index, Corriere corriere) throws Exception {
       consegnaEffettuata(this.consegne.get(index), corriere);
    }

    @Override
    public void consegnaRitirata(Consegna consegna, Cliente cliente) throws IllegalArgumentException {
        if(consegna.getOrdine().getCliente().equals(cliente)&&consegna.getStato().equals(StatoConsegna.EFFETTUATA)){
            setStato(consegna, StatoConsegna.RITIRATA);
            consegna.getPuntoRitiro().decrementOccupati(1);
        }else throw new IllegalArgumentException();
    }

    @Override
    public void consegnaRitirata(int index, Cliente cliente) throws IllegalArgumentException {
        consegnaRitirata(this.consegne.get(index),cliente);
    }


    @Override
    public void setStato(Consegna consegna, StatoConsegna stato) throws IllegalArgumentException{
        if(consegne.contains(consegna)) {
            consegna.setStato(stato);
        }else throw new IllegalArgumentException("Error setStato consegna: consegna non trovata!");
    }

    @Override
    public void setStato(int index, StatoConsegna stato) throws IllegalArgumentException{
        setStato(consegne.get(index), stato);
    }

    @Override
    public void abilitaRitiro(Consegna consegna){
        consegna.setRitirabile(true);
    }

    @Override
    public void annullaPresaInCarico(Consegna consegna, Corriere corriere){
        if(consegna.getCorriere().equals(corriere)){
            consegna.setCorriere(null);
            consegna.setStato(StatoConsegna.IN_ATTESA);
        }
    }

    public void annullaPresaInCarico(int indexConsegna, Corriere corriere){
        this.annullaPresaInCarico(this.consegne.get(indexConsegna),corriere);
    }
}
