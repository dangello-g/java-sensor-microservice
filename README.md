# Sensor Microservice Java

A Java-based microservice that simulates temperature and CO2 level data and streams it via WebSockets.

## Overview

This microservice simulates real-time simulated IoT sensor data and streams it via WebSockets. Built for scalability, it supports IoT applications, environmental monitoring systems, and other sensor-driven solutions requiring continuous data streams.

## Features

- Simulated Sensor Data: Generates real-time temperature and CO2 level data for testing and development purposes.
- WebSocket Streaming: Streams sensor data to connected clients via WebSockets, enabling real-time data consumption.
- InfluxDB Integration: Ingests simulated sensor data into InfluxDB, providing efficient storage and retrieval of time series data.

## Planned Features

- Kafka Integration: Implement a Kafka producer to stream sensor data, enabling real-time event processing and integration with other microservices.
- Enhanced Scalability: Use clustering and load balancing strategies to handle increased data volumes and WebSocket connections.
- Improved Fault Tolerance: Introduce failover mechanisms and data replication to ensure high availability.
- Threshold-Based Actions: Enable the frontend to send messages when sensor values exceed predefined thresholds. The backend will process these alerts and trigger appropriate actions, such as turning on an air conditioner when the temperature exceeds 50°C.

## Technology Stack

- Java 17
- Spring Boot 3.4.2
- Spring WebSocket
- Lombok
- InfluxDB for time-series data storage
- Maven
