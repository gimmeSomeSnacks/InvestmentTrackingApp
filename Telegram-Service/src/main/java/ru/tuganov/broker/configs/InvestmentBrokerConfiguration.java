package ru.tuganov.broker.configs;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InvestmentBrokerConfiguration {

    @Bean
    public Queue instrumentQueue() {
        return new Queue("instrumentQueue", false);
    }

    @Bean
    public Queue instrumentListQueue() {
        return new Queue("instrumentListQueue", false);
    }

    @Bean
    public Queue instrumentPricesQueue() {
        return new Queue("instrumentPricesQueue", false);
    }

    @Bean
    public DirectExchange instrumentExchange() {
        return new DirectExchange("instrumentExchange");
    }

    @Bean
    public DirectExchange instrumentListExchange() {
        return new DirectExchange("instrumentListExchange");
    }

    @Bean
    public DirectExchange instrumentPricesExchange() {
        return new DirectExchange("instrumentPricesExchange");
    }

    @Bean
    public Binding instrumentNameBinding(Queue instrumentQueue, DirectExchange instrumentExchange) {
        return BindingBuilder.bind(instrumentQueue).to(instrumentExchange).with("instrument");
    }

    @Bean
    public Binding instrumentBinding(Queue instrumentListQueue, DirectExchange instrumentListExchange) {
        return BindingBuilder.bind(instrumentListQueue).to(instrumentListExchange).with("instrumentList");
    }

    @Bean
    public Binding instrumentPriceBinding(Queue instrumentPricesQueue, DirectExchange instrumentPricesExchange) {
        return BindingBuilder.bind(instrumentPricesQueue).to(instrumentPricesExchange).with("instrumentPrices");
    }
}
