package foursquare.venue.category;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import main.APIKeys;

/**
 * Database of all available foursquare categories with an update method to
 * fetch the latest version
 * 
 * @author jasper
 *
 */
public class CategoryDB {
	public static final String FOURSQUARE_CATEGORIES_PATH = "data_directory/foursquare/categorie_db.json";

	private Category[] categories;

	/**
	 * Program to fetch the latest version of the foursquare categories and to
	 * write them into a json file
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		CategoryDB db = new CategoryDB();

		db.update();

		db.writeCategoriesToJson();
	}

	/**
	 * Retrieves the latest version of the foursquare categories
	 */
	public void update() {
		String https_url = "https://api.foursquare.com/v2/venues/categories?oauth_token="
				+ APIKeys.FOURSQUARE_OAUTH_TOKEN + "&v=20160720";
		URL url;

		try {

			url = new URL(https_url);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

				Gson gson = new GsonBuilder().create();
				JsonObject jsonObject = gson.fromJson(br, JsonObject.class);

				categories = gson.fromJson(((JsonObject) jsonObject.get("response")).get("categories"),
						Category[].class);

			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the highest top category of a given category-id if there is one
	 * 
	 * @param id
	 *            Category-id to find the top category for
	 * @return Top category if found, otherwise an unknown category object
	 */
	public Category getTopCategory(String id) {
		for (Category c : categories) {
			if (getTopCategoryRecursive(c, id) != null) {
				return c;
			}
		}

		return Category.Unknown();
	}

	private Category getTopCategoryRecursive(Category category, String id) {
		if (category.id.equals(id)) {
			return category;
		}

		for (Category c : category.categories) {
			if (getTopCategoryRecursive(c, id) != null) {
				return c;
			}
		}

		return null;
	}

	/**
	 * Find Category for a given id
	 * 
	 * @param id
	 *            Category-id to search for
	 * @return Found category, otherwise an unknown category object
	 */
	public Category find(String id) {
		for (Category c : categories) {
			Category f = findRecursive(c, id);

			if (f != null) {
				return f;
			}
		}

		return Category.Unknown();
	}

	private Category findRecursive(Category category, String id) {
		if (category.id.equals(id)) {
			return category;
		}

		for (Category c : category.categories) {
			Category f = findRecursive(c, id);

			if (f != null) {
				return f;
			}
		}

		return null;
	}

	/**
	 * Reads database content from json file
	 */
	public void readJsonCategories() {
		try {
			String json = FileUtils.readFileToString(new File(FOURSQUARE_CATEGORIES_PATH), StandardCharsets.UTF_8);
			Gson gson = new Gson();

			categories = gson.fromJson(json, Category[].class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes database content to json file
	 */
	public void writeCategoriesToJson() {
		if (categories != null) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(categories);

			try {
				FileUtils.write(new File(FOURSQUARE_CATEGORIES_PATH), json, StandardCharsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
