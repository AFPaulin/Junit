package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class Calculadora {

	public int soma(int a, int b) {
		System.out.println("Estou executando o metodo somar");
		return a+b;
	}

	public int subtracao(int a, int b) {
		return a-b;
	}

	public double divisao(int a, int b) throws NaoPodeDividirPorZeroException {
		double c = (double) a;
		double d = (double) b;
		if (d ==0) {
			throw new NaoPodeDividirPorZeroException();
		}
		return c/d;
	}
	
	public void imprime() {
		System.out.println("Passei aqui");
	}
	
	//public int divisao(String a,String b) {
	//	return Integer.valueOf(a)/Integer.valueOf(b);
	//}

	//public static void main(String[] args) {
	//	new Calculadora().divisao("5", "0");
	//}
	
}
