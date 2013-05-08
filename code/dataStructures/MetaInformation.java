package dataStructures;


/** Data Structure to Store Meta-Informations<br>
 * hashCode final changed to key.hashCode in order to Hash a Set of MetaInformations correct
 *  
 * @author S. Berndt
 *
 * @param <T> .toString() should return something useable (not something like Object.toString)
 */
public class MetaInformation<T> {


	
	public static final String	DOC_TITLE	= "Document Title";
	public static final String	DESCRIPTION	= "Description";
	public static final String	AUTHOR		= "Author";

	private final String		key;
	private final T				data;

	public MetaInformation(String key, T data) {
		this.key = key;
		this.data = data;
	}

	public final String getKey() {
		return key;
	}

	public final T getData() {
		return data;
	}

	@Override
	public final int hashCode() {
		return key.hashCode();
	}

	@Override
	public String toString() {
		return data.toString();
	}

}
