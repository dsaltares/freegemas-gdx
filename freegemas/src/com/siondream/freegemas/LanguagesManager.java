package com.siondream.freegemas;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class LanguagesManager {
	private static LanguagesManager _instance = null;
	
	private static final String LANGUAGES_FILE = "data/languages.xml";
	private static final String DEFAULT_LANGUAGE = "en_UK";
	
	//private HashMap<String, HashMap<String, String>> _strings = null;
	private HashMap<String, String> _language = null;
	private String _languageName = null;
	
	private LanguagesManager() {
		// Create language map
		_language = new HashMap<String, String>();
		
		// Try to load system language
		// If it fails, fallback to default language
		_languageName = java.util.Locale.getDefault().toString();
		if (!loadLanguage(_languageName)) {
			loadLanguage(DEFAULT_LANGUAGE);
			_languageName = DEFAULT_LANGUAGE;
		}
	}
	
	public static LanguagesManager getInstance() {
		if (_instance == null) {
			_instance = new LanguagesManager();
		}
		
		return _instance;
	}
	
	public String getLanguage() {
		return _languageName;
	}

	public String getString(String key) {
		String string;
		
		if (_language != null) {
			// Look for string in selected language
			string = _language.get(key);
			
			if (string != null) {
				return string;
			}
		}
	
		// Key not found, return the key itself
		return key;
	}
	
	private boolean loadLanguage(String languageName) {
		try {
			XmlReader reader = new XmlReader();
			Element root = reader.parse(Gdx.files.internal(LANGUAGES_FILE));
			
			Array<Element> languages =  root.getChildrenByNameRecursively("language");
			 
			// Iterate through every language to fetch the one we're interested in
			 for (int i = 0; i < languages.size; ++i) {
				 Element languageElement = languages.get(i);
				 
				 // If it's the language we're looking for
				 if (languageElement.getAttribute("name").equals(languageName)) {
					 // Empty language
					 _language.clear();
					 
					// Fetch every string for that language
					 Array<Element> strings = languageElement.getChildrenByName("string");
					 
					 for (int j = 0; j < strings.size; ++j) {
						 Element stringElement = strings.get(j);
						 _language.put(stringElement.getAttribute("key"), stringElement.getAttribute("value"));
					 }
					 
					 return true;
				 }
			 }
			
		}
		catch (Exception e) {
			System.out.println("Error loading languages file " + LANGUAGES_FILE);
		}
		
		return false;
	}
}
