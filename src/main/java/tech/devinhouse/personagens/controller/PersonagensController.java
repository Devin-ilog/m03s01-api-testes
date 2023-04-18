package tech.devinhouse.personagens.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.devinhouse.personagens.dto.PersonagemIdadeResponse;
import tech.devinhouse.personagens.dto.PersonagemRequest;
import tech.devinhouse.personagens.dto.PersonagemResponse;
import tech.devinhouse.personagens.dto.PersonagemUpdateRequest;
import tech.devinhouse.personagens.model.Personagem;
import tech.devinhouse.personagens.service.PersonagemService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/personagens")
public class PersonagensController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PersonagemService service;

    @PostMapping
    public ResponseEntity inserir(@RequestBody @Valid PersonagemRequest request) {
        Personagem personagem = modelMapper.map(request, Personagem.class);
        personagem = service.inserir(personagem);
        PersonagemResponse resp = modelMapper.map(personagem, PersonagemResponse.class);
        return ResponseEntity.created(URI.create(resp.getId().toString())).body(resp);
    }

    @GetMapping
    public ResponseEntity<List<PersonagemResponse>> consultar() {
        List<Personagem> personagens = service.consultar();
        List<PersonagemResponse> resp = personagens.stream()
                .map(p -> modelMapper.map(p, PersonagemResponse.class)).toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("{id}")
    public ResponseEntity<PersonagemResponse> consultar(@PathVariable("id") Long id) {
        Personagem personagem = service.consultar(id);
        PersonagemResponse resp = modelMapper.map(personagem, PersonagemResponse.class);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("cpf/{cpf}")
    public ResponseEntity<PersonagemResponse> consultarPorCPF(@PathVariable("cpf") Long cpf) {
        Personagem personagem = service.consultarPor(cpf);
        PersonagemResponse resp = modelMapper.map(personagem, PersonagemResponse.class);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("{id}")
    public ResponseEntity<PersonagemResponse> alterar(@PathVariable("id") Long id,
                                                      @RequestBody @Valid PersonagemUpdateRequest request) {
        Personagem personagem = modelMapper.map(request, Personagem.class);
        personagem = service.alterar(personagem);
        PersonagemResponse resp = modelMapper.map(personagem, PersonagemResponse.class);
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}/idade")
    public ResponseEntity<PersonagemIdadeResponse> consultarIdade(@PathVariable("id") Long id) {
        Integer idade = service.consultarIdade(id);
        String nome = service.consultarNome(id);
        var resp = new PersonagemIdadeResponse(nome, idade);
        return ResponseEntity.ok(resp);
    }

}
