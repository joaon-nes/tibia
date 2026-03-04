package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.dto.EquipamentoFilterDTO;
import com.joao.tibia_scrapper.model.Equipamento;
import com.joao.tibia_scrapper.repository.EquipamentoRepository;
import com.joao.tibia_scrapper.model.HuntRecord;
import com.joao.tibia_scrapper.repository.HuntRecordRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class EquipamentoViewController {

    @Autowired
    private EquipamentoRepository repository;

    @Autowired
    private HuntRecordRepository huntRecordRepository;

    @GetMapping({ "/", "/home" })
    public String home(Model model) {
        HuntRecord ultimaHunt = huntRecordRepository.findFirstByOrderByDataRegistroDesc();
        
        model.addAttribute("ultimaHunt", ultimaHunt);
        
        return "home";
    }

    @GetMapping("/enciclopedia/equipamentos")
    public String listarEquipamentos(EquipamentoFilterDTO filtro, HttpSession session, Model model) {

        if (filtro.categoria() != null && !filtro.categoria().isEmpty()) {
            List<String> categoriasLista = Arrays.asList(filtro.categoria().split(","));

            List<Equipamento> itens = repository.findComFiltrosAvancados(
                    categoriasLista,
                    filtro.level(),
                    filtro.vocacao(),
                    filtro.elemento(),
                    filtro.protecao(),
                    filtro.atributos(),
                    filtro.bonusEspecial(),
                    filtro.slots(),
                    filtro.tier());

            model.addAttribute("itens", itens);
            model.addAttribute("titulo", filtro.categoria());
            model.addAttribute("f", filtro);
        } else {
            model.addAttribute("exibirMenu", true);
        }

        return "equipamentos";
    }

    @GetMapping("/builder")
    public String abrirMontarSet(HttpSession session, Model model) {
        return "builder";
    }
}