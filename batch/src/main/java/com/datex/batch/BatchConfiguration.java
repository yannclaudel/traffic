package com.datex.batch;

import com.datex.batch.model.MeteoMeasure;
import com.datex.batch.model.TrafficMeasure;
import com.datex.batch.model.TraficPoint;
import com.datex.batch.steps.DatexMeteoProcessor;
import com.datex.batch.steps.DatexMeteoReader;
import com.datex.batch.steps.DatexTrafficProcessor;
import com.datex.batch.steps.DatexTrafficReader;
import com.datex.batch.tasklet.LogTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public LogTasklet logtasklet;

    @Autowired
    public DataSource dataSource;

    @Bean
    @StepScope
    public Resource getResource(@Value("#{jobParameters['targetFile']}") String targetFile) {
        return new FileSystemResource(targetFile);
    }

    @Bean
    @StepScope
    public DatexTrafficReader getDatexTrafficReader(@Value("#{jobParameters['targetUrl']}") String targetUrl) {
        return new DatexTrafficReader(targetUrl);
    }

    @Bean
    @StepScope
    public DatexMeteoReader getDatexMeteoReader() {
        return new DatexMeteoReader();
    }

    @Bean
    public DatexTrafficProcessor getTrafficProcessor() {
        return new DatexTrafficProcessor();
    }
    @Bean
    public DatexMeteoProcessor getMeteoProcessor() {
        return new DatexMeteoProcessor();
    }

    @Bean
    public FlatFileItemWriter<TrafficMeasure> writerCSV() {
        //Create writer instance
        FlatFileItemWriter<TrafficMeasure> writer = new FlatFileItemWriter<>();

        //Set output file location
        writer.setResource(getResource(null));

        //All job repetitions should "append" to same output file
        writer.setAppendAllowed(true);

        //Name field values sequence based on object properties
        writer.setLineAggregator(new DelimitedLineAggregator<TrafficMeasure>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<TrafficMeasure>() {
                    {
                        setNames(new String[]{"id", "measurementTime", "latitude", "longitude", "direction", "road", "averageVehicleSpeed", "vehicleFlowRate", "trafficConcentration"});
                    }
                });
            }
        });
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<TraficPoint> writer() {
        JdbcBatchItemWriter<TraficPoint> itemWriter = new JdbcBatchItemWriter<>();

        itemWriter.setDataSource(this.dataSource);
//        itemWriter.setSql("INSERT INTO trafic(time , camera , latitude, longitude, direction, road , averageVehicleSpeed, vehicleFlowRate, trafficConcentration)" +
//                " VALUES (:time, :camera, :latitude,:longitude,:direction,:road,:averagevehiclespeed,:vehicleflowrate,:trafficconcentration)" +
//                " ON CONFLICT (time, camera) DO UPDATE " +
//                " SET averageVehicleSpeed = excluded.averageVehicleSpeed, " +
//                " vehicleFlowRate = excluded.vehicleFlowRate, " +
//                " trafficConcentration = excluded.trafficConcentration;");

        itemWriter.setSql("INSERT INTO trafic_time(time , camera , average_vehicle_speed, vehicle_flow_rate, traffic_concentration)" +
                " VALUES (:time, :camera, :averagevehiclespeed,:vehicleflowrate,:trafficconcentration)" +
                " ON CONFLICT (time, camera) DO UPDATE " +
                " SET average_vehicle_speed = excluded.average_vehicle_speed, " +
                " vehicle_flow_rate = excluded.vehicle_flow_rate, " +
                " traffic_concentration = excluded.traffic_concentration;");

        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

    @Bean
    public JdbcBatchItemWriter<MeteoMeasure> writerMeteo() {
        JdbcBatchItemWriter<MeteoMeasure> itemWriter = new JdbcBatchItemWriter<>();

        itemWriter.setDataSource(this.dataSource);

        itemWriter.setSql("INSERT INTO meteo(time , point , humidity , no_precipitation , millimetres_per_hour_intensity , road_surface_temperature , temperature_below_road_surface , weather_related_road_condition_type , air_temperature , dew_point_temperature , wind_speed , maximum_wind_speed , wind_direction_bearing , status_type)\n" +
                "                 VALUES (:time, :id, :humidity, :noPrecipitation, :millimetresPerHourIntensity, :roadSurfaceTemperature, :temperatureBelowRoadSurface, :weatherRelatedRoadConditionType, :airTemperature, :dewPointTemperature, :windSpeed, :maximumWindSpeed, :windDirectionBearing, :statusType)\n" +
                "                 ON CONFLICT (time, point) DO UPDATE SET millimetres_per_hour_intensity = excluded.millimetres_per_hour_intensity");

        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }


    @Bean
    public Step stepMeteo(JdbcBatchItemWriter<MeteoMeasure> writerMeteo) {
        return stepBuilderFactory.get("stepM")
                .<MeteoMeasure, MeteoMeasure>chunk(10)
                .reader(getDatexMeteoReader())
                .processor(getMeteoProcessor())
                .writer(writerMeteo)
                .build();
    }

    @Bean
    public Step step(JdbcBatchItemWriter<TraficPoint> writer) {
        return stepBuilderFactory.get("stepD")
                .<TrafficMeasure, TraficPoint>chunk(10)
                .reader(getDatexTrafficReader(null))
                .processor(getTrafficProcessor())
                .writer(writer)
                .build();
    }
    @Bean(name="readDatexJob")
    public Job readDatexJob(Step step) {
        return jobBuilderFactory.get("readDatexJob")
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }

    @Bean(name="logTaskletJob")
    public Job logTaskletJob() {
        return this.jobBuilderFactory.get("logTasklet").incrementer(new RunIdIncrementer())
                .start(stepBuilderFactory.get("stepTask").tasklet(logtasklet).build()).build();
    }
    @Bean(name="readMeteoJob")
    public Job readMeteoJob(Step stepMeteo) {
        return jobBuilderFactory.get("readDatexJob")
                .incrementer(new RunIdIncrementer())
                .flow(stepMeteo)
                .end()
                .build();
    }

}