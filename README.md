Runtime DataSource generation
---

Recently I was asked if there was an way to dynamically create a `DataSource` whenever it was necessary to connect
to a database where the credentials, ip and schema where provided by an external `API`. Regardless of the overhead 
here's an attempt to make it work.

Technology Stack
---

  * [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
  * [Maven](https://maven.apache.org/)
  * [Spring Boot 2](https://spring.io/projects/spring-boot) 
  * Spring Data JPA 
  * MySQL (sorry, I did not use an in-memory DB)
  
Credentials API
---

Just to mimic the real use case I've created this project `credentials-api`, it's just a plain spring-boot
app in which returns some hardcoded values, the response is something like this:

```bash
$ curl localhost:8080/credentials

{  
   "hostname":"localhost",
   "port":3306,
   "username":"root",
   "schema":"sample"
}
```

Database Service
---

>Yeah, I know it's a bad name

This service is the actual service that communicates with the database, to make it work I've used a multi tenant 
approach, you can find a lot of blog posts explaining what it is.

The `magic` happens in the class `DynamicDataSourceConnectionProvider`, whenever it's necessary to have a
new connection to the database this class creates a new `DataSource` based on the `credentials-api` response.

Here's a sample request to the single endpoint available:

```bash
$ curl localhost:8081/avengers

[  
   {  
      "id":1,
      "name":"iron man"
   },
   {  
      "id":2,
      "name":"hulk"
   },
   {  
      "id":3,
      "name":"thor"
   },
   {  
      "id":4,
      "name":"captain"
   }
]
```

>The database structure is defined in the class `Avenger`.

Footnote
---

I don't know if that's the best approach to solve this kind of problem, also there's a lot of improvement that could be
done in this sample but well, it's just a sample after all, don't follow it as a source of truth.