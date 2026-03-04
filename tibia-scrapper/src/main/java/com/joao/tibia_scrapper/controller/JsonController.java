package com.joao.tibia_scrapper.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class JsonController {

    @GetMapping("/json/eventschedule.json")
    public ResponseEntity<String> getEventSchedule() {
        String filePath = "C:/Users/joaon/Desktop/senac/tibia/tibia-scrapper/src/main/resources/static/json/eventschedule.json";
        
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                String content = Files.readString(path);
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(content);
            } else {
                System.err.println("Arquivo de eventos não encontrado em: " + filePath);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{}");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{}");
        }
    }
}