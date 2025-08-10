# Sent Server

> **All-in-One 생산성 서비스**  
> 투두, 채팅 등 다양한 기능을 통합하는 멀티모듈 기반 서버 프로젝트

---

## 프로젝트 개요

| 항목      | 내용                                                           |
|-----------|---------------------------------------------------------------|
| 목표      | 투두, 채팅 등 생산성 기능을 한 곳에서 제공하는 통합 백엔드 서비스 |
| 특징      | 멀티모듈, 레이어드 아키텍처 적용<br>확장성/유지보수성/보안 중시 |
| 역할      | 프로젝트 리드, 서버 설계 및 개발, 인프라 구축, 기획/디자인  |

---

## 모듈 구조

```
life-tracker/
├─ api                  # REST API 엔트리포인트
├─ common               # 공통 유틸/예외/DTO
├─ chat                 # 채팅 도메인
├─ storage/
│   ├─ db-core-jpa      # MySQL/JPA 연동
│   └─ db-core-mongo    # MongoDB 연동
├─ tests/
│   └─ api-docs         # API 문서/테스트
├─ core/
│   ├─ user-core        # 사용자 도메인
│   └─ todo-core        # 투두 도메인
└─ support/
    └─ monitoring       # 모니터링/알림/로깅
```

- 모듈별 책임 분리 및 계층화
- 레이어드 아키텍처: Presentation, Application, Domain, Infra

---

## 기술 스택

| 영역             | 기술/도구                                                                                  |
|------------------|------------------------------------------------------------------------------------------|
| Backend          | Spring Boot, Spring Data JPA, Spring Security, Kotlin                                    |
| Database         | MySQL, MongoDB                                                                           |
| Infra/DevOps     | Docker, Docker-compose, Nginx, GCP Compute Engine, Github Actions                        |
| Messaging/알림   | RabbitMQ, FCM                                                                          |
| 테스트/문서화    | Spring Rest Docs, MockK, Asciidoctor                                                     |

---

## 주요 버전

| 구성요소              | 버전           |
|-----------------------|----------------|
| Application           | 0.0.1-SNAPSHOT |
| Kotlin                | 1.9.25         |
| Java                  | 21             |
| Spring Boot           | 3.5.3          |
| Dependency Management | 1.1.7          |
| Asciidoctor           | 3.3.2          |
| ktlint                | 13.0.0         |

---

## 개발 및 운영 환경

- Infra: GCP Compute Engine, Docker, Nginx
- CI/CD: Github Actions
- Security: Spring Security, Nginx
- Monitoring/Alert: Prometheus, Grafana, Discord Webhook, FCM

---

## 프로젝트 방향성

- 통합 생산성 앱: 투두, 채팅 등 다양한 기능을 한 곳에서 제공
- 확장성/유지보수성: 멀티모듈 + 레이어드 아키텍처로 구조화
- 보안 및 인프라 자동화: 실서비스 수준의 보안, 배포, 모니터링 적용
- 실시간 알림/외부 연동: RabbitMQ, FCM 등 다양한 연동 지원

---
