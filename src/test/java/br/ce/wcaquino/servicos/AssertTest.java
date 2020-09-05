package br.ce.wcaquino.servicos;

import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;
import junit.framework.Assert;

public class AssertTest {

	@SuppressWarnings("deprecation")
	@Test
	public void test() {
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		
		// primeiro = esperado segundo = obtido
		Assert.assertEquals("Msg de erro",1, 1);
		Assert.assertEquals(0.51, 0.58,0.1);
		Assert.assertEquals("bola", "bola");
		
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		Assert.assertTrue("bola".startsWith("bo"));
		
		
		Usuario u1 = new Usuario("Usuario 1");
		Usuario u2 = new Usuario("Usuario 1");
		Usuario u3 = null;
		
		Assert.assertEquals(u1, u2);
		
		// vertifica se as instancias do objeto é a mesma
		Assert.assertSame(u1, u1);
		
		Assert.assertNull(u3);
		
	}
	
}
