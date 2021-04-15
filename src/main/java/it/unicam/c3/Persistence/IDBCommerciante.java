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

package it.unicam.c3.Persistence;

import it.unicam.c3.Anagrafica.Commerciante;
import it.unicam.c3.Commercio.IOfferta;
import it.unicam.c3.Commercio.OffertaSemplice;
import it.unicam.c3.Commercio.Prodotto;
import it.unicam.c3.Commercio.PuntoVendita;
import it.unicam.c3.Consegne.Consegna;
import it.unicam.c3.Ordini.Ordine;
import it.unicam.c3.Ordini.StatoOrdine;

import java.sql.SQLException;
import java.time.LocalDate;

public interface IDBCommerciante{

    /**
     * Salva un punto vendita
     * @param pv
     * @throws Exception
     */
    void savePuntoVendita(PuntoVendita pv) throws Exception;

    /**
     * Salva un prodotto
     * @param pv
     * @param prodotto
     * @throws Exception
     */
    void saveProdotto(PuntoVendita pv, Prodotto prodotto) throws Exception;

    /**
     * Aggiorna disponibilità prodotto
     * @param prodotto
     * @throws Exception
     */
    void updateDisponibilitaProdotto(Prodotto prodotto) throws Exception;

    /**
     * Salva offerta
     * @param pv
     * @param offerta
     * @throws Exception
     */
    void saveOfferta(PuntoVendita pv, IOfferta offerta) throws Exception;

    /**
     * Salva offerta a tempo
     * @param pv
     * @param offerta
     * @param date
     * @throws Exception
     */
    void saveOfferta(PuntoVendita pv, IOfferta offerta, LocalDate date) throws Exception;
    /**
     * Salva rimozione punto vendita
     * @param pv
     * @throws Exception
     */
    void removePuntoVendita(PuntoVendita pv) throws Exception;

    /**
     * Salva rimozione prodotto
     * @param prodotto
     * @throws Exception
     */
    void removeProdotto(Prodotto prodotto) throws  Exception;

    /**
     * Salva rimozione offerta
     * @param offerta
     * @throws Exception
     */
    void removeOfferta(IOfferta offerta) throws Exception;

    /**
     * Aggiorna lo stato dell'ordine
     * @param ordine
     * @param stato
     * @throws Exception
     */
    void updateStatoOrdine(Ordine ordine, StatoOrdine stato) throws Exception;

    /**
     * Salva consegna
     * @param consegna
     * @throws Exception
     */
    void saveConsegna(Consegna consegna) throws Exception;

    /**
     * Aggiorna ritirabilità consegna
     * @param consegna
     * @throws Exception
     */
    void updateConsegnaRitirabile(Consegna consegna) throws Exception;

}
