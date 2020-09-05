package br.ce.wcaquino.servicos;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import junit.framework.Assert;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrdemTest {

	public static int contador =0;
	
	@Test	
	public void inicio() {
		contador = 1;
	}
	
	@Test
	public void verifica() {
		Assert.assertEquals(1, contador);
	}
	
	
	
	// Possivel solução nao mto boa para a questao da ordem dos testes
	//@Test
	//public void testGeral() {
	//	inicio();
	//	verifica();
	//}
	
}
