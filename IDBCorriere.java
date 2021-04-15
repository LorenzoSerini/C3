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

import it.unicam.c3.Citta.PuntoRitiro;
import it.unicam.c3.Consegne.Consegna;

import java.sql.SQLException;


public interface IDBCorriere {

    /**
     * Aggiorna il fatto che la consegna è stata presa in carico
     * @param consegna
     * @throws Exception
     */
    void updateConsegnaInCarico(Consegna consegna) throws Exception;

    /**
     * Aggiorna il corriere della consegna
     * @param consegna
     * @throws Exception
     */
    void updateCorriere(Consegna consegna)throws Exception;

    /**
     * Aggiorna il fatto che la consegna è stata effettuata
     * @param consegna
     * @throws Exception
     */
    void updateConsegnaEffettuata(Consegna consegna) throws Exception;

    /**
     * Aggiorna il fatto che la consegna è in attesa
     * @param consegna
     * @throws Exception
     */
    void updateConsegnaInAttesa(Consegna consegna) throws Exception;

    /**
     * Aggiorna il fatto che il corriere ha annullato la presa in carico della
     * consegna (set corriere a null)
     * @param consegna
     * @throws Exception
     */
    void updateCorriereNullo(Consegna consegna) throws Exception;

    /**
     * Aggiorna slot disponibili
     * @param pr
     * @throws SQLException
     */
    void updateDisponibilita(PuntoRitiro pr) throws Exception;

}
