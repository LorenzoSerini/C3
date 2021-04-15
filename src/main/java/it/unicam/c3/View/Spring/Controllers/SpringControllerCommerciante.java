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

import it.unicam.c3.Anagrafica.Commerciante;
import it.unicam.c3.Citta.CentroCittadino;
import it.unicam.c3.Citta.PuntoRitiro;
import it.unicam.c3.Commercio.IOfferta;
import it.unicam.c3.Commercio.OffertaATempo;
import it.unicam.c3.Commercio.Prodotto;
import it.unicam.c3.Commercio.PuntoVendita;
import it.unicam.c3.Consegne.Consegna;
import it.unicam.c3.Controller.ControllerCommerciante;
import it.unicam.c3.Ordini.GestoreOrdini;
import it.unicam.c3.Ordini.Ordine;
import it.unicam.c3.Ordini.StatoOrdine;
import it.unicam.c3.View.Spring.OffertaGenerica;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"DuplicatedCode", "SpringMVCViewInspection"})
@Controller
@RequestMapping("commerciante")
public class SpringControllerCommerciante extends SpringControllerBase {

    private Commerciante commerciante;
    private ControllerCommerciante controller;

    private boolean isUnauthorized(HttpSession session) {
        commerciante = getCommerciante(session);
        controller = getControllerCommerciante(session);

        return commerciante == null || controller == null;
    }

    @GetMapping
    public String getHomeCommerciante (HttpSession session, Model model)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        model.addAttribute("emailCommerciante", commerciante.getEmail());
        model.addAttribute("puntiVendita", controller.getPuntiVendita());
        model.addAttribute("ordiniInAttesa",
                controller.getOrdini(StatoOrdine.IN_ATTESA));

