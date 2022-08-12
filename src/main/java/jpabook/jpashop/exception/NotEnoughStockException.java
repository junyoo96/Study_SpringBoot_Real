package jpabook.jpashop.exception;

/**
 * @클래스설명 : Exception이 발생했을 때, 메세지와 근본 Exception을 전달하기 위해 오버라이딩 해서 사용
**/

public class NotEnoughStockException extends RuntimeException {

    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }

}
