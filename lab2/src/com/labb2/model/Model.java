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

public class Model {
    private Control control;

    public void setControl(Control control) {
        this.control = control;
    }

    public void getWeather(int city, int h)throws Exception{
        String[] latandlon = getLatLon(city);
        URL weather = new URL("http://api.met.no/weatherapi/locationforecast/1.9/?lat="+latandlon[0]+";lon="+latandlon[1]);
        URLConnection con = weather.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader((con.getInputStream())));
        String currentLine;
        StringBuilder yr = new StringBuilder();
        while ((currentLine = in.readLine()) != null){
            //System.out.println(currentLine);
            yr.append(currentLine);
        }

        InputSource inputSource = new InputSource(new StringReader(yr.toString()));
        DOMParser domParser = new DOMParser();
        domParser.parse(inputSource);
        Document document = domParser.getDocument();
        NodeList nodeList = document.getElementsByTagName("minTemperature");

        int index = convertHourToIndex(h);
        Node node = nodeList.item(index);
        NamedNodeMap namedNodeMap = node.getAttributes();
        String minTemperature = namedNodeMap.getNamedItem("value").getFirstChild().getTextContent();

        nodeList = document.getElementsByTagName("maxTemperature");
        node = nodeList.item(index);
        namedNodeMap = node.getAttributes();
        String maxTemperature = namedNodeMap.getNamedItem("value").getFirstChild().getTextContent();

        control.setWeatherLabel(minTemperature, maxTemperature);

        in.close();
    }

    private String[] getLatLon(int city){
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

    private int convertHourToIndex(int h){
        if (h == 0) h=24;

        if(9 <= h && h <=24){
            return(h-9);
        }else{
            return (h+15);
        }
    }
}
