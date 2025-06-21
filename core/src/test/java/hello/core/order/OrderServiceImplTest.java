package hello.core.order;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OrderServiceImplTest {

    @Test
    void createOrder() {
        OrderServiceImpl orderService = new OrderServiceImpl();
        orderService.createOrder(1L, "itemA", 10000);

        // java.lang.NullPointerException 발생 -> 수정자 주입을 사용하면 자주 발생하는 문제
    }

}