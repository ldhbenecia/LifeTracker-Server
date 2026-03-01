# CLAUDE.md

## 작업 순서

기능 구현 후 반드시 아래 순서를 따른다:

1. **구현** - 기능 코드 작성
2. **테스트 코드 작성/수정** - 새 기능은 테스트 추가, 변경된 기능은 기존 테스트 수정
3. **테스트 실행 확인** - `./gradlew :api:restDocsTest` 로 테스트가 통과하는지 확인
4. **asciidoc 작성/수정** - `api/src/docs/asciidoc/` 하위 파일 업데이트
5. **ktlint 포맷** - `./gradlew ktlintFormat`
6. **커밋** - 반드시 아래 커밋 규칙에 따라 쪼개서 커밋

## 커밋 규칙

**절대 여러 레이어를 한 커밋에 묶지 않는다.** 반드시 아래 순서대로 각각 분리해서 커밋한다:

| 순서 | 대상 | 경로 |
|------|------|------|
| 1 | `core:*` 도메인 변경 | `core/` |
| 2 | `storage:db-core-jpa` 변경 | `storage/db-core-jpa/` |
| 3 | `api` 컨트롤러/DTO 변경 | `api/src/main/` |
| 4 | 테스트 및 문서 | `api/src/test/`, `api/src/docs/` |

> gradle 설정 변경(`build.gradle.kts`, `settings.gradle.kts`)이 있으면 별도 커밋(순서 0)으로 앞에 추가한다.

**잘못 묶어서 커밋했을 때**: `git reset --soft HEAD~N` → `git reset HEAD` (unstage) → 순서대로 다시 커밋

## 프로젝트 구조

멀티모듈 Gradle 프로젝트 (Kotlin + Spring Boot)

```
api/                  - Spring Boot 진입점, Controller, DTO
core/
  user-core/          - User, Friend 도메인 (Service, Repository 인터페이스)
  todo-core/          - Todo, Category 도메인
storage/
  db-core-jpa/        - JPA Entity, JpaRepository 구현체
  db-core-mongo/      - MongoDB 설정
  db-core-redis/      - Redis 설정
chat/                 - 채팅 도메인
support/              - 모니터링, 로깅
```

## 아키텍처 패턴

- **Reader/Writer/Appender 패턴**: 서비스 레이어를 역할별로 분리
  - `Reader`: 조회 전담
  - `Writer`: 수정/삭제 전담 (단순 위임)
  - `Appender`: 생성 및 상태 변경 전담 (비즈니스 검증 포함)
- **Repository 인터페이스**: `core` 모듈에 인터페이스 정의, `storage` 모듈에 JPA 구현체
- **도메인 객체 분리**: Entity ↔ Domain ↔ DTO 레이어 명확히 분리
- **현재 유저**: `@AuthenticationPrincipal loginUser: LoginUser` 로 컨트롤러에서 주입

## 로컬 서버 실행

```bash
# .env의 환경변수를 로드하고 실행
export $(cat .env | grep -v '^#' | grep -v '^$' | xargs) && ./gradlew :api:bootRun --args='--spring.profiles.active=local'
```

## 테스트

```bash
# RestDocs 테스트 (API 문서 스니펫 생성 포함) - @Tag("restdocs") 기반
./gradlew :api:restDocsTest

# 일반 단위 테스트 (restdocs 태그 제외)
./gradlew :api:test
```

## 코드 스타일

```bash
./gradlew ktlintFormat
```

## API 응답 형식

```json
{
  "status": 200,
  "message": "Success",
  "data": { ... },
  "timestamp": "2024-01-01T00:00:00"
}
```
