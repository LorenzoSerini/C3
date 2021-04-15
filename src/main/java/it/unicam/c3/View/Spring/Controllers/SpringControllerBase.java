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

package it.unicam.c3.View.Spring.Controllers;

import it.unicam.c3.Anagrafica.Cliente;
import it.unicam.c3.Anagrafica.Commerciante;
import it.unicam.c3.Anagrafica.Corriere;
import it.unicam.c3.Controller.ControllerCliente;
import it.unicam.c3.Controller.ControllerCommerciante;
import it.unicam.c3.Controller.ControllerCorriere;

import javax.servlet.http.HttpSession;

public class SpringControllerBase {

    @SuppressWarnings("unchecked")
    private static <T> T getSessionAttribute (
            HttpSession session,
            String attribute,
            Class<T> type )
    {
        Object o = session.getAttribute(attribute);
        if (!type.isInstance(o)) return null;
        return (T)o;
    }
    
    protected static Commerciante getCommerciante(HttpSession session)
    {
        return getSessionAttribute (
                session,
                "utente",
                Commerciante.class );
    }

    protected static Cliente getCliente(HttpSession session)
    {
        return getSessionAttribute (
                session,
                "utente",
                Cliente.class );
    }

    protected static Corriere getCorriere(HttpSession session)
    {
        return getSessionAttribute (
                session,
                "utente",
                Corriere.class );
    }

    protected static ControllerCommerciante getControllerCommerciante(HttpSession session)
    {
        return getSessionAttribute (
                session,
                "controller",
                ControllerCommerciante.class );
    }

    protected static ControllerCliente getControllerCliente(HttpSession session)
    {
        return getSessionAttribute (
                session,
                "controller",
                ControllerCliente.class );
    }

    protected static ControllerCorriere getControllerCorriere(HttpSession session)
    {
        return getSessionAttribute (
                session,
                "controller",
                ControllerCorriere.class );
    }
}
