package tech.devinhouse.personagens.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import tech.devinhouse.personagens.exception.RegistroExistenteException;
import tech.devinhouse.personagens.model.Personagem;
import tech.devinhouse.personagens.repository.PersonagemRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)  // para usar o Mockito
class PersonagemServiceTest {

    @Mock  // mockando a dependencia da classe que eu quero testar
    private PersonagemRepository repo;

    @InjectMocks  // injentando os mocks como dependencia da classe que eu quero testar
    private PersonagemService service;  // classe que eu quero testar


    @Test
    @DisplayName("Quando nao tem registros, deve retornar lista vazia")
    void consultar_listaVazia() {
        // given
        Mockito.when(repo.findAll()).thenReturn(new ArrayList<>());
        // when
        List<Personagem> resultado = service.consultar();
        // then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Quando tem registros, deve retornar lista com registros")
    void consultar_listaPreenchida() {
        // given
        var lista = List.of(
                new Personagem(1L, 123456789L, "sapato", LocalDate.now().minusYears(20), "Serie do Sapato"),
                new Personagem(2L, 222222222L, "gato", LocalDate.now().minusYears(15), "Serie do Garfield")
        );
        Mockito.when(repo.findAll()).thenReturn(lista);
        // when
        List<Personagem> resultado = service.consultar();
        // then
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(2, resultado.size());
        assertEquals(1, resultado.get(0).getId());
        assertEquals(2, resultado.get(1).getId());
    }

    @Test
    @DisplayName("Quando tenta inserir registro com cpf já existente, deve lançar excecao")
    void inserir_cpfRepetido() {
        Personagem personagem = new Personagem(1L, 123456789L, "super sapato", LocalDate.now().minusYears(20), "Serie do Sapato");
        Mockito.when(repo.existsPersonagemByCpf(Mockito.anyLong())).thenReturn(true);
        assertThrows(RegistroExistenteException.class, () -> service.inserir(personagem));
    }

    @Test
    @DisplayName("Quando tenta inserir registro com cpf já existente, deve lançar excecao")
    void inserir_cpfNaoExistente() {
        // given  (pre-condicoes)
        Personagem personagem = new Personagem(null, 123456789L, "super sapato", LocalDate.now().minusYears(20), "Serie do Sapato");
        Personagem personagemComIdCarregado = new Personagem(1L, 123456789L, "super sapato", LocalDate.now().minusYears(20), "Serie do Sapato");
            // mockar a chamada de verificacao se existe personagem com o cpf informado
        Mockito.when(repo.existsPersonagemByCpf(Mockito.anyLong())).thenReturn(false);
            // mockar a chamada de gravacao do personagem no repositorio
        Mockito.when(repo.save(Mockito.any(Personagem.class))).thenReturn(personagemComIdCarregado);
        // when  (chamada do método que quero testar)
        Personagem personagemInserido = service.inserir(personagem);
        // then (conferindo resultados)
        assertNotNull(personagemInserido);
        assertNotNull(personagemInserido.getId());
        assertEquals(personagem.getNome(), personagemInserido.getNome());
    }
}