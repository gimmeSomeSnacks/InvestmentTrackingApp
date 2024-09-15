package ru.tuganov.broker.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseBrokerConfiguration {
    @Bean
    public Queue saveInstrumentQueue() {
        return new Queue("saveInstrumentQueue", false);
    }

    @Bean
    public Queue deleteInstrumentQueue() { return new Queue("deleteInstrumentQueue", false); }

    @Bean
    public Queue getInstrumentQueue() { return new Queue("getInstrumentQueue", false); }

    @Bean
    public Queue getInstrumentsQueue() { return new Queue("getInstrumentsQueue", false); }

    @Bean
    public Queue getAllInstrumentsQueue() { return new Queue("getAllInstrumentsQueue", false); }

    @Bean
    public DirectExchange saveInstrumentExchange() {
        return new DirectExchange("saveInstrumentExchange");
    }

    @Bean
    public DirectExchange deleteInstrumentExchange() {
        return new DirectExchange("deleteInstrumentExchange");
    }

    @Bean
    public DirectExchange getInstrumentExchange() {
        return new DirectExchange("getInstrumentExchange");
    }

    @Bean
    public DirectExchange getInstrumentsExchange() {
        return new DirectExchange("getInstrumentsExchange");
    }

    @Bean
    public DirectExchange getAllInstrumentsExchange() { return new DirectExchange("getAllInstrumentsExchange"); }

    @Bean
    public Binding saveInstrumentBinding(Queue saveInstrumentQueue, DirectExchange saveInstrumentExchange) {
        return BindingBuilder.bind(saveInstrumentQueue).to(saveInstrumentExchange).with("saveInstrument");
    }

    @Bean
    public Binding deleteInstrumentBinding(Queue deleteInstrumentQueue, DirectExchange deleteInstrumentExchange) {
        return BindingBuilder.bind(deleteInstrumentQueue).to(deleteInstrumentExchange).with("deleteInstrument");
    }

    @Bean
    public Binding getInstrumentBinding(Queue getInstrumentQueue, DirectExchange getInstrumentExchange) {
        return BindingBuilder.bind(getInstrumentQueue).to(getInstrumentExchange).with("getInstrument");
    }

    @Bean
    public Binding getInstrumentsBinding(Queue getInstrumentsQueue, DirectExchange getInstrumentsExchange) {
        return BindingBuilder.bind(getInstrumentsQueue).to(getInstrumentsExchange).with("getInstruments");
    }

    @Bean
    public Binding getAllInstrumentsBinding(Queue getAllInstrumentsQueue, DirectExchange getAllInstrumentsExchange) {
        return BindingBuilder.bind(getAllInstrumentsQueue).to(getAllInstrumentsExchange).with("getAllInstruments");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
