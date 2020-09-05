package br.ce.wcanquino.suites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.servicos.CalculaValorLocacaoTest;
import br.ce.wcaquino.servicos.CalculadoraTest;
import br.ce.wcaquino.servicos.LocacaoServiceTest;

//@RunWith(Suite.class)
@SuiteClasses({
//	CalculadoraTest.class,
	CalculaValorLocacaoTest.class,
	LocacaoServiceTest.class
})
public class SuiteExecucao {

	

	
}
