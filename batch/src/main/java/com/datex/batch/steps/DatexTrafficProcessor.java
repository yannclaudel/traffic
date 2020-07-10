package com.datex.batch.steps;

import com.datex.batch.model.TrafficMeasure;
import com.datex.batch.model.TraficPoint;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatexTrafficProcessor implements ItemProcessor<TrafficMeasure, TraficPoint> {
    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public TraficPoint process(TrafficMeasure t) throws Exception {
        TraficPoint point = new TraficPoint();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date parsedDate = dateFormat.parse(t.getMeasurementTime());
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            point.setTime(timestamp);
        } catch(Exception e) { //this generic but you can control another types of exception
            e.printStackTrace();
            point.setTime(new Timestamp((new Date()).getTime()));
        }
        point.setCamera(t.getId());
        point.setDirection(t.getDirection());
        point.setLatitude(t.getLatitude());
        point.setLongitude(t.getLongitude());
        point.setRoad(t.getRoad());
        try {
            point.setAveragevehiclespeed(Double.parseDouble(t.getAverageVehicleSpeed()));
            point.setTrafficconcentration(Double.parseDouble(t.getTrafficConcentration()));
            point.setVehicleflowrate(Double.parseDouble(t.getVehicleFlowRate()));
        }catch(NumberFormatException e){
            e.printStackTrace();
        }

        return point;
    }
}
