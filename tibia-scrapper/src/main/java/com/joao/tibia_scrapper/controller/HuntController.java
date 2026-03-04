package com.joao.tibia_scrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joao.tibia_scrapper.dto.HuntingSessionDTO;
import com.joao.tibia_scrapper.model.HuntRecord;
import com.joao.tibia_scrapper.model.Usuario;
import com.joao.tibia_scrapper.repository.HuntRecordRepository;
import com.joao.tibia_scrapper.repository.UsuarioRepository;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/hunts")
public class HuntController {

    private final UsuarioRepository usuarioRepository;
    private final HuntRecordRepository huntRecordRepository;
    private final ObjectMapper objectMapper;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public HuntController(UsuarioRepository usuarioRepository,
            HuntRecordRepository huntRecordRepository,
            ObjectMapper objectMapper) {
        this.usuarioRepository = usuarioRepository;
        this.huntRecordRepository = huntRecordRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String exibirPaginaHunts(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null)
            return "redirect:/login";

        Optional<Usuario> userOpt = usuarioRepository.findByUsername(userDetails.getUsername());
        if (userOpt.isPresent()) {
            Usuario usuario = userOpt.get();
            model.addAttribute("usuario", usuario);

            List<HuntRecord> todasHunts = huntRecordRepository.findAllByOrderByDataRegistroDesc();
            model.addAttribute("todasHunts", todasHunts);

            List<HuntRecord> minhasHunts = huntRecordRepository.findByUsuarioOrderByDataRegistroDesc(usuario);
            model.addAttribute("minhasHunts", minhasHunts);
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

        Optional<Usuario> currentUserOpt = usuarioRepository.findByUsername(userDetails.getUsername());
        if (currentUserOpt.isEmpty())
            return "redirect:/login";
        Usuario currentUser = currentUserOpt.get();

        try {
            HuntingSessionDTO sessionData = objectMapper.readValue(jsonText, HuntingSessionDTO.class);

            List<Map<String, Object>> partySnapshotList = new ArrayList<>();

            Map<String, Object> ownerData = new HashMap<>();
            ownerData.put("name",
                    currentUser.getCharName() != null ? currentUser.getCharName() : currentUser.getUsername());
            ownerData.put("vocation",
                    currentUser.getCharVocation() != null ? currentUser.getCharVocation() : "Unknown");
            ownerData.put("level", currentUser.getCharLevel() != null ? currentUser.getCharLevel() : 0);
            ownerData.put("activeSetIndex", meuSetId);
            ownerData.put("setsJson", currentUser.getSetsEquipamentos());
            ownerData.put("magicLevel", currentUser.getMagicLevel());
            ownerData.put("distanceSkill", currentUser.getDistanceSkill());
            ownerData.put("swordSkill", currentUser.getSwordSkill());
            ownerData.put("axeSkill", currentUser.getAxeSkill());
            ownerData.put("clubSkill", currentUser.getClubSkill());
            ownerData.put("shieldingSkill", currentUser.getShieldingSkill());
            ownerData.put("fistSkill", currentUser.getFistSkill());
            partySnapshotList.add(ownerData);

            if (partyMembers != null) {
                for (String memberName : partyMembers) {
                    if (memberName != null && !memberName.trim().isEmpty()) {
                        Optional<Usuario> memberOpt = usuarioRepository.findByCharNameIgnoreCase(memberName.trim());
                        if (memberOpt.isPresent()) {
                            Usuario member = memberOpt.get();
                            Map<String, Object> mData = new HashMap<>();
                            mData.put("name", member.getCharName());
                            mData.put("vocation",
                                    member.getCharVocation() != null ? member.getCharVocation() : "Unknown");
                            mData.put("level", member.getCharLevel() != null ? member.getCharLevel() : 0);
                            mData.put("activeSetIndex", 1);
                            mData.put("setsJson", member.getSetsEquipamentos());
                            mData.put("magicLevel", member.getMagicLevel());
                            mData.put("distanceSkill", member.getDistanceSkill());
                            mData.put("swordSkill", member.getSwordSkill());
                            mData.put("axeSkill", member.getAxeSkill());
                            mData.put("clubSkill", member.getClubSkill());
                            mData.put("shieldingSkill", member.getShieldingSkill());
                            mData.put("fistSkill", member.getFistSkill());
                            partySnapshotList.add(mData);
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

            String partySnapshotJson = objectMapper.writeValueAsString(partySnapshotList);

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
            record.setPartySnapshot(partySnapshotJson);
            record.setSlug(gerarSlugAleatorio());

            huntRecordRepository.save(record);

        } catch (Exception e) {
            log.error("Erro ao processar/salvar a nova hunt: {}", e.getMessage(), e);
            return "redirect:/hunts?erro=true";
        }

        return "redirect:/hunts";
    }

    @GetMapping("/{slug}/{nomeDaHunt}")
    public String visualizarHuntCompletaPersonalizada(@PathVariable String slug, @PathVariable String nomeDaHunt,
            Model model) {
        HuntRecord record = huntRecordRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Hunt não encontrada"));

        try {
            HuntingSessionDTO sessionData = objectMapper.readValue(record.getHuntJsonOriginal(),
                    HuntingSessionDTO.class);

            if (sessionData.getKilledMonsters() != null) {
                sessionData.getKilledMonsters().sort((m1, m2) -> Integer.compare(m2.getCount(), m1.getCount()));
            }
            if (sessionData.getLootedItems() != null) {
                sessionData.getLootedItems().sort((i1, i2) -> Integer.compare(i2.getCount(), i1.getCount()));
            }

            long valorLoot = extrairNumero(sessionData.getLoot());
            long valorSupplies = extrairNumero(sessionData.getSupplies());
            long balanceCalculadoNum = valorLoot - valorSupplies;
            boolean isWasteCalculado = balanceCalculadoNum < 0;

            DecimalFormat df = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.US));
            String balanceFormatado = df.format(balanceCalculadoNum);

            String dataAcesso = record.getDataRegistro() != null ? record.getDataRegistro().format(formatter) : "--";

            String autorNome = "Desconhecido";
            if (record.getUsuario() != null) {
                autorNome = record.getUsuario().getCharName() != null ? record.getUsuario().getCharName()
                        : record.getUsuario().getUsername();
            }

            model.addAttribute("huntData", sessionData);
            model.addAttribute("nomeHunt", record.getTitulo());
            model.addAttribute("isWaste", isWasteCalculado);
            model.addAttribute("balanceCalculado", balanceFormatado);
            model.addAttribute("partySnapshot", record.getPartySnapshot());
            model.addAttribute("autorHunt", autorNome);
            model.addAttribute("dataCriacao", dataAcesso);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
                usuarioRepository.findByUsername(auth.getName()).ifPresent(user -> model.addAttribute("usuario", user));
            } else {
                model.addAttribute("usuario", null);
            }

        } catch (Exception e) {
            log.error("Erro ao carregar dados para visualizar a hunt (Slug: {}): {}", slug, e.getMessage(), e);
            return "redirect:/hunts?erro=true";
        }

        return "hunts";
    }

    @PostMapping("/excluir/{id}")
    public String excluirHunt(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null)
            return "redirect:/login";

        Optional<HuntRecord> recordOpt = huntRecordRepository.findById(id);
        if (recordOpt.isPresent()) {
            HuntRecord record = recordOpt.get();
            if (record.getUsuario().getUsername().equals(userDetails.getUsername())) {
                huntRecordRepository.delete(record);
            }
        }
        return "redirect:/hunts";
    }

    private String gerarSlugAleatorio() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(SECURE_RANDOM.nextInt(chars.length())));
        }
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