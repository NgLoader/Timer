package de.ngloader.timer.api.i18n;

import java.text.FieldPosition;
import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class PercentFormat extends Format {

	private static final long serialVersionUID = 7743045749277601549L;

	private static final String FORMAT = "{0,number,#.##%}";

	private static final PercentFormat INSTANCE = new PercentFormat();

	public static PercentFormat getPercentInstance(Locale locale) {
		return INSTANCE;
	}

	private PercentFormat() {
	}

	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
		return new StringBuffer(MessageFormat.format(FORMAT, (String) obj));
	}

	@Override
	public Object parseObject(String source, ParsePosition pos) {
		return null;
	}
}