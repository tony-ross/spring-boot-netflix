# 🎬 Netflix-Style Federated GraphQL Microservices

A comprehensive implementation of Netflix's microservices architecture patterns using Spring Boot 3, demonstrating the evolution from monolith to federated GraphQL microservices.

## 📋 Project Overview

This project showcases a **complete three-phase transformation**:

1. **Phase 1**: Structured Monolith with REST APIs
2. **Phase 2**: GraphQL Integration & Schema Stitching
3. **Phase 3**: Federated Microservices Architecture

### 🏗️ Final Architecture (Phase 3)

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  Client Apps    │    │   Web Browser   │    │  Mobile Apps    │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
          │                      │                      │
          └──────────────────────┼──────────────────────┘
                                 │
                    ┌─────────────▼───────────────┐
                    │     Gateway Service         │
                    │    (GraphQL Federation)     │
                    │       Port: 8080           │
                    └─────────────┬───────────────┘
                                  │
        ┌─────────────────────────┼─────────────────────────┐
        │                         │                         │
┌───────▼────────┐    ┌───────────▼───────┐    ┌───────────▼────────┐
│ Movies Service │    │  Users Service    │    │ Reviews Service    │
│   Port: 8081   │    │   Port: 8082     │    │   Port: 8083      │
└───────┬────────┘    └───────────┬───────┘    └───────────┬────────┘
        │                         │                         │
┌───────▼────────┐    ┌───────────▼───────┐    ┌───────────▼────────┐
│ Movies DB      │    │  Users DB         │    │ Reviews DB         │
│ Port: 5433     │    │  Port: 5434      │    │ Port: 5435        │
└────────────────┘    └───────────────────┘    └────────────────────┘
```

## 🚀 Quick Start

### Prerequisites
- Java 21+
- Docker and Docker Compose
- IntelliJ IDEA (recommended)

### Run All Microservices (Phase 3)

```bash
# Start all services with Docker Compose
docker-compose -f docker-compose-microservices.yml up --build

# Or run individual services for development
cd services/movies-service && ./gradlew bootRun    # Terminal 1
cd services/users-service && ./gradlew bootRun     # Terminal 2
cd services/reviews-service && ./gradlew bootRun   # Terminal 3
cd services/gateway-service && ./gradlew bootRun   # Terminal 4
```

### Run Original Monolith (Phase 1)

```bash
# Start PostgreSQL
docker-compose up -d

# Run the monolith
./gradlew bootRun
```

## 🔗 Service Endpoints

| Service | REST API | GraphQL | GraphiQL | Health |
|---------|----------|---------|----------|--------|
| **Gateway** | N/A | http://localhost:8080/graphql | http://localhost:8080/graphiql | http://localhost:8080/actuator/health |
| **Movies** | http://localhost:8081/api/movies | http://localhost:8081/graphql | http://localhost:8081/graphiql | http://localhost:8081/actuator/health |
| **Users** | http://localhost:8082/api/users | http://localhost:8082/graphql | http://localhost:8082/graphiql | http://localhost:8082/actuator/health |
| **Reviews** | http://localhost:8083/api/reviews | http://localhost:8083/graphql | http://localhost:8083/graphiql | http://localhost:8083/actuator/health |
| **Monolith** | http://localhost:8080/api/* | http://localhost:8080/graphql | http://localhost:8080/graphiql | http://localhost:8080/actuator/health |

## 📊 Technology Stack

### Core Technologies
- **Java 21** - Latest LTS with modern language features
- **Spring Boot 3.2.2** - Enterprise application framework
- **Spring Data JPA** - Data persistence layer
- **PostgreSQL 15** - Primary database
- **GraphQL Java** - GraphQL implementation
- **Docker & Docker Compose** - Containerization

### Architecture Patterns
- **Domain-Driven Design (DDD)** - Clear domain boundaries
- **Database-per-Service** - Data ownership and autonomy
- **API Gateway Pattern** - Single entry point for clients
- **GraphQL Federation** - Unified schema across microservices
- **CQRS & Event Sourcing Ready** - Prepared for advanced patterns

## 📁 Project Structure

```
spring-boot-netflix/
├── 📁 services/                    # Phase 3: Microservices
│   ├── gateway-service/           # GraphQL Federation Gateway
│   ├── movies-service/            # Movies Domain Service
│   ├── users-service/             # Users Domain Service
│   └── reviews-service/           # Reviews Domain Service
├── 📁 src/                        # Phase 1: Monolith
│   ├── main/java/com/yourapp/
│   │   ├── movies/               # Movies Domain
│   │   ├── users/                # Users Domain
│   │   ├── reviews/              # Reviews Domain
│   │   └── common/               # Shared Components
│   └── main/resources/
├── 📄 docker-compose.yml         # Monolith + Database
├── 📄 docker-compose-microservices.yml  # All Microservices
├── 📄 README-Phase3.md           # Detailed microservices guide
└── 📄 build.gradle               # Monolith build configuration
```

## 🎯 Feature Highlights

### Phase 1: Structured Monolith
✅ **Domain-Driven Architecture** - Clean separation of concerns
✅ **REST APIs** - Full CRUD operations for Movies, Users, Reviews
✅ **JPA Relationships** - Optimized queries with @EntityGraph
✅ **Validation & Error Handling** - Comprehensive input validation
✅ **Database Integration** - PostgreSQL with connection pooling
✅ **Health Checks** - Spring Actuator monitoring

### Phase 2: GraphQL Integration
✅ **GraphQL Schema** - Type-safe API with custom scalars
✅ **Data Fetchers** - Efficient resolvers with @QueryMapping
✅ **Schema Stitching** - Combined REST and GraphQL APIs
✅ **GraphiQL Interface** - Interactive query playground
✅ **Computed Fields** - averageRating, reviewCount

### Phase 3: Federated Microservices
✅ **Service Independence** - Separate deployments and scaling
✅ **GraphQL Federation** - @key directives for entity resolution
✅ **Database per Service** - Complete data ownership
✅ **Service Discovery** - Docker networking and health checks
✅ **Cross-Service Queries** - Seamless data federation
✅ **Production Ready** - Docker Compose orchestration

## 🔍 GraphQL Federation Demo

Query movies with reviews from multiple services:

```graphql
query MoviesWithReviews {
  movies {
    id
    title
    director
    reviews {
      id
      rating
      text
      user {
        username
        fullName
      }
    }
    averageRating
    reviewCount
  }
}
```

This single query:
1. Fetches movies from **Movies Service** (Port 8081)
2. Resolves reviews from **Reviews Service** (Port 8083)
3. Resolves user data from **Users Service** (Port 8082)
4. All orchestrated by the **Gateway Service** (Port 8080)

## 🧪 Testing Examples

### REST API Testing
```bash
# Create a movie
curl -X POST http://localhost:8081/api/movies \
  -H "Content-Type: application/json" \
  -d '{"title":"The Matrix","director":"Wachowski Sisters","genre":"Sci-Fi"}'

