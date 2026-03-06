package com.joao.tibia_scrapper.util;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

@Component("dateUtil")
public class DateUtil {

    public String formatarData(LocalDateTime data) {
        if (data == null) return "";

        LocalDateTime agora = LocalDateTime.now();
        Locale ptBR = Locale.forLanguageTag("pt-BR");

        WeekFields weekFields = WeekFields.of(ptBR);
        int semanaDaData = data.get(weekFields.weekOfWeekBasedYear());
        int semanaAtual = agora.get(weekFields.weekOfWeekBasedYear());
        int anoDaData = data.getYear();
        int anoAtual = agora.getYear();

        if (anoDaData == anoAtual && semanaDaData == semanaAtual) {
            return data.format(DateTimeFormatter.ofPattern("EEEE", ptBR)).toLowerCase();
        } else {
            String dataFormatada = data.format(DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy", ptBR));
            
            String[] partes = dataFormatada.split(" ");
            if (partes.length >= 3) {
                partes[2] = partes[2].substring(0, 1).toUpperCase() + partes[2].substring(1);
            }
            return String.join(" ", partes);
        }
    }
}