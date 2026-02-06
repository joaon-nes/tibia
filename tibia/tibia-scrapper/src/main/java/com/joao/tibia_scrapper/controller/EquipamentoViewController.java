package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.model.Equipamento;
import com.joao.tibia_scrapper.model.TibiaCoin;
import com.joao.tibia_scrapper.repository.EquipamentoRepository;
import com.joao.tibia_scrapper.repository.TibiaCoinRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class EquipamentoViewController {

    @Autowired
    private EquipamentoRepository repository;

    @Autowired
    private TibiaCoinRepository tibiaCoinRepository;

    @GetMapping("/equipamentos")
    public String listarEquipamentos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Integer level,
            @RequestParam(required = false) String vocacao,
            @RequestParam(required = false) String protecao,
            @RequestParam(required = false) String elemento,
            @RequestParam(required = false) String bonus,
            @RequestParam(required = false) String atributos,
            @RequestParam(required = false) String cargas,
            @RequestParam(required = false) String duracao,
            @RequestParam(required = false) Integer range,
            @RequestParam(required = false) Integer slots,
            @RequestParam(required = false) Integer tier,
            @RequestParam(required = false) String mundo,
            HttpSession session,
            Model model) {

        List<TibiaCoin> mundosOrdenados = tibiaCoinRepository.findAllByOrderByMundoAsc();
        model.addAttribute("mundos", mundosOrdenados);

        if (mundo != null && !mundo.isEmpty()) {
            session.setAttribute("mundoSelecionado", mundo);
        }
        String mundoNaSessao = (String) session.getAttribute("mundoSelecionado");
        model.addAttribute("mundoSelecionado", mundoNaSessao);

        if (mundoNaSessao != null) {
            tibiaCoinRepository.findByMundo(mundoNaSessao).ifPresent(tc -> {
                model.addAttribute("valorTc", tc.getPrecoMedio());

                String tempo = (tc.getTempoReferenciaSite() != null) ? tc.getTempoReferenciaSite() : "Sincronizando...";
                model.addAttribute("ultimaVerificacao", tempo);
                model.addAttribute("mundoAtivoNome", tc.getMundo());
            });
        }

        if (categoria != null && !categoria.isEmpty()) {
            List<Equipamento> itens = repository.findComFiltrosAvancados(
                    categoria, level, vocacao, protecao, elemento, bonus, atributos, cargas, duracao, range, slots,
                    tier);

            model.addAttribute("itens", itens);
            model.addAttribute("titulo", categoria);
            model.addAttribute("f", new FilterDTO(level, vocacao, protecao, elemento, bonus, atributos, cargas, duracao,
                    range, slots, tier));
        } else {
            model.addAttribute("exibirMenu", true);
        }

        return "lista-equipamentos";
    }

    public record FilterDTO(Integer level, String vocacao, String protecao, String elemento, String bonus,
            String atributos, String cargas, String duracao, Integer range, Integer slots, Integer tier) {
    }
}