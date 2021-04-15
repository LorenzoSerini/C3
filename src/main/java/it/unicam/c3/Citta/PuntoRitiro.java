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


package it.unicam.c3.Citta;

import java.util.UUID;

public class PuntoRitiro {
    private String id;
    private String indirizzo;
    private int capienza;
    private int occupati;

    public PuntoRitiro() { }

    public PuntoRitiro(String indirizzo, int capienza, String id) {
        this.indirizzo = indirizzo;
        this.capienza = capienza;
        if(id!=null) {
            this.id = id;
        } else this.id= UUID.randomUUID().toString();
    }

    public PuntoRitiro(String indirizzo, int capienza){
        this(indirizzo, capienza, null);
    }


    /**
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * set the id
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return address
     */
    public String getIndirizzo(){
        return this.indirizzo;
    }

    /**
     * set address
     * @param indirizzo
     */
    public void setIndirizzo(String indirizzo){
        this.indirizzo=indirizzo;
    }

    /**
     * return available slots
     * @return
     */
    public int getSlotDisponibili(){
        return this.capienza-this.occupati;
    }

    /**
     *
     * @return capacity
     */
    public int getCapienza(){
        return this.capienza;
    }

    /**
     *
     * @return occupied slots
     */
    public int getSlotOccupati(){ return this.occupati;}

    /**
     * increase occupied slots
     * @param amount
     */
    public void incrementOccupati(int amount){
        this.occupati=this.occupati+amount;
    }

    /**
     * decreases occupied slots
     * @param amount
     */
    public void decrementOccupati(int amount){
        this.occupati=this.occupati-amount;
    }

    public String toString(){
        return this.indirizzo+" Capienza: ["+this.capienza+"] Slot Disponibili:["+getSlotDisponibili()+"]";
    }
}
