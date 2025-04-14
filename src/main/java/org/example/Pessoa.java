package com.exemplo.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    private final Map<Long, PessoaDTO> pessoas = new HashMap<>();
    private final AtomicLong contador = new AtomicLong(1);

    @PostMapping
    public ResponseEntity<PessoaDTO> criarPessoa(@RequestBody PessoaDTO dto) {
        long id = contador.getAndIncrement();
        dto.setId(id);
        pessoas.put(id, dto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PessoaDTO>> listarPessoas() {
        return ResponseEntity.ok(new ArrayList<>(pessoas.values()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaDTO> buscarPorId(@PathVariable Long id) {
        PessoaDTO dto = pessoas.get(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaDTO> atualizarPessoa(@PathVariable Long id, @RequestBody PessoaDTO dto) {
        if (!pessoas.containsKey(id)) return ResponseEntity.notFound().build();
        dto.setId(id);
        pessoas.put(id, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPessoa(@PathVariable Long id) {
        if (!pessoas.containsKey(id)) return ResponseEntity.notFound().build();
        pessoas.remove(id);
        return ResponseEntity.noContent().build();
    }


    static class PessoaDTO {
        private Long id;
        private String nome;
        private String email;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}
