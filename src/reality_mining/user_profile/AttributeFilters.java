package reality_mining.user_profile;

public class AttributeFilters {
	private final static String AVAILABLE_PROVIDERS[] = { "AT&TWirel", "AT&T", "T-Mobile", "TMO", "SONERA", "PANNONGSM",
			"WESTEL", "VodafoneG", "VodafoneH", "Cingular", "verizon", "METEOR" };

	private final static String CELLNAMES_SUBSTITUTIONS[][] = { { "ML", "MediaLab" } };

	public static String filterProviderFromStart(String text) {
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

	public static String filterStarFromString(String text) {
		return text.replace("*", "");
	}

	public static String substituteCellnameUserLabel(String label) {
		String result = label;

		for (String p[] : CELLNAMES_SUBSTITUTIONS) {
			if (label.toLowerCase().startsWith(p[0].toLowerCase())) {
				result = p[1] + label.substring(p[0].length());

				break;
			}
		}

		return result;
	}
}
