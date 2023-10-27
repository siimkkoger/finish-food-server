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

## Setup
...
