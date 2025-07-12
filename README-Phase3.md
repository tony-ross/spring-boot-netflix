# Netflix Federated GraphQL Gateway - Phase 3: Microservices Architecture

## 🎯 Phase 3 Overview

Phase 3 implements a **Netflix-style federated microservices architecture** where the monolith is decomposed into separate services, each with its own database and GraphQL schema. The architecture follows Netflix's patterns for scalability and team autonomy.

## 🏗️ Microservices Architecture

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

## 🎬 Services Overview

### 1. Gateway Service (Port 8080)
**Purpose**: GraphQL Federation gateway that orchestrates all microservices
- **Technology**: Spring Boot 3.2.2 + GraphQL
- **Responsibilities**:
  - Route GraphQL queries to appropriate services
  - Merge schemas from all microservices
  - Provide unified GraphQL API
  - Handle cross-service data resolution

### 2. Movies Service (Port 8081)
**Purpose**: Manages movie catalog and metadata
- **Technology**: Spring Boot 3.2.2 + JPA + PostgreSQL
- **Database**: `netflix_movies` (Port 5433)
- **Responsibilities**:
  - Movie CRUD operations
  - Movie search (title, genre, director)
  - Movie metadata management
  - GraphQL schema with `@key(fields: "id")`

### 3. Users Service (Port 8082)
**Purpose**: Manages user accounts and profiles
- **Technology**: Spring Boot 3.2.2 + JPA + PostgreSQL
- **Database**: `netflix_users` (Port 5434)
- **Responsibilities**:
  - User registration and management
  - User profile operations
  - Authentication data (username/email uniqueness)
  - GraphQL schema with `@key(fields: "id")`

### 4. Reviews Service (Port 8083)
**Purpose**: Manages movie reviews and ratings
- **Technology**: Spring Boot 3.2.2 + JPA + PostgreSQL
- **Database**: `netflix_reviews` (Port 5435)
- **Responsibilities**:
  - Review CRUD operations
  - Rating calculations
  - Review aggregations
  - Cross-service data federation (extends Movie and User types)

## 🚀 Quick Start

### Prerequisites
- Java 21+
- Docker and Docker Compose
- IntelliJ IDEA (recommended)

### Option 1: Run Individual Services (Development)

1. **Start databases**:
```bash
# Start individual databases
docker run --name movies-db -e POSTGRES_DB=netflix_movies -e POSTGRES_USER=netflix_user -e POSTGRES_PASSWORD=netflix_password -p 5433:5432 -d postgres:15
docker run --name users-db -e POSTGRES_DB=netflix_users -e POSTGRES_USER=netflix_user -e POSTGRES_PASSWORD=netflix_password -p 5434:5432 -d postgres:15
docker run --name reviews-db -e POSTGRES_DB=netflix_reviews -e POSTGRES_USER=netflix_user -e POSTGRES_PASSWORD=netflix_password -p 5435:5432 -d postgres:15
```

2. **Start services in order**:
```bash
# Terminal 1: Movies Service
cd services/movies-service
./gradlew bootRun

# Terminal 2: Users Service
cd services/users-service
./gradlew bootRun

# Terminal 3: Reviews Service
cd services/reviews-service
./gradlew bootRun

# Terminal 4: Gateway Service
cd services/gateway-service
./gradlew bootRun
```

### Option 2: Docker Compose (Production-like)

```bash
# Build and run all services
docker-compose -f docker-compose-microservices.yml up --build

# Run in background
docker-compose -f docker-compose-microservices.yml up -d --build

# Stop all services
docker-compose -f docker-compose-microservices.yml down
```

## 🔗 Service Endpoints

| Service | REST API | GraphQL | GraphiQL | Health |
|---------|----------|---------|----------|--------|
| Gateway | N/A | http://localhost:8080/graphql | http://localhost:8080/graphiql | http://localhost:8080/actuator/health |
| Movies | http://localhost:8081/api/movies | http://localhost:8081/graphql | http://localhost:8081/graphiql | http://localhost:8081/actuator/health |
| Users | http://localhost:8082/api/users | http://localhost:8082/graphql | http://localhost:8082/graphiql | http://localhost:8082/actuator/health |
| Reviews | http://localhost:8083/api/reviews | http://localhost:8083/graphql | http://localhost:8083/graphiql | http://localhost:8083/actuator/health |

