package com.kurtmustafa.countryselector.utils;

import android.content.res.Resources;

import com.google.gson.GsonBuilder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import timber.log.Timber;

public class JSONResourceReader
    {

        private String jsonString;

        /**
         * Read from a json resource file and allow it to be retrieved in following forms: <br>
         * {@link JSONObject}, {@link String} and any model class that is suitable by using GSON.
         *
         * @param resources An application {@link Resources} object.
         * @param id The id for the resource to load, typically held in the raw/ folder.
         */

        public JSONResourceReader(Resources resources, Integer id)
            {
                InputStream resourceReader = resources.openRawResource(id);
                Writer writer = new StringWriter();
                try
                    {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(resourceReader, StandardCharsets.UTF_8));
                        String line = reader.readLine();
                        while (line != null)
                            {
                                writer.write(line);
                                line = reader.readLine();

                            }
                    } catch (Exception e)
                    {
                        Timber.e(e, "Unhandled exception while using JSONResourceReader");
                    } finally
                    {
                        try
                            {
                                resourceReader.close();
                            } catch (Exception e)
                            {
                                Timber.e(e, "Unhandled exception while using JSONResourceReader");
                            }
                    }

                jsonString = writer.toString();
            }

        /**
         * Build an object from the specified JSON resource using Gson.
         *
         * @param type The type of the object to build.
         * @return An object of type T, with member fields populated using Gson.
         */
        public <T> T constructUsingGson(Class<T> type)
            {
                return new GsonBuilder().create().fromJson(jsonString, type);
            }

        /**
         * Get the json file contents in {@link JSONObject} form
         * @return {@link JSONObject} json contents.
         */
        public JSONObject getAsJsonObject()
            {
                JSONParser parser = new JSONParser();
                try
                    {

                        return (JSONObject) parser.parse(jsonString);
                    } catch (ParseException e)
                    {
                        Timber.e(e, "Couldn't parse jsonString to JSONObject.");
                        return null;
                    }
            }

        /**
         * Get the json contents in the raw {@link String} form.
         * @return Raw {@link String} json contents.
         */
        public String getJsonString()
            {
                return jsonString;
            }
    }

