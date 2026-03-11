package com.joao.tibia_scrapper.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joao.tibia_scrapper.model.Criatura;
import com.joao.tibia_scrapper.model.Equipamento;
import com.joao.tibia_scrapper.model.Magia;
import com.joao.tibia_scrapper.model.Runa;
import com.joao.tibia_scrapper.repository.CriaturaRepository;
import com.joao.tibia_scrapper.repository.EquipamentoRepository;
import com.joao.tibia_scrapper.repository.MagiaRepository;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.joao.tibia_scrapper.repository.RunaRepository;

import java.io.File;
import java.util.List;

@Slf4j
@Service
public class ScraperService {

    private final EquipamentoRepository repository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final RunaRepository runaRepository;
    private final MagiaRepository magiaRepository;
    private final CriaturaRepository criaturaRepository;

    private static final String URL_BASE = "https://www.tibiawiki.com.br";

    public ScraperService(EquipamentoRepository repository,
            RunaRepository runaRepository,
            MagiaRepository magiaRepository,
            CriaturaRepository criaturaRepository,
            RestTemplate restTemplate,
            ObjectMapper objectMapper) {
        this.repository = repository;
        this.runaRepository = runaRepository;
        this.magiaRepository = magiaRepository;
        this.criaturaRepository = criaturaRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public void importarCategoria(String nomeCategoria, String subUrl) {
        log.info("Iniciando busca da categoria: {}", nomeCategoria);
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

                String nome = col.get(0).text().trim();

                if (nome.isEmpty() || nome.equalsIgnoreCase("Nome") || nome.contains("Update") || nome.length() > 100) {
                    continue;
                }

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
                }
            }
        } catch (Exception e) {
            log.error("Erro na categoria {}: {}", nomeCategoria, e.getMessage());
        }
    }

    public void atualizarEquipamentosViaApi() {
        List<Equipamento> equipamentos = repository.findAll();
        for (Equipamento equip : equipamentos) {
            try {
                String apiUrl = "https://tibiawiki.dev/api/items/{nome}";
                ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class, equip.getNome());

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    JsonNode root = objectMapper.readTree(response.getBody());

                    if (root.has("levelrequired"))
                        equip.setLevelMinimo(extrairInteiro(root.get("levelrequired").asText()));
                    if (root.has("vocrequired"))
                        equip.setVocacoes(padronizarVocacao(root.get("vocrequired").asText()));
                    if (root.has("defense"))
                        equip.setDefesa(extrairInteiro(root.get("defense").asText()));
                    if (root.has("attack"))
                        equip.setAtaque(extrairInteiro(root.get("attack").asText()));
                    if (root.has("armor"))
                        equip.setArmadura(extrairInteiro(root.get("armor").asText()));

                    repository.save(equip);
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                log.warn("Erro ao atualizar item: {}", equip.getNome());
            }
        }
    }

    private Integer extrairInteiro(String texto) {
        try {
            String limpo = texto.replaceAll("[^0-9-]", "");
            return limpo.isEmpty() ? 0 : Integer.parseInt(limpo);
        } catch (Exception e) {
            return 0;
        }
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
        return r.toString();
    }

    public void importarRunasTibiaWikiBrCompleto() {
        log.info("Iniciando scraping completo de Runas via TibiaWiki BR...");
        String url = "https://www.tibiawiki.com.br/wiki/Runas";

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(30000)
                    .get();

            Elements tabelas = doc.select("table.wikitable, table.sortable");

            if (tabelas.isEmpty()) {
                log.warn("Nenhuma tabela encontrada na página de Runas.");
                return;
            }

            for (Element tabela : tabelas) {
                Elements linhas = tabela.select("tr");

                for (int i = 1; i < linhas.size(); i++) {
                    Element linha = linhas.get(i);
                    Elements colunas = linha.select("td, th");

                    if (colunas.size() > 10) {
                        Element tdImg = colunas.get(0);
                        Element tdNome = colunas.get(1);

                        if (tdNome.selectFirst("img") != null && tdImg.selectFirst("img") == null) {
                            tdImg = colunas.get(1);
                            tdNome = colunas.get(0);
                        }

                        String nome = tdNome.text().replaceAll("\\(Runa\\)", "").trim();
                        if (nome.isEmpty() || nome.equalsIgnoreCase("Nome") || nome.matches("^[0-9]+$")) {
                            continue;
                        }

                        String spellWords = colunas.get(2).text().trim();
                        String vocacoes = colunas.get(3).text().trim();
                        String premium = colunas.get(4).text().trim();
                        Integer level = extrairInteiro(colunas.get(6).text());
                        Integer magicLevel = extrairInteiro(colunas.get(7).text());
                        Integer mana = extrairInteiro(colunas.get(8).text());
                        Integer soulPoints = extrairInteiro(colunas.get(9).text());
                        String efeito = colunas.get(10).text().trim();

                        String nomeArquivoImagem = nome.replace(" ", "_") + ".gif";
                        String urlImagemLocal = "/tibia-images/Runas/" + nomeArquivoImagem;

                        Runa r = runaRepository.findByNome(nome).orElse(new Runa());
                        r.setNome(nome);
                        r.setImagemUrl(urlImagemLocal);
                        r.setSpellWords(spellWords);
                        r.setVocacoes(vocacoes);
                        r.setPremium(premium);
                        r.setLevelMinimo(level);
                        r.setMagicLevel(magicLevel);
                        r.setMana(mana);
                        r.setSoulPoints(soulPoints);
                        r.setEfeito(efeito);

                        runaRepository.save(r);
                        log.info("Runa processada com sucesso: {} (Efeito: {})", nome, efeito);
                    }
                }
            }
            log.info("Processo de importação de Runas finalizado!");

        } catch (Exception e) {
            log.error("Erro ao importar runas: {}", e.getMessage());
        }
    }

    public void importarMagiasTibiaWikiBr() {
        log.info("Iniciando scraping completo de Magias via TibiaWiki BR...");
        String url = "https://www.tibiawiki.com.br/wiki/Magias_Instant%C3%A2neas";

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(30000)
                    .get();

            Elements tabelas = doc.select("table.wikitable, table.sortable");

            if (tabelas.isEmpty()) {
                log.warn("Nenhuma tabela encontrada na página de Magias.");
                return;
            }

            for (Element tabela : tabelas) {
                Elements linhas = tabela.select("tr");
                if (linhas.isEmpty())
                    continue;

                for (int i = 1; i < linhas.size(); i++) {
                    Element linha = linhas.get(i);
                    Elements colunas = linha.select("td, th");

                    if (colunas.size() > 8) {
                        Element tdImg = colunas.get(0);
                        Element tdNome = colunas.get(1);

                        if (tdNome.selectFirst("img") != null && tdImg.selectFirst("img") == null) {
                            tdImg = colunas.get(1);
                            tdNome = colunas.get(0);
                        }

                        String nome = tdNome.text().trim();
                        if (nome.isEmpty() || nome.equalsIgnoreCase("Nome") || nome.matches("^[0-9]+$")) {
                            continue;
                        }

                        Integer level = extrairInteiro(colunas.get(2).text());
                        String vocacoes = colunas.get(3).text().trim();

                        String premium = "Não";
                        Element imgPremium = colunas.get(4).selectFirst("img");
                        if (imgPremium != null) {
                            String alt = imgPremium.attr("alt").toLowerCase();
                            String srcImg = imgPremium.attr("src").toLowerCase();
                            if (alt.contains("tick") || srcImg.contains("tick")) {
                                premium = "Sim";
                            }
                        }

                        String spellWords = colunas.get(5).text().trim();
                        Integer mana = extrairInteiro(colunas.get(6).text());

                        String grupo = colunas.get(7).text().replace("[", "").replace("]", "").trim();
                        String efeito = colunas.get(8).text().trim();

                        String nomeArquivoImagem = nome.replace(" ", "_") + ".gif";
                        String urlImagemLocal = "/tibia-images/Magias/" + grupo + "/" + nomeArquivoImagem;

                        Magia m = magiaRepository.findByNome(nome).orElse(new Magia());
                        m.setNome(nome);
                        m.setImagemUrl(urlImagemLocal);
                        m.setLevelMinimo(level);
                        m.setVocacoes(vocacoes);
                        m.setPremium(premium);
                        m.setSpellWords(spellWords);
                        m.setMana(mana);
                        m.setGrupo(grupo);
                        m.setEfeito(efeito);

                        magiaRepository.save(m);
                        log.info("Magia processada: {} (Pasta do Grupo: {})", nome, grupo);
                    }
                }
            }
            log.info("Processo de importação de Magias finalizado com sucesso!");

        } catch (Exception e) {
            log.error("Erro ao importar magias: {}", e.getMessage());
        }
    }

    public void migrarCriaturasLocal() {
        log.info("Iniciando migração local de criaturas...");
        try {
            // caminho do arquivo
            File jsonFile = new File("C:\\tibia-enciclopedia\\creatures.json");
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            JsonNode creaturesNode = rootNode.get("creatures");

            creaturesNode.fields().forEachRemaining(entry -> {
                String nomeCriatura = entry.getKey();
                JsonNode data = entry.getValue();

                Criatura c = criaturaRepository.findByNome(nomeCriatura).orElse(new Criatura());
                c.setNome(nomeCriatura);

                // atributos principais
                c.setHp(data.path("hp").asText());
                c.setExp(data.path("exp").asText());
                c.setSpeed(data.path("speed").asText());
                c.setArmor(data.path("armor").asText());

                // comportamento
                c.setSummon(data.path("summon").asText());
                c.setPushobjects(data.path("pushobjects").asText());
                c.setSenseinvis(data.path("senseinvis").asText());
                c.setRunsat(data.path("runsat").asText());
                c.setParaimmune(data.path("paraimmune").asText());
                c.setPushable(data.path("pushable").asText());

                // resistências
                c.setPhysicalDmgMod(data.path("physicalDmgMod").asText("100%"));
                c.setEarthDmgMod(data.path("earthDmgMod").asText("100%"));
                c.setFireDmgMod(data.path("fireDmgMod").asText("100%"));
                c.setDeathDmgMod(data.path("deathDmgMod").asText("100%"));
                c.setEnergyDmgMod(data.path("energyDmgMod").asText("100%"));
                c.setHolyDmgMod(data.path("holyDmgMod").asText("100%"));
                c.setIceDmgMod(data.path("iceDmgMod").asText("100%"));
                c.setHealMod(data.path("healMod").asText("100%"));
                c.setHpDrainDmgMod(data.path("hpDrainDmgMod").asText("100%"));
                c.setDrownDmgMod(data.path("drownDmgMod").asText("100%"));

                // textos e listas
                c.setSounds(data.path("sounds").toString());
                c.setAbilities(data.path("abilities").asText());
                c.setLoot(data.path("loot").toString());
                c.setBestiarytext(data.path("bestiarytext").asText());

                // categoria e imagem
                String classeIngles = data.path("creatureclass").asText("Unknown");
                String categoriaPt = traduzirCategoria(classeIngles);
                c.setCategoria(categoriaPt);

                String nomeImagem = nomeCriatura.replace(" ", "_") + ".gif";
                c.setImagemUrl("/tibia-images/Criaturas/" + categoriaPt + "/" + nomeImagem);

                criaturaRepository.save(c);
            });

            log.info("Migração de criaturas concluída!");
        } catch (Exception e) {
            log.error("Erro na migração: " + e.getMessage());
        }
    }

    private String traduzirCategoria(String creatureClass) {
        if (creatureClass == null || creatureClass.isEmpty())
            return "Desconhecido";

        switch (creatureClass.toLowerCase()) {
            case "amphibians":
                return "Anfíbios";
            case "aquatics":
                return "Aquáticos";
            case "birds":
                return "Aves";
            case "bosses":
                return "Bosses";
            case "constructs":
                return "Constructos";
            case "magicals":
                return "Criaturas Mágicas";
            case "demons":
                return "Demônios";
            case "dragons":
                return "Dragões";
            case "elementals":
                return "Elementais";
            case "extra dimensionals":
                return "Extra Dimensionais";
            case "fey":
                return "Fadas";
            case "giants":
                return "Gigantes";
            case "humans":
                return "Humanos";
            case "humanoids":
                return "Humanóides";
            case "immortals":
                return "Imortais";
            case "inkborn":
                return "Inkborn";
            case "lycanthropes":
                return "Licantropos";
            case "mammals":
                return "Mamíferos";
            case "undeads":
                return "Mortos-Vivos";
            case "plants":
                return "Plantas";
            case "reptiles":
                return "Répteis";
            case "slimes":
                return "Slimes";
            case "vermins":
                return "Vermes";
            default:
                return creatureClass;
        }
    }

    public void importarCriaturasJson(String pastaJson) {
        log.info("Iniciando importação de criaturas dos JSONs em: {}", pastaJson);

        File pasta = new File(pastaJson);
        if (!pasta.exists() || !pasta.isDirectory()) {
            log.error("Pasta não encontrada: {}", pastaJson);
            return;
        }

        File[] arquivos = pasta.listFiles((dir, name) -> name.endsWith(".json"));
        if (arquivos == null || arquivos.length == 0) {
            log.warn("Nenhum JSON encontrado em: {}", pastaJson);
            return;
        }

        java.util.Arrays.sort(arquivos, (f1, f2) -> {
            boolean isBoss1 = f1.getName().toLowerCase().contains("boss");
            boolean isBoss2 = f2.getName().toLowerCase().contains("boss");

            if (isBoss1 && !isBoss2)
                return 1;
            if (!isBoss1 && isBoss2)
                return -1;
            return f1.getName().compareToIgnoreCase(f2.getName()); // ordem alfabética para o resto
        });

        int totalSalvas = 0;
        java.util.Set<String> nomesUnicos = new java.util.HashSet<>();

        for (File arquivo : arquivos) {
            log.info("Lendo arquivo: {}", arquivo.getName());
            try {
                JsonNode root = objectMapper.readTree(arquivo);

                JsonNode creatures = root.path("creatures");
                while (!creatures.isMissingNode() && creatures.has("creatures")
                        && creatures.path("creatures").isObject()) {
                    creatures = creatures.path("creatures");
                }

                log.info("  Criaturas no JSON: {}", creatures.size());

                int salvosNesteCat = 0;
                java.util.Iterator<java.util.Map.Entry<String, JsonNode>> it = creatures.fields();
                while (it.hasNext()) {
                    java.util.Map.Entry<String, JsonNode> entry = it.next();
                    JsonNode data = entry.getValue();

                    if (!data.isObject())
                        continue;

                    String nomeCriatura = jsonTexto(data, "nome");
                    if (nomeCriatura == null || nomeCriatura.isBlank()) {
                        nomeCriatura = jsonTexto(data, "name");
                    }
                    if (nomeCriatura == null || nomeCriatura.isBlank()) {
                        nomeCriatura = entry.getKey();
                    }

                    if (nomeCriatura == null || nomeCriatura.isBlank() || nomeCriatura.equals("Unknown")) {
                        continue;
                    }

                    if (!nomesUnicos.add(nomeCriatura)) {
                        log.debug("Criatura duplicada encontrada em '{}' (Atualizando categoria para este JSON): {}",
                                arquivo.getName(), nomeCriatura);
                    }

                    String categoria = jsonTexto(data, "categoria");

                    Criatura c = criaturaRepository.findByNome(nomeCriatura).orElse(new Criatura());
                    c.setNome(nomeCriatura);
                    c.setCategoria(categoria);

                    String nomeArquivo = nomeCriatura.replace(" ", "_")
                            .replace("(", "%28")
                            .replace(")", "%29") + ".gif";
                    String catDir = (categoria != null ? categoria : "Outros");
                    c.setImagemUrl("/tibia-images/Criaturas/" + catDir + "/" + nomeArquivo);

                    c.setHp(jsonInteiroTexto(data, "hp"));
                    c.setExp(jsonInteiroTexto(data, "experiencia"));
                    c.setCharms(jsonInteiro(data, "charms"));

                    c.setPhysicalDmgMod(jsonTexto(data, "physicalDmgMod"));
                    c.setEarthDmgMod(jsonTexto(data, "earthDmgMod"));
                    c.setFireDmgMod(jsonTexto(data, "fireDmgMod"));
                    c.setDeathDmgMod(jsonTexto(data, "deathDmgMod"));
                    c.setEnergyDmgMod(jsonTexto(data, "energyDmgMod"));
                    c.setHolyDmgMod(jsonTexto(data, "holyDmgMod"));
                    c.setIceDmgMod(jsonTexto(data, "iceDmgMod"));
                    c.setHealMod(jsonTexto(data, "healMod"));
                    c.setHpDrainDmgMod(jsonTexto(data, "hpDrainDmgMod"));
                    c.setDrownDmgMod(jsonTexto(data, "drownDmgMod"));

                    c.setSummon(jsonTexto(data, "summona"));
                    c.setPushobjects(jsonTexto(data, "empurraObjetos"));
                    c.setPushable(jsonTexto(data, "podeSerPuxado"));
                    c.setPassaPor(jsonTexto(data, "passaPor"));
                    c.setImunidades(jsonTexto(data, "imunidades"));

                    c.setAbilities(jsonTexto(data, "habilidades"));
                    c.setLoot(jsonTexto(data, "loot"));
                    c.setComportamento(jsonTexto(data, "comportamento"));
                    c.setLocaisFixos(jsonTexto(data, "locaisFixos"));
                    c.setLocaisInvasao(jsonTexto(data, "locaisInvasao"));

                    c.setBoss(jsonTexto(data, "boss"));
                    c.setBossDo(jsonTexto(data, "bossDo"));

                    criaturaRepository.save(c);
                    salvosNesteCat++;
                }

                totalSalvas += salvosNesteCat;
                log.info("  ✅ {} processamentos no arquivo '{}'", salvosNesteCat, arquivo.getName());

            } catch (Exception e) {
                log.error("Erro ao processar {}: {}", arquivo.getName(), e.getMessage());
            }
        }

        log.info("Importação concluída: {} processamentos efetuados. Criaturas Únicas no BD: {}", totalSalvas,
                nomesUnicos.size());
    }

    private String jsonTexto(JsonNode node, String campo) {
        JsonNode n = node.get(campo);
        return (n != null && !n.isNull()) ? n.asText().trim() : null;
    }

    private String jsonInteiroTexto(JsonNode node, String campo) {
        JsonNode n = node.get(campo);
        if (n == null || n.isNull())
            return null;

        String texto = n.asText().replaceAll("[^0-9]", "");
        return texto.isEmpty() ? null : texto;
    }

    private Integer jsonInteiro(JsonNode node, String campo) {
        JsonNode n = node.get(campo);
        if (n == null || n.isNull())
            return null;

        if (n.isNumber()) {
            return n.asInt();
        }

        try {
            String texto = n.asText().replaceAll("[^0-9]", "");
            return texto.isEmpty() ? null : Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}