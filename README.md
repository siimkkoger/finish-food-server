## The goal is to build a website to buy products (foods, clothes, etc...) that would be thrown away.



~ *This project is part of personal journey to remind myself of the technologies 
that I have either forgotten or yet not so good at.* ~
____________________
### Terms
- **Platform**
  - **Frontend**: https://github.com/siimkkoger/finish-food-client
  - **Server**: https://github.com/siimkkoger/finish-food-server
- **Product**
  - **Food**
  - **Clothes**
- **Account**:
  - **Producer**: sells products on the platform
  - **User**: buys products from the platform
  - **Admin**: manages the platform

____________________
### Technologies used
- Application
  - Spring Boot
  - Spring Security
  - Spring Data JPA
  - Lombok (@Getter, @Setter, @NoArgsConstructor)
- Load Balancer
  - HAProxy
- Database 
  - PostgreSQL
  - Flyway OR Liquibase
- Messaging
  - RabbitMQ
  - Kafka
- Caching
  - Redis
- Deployment
  - Docker
  - Kubernetes
- Testing
  - JUnit
  - AssertJ
  - Mockito
  - Gatling
- Logging / documentation
  - OpenAPI (Swagger)
  - SpringDoc
- Hosting
  - AWS
- CI/CD
  - Jenkins
- MAYBE (*probably for another project*):
  - Spring WebFlux + R2DBC (Reactive Relational Database Connectivity)
  - Virtual Threads (Project Loom)
  - Graphql
____________________
### The platform should handle the following
- Providers can add/update/delete products on the platform
- Users can buy products from the platform
- Users can rate products and providers
- Loyalty system
____________________

### Backend should have
- High throughput
- High availability
- High scalability
- High security
____________________
## Architecture
...

____________________

## Setup (localhost windows)
### Redis
Redis is not supported on Windows but can be installed on WSL2.
```bash
curl -fsSL https://packages.redis.io/gpg | sudo gpg --dearmor -o /usr/share/keyrings/redis-archive-keyring.gpg
echo "deb [signed-by=/usr/share/keyrings/redis-archive-keyring.gpg] https://packages.redis.io/deb $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/redis.list
sudo apt-get update
sudo apt-get install redis
```
To start Redis server:
```bash
sudo service redis-server start
```
To stop Redis server:
```bash
sudo service redis-server stop
```
To check Redis server status:
```bash
sudo service redis-server status
```
Determine WSL2 IP address:
```bash
ip addr show eth0 | grep -oP '(?<=inet\s)\d+(\.\d+){3}'
--- or ---
hostname -I
```
