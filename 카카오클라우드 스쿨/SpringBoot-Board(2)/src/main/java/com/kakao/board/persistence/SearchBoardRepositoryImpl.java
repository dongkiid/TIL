package com.kakao.board.persistence;

import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.kakao.board.domain.Board;
import com.kakao.board.domain.QBoard;
import com.kakao.board.domain.QMember;
import com.kakao.board.domain.QReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

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


