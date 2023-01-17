# 카카오 클라우드 스쿨 20230115 - Spring Boot(5)

# 게시판 CRUD

### 게시물 등록

- 처리 과정
    - 게시물 등록 링크를 클릭해서 게시물 등록 화면으로 이동하고
    - 데이터를 입력한 후 게시물 등록을 클릭하면 실제 게시물이 등록되고
    - 목록 화면으로 리다이렉트

### 수정 과 삭제

- 수정 과 삭제는 상세보기에 작성
    - 로그인이 있다면 로그인 한 유저 와 작성한 유저의 기본키의 같을 때 만 링크가 보이도록 설정
    - 수정을 한 후 상세보기에서 이전 목록 페이지로 돌아가는지 확인

### Tuple

- 관계형 데이터베이스에서 하나의 행(row)을 Tuple이라고 함
- 특정 언어들에서는 Tuple을 자료형을 제공하는데,
    - 스프링에서는 여러 개의 데이터를 묶어서 하나의 데이터를 표현하기 위한 자료형이다.
- 가장 유사한 다른 형태는 Struct(구조체)
- Java에는 Tuple이라는 자료형이 제공되지 않지만 Spring에서 제공
- 튜플로 그룹화 하는 방법

1. 서치 보드 레포지토리 인터페이스를 만든다
2. 튜플에 관한 것을 선언한다
= 보드로 그룹화 하는 search1 메서드를 선언
2. 서치 보드 레포지토리 임플리먼트 클래스를 만든다
= 보드로 그룹화 하는 serch1 메서드의 구현 기능을 정의
3. 테스트 클래스에서 확인

-> 페이징
1. 서치보드레포지토리 인터페이스에 페이져블을 전송하고 페이지 오브젝트 배열로 리턴 받는 메서드를 선언
2. 서치보드관련 클래스에서 기능 구현
3. 테스트 클래스에서 확인

### 검색 구현

- 검색은 동적 쿼리가 필요하고, 동적 쿼리를 만들기 위해 Querydsl 사용
- 실습
    
    ```jsx
    @Log4j2
    public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {
    
        //QuerydslRepositorySupport 클래스에
        //Default Constructor 가 없기 때문에
        //Constructor를 직접 생성해서
        //필요한 Constructor를 호출해 주어야 하고
        //검색에 사용할 Entity 클래스를 대입해주어야 한다.
    
        public SearchBoardRepositoryImpl() {
            super(Board.class);
        }
    
        @Override
        public Board search1() {
            log.info("search1........................");
            //1. Q도메인 생성 - Querhz
            QBoard board = QBoard.board;
            QReply reply = QReply.reply;
            QMember member = QMember.member;
            // 2. from 절 설정
            JPQLQuery<Board> jpqlQuery = from(board);
    
            // 3. join
            //기존에 @Query를 사용할 땐 특정 클래스를 기준으로 연관관계가 참조된 엔티티를 부를때
            //on절을 사용하지 않아도 되지만, jpql에서는 꼭 써준다.
            jpqlQuery.leftJoin(member).on(board.writer.eq(member));
            jpqlQuery.leftJoin(reply).on(reply.board.eq(board));
    
            /*
            // 5. group by
            jpqlQuery.select(board, member.email, reply.count()).groupBy(board);
            // 만든 쿼리문의 결과집합을 가져온다.
            List<Board> result = jpqlQuery.fetch();
            */
    
            //위에 코드로 Tuple로 변경
            JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
            tuple.groupBy(board);
            //튜플로 만든 쿼리문의 집합 가져오기
            List<Tuple> result = tuple.fetch();
            log.info(result);
            return null;
        }
    
        @Override
        public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
            log.info("searchPage......................");
    
            QBoard board = QBoard.board;
            QMember member = QMember.member;
            QReply reply = QReply.reply;
    
            JPQLQuery<Board> jpqlQuery = from(board);
    
            jpqlQuery.leftJoin(member).on(board.writer.eq(member));
            jpqlQuery.leftJoin(reply).on(reply.board.eq(board));
    
            JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());
    
            //검색 결과가 null인지 아닌지에 따라 동적으로 쿼리를 바꿔서 적용할 수 있게 만드는 코드
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            BooleanExpression expression = board.bno.gt(0L);
    
            booleanBuilder.and(expression);
            // 검색 결과가 null이 아니라면
            if(type != null) {
                String[] typeArr = type.split("");
    
                BooleanBuilder conditionBuilder = new BooleanBuilder();
                //키워드 카테고리 별로 포함된 결과를 조회
                for(String t : typeArr) {
                    switch (t){
                        case "t": conditionBuilder.or(board.title.contains(keyword));
                            break;
    
                        case "w": conditionBuilder.or(member.email.contains(keyword));
                            break;
    
                        case "c": conditionBuilder.or(board.content.contains(keyword));
                            break;
                    }
                }
    
                booleanBuilder.and(conditionBuilder);
            }
            //조건을 tuple에 적용
            tuple.where(booleanBuilder);
    
            for(Sort.Order order: pageable.getSort()) {
                //sorting의 방향
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                //sorting의 기준
                String prop = order.getProperty();
    
                //prop이 담긴 엔티티 경로를 알려줌
                PathBuilder<Board> orderByExpression = new PathBuilder<>(Board.class, "board");
                tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
    
            };
            //그룹화
            tuple.groupBy(board);
    
            //페이지 처리
            tuple.offset(pageable.getOffset());
            tuple.limit(pageable.getPageSize());
    
            //데이터 가져오기
            List<Tuple> result = tuple.fetch();
    
            log.info(result);
    
            //총 개수를 바로 뽑을 수 있음
            long count = tuple.fetchCount();
    
            log.info("COUNT: " + count);
    
            return new PageImpl<Object[]>(
                    result.stream()
                            .map(t -> t.toArray()).collect(Collectors.toList()),
                            pageable, count);
    
        }
    }
    ```
    

+@

Findbytext  = 텍스트로 찾기 
Fintbyrno = 리플 넘버로 찾기

m:n같은 경우는 private으로 선언한 이름으로 FindBy 해주면 됨.
정렬하고 싶으면 orderby로 하는데, 여기서 가치 개입이 일어남.  효율적인 선택을 해야함

### 댓글 처리 (복습 필요)

- 댓글 작업은 RestController 이용

**방식	url			                           파라미터		                     반환되는 데이터**
GET	/replies/board/{bno}	         게시글 번호	          댓글 배열 - 댓글 배열을 포함한 객체
 
POST	/replies			        JSON 문자열	                      추가된 댓글 번호 - 객체

DELETE	/replies/{rno}		         댓글번호		             삭제된 댓글 번호 - 객체

PUT	/replies/{rno}		               댓글번호+ 내용	             수정된 댓글 번호 - 객체

---

복습 자료 

[https://bedmil.tistory.com/31](https://bedmil.tistory.com/31) 

Querydsl의 동적 쿼리를 가독성 좋게 리팩토링한 글

[https://velog.io/@shining_dr/Querydsl-Repository-expansion](https://velog.io/@shining_dr/Querydsl-Repository-expansion)