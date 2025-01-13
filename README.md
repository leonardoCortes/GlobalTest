Este proyecto contiene un microservicio Java 11 Con SpringBoot 2.5.14 y Graddle 7.4
La Base de datos H2 est{a configurada en el archivo application.properties

Para Ejecutar la API podemos ir a la clase principal del proyecto Springboot UsersApplication
Clic derecho y Run: UsersApplication. 

Se adiciona la coleccion en Postman con dos enpoints:

1. Se debera correr primero el POST: http://localhost:8080/api/users/sign-up
El cual está configurado en el body con:
   {
   "name": "Leonardo",
   "email": "ingeniero.lfcortes@gmail.com",
   "password": "Leo1n2cortes",
   "phones": [
   {
   "number": 3173802332,
   "cityCode": 1234,
   "countryCode": "12345"
   }
   ]
   }


2. Una vez se ejecute el POST Obtendremos una respuesta de creación de usuario, tambien se generara un TOKEN que viene
en la respuesta
3. Se debe copiar este TOKEN y ejecutaremos el siguiente endPoint
4. Debemos pegar el TOKEN generado anteriormente en Athorization Bearear Token. 
5. Ejecutaremos el GET: http://localhost:8080/api/users/login 
6. Nos retornará los datos asociados al usuario creado previamente. 