## 📊 Database Configuration

Each service has its own dedicated PostgreSQL database:

| Database | Port | Service | Schema |
|----------|------|---------|--------|
| `netflix_movies` | 5433 | Movies Service | movies table |
| `netflix_users` | 5434 | Users Service | users table |
| `netflix_reviews` | 5435 | Reviews Service | reviews table (with movieId, userId FKs) |

## 🔍 GraphQL Federation Examples

### Query Movies with Reviews (Cross-Service)
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

### Query User with Their Reviews
```graphql
query UserWithReviews($userId: ID!) {
  user(id: $userId) {
    id
    username
    fullName
    reviews {
      id
      rating
      text
      movie {
        title
        director
      }
    }
  }
}
```

### Create Review (Cross-Service Mutation)
```graphql
mutation CreateReview($input: CreateReviewInput!) {
  createReview(input: $input) {
    id
    rating
    text
    movie {
      title
    }
    user {
      username
    }
  }
}
```

## 🔧 Development Guidelines

### Adding New Services
1. Create new service directory under `services/`
2. Follow the established package structure: `com.netflix.{service}`
3. Implement GraphQL schema with federation directives
4. Add service to `docker-compose-microservices.yml`
5. Update gateway service configuration

### Federation Directives
- `@key(fields: "id")`: Define entity key for federation
- `@extends`: Extend types from other services
- `@external`: Mark fields as defined in other services
- `@requires(fields: "...")`: Specify required fields
- `@provides(fields: "...")`: Specify provided fields

### Service Communication
- Services communicate through GraphQL federation
- No direct service-to-service HTTP calls
- Gateway handles query planning and execution
- Each service maintains its own data domain

## 🧪 Testing the Architecture

### Health Checks
```bash
# Check all services are running
curl http://localhost:8080/actuator/health  # Gateway
curl http://localhost:8081/actuator/health  # Movies
curl http://localhost:8082/actuator/health  # Users
curl http://localhost:8083/actuator/health  # Reviews
```

### Sample Data Creation
```bash
# Create a movie
curl -X POST http://localhost:8081/api/movies \
  -H "Content-Type: application/json" \
  -d '{"title":"The Matrix","director":"Wachowski Sisters","genre":"Sci-Fi"}'

# Create a user
curl -X POST http://localhost:8082/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"johndoe","email":"john@example.com","password":"password123"}'

# Create a review (use actual movie and user IDs)
curl -X POST http://localhost:8083/api/reviews \
  -H "Content-Type: application/json" \
  -d '{"movieId":1,"userId":1,"rating":5,"text":"Amazing movie!"}'
```

### GraphQL Testing
Visit http://localhost:8080/graphiql to test federated queries that span multiple services.

## 📈 Scaling Considerations

### Service Independence
- Each service can be scaled independently
- Separate databases prevent cross-service dependencies
- Individual deployment pipelines per service

### Performance Optimization
- Implement caching at the gateway level
- Use database connection pooling
- Add service mesh for advanced networking
- Implement circuit breakers for resilience

### Monitoring & Observability
- Each service exposes actuator endpoints
- Centralized logging recommendations
- Distributed tracing for cross-service requests
- Metrics collection for performance monitoring

## 🔄 Evolution Path

### Phase 4 (Future): Service Mesh & Production Features
- Istio/Consul Connect service mesh
- Authentication/authorization service
- Rate limiting and throttling
- Advanced monitoring and alerting
- CI/CD pipelines per service
- Kubernetes deployment manifests

## 🏆 Benefits Achieved

✅ **Service Independence**: Each team can develop and deploy independently
✅ **Technology Freedom**: Services can use different tech stacks if needed
✅ **Scalability**: Individual services scale based on demand
✅ **Fault Isolation**: Service failures don't cascade
✅ **GraphQL Federation**: Unified API despite distributed architecture
✅ **Database Per Service**: Data ownership and autonomy
✅ **Netflix Patterns**: Following proven microservices patterns

This Phase 3 implementation provides a solid foundation for a production-ready, Netflix-style microservices architecture with GraphQL Federation!
