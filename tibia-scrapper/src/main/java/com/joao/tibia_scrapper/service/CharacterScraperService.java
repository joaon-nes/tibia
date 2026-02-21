package com.joao.tibia_scrapper.service;

import com.joao.tibia_scrapper.dto.CharacterDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class CharacterScraperService {

    private static final String TIBIA_URL = "https://www.tibia.com/community/?subtopic=characters&name=";

    public CharacterDTO buscarPersonagem(String nomeChar) {
        try {
            String url = TIBIA_URL + URLEncoder.encode(nomeChar, StandardCharsets.UTF_8);

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            if (doc.text().contains("Could not find character")) {
                return null;
            }

            // Procura a tabela correta
            Element tabelaChar = null;
            for (Element tb : doc.select("table")) {
                if (tb.text().contains("Character Information") && tb.text().contains("Name:")) {
                    tabelaChar = tb;
                    break;
                }
            }

            if (tabelaChar == null)
                return null;

            Elements rows = tabelaChar.select("tr");

            String name = "";
            String vocation = "";
            int level = 0;
            String world = "";
            String residence = "";
            String sex = "";

            for (Element row : rows) {
                Elements cols = row.select("td");
                if (cols.size() < 2)
                    continue;

                String label = cols.get(0).text().replace(":", "").trim();
                String value = cols.get(1).text().trim();

                switch (label) {
                    case "Name":
                        name = value.split(",")[0].trim();
                        if (name.contains("(")) {
                            name = name.substring(0, name.indexOf("(")).trim();
                        }
                        break;
                    case "Vocation":
                        vocation = value;
                        break;
                    case "Level":
                        try {
                            level = Integer.parseInt(value);
                        } catch (Exception e) {
                            level = 0;
                        }
                        break;
                    case "World":
                        world = value;
                        break;
                    case "Residence":
                        residence = value;
                        break;
                }
            }

            if (name.isEmpty())
                return null;
            return new CharacterDTO(name, vocation, level, world, residence, sex);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}