package sensor.sensorservice.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sensor.sensorservice.model.SensorData;
import sensor.sensorservice.service.interfaces.SensorService;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SensorWebSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(SensorWebSocketHandler.class);

    @Autowired
    private SensorService sensorService;
    @Autowired
    Environment env;

    private final ObjectMapper objectMapper;

    public SensorWebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("Connected from session " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        log.info("Disconnected from session " + session.getId());
    }

    public void sendData() throws Exception {
        double temperature = sensorService.measureTemperature();
        double co2Level = sensorService.measureCO2();

        //insert(temperature, co2Level);

        List<SensorData> sensorReadings = List.of(
                SensorData.builder()
                        .type("temperature")
                        .value(temperature)
                        .unit("°C")
                        .timestamp(Instant.now())
                        .build(),
                SensorData.builder()
                        .type("co2")
                        .value(co2Level)
                        .unit("ppm")
                        .timestamp(Instant.now())
                        .build()
        );

        String json = objectMapper.writeValueAsString(sensorReadings);
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                log.info("Sending data to session " + session.getId());
                session.sendMessage(new TextMessage(json));
            }
        }
    }

    public void insert(double temperature, double co2Level) throws Exception {
        String hostUrl = "https://us-east-1-1.aws.cloud2.influxdata.com";
        char[] authToken = env.getProperty("INFLUXDB_TOKEN").toCharArray();
        String database = "othertest";
        String org = "test";

        try(InfluxDBClient influxDBClient = InfluxDBClientFactory.create(hostUrl, authToken, org, database)) {
            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
            Point[] points = new Point[]{
                    Point.measurement("environmental_sensors")
                            .addTag("sensor_id", "temperature_sensor")
                            .addField("temperature", temperature)
                            .time(Instant.now().toEpochMilli(), WritePrecision.MS),
                    Point.measurement("environmental_sensors")
                            .addTag("sensor_id", "co2_sensor")
                            .addField("co2_level", co2Level)
                            .time(Instant.now().toEpochMilli(), WritePrecision.MS)
            };

            for (Point point : points) {
                writeApi.writePoint(point);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
