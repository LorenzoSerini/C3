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

import java.util.List;

public interface IGestoreConsegna {
    /**
     * adds a new delivery
     * @param ordine
     * @param commerciante
     * @param puntoRitiro
     */
    void addConsegna(Ordine ordine, Commerciante commerciante, PuntoRitiro puntoRitiro);

    /**
     *
     * @param corriere
     * @return delivery by courier
     */
    List<Consegna> getConsegnePreseInCaricoDa(Corriere corriere);

    /**
     *
     * @return all delivery
     */
    List<Consegna> getConsegne();

    /**
     *
     * @param stato
     * @return delivery with status parameter
     */
    List<Consegna> getConsegne(StatoConsegna stato);

    /**
     *
     * @param commerciante
     * @return
     */
    List<Consegna> getConsegne(Commerciante commerciante);

    /**
     *
     * @param stato
     * @return
     */
    List<Consegna> getConsegne(Commerciante commerciante, StatoConsegna stato);

    /**
     * used when a courier takes over a delivery
     * @param consegna
     * @param corriere
     * @throws IllegalArgumentException
     */
    void prendiInCaricoConsegna(Consegna consegna, Corriere corriere) throws Exception;

    /**
     * used when a courier takes over a delivery
     * @param index
     * @param corriere
     * @throws IllegalArgumentException
     */
    void prendiInCaricoConsegna(int index, Corriere corriere) throws Exception;

    /**
     * used when a courier makes a delivery
     * @param consegna
     * @param corriere
     * @throws IllegalArgumentException
     */
    void consegnaEffettuata(Consegna consegna, Corriere corriere) throws Exception;

    /**
     * used when a courier makes a delivery
     * @param index
     * @param corriere
     * @throws IllegalArgumentException
     */
    void consegnaEffettuata(int index, Corriere corriere) throws Exception;

    /**
     * used when a customer picks up a delivery
     * @param consegna
     * @param cliente
     * @throws IllegalArgumentException
     */
    void consegnaRitirata(Consegna consegna, Cliente cliente) throws IllegalArgumentException;

    /**
     * used when a customer picks up a delivery
     * @param index
     * @param cliente
     * @throws IllegalArgumentException
     */
    void consegnaRitirata(int index, Cliente cliente) throws IllegalArgumentException;


    /**
     * set delivery state
     * @param consegna
     * @param stato
     * @throws IllegalArgumentException
     */
    void setStato(Consegna consegna, StatoConsegna stato) throws IllegalArgumentException ;

    /**
     * set delivery state
     * @param index
     * @param stato
     * @throws IllegalArgumentException
     */
    void setStato(int index, StatoConsegna stato) throws IllegalArgumentException;

    /**
     * Abilita la consegna al ritiro
     * @param consegna
     */
    void abilitaRitiro(Consegna consegna);

    /**
     * Annulla la presa in carico di una consegna da parte di un corriere
     * @param consegna
     * @param corriere
     */
    void annullaPresaInCarico(Consegna consegna, Corriere corriere);
}
