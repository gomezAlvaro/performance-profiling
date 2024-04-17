# Profiling with Java Flight Recorder

## Setup

### Start a Database
You can use an in-memory standalone H2 or a PostgreSQL in a Docker.
#### A) Standalone H2
Run `StartDatabase.java` to start the H2 database server. 
Make sure you use option (GB-H2) in the application.properties file.
(url = `jdbc:h2:tcp://localhost/~/test`)

#### B) Postgres in Docker
If you have a Docker Desktop installed on your machine:
- Start Postgres using the `docker/docker-compose.yml`
- Use option (DB-PG) in the application.properties file.

### Add DB Latency😴
To simulate network delay in talking to a DB running on the same machine, choose ONE of the options below.
#### A) Hibernate Interceptor
Use `SimulateNetworkDelayHibernateInterceptor` in the properties files to add a fixed delay to any prepared statement 
created by Hibernate.
#### B) ToxiProxy
Emulate network latency by driving all DB traffic through a proxy delaying network packets. 
- Install [ToxiProxy](https://github.com/Shopify/toxiproxy#1-installing-toxiproxy) to your local machine.
- Check it's started on port 8474 - you should see a 404 page at [http://localhost:8474](http://localhost:8474) 
- Run `ConfigureToxiproxy`
- Change DB Port in application properties to point to the proxy port (eg) 5432 -> 55432


### WireMock to simulate API calls with delay
The WireMock stubs are in `src/test/wiremock/mappings` folder.
#### A) Standalone Java Program
Run `StartWireMock.java` to start the WireMock server.
#### B) In Docker
Start 'wiremock' image in docker-compose.yml


### Start your app with Glowroot agent
Glowroot is a lightweight Java agent that collects performance metrics and traces.
You can download it from [glowroot.org](https://glowroot.org/).
Unzip the dist zip, locate the `glowroot.jar` and copy the path to it.

Add glowroot.jar as a java agent to your application by adding `-javaagent:/path/to/glowroot.jar`.
You can do that in IntelliJ by filling the 'VM option' field of your run configuration.

When you run the application, you can access the Glowroot UI at http://localhost:4000. You should see a page like this:
![img.png](art/glowroot.png)

## Start the application
Run `ProfiledApp.java` with the Glowroot agent.

## Exercices - Run the load tests and find the issues using the flame graph
1. Run `BasicLoadTest.java` -- We have an unexpected column, why?
2. Run `LoanStatusLoadTest.java` -- What is the biggest time loss? 
3. Run `GetLoanLoadTest.java` -- Some more unexpected code is executed and appears in the flame graph, can you find it?

Note: We are using an artificial 'closed' load test model:
(the number of users if fixed)

## See the flamegraph
Go to http://localhost:4000/transaction/thread-flame-graph?transaction-type=Web
