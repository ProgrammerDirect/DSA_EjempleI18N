package org.example;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;

public class I18NManager {

    private static I18NManager instance;

    private Map<String, Properties> diccionariSalutacions;

    private I18NManager() {
        this.diccionariSalutacions = new HashMap<>();
    }

    public static I18NManager getInstance() {
        if (instance == null) {
            instance = new I18NManager();
        }
        return instance;
    }

    public String getText(String language, String key) {

        Properties prop = this.diccionariSalutacions.get(language);

        if (prop == null)
        {
            prop = new Properties();
            String filename = language + ".properties";

            try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename))
            {
                if (input == null) {
                    throw new MissingResourceException("Encara no tenim traducció a aquest idioma: " + filename, "Properties", key);
                }

                prop.load(input); //Carregar les dades del fitxer.

                this.diccionariSalutacions.put(language, prop); //Guardar el dades al HashMap

            }

            catch (Exception e)
            {
                if (e instanceof MissingResourceException)
                {
                    throw (MissingResourceException) e;
                }

                throw new MissingResourceException("Error al llegir el recurs", "Properties", key);
            }
        }

        String value = prop.getProperty(key);

        if (value == null)
        {
            throw new MissingResourceException("No s'ha trobat la clau: " + key, "Properties", key);
        }

        return value;
    }

    public void clear()
    {
        this.diccionariSalutacions.clear();
    }
}