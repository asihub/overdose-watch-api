# OverdoseWatch API

> Analytics REST API for federal overdose surveillance data, enabling public health analysts to identify emerging crisis hotspots across the United States.

**Part of:** [OverdoseWatch](https://github.com/asihub/overdose-watch-ingestion) — open-source overdose surveillance platform  
**License:** Apache 2.0  
**Data Sources:** CDC VSRR · CDC Socrata API · NCHS Drug Overdose Statistics  
**Federal Context:** [CDC Overdose Data to Action](https://www.cdc.gov/drugoverdose/od2a/index.html) · [SAMHSA Strategic Plan 2023-2026](https://www.samhsa.gov/about-us/strategic-plan)

---

## The Problem

Public health analysts tracking the overdose crisis face a fragmented data landscape. Federal overdose surveillance data exists across multiple CDC and SAMHSA sources — each with different formats, geographic encodings, and update schedules.

The result: analysts spend hours manually downloading and normalizing data before any real analysis can begin. Emerging hotspots are identified weeks or months later than they could be.

---

## What This API Does

OverdoseWatch API provides a single standardized REST interface over normalized federal overdose data:
```
CDC VSRR ──────────┐
CDC Socrata API ───┤──► PostgreSQL ──► Analytics API
NCHS Statistics ───┘
```

**Key endpoints:**

- **Trend Analysis** — overdose death trends by drug type and jurisdiction over time
- **Anomaly Detection** — jurisdictions with statistically significant increases
- **Comparative Analytics** — normalized death rates across drug types and regions

---

## Quick Start
```bash
# Clone the repository
git clone https://github.com/asihub/overdose-watch-api.git

# Configure database (requires OverdoseWatch ingestion pipeline)
cp src/main/resources/application.properties.example src/main/resources/application.properties

# Run (requires Java 17+)
./mvnw spring-boot:run
```

API documentation: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## API Reference

### Trend Analysis
```bash
# Deaths by drug type for a jurisdiction
GET /api/v1/trends/by-drug?jurisdiction=United States&fromYear=2020&toYear=2023

# Monthly trend for a specific drug
GET /api/v1/trends/monthly?jurisdiction=United States&drug=Fentanyl&fromYear=2020&toYear=2023
```

### Anomaly Detection
```bash
# Jurisdictions with 20%+ increase in deaths
GET /api/v1/anomalies/detect?drug=Fentanyl&baselineYear=2020&currentYear=2023&threshold=20
```

### Comparative Analytics
```bash
# Death rates per 100,000 by drug type over time
GET /api/v1/compare/rates?panel=All drug overdose deaths&fromYear=2015&toYear=2020

# Deaths by jurisdiction for a specific drug
GET /api/v1/compare/jurisdictions?drug=Fentanyl&year=2023

# Available drug panels
GET /api/v1/compare/panels
```

---

## Sample Response
```json
// GET /api/v1/anomalies/detect?drug=Fentanyl&baselineYear=2020&currentYear=2023&threshold=20
[
  {
    "jurisdiction": "Region 10",
    "drug": "Fentanyl",
    "baselineYear": 2020,
    "baselineDeaths": 8463,
    "currentYear": 2023,
    "currentDeaths": 47205,
    "changePercent": 457.8
  }
]
```

---

## Architecture
```
overdose-watch-api/
├── src/main/java/com/overdosewatch/
│   ├── model/          # JPA entities
│   ├── repository/     # Spring Data repositories
│   ├── controller/     # REST controllers
│   └── config/         # Spring configuration
└── pom.xml
```

**Tech stack:** Java 17 · Spring Boot 3.5.6 · Spring Data JPA · PostgreSQL · Springdoc OpenAPI

---

## Data

This API serves data ingested by the [overdose-watch-ingestion](https://github.com/asihub/overdose-watch-ingestion) pipeline:

| Dataset | Source | Records | Updated |
|---|---|---|---|
| Provisional deaths by specific drugs | CDC Socrata | 6,006 | Monthly |
| State monthly provisional counts | CDC VSRR | 81,270 | Monthly |
| County-level death rates | CDC NCHS | 59,584 | Annual |
| Death rates by drug type | CDC NCHS | 6,228 | Annual |

---

## Background

OverdoseWatch was created to address a gap in public health infrastructure: federal overdose surveillance data is publicly available but technically fragmented, making it difficult for analysts to act quickly on emerging trends.

The project aligns with federal initiatives including CDC Overdose Data to Action, SAMHSA's Behavioral Health Integration program, and the broader federal effort to reduce overdose deaths in the United States.

---

## Related

- [overdose-watch-ingestion](https://github.com/asihub/overdose-watch-ingestion) — Python data ingestion pipeline
- [SUDConnect](https://github.com/asihub/sudconnect) — Individual-level SUD care coordination pipeline

---

## License

Apache 2.0 — free to use, modify, and deploy.

## Contact

For questions, research partnerships, or integration support: [open an issue](https://github.com/asihub/overdose-watch-api/issues)
