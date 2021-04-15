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

package it.unicam.c3.Commercio;

import it.unicam.c3.Anagrafica.Commerciante;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PuntoVendita {
    private String id;
    private String nome;
    private String posizione;
    private Commerciante commerciante;
    private List<Prodotto> prodotti;
    private List<IOfferta> offerte;



    public PuntoVendita(Commerciante commerciante, String nome, String posizione, String id) {
       this.commerciante=commerciante;
        this.nome = nome;
        this.posizione = posizione;
        this.prodotti = new LinkedList<>();
        this.offerte=new LinkedList<>();
        if(id!=null) {
            this.id=id;
        }else this.id= UUID.randomUUID().toString();
    }

    public PuntoVendita(Commerciante commerciante, String nome, String posizione) {
        this(commerciante,nome,posizione,null);
    }

    public void setId(String id){
        this.id=id;
    }

    public String getId(){
        return this.id;
    }

    public String getPosizione() {
        return posizione;
    }

    public void setPosizione(String posizione) {
        this.posizione = posizione;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Commerciante getCommerciante() {
        return this.commerciante;
    }

    public List<Prodotto> getProdotti() {
        return this.prodotti;
    }

    public List<Prodotto> getProdottiDisponibili(){
        return this.prodotti.stream().filter(Prodotto::getDisponibilita).collect(Collectors.toList());
    }

    public void addProdotto(String descrizione, double prezzo) {
        this.prodotti.add(new Prodotto(descrizione, prezzo));
    }

    public void addProdotto(String id, String descrizione, double prezzo) {
        this.prodotti.add(new Prodotto(descrizione, prezzo, id));
    }

    public void removeProdotto(Prodotto prodotto){
        this.prodotti.remove(prodotto);
    }

    public void removeProdotto(int indexProdotto){
        this.removeProdotto(this.prodotti.get(indexProdotto));
    }

    public List<IOfferta> getOfferte() {
        return this.offerte;
    }

    public void addOfferta(String descrizione, String importo) {
        this.offerte.add(new OffertaSemplice(descrizione, importo));
    }

    public void addOfferta(String id, String descrizione, String importo) {
        this.offerte.add(new OffertaSemplice(descrizione, importo,id));
    }

    public void addOfferta(String id, String descrizione, String importo, LocalDate scadenza) {
        this.offerte.add(new OffertaATempo(descrizione, importo, scadenza, id));
    }

    public void addOfferta(String descrizione, String importo, LocalDate scadenza) {
        this.offerte.add(new OffertaATempo(descrizione, importo, scadenza));
    }

    public String toString(){
        return "Nome: ["+this.nome+"] Posizione: ["+this.posizione+"]";
    }

}
