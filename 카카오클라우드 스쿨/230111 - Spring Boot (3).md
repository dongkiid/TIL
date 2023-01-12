# 카카오 클라우드 스쿨 20230111 - Spring Boot(3)


# Querydsl

- JPQL을 코그로 작성할 수 있도록 도와주는 빌더 API
- Querydsl은 SQL 구문을 문자열이 아닌 코드로 작성하기 때문에 컴파일러의 도움을 받을 수 있어서 소스 작성 시 오타가 발생하면 개발자에게 오타가 있음을 바로 알려준다.
- 장점
    - 조건에 맞게 동적으로 쿼리 생성 가능
    - 재사용 및 제약조건 조립, 가독성 향상 가능
    - 컴파일 시점에 오류를 발견하는 것이 쉬움
    - IDE의 도움을 받아서 자동 완성 기능을 이용할 수 있기 때문에 생산성을 향상시킬 수 있음
- 단점
    - 동적인 쿼리를 생성하는 것이 어려움
    - Query annotation 안에 JPQL 문법으로 문자열을 입력하기 때문에 잘못 입력하면 컴파일 시점에 에러를 발견할 수 없음

### MappedSuperclass

- 테이블로 생성되지 않는 Entity 클래스
- 추상 클래스와 유사한데 여러 Entity가 공통으로 가져야하는 속성을 정의하는 클래스
    - 등록 시간이나 수정 시간 등

### JPA Auditing

- Entity 객체에 어떤 변화가 생길 때 감지하는 리스너가 존재
- `@EntityListeners(nalue={클래스이름.class})`
    - Entity에 변화가 생겼을 때 클래스의 메서드가 동작하게 해주는 역할
- 보통 클래스를 직접 만들지 않고 Spring JPA가 제공하는 AuditingEntityListener.class를 설정
    - 이 기능을 사용하기 위해서는 SpringBootApplication 클래스 상단에 @EnableJpaAuditing을 추가해야하 함.

### Entity와 DTO

- Entity와 DTO의 공통점: 속성들을 합쳐서 하나로 묶기 위해 만드는 클래스의 일종
- Repository에서는 Emntity를 이용하고 그 이외의 영역에서는 DTO를 사용하는 것을 권장
- 사용자의 요청이나 응답과 Entity가 일치하지 않는 경우가 많고 Entity는 JPA가 관리하는 Context에 속하기 때문에 직접 관리하는 것은 일관성에 문제가 발생할 가능성이 있기 때문
- DTO는 용도별로 생성하는 것을 기본으로 한다
    - 게시판 CRUD : 읽을 때, 쓸 때 따로 따로.. 최악의 경우 4개까지 (기능별)
    
    ---
    
    실습 게시판 애플리케이션 개요
    
    - 목록 보기 : list ; (get)
    - 등록 : register ; get(등록 화면으로 이동) & post(실제 등록)
    - 조회 : raad ; get
    - 수정 : modify ; get, post
    - 삭제 : remove ; post
    - post 작업은 끝나고 난 후 작업내역 연결을 끊어야한다(=리다이렉트).
        - (새로 고침 했을 때 그 작업이 다시 일어나서 문제를 발생 시키는 것을 방지하기 위해)
    - 구조
        
        Controller(Rest Controller도 생성) ↔ Service(ServiceImpl)↔Repository
        
        DTO                                                       ENTITY
        
    - 목록보기
        - 게시판 형태에서 목록보기 요청
        - 페이지 번호, 페이지당 데이터 개수, 검색항목, 검색값 - 상세한 형태의 요청 DTO 필요
    - 게시판 형태에서 목록보기 응답
        - Pageable 형태의 DTO 목록
        - 페이지 번호 리스트
        - 이전.다음 페이지 여부 및 현재페이지 번호
        - 검색 항목, 검색 값
    - 프로젝트 기본 구조
        - 브라우저에서 들어오는 Req는 GuestbookConroller라는 객체로 처리
        - GuestbookController는 GuestbookService 타입을 주입받는 구조로 만들고 이를 이용해 원하는 작업을 처리
        - GuestbookRepository는 Spring Data JRA를 이용해서 구성하고 GuestbookServicelmpI 클래스에 주입해서 사용
        - 결과는 타임리프를 이용해 레이아웃 템플릿을 활용해 처리
    