# Create a user
curl -X POST http://localhost:8082/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"johndoe","email":"john@example.com","password":"password123"}'

# Create a review
curl -X POST http://localhost:8083/api/reviews \
  -H "Content-Type: application/json" \
  -d '{"movieId":1,"userId":1,"rating":5,"text":"Amazing movie!"}'
```

### GraphQL Testing
Visit the GraphiQL interfaces:
- **Gateway**: http://localhost:8080/graphiql (Federated queries)
- **Individual Services**: http://localhost:808{1,2,3}/graphiql

## 📈 Performance & Scalability

### Current Optimizations
- **Connection Pooling** - HikariCP for database connections
- **Lazy Loading** - JPA FetchType.LAZY relationships
- **N+1 Prevention** - @EntityGraph annotations
- **Query Optimization** - Custom JPQL queries
- **Health Monitoring** - Actuator endpoints

### Scaling Strategy
- **Horizontal Scaling** - Each service scales independently
- **Database Sharding** - Separate databases per service
- **Caching Layer** - Ready for Redis/Hazelcast integration
- **Load Balancing** - Multiple instances per service
- **Circuit Breakers** - Resilience patterns ready

## 🔄 Evolution Roadmap

### ✅ Completed Phases
- [x] Phase 1: Structured Monolith with REST APIs
- [x] Phase 2: GraphQL Integration & Schema Stitching
- [x] Phase 3: Federated Microservices Architecture

### 🚀 Future Phases
- [ ] **Phase 4**: Service Mesh (Istio/Linkerd)
- [ ] **Phase 5**: Event-Driven Architecture (Kafka/RabbitMQ)
- [ ] **Phase 6**: CQRS & Event Sourcing
- [ ] **Phase 7**: Kubernetes Deployment
- [ ] **Phase 8**: Observability & Monitoring (Prometheus/Grafana)

## 🏆 Learning Outcomes

This project demonstrates:

🎯 **Microservices Patterns** - Service decomposition, database per service
🎯 **GraphQL Federation** - Schema stitching across services
🎯 **Spring Boot Mastery** - Advanced Spring ecosystem usage
🎯 **Docker Orchestration** - Multi-service containerization
🎯 **Database Design** - JPA relationships and optimization
🎯 **API Design** - RESTful and GraphQL best practices
🎯 **Testing Strategies** - Service and integration testing
🎯 **DevOps Practices** - CI/CD ready structure

## 📚 Documentation

- **[README-Phase3.md](./README-Phase3.md)** - Detailed microservices documentation
- **[API Documentation](./docs/api/)** - REST and GraphQL API references
- **[Architecture Decisions](./docs/architecture/)** - ADRs and design decisions

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙋‍♂️ Author

**Tony Ross** - [GitHub](https://github.com/tony-ross)

*Building scalable, production-ready microservices with modern Java and Spring Boot*

---

⭐ **Star this repository** if you found it helpful for learning microservices architecture!
