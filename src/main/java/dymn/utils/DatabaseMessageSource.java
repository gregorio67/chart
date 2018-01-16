import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;

import ncd.spring.common.exception.SysException;
import ncd.spring.common.util.NullUtil;

public class DatabaseMessageSource extends AbstractMessageSource implements RefreshableMessageSource {
	protected String loadType;
	private Map<String, Map<Locale, MessageFormat>> cachedMessageFormats;
	private Map<String, Map<Locale, MessageFormat>> tempCachedMessageFormats;
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseMessageSource.class);
	private JdbcTemplate jdbcTemplate;
	private String tableName;
	private String messageColumn;
	private String codeColumn;
	private String localeColumn;
	private String defaultQuery;

	public DatabaseMessageSource() {
		this.loadType = "preLoad";

		this.cachedMessageFormats = new HashMap();

		this.tempCachedMessageFormats = new HashMap();

		this.tableName = "MESSAGES";

		this.messageColumn = "MESSAGE";

		this.codeColumn = "CODE";

		this.localeColumn = "LOCALE";

		this.defaultQuery = "";
	}

	public void setLoadType(String loadType) {
		if (("preLoad".equals(loadType)) || ("lazyLoad".equals(loadType)))
			this.loadType = loadType;
		else
			throw new SysException("Message load mode should be defined among preLoad,lazyLoad");
	}

	public void setDataSource(DataSource dataSource) {
		if (this.jdbcTemplate == null)
			this.jdbcTemplate = new JdbcTemplate(dataSource);
		else if (dataSource != this.jdbcTemplate.getDataSource())
			this.jdbcTemplate.setDataSource(dataSource);
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setMessageColumn(String messageColumn) {
		this.messageColumn = messageColumn;
	}

	public void setCodeColumn(String codeColumn) {
		this.codeColumn = codeColumn;
	}

	public void setLocaleColumn(String localeColumn) {
		this.localeColumn = localeColumn;
	}

	public void setDefaultQuery(String defaultQuery) {
		this.defaultQuery = defaultQuery;
	}

	protected Map<String, Map<Locale, MessageFormat>> getCachedMessageFormats() {
		return this.cachedMessageFormats;
	}

	protected MessageFormat resolveCode(String code, Locale locale) {
		MessageFormat messageFormat = getMessageFormat(code, locale);
		return messageFormat;
	}

	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		String message = super.resolveCodeWithoutArguments(code, locale);
		return message;
	}

	public void afterPropertiesSet() throws Exception {
		if (isPreLoad())
			refresh();
	}

	public void refresh() {
		if (isLazyLoad()) {
			synchronized (this.cachedMessageFormats) {
				this.cachedMessageFormats.clear();
			}

		} else
			synchronized (this.cachedMessageFormats) {
				Map tempCachedMessageFormats = createTempCacheMessageMap();
				this.cachedMessageFormats = tempCachedMessageFormats;
			}
	}

	public void refreshIncludingAncestors() {
		refresh();
		MessageSource parentMessageSource = getParentMessageSource();
		if (parentMessageSource instanceof RefreshableMessageSource)
			((RefreshableMessageSource) parentMessageSource).refreshIncludingAncestors();
	}

	protected List<Map<String, Object>> readMessageListFromDatabase(String code, String locale) {
		if (this.jdbcTemplate != null) {
			DatabaseMessageQuery queryObj = new DatabaseMessageQuery(this.tableName, this.codeColumn, this.localeColumn,
					this.messageColumn);
			String generatedQuery = queryObj.generateQuery(code, locale, this.defaultQuery);
			return this.jdbcTemplate.queryForList(generatedQuery);
		}
		throw new SysException("Datasource must be defined");
	}

	private Locale getLocale(String localeStr) {
		Locale locale = null;
		if (localeStr.contains("_")) {
			String[] compLocale = localeStr.split("_");
			if (compLocale.length == 3)
				locale = new Locale(compLocale[0], compLocale[1], compLocale[2]);
			else if (compLocale.length == 2)
				locale = new Locale(compLocale[0], compLocale[1]);
		} else {
			locale = new Locale(localeStr);
		}
		return locale;
	}

	protected Map<String, Map<Locale, MessageFormat>> createTempCacheMessageMap() {
		List<Map<String, Object>> messageList = readMessageListFromDatabase(null, null);

		Map tempCachedMessageFormats = new HashMap();

		for (Map messageInfo : messageList) {
			String code = (String) messageInfo.get(this.codeColumn);
			String message = (String) messageInfo.get(this.messageColumn);
			Locale locale = getLocale((String) messageInfo.get(this.localeColumn));

			if (NullUtil.notNone(code)) {
				Map messageMap = (Map) tempCachedMessageFormats.get(code);
				if (messageMap == null) {
					messageMap = new HashMap();
					messageMap.put(locale, createMessageFormat(message, locale));
					tempCachedMessageFormats.put(code, messageMap);
				} else if (messageMap.get(locale) == null) {
					messageMap.put(locale, createMessageFormat(message, locale));
				} else {
					LOGGER.error("Message code, locale (PK) are duplicated in message db");
				}
			}

		}

		return tempCachedMessageFormats;
	}

	protected MessageFormat getMessageFormat(String code, Locale locale) {
		MessageFormat messageFormat = null;
		Map localeMap = (Map) this.cachedMessageFormats.get(code);
		if (localeMap != null) {
			List<Locale> localePriority = calculateLocalePriority(locale);
			for (Locale localLocale : localePriority) {
				messageFormat = (MessageFormat) localeMap.get(localLocale);
				if (messageFormat != null)
					break;
			}
		} else if (isLazyLoad()) {
			List<Locale> localePriority = calculateLocalePriority(locale);
			for (Locale localLocale : localePriority) {
				List messageList = readMessageListFromDatabase(code, localLocale.toString());
				if (!(NullUtil.isNone(messageList))) {
					Map messageInfo = (Map) messageList.get(0);
					String message = (String) messageInfo.get(this.messageColumn);
					if (message != null) {
						if (localeMap == null) {
							localeMap = new HashMap();
						}
						messageFormat = new MessageFormat(message, localLocale);
						localeMap.put(locale, messageFormat);
						this.cachedMessageFormats.put(code, localeMap);
						break;
					}
				}
			}
		}

		return messageFormat;
	}

	protected List<Locale> calculateLocalePriority(Locale locale) {
		List locales = null;
		if (locale != null) {
			locales = new ArrayList();

			String country = locale.getCountry();
			String language = locale.getLanguage();
			String variant = locale.getVariant();

			if ((!("".equals(variant))) && (!("".equals(country))) && (!("".equals(language)))) {
				locales.add(new Locale(language, country, variant));
			}

			if ((!("".equals(country))) && (!("".equals(language)))) {
				locales.add(new Locale(language, country));
			}

			if (!("".equals(language))) {
				locales.add(new Locale(language));
			}
		}
		return locales;
	}

	public void clearCache() {
		LOGGER.debug("Clearing entire resource bundle cache");
		synchronized (this.cachedMessageFormats) {
			this.cachedMessageFormats.clear();
		}
		synchronized (this.tempCachedMessageFormats) {
			this.tempCachedMessageFormats.clear();
		}
	}

	public void clearCacheIncludingAncestors() {
		clearCache();
		MessageSource parentMessageSource = getParentMessageSource();
		if (parentMessageSource instanceof RefreshableMessageSource)
			((RefreshableMessageSource) parentMessageSource).clearCacheIncludingAncestors();
	}

	private boolean isPreLoad() {
		return "preLoad".equals(this.loadType);
	}

	private boolean isLazyLoad() {
		return "lazyLoad".equals(this.loadType);
	}
}
