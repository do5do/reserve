package com.zerobase.reserve.domain.store.repository;

public interface CustomStoreRepository {
    /**
     * 매장 키를 가진 해당 매장을 삭제하는 쿼리입니다.
     * JPA repository에 등록된 delete 메서드 수행 시 select 후 delete로 쿼리가 두번 실행되기 때문에
     * delete만 수행하는 쿼리를 따로 작성하였습니다.
     */
    void deleteByStoreKey(String storeKey);
}
