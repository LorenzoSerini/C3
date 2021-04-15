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

package it.unicam.c3.Ordini;

import it.unicam.c3.Anagrafica.Cliente;
import it.unicam.c3.Commercio.Prodotto;
import it.unicam.c3.Commercio.PuntoVendita;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Ordine {
    private String id;
    private StatoOrdine stato;
    private Cliente clienteOrdinante;
    private PuntoVendita pv;
    private List<Prodotto> prodotti = new LinkedList<>();

    public Ordine() { }

    public Ordine(Cliente clienteOrdinante, PuntoVendita pv, List<Prodotto> prodotti, String id, StatoOrdine stato) {
        this.clienteOrdinante = clienteOrdinante;
        this.pv = pv;
        this.stato=stato;
        if(prodotti!=null)  this.prodotti = prodotti;
        if (id == null) this.id = UUID.randomUUID().toString();
        else this.id = id;
    }

    public Ordine(Cliente clienteOrdinante, PuntoVendita pv, List<Prodotto> prodotti) {
        this(clienteOrdinante, pv, prodotti, null, StatoOrdine.IN_ATTESA);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StatoOrdine getStato() {
        return stato;
    }

    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }

    public Cliente getCliente() {
        return clienteOrdinante;
    }

    public void setCliente(Cliente cliente) {
        clienteOrdinante = cliente;
    }

    public PuntoVendita getPuntoVendita() {
        return pv;
    }

    public void setPuntoVendita(PuntoVendita pv) {
        this.pv = pv;
    }

    public void addProdotto(Prodotto prodotto) {
        prodotti.add(prodotto);
    }

    public List<Prodotto> getProdotti() {
        return prodotti;
    }

    public void removeProdotto(int index) {
        prodotti.remove(index);
    }

    public double getPrice() {
        double price = 0;
        for (Prodotto p : prodotti) {
            price += p.getPrezzo();
        }
        return price;
    }

    public String toString(){
       if(clienteOrdinante!=null && pv!=null) {
           return "Cliente: ["+this.clienteOrdinante.toString()+"] PuntoVendita: ["+this.pv.toString()+"] Stato: ["+getStato().toString()+"] N. Prodotti: ["+getProdotti().size()+"]";
       }
       else return "Cliente: [NULL] PuntoVendita: [NULL] Stato: ["+getStato().toString()+"] N. Prodotti: ["+getProdotti().size()+"]";
    }
}
