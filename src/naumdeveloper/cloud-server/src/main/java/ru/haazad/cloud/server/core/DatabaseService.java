public interface DatabaseService {

    void connect();

    void disconnect();
    
    boolean tryLogin(Object[] args);

    boolean tryRegister(Object[] args);

    boolean checkLogin(Object[] args);
}
