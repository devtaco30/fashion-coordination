# 패션 쇼핑몰 코디네이션 서비스

쇼핑몰에서 최저가로 코디 세트를 구성할 수 있는 서비스를 구현합니다.

## 📝 과제 해석 및 구현 범위

**핵심 비즈니스 요구사항:**
1. **카테고리별 최저가 조합**: 사용자가 각 카테고리에서 최저가 상품을 선택하여 전체 코디를 구성
2. **단일 브랜드 최저가**: 사용자가 한 브랜드의 상품만으로 코디를 완성할 때의 최저가 브랜드
3. **카테고리별 가격 범위**: 특정 카테고리의 최저가/최고가 정보로 가격대 파악
4. **상품 관리**: 브랜드와 상품의 CRUD 기능

### 구현 범위
- **기능 1-3**: 조회 기능 (Query Side) - 복잡한 비즈니스 로직 구현
- **기능 4**: 관리 기능 (Command Side) - 기본적인 CRUD + 조회 기능
- **아키텍처**: Clean Architecture + DDD + CQRS 패턴 적용
- **성능**: DB 레벨 최적화 (인덱스, 정렬) + 알고리즘 최적화 (O(n) Linear Search)


## 🚀 기술 스택

- **Language**: Kotlin 1.9.25
- **Framework**: Spring Boot 3.5.3
- **Database**: H2 Database (In-Memory)
- **Architecture**: Clean Architecture + DDD + CQRS
- **Build Tool**: Gradle

## 📋 주요 기능

### 상품 조회 (Query Side)
- **기능 1**: 카테고리별 최저가 상품 조합으로 최저가 코디 세트 조회
- **기능 2**: 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가 브랜드 조회
- **기능 3**: 특정 카테고리의 최저가/최고가 브랜드와 상품 가격 조회
- **기능 4**: 전체 브랜드 및 카테고리 목록 조회

### 상품 관리 (Command Side)
- **기능 4**: 브랜드 생성
- **기능 4**: 상품 생성
- **기능 4**: 상품 수정
- **기능 4**: 상품 삭제

## 📁 아키텍처

본 프로젝트는 **DDD 기반 Clean Architecture**로 구성되어 있습니다.

- **Domain Layer**: 순수 비즈니스 규칙 (Entities, Domain Models, Domain Services, Domain Helpers)
- **Application Layer**: UseCases (Query/Command 분리), Application Models
- **Infrastructure Layer**: Persistence, Web Controllers
- **CQRS 패턴**을 적용하여 Query와 Command를 분리했습니다.

### 프로젝트 구조

```
src/
├── main/kotlin/com/devtaco/fashion/
│   ├── application/
│   │   ├── model/
│   │   └── services/
│   ├── domain/
│   │   ├── entity/
│   │   ├── model/
│   │   ├── service/
│   │   ├── helper/
│   │   └── port/
│   ├── infrastructure/
│   │   ├── persistence/
│   │   └── initialization/
│   ├── presentation/
│   │   └── web/
│   └── global/
│       ├── common/
│       └── config/
```

## 🚀 실행 방법

### Docker Compose로 실행 (권장)

```bash
# 프로젝트 클론
git clone https://github.com/devtaco30/fashion-coordination
cd fashion-coordination

# Docker Compose로 실행
docker compose up -d

# 애플리케이션 시작 확인
curl http://localhost:8090/actuator/health

# 로그 확인
docker compose logs -f

# 컨테이너 중지
docker compose down
```

⚠️ **주의:**  
본 프로젝트는 JDK 21 버전에서 개발 및 테스트되었습니다.  
로컬에 JDK 21이 설치되어 있지 않으면 빌드/실행이 정상적으로 되지 않을 수 있습니다.  
  
- JDK 21 설치 후 JAVA_HOME 환경변수도 21로 맞춰주세요.
- `java -version` 명령어로 버전을 꼭 확인하세요.

💡 **Docker 사용 권장:**  
로컬 환경 설정 없이 바로 실행하려면 Docker Compose를 사용하세요.  
Docker만 설치되어 있으면 별도의 JDK 설치 없이 바로 실행 가능합니다.

### 로컬 실행

```bash
# 프로젝트 클론
git clone https://github.com/devtaco30/fashion-coordination
cd fashion-coordination

# 애플리케이션 실행
./gradlew bootRun

# 또는 IDE에서 FashionApplication.kt 실행
```

### 테스트 실행

```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 실행
./gradlew test --tests "ProductQueryUseCaseTest"
```

## 📚 API 문서

애플리케이션 실행 후 다음 URL에서 API를 테스트할 수 있습니다:

- **Swagger UI**: http://localhost:8090/docs (추천!)
  - 실시간 API 문서 및 테스트
  - 요청/응답 예제 확인
  - 인터랙티브한 API 테스트 가능
- **HTTP 테스트**: `src/test/http/product-api.http`
- **Health Check**: http://localhost:8090/actuator/health

## 🧪 테스트 데이터

애플리케이션 시작 시 자동으로 테스트 데이터가 생성됩니다


## 📖 API 사용법

### 1. 최저가 코디 세트 조회 (카테고리별)

```bash
curl http://localhost:8090/api/v1/products/coordi-set/min-price/category
```

### 2. 최저가 브랜드 코디 세트 조회

```bash
curl http://localhost:8090/api/v1/products/coordi-set/min-price/brand
```

### 3. 카테고리별 최저/최고가 조회

```bash
curl "http://localhost:8090/api/v1/products/price-range?category=top"
```

### 4. 전체 브랜드 목록 조회

```bash
curl http://localhost:8090/api/v1/products/brands
```

### 5. 전체 카테고리 목록 조회

```bash
curl http://localhost:8090/api/v1/products/categories
```

### 6. 브랜드 생성

```bash
curl -X POST http://localhost:8090/api/v1/admin/brands \
  -H "Content-Type: application/json" \
  -d '{
    "name": "새로운 브랜드"
  }'
```

### 7. 상품 생성

```bash
curl -X POST http://localhost:8090/api/v1/admin/products \
  -H "Content-Type: application/json" \
  -d '{
    "brandId": 1,
    "categoryId": 1,
    "price": 15000
  }'
```

### 8. 상품 수정

```bash
curl -X PUT http://localhost:8090/api/v1/admin/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "price": 20000
  }'
```

### 9. 상품 삭제

```bash
curl -X DELETE http://localhost:8090/api/v1/admin/products/1
```

## 🏗️ 아키텍처 특징

### CQRS 패턴
- **Query Side**: `ProductQueryUseCase` - 읽기 전용 작업
- **Command Side**: `ProductManagementUseCase` - 상태 변경 작업

### Domain Helper
- `CoordiSetHelper`: 복잡한 비즈니스 로직을 도메인 헬퍼로 분리
- 카테고리별 최저가 조합, 브랜드별 최저가 조합 로직 구현

### Repository Pattern
- Domain Port: Repository 인터페이스 정의
- Infrastructure: Repository 구현체
- JPA를 통한 데이터 접근

## 🧪 테스트 전략

### Unit Tests
- **Domain Helper Tests**: 비즈니스 로직 테스트
- **Use Case Tests**: 애플리케이션 서비스 테스트
- **Repository Tests**: 데이터 접근 계층 테스트

### HTTP Tests
- IntelliJ IDEA의 HTTP Client를 사용한 API 테스트
- 실제 HTTP 요청을 통한 통합 테스트

