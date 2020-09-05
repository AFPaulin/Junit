package br.ce.wcaquino.servicos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.ce.wcaquino.entidades.Locacao;
import junit.framework.Assert;

public class CalculadoraMockTest {
	
	@Mock
	private Calculadora calcMock;
	
	@Spy
	private Calculadora calcSpy;
	
	@Mock
	private EmailService email;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void devoMostrarDiferencaEntreMockSpy() {
	
		// thenCallRealMethod para caso queira q mock efetue a funcao
		Mockito.when(calcMock.soma(1, 2)).thenReturn(5);
		//Mockito.when(calcSpy.soma(1, 2)).thenReturn(5);
		Mockito.doReturn(5).when(calcSpy).soma(1, 2);
		Mockito.doNothing().when(calcSpy).imprime();
		
		System.out.println("Mock: " + calcMock.soma(1, 2));
		// qdo spy n sabe o q fazer ele retorna a execucao da funcao ao contrario do mock que retorna 0
		System.out.println("Spy: " + calcSpy.soma(1, 2));
		
		System.out.println("Mock");
		calcMock.imprime();
		System.out.println("Spy");
		calcSpy.imprime();
	}
	
	
	@Test
	public void teste() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		
		ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
		
		// n aceita uma funcao com numero fixo e um matcher,ou os dois sao matchers ou
		// sao fixos
		Mockito.when(calc.soma(argCapt.capture(), argCapt.capture())).thenReturn(5);
		
		
		
		Assert.assertEquals(5, calc.soma(1, 8));
	//	System.out.println(argCapt.getAllValues());

	}

}
