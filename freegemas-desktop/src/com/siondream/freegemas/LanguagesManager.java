package com.siondream.freegemas;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class LanguagesManager {
	private static LanguagesManager _instance = null;
	
	private static final String LANGUAGES_FILE = "data/languages.xml";
	
	private HashMap<String, HashMap<String, String>> _strings = null;
	private HashMap<String, String> _currentLanguage = null;
	private HashMap<String, String> _defaultLanguage = null;
	private String _currentLanguageName = null;
	
	private LanguagesManager() {
		// Create dictionary
		_strings = new HashMap<String, HashMap<String, String>>();
		
		// Parse strings file
		loadLanguagesFile();
	}
	
	public static LanguagesManager getInstance() {
		if (_instance == null) {
			_instance = new LanguagesManager();
		}
		
		return _instance;
	}
	
	public void setLanguage(String languageName) {
		HashMap<String, String> language = _strings.get(languageName);
		
		if (language != null) {
			_currentLanguage = language;
			_currentLanguageName = languageName;
		}
	}
	
	public String getLanguage() {
		return _currentLanguageName;
	}
	
	public String getString(String key) {
		String string;
		
		if (_currentLanguage != null) {
			// Look for string in selected language
			string = _currentLanguage.get(key);
			
			if (string != null) {
				return string;
			}
				
			// Fallback to default language
			if (_defaultLanguage != null) {
				string = _defaultLanguage.get(key);
				
				if (string != null) {
					return string;
				}
			}
		}
	
		// Key not found, return the key itself
		return key;
	}
	
	private void loadLanguagesFile() {
		try {
			XmlReader reader = new XmlReader();
			Element root = reader.parse(Gdx.files.internal(LANGUAGES_FILE));
			
			 Array<Element> languages =  root.getChildrenByNameRecursively("language");
			 boolean defaultFound = false;
			 
			 for (int i = 0; i < languages.size; ++i) {
				 // Get language name and whether if it´s the default one or not
				 Element languageElement = languages.get(i);
				 String languageName = languageElement.getAttribute("name");
				 boolean isDefault = languageElement.getAttribute("default", "0").equals("1");

				 // Create hashmap element for that language
				 HashMap<String, String> language = new HashMap<String, String>();
				 _strings.put(languageName, language);
				 
				 // Fetch every string for that language
				 Array<Element> strings = languageElement.getChildrenByName("string");
				 
				 for (int j = 0; j < strings.size; ++j) {
					 Element stringElement = strings.get(j);
					 String key = stringElement.getAttribute("key");
					 String value = stringElement.getAttribute("value");
					 language.put(key, value);
				 }
				 
				 // By default, the default language is the first one
				 if (i == 0) {
					 _defaultLanguage = language;
					 _currentLanguageName = languageName;
				 }
				 
				 if (!defaultFound && isDefault) {
					 defaultFound = true;
					 _defaultLanguage = language;
					 _currentLanguageName = languageName;
				 }
			 }
			 
			 _currentLanguage = _defaultLanguage;
		}
		catch (Exception e) {
			System.out.println("Error loading languages file " + LANGUAGES_FILE);
		}
	}
}
