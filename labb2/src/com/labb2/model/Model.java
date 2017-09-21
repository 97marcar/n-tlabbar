package com.labb2.model;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

public class Model {

    public void getWeather(String city, int h)throws Exception{

        URL weather = new URL("http://api.met.no/weatherapi/locationforecast/1.9/?lat=60.10;lon=9.58");
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
        NodeList nodeList = document.getElementsByTagName("minTemperature");
        Node node = nodeList.item(0);
        NamedNodeMap namedNodeMap = node.getAttributes();
        String minTemperature = namedNodeMap.getNamedItem("value").getFirstChild().getTextContent();
        System.out.println(minTemperature);

        in.close();
    }

    private String getLat(String city){

    }
}
