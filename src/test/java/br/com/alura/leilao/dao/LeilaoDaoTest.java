package br.com.alura.leilao.dao;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
import br.com.alura.leilao.util.builder.LeilaoBuilder;
import br.com.alura.leilao.util.builder.UsuarioBuilder;

class LeilaoDaoTest {

	private LeilaoDao dao;
	private EntityManager em;

	@BeforeEach
	public void beforeEach() {
		/*
		 * Preparação de infraestrutura
		 */
		this.em = JPAUtil.getEntityManager();
		this.dao = new LeilaoDao(em);
		this.em.getTransaction().begin();
	}

	@AfterEach
	public void afterEach() {
		this.em.getTransaction().rollback();
	}

	@Test
	void deveriaCadastrarUmLeilao() {
		Usuario usuario = new UsuarioBuilder()
				.comNome("Fulano")
				.comEmail("fulano@email.com")
				.comSenha("12345678")
				.criar();
		em.persist(usuario);

		Leilao leilao = new LeilaoBuilder()
				.comNome("Mochila")
				.comValorInicial("500")
				.comData(LocalDate.now())
				.comUsuario(usuario).criar();
		leilao = this.dao.salvar(leilao);

		Leilao salvo = dao.buscarPorId(leilao.getId());
		Assert.assertNotNull(salvo);
	}

	@Test
	void deveriaAtualizarUmLeilao() {
		Usuario usuario = new UsuarioBuilder()
				.comNome("Fulano")
				.comEmail("fulano@email.com")
				.comSenha("12345678")
				.criar();
		em.persist(usuario);

		Leilao leilao = new LeilaoBuilder()
				.comNome("Mochila")
				.comValorInicial("500")
				.comData(LocalDate.now())
				.comUsuario(usuario).criar();
		leilao = this.dao.salvar(leilao);

		Leilao salvo = dao.buscarPorId(leilao.getId());

		salvo.setNome("Celular");
		salvo.setValorInicial(new BigDecimal("400"));

		salvo = this.dao.salvar(salvo);
		Assert.assertEquals("Celular", salvo.getNome());
		Assert.assertEquals(new BigDecimal("400"), salvo.getValorInicial());
	}
}
