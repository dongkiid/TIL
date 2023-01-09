# 카카오 클라우드 스쿨 20230105 - Spring Boot

## Spring Boot

- 단독 실행되는 스프링 기반 애플리케이션을 최소한의 설정으로 만들어 사용하기 위해 등장
- 장점
    - 환경 설정 최소화
    - WAS를 내장해서 독립 실행이 가능한 스프링 애플리케이션 개발이 가능
    - Spring Boot Starter라는 의존성을 제공해서 기존의 Maven, Gradle 설정을 간소화
    - xml 설정 없이 자바 수준의 설정 방식 제공
    - JAR 사용해 자바 옵션만으로 배포 가능
    - 애플리케이션의 모니터링과 관리를 위한 Spring Actuator 제공
- 단점
    - 버전 변경이 너무 잦음
    - 특정 설정을 별도로하거나 설정 자체를 변경하고자하는 경우 내부의 설정 코드를 살펴봐야함

![Untitled](%E1%84%8F%E1%85%A1%E1%84%8F%E1%85%A1%E1%84%8B%E1%85%A9%20%E1%84%8F%E1%85%B3%E1%86%AF%E1%84%85%E1%85%A1%E1%84%8B%E1%85%AE%E1%84%83%E1%85%B3%20%E1%84%89%E1%85%B3%E1%84%8F%E1%85%AE%E1%86%AF%2020230105%20-%20Spring%20Boot%20455296d8789a41fb9c27440cc7153f23/Untitled.png)

- 프레젠테이션 계층 : 보여지는 계층. 모델&뷰와 그것을 관리하는 디스패치서블릿, 컨트롤러 등
- 비즈니스 계층 : 사용자의 요청을 처리하는 부분을 만드는 핵심 비즈니스 로직을 구현하는 영역(트랜잭션 처리나 유효성 검사등의 작업도 여기서!)
- 데이터 접근 계층: 영속(persistence)계층이라고도 하며, DB 접근에 관한 영역

++ 엔티티: 메모리에 상주 / DTO:그때 그때 만들어서 사용하는 것 (완전히 다른 성질)

---

컴파일 되어야 하는 것들은 java 폴더에 넣어야 함니동 (클래스를 만드는 등의 생성 작업을 할 때)

---

### Rest API

