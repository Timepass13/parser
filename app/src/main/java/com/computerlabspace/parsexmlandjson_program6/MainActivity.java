package com.computerlabspace.parsexmlandjson_program6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button parseXMLBtn, parseJSONBtn;

    TextView xmlContentTextView, jsonContentTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parseXMLBtn = (Button) findViewById(R.id.main_parse_xml_btn_id);
        parseJSONBtn = (Button) findViewById(R.id.main_parse_json_id);
        xmlContentTextView = (TextView) findViewById(R.id.viewcontent_id);
        jsonContentTextView = findViewById(R.id.jsoncontent);
        parseXMLBtn.setOnClickListener(this);
        parseJSONBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(parseXMLBtn)) {
            jsonContentTextView.setText("");
            parseXML();
        } else if(view.equals(parseJSONBtn)) {
            xmlContentTextView.setText("");

            parseJSON();
        }
    }
    public void parseXML() {
        xmlContentTextView.setText("Test xml parsed content\n");
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("weather.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputStream);
            doc.normalize();
            NodeList nodeList = doc.getElementsByTagName("weather");
            for(int i=0; i<nodeList.getLength();i++) {
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    xmlContentTextView.append("City_Name: "+element.getElementsByTagName("city_name").item(0).getTextContent() + "\n");
                    xmlContentTextView.append("Latitude: "+element.getElementsByTagName("latitude").item(0).getTextContent() + "\n");
                    xmlContentTextView.append("Longitude: "+element.getElementsByTagName("longitude").item(0).getTextContent() + "\n");
                    xmlContentTextView.append("Temperature: "+element.getElementsByTagName("temperature").item(0).getTextContent() + "\n");
                    xmlContentTextView.append("Humidity: "+element.getElementsByTagName("humidity").item(0).getTextContent()+"\n\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void parseJSON() {
        jsonContentTextView.setText("Test json parsed content\n");
        try (InputStream inputStream = getAssets().open("weather.json")) {
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            String jsonString = new String(data);
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject weather = jsonObject.getJSONObject("weather");
            jsonContentTextView.append("City_Name: "+weather.getString("city_name") + "\n");
            jsonContentTextView.append("Latitude: "+weather.getString("latitude") + "\n");
            jsonContentTextView.append("Longitude: "+weather.getString("longitude") + "\n");
            jsonContentTextView.append("Temperature: "+weather.getString("temperature") + "\n");
            jsonContentTextView.append("Humidity: "+weather.getString("humidity") + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
