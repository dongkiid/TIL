# 카카오 클라우드 스쿨 20230112 - Spring Boot(4)


# 1: N 관계

### 관계형 데이터베이스의 관계

1) 종류

- 1:1
- 1:N
- N:N(다대다 관계) - 1:N 관계 2개로 분할

2)JPA에서의 관계

- 종류
    - OneToOne
    - OneToMany
    - ManyToOne
    - ManyToMany
- 방향성
    - 단방향성
    - 양방향성
    

3) 회원, 게시글, 댓글간의 관계

회원 (1) : 게시글 (N)
게시글 (1) : 댓글 (N)
회원(1) : 댓글 (N)

⇒ 관계형 데이터베이스에서는 1:n의 경우, 회원 테이블의 기본키를 게시글 테이블의 외래키로 설정
jpa에는 양방향성을 가질 수 있고 반대 방향으로 관계 설정이 가능

---

### 관계 어노테이션의 세부 속성

1) optional

- 필수 여부를 설정하는 것으로 기본값은 true
- 반드시 존재해야하는 경우라면 false로 설정
- 이 값이 true이면 데이터를 가져올 때  outer join하게 되고 false면 inner join을 수행한다
    - inner join: 양쪽 테이블에 모두 존재하는 데이터만 join에 참여
    - outer join 한쪽 테이블에만 존재하는 데이터도 join에 참여
    

2)mappedBy: 양방향 관계 설정 시 다른 테이블에 매핑되는 Entity 필드를 설정 

3)cascade: 상태 변화를 부모 ENTITY에서 자식 ENTITY로 전파 시키는 옵션

- PERSIST : 영속화
- MERGE : 병합
- REMOVE : 삭제
- REFRESH : 새로고침
- DETACH : 더이상 Context의 관리를 받지 않도록 하는 것, 트랜잭션이 종료되더라도 이 객체는 변화가 생기지 않음
- ALL : 모든 상태를 전이

4) opphanRemoval

- JoinColumn의 값이 null인 자식 객체를 삭제하는 옵션

5)fetch

- 외래키로 설정된 데이터를 가져오는 시점에 대한 옵셥으로 Eager과 Lazy 설정

---

### ManyToOne

- 외래키를 소유해야하는 테이블을 위한 Entity에 설정
- @ManyToOne을 컬럼위에 작성
    - 테이블의 방향을 반대로 설정하고 싶으면 @OneToMany
- JoinClumn을 이용해서 join하는 컬럼을 명시적으로 작성할 수 있는데 생략하면 참조변수이름_참조하는 테이블의 기본키컬럼이름으로 생성
- 외래키 값은 부모 Entity에서만 삽입, 수정, 삭제가 가능하며 자식 Entity에서는 읽기만 가능

```jsx
=>Board 테이블에 Member 테이블을 참조할 수 있는 관계를 설정
   
 @ManyToOne
    private Member writer;

=>Replay 테이블에 Board 테이블을 참조할 수 있는 관계를 설정
  
 @ManyToOne
    private Board board;

-----------------------------실행 한 후 데이터베이스 확인
show tables;

desc tbl_member;

desc board;

desc reply;
```

테이블 별 레포지토리 인터페이스 생성 → 테스트 클래스 생성 후 데이터 삽입 테스트를 실시

---

### Eager & Lazy Loading

Entity에 관계를 설정하면 데이터를 가져올 때 참조하는 데이터도 같이 가져온다.

