package com.joao.tibia_scrapper.service;

import com.joao.tibia_scrapper.model.TibiaCoin;
import com.joao.tibia_scrapper.repository.TibiaCoinRepository;
// import org.jsoup.Jsoup;
// import org.jsoup.nodes.Document;
// import org.jsoup.nodes.Element;
// import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class TibiaCoinService {

    @Autowired
    private TibiaCoinRepository repository;

    public static final List<String> LISTA_MUNDOS = Arrays.asList(
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
    }

    private void atualizarDadosMundo(String nomeMundo) {
    }

    // private Integer extrairNumeros(String texto) {
    //     if (texto == null)
    //         return 0;
    //     String n = texto.replaceAll("[^0-9]", "");
    //     return n.isEmpty() ? 0 : Integer.parseInt(n);
    // }

    @Scheduled(cron = "0 0 */3 * * *", zone = "America/Sao_Paulo")
    public void atualizacaoAutomatica() {
        System.out.println(">>> [AUTO] Iniciando atualização agendada (3h)...");
        sincronizarTodosOsMundos(LISTA_MUNDOS);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void rodarAoIniciar() {
        System.out.println(">>> [STARTUP] Verificando Tibia Coins ao ligar...");
        sincronizarTodosOsMundos(LISTA_MUNDOS);
    }

    public String getUltimaAtualizacaoFormatada() {
        return repository.findAll().stream()
                .findFirst()
                .map(tc -> {
                    if (tc.getUltimaAtualizacao() == null)
                        return "Nunca";
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    return tc.getUltimaAtualizacao().format(formatter);
                })
                .orElse("Pendente...");
    }
}