# Proyecto Backend: CineX

Este proyecto es un sistema backend desarrollado en Java con el framework Spring Boot. Su propósito es simular la lógica de negocio de una aplicación de cine, gestionando datos como películas, salas, funciones y reservas a través de una API REST.

## Alcance del Proyecto

El alcance del sistema en su versión actual permite realizar consultas de información sobre películas, salas y funciones. Además, ofrece la capacidad de crear nuevas reservas para las funciones disponibles y agregar nuevas películas al catálogo. 

Como limitaciones, el proyecto no incluye un sistema de gestión de usuarios, la modificación o eliminación de datos existentes, ni una interfaz gráfica, ya que su función es puramente la de un servicio backend.

## Arquitectura del Sistema

El sistema está construido siguiendo una arquitectura en capas, un diseño estándar que promueve un código organizado y fácil de mantener. Cada capa tiene una responsabilidad clara y definida:

- **Capa de Controladores:** Actúa como la puerta de entrada de la aplicación. Es la única responsable de recibir las peticiones del exterior (como las de un navegador o Postman) y de enviar las respuestas correspondientes.

- **Capa de Servicios:** Es el cerebro de la aplicación. Aquí reside la lógica de negocio principal, se coordinan las operaciones y se toman las decisiones sobre cómo procesar cada petición.

- **Capa de Repositorios:** Funciona como el almacén de datos. Esta capa se encarga de la comunicación con la fuente de datos, abstrayendo los detalles de cómo y dónde se guarda la información. En este proyecto, los datos se guardan en listas dentro de la memoria de la aplicación.

- **Capa de Entidades:** Representa el ADN de nuestros datos. Define la estructura y los atributos de los objetos con los que trabaja la aplicación, como qué es una 'película' o una 'reserva'.

## Tecnologías Utilizadas

Para la construcción de este proyecto se utilizaron tecnologías estándar en la industria del desarrollo de software, incluyendo el lenguaje de programación Java, el framework Spring Boot para la creación de la API REST y Maven para la gestión de las dependencias del proyecto.

---
*Proyecto desarrollado por [Daniel Esteban Jurado Diaz, Alexander Mejia Ospina, Santiago Rodriguez Jimenez, Santiago Mayorga Hueso, Ronald Garcia Muñoz].*
