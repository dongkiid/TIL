# 카카오 클라우드 스쿨 20230117 - Spring Boot(6)


### 회원 탈퇴 처리 (게시물 정보 삭제 시 Image 와 Review 삭제 문제도 동일)

- 회원 정보를 삭제하는 경우 외래키로 연결된 Review 데이터를 어떻게 할 것이냐 하는 문제
⇒ 같이 삭제하는 경우가 있고 외래키의 값을 null로 설정하는 방법이 있음
- 회원 정보가 없더라도 리뷰 데이터 자체가 의미를 같는 경우라면 null로 설정하는 것이 바람직하고 리뷰 데이터 자체가 의미가 없다면 삭제한다.
- 삭제를 할 때도 실제 삭제를 하는 경우도 있고 별도의 컬럼을 제공해서 삭제를 한 것처럼 만들기도 한다.
- 회원 정보를 삭제할 때 회원 정보를 삭제하고 리뷰 정보를 삭제하거나 수정하면 에러가 난다.
    - 외래키로 참조하는 테이블의 데이터를 먼저 삭제하거나 수정하고 참조되는 테이블의 데이터를 삭제해야 합니다.
    
    ---
    
    ### 파일 업로드 (썸네일 이미지를 만들어서 출력하고 썸네일 이미지를 클릭하면 원본 이미지 출력)
    
    1.MultipartFile이라는 자료형을 통해 파일 업로드
    
    - 파일 업로드 처리를 위한 Controller 클래스를 생성하고 파일 업로드를 처리할 메서드를 작성 - UploadController
    - 파일 업로드 화면 이동을 위한 Controller 클래스를 생성 - UploadTestController
    - 

2. templates 디렉토리에 uploadajax.html 파일을 만들고 파일 업로드 화면을 작성

- 이미지 파일이 아닐 때 / 파일이 없을 때 Alert
- 유효성 검사 완료시 서버에 데이터 전송

>실행하고 브라우저에서 localhost/uploadajax 를 입력하고 파일을 선택해서 업로드를 진행한 후 브라우저의 콘솔 과 IDE의 콘솔을 확인해서 선택한 파일이 제대로 출력되는지 확인

1. 서버에 파일 저장
    - Spring에서 제공하는 FileCopyUtils 클래스를 이용해도 되고 MultipartFile 클래스에 transferTo 라는 메서드를 이용해도 byte 배열을 직접 읽어서 쓰기 작업을 해도 가능
    - yml 파일에 파일의 업로드 경로를 변수로 생성
    - UploadController 클래스에 날짜 별로 디렉토리를 생성해주는 메서드를 작성
        - UploadController의 파일 업로드 처리 메서드 수정 (저장 경로 추가)
        
2. 파일 업로드 결과 반환 과 화면에서의 처리
    - 파일 업로드 결과를 위한 DTO 클래스 생성 - dto.UploadResultDTO
    - UploadController 클래스의 파일 업로드 처리 메서드를 수정 (리스트에 결과 추가)
    - uploadajax.html 파일에서 업로드 성공 후 result를 출력하도록 코드 수정
    - 업로드 된 이미지를 화면에 출력하기 위해 UploadController 클래스에 파일이름을 받아서 파일의 내용을 전송해주는 요청 처리 메서드를 작성 (GET)
    - uploadajax.html 파일의 업로드 버튼을 눌렀을 때 파일을 전송하고 성공했을 때 호출되는 메서드를 수정 (이미지 파일을 업로드 했을 때 이미지 미리보기가 가능하도록)
    - Thumbnail 이미지 출력
        - 목록보기에서 이미지를 원본 크기 그대로 출력하는 것은 바람직하지 않은데 목록 보기에서는 이미지를 여러 개 출력해야 하기 때문
        이런 경우에는 Thumbnail 이미지를 출력한 후 필요한 경우 원본 이미지를 출력하는 것이 효율적!
        - Thumbnail을 생성해주는 라이브러리를 이용해서 Thumbnail 이미지를 생성하는데 이 때 Thumbnail 이미미지 이름 앞에 s_를 추가
        - UploadResultDTO 클래스에 getThumbnailURL을 추가해서 Thumbnail 이미지의 경로를 클라이언트에 전달
        - UploadResultDTO 클래스에 Thumbnail 이미지 경로를 리턴하는 메서드를 추가
        - UploadController 의 파일 업로드 요청 처리 메서드를 수정 (썸네일 파일 이름, 썸네일 생성)
        - uploadajax.html 파일의 업로드에 성공했을 때 이미지를 출력하는 부분을 수정
    1. 업로드 된 이미지 삭제
    - uploadajax.html 파일에서 삭제 아이콘을 추가하기 위해서 업로드 성공 후 호출되는 코드 수정
    - uploadajax.html 파일에 삭제 버튼을 눌렀을 때 호출되는 코드를 추가
    //삭제 버튼을 눌렀을 때 처리
    - UploadController에 removefile 요청을 POST 방식으로 전송했을 때 처리해주는 메서드를 작성
    
    1. 영화 정보 등록(영화 제목과 영화에 대한 이미지만 선택해서 업로드)
    - 영화에 대한 요청을 처리할 Controller를 생성하고 등록 요청을 처리할 메서드를 생성
    - 영화 등록을 위한 화면을 templates/moview/register.html 파일로 생성
    - 영화 이미지 데이터를 위한 DTO 클래스 생성 - dto.MovieImageDTO
    - Thumbnail 이미지 경로를 리턴하는 메서드
    - 영화 데이터를 위한 DTO 클래스를 생성 - dto.MovieDTO
    - 영화 데이터에 대한 처리를 위한 서비스 인터페이스를 생성하고 삽입 메서드를 선언 - service.MovieService
    - 영화 데이터에 대한 처리를 위한 서비스 클래스를 생성하고 삽입 메서드를 구현 - service.MovieServiceImpl
    - MovieController 클래스에 등록 요청을 처리하는 메서드를 작성
    private final MovieService movieService;
    - register.html 파일에 파일 업로드 와 이미지 미리보기 코드를 작성
    =>파일을 선택했을 때 업로드 하기 위한 스크립트 코드를 작성