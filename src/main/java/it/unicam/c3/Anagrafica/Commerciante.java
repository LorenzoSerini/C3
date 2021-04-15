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


package it.unicam.c3.Anagrafica;

import it.unicam.c3.Commercio.PuntoVendita;

import java.util.LinkedList;
import java.util.List;

public class Commerciante extends Utente {
    private List<PuntoVendita> puntiVendita;

    public Commerciante() {
        this.puntiVendita = new LinkedList<>();
    }

    public Commerciante(List<PuntoVendita> puntiVendita) {
        this.puntiVendita.addAll(puntiVendita);
    }

    public Commerciante(String nome, String cognome, String email, String password) {
        this.setNome(nome);
        this.setCognome(cognome);
        this.setEmail(email);
        this.setPassword(password);
        this.puntiVendita = new LinkedList<>();
    }

    public List<PuntoVendita> getPuntiVendita() {
        return puntiVendita;
    }

    public void addPuntoVendita(String nome, String posizione) {
        puntiVendita.add(new PuntoVendita(this,nome, posizione));
    }

    public void addPuntoVendita(String id, String nome, String posizione) {
        puntiVendita.add(new PuntoVendita(this,nome, posizione,id));
    }
}
