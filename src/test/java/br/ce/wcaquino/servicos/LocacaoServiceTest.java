package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static br.ce.wcanquino.matchers.MatchesProprios.ehHoje;
import static br.ce.wcanquino.matchers.MatchesProprios.ehHojeComDiferençaDias;
import static br.ce.wcanquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcanquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcanquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcanquino.builders.LocacaoBuilder.umLocacao;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.configuration.PowerMockConfiguration;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.internal.WhiteboxImpl;

import br.ce.wcanquino.builders.LocacaoBuilder;
import br.ce.wcanquino.matchers.MatchesProprios;
import br.ce.wcanquino.runners.ParallelRunner;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.daos.LocacaoDAOFake;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import buildermaster.BuilderMaster;

@RunWith(ParallelRunner.class)
public class LocacaoServiceTest {

	@InjectMocks @Spy
	private LocacaoService service;
	
	@Mock
	private SPCService spc;
	@Mock
	private LocacaoDAO dao;
	@Mock
	private EmailService email;
	
	//variaveis estaticas o junit não reinicializa
	
	// junit não garante a ordem de execução dos testes na
	// mesma ordem da declaração
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule 
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		System.out.println("Iniciando 2...");
	}
	
	@After
	public void encer() {
		System.out.println("Finalizando 2...");
	}

	
	@Test
	public void naoDeveDevolverFilmeDomingo() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		// Com Spy do ObterData
		Mockito.doReturn(DataUtils.obterData(29, 4, 2017)).when(service).obterData();
						
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		assertThat(retorno.getDataRetorno(),MatchesProprios.caiNumaSegunda());
		
	}
	
	@Test
	public void deveAlugarFilme() throws Exception {
		
		//CENARIO
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());
	
		// Com Spy do ObterData
		Mockito.doReturn(DataUtils.obterData(27, 4, 2017)).when(service).obterData();
		
		//AÇAO
		Locacao locacao = service.alugarFilme(usuario, filmes);
			
		//VERIFICAÇÃO
		error.checkThat(locacao.getValor(),is(equalTo(5.0)));
		
	
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(27, 4, 2017)),is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(28, 4, 2017)),is(true));
		
		
	}	
	
	//Solução elegante - funciona bem quando voce sabe que a exceção só sera lançada por 
	// esse motivo
	@Test(expected = FilmeSemEstoqueException.class) 
	public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());
	
		
		//AÇAO
		service.alugarFilme(usuario, filmes);
	}
	
	//Solução robusta - alem de capturar exceção vc vai pode ver a 
	// msg da exceção - recomendada pelo instrutor
	@Test
	public void deveLancarExcecaoAoAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		//AÇAO
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}

	}
	
	// formula nova - tb mostra msg
	@Test
	public void deveLancarExcecaoAoAlugarFilmeSemFilme() throws LocadoraException, FilmeSemEstoqueException {
		Usuario usuario = umUsuario().agora();

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		
		//AÇAO
		service.alugarFilme(usuario, null);
		
		
		
	}
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Usuario 2").agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		Mockito.when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);
		
		try {
			service.alugarFilme(usuario, filmes);
			//verificacao
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuario Negativado"));
		}
		

		Mockito.verify(spc).possuiNegativacao(usuario);
		
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		//cenario
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = umUsuario().comNome("Outro atrasado").agora();
		List<Locacao> locacoes = 
				Arrays.asList(
				umLocacao().atrasado().comUsuario(usuario).agora(),
				umLocacao().comUsuario(usuario2).agora(),
				umLocacao().atrasado().comUsuario(usuario3).agora(),
				umLocacao().atrasado().comUsuario(usuario3).agora());
		
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		
		//acao
		service.notificarAtrasos();
		
		//verificacao - any Usuario.class - fica bem generico porem fica mais facil
		Mockito.verify(email,Mockito.times(3)).notificarAtraso(Mockito.any(Usuario.class));
		// Mockito.times(2) ou Mockito.atleast(2) ou atLeasOnce
		Mockito.verify(email,Mockito.atLeastOnce()).notificarAtraso(usuario3);
		Mockito.verify(email, Mockito.never()).notificarAtraso(usuario2);
		Mockito.verifyNoMoreInteractions(email);
		// como o servico nao usa spc nao precisa Mockito.verifyZeroInteractions(spc);
		
	}
	
	
	 @Test
	 public void deveTratarErronoSPC() throws Exception {
		 
		 Usuario usuario = umUsuario().agora();
		 List<Filme> filmes = Arrays.asList(umFilme().agora());
		 
		 when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catratrófica"));
		 
		 // Existe dois tipos de excecoes: 
		 // exceção checada e nao checadas
		 // Runtime Exception(tempo de execucao) é uma excecao nao checada ,todas as outras sao checadas
		 
		 exception.expect(LocadoraException.class);
		 exception.expectMessage("Problemas com SPC, tente novamente");
		 
		 service.alugarFilme(usuario, filmes);
		 
		 
	 }
	
	 @Test
	 public void deveProrrogarUmaLocacao() {
		 
		 Locacao locacao = LocacaoBuilder.umLocacao().agora();
		 
		 service.prorrogarLocacao(locacao, 3);
		 
		 ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		 Mockito.verify(dao).salvar(argCapt.capture());
		 Locacao locacaoRetornado = argCapt.getValue();
		 
		 error.checkThat(locacaoRetornado.getValor(), is(12.0));
		 error.checkThat(locacaoRetornado.getDataLocacao(), ehHoje());
		 error.checkThat(locacaoRetornado.getDataRetorno(), ehHojeComDiferençaDias(3));
	 }
	 
 
	 // usar metodos privados sem uso de powermock
	 @Test
	 public void deveCalcularValorLocacao() throws Exception {
		 List<Filme> filmes = Arrays.asList(umFilme().agora());
		 
		 Class<LocacaoService> clazz = LocacaoService.class;
		 Method metodo = clazz.getDeclaredMethod("calcularValorLocacao", List.class);
		 metodo.setAccessible(true);
		 Double valor =(Double) metodo.invoke(service, filmes);
		 
		 
		 Assert.assertThat(valor, is(4.0));
	 }
	
	
}
