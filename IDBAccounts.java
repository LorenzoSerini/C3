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

import it.unicam.c3.Anagrafica.Cliente;
import it.unicam.c3.Anagrafica.Commerciante;
import it.unicam.c3.Anagrafica.Corriere;

import java.sql.SQLException;
import java.util.List;

public interface IDBAccounts {

    /**
     *
     * @return lista dei commercianti
     * @throws SQLException
     */
    List<Commerciante> getCommercianti() throws Exception;

    /**
     *
     * @return lista dei clienti
     * @throws SQLException
     */
    List<Cliente> getClienti() throws Exception;

    /**
     *
     * @return lista dei corrieri
     * @throws SQLException
     */
    List<Corriere> getCorrieri() throws Exception;

    /**
     * Salva un account commerciante
     * @param commerciante
     * @throws SQLException
     */
    void registerCommerciante(Commerciante commerciante) throws Exception;

    /**
     * Salva un account cliente
     * @param cliente
     * @throws Exception
     */
    void registerCliente(Cliente cliente) throws Exception;

    /**
     * Salva un account corriere
     * @param corriere
     * @throws SQLException
     */
    void registerCorriere(Corriere corriere) throws Exception;
}
