package it.unicam.c3.View.Spring.Controllers;

import it.unicam.c3.Anagrafica.Cliente;
import it.unicam.c3.Commercio.Prodotto;
import it.unicam.c3.Commercio.PuntoVendita;
import it.unicam.c3.Consegne.Consegna;
import it.unicam.c3.Consegne.StatoConsegna;
import it.unicam.c3.Controller.ControllerCliente;
import it.unicam.c3.Ordini.Ordine;
import it.unicam.c3.View.Spring.OffertaGenerica;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("cliente")
public class SpringControllerCliente extends SpringControllerBase {

    private Cliente cliente;
    private ControllerCliente controller;

    private boolean isUnauthorized(HttpSession session) {
        cliente = getCliente(session);
        controller = getControllerCliente(session);

        return cliente == null || controller == null;
    }

    public static class ProdottoView {
        private Prodotto prodotto;
        private int inCarrello;

        public ProdottoView() {}

        public ProdottoView(Prodotto prodotto, int inCarrello) {
            this.prodotto = prodotto;
            this.inCarrello = inCarrello;
        }

        public Prodotto getProdotto() {
            return prodotto;
        }

        public void setProdotto(Prodotto prodotto) {
            this.prodotto = prodotto;
        }

        public int getInCarrello() {
            return inCarrello;
        }

        public void setInCarrello(int inCarrello) {
            this.inCarrello = inCarrello;
        }
    }

    @GetMapping
    public String getHome(HttpSession session, Model model) {
        if (isUnauthorized(session)) return "redirect:/auth";

        model.addAttribute("cliente", cliente);

        List<PuntoVendita> puntiVendita = controller.getPuntiVendita();
        model.addAttribute("puntiVendita", puntiVendita);

        List<OffertaGenerica> offerte = new ArrayList<>();
        controller.getOfferte().forEach(
                (pv, O) -> O.forEach(
                        o -> offerte.add(new OffertaGenerica(o, pv))
                )
        );
        model.addAttribute("offerte", offerte);

        return "/cliente/home";
    }

    @GetMapping("puntoVendita")
    public String getPuntoVendita(HttpSession session,
                                  Model model,
                                  @RequestParam("id") String id)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        PuntoVendita puntoVendita = controller.getPuntiVendita()
                .stream().filter(pv -> pv.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (puntoVendita == null)
            return "/not-found";

        model.addAttribute("puntoVendita", puntoVendita);

        List<OffertaGenerica> offerte = controller.getOfferte(puntoVendita)
                .stream().map(OffertaGenerica::new).collect(Collectors.toList());
        model.addAttribute("offerte", offerte);

        List<Prodotto> prodotti = puntoVendita.getProdottiDisponibili();
        List<Prodotto> prodottiNelCarrello = controller.getCarrello().get(puntoVendita);
        model.addAttribute("prodotti", prodotti.stream()
        .map(
                p -> new ProdottoView(p, prodottiNelCarrello == null ? 0 :
                        (int)prodottiNelCarrello
                                .stream()
                                .filter(p2 -> p2.equals(p))
                                .count()
                )
        ).collect(Collectors.toList()));

        return "/cliente/puntoVendita";
    }

    @GetMapping("aggiungiAlCarrello")
    public String aggiungiAlCarrello(HttpSession session,
                                     Model model,
                                     @RequestParam("idPuntoVendita") String idPuntoVendita,
                                     @RequestParam("idProdotto") String idProdotto)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        PuntoVendita puntoVendita = controller.getPuntiVendita()
                .stream().filter(pv -> pv.getId().equals(idPuntoVendita))
                .findFirst()
                .orElse(null);

        if (puntoVendita == null) return "/not-found";

        Prodotto prodotto = puntoVendita.getProdottiDisponibili()
                .stream().filter(p -> p.getId().equals(idProdotto))
                .findFirst()
                .orElse(null);

        if (prodotto == null) return "/not-found";

        controller.addInCarrello(puntoVendita, prodotto);
        return "redirect:/cliente/puntoVendita?id=" + idPuntoVendita;
    }

    @GetMapping("carrello")
    public String getCarrello(HttpSession session, Model model)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        Map<PuntoVendita, List<Prodotto>> carrello = controller.getCarrello();
        model.addAttribute("carrello", carrello);

        double totale = 0;
        for (PuntoVendita pv : carrello.keySet())
            for (Prodotto p : carrello.get(pv))
                totale += p.getPrezzo();
        totale = Math.round(totale * 100.0) / 100.0;
        model.addAttribute("totale", totale);

        return "/cliente/carrello";
    }

    @GetMapping("carrello/clear")
    public String clearCarrello(HttpSession session) {
        if (isUnauthorized(session)) return "redirect:/auth";

        controller.clearCarrello();

        return "redirect:/cliente/carrello";
    }

    @GetMapping("carrello/ordina")
    public String ordinaCarrello(HttpSession session) {
        if (isUnauthorized(session)) return "redirect:/auth";

        try {
            controller.ordinaProdotti();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/cliente/ordini";
    }

    @GetMapping("ordini")
    public String getOrdini(HttpSession session, Model model) {
        if (isUnauthorized(session)) return "redirect:/auth";

        List<Ordine> ordini = controller.getOrdini();
        model.addAttribute("ordini", ordini);

        return "/cliente/ordini";
    }

    @GetMapping("consegne")
    public String getConsegneEffettuate(HttpSession session, Model model) {
        if (isUnauthorized(session)) return "redirect:/auth";

        List<Consegna> consegne = controller.getConsegne(StatoConsegna.EFFETTUATA);
        model.addAttribute("consegne", consegne);

        return "/cliente/consegne";
    }

    @GetMapping("ritiro")
    public String getDettagliRitiro(HttpSession session, Model model,
                                    @RequestParam("id") String id)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        Consegna consegna = controller.getConsegne()
                .stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (consegna == null) return "/not-found";

        String[] dettagliRitiro = controller.getDettagliRitiro(consegna);
        model.addAttribute("id", dettagliRitiro[0]);
        model.addAttribute("indirizzo", dettagliRitiro[1]);
        model.addAttribute("ritirabile", consegna.isRitirabile());

        return "/cliente/ritiro";
    }

    @GetMapping("segnalaRitiro")
    public String segnalaRitiroMerce(HttpSession session,
                                     @RequestParam("id") String idConsegna)
    {
        if (isUnauthorized(session)) return "redirect:/auth";

        Consegna consegna = controller.getConsegne()
                .stream()
                .filter(c -> c.getId().equals(idConsegna))
                .findFirst()
                .orElse(null);

        if (consegna == null) return "/not-found";

        try {
            controller.setConsegnaRitirata(consegna);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/cliente";
    }
}
