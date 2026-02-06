package com.joao.tibia_scrapper.service;

import com.joao.tibia_scrapper.model.TibiaCoin;
import com.joao.tibia_scrapper.repository.TibiaCoinRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class TibiaCoinService {

    @Autowired
    private TibiaCoinRepository repository;

    @Transactional
    public void sincronizarTodosOsMundos(List<String> listaMundosManual) {
        System.out.println("Iniciando varredura lenta para " + listaMundosManual.size() + " mundos...");

        for (String nomeMundo : listaMundosManual) {
            try {
                TibiaCoin tc = repository.findByMundo(nomeMundo).orElse(new TibiaCoin());
                tc.setMundo(nomeMundo);
                repository.saveAndFlush(tc);

                atualizarDadosMundo(nomeMundo);

                Thread.sleep(2500);

            } catch (Exception e) {
                System.err.println("Erro ao processar " + nomeMundo + ": " + e.getMessage());
            }
        }
        System.out.println("Varredura minuciosa finalizada.");
    }

    private void atualizarDadosMundo(String nomeMundo) {
        try {
            String url = "https://tibiaprices.com/pt/world/" + nomeMundo.toLowerCase().trim() + "/item/tibia-coins/";
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(20000)
                    .get();

            TibiaCoin tc = repository.findByMundo(nomeMundo).orElseThrow();

            Elements cardBodies = doc.select(".chakra-card__body");
            if (!cardBodies.isEmpty()) {
                Integer preco = extrairNumeros(cardBodies.get(0).text());
                if (preco > 0)
                    tc.setPrecoMedio(preco);
            }

            Element tempoElement = doc.select("p:contains(verificação), .chakra-stat__help-text").first();
            if (tempoElement != null) {
                tc.setTempoReferenciaSite(tempoElement.text());
            }

            tc.setUltimaAtualizacao(LocalDateTime.now());
            repository.saveAndFlush(tc);

        } catch (Exception e) {
            System.out.println(">>> [FALHA] Não foi possível ler dados de " + nomeMundo);
        }
    }

    private Integer extrairNumeros(String texto) {
        if (texto == null)
            return 0;
        String n = texto.replaceAll("[^0-9]", "");
        return n.isEmpty() ? 0 : Integer.parseInt(n);
    }

    @Scheduled(cron = "0 0 */3 * * *") // atualizar a cada 3 horas
    public void atualizacaoAutomatica() {
        List<String> mundos = Arrays.asList(
                "Aethera", "Ambra", "Antica", "Astera", "Belobra", "Blumera", "Bona", "Bravoria",
                "Calmera", "Cantabra", "Celebra", "Celesta", "Citra", "Collabra", "Descubra",
                "Dia", "Divina", "Eclipta", "Epoca", "Escura", "Esmera", "Etebra", "Ferobra",
                "Fibera", "Firmera", "Flamera", "Gentebra", "Gladera", "Gladibra", "Gravitera",
                "Harmonia", "Havera", "Honbra", "Ignitera", "Inabra", "Issobra", "Jacabra",
                "Jadebra", "Jaguna", "Kalanta", "Kalibra", "Kalimera", "Karmeya", "Lobera",
                "Luminera", "Lutabra", "Luzibra", "Malivora", "Menera", "Monstera", "Monza",
                "Mystera", "Nefera", "Nevia", "Noctalia", "Obscubra", "Oceanis", "Ombra",
                "Ourobra", "Pacera", "Peloria", "Penumbra", "Premia", "Quebra", "Quelibra",
                "Quidera", "Quintera", "Rasteibra", "Refugia", "Retalia", "Runera", "Secura",
                "Serdebra", "Solidera", "Sombra", "Sonira", "Stralis", "Talera", "Temera",
                "Tempestera", "Terribra", "Thyria", "Tornabra", "Ulera", "Unebra", "Ustebra",
                "Vandera", "Venebra", "Victoris", "Vitera", "Vunira", "Wadira", "Wildera",
                "Wintera", "Xyla", "Xybra", "Xymera", "Yara", "Yonabra", "Yovera", "Yubra", "Zephyra");

        System.out.println(">>> [AUTO] Iniciando atualização agendada...");
        sincronizarTodosOsMundos(mundos);
    }
}