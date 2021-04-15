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

package it.unicam.c3.Controller;

import it.unicam.c3.Anagrafica.Cliente;
import it.unicam.c3.Anagrafica.Commerciante;
import it.unicam.c3.Anagrafica.Corriere;
import it.unicam.c3.Citta.CentroCittadino;
import it.unicam.c3.Consegne.GestoreConsegne;
import it.unicam.c3.Exception.EmailSyntaxException;
import it.unicam.c3.Ordini.GestoreOrdini;
import it.unicam.c3.Persistence.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ControllerAutenticazione {
    private IDBAccounts dbAccounts;


    public ControllerAutenticazione(IDBAccounts dbAccounts, IDBPuntiRitiro dbPuntiRitiro, IDBOrdini dbOrdini, IDBConsegne dbConsegne ) throws Exception {
        this.dbAccounts=dbAccounts;
        CentroCittadino.getInstance(this.dbAccounts.getCommercianti(), this.dbAccounts.getClienti(), this.dbAccounts.getCorrieri(), dbPuntiRitiro.getPuntiRitiro());
        GestoreOrdini.getInstance(dbOrdini.getOrdini());
        GestoreConsegne.getInstance(dbConsegne.getConsegne());
    }

    public ControllerAutenticazione() throws Exception {
        this(new DBAccounts(), new DBPuntiRitiro(), new DBOrdini(), new DBConsegne());
    }

    private boolean emailController(String email){
        String espressione = "^[0-9a-z]([-_.]?[0-9a-z])*@[0-9a-z]([-.]?[0-9a-z])*\\.[a-z]{2,4}$";
        Pattern p = Pattern.compile(espressione);

        Matcher m = p.matcher(email);

        return m.matches();
        }

    public Commerciante autenticaCommerciante(String email, String password) {
       List<Commerciante> commercianti = CentroCittadino.getInstance()
                .getCommercianti()
                .stream()
                .filter(commerciante ->
                        commerciante.getEmail().equals(email) &&
                        commerciante.getPassword().equals(password))
                .collect(Collectors.toList());
        if (commercianti.size() < 1) {
            return null;
        }
        return commercianti.get(0);
    }

    public Cliente autenticaCliente(String email, String password) {
        List<Cliente> clienti = CentroCittadino.getInstance()
                .getClienti()
                .stream()
                .filter(cliente ->
                        cliente.getEmail().equals(email) &&
                                cliente.getPassword().equals(password))
                .collect(Collectors.toList());
        if (clienti.size() < 1) {
            return null;
        }
        return clienti.get(0);
    }

    public Corriere autenticaCorriere(String email, String password) {
        List<Corriere> corrieri = CentroCittadino.getInstance()
                .getCorrieri()
                .stream()
                .filter(corriere ->
                        corriere.getEmail().equals(email) &&
                        corriere.getPassword().equals(password))
                .collect(Collectors.toList());
        if (corrieri.size() < 1) {
            return null;
        }
        return corrieri.get(0);
    }


    public enum TipoUtente {
        COMMERCIANTE, CLIENTE, CORRIERE
    }

    public void registra(String nome, String cognome, String email, String password, TipoUtente tipo) throws Exception{
        if (emailController(email)) {
            switch (tipo) {
                case CLIENTE:
                    Cliente cliente = new Cliente(nome, cognome, email, password);
                    CentroCittadino.getInstance().addCliente(cliente);
                    this.dbAccounts.registerCliente(cliente);
                    break;
                case COMMERCIANTE:
                    Commerciante commerciante = new Commerciante(nome, cognome, email, password);
                    CentroCittadino.getInstance().addCommerciante(commerciante);
                    this.dbAccounts.registerCommerciante(commerciante);
                    break;
                case CORRIERE:
                    Corriere corriere = new Corriere(nome, cognome, email, password);
                    CentroCittadino.getInstance().addCorriere(corriere);
                    this.dbAccounts.registerCorriere(corriere);
                    break;
            }
        }else throw new EmailSyntaxException();
    }
}