- Lazy Loading
    - 지연로딩이라고 부르며, 데이터를 사용할 때에 가져온다.
    - 관계를 설정할 때 `fetch=FetchType.LAZY` 을 추가하면 지연 로딩이 된다.
    - 지연 로딩을 사용할 때 주의할 사항
        - toString: 내부 모든 속성의 toString을 호출해서 그 결과를 가지고 하나의 문자열을 생성
        - 지연로딩을 하게되면 참조하는 속성의 값이 처음에는 null이 된다. 따라서 null.toString이 호출되서 NullPointerException이 발생하게 되는데,이 문제를 해결하기 위해 지연로딩되는 속성은 toString에서 제외를 해야한다. `@ToString(exclude="제외할 속성이름")`  <<관계 설정 시 추가하면 된다.
        - 지연 로딩을 사용하게 되면 2개의 Select 구문이 수행되어야 한다.
            1. 데이터 찾아오기
            2. 찾아온 데이터의 외래키를 이용해 참조하는 테이블에서 데이터 찾아오기
            
            ⇒  하지만 JPA는 하나의 메서드에서 하나의 Context 연결만을 사용하는데 하나의 SQL 구문이 끝나면 Context 와의 연결해제 되는 특성 탓에 2개의 SQL을 실행할 수 없다. 이런 경우에는 메서드에 `@Transactional`을 붙여서 메서드의 수행이 종료될 때 까지 연결을 해제하지 말라고 주석을 달아두어야 한다.
            
        
        예시 
        
        ```jsx
        (Board Entity)
        @Entity
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @ToString(exclude = "writer") //toString을 만들 때 writer의 toString 호출 안함
        public class Board extends BaseEntity{
            @Id
            @GeneratedValue(strategy = GenerationType.AUTO)
            private Long bno;
            private String title;
            private String content;
        
            @ManyToOne(fetch = FetchType.LAZY)
            //Lazy Loading - 처음에는 가져오지 않고 사용을 할 때 가져옵니다.
            private Member writer;
        }
        (Test)
        @Test
            @Transactional 
            //게시글 1개를 가져오는 메서드
            public void readBoard(){
                Optional<Board> result = boardRepository.findById(100L);
                Board board = result.get();
                System.out.println(board);
                System.out.println(board.getWriter());
            }
        
        => board 테이블이 지연 로딩을 통해 member의 writer를 참조하기 위해서 ToString에 exclude ="writer"를 해주었고, 
         구현 코드에 @Tracsactional 어노테이션을 달아 주었다.
        ```
        

---

### Join

  2개 이상의 테이블 (동일한 테이블 2개도 가능)을 가로방향으로 합치는 작업

1) 종류

- CROSS JOIN (Cartesian Product): 2개 테이블의 모든 조합을 만들어내는 것, 외래키를 설정하지 않았을 때 수행
- INNER JOIN - EQUI JOIN: 양쪽 테이블에 동일한 의미를 갖는 컬럼의 값이 동일한 경우에만 결합
- NON EQUI JOIN: 양쪽 테이블에 동일한 의미를 갖는 컬럼의 값이 동일한 경우를 제외한 방식(>, >=, <, <= 또는 between)으로 결합
- OUTER JOIN: 한 쪽 테이블에만 존재하는 데이터도 JOIN에 참여하는 방식
    - 방향에 따라서 LEFT OUTER JOIN, RIGHT OUTER JOIN, FULL OUTER JOIN 이 있음
- SELF JOIN: 동일한 테이블을 가지고 JOIN, 하나의 테이블에 동일한 의미를 갖는 컬럼이 2개 이상 존재할 때 수행
    - ex) 테이블에 회원 아이디 와 친구 아이디가 같이 존재하는 경우 - 내 친구의 친구를 조회하고자 할 때

2) Join Query

- `@Query("select ? from 엔티티이름 left outer join 엔티티안의참조속성 참조하는테이블의별칭")`
- 이렇게 만들어진 쿼리의 결과는 Object 타입이기 때문에 결과를 Object type의 배열로 형변환해서 사용해야한다.
- join을 수행한 경우는 `Arrays.toString` 을 이용해서 내용을 출력해서 확인해보고 사용해야 함.

3) Join test

```jsx
1. 데이터를 가지고 올 때 참조하는 데이터도 가져오기
(ex. 게시글을 가져올 때 연관된 모든 댓글을 가져오기)

2. Repository 인터페이스에 메서드를 선언

@Query("select b, w from Board b left join b.writer w where b.bno=:bno")
    public Object getBoardWithWriter(@Param("bno") Long bno);

3. 테스트 클래스에 메서드를 만들어서 확인

@Test
    public void joinTest1(){
        Object result = boardRepository.getBoardWithWriter(100L);
        //결과가 배열
        System.out.println(result);
        Object [] ar = (Object []) result;
        System.out.println(Arrays.toString(ar));
        Board board = (Board)ar[0];
        Member member = (Member)ar[1];
    }

=>관계가 설정되지 않은 경우에는 on 절을 이용해서 조인이 가능
```

