package hello.core.singleton;

public class SingletonService {

    //1. static 영역에 객체를 딱 1개만 생성해둔다.
    private static final SingletonService instance = new SingletonService();

    //2. public으로 열어서 객체 인스턴스가 필요하면 이 static 메서드를 통해서만 조회하도록 한다.
    // 항상 같은 인스턴스가 반환된다.
    public static SingletonService getInstance() {
        return instance;
    }

    // 3. 생성자를 private으로 선언해서 외부에서 new 키워드를 사용한 객체 생성을 못하게 막는다.
    private SingletonService() {
        /*
        private 생성자는 생성을 막는다.

        다른 클래스에서 new SingletonService()를 못한다.
         */
    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}