        return "/commerciante/home";
    }

    @GetMapping("puntoVendita")
    public String getPuntoVendita (HttpSession session, Model model,
                                   @RequestParam("id") String id)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        PuntoVendita puntoVendita = controller.getPuntiVendita()
                .stream()
                .filter(pv -> pv.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (puntoVendita == null)
            return "/not-found";

        model.addAttribute("puntoVendita", puntoVendita);
        List<OffertaGenerica> offerte = puntoVendita.getOfferte()
                .stream()
                .filter(o -> !
                        (o instanceof OffertaATempo &&
                                ((OffertaATempo) o).getScadenza().isBefore(LocalDate.now()))
                ) // (non è scaduta)
                .map(OffertaGenerica::new)
                .collect(Collectors.toList());
        model.addAttribute("offerte", offerte);
        return "/commerciante/puntoVendita";
    }

    @GetMapping("puntoVendita/elimina")
    public String eliminaPuntoVendita (HttpSession session,
                                       @RequestParam("id") String id)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        PuntoVendita puntoVendita = controller.getPuntiVendita()
                .stream()
                .filter(pv -> pv.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (puntoVendita == null)
            return "/not-found";

        try {
            controller.removePuntoVendita(puntoVendita);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/commerciante";
    }

    @GetMapping("puntoVendita/eliminaProdotto")
    public String eliminaProdotto(HttpSession session,
                                  @RequestParam("idPuntoVendita") String idPuntoVendita,
                                  @RequestParam("idProdotto") String idProdotto)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        PuntoVendita puntoVendita = controller.getPuntiVendita()
                .stream()
                .filter(pv -> pv.getId().equals(idPuntoVendita))
                .findFirst()
                .orElse(null);

        if (puntoVendita == null) return "/not-found";

        Prodotto prodotto = puntoVendita.getProdotti()
                .stream()
                .filter(p -> p.getId().equals(idProdotto))
                .findFirst()
                .orElse(null);

        if (prodotto == null) return "/not-found";

        try {
            controller.removeProdotto(puntoVendita,prodotto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/commerciante/puntoVendita?id=" + idPuntoVendita;
    }

    @GetMapping("puntoVendita/cambiaDisponibilita")
    public String cambiaDisponibilitaProdotto (
            HttpSession session,
            @RequestParam("idPuntoVendita") String idPuntoVendita,
            @RequestParam("idProdotto") String idProdotto)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        PuntoVendita puntoVendita = controller.getPuntiVendita()
                .stream()
                .filter(pv -> pv.getId().equals(idPuntoVendita))
                .findFirst()
                .orElse(null);

        if (puntoVendita == null) return "/not-found";

        Prodotto prodotto = puntoVendita.getProdotti()
                .stream()
                .filter(p -> p.getId().equals(idProdotto))
                .findFirst()
                .orElse(null);

        if (prodotto == null) return "/not-found";

        try {
            controller.cambiaDisponibilitaProdotto(prodotto, !prodotto.getDisponibilita());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/commerciante/puntoVendita?id=" + idPuntoVendita;
    }

    @GetMapping("puntoVendita/aggiungiProdotto")
    public String getAggiungiProdotto (HttpSession session,
                                       Model model,
                                       @RequestParam("idPuntoVendita") String idPuntoVendita)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        PuntoVendita puntoVendita = controller.getPuntiVendita()
                .stream()
                .filter(pv -> pv.getId().equals(idPuntoVendita))
                .findFirst()
                .orElse(null);

        if (puntoVendita == null) return "/not-found";

        model.addAttribute("puntoVendita", puntoVendita);
        return "/commerciante/aggiungiProdotto";
    }

    @PostMapping("puntoVendita/aggiungiProdotto")
    public String doAggiungiProdotto  (HttpSession session,
                                       @RequestParam("idPuntoVendita") String idPuntoVendita,
                                       @RequestParam("nome") String nome,
                                       @RequestParam("prezzo") double prezzo)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        PuntoVendita puntoVendita = controller.getPuntiVendita()
                .stream()
                .filter(pv -> pv.getId().equals(idPuntoVendita))
                .findFirst()
                .orElse(null);

        if (puntoVendita == null)
            return "/not-found";

        try {
            controller.addProdotto(puntoVendita,nome,prezzo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/commerciante/puntoVendita?id=" + idPuntoVendita;
    }

    @GetMapping("puntoVendita/eliminaOfferta")
    public String eliminaOfferta   (HttpSession session,
                                    @RequestParam("idPuntoVendita") String idPuntoVendita,
                                    @RequestParam("idOfferta") String idOfferta)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        PuntoVendita puntoVendita = controller.getPuntiVendita()
                .stream()
                .filter(pv -> pv.getId().equals(idPuntoVendita))
                .findFirst()
                .orElse(null);

        if (puntoVendita == null) return "/not-found";

        IOfferta offerta = puntoVendita.getOfferte()
                .stream()
                .filter(o -> o.getId().equals(idOfferta))
                .findFirst()
                .orElse(null);

        if (offerta == null) return "/not-found";


        try {
            controller.removeOfferta(puntoVendita,offerta);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/commerciante/puntoVendita?id=" + idPuntoVendita;
    }

    @GetMapping("puntoVendita/aggiungiOfferta")
    public String getAggiungiOfferta  (HttpSession session,
                                       Model model,
                                       @RequestParam("idPuntoVendita") String idPuntoVendita)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        PuntoVendita puntoVendita = controller.getPuntiVendita()
                .stream()
                .filter(pv -> pv.getId().equals(idPuntoVendita))
                .findFirst()
                .orElse(null);

        if (puntoVendita == null) return "/not-found";

        model.addAttribute("puntoVendita", puntoVendita);
        return "/commerciante/aggiungiOfferta";
    }

    @PostMapping("puntoVendita/aggiungiOfferta")
    public String doAggiungiOfferta  (HttpSession session,
                                      @RequestParam("idPuntoVendita") String idPuntoVendita,
                                      @RequestParam("descrizione") String descrizione,
                                      @RequestParam("importo") String importo,
                                      @RequestParam(value = "scadenza", required = false) String scadenza)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        PuntoVendita puntoVendita = controller.getPuntiVendita()
                .stream()
                .filter(pv -> pv.getId().equals(idPuntoVendita))
                .findFirst()
                .orElse(null);

        if (puntoVendita == null) return "/not-found";

        if (scadenza != null && !scadenza.isEmpty()) {
            LocalDate date = LocalDate.parse(scadenza, DateTimeFormatter.ISO_DATE_TIME);


            try {
                controller.addOfferta(puntoVendita,descrizione,importo, date);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else {

            try {
                controller.addOfferta(puntoVendita,descrizione,importo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "redirect:/commerciante/puntoVendita?id=" + idPuntoVendita;
    }

    @GetMapping("ordini")
    public String getOrdini (HttpSession session,
                             Model model,
                             @RequestParam(value = "stato", required = false) String stato)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        List<Ordine> ordini;

        if (stato != null && !stato.isEmpty() && !stato.equals("QUALSIASI")) {
            ordini = controller.getOrdini(StatoOrdine.valueOf(stato));
        }
        else ordini = controller.getOrdini();

        model.addAttribute("ordini", ordini);
        model.addAttribute("filtro", stato);
        return "/commerciante/ordini";
    }

    @GetMapping("ordine")
    public String getOrdine (HttpSession session,
                             Model model,
                             @RequestParam("id") String id)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        List<Ordine> ordini = GestoreOrdini.getInstance().getOrdini(commerciante);
        Ordine ordine = ordini.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (ordine == null) return "/not-found";

        model.addAttribute("ordine", ordine);

        if (ordine.getStato() == StatoOrdine.IN_ATTESA) {
            model.addAttribute("puntiRitiro", controller.getPuntiRitiroDisponibili());
        }

        return "/commerciante/ordine";
    }

    @GetMapping("ordine/accetta")
    public String accettaOrdine(HttpSession session,
                                @RequestParam("id") String id,
                                @RequestParam("idPuntoRitiro") String idPuntoRitiro)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        List<Ordine> ordini = GestoreOrdini.getInstance().getOrdini(commerciante);
        Ordine ordine = ordini.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (ordine == null) return "/not-found";

        PuntoRitiro puntoRitiro = CentroCittadino.getInstance().getPuntiRitiro()
                .stream()
                .filter(pr -> pr.getId().equals(idPuntoRitiro))
                .findFirst()
                .orElse(null);

        if (puntoRitiro == null) return "/not-found";

        try {
            controller.accettaOrdine(ordine, puntoRitiro);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/commerciante/ordine?id=" + ordine.getId();
    }

    @GetMapping("ordine/rifiuta")
    public String rifiutaOrdine(HttpSession session,
                                @RequestParam("id") String id)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        List<Ordine> ordini = GestoreOrdini.getInstance().getOrdini(commerciante);
        Ordine ordine = ordini.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (ordine == null) return "/not-found";

        try {
            controller.rifiutaOrdine(ordine);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/commerciante/ordine?id=" + ordine.getId();
    }

    @GetMapping("aggiungiPuntoVendita")
    public String getAggiungiPuntoVendita(HttpSession session, Model model) {
        if (isUnauthorized(session)) return "redirect:/auth";

        model.addAttribute("commerciante", commerciante);
        return "commerciante/aggiungiPuntoVendita";
    }

    @PostMapping("aggiungiPuntoVendita")
    public String doAggiungiPuntoVendita(HttpSession session,
                                         @RequestParam("nome") String nome,
                                         @RequestParam("posizione") String posizione)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        try {
            controller.addPuntoVendita(posizione, nome);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/commerciante";
    }

    @GetMapping("abilitaRitiroConsegna")
    public String getAbilitaRitiroConsegna(HttpSession session, Model model) {
        if (isUnauthorized(session)) return "redirect:/auth";

        List<Consegna> consegne = controller.getConsegneDaAbilitareAlRitiro();
        model.addAttribute("consegne", consegne);
        return "/commerciante/abilitaRitiroConsegna";
    }

    @PostMapping("abilitaRitiroConsegna")
    public String doAbilitaRitiroConsegna(HttpSession session,
                                          @RequestParam("id") String id)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        List<Consegna> consegne = controller.getConsegneDaAbilitareAlRitiro();
        Consegna consegna = consegne.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (consegna == null) return "/not-found";

        try {
            controller.abilitaRitiro(consegna);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/commerciante/abilitaRitiroConsegna";
    }
}
