# capacity-api

Reactive microservice (Spring WebFlux, R2DBC + MySQL) based on the Pragma hexagonal archetype.

- Port: 8083
- Endpoints: POST and GET on /api/v1/capacity/
- Swagger: http://localhost:8083/swagger-ui.html
- Run: ./gradlew bootRun
- Env: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`
