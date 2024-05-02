package com.keb.kebsmartfarm.config;

import com.keb.kebsmartfarm.service.MqttReceiver;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;

@Configuration
@IntegrationComponentScan
public class MqttConfig {
    private final static String BROKER_URL = "tcp://io.adafruit.com:";
    private final static String SUB_MQTT_CLIENT_ID = MqttAsyncClient.generateClientId();
    private final static String PUB_MQTT_CLIENT_ID = MqttAsyncClient.generateClientId();
    private final int PORT = 1883;
    private final String USERNAME;
    private final String PASSWORD;

    private final MqttReceiver mqttReceiver;

    public MqttConfig(@Value("${Adafruit.username}") String username, @Value("${Adafruit.password}") String password, MqttReceiver mqttReceiver){
        this.USERNAME = username;
        this.PASSWORD = password;
        this.mqttReceiver = mqttReceiver;
    }

    public MqttConnectOptions getMQTTConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{String.format("%s%d", BROKER_URL, PORT)});
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());
        options.setCleanSession(true);
        return options;
    }

    @Bean
    public DefaultMqttPahoClientFactory defaultMqttPahoClientFactory() {
        DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
        clientFactory.setPersistence(new MemoryPersistence());
        clientFactory.setConnectionOptions(getMQTTConnectOptions());
        return clientFactory;
    }

    // MQTT 수신용 채널 정의
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    // MQTT 송신용 채널 정의
    @Bean
    public MessageChannel mqttOutBoundChannel() {
        return new DirectChannel();
    }

    //MQTT 메시지를 수신하는 빈 설정
    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(SUB_MQTT_CLIENT_ID, defaultMqttPahoClientFactory(),USERNAME + "/f/data");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return mqttReceiver::handle;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutBoundChannel")
    public MessageHandler mqttOutBound() {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler(PUB_MQTT_CLIENT_ID, defaultMqttPahoClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(1);
        return messageHandler;
    }

    @MessagingGateway(defaultRequestChannel = "mqttOutBoundChannel")
    public interface MyGateway {

        void sendToMqtt(String payload, @Header(MqttHeaders.TOPIC) String topic);
    }

}
