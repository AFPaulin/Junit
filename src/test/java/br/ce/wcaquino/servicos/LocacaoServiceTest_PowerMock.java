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


import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.configuration.PowerMockConfiguration;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.powermock.reflect.internal.WhiteboxImpl;

import br.ce.wcanquino.builders.LocacaoBuilder;
import br.ce.wcanquino.matchers.MatchesProprios;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.daos.LocacaoDAOFake;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import buildermaster.BuilderMaster;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService.class})
public class LocacaoServiceTest_PowerMock {

	@InjectMocks
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
		service = PowerMockito.spy(service);
		
		/****** Instancianção sem uso de annotations mto mais trabalhoso ********/
		
		//service = new LocacaoService();
		//dao = Mockito.mock(LocacaoDAO.class);
		//service.setLocacaoDAO(dao);
		//spc = Mockito.mock(SPCService.class);
		//service.setSPCService(spc);
		//email = Mockito.mock(EmailService.class);
		//service.setEmailService(email);
	}
	
	//@After
	
	//@BeforeClass
		
	// @AfterClass
	

	
	@Test
	public void deveAlugarFilme() throws Exception {
		
		//CENARIO
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());
	
		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(28, 4, 2017));

		// Mock com membros estaticos
		//Calendar calendar = Calendar.getInstance();
		//calendar.set(Calendar.DAY_OF_MONTH, 28);
		//calendar.set(Calendar.MONTH, Calendar.APRIL);
		//calendar.set(Calendar.YEAR, 2017);
		//PowerMockito.mockStatic(Calendar.class);
		//PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);
		
		
		//AÇAO
		Locacao locacao = service.alugarFilme(usuario, filmes);
			
		//VERIFICAÇÃO
		error.checkThat(locacao.getValor(),is(equalTo(5.0)));
		
		//error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()),is(true));
		//error.checkThat(locacao.getDataLocacao(), ehHoje());
		//error.checkThat(locacao.getDataRetorno(), ehHojeComDiferençaDias(1));
		
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)),is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)),is(true));
	}	
	
	@Test
	public void naoDeveDevolverFilmeDomingo() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		// Mock em construtores
		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(29, 4, 2017));
		
		// Mock com membros estaticos
		//Calendar calendar = Calendar.getInstance();
		//calendar.set(Calendar.DAY_OF_MONTH, 29);
		//calendar.set(Calendar.MONTH, Calendar.APRIL);
		//calendar.set(Calendar.YEAR, 2017);
		//PowerMockito.mockStatic(Calendar.class);
		//PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);
		
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		//assertThat(retorno.getDataRetorno(),caiEm(Calendar.SUNDAY));
		assertThat(retorno.getDataRetorno(),MatchesProprios.caiNumaSegunda());
		//PowerMockito.verifyNew(Date.class,Mockito.times(2)).withNoArguments();
		//PowerMockito.verifyStatic(Mockito.times(2));
		//Calendar.getInstance();
		
	}
	
	
	@Test
	public void deveAlugarFilme_SemCalcularValor() throws Exception{
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes);
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificacao
		Assert.assertThat(locacao.getValor(), is(1.0));
		PowerMockito.verifyPrivate(service).invoke("calcularValorLocacao", filmes);
	}
	
	@Test
	public void deveCalcularValorLocacao() throws Exception{
		//cenario
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		//acao
		Double valor = (Double) Whitebox.invokeMethod(service, "calcularValorLocacao", filmes);
		
		//verificacao
		Assert.assertThat(valor, is(4.0));
	}
	
	
	
	
	
	
}
