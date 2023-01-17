# 카카오 클라우드 스쿨 20230110 - Spring Boot(2)


# Spring Boot 에서 데이터베이스 연동

### Test

- 테스트 코드 작성 이유
    - 개발 과정에서 문제를 미리 발견 가능
    - 리팩토링 리스크 줄어듬
    - 하나의 명세 문서가 됨
    - 불필요한 내용이 추가되는 것을 방지

- 테스트 종류
    - 단위 테스트 : 메서드나 클래스 테스트 ( `레포지토리, 서비스, 컨트롤러` )
    - 통합 테스트 : 하나의 (모듈 결합)서비스를 테스트 (각 모듈들을 합쳐서 검증)
    - 시스템 테스트 :
    - 인수 테스트 : 최근, 클라우드 환경에서 개발시 이과정은 생략되기도 한다.

- 코드 작성 방법
    - Give When Then 패턴
        - 단계별로 설정해서 코드를 작성
        - Given : 테스트에 필요한 호나경을 설정 (변수 설정이나 mock객체(가짜객체) 생성
        - When : 테스트의 목적을 보여주는 단계로, 테으스 코드를 작성하고 결괏값을 가져오는 과정
        - Then:결과 검증

- 좋은 Test
    - Fast
    - Elated(독립)
    - Repeatable
    - Self Validating (자가 검증)
    - Timely (적시에) : 실제 구현 되기 전에 테스트

- Spring Boot 에서의 Test
    - spring-boot-starter-test 의 의존성을 설정하면 됨
    - JUnit5 와 Mock이 포함됨

- JUnit5에서 어노테이션
    - @Test: 테스트를 위한 메서드
    @BeforeAll: 테스트를 시작하기 전에 한 번 호출
    @BeforeEach: 모든 테스트 마다 시작하기 전에 호출
    @AfterAll: 테스트를 수행하고 난 후 한 번 호출
    @AfterEach: 모든 테스트 마다 수행하고 난 후 호출

- 가짜 객체? (@Mock Bean)
    - 외부로부터 주입 받아야 하는 경우, 주입 받기 전에 가짜 객체를 생성하여 테스트 가능
    - `@MockBean 클래스 변수이름;`
    
    ---
    

### DB 연동 방법

1. Spring JDBC
2. **JPA(ORM)**이용 // 사용 빈도 높음
3. SQL Mapper Framework (Mybatis)

---

### ORM (Object Relational Mapping)

- 객체 지향 패러다임을 관계형 데이터베이스에 보존하는 기술
- `객체`와 관계형 데이터베이스의 `테이블`을 `매핑`해서 사용
- 관계형 데이터베이스에서 테이블을 설계하는 것 과 Class를 만드는 것을 템플릿을 만든다는 점에서 유사
    
    member 테이블			Member Class				
    uid varchar(50)				String uid
    upw varchar(50)				String upw
    
    =>Instance 와 Row 가 유사
    
- 장점
    - 특정 관계형 데이터베이스에 종속되지 않음
    - 객체지향 프로그래밍
    - 생산성 향상

- 단점
    - 쿼리 처리가 복잡
    - 성능 저하 위험
    - 학습 시간

- JPA(Java Persistence API)
    - Java ORM 기술에 대한 표준 API
    - JPA는 인터페이스이고 구현체로는 Hibernate, EclipseLink, OpenJpa 등이 있음 (Hibernate를 주로 사용)
    - **데이터베이스 ↔ JDBC ↔ Hibernate ↔ JPA**
    - Persistence Context: 애플리케이션 과 데이터베이스 사이의 중재자 역할
        - 직접 연결하지 않고 Persistence Context를 두는 이유는 중간 매개체를 두면 버퍼링 이나 캐싱 등을 활용할 수 있기 때문.
    - 쓰기 지연을 수행: 트랜잭션 처리 - commit 하기 전까지는 데이터베이스에 반영하지 않음
    
    ---
    

### Entity Class 와 JpaRepository

- 개발에 필요한 코드
    - Entity class와 Entity Object
    - Entity를 처리하는 Repository

- 인터페이스로 설계하며, JPA에서 제공하는 인터페이스를 상속만 하면 스프링부트 내부에서 자동으로 객체를 생성하고 실행하는 구조

- 어느 정도의 메서드는 선언만 해도 구현해주는 경우가 있어 단순 CRUD나 페이지 처리 등을 개발할 때 직접 코드를 작성하지 않아도 된다.

- Entity 관련 Annotation
    - @Entity: Entity 클래스 생성, 필수
    - @Table: Entity 와 매핑할 테이블을 설정하는데 생략하면 클래스 이름 과 동일한 이름의 테이블 과 매핑되는데 mysql 이나 maria db는 첫글자를 제외하고 대문자가 있으면 _를 추가하고 소문자로 변경해서 테이블 이름을 생성한다.
    `Member`: oracle-Member, mariadb-member
    `TblMember`: oracle-TblMember, mariadb-tbl_member
    name 속성: 테이블 이름 설정
    uniqueConstraints 속성: 여러 개의 컬럼이 합쳐져서 유일성을 갖는 경우 사용
    uniqueConstraints={@UniqueConstraint(columnNames={컬럼이름 나열})}

    - @Id: 컬럼 위에 기재해서 Primary Key를 설정, 필수

    - @GeneratedValue: 키 생성 전략을 기재하는데 @Id 와 같이 사용

    - @Column: 컬럼 위에 기재해서 테이블의 열 과 매핑
    name: 컬럼 이름 설정, 생략하면 속성 이름 과 동일한 컬럼으로 매핑
    unique
    nullable
    insertable, updateable
    precision: 숫자의 전체 자릿수
    scale: 소수점 자릿수
    length: 문자열 길이

    - @Lob: BLOB 나 CLOB 타입
    lob는 바이트 배열, 보통은 파일의 내용을 저장할 목적으로 사용
    파일을 저장할 때는 대부분의 경우 파일의 경로를 문자열로 저장하고 파일의 내용은 파일 스토리지(Amazon 의 S3가 대표적)에 별도로 둡니다.
    
    - @Enumerated: enum 타입 - check 제약조건
    - @Transient: 테이블에서 제외, 파생 속성 - 다른 속성들을 가지고 만들어내는 속성
    - @Temporal: 날짜 타입 매핑
    - @CreatedDate: 생성 날짜 자동 삽입
    - @LastModifiedDate: 수정 날짜 자동 삽입
    - @Access: 사용자가 값을 사용할 때 바인딩 하는 방식
    - @Access(AccessType.FIELD): 속성을 직접 사용
    - @Access(AccessType.PROPERTY): getter 와 setter 사용