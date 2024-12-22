package org.CCJohnCrickett;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONCheck {
    Logger logger = Logger.getLogger(JSONCheck.class.getName());
    Set<Character> tokens = Set.of('{', '"',',',':', '}');
    StringBuilder stringBuilder = new StringBuilder();

    /// Currently won't set the key5=value5, I need to fix that (I left a quick line down bottom to find where I left off)
    private final Map<String, Object> JSONKeyValue = new HashMap<>();

    /// Main JSON method for finding the json objects and their values
    public boolean valueOfJSON(File file) {

        String key = "";
        Object value = null;
        boolean activeObj = false;
        boolean keyOrValue = false;
        boolean collectKV = false;
        // Step1 to check if the file includes a valid json object aka "{}"
        boolean validObj = false;
        char lastChar = 0;

        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int charValue;
            if(reader.read() == '{') {
                activeObj = true;
            } else {
                return false;
            }

            while((charValue = reader.read()) != -1 && activeObj) {
                char currentChar = (char) charValue;

                switch(currentChar) {
                    case '"':
                        collectKV = true;
                        break;
                    case ':':
                        keyOrValue = true;
                        break;
                    case 't':
                    case 'f':
                    case 'n':
                        collectKV = true;

                        while(collectKV) {
                            if(tokens.contains(currentChar)){
                                String stringValue = stringBuilder.toString().trim();
                                value = parseValue(stringValue);
                                stringBuilder.setLength(0);
                                collectKV = false;
                            } else {
                                stringBuilder.append(currentChar);
                                currentChar = (char) reader.read();
                            }
                        }
                        JSONKeyValue.put(key, value);
                        break;
                }

                /// While collecting is active , runs this loop to collect the key and values.
                while(collectKV) {
                    currentChar = (char) reader.read();

                    if (currentChar == '"') {
                        collectKV = false;
                        if (key.isEmpty() && !keyOrValue) {
                            key = stringBuilder.toString();
                        } else {
                            value = parseValue(stringBuilder.toString());
                        }
                        stringBuilder.setLength(0);
                    } else {
                        stringBuilder.append(currentChar);
                    }
                }
                /// If end of pair is reached, saves the key and its value to a Map
                /// Checks if there is a key or value before the comma , or if it's followed by a "}"
                switch(currentChar) {
                    case ',':                   /// This part seems to be messing with the setting of key5 and value5 into the Map
                        if(key.isEmpty() || lastChar  == ',') {
                            return false;
                        }
                        JSONKeyValue.put(key, value);
                        key = "";
                        value = null;
                        keyOrValue = false;
                        break;
                    case '}':
                        if(lastChar == ',') {
                            return false; // Invalid JSON: trailing comma before closing brace
                        }
                        activeObj = false;
                        validObj = true;
                        break;
                }
                lastChar = currentChar;
            }
        } catch(IOException e) {
            logger.log(Level.WARNING, "Invalid JSON, Unable to read File or Object/s in file", e);
        }

        return validObj;
    }

    /// Parses the Value into it's proper data type, Either a boolean, number ( Double or Integer )  , null value or String.
    private Object parseValue(String value) {
        if(value.equals("true") || value.equals("false")) {
            return Boolean.parseBoolean(value);
        }
        if(value.equals("null")) {
            return null;
        }

        try {
            if(value.contains(".")) {
                return Double.parseDouble(value);
            } else {
                return Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
            return value;
        }

    }

//    public void collectKeyValue(boolean collectKV) {
//        while(collectKV) {
//            charValue = reader.read();
//            currentChar = (char) charValue;
//
//            if (currentChar == '"') {
//                collectKV = false;
//                if (key.isEmpty() && !keyOrValue) {
//                    key = stringBuilder.toString();
//                } else {
//                    value = parseValue(stringBuilder.toString());
//                }
//                stringBuilder.setLength(0);
//            } else {
//                stringBuilder.append(currentChar);
//            }
//        }
//    }


    public Map<String, Object> getJSONKeyValue() {
        return JSONKeyValue;
    }





}





