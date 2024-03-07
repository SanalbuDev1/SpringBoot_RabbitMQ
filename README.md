# SpringBoot_RabbitMQ
Diferentes ejercicios con rabbit

## Ejercicios

| Ejercicios | descripcion |
|--- |--- |
| 1. Ejercicio RabbitMQ1 | En este ejercicio simplemente se configura un sender que envia directamente a la cola y un consumidor | 
| 2. Ejercicio workqueues | En este ejemplo se escribió un programa que envía y recibe mensajes de una cola, la diferencia con el primer ejemplo es que esta vez tendremos más de un consumidor activo. |
| 3. Ejercicio exchange direct | A diferencia del ejemplo anterior que el productor se conectaba directamente con la cola, en este ejemplo enfocados un poco más a la realidad creamos un exchange que recibe los mensajes y los manda a las colas, el exchange es de tipo fanout |
| 4. Ejercicio exchange direct | A diferencia del ejemplo anterior, creamos varios bindings que se conectan entre la cola y el exchange, pero esta vez de tipo Direct a diferencia del anterior este no manda mensaje a todos si no simplemente al que se le indica en este caso GREEN,BLACK,ORANGE|
| 5. Ejercicio exchange topic| A diferencia del ejemplo anterior, creamos varios bindings que se conectan entre la cola y el exchange de tipo topic, pero esta vez de tipo topic creamos patrones para que la cola envie a diferentes routingKey adicional al ejemplo se agrego .car y ..motorcycle |

[!NOTE]
En el ejercicio 2 `workqueues` se agrego la siguiente linea de codigo para permitir que rabbit envie mensajes a todos los consumidores

