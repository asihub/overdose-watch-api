# OverdoseWatch API — Architecture

## Overview

OverdoseWatch API is a Spring Boot REST API that serves normalized federal overdose surveillance data. It is one of two components in the OverdoseWatch platform — the other being the [Python ingestion pipeline](https://github.com/asihub/overdose-watch-ingestion).
```
CDC VSRR ──────────────────┐
CDC Socrata API ────────────┤──► Python Ingestion Pipeline ──► PostgreSQL ──► Analytics API ──► Consumers
CDC NCHS Drug Statistics ───┘
```

---

## Components

### Data Layer (PostgreSQL)

Four normalized tables populated by the ingestion pipeline:

| Table | Source | Description |
|---|---|---|
| `overdose_specific_drugs` | CDC Socrata `8hzs-zshh` | Provisional deaths by specific drug and jurisdiction |
| `overdose_state_monthly` | CDC VSRR `xkb8-kh2a` | Monthly provisional counts by state |
| `overdose_county` | CDC NCHS `rpvx-m2md` | Model-based death rates by county |
| `overdose_by_drug_type` | CDC NCHS `95ax-ymtc` | Death rates by drug type and demographics |

### API Layer (Spring Boot)
```
com.overdosewatch/
├── model/          # JPA entities mapping to PostgreSQL tables
├── repository/     # Spring Data JPA repositories with JPQL queries
├── controller/     # REST controllers
│   ├── TrendController       # /api/v1/trends
│   ├── AnomalyController     # /api/v1/anomalies
│   └── CompareController     # /api/v1/compare
└── config/         # Spring Security, OpenAPI configuration
```

---

## API Design

### Trend Analysis (`/api/v1/trends`)

Queries `overdose_specific_drugs` table. Supports filtering by jurisdiction, drug type, and year range. Returns aggregated death counts grouped by drug or by month.

### Anomaly Detection (`/api/v1/anomalies`)

Compares death counts between a baseline year and current year for each jurisdiction. Calculates percent change and returns jurisdictions exceeding the threshold. Uses `overdose_specific_drugs` table.

**Algorithm:**
```
changePercent = ((currentDeaths - baselineDeaths) / baselineDeaths) * 100
if changePercent >= threshold → flag as anomaly
```

### Comparative Analytics (`/api/v1/compare`)

Queries `overdose_by_drug_type` for normalized death rates per 100,000 population. Enables comparison across drug types and demographic groups over time.

---

## Technology Stack

| Component | Technology | Version |
|---|---|---|
| Runtime | Java | 17 |
| Framework | Spring Boot | 3.5.6 |
| ORM | Spring Data JPA / Hibernate | 6.x |
| Database | PostgreSQL | 16 |
| API Documentation | Springdoc OpenAPI | 2.8.x |
| Build | Maven | 3.x |

---

## Security

**Current (prototype):** Spring Security disabled for Swagger access. All endpoints are public.

**Production roadmap:**
- API key authentication for public endpoints
- Rate limiting per consumer
- HTTPS enforced via reverse proxy

---

## Deployment

**Local development:**
```bash
# Requires PostgreSQL with ingested data
./mvnw spring-boot:run
```

**Docker:**
```bash
docker build -t overdose-watch-api .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5433/overdosewatch \
  overdose-watch-api
```

**Production (roadmap):** Railway.app with managed PostgreSQL.

---

## Related Components

- [overdose-watch-ingestion](https://github.com/asihub/overdose-watch-ingestion) — Python pipeline that populates the database
- [SUDConnect](https://github.com/asihub/sudconnect) — Individual-level SUD care coordination (companion project)

---

## Future Roadmap

- County-level anomaly detection using `overdose_county` table
- SAMHSA NSDUH data integration
- CDC WONDER integration for verified final counts
- ML-based early warning system
- FHIR-compatible data export