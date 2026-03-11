package com.joao.tibia_scrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joao.tibia_scrapper.dto.HuntingSessionDTO;
import com.joao.tibia_scrapper.model.HuntRecord;
import com.joao.tibia_scrapper.model.Usuario;
import com.joao.tibia_scrapper.repository.HuntRecordRepository;
import com.joao.tibia_scrapper.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/hunts")
@RequiredArgsConstructor
public class HuntController {

    private final UsuarioRepository usuarioRepository;
    private final HuntRecordRepository huntRecordRepository;
    private final ObjectMapper objectMapper;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @GetMapping
    public String exibirPaginaHunts(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("todasHunts", huntRecordRepository.findAllByOrderByDataRegistroDesc());

        if (userDetails != null) {
            usuarioRepository.findByUsername(userDetails.getUsername()).ifPresent(usuario -> {
                model.addAttribute("usuario", usuario);
                model.addAttribute("minhasHunts", huntRecordRepository.findByUsuarioOrderByDataRegistroDesc(usuario));
            });
        }
        return "hunts";
    }

    @PostMapping("/adicionar")
    public String adicionarHunt(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("nomeHunt") String nomeHunt,
            @RequestParam("jsonText") String jsonText,
            @RequestParam(value = "meuSetId", defaultValue = "1") Integer meuSetId,
            @RequestParam(value = "partyMembers", required = false) List<String> partyMembers) {

        if (userDetails == null)
            return "redirect:/login";

        Usuario currentUser = usuarioRepository.findByUsername(userDetails.getUsername()).orElse(null);
        if (currentUser == null)
            return "redirect:/login";

        try {
            HuntingSessionDTO sessionData = objectMapper.readValue(jsonText, HuntingSessionDTO.class);
            List<Map<String, Object>> partySnapshotList = new ArrayList<>();

            partySnapshotList.add(extrairDadosJogadorParaSnapshot(currentUser, meuSetId, currentUser.getUsername()));

            if (partyMembers != null) {
                for (String memberName : partyMembers) {
                    if (memberName != null && !memberName.trim().isEmpty()) {
                        Optional<Usuario> memberOpt = usuarioRepository.findByCharNameIgnoreCase(memberName.trim());
                        if (memberOpt.isPresent()) {
                            partySnapshotList
                                    .add(extrairDadosJogadorParaSnapshot(memberOpt.get(), 1, memberName.trim()));
                        } else {
                            Map<String, Object> mData = new HashMap<>();
                            mData.put("name", memberName.trim());
                            mData.put("vocation", "Desconhecida");
                            mData.put("level", "?");
                            mData.put("activeSetIndex", "-");
                            mData.put("setsJson", null);
                            partySnapshotList.add(mData);
                        }
                    }
                }
            }

            HuntRecord record = new HuntRecord();
            record.setUsuario(currentUser);
            record.setTitulo(nomeHunt);
            record.setSessionStart(sessionData.getSessionStart());
            record.setSessionLength(sessionData.getSessionLength());
            record.setXpGain(sessionData.getXpGain());
            record.setLoot(sessionData.getLoot());
            record.setSupplies(sessionData.getSupplies());
            record.setBalance(sessionData.getBalance());
            record.setWaste(sessionData.getBalance() != null && sessionData.getBalance().startsWith("-"));
            record.setHuntJsonOriginal(jsonText);
            record.setPartySnapshot(objectMapper.writeValueAsString(partySnapshotList));
            record.setSlug(gerarSlugAleatorio());

            huntRecordRepository.save(record);

        } catch (Exception e) {
            log.error("Erro ao processar nova hunt: {}", e.getMessage(), e);
            return "redirect:/hunts?erro=true";
        }
        return "redirect:/hunts";
    }

    private Map<String, Object> extrairDadosJogadorParaSnapshot(Usuario user, int setId, String defaultName) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", user.getCharName() != null ? user.getCharName() : defaultName);
        data.put("vocation", user.getCharVocation() != null ? user.getCharVocation() : "Unknown");
        data.put("level", user.getCharLevel() != null ? user.getCharLevel() : 0);
        data.put("activeSetIndex", setId);
        data.put("setsJson", user.getSetsEquipamentos());
        data.put("magicLevel", user.getMagicLevel());
        data.put("distanceSkill", user.getDistanceSkill());
        data.put("swordSkill", user.getSwordSkill());
        data.put("axeSkill", user.getAxeSkill());
        data.put("clubSkill", user.getClubSkill());
        data.put("shieldingSkill", user.getShieldingSkill());
        data.put("fistSkill", user.getFistSkill());
        return data;
    }

    @GetMapping("/{slug}/{nomeDaHunt}")
    public String visualizarHuntCompletaPersonalizada(@PathVariable String slug, @PathVariable String nomeDaHunt,
            Model model) {
        HuntRecord record = huntRecordRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Hunt não encontrada"));

        try {
            HuntingSessionDTO sessionData = objectMapper.readValue(record.getHuntJsonOriginal(),
                    HuntingSessionDTO.class);

            if (sessionData.getKilledMonsters() != null)
                sessionData.getKilledMonsters().sort((m1, m2) -> Integer.compare(m2.getCount(), m1.getCount()));
            if (sessionData.getLootedItems() != null)
                sessionData.getLootedItems().sort((i1, i2) -> Integer.compare(i2.getCount(), i1.getCount()));

            long balanceCalculadoNum = extrairNumero(sessionData.getLoot()) - extrairNumero(sessionData.getSupplies());

            model.addAttribute("huntData", sessionData);
            model.addAttribute("nomeHunt", record.getTitulo());
            model.addAttribute("isWaste", balanceCalculadoNum < 0);
            model.addAttribute("balanceCalculado",
                    new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.US)).format(balanceCalculadoNum));
            model.addAttribute("partySnapshot", record.getPartySnapshot());
            model.addAttribute("autorHunt",
                    record.getUsuario() != null
                            ? (record.getUsuario().getCharName() != null ? record.getUsuario().getCharName()
                                    : record.getUsuario().getUsername())
                            : "Desconhecido");
            model.addAttribute("dataCriacao",
                    record.getDataRegistro() != null ? record.getDataRegistro().format(formatter) : "--");

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                usuarioRepository.findByUsername(auth.getName()).ifPresent(user -> model.addAttribute("usuario", user));
            }

        } catch (Exception e) {
            log.error("Erro ao carregar a hunt: {}", e.getMessage());
            return "redirect:/hunts?erro=true";
        }
        return "hunts";
    }

    @PostMapping("/excluir/{id}")
    public String excluirHunt(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null)
            return "redirect:/login";
        huntRecordRepository.findById(id).ifPresent(record -> {
            if (record.getUsuario().getUsername().equals(userDetails.getUsername())) {
                huntRecordRepository.delete(record);
            }
        });
        return "redirect:/hunts";
    }

    private String gerarSlugAleatorio() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++)
            sb.append(chars.charAt(SECURE_RANDOM.nextInt(chars.length())));
        return sb.toString();
    }

    private long extrairNumero(String valor) {
        if (valor == null || valor.trim().isEmpty())
            return 0;
        try {
            return Long.parseLong(valor.replaceAll("[^\\d-]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}