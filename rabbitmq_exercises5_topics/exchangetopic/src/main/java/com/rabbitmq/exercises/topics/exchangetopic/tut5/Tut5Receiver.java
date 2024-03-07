package com.rabbitmq.exercises.topics.exchangetopic.tut5;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.util.StopWatch;
import com.rabbitmq.client.Channel;

public class Tut5Receiver {

    static int contador = 0;

    @RabbitListener(queues = "#{autoDeleteQueue1.name}", ackMode = "MANUAL")
    public void receive1(String in, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag)
            throws InterruptedException {
        try {
            contador = contador + 1;
            System.out.println(contador);
            if (contador == 10) {
                contador = 0;
                throw new Exception("Error en el proceso de la cola 1");
            }
            receive(in, 1);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            try {
                channel.basicNack(tag, false, true);
                System.out.println("Se devuelve el mensaje a la cola" + in);
            } catch (IOException e1) {
                System.out.println("Error en el proceso de la cola 1");
            }
        }
    }

    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    public void receive2(String in) throws InterruptedException {
        receive(in, 2);
    }

    @RabbitListener(queues = "#{autoDeleteQueue3.name}")
    public void receive3(String in) throws InterruptedException {
        receive(in, 3);
    }

    public void receive(String in, int receiver) throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        System.out.println("instance " + receiver + " [x] Received '"
                + in + "'");
        doWork(in);
        watch.stop();
        System.out.println("instance " + receiver + " [x] Done in "
                + watch.getTotalTimeSeconds() + "s");
    }

    private void doWork(String in) throws InterruptedException {
        for (char ch : in.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
        }
    }
}