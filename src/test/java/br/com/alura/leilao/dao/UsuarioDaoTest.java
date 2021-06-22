package br.com.alura.leilao.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
import br.com.alura.leilao.util.builder.UsuarioBuilder;

class UsuarioDaoTest {

	private UsuarioDao dao;
	private EntityManager em;

	@BeforeEach
	public void beforeEach() {
		/*
		 * Preparação de infraestrutura
		 */
		this.em = JPAUtil.getEntityManager();
		this.dao = new UsuarioDao(em);
		this.em.getTransaction().begin();
	}

	@AfterEach
	public void afterEach() {
		this.em.getTransaction().rollback();
	}

	@Test
	void deveriaBuscarUsuarioCadastrado() {

		Usuario usuario = new UsuarioBuilder()
				.comNome("Fulano")
				.comEmail("fulano@email.com")
				.comSenha("12345678")
				.criar();
		em.persist(usuario);
		/*
		 * Realização do teste.
		 */
		Usuario usuarioEncontrado = this.dao.buscarPorUsername(usuario.getNome());
		Assert.assertNotNull(usuarioEncontrado);
	}

	@Test
	void naoDeveriaBuscarUsuarioNaoCadastrado() {
		/*
		 * Cria uma usuário para o teste.
		 */
		Usuario usuario = new UsuarioBuilder()
				.comNome("Fulano")
				.comEmail("fulano@email.com")
				.comSenha("12345678")
				.criar();
		em.persist(usuario);
		/*
		 * Realização do teste. Captura uma exceção lançada pelo método
		 * .getSingleResult() do EM no DAO buscarPorUserName(). Essa exceção é esperada
		 * quando ocorrer a busca pelo usuário que não existe "beltrano".
		 */
		Assert.assertThrows(NoResultException.class, () -> this.dao.buscarPorUsername("beltrano"));
	}
	
	@Test
	void deveriaRemoverUmUsuario() {
		/*
		 * Prepara o teste
		 */
		Usuario usuario = new UsuarioBuilder()
				.comNome("Fulano")
				.comEmail("fulano@email.com")
				.comSenha("12345678")
				.criar();
		em.persist(usuario);
		this.dao.deletar(usuario);
		/*
		 * Executa o teste
		 */
		Assert.assertThrows(NoResultException.class, () -> this.dao.buscarPorUsername(usuario.getNome()));
	}
}
