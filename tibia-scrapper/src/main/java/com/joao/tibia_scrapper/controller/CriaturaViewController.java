package com.joao.tibia_scrapper.controller;

import com.joao.tibia_scrapper.model.Criatura;
import com.joao.tibia_scrapper.repository.CriaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CriaturaViewController {

    @Autowired
    private CriaturaRepository criaturaRepository;

    private static final int PAGE_SIZE = 20;

    @GetMapping("/enciclopedia/criaturas")
    public String listarCriaturas(
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        List<String> categorias = criaturaRepository.findDistinctCategorias();
        model.addAttribute("categorias", categorias);

        if (categoria != null && !categoria.isEmpty()) {
            PageRequest pageable = PageRequest.of(page, PAGE_SIZE);
            Page<Criatura> paginacao = criaturaRepository.findByCategoriaOrderByNome(categoria, pageable);

            model.addAttribute("criaturas", paginacao.getContent());
            model.addAttribute("categoriaSelecionada", categoria);
            model.addAttribute("paginaAtual", paginacao.getNumber());
            model.addAttribute("totalPaginas", paginacao.getTotalPages());
            model.addAttribute("totalCriaturas", paginacao.getTotalElements());
            model.addAttribute("temAnterior", paginacao.hasPrevious());
            model.addAttribute("temProxima", paginacao.hasNext());
        } else {
            model.addAttribute("mensagemInicial", "Selecione uma categoria ao lado para explorar o bestiário.");
        }

        return "criaturas";
    }
}