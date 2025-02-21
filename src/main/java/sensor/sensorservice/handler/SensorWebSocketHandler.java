package sensor.sensorservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import sensor.sensorservice.service.interfaces.SensorService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SensorWebSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(SensorWebSocketHandler.class);

    @Autowired
    private SensorService sensorService;

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
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                log.info("Sending data to session " + session.getId());
                session.sendMessage(new TextMessage("Current temperature: " + temperature + "°C"));
            }
        }
    }
}
