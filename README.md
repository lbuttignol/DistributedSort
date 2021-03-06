# My first distributed system #

This project was build on the context of a course on The U.N.R.C.

The main idea is develop a distributed BubbleSort, on a distributed
array.

*To compile the project, run:*

mvn compile

*To execute the project, run:*

mvn exec:java -Dexec.args="<node-numbers> <array-length> <number-of-instance-to-run>"


# Project Structure (In Spanish for the moment) # 

+-------------------------------------------------------+ <br />
|   App.java                                            | <br />
|                                                       | <br />
|   +-------------------+                               | <br /> 
|   | DistSystem.java   |                               | <br />
|   |                   |                               | <br />
|   +-------------------+                               | <br />
|                                                       | <br />
|   +------------------------------+                    | <br />
|   | DistributedArray.java        |                    | <br />
|   |                              |                    | <br />
|   +------------------------------+                    | <br />
|                                                       | <br />
|                                                       | <br />
+-------------------------------------------------------+ <br />
 <br />
 <br />
+-------------------------------------------------------+ <br />
|   Middlewar.java                                      | <br />
|    ear                                                | <br />
|    +---------------+                                  | <br />
|    | Listener.java |                                  | <br />
|    |               |                                  | <br />
|    +---------------+                                  | <br />
|                                                       | <br />
|                                                       | <br />
|    registry (HashMap)                                 | <br />
|    +-----------------------------+                    | <br />
|    |  arrayName   | arrayRef     |                    | <br />
|    |   (String)   | (DistArray ) |                    | <br />
|    |              |              |                    | <br />
|    |              |              |                    | <br />
|    +-----------------------------+                    | <br />
|                                                       | <br />
|                                                       | <br />
|                                                       | <br />
|                                                       | <br />
+-------------------------------------------------------+ <br />


El sistema consta de una clase principal App.java, éste es el programa que va 
a ejecutarse en diferentes máquinas, es el encargado de crear el arreglo 
distribuido (DistributedArray.java), completarlo con ciertos valores y realizar
el ordenamiento de éstos elementos. Además tiene que crear una instancia de 
DistSystem.java, éste objeto es el que contiene toda la información del systema
(cantidad de nodos, ip y puerto en el que escucha cada nodo).

Cada partición del arreglo distribuido, que puede correr en una máquina 
independiente, consta de un Middlewar.java para realizar la comunicación con las
otras particiones. 

Cada Middlewar.java contiene un Listener.java, cuyo trabajo es el de
recibir o escuchar todos los mensajes correspondientes a esa instancia y realizar
las acciones pertinentes de acuerdo al tipo de mensaje recibido. A su vez tiene
un HashMap llamado registry, éste atributo es necesario para poder realizar la 
comunicación asincrónica requerida por los sets y gets del arreglo, en esta
estructura se guarda como clave el nombre interno que se le asigna a un arreglo
distribuido y como valor se deja una referencia al mismo. Ésto se hace a través
del método bind del Middleware.java

