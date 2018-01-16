public class DatabaseMessageQuery {
	private String tableName;
	private String messageColumn;
	private String codeColumn;
	private String localeColumn;

	public DatabaseMessageQuery() {
	}

	public DatabaseMessageQuery(String tableName, String codeColumn, String localeColumn, String messageColumn) {
		this.tableName = tableName;
		this.codeColumn = codeColumn;
		this.localeColumn = localeColumn;
		this.messageColumn = messageColumn;
	}

	public String generateQuery(String defaultQuery) {
		validateNullOrEmpty(this.tableName, "tableName");
		validateNullOrEmpty(this.codeColumn, "codeColumn");
		validateNullOrEmpty(this.localeColumn, "localeColumn");
		validateNullOrEmpty(this.messageColumn, "messageColumn");

		StringBuilder query = null;

		if (NullUtil.isNone(defaultQuery)) {
			query = new StringBuilder("SELECT ");
			query.append(this.codeColumn);
			query.append(", ");
			query.append(this.localeColumn);
			query.append(", ");
			query.append(this.messageColumn);
			query.append(" FROM ");
			query.append(this.tableName);
		} else {
			query = new StringBuilder(defaultQuery);
		}
		return query.toString();
	}

	public String generateQuery(String codeCondition, String localeCondition, String defaultQuery) {
		StringBuilder query = new StringBuilder(generateQuery(defaultQuery));

		if (codeCondition != null) {
			query.append(" WHERE ");
			query.append(this.codeColumn);
			query.append(" = ");
			query.append("'").append(codeCondition).append("'");
		}

		if (localeCondition != null) {
			query.append((codeCondition == null) ? " WHERE " : " AND ");
			query.append(this.localeColumn);
			query.append(" = ");
			query.append("'").append(localeCondition).append("'");
		}

		return query.toString();
	}

	private void validateNullOrEmpty(String value, String name) {
		if ((value == null) || ("".equals(value)))
			throw new IllegalArgumentException(name + " is null or empty.");
	}
}
