package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.dto.EquipamentoFilterDTO;
import com.joao.tibia_scrapper.model.Equipamento;
import com.joao.tibia_scrapper.model.TibiaCoin;
import com.joao.tibia_scrapper.repository.EquipamentoRepository;
import com.joao.tibia_scrapper.repository.TibiaCoinRepository;
import com.joao.tibia_scrapper.service.TibiaCoinService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;

@Controller
public class EquipamentoViewController {

    @Autowired
    private EquipamentoRepository repository;
    @Autowired
    private TibiaCoinRepository tibiaCoinRepository;
    @Autowired
    private TibiaCoinService tibiaCoinService;

    @ModelAttribute("ultimaAtualizacao")
    public String adicionarDataAtualizacao() {
        return tibiaCoinService.getUltimaAtualizacaoFormatada();
    }

    @ModelAttribute("listaCoins")
    public List<TibiaCoin> popularMundos() {
        return tibiaCoinRepository.findAll();
    }

    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }

    @GetMapping("/enciclopedia")
    public String listarEquipamentos(EquipamentoFilterDTO filtro, HttpSession session, Model model) {
        
        List<TibiaCoin> mundosOrdenados = tibiaCoinRepository.findAllByOrderByMundoAsc();
        model.addAttribute("mundos", mundosOrdenados);

        if (filtro.mundo() != null && !filtro.mundo().isEmpty()) {
            session.setAttribute("mundoSelecionado", filtro.mundo());
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

        if (filtro.categoria() != null && !filtro.categoria().isEmpty()) {
            List<String> categoriasLista = Arrays.asList(filtro.categoria().split(","));
            
            List<Equipamento> itens = repository.findComFiltrosAvancados(
                    categoriasLista, filtro.level(), filtro.vocacao(), filtro.protecao(), 
                    filtro.elemento(), filtro.bonus(), filtro.atributos(), 
                    filtro.range(), filtro.slots(), filtro.tier()
            );

            model.addAttribute("itens", itens);
            model.addAttribute("titulo", filtro.categoria());
            model.addAttribute("f", filtro); 
        } else {
            model.addAttribute("exibirMenu", true);
        }

        return "lista-equipamentos";
    }

    @GetMapping("set-builder")
    public String abrirMontarSet(HttpSession session, Model model) {
        List<TibiaCoin> mundos = tibiaCoinRepository.findAllByOrderByMundoAsc();
        model.addAttribute("mundos", mundos);

        String mundoSelecionado = (String) session.getAttribute("mundoSelecionado");
        if (mundoSelecionado != null) {
            tibiaCoinRepository.findByMundo(mundoSelecionado).ifPresent(tc -> {
                model.addAttribute("valorTc", tc.getPrecoMedio());
                model.addAttribute("ultimaVerificacao", tc.getTempoReferenciaSite());
                model.addAttribute("mundoSelecionado", mundoSelecionado);
            });
        }

        return "montar-set";
    }
}