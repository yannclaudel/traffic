package com.datex.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sandbox {
    private static final Logger log = LoggerFactory.getLogger(Sandbox.class);


    public static void main(String[] args){
    String toParse = "2020-06-27T09:05:04.288+02:00";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date parsedDate = dateFormat.parse(toParse);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            log.info(timestamp.toString());
        } catch(Exception e) { //this generic but you can control another types of exception
            e.printStackTrace();
        }

    }

//    public static void main(String[] args) {
//        List<TrafficMeasure> out = new ArrayList<>();
//
//        try {
//            URL urlstream = new URL(Datex.TRAFFIC_A3);
//            DatexTrafficParser parser = new DatexTrafficParser();
//            out.addAll(parser.parseXml(urlstream.openStream()));
//        } catch (MalformedURLException e) {
//            log.error("MalformedURLException [" + "url" + "] msg [" + e.getMessage() + "]");
//            e.printStackTrace();
//        } catch (IOException e) {
//            log.error("IOException - msg [" + e.getMessage() + "]");
//            e.printStackTrace();
//        }
//        out.forEach(m->log.info(m.toString()));
//    }

//    public static void main(String[] args) {
//        List<MeteoMeasure> out = new ArrayList<>();
//
//        try {
//            URL urlstream = new URL(Datex.URL_METEO);
//            DatexMeteoParser parser = new DatexMeteoParser();
//            out.addAll(parser.parseXml(urlstream.openStream()));
//        } catch (MalformedURLException e) {
//            log.error("MalformedURLException [" + "url" + "] msg [" + e.getMessage() + "]");
//            e.printStackTrace();
//        } catch (IOException e) {
//            log.error("IOException - msg [" + e.getMessage() + "]");
//            e.printStackTrace();
//        }
//        out.forEach(m->log.info(m.toString()));
//    }
}