---

### 서비스 계층 (Board - 게시글 등록)

**흐름**

1. 데이터 전송에 사용하는 게시글의 **DTO**를 생성

<aside>
💡 DTO는 계층 간 데이터 교환을 하기 위해 사용하는 객체로, DTO는 로직을 가지지 않는 순수한 데이터 객체 (getter & setter 만 가진 클래스)

</aside>

1. 게시글 관련 ‘서비스 인터페이스 (service.BoardService)’를 생성하고 기본 메서드를 작성
    - DTO → Entity / Entity → DTO 변환해주는 메서드 등
2. 게시글 관련 ‘서비스 메서드를 구현할 클래스 (service.BoardServiceImpl)’를 생성
3. 서비스 인터페이스에 게시글 등록 요청을 처리할 메서드를 선언
4. 서비스 클래스에 게시글 등록 요청을 처리할 메서드를 구현
5. 서비스 테스트 클래스를 만들어서 확인

### 서비스 계층 (Board - 게시글 목록 보기, 페이징)

**흐름**

1. 게시글 목록 보기 **요청을 저장**할 **DTO**를 생성
    - 페이징 처리를 위한 속성
    - 검색 관련 속성
    - 페이지 관련 기본값 설정을 위한 생성자, 이를 가지고 Pageable 객체를 생성해주는 메서드
2. 게시글 목록 결과를 출력하기 위한 DTO 클래스 생성
    - 데이터 목록 (검색결과, 페이지 번호 관련 속성)
    
    ```jsx
    (dto.PageResponeDTO)
    @Data
    public class PageResponseDTO <DTO, EN>{
    //데이터 목록
    private List<DTO> dtoList;
    
    //페이지 번호 관련 속성
    private int totalPage;//전체 페이지 개수
    private int page;//현재 페이지 번호
    private int size;//페이지 당 데이터 출력 개수
    private int start, end; //페이지 시작 번호 와 끝 번호
    private boolean prev, next; //이전 과 다음 출력 여부
    private List<Integer> pageList; //페이지 번호 목록
    
    //검색 결과(Page<Board>)를 가지고 데이터를 생성해주는 메서드
    public PageResponseDTO(Page<EN> result, Function<EN, DTO> fn){
    //검색 결과 목록을 DTO 의 List로 변환
    dtoList = result.stream().map(fn).collect(Collectors.toList());
    //전체 페이지 개수 구하기
    totalPage = result.getTotalPages();
    //페이지 번호 목록 관련 속성을 결정하는 메서드
    makePageList(result.getPageable());
    }
    
    //페이지 번호 목록 관련 속성을 결정하는 메서드
    private void makePageList(Pageable pageable){
    //현재 페이지 번호
    this.page = pageable.getPageNumber() + 1;
    //데이터 개수
    this.size = pageable.getPageSize();
    
    //임시로 마지막 페이지 번호 계산
    int tempEnd = (int)(Math.ceil(page/10.0)) * 10;
    //시작하는 페이지 번호
    start = tempEnd - 9;
    //이전 여부
    prev = start > 1;
    //마지막 페이지 번호 수정
    end = totalPage > tempEnd ? tempEnd: totalPage;
    //다음 여부
    next = totalPage > end;
    //페이지 번호 목록 만들기
    pageList = IntStream.rangeClosed(start, end)
    .boxed().collect(Collectors.toList());
    }
    }
    ```
    
3. 목록보기 요청을 처리하기 위한 메서드를 ‘서비스 인터페이스 (service.BoardService)’에 선언
    - DTO → Entity / Entity → DTO 변환해주는 메서드 등
4. 목록보기 요청을 처리하기 위한 메서드를 BoardServiceImpl 클래스에 구현
5. 서비스 테스트 클래스를 만들어서 확인

---

### 게시물 CRUD

- 부모 Entity에 있는 데이터를 삭제할 때는 자식 Entity의 데이터를 어떻게 할 것인지 고민
    - 삭제?
    - 삭제된 효과만 나타낼 것인지? (외래키의 값을 null로 하거나 삭제 여부를 나타내는 필드를 추가해서 필드의 값을 변경하는 등)