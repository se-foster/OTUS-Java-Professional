import ru.otus.crm.service.DBCashedServiceClient;

public class CacheApp {
    public static void main(String[] args) {
        var cachedDB = new DBCashedServiceClient();
        cachedDB.run();
    }
}
