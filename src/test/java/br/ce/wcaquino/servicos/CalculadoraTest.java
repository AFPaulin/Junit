package br.ce.wcaquino.servicos;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import br.ce.wcanquino.runners.ParallelRunner;
import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import junit.framework.Assert;

// Se voce nao coloca que classe ele ira rodar ele considera como padrao
// o JUnit

//@RunWith(JUnit4.class) ou (BlockJUnit4ClassRunner.class)

@RunWith(ParallelRunner.class)
@SuppressWarnings("deprecation")
public class CalculadoraTest {

	
	private Calculadora calc;
	
	@Before
	public void setup() {
		calc = new Calculadora();
		System.out.println("iniciando...");
	}
	
	@After
	public void tearDowwn() {
		System.out.println("finalizando");
	}
	
	@Test
	public void deveSomarDoisValores() {
	// cenario
	int a=5;
	int b=3;

		
		
	//acao
	int resultado = calc.soma(a,b);
	
	//verificacao
	Assert.assertEquals(8, resultado);
		
	}
	
	@Test
	public void deveSubtraiValores() {
		
	int a=8;
	int b=5;

	
	int resultado = calc.subtracao(a,b);
	
	
	Assert.assertEquals(3, resultado);
		
	}
	
	@Test
	public void deveDividirValores() throws NaoPodeDividirPorZeroException {
		
	int a=8;
	int b=5;

	
	double resultado = calc.divisao(a,b);
	
	
	Assert.assertEquals(1.60, resultado,0.01);
		
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		
		int a=10;
		int b=0;
		
		
		calc.divisao(a,b);
		 
		
	}
	

	
	
	
}
