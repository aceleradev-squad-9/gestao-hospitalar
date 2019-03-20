package gestao.util;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe responsável pelas operações de conversão e tratamento relacionadas às datas.
 * 
 * @author edmilson.santana
 *
 */
public class DateUtil {

	private static final Logger LOG = LogManager.getLogger(DateUtil.class);
	private static final String DATE_FORMAT = "dd/MM/yyyy";

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

	/**
	 * Método responsável por converter uma {@link String} em um {@link LocalDate}.
	 * 
	 * @param str - {@link String}
	 * @return data em formato {@link LocalDate}
	 */
	public static LocalDate toDate(String str) {
		LocalDate date = null;

		try {
			date = LocalDate.parse(str, formatter);
		} catch (DateTimeException e) {
			LOG.debug(e.getMessage(), e);
		}

		return date;
	}

	/**
	 * Método responsável por converter um {@link LocalDate} em uma {@link String}.
	 * 
	 * @param date - {@link LocalDate}
	 * @return data em formato {@link String}
	 */
	public static String toStr(LocalDate date) {
		String str = null;
		try {
			str = formatter.format(date);
		} catch (DateTimeException e) {
			LOG.debug(e.getMessage(), e);
		}
		return str;
	}
}
