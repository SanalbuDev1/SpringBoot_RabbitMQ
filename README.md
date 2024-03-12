# SpringBoot_RabbitMQ :rabbit:
Diferentes ejercicios con rabbit

## Ejercicios

| Ejercicios | descripcion |
|--- |--- |
| 1. Ejercicio RabbitMQ1 | En este ejercicio simplemente se configura un sender que envia directamente a la cola y un consumidor | 
| 2. Ejercicio workqueues | En este ejemplo se escribió un programa que envía y recibe mensajes de una cola, la diferencia con el primer ejemplo es que esta vez tendremos más de un consumidor activo. |
| 3. Ejercicio exchange fanout | A diferencia del ejemplo anterior que el productor se conectaba directamente con la cola, en este ejemplo enfocados un poco más a la realidad creamos un exchange que recibe los mensajes y los manda a las colas, el exchange es de tipo fanout |
| 4. Ejercicio exchange direct | A diferencia del ejemplo anterior, creamos varios bindings que se conectan entre la cola y el exchange, pero esta vez de tipo Direct a diferencia del anterior este no manda mensaje a todos si no simplemente al que se le indica en este caso GREEN,BLACK,ORANGE|
| 5. Ejercicio exchange topic| A diferencia del ejemplo anterior, creamos varios bindings que se conectan entre la cola y el exchange de tipo topic, pero esta vez de tipo topic creamos patrones para que la cola envie a diferentes routingKey adicional al ejemplo se agrego .car y ..motorcycle |




> Nota
En el ejercicio 2,3,4,5 `workqueues` se agrego la siguiente linea de codigo para permitir que rabbit envie mensajes a todos los consumidores :shipit:

```
@Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPrefetchCount(1);
        return factory;
    }
```

> Nota
En el ejercicio 3,4,5 `workqueues` se agrego la la congfiguracion que agrega la Queue = hello por defecto en rabbitMQ
> Nota
En el ejercicio 5 `workqueues` se modifico el exchange topic para que reciba los siguientes parametros en la cola 1 *.car.* ,"*.*.motorcycle" adicional se configuro para aceptar o rechazar los mensajes de la cola
 ```
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
```

