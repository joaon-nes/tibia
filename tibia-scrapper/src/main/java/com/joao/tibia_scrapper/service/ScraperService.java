package com.joao.tibia_scrapper.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joao.tibia_scrapper.model.Equipamento;
import com.joao.tibia_scrapper.repository.EquipamentoRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ScraperService {

    @Autowired
    private EquipamentoRepository repository;

    private static final String URL_BASE = "https://www.tibiawiki.com.br";

    public void importarCategoria(String nomeCategoria, String subUrl) {
        System.out.println("Iniciando busca da categoria: " + nomeCategoria);
        try {
            Document doc = Jsoup.connect(URL_BASE + "/" + subUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/121.0.0.0")
                    .timeout(20000)
                    .get();

            Elements linhas = doc.select("table tr:has(td)");

            for (Element linha : linhas) {
                Elements col = linha.select("td");
                if (col.size() < 3)
                    continue;

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
                if (link == null)
                    continue;

                String nome = col.get(0).text().trim();
                if (nome.isEmpty() || nome.equalsIgnoreCase("Nome"))
                    continue;

                if (!repository.existsByNome(nome)) {
                    Equipamento item = new Equipamento();

                    item.setNome(nome);
                    item.setCategoria(nomeCategoria);

                    Element img = col.get(1).selectFirst("img");
                    if (img != null) {
                        String src = img.hasAttr("data-src") ? img.attr("data-src") : img.attr("src");
                        item.setImagemUrl(src.startsWith("http") ? src : URL_BASE + src);
                    }

                    repository.save(item);
                    System.out.println("Salvo (Básico): " + nome);
                }
            }
            System.out.println("Sucesso ao importar: " + nomeCategoria);

        } catch (Exception e) {
            System.err.println("Erro na categoria " + nomeCategoria + ": " + e.getMessage());
        }
    }

    public void atualizarEquipamentosViaApi() {
        List<Equipamento> equipamentos = repository.findAll();
        System.out.println("Iniciando enriquecimento de " + equipamentos.size() + " equipamentos via API...");

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        for (Equipamento equip : equipamentos) {
            try {
                String apiUrl = "https://tibiawiki.dev/api/items/{nome}";
                ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class, equip.getNome());

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    JsonNode root = mapper.readTree(response.getBody());

                    if (root.has("levelrequired"))
                        equip.setLevelMinimo(extrairInteiro(root.get("levelrequired").asText()));
                    if (root.has("vocrequired")) {
                        equip.setVocacoes(padronizarVocacao(root.get("vocrequired").asText()));
                    }
                    if (root.has("hands")) {
                        if (!equip.getCategoria().equalsIgnoreCase("Wands")
                                && !equip.getCategoria().equalsIgnoreCase("Rods")) {
                            equip.setMaos(root.get("hands").asText());
                        }
                    }
                    if (root.has("defense"))
                        equip.setDefesa(extrairInteiro(root.get("defense").asText()));
                    if (root.has("defensemod"))
                        equip.setModDefesa(root.get("defensemod").asText());
                    if (root.has("attack"))
                        equip.setAtaque(extrairInteiro(root.get("attack").asText()));
                    if (root.has("armor"))
                        equip.setArmadura(extrairInteiro(root.get("armor").asText()));
                    if (root.has("imbueslots"))
                        equip.setSlots(extrairInteiro(root.get("imbueslots").asText()));
                    if (root.has("upgradeclass"))
                        equip.setTier(extrairInteiro(root.get("upgradeclass").asText()));
                    if (root.has("attrib"))
                        equip.setAtributos(root.get("attrib").asText());
                    if (root.has("hpleech_ch")) {
                        equip.setLifeLeechChance(extrairInteiro(root.get("hpleech_ch").asText()));
                    }
                    if (root.has("hpleech_am")) {
                        equip.setLifeLeechAmount(extrairInteiro(root.get("hpleech_am").asText()));
                    }
                    if (root.has("manaleech_ch")) {
                        equip.setManaLeechChance(extrairInteiro(root.get("manaleech_ch").asText()));
                    }
                    if (root.has("manaleech_am")) {
                        equip.setManaLeechAmount(extrairInteiro(root.get("manaleech_am").asText()));
                    }
                    if (root.has("crithit_ch")) {
                        equip.setCriticalChance(extrairInteiro(root.get("crithit_ch").asText()));
                    }
                    if (root.has("critextra_dmg")) {
                        equip.setCriticalDamage(extrairInteiro(root.get("critextra_dmg").asText()));
                    }
                    if (root.has("mantra")) {
                        equip.setMantra(extrairInteiro(root.get("mantra").asText()));
                    }
                    if (root.has("range")) {
                        equip.setAlcance(extrairInteiro(root.get("range").asText()));
                    }
                    if (root.has("resist")) {
                        equip.setProtecao(root.get("resist").asText());
                    }
                    if (root.has("elementalbond")) {
                        equip.setElementalBond(root.get("elementalbond").asText());
                    }

                    String danoElemental = extrairDanoElemental(root);
                    if (!danoElemental.isEmpty())
                        equip.setDanoElemental(danoElemental);

                    repository.save(equip);
                    System.out.println("✅ Status Atualizados: " + equip.getNome());
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                System.err.println("❌ Sem dados na API para: " + equip.getNome());
            }
        }
        System.out.println("🎉 Processo de enriquecimento via API finalizado!");
    }

    private Integer extrairInteiro(String texto) {
        try {
            String limpo = texto.replaceAll("[^0-9-]", "");
            if (limpo.isEmpty())
                return 0;
            return Integer.parseInt(limpo);
        } catch (Exception e) {
            return 0;
        }
    }

    private String extrairDanoElemental(JsonNode root) {
        String[] elementos = { "ice", "fire", "earth", "energy", "death", "holy" };
        for (String elemento : elementos) {
            if (root.has(elemento + "_attack")) {
                String valor = root.get(elemento + "_attack").asText();
                return valor + " " + elemento.substring(0, 1).toUpperCase() + elemento.substring(1);
            }
        }
        return "";
    }

    private String padronizarVocacao(String t) {
        if (t == null)
            return "";
        String b = t.toLowerCase();
        StringBuilder r = new StringBuilder();
        if (b.contains("knights"))
            r.append("Knights;");
        if (b.contains("paladins"))
            r.append("Paladins;");
        if (b.contains("sorcerers"))
            r.append("Sorcerers;");
        if (b.contains("druids"))
            r.append("Druids;");
        if (b.contains("monks"))
            r.append("Monks;");
        return r.toString();
    }
}