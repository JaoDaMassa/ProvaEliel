package com.exemplo.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/trabalhos")
public class TrabalhoController {

    private final Map<Long, TrabalhoDTO> trabalhos = new HashMap<>();
    private final AtomicLong contador = new AtomicLong(1);

    // Simula uma chamada para verificar se a pessoa existe (normalmente seria via service/repository)
    private boolean pessoaExiste(Long id) {
        String url = "http://localhost:8080/pessoas/existe/" + id;
        RestTemplate restTemplate = new RestTemplate();
        try {
            return Boolean.TRUE.equals(restTemplate.getForObject(url, Boolean.class));
        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping
    public ResponseEntity<Object> criarTrabalho(@RequestBody TrabalhoDTO dto) {
        if (!pessoaExiste(dto.getPessoaId())) {
            return new ResponseEntity<>("Pessoa não encontrada!", HttpStatus.BAD_REQUEST);
        }

        long id = contador.getAndIncrement();
        dto.setId(id);
        trabalhos.put(id, dto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TrabalhoDTO>> listarTrabalhos() {
        return ResponseEntity.ok(new ArrayList<>(trabalhos.values()));
    }

    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<TrabalhoDTO>> listarPorPessoa(@PathVariable Long pessoaId) {
        List<TrabalhoDTO> lista = new ArrayList<>();
        for (TrabalhoDTO t : trabalhos.values()) {
            if (t.getPessoaId().equals(pessoaId)) {
                lista.add(t);
            }
        }
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarTrabalho(@PathVariable Long id, @RequestBody TrabalhoDTO dto) {
        if (!trabalhos.containsKey(id)) return ResponseEntity.notFound().build();
        if (!pessoaExiste(dto.getPessoaId())) return new ResponseEntity<>("Pessoa não encontrada!", HttpStatus.BAD_REQUEST);

        dto.setId(id);
        trabalhos.put(id, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTrabalho(@PathVariable Long id) {
        if (!trabalhos.containsKey(id)) return ResponseEntity.notFound().build();
        trabalhos.remove(id);
        return ResponseEntity.noContent().build();
    }

    static class TrabalhoDTO {
        private Long id;
        private String titulo;
        private String descricao;
        private Long pessoaId;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }

        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }

        public Long getPessoaId() { return pessoaId; }
        public void setPessoaId(Long pessoaId) { this.pessoaId = pessoaId; }
    }
}
