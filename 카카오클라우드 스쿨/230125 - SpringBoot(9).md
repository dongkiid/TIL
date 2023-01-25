# 카카오 클라우드 스쿨 20230125 - Spring Boot(9)

### 카카오 로그인 구현 방법

**<카카오 홈페이지에서 해야할 일>**

**REST API 키** 복사

**플랫폼 등록** (포트번호)

카카오 로그인 활성화

**Redirect URI** 설정

동의항목 수집 정보 선택

보안메뉴에서 **Client Secret 코드** 발급 및 저장 

**<스프링에서 해야할 일>**

1. build.gradle에 OAuth2 라이브러리 의존성 설정 확인 (안되어있다면 추가)
2. application.yaml 수정
    
    ```jsx
    //스프링 단에 추가
      security:
        oauth2:
          client:
            registration:
              kakao:
                client-id: {REST_API KEY}
                client-secret: {**Client Secret CODE}**
                redirect-uri: {redirect-uri 주소}
                authorization-grant-type: authorization_code
                client-authentication-method: POST
                client-name: Kakao
                scope: 
                  - profile_nickname
                  - account_email
            provider:  //regiter단과 맞춰야함
              kakao:
                authorization-uri: https://kauth.kakao.com/oauth/authorize
                token-uri: https://kauth.kakao.com/oauth/token
                user-info-uri: https://kapi.kakao.com/v2/user/me
                user-name-attribute: id
    ```
    
    <aside>
    💡 LinkedHashmap과 Hashmap의 차이: 순서 유무(Linked= 순서 有)
    
    </aside>
    

---

OAuth2를 사용했을 때 문제점

- 매번 새로운 유저로 판단한다는 점
    - 따라서 email 같은 정보를 데이터베이스 테이블에 저장해서 이전에 로그인한 적이 있는 유저인지 판단하는 구조로 해결
        - 여러가지 로그인 방법을 제시하는 경우 서로 다른 유저로 판단하는 문제: 소셜 로그인을 통해 로그인을 시도했음에도 불구하고 회원 가입을 다시 시킨다. 이렇게 재가입을 통해 이메일을 등록 하게 만들어서 소셜로 로그인했을 때, 아이디를 찾아주는 방식으로 해결한다.

---

filter는 컨트롤러 전에 호출되는 애들. Handler도 필터의 일부.  handler에서 중간에 나가서 다른 작업을 수행하려고하는 경우 return을 반드시 써주어야함

---

API Server

- 무상태
- 화면을 구성하지 않는다

⇒HTTP의 특성이기도 함( 개발에 약간의 차이가 있기에 주의는 필요하다)

- JSESSIONID 이름의 쿠키를 발행하거나 개발자가 직접 쿠키를 생성하지는 않는 형태로 동작 (그래서 클라이언트와 백엔드의 서버가 다를 경우 쿠키를 공유할 수가 없는데, 이에 대한 대안으로 토큰 부여 방식이 있다.)
    - JWT토큰의 단점
        - RESTAPI key와 도메인 주소만 있으면 탈취(?)가 가능함

SSR은 뷰까지 전달하는 것

CSR 서버에서는 순수 데이터만 전송 / 무상태 / JSON과 XML로 데이터 주고 받음