1)REST(Representational State Transfer

- 분산 하이퍼 미디어 시스템 아키텍쳐의 한 형식
- 자원에 이름을 정하고 (URL) / 명시된 HTTP메소드를 통해서 해당자원 상태를 주고받는 것
    - 동일한 자원에 대한 요청은 모두 동일한 URL로 처리: seamless로 가능
    - URL은 소문자로만 작성

### 인터페이스

- 일관된 엔터페이스 ⇒ 서버의 상태 정보를 따로 보관하거나 관리하지 않는다는 의미
- 키 사용을 하지 않음 ⇒ 서버에 불필요한 정보를 저장하지 않고 webt token이나  로컬스리지 사용으로 대체 → 캐시 가능성
- Layerd System 서버는 네트워크 상의 여러 계층으로 구성될 수 있지만 클라이언트는 서버의 복잡도와 상관없이 EndPoint만 알면 됨
- 클라이언트 - 서버 아키텍쳐: 클라이언트 애플리케이션과 서버 애플리케이션을 별도로 설계하고 서로에 대한 의존성을 낮추는 것

### URL 설계 규칙

- URL 마지막에 /를 포함하지 않음
- 언더바 대신 하이픈 사용, 소문자로 작성, 확장자 표현 X (다만 파일의 확장자는 accept헤더를 이용)
- URL에는 행위가 아닌 결과를 포함
- 상황) 123번 product를 삭제
    - ex 1) [https://localhost.com/product/123](https://localhost.com/product/123) - Delete 방식으로 요청
    - ex 2) [https://localhost.com/product/delete_product/123](https://localhost.com/product/delete_product/123) - 잘못된 방식

### Get API

- 웹 애플리케이션 서버에서 값을 가져올 때 주로 이용
- URL 매핑
    - 이전에 많이 사용하던 방식: @RequestMapping(method=RequestMethod.GET, value=”url”)
    - 최근: @GetMapping(”URL”)
- 테스트
    - 브라우저에서 테스트 가능
    - API 요청 툴 (ex. POST MAN 등)에서도 가능
- URL에 포함된 파라미터 처리
    - 파라미터가 한개일 때: 파라미터를 URL에 포함시켜 전송할 수 있다.

### Post API

- 리소스를 저장활 때 사용하는 요청 방식
- 리소스나 값을 HTTP body에 담아서 서버에 전달
- 파라미터 처리는 HttpServletRequest나 @RequestParam 그리고 Command 객체를 이용한 처리가 모두 가능한데
    - Command → 클래스 이름 앞에 @RequestBody를 추가해서 HttpBody의 내용을 객체에 매핑하겠다고 며잇적으로 알려주는 것을 권장
- Post방식에서의 파라미터 전송
    - form 태그의 method를 post로 설정해서 폼의 데이터 전송
    - ajax나 fetch api에서 method 속성의 값을 post로 설정해서 전송

### Put API

- 데이터를 수정할 때 사용하는 방식
- 유사한 역할을 하는 것으로 Fetch도 존재
    - 그러나 Put과 달리 Fetch는 멱등성이 없음
    - 일부분 수정 시 Fetch, 전체 수정 시 Put을 사용해야 함
- 사용 방법은 Post와 유사함
- 요청방법
    - ajax나 fetch api에서는 method를 PUT으로 설정
        - form에는 Get과 Post만 설정 가능하므로, put으로 설정시 Get으로 처리 된다. 따라서 form에서 처리하고자 하는 경우에는
        - form 안에 `<input type="hidden" name="_method" value="PUT"/>`을 추가 해서 전송해야 한다.

### DELETE API

- 데이터 삭제, 삭제시 기본키 값 하나만으로 삭제하는 경우가 많기에 GET방식과 동일한 방식으로 처리

### 로깅 라이브러리

- Logging : 애플리케이션이 동작하는 동안 시스템의 상태 정보나 동작 정보를 시간순으로 기록하는 것
- Common Concern(비기능 요구사항)에 속하지만 디버깅하거나 개발 이후 발생한 문제를 해결 할 때 원인 분석에 꼭 필요한 요소
- 로깅 라이브러리 ; Logback
    - spring-boot-starter-web 에 내장 slf4j 를 기반으로 구현
    - 5개의 로그레벨 설정 가능
        - ERROR: 심각한 문제가 발생해서 애플리케이션의 동작이 불가능
        WARN: 시스템 에러의 원인이 될 수 있는 경고 레벨
        INFO: 상태 변경과 같은 정보 전달
        DEBUG: 디버깅할 때 메시지를 출력
        TRACE: DEBUG 보다 자세한 메시지를 출력하고자 할 때 사용

---

# Spring View

### JWA에서 화면을 출력하는 방법

 **JSP 이용** (서버의 데이터를 출력하기 위해 EL(내장), JSTL(외부 라이브러리)을 학습해야함

- EL 은 표현식으로 자바에서 전달한 데이터를 출력하는 문법: `${데이터}`
- JSTL은 Apache 에서 제공하는 Custom Tag로 웹 프로그래밍에서 많이 사용하는 자바 기능을 `태그` 형태로 만들어 준 것

**Template Engine**

- **Tymeleaf (**화면 출력을 위한 템플릿 엔진 중 하나)
    - 별도의 Web Application 이나 Mobile Application을 별도로 제작해서 Server의 데이터를 출력
        - avascript를 이용할 것이라면 ajax, fetch api 또는 axios 라이브러리 사용법을 익혀야 함
        - 모바일의 경우는 서버에서 데이터를 받아오는 방법과 스레드를 미리 학습해야 함.
        - 사용하려면 build.gradle에 의존성을 추가해야한다. `implementation 'org.apache.tomcat.embed:tomcat-embed-jasper’`
    - 장점
        - 데이터 출력이 JSP의 EL과 유사하게 ${}를 이용
        - Model에 담긴 데이터를 화면에서 JavaScript로 처리하기 편리
        - 연산이나 포맷과 관련된 기능을 추가적 라이브러리 없이 지원
        - HTML 파일로 바로 출력이 가능 - 서버 사이드 렌더링을 하지 않고 출력 가능
        - 도큐먼트: https://www.thymleaf.org
        