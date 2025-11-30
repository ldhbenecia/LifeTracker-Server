# Sent Server

> **All-in-One 생산성 서비스**  
> Multi-Module 기반 All-in-One 생산성 서비스 > Todo, Chat 등 다양한 기능을 통합 제공하는 확장 가능한 백엔드 플랫폼



## 📋 Project Overview
* **Goal**: 생산성 기능(**Todo**, **Chat**)을 통합 제공하는 중앙화된 백엔드 구축
* **Architecture**: 확장성과 유지보수를 고려한 **Multi-Module** 및 **Layered Architecture** 설계
* **Role**: Project Lead (서버 설계/개발, 인프라 구축, 기획)


## 🛠 Tech Stack
| Category | Technology                           |
| :--- |:-------------------------------------|
| **Language & Framework** | Kotlin 1.9, Java 21, Spring Boot 3.5 |
| **Database** | MySQL (JPA), MongoDB, Redis          |
| **Infra & DevOps** | GCP, Docker, Nginx, GitHub Actions   |
| **Messaging & Monitor** | RabbitMQ, Prometheus, Grafana        |
| **Docs & Test** | Spring Rest Docs, MockK              |



## 📦 Module Structure
각 모듈은 역할과 책임에 따라 계층화되어 있습니다.

```text
sent-server/
├── api             # Presentation Layer (REST API)
├── core/           # Domain Layer (User, Todo)
├── chat/           # Chat Domain & Socket Logic
├── storage/        # Persistence Layer (MySQL, MongoDB)
├── support/        # Monitoring, Logging, Alerting
├── common/         # Global Utils, Error Handling, DTOs
└── tests/          # API Docs & Integrated Tests
```
