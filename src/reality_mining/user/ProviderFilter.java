package reality_mining.user;

public class ProviderFilter {
	private final static String AVAILABLE_PROVIDERS[] = { "AT&TWirel", "AT&T", "T-Mobile", "TMO" };

	public static String removeProviderFromStart(String text) {
		String filteredText = text;
		String lowerText = text.toLowerCase();

		for (String p : AVAILABLE_PROVIDERS) {
			String lowerProvider = p.toLowerCase();

			if (lowerText.startsWith(lowerProvider)) {
				filteredText = text.substring(lowerProvider.length());
				break;
			}
		}

		return filteredText;
	}
}
