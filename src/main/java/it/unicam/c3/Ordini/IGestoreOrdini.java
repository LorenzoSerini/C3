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

import java.util.List;

public interface IGestoreOrdini {
    /**
     * adds a new order
     * @param cliente
     * @param pv
     * @param prodotti
     */
    void addOrdine(Cliente cliente, PuntoVendita pv, List<Prodotto> prodotti);

    /**
     *
     * @param cliente
     * @return the order list
     */
    List<Ordine> getOrdini(Cliente cliente);

    /**
     *
     * @param cliente
     * @param stato
     * @return the list of customer orders with status parameter
     */
    List<Ordine> getOrdini(Cliente cliente, StatoOrdine stato);

    /**
     *
     * @param commerciante
     * @return the customer order list for the merchant parameter
     */
    List<Ordine> getOrdini(Commerciante commerciante);

    /**
     *
     * @param commerciante
     * @param stato
     * @return the customer order list for the merchant parameter with status parameter
     */
    List<Ordine> getOrdini(Commerciante commerciante, StatoOrdine stato);

    /**
     * set order state in index position
     * @param index
     * @param stato
     * @throws IllegalArgumentException
     */
    void setStato(int index, StatoOrdine stato) throws IllegalArgumentException;

    /**
     * set order state
     * @param ordine
     * @param stato
     * @throws IllegalArgumentException
     */
    void setStato(Ordine ordine, StatoOrdine stato) throws IllegalArgumentException;

}
