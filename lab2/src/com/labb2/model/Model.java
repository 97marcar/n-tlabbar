package com.labb2.model;

import com.labb2.controller.Control;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A Model that collects weather data from yr.no in a specific city a specific time of the day.
 * @author Marcus Carlsson
 * @since 2017-09-14
 * @version 1.0
 */
public class Model {
    private Control control;

    /**
     * Set the controller that you want to control the Model
     * @param control Controller
     */
    public void setControl(Control control) {
        this.control = control;
    }

    /**
     * A method that collects the temperature a given hour in a given city and run a method that enables the controller
     * to tell the GUI to display it to the user.
     * @param city What city given from a number of choices this number tells which one from 0-3(can be increased)
     *             this number then correlates to a city in a XML file.
     * @param h Time the user want to know the temperature.
     * @throws Exception Throws an exception if the URL cannot be found
     */
    public void getWeather(int city, int h)throws Exception{

        String[] latandlon = getLatLon(city);
        URL weather = new URL("http://api.met.no/weatherapi/locationforecast/1.9/?lat="+latandlon[0]+";lon="+latandlon[1]);
        URLConnection con = weather.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader((con.getInputStream())));
        String currentLine;
        StringBuilder yr = new StringBuilder();

        while ((currentLine = in.readLine()) != null){
            yr.append(currentLine);
        }

        InputSource inputSource = new InputSource(new StringReader(yr.toString()));
        DOMParser domParser = new DOMParser();
        domParser.parse(inputSource);
        Document document = domParser.getDocument();
        DateFormat hourFormat = new SimpleDateFormat("HH");
        Date date = new Date();
        int currentHour = Integer.parseInt(hourFormat.format(date));

        int index;
        if ((h+1) >= currentHour){
            index = (h+1)-currentHour;
        }else{
            index = (h+25)-currentHour;
        }
        NodeList nodeList = document.getElementsByTagName("temperature");
        Node node = nodeList.item(index);
        NamedNodeMap namedNodeMap = node.getAttributes();
        String temperature = namedNodeMap.getNamedItem("value").getFirstChild().getTextContent();
        in.close();

        control.setWeatherLabel(temperature);

    }

    private String[] getLatLon(int city){
        //Collects the Latitude and Longitude of the chosen city and returns it in an Array.
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(new File("C:\\Users\\marcu\\Documents\\GitHub\\n-tlabbar\\lab2\\src\\com\\labb2\\model\\places.xml")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String currentLine;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((currentLine = in.readLine()) != null){
                stringBuilder.append(currentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputSource inputSource = new InputSource(new StringReader(stringBuilder.toString()));
        DOMParser domParser = new DOMParser();
        try {
            domParser.parse(inputSource);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document document = domParser.getDocument();
        NodeList nodeList = document.getElementsByTagName("location");
        Node node = nodeList.item(city);
        NamedNodeMap namedNodeMap = node.getAttributes();
        String lat = namedNodeMap.getNamedItem("latitude").getFirstChild().getTextContent();
        String lon = namedNodeMap.getNamedItem("longitude").getFirstChild().getTextContent();

        String[] latandlon = {lat,lon};
        return latandlon;
    }


}
