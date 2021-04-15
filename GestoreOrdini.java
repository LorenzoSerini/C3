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
import it.unicam.c3.Anagrafica.Commerciante;
import it.unicam.c3.Commercio.Prodotto;
import it.unicam.c3.Commercio.PuntoVendita;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GestoreOrdini implements IGestoreOrdini{
    private static GestoreOrdini instance;
    private List<Ordine> ordini;

    private GestoreOrdini() {
        ordini = new LinkedList<>();
    }

    private GestoreOrdini(List<Ordine> ordini) {
        this.ordini = ordini;
    }

    public static GestoreOrdini getInstance() {
        if (instance == null) {
            instance = new GestoreOrdini();
        }
        return instance;
    }

    public static GestoreOrdini getInstance(List<Ordine> ordini) {
        if (instance == null) {
            instance = new GestoreOrdini(ordini);
        }
        return instance;
    }

    @Override
    public void addOrdine(Cliente cliente, PuntoVendita pv, List<Prodotto> prodotti) {
        Ordine ordine = new Ordine(cliente, pv, prodotti);
        ordini.add(ordine);
    }

    @Override
    public List<Ordine> getOrdini(Cliente cliente) {
        return ordini.stream()
                .filter(ordine->ordine.getCliente()!=null)
                .filter(ordine -> ordine.getCliente().equals(cliente))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ordine> getOrdini(Cliente cliente, StatoOrdine stato) {
        return ordini.stream()
                .filter(ordine->ordine.getCliente()!=null)
                .filter(ordine -> ordine.getCliente().equals(cliente) &&
                                  ordine.getStato().equals(stato))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ordine> getOrdini(Commerciante commerciante) {
        return ordini.stream()
                .filter(ordine->ordine.getPuntoVendita()!=null)
                .filter(ordine->ordine.getPuntoVendita().getCommerciante()!=null)
                .filter(ordine ->
                        ordine.getPuntoVendita().getCommerciante().equals(commerciante))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ordine> getOrdini(Commerciante commerciante, StatoOrdine stato) {
        return ordini.stream()
                .filter(ordine->ordine.getPuntoVendita()!=null)
                .filter(ordine->ordine.getPuntoVendita().getCommerciante()!=null)
                .filter(ordine ->
                        ordine.getPuntoVendita().getCommerciante().equals(commerciante) &&
                        ordine.getStato().equals(stato))
                .collect(Collectors.toList());
    }

    @Override
    public void setStato(int index, StatoOrdine stato) throws IllegalArgumentException{
        this.setStato(ordini.get(index),stato);
    }

    @Override
    public void setStato(Ordine ordine, StatoOrdine stato) throws IllegalArgumentException{
        if(ordini.contains(ordine)){
                ordine.setStato(stato);
            }else throw new IllegalArgumentException("Error setStato Ordine: Ordine non trovato");
        }
}
