package tech.devinhouse.personagens.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.devinhouse.personagens.exception.RegistroExistenteException;
import tech.devinhouse.personagens.exception.RegistroNaoEncontradoException;
import tech.devinhouse.personagens.model.Personagem;
import tech.devinhouse.personagens.repository.PersonagemRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @DisplayName("Quando tenta inserir registro com cpf não existente, deve inserir registro")
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

    @Test
    @DisplayName("Quando existe um personagem com o id informado, deve retornar este personagem")
    void consultarPorId() {
        Long id = 1L;
        Personagem personagem = new Personagem(1L, 123456789L, "super sapato", LocalDate.now().minusYears(20), "Serie do Sapato");
        Mockito.when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(personagem));
        Personagem resultado = service.consultar(id);
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
    }

    @Test
    @DisplayName("Quando nao existe um personagem com o id informado, deve lançar exceção")
    void consultarPorId_naoExistente() {
        Mockito.when(repo.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(RegistroNaoEncontradoException.class, () -> service.consultar(1L));
    }

    @Test
    @DisplayName("Quando existe registro com o cpf informado, deve retornar este registro")
    void consultarPor_registroExistente() {
        // pre-condicoes
        Long cpf = 12345678901L;
        Personagem personagem = new Personagem(10L, cpf, "super sapato", LocalDate.now().minusYears(20), "Serie do Sapato");
        Mockito.when(repo.findByCpf(Mockito.anyLong())).thenReturn(Optional.of(personagem));
        // chamada do servico
        Personagem resultado = service.consultarPor(cpf);
        // conferindo
        assertNotNull(resultado);
        assertEquals(cpf, resultado.getCpf());
    }

    @Test
    @DisplayName("Quando nao existe registro com o cpf informando, deve lançar exceção")
    void consultarPor_naoExistente() {
        Long cpf = 12345678901L;
        Mockito.when(repo.findByCpf(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(RegistroNaoEncontradoException.class, () -> service.consultarPor(cpf));
    }

    @Test
    @DisplayName("Quando existe o registro com o id informando, deve ser excluido")
    void excluir_existente() {
        Long id = 1L;
        Mockito.when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(new Personagem()));
        assertDoesNotThrow(() -> service.excluir(id));
    }

    @Test
    @DisplayName("Quando nao existe o registro com o id informando, deve lancar exceção")
    void excluir_naoExistente() {
        Long id = 1l;
        Mockito.when(repo.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(RegistroNaoEncontradoException.class, () -> service.excluir(id));
    }

    @Test
    @DisplayName("Quando existe personagem com o id informando, deve retornar seu nome")
    void consultarNome() {
        Long id = 10L;
        Personagem personagem = new Personagem(id, 12345678901L, "super sapato", LocalDate.now().minusYears(20), "Serie do Sapato");
        Mockito.when(repo.findById(Mockito.anyLong())).thenReturn(Optional.of(personagem));
        String nomeRetornado = service.consultarNome(id);
        assertNotNull(nomeRetornado);
        assertEquals(personagem.getNome(), nomeRetornado);
    }
    
    //TODO: COMPLEMENTAR COM OUTROS TESTES ...

}