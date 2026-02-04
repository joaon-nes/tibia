package com.joao.tibia_scrapper.service;

import com.joao.tibia_scrapper.model.Equipamento;
import com.joao.tibia_scrapper.repository.EquipamentoRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScraperService {

    @Autowired
    private EquipamentoRepository repository;

    private static final String URL_BASE = "https://www.tibiawiki.com.br";

    public void importarCategoria(String nomeCategoria, String subUrl) {
        try {
            Document doc = Jsoup.connect(URL_BASE + "/" + subUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/121.0.0.0")
                    .timeout(20000)
                    .get();

            Elements linhas = doc.select("table tr:has(td)");

            for (Element linha : linhas) {
                Elements col = linha.select("td");
                if (col.size() < 3) continue;

                String nomeFull = col.get(0).text().trim();

                if (nomeFull.isEmpty() || 
                    nomeFull.equalsIgnoreCase("Nome") || 
                    nomeFull.contains("Update") || 
                    nomeFull.contains("Saiba mais") || 
                    nomeFull.contains("Alternativos") || 
                    nomeFull.length() > 100) { 
                    continue;
                }

                Element link = col.get(0).selectFirst("a");
                if (link == null) {
                    continue;
                }

                if (nomeFull.isEmpty() || nomeFull.equalsIgnoreCase("Nome") || nomeFull.length() > 200) {
                    continue;
                }
                
                String nome = col.get(0).text().trim();
                if (nome.isEmpty() || nome.equalsIgnoreCase("Nome")) continue;
                
                Optional<Equipamento> itemOpt = repository.findByNome(nome);
                Equipamento item = itemOpt.orElse(new Equipamento());
                
                item.setNome(nomeFull);
                item.setNome(nome);
                item.setCategoria(nomeCategoria);
                
                Element img = col.get(1).selectFirst("img");
                if (img != null) {
                    String src = img.hasAttr("data-src") ? img.attr("data-src") : img.attr("src");
                    item.setImagemUrl(src.startsWith("http") ? src : URL_BASE + src);
                }

                mapearColunasPorTipo(item, col, nomeCategoria);
                if (item.getNome() != null && !item.getNome().isEmpty()) {
                
                if (item.getNome() != null && item.getNome().toLowerCase().contains("backpack")) {
                    item.setSlots(1);
                }
                if (nomeCategoria.equalsIgnoreCase("Backpacks")) {
                    if (item.getVolume() == null || item.getVolume() <= 0) {
                        System.out.println("Pulando " + item.getNome() + " por estar sem volume.");
                        continue;
                    }
                }

                repository.save(item);
                }
            }
            System.out.println("Sucesso ao importar: " + nomeCategoria);

        } catch (Exception e) {
            System.err.println("Erro na categoria " + nomeCategoria + ": " + e.getMessage());
        }
    }

    private void mapearColunasPorTipo(Equipamento item, Elements col, String cat) {
        int size = col.size();
        if (size < 2) return;   

        item.setLevelMinimo(limparInt(col.get(2).text()));
        item.setVocacoes(padronizarVocacao(col.get(3).text()));

        switch (cat) {   
            case "Armors": case "Helmets": case "Legs": case "Boots":
                if (size > 4) item.setArmadura(limparInt(col.get(4).text()));
                if (size > 5) item.setBonusSkill(col.get(5).text());
                if (size > 6) item.setProtecao(col.get(6).text());
                if (size > 7) item.setSlots(limparInt(col.get(7).text()));
                if (size > 8) item.setTier(limparInt(col.get(8).text()));
            break;
            case "Shields":
                if (size > 4) item.setSlots(limparInt(col.get(4).text()));
                if (size > 5) item.setDefesa(limparInt(col.get(5).text()));
                if (size > 6) item.setBonusSkill(col.get(6).text());
                if (size > 7) item.setProtecao(col.get(7).text());
            break;
            case "Spellbooks":
                if (size > 4) item.setDefesa(limparInt(col.get(4).text()));
                if (size > 5) item.setBonusSkill(col.get(5).text());
                if (size > 6) item.setProtecao(col.get(6).text());
                if (size > 7) item.setSlots(limparInt(col.get(7).text()));
            break;
            case "Quivers":
                if (size > 4) item.setVolume(limparInt(col.get(4).text()));
                if (size > 5) item.setBonusSkill(col.get(5).text());
                if (size > 6) item.setProtecao(col.get(6).text());
            break;
            case "Extra Slot":
                item.setNome(col.get(0).text().trim());
                item.setBonusSkill(col.get(2).text().trim());
                item.setVocacoes("Paladin");
            break;
            case "Swords": case "Axes": case "Clubs":
                if (size > 4) item.setMaos(col.get(4).text());
                if (size > 5) item.setAtaque(limparInt(col.get(5).text()));
                if (size > 6) item.setDanoElemental(col.get(6).text().trim());
                if (size > 7) item.setDefesa(limparInt(col.get(7).text()));
                if (size > 8) item.setModDefesa(col.get(8).text());
                if (size > 9) item.setBonusSkill(col.get(9).text());
                if (size > 10) item.setSlots(limparInt(col.get(10).text()));
                if (size > 11) item.setTier(limparInt(col.get(11).text()));
            break;
            case "Fist":
                if (size > 4) item.setMaos(col.get(4).text());
                if (size > 5) item.setAtaque(limparInt(col.get(5).text()));
                if (size > 6) item.setElementalBond(col.get(6).text().trim());
                if (size > 7) item.setDefesa(limparInt(col.get(7).text()));
                if (size > 8) item.setModDefesa(col.get(8).text());
                if (size > 9) item.setBonusSkill(col.get(9).text());
                if (size > 10) item.setSlots(limparInt(col.get(10).text()));
                if (size > 11) item.setTier(limparInt(col.get(11).text()));
            break;
            case "Wands": case "Rods":
                if (size > 4) item.setTipoDano(col.get(4).text());
                if (size > 5) item.setBonusSkill(col.get(5).text());
                if (size > 6) item.setProtecao(col.get(6).text());
                if (size > 7) item.setDanoMedio(col.get(7).text());
                if (size > 8) item.setManaPorAtaque(limparInt(col.get(8).text()));
                if (size > 9) item.setSlots(limparInt(col.get(9).text()));
                if (size > 10) item.setTier(limparInt(col.get(10).text()));
            break;
            case "Distance":
                if (size > 4) item.setMaos(col.get(4).text());
                if (size > 5) item.setAlcance(limparInt(col.get(5).text()));
                if (size > 6) item.setAtaque(limparInt(col.get(6).text()));
                if (size > 7) item.setHitPercent(col.get(7).text().trim());
                if (size > 8) item.setBonusSkill(col.get(8).text());
                if (size > 9) item.setDanoElemental(col.get(9).text().trim());
                if (size > 10) item.setProtecao(col.get(10).text());
                if (size > 11) item.setSlots(limparInt(col.get(11).text()));
                if (size > 12) item.setTier(limparInt(col.get(12).text()));
            break;
            case "Ammunition":
                if (size > 2) item.setLevelMinimo(limparInt(col.get(2).text()));
                if (size > 3) item.setAtaque(limparInt(col.get(3).text()));
                if (size > 4) {
                    String texto = col.get(4).text().trim();
                    boolean isNumerico = texto.matches("\\d*\\.?\\d+");
                    if (!isNumerico && !texto.isEmpty()) { 
                        item.setDanoElemental(texto);
                    } else if (isNumerico) {
                    }
                }
                item.setVocacoes("Paladin");
            break;
            case "Amulets": case "Rings":
                if (size > 4) item.setArmadura(limparInt(col.get(4).text()));
                if (size > 5) item.setCargas(col.get(5).text());
                if (size > 6) item.setDuracao(col.get(6).text());
                if (size > 7) item.setAtributos(col.get(7).text());
                if (size > 8) item.setBonusSkill(col.get(8).text());
                if (size > 9) item.setProtecao(col.get(9).text());
            break;
            case "Backpacks":
                if (size > 2) {
                int valorVolume = limparInt(col.get(2).text());
                item.setVolume(valorVolume);
            }
                item.setLevelMinimo(0);
            break;
            default:
            break;
        }
    }

    private Integer limparInt(String t) {
        if (t == null || t.isEmpty() || t.equals("-")) return 0;
        String n = t.replaceAll("[^0-9]", "");
        return n.isEmpty() ? 0 : Integer.parseInt(n);
    }

    private String padronizarVocacao(String t) {
        if (t == null) return "";
        String b = t.toLowerCase();
        StringBuilder r = new StringBuilder();
        if (b.contains("knight")) r.append("Knight;");
        if (b.contains("paladin")) r.append("Paladin;");
        if (b.contains("sorcerer")) r.append("Sorcerer;");
        if (b.contains("druid")) r.append("Druid;");
        if (b.contains("monk")) r.append("Monk;");
        return r.toString();
    }
}