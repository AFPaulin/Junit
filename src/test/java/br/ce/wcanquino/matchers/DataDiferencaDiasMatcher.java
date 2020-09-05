package br.ce.wcanquino.matchers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import br.ce.wcaquino.utils.DataUtils;

public class DataDiferencaDiasMatcher extends TypeSafeMatcher<Date> {

	private Integer qtdeDias;
	
	public DataDiferencaDiasMatcher(Integer qtdeDias) {
		this.qtdeDias = qtdeDias;
	}
	
	@Override
	public void describeTo(Description desc) {
		Date dataEsperada = DataUtils.obterDataComDiferencaDias(qtdeDias);
		DateFormat format = new SimpleDateFormat("dd/MM/YYYY");
		desc.appendText(format.format(dataEsperada));
	}

	@Override
	protected boolean matchesSafely(Date data) {
		return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(qtdeDias));
	}

	

}
