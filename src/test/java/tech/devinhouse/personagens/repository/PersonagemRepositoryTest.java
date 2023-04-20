package tech.devinhouse.personagens.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tech.devinhouse.personagens.model.Personagem;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest  // carrega o minimo da parte do sistema relacionado a acesso à Banco de Dados
class PersonagemRepositoryTest {

    @Autowired
    private TestEntityManager em;  // entity manager de teste

    @Autowired
    private PersonagemRepository repo; // classe que está sendo testada

    @Test
    @DisplayName("Quando existem 2 registros sendo que um deles tem data de nasc maior que o parametro, deve retornar somente este registro")
    void consultarPorDataNascimentoPosteriorA_possuiResultados() {
        // pre condicoes (given)
        Personagem pers1 = new Personagem(null, 111L, "Personagem 1", LocalDate.of(1700, Month.JANUARY, 1), "serie 1");
        Personagem pers2 = new Personagem(null, 222L, "Personagem 2", LocalDate.of(1902, Month.JANUARY, 1), "serie 2");
        pers1 = em.persist(pers1);
        pers2 = em.persist(pers2);
        // chamando o metodo a ser testado (when)
        List<Personagem> resultado = repo.consultarPorDataNascimentoPosteriorA(LocalDate.of(1900, Month.JANUARY, 1));
        // conferindo resultados (then)
        assertEquals(1, resultado.size());
        assertEquals(pers2.getNome(), resultado.get(0).getNome());
        assertNotNull(pers1.getId());
        assertNotNull(pers2.getId());
    }

    @Test
    @DisplayName("Quando existem registros mas nenhum tem data de nasc maior que o parametro, deve retornar a lista vazia")
    void consultarPorDataNascimentoPosteriorA_naoPossuiResultados() {
        // pre condicoes (given)
        Personagem pers1 = new Personagem(null, 111L, "Personagem 1", LocalDate.of(1700, Month.JANUARY, 1), "serie 1");
        Personagem pers2 = new Personagem(null, 222L, "Personagem 2", LocalDate.of(1902, Month.JANUARY, 1), "serie 2");
        pers1 = em.persist(pers1);
        pers2 = em.persist(pers2);
        // chamando o metodo a ser testado (when)
        List<Personagem> resultado = repo.consultarPorDataNascimentoPosteriorA(LocalDate.of(1905, Month.JANUARY, 1));
        // conferindo resultados (then)
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Quando nao existem registros, deve retornar a lista vazia")
    void consultarPorDataNascimentoPosteriorA_semRegistros() {
        List<Personagem> resultado = repo.consultarPorDataNascimentoPosteriorA(LocalDate.of(1905, Month.JANUARY, 1));
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Quando existe 1 personagem com parte do nome igual ao parametro, deve retornar este personagem")
    void consultarPorNomeParcial_existe() {
        var pers1 = new Personagem(null, 111L, "Personagem ABC", LocalDate.of(1700, Month.JANUARY, 1), "serie 1");
        var pers2 = new Personagem(null, 222L, "Personagem DEF", LocalDate.of(1902, Month.JANUARY, 1), "serie 2");
        em.persist(pers1);
        em.persist(pers2);
        var resultado = repo.consultarPorNomeParcial("%AB%");
        assertEquals(1, resultado.size());
        assertEquals(pers1.getNome(), resultado.get(0).getNome());
    }

    @Test
    @DisplayName("Quando nao tem registros, deve retornar lista vazia")
    void consultarPorNomeParcial_semRegistros() {
        List<Personagem> resultado = repo.consultarPorNomeParcial("%AB%");
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Quando nao existem personagens cadastrados que atendem ao critério de nome parcial, deve retornar lista vazia")
    void consultarPorNomeParcial_naoExisteRegistro() {
        var pers1 = new Personagem(null, 111L, "Personagem ABC", LocalDate.of(1700, Month.JANUARY, 1), "serie 1");
        var pers2 = new Personagem(null, 222L, "Personagem DEF", LocalDate.of(1902, Month.JANUARY, 1), "serie 2");
        em.persist(pers1);
        em.persist(pers2);
        var resultado = repo.consultarPorNomeParcial("%ZZ%");
        assertTrue(resultado.isEmpty());
    }

}