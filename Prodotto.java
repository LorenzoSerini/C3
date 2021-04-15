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

import java.util.UUID;

public class Prodotto {
    private String id;
    private String descrizione;
    private double prezzo;
    private boolean disponibilita;

    public Prodotto() { }

    public Prodotto(String descrizione, double prezzo, String id) {
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.disponibilita = true;
        if(id!=null){
            this.id = id;
        }else this.id= UUID.randomUUID().toString();
    }

    public Prodotto(String descrizione, double prezzo) {
        this(descrizione, prezzo, null);
    }

    /**
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * set id
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return description
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * set description
     * @param descrizione
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     *
     * @return price
     */
    public double getPrezzo() {
        return Math.round(prezzo * 100.0) / 100.0;
    }

    /**
     * set price
     * @param prezzo
     */
    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    /**
     * set availability
     * @param disponibilita
     */
    public void setDisponibilita(boolean disponibilita) {this.disponibilita=disponibilita; }

    /**
     *
     * @return availability
     */
    public boolean getDisponibilita() {
        return this.disponibilita;
    }


    public String toString(){
        return "Prodotto: ["+this.descrizione+"] Prezzo: ["+this.prezzo+"]";
    }
}
