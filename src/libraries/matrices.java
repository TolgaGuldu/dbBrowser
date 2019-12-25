package libraries;

public class matrices {
	public String fieldName;
	public String value;

	/**
	 * @param fieldName selected table fields
	 * @param value selected row values respect to the fields
	 */
	public matrices(String fieldName, String value) {
		this.fieldName = fieldName;
		this.value = value;
	}
}
