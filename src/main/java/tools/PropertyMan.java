package tools;

import enums.Property;

import java.io.*;
import java.util.Properties;

public class PropertyMan
{
    private static final String path = "src/main/resources/propertiesFile.properties";

    public static void setProperty(Property property, String value)
    {
        Properties properties = new Properties();

        //Load files out of .properties file
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(path);
            properties.load(inputStream);


            //Add new property
            properties.setProperty(property.name(), value);
            OutputStream outputStream = null;

            try {
                outputStream = new FileOutputStream(path);
                properties.store(outputStream, null);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Object getProperty(Property property)
    {
        Properties properties = new Properties();
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(path);
            properties.load(inputStream);

            String string = properties.getProperty(property.name());
            if(string == "")
            {
                return null;
            }

            switch (property.getDataType())
            {
                case FILE:
                    return getFile(string);
                default:
                    return null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if(inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static File getFile(String string)
    {
        if(string == null)
        {
            return null;
        }
        else
        {
            return new File(string);
        }
    }
}
