package com.example.iwillwin;

import com.example.iwillwin.domain.Memo;
import com.example.iwillwin.pesistence.MemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class RepositoryTest {
    @Autowired
    public MemoRepository memoRepository;

    //------------------DB CRUD---------------

    //주입 확인
    @Test
    public void testDependency() {
        System.out.println("주입 여부:" + memoRepository.getClass().getName());
    }


    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample..."+i).build();
            memoRepository. save(memo);
        });
    }

    @Test
    public void testSelect(){
        //데이터베이스에 존재하는 mno
        Long mno = 100L;
        Optional<Memo> result = memoRepository.findById(mno);
        System.out.println("==================================");
        if(result.isPresent()){
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    //데이터 수정
    @Test
    public void testUpdate() {
        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();
        System.out.println(memoRepository.save(memo));
    }

    //데이터 전체 삭제
    @Test
    public void testDeleteall() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder().build();
            memoRepository.deleteAll();
        });
    }

    //특정 데이터 삭제
    //데이터 삭제 - 100번째 데이터 삭제
    @Test
    public void testDelete() {
        Long mno = 100L;
        memoRepository.deleteById(mno);
    }


    //----------------정렬 및 리스트---------------------------
    @Test
    //페이징
    public void testPageDefault() {
        //1페이지 10개
        Pageable pageable = PageRequest.of(0, 10);
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);

        System.out.println("--------------------------------------------------------------- ");
        System.out.println("Total Pages: " + result.getTotalPages()); //전체 페이지 개수
        System.out.println("Total Count: " + result.getTotalElements()); //전체 데이터 개수
        System.out.println("Page Number: " + result.getNumber()); //현재 페이지 번호 0부터 시작
        System.out.println("Page Size: " + result.getSize()); //페이지당 데이터 개수
        System.out.println("Has next page?:" + result.hasNext()); //다음 페이지존재 여부
        System.out.println("First page?: " + result.isFirst()); //시작 페이지 (0) 여부
        System.out.println("---------------------------------------------------------");

//데이터 순회
        for (Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }
    //데이터 내림차순으로 정렬
    @Test
    public void testSort() {
        Sort sort1 = Sort.by("mno").descending();
        Pageable pageable = PageRequest.of(0, 100, sort1);
        Page<Memo> result = memoRepository.findAll(pageable);
        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    //결합된 조건 하에 정렬
    @Test
    public void testSortConcate() {
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2); //and를 이용한 연결
        Pageable pageable = PageRequest.of(0, 100, sortAll); //결합된 정렬 조건 사용

        Page<Memo> result = memoRepository.findAll(pageable);
        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    //Memo 객체의 mno 값이 70 부터 80 사이의 객체를 검색하고 mno의 역순으로 정렬
    @Test
    public void testQueryMethods(){
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L,80L);
        for (Memo memo : list) {
            System.out.println(memo);
        }
    }
    //Memo객체의 mno값이 10부터 50사이의 객체를 내림차순 정렬해서 검색하고 페이징
    @Test
    public void testQueryMethodsPaging(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("mno").descending());
        Page<Memo> result =
                memoRepository.findByMnoBetween(10L,50L, pageable);

        result.get().forEach(System.out::println);

    }
    //--------------------쿼리--------------------------
    //파라미터 바인딩 – 수정하는 메서드 테스트
    @Test
    public void testUpdateQuery(){
        System.out.println(memoRepository.updateMemoText(11L, "@Query를 이용한 수정"));
        System.out.println(memoRepository.updateMemoText(Memo.builder().mno(12L).memoText("@Query를 이용한 수정").build()));
    }


    //쿼리를 이용한 내림차순 정렬과 페이징 처리
    @Test
    public void testSelectQuery(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Memo> page = memoRepository.getListWithQuery(50L, pageable );
        for(Memo memo : page){
            System.out.println(memo);
        }
    }

    //binding parameter [1] as [BIGINT] - [50], 내림차순 리스트
    @Test
    public void testSelectQueryObjectReturn(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Object []> page = memoRepository.getListWithQueryObject(50L, pageable );
        for(Object [] ar : page){
            System.out.println(Arrays.toString(ar));
        }
    }

    //native SQL
    //JPA 자체가 데이터베이스에 독립적으로 구현이 가능하다는 장점을 잃어버리기는 하지만
    //경우에 따라서는 복잡한 JOIN 구문 등을 처리하기 위해서 사용할 때가 가끔 있음.
    //@Query의 nativeQuery 속성 값을 true로 지정하고 일반 SQL을 그대로 사용할 수 있음.
    @Test
    public void testSelectNativeQuery(){
        List<Object []> list = memoRepository.getNativeResult();
        for(Object [] ar : list){
            System.out.println(Arrays.toString(ar));
        }
    }

}




