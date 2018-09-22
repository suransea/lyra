import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        try {
//            Class.forName("com.sea.lyra.jdbc.LyraDriver");
//            Connection connection = DriverManager
//                    .getConnection("jdbc:lyra://localhost:5494/test",
//                            "root",
//                            "123456");
//            System.out.println(connection);
            List<String> names = new ArrayList<>();
            names.add("qwe");
            names.add("afsef");
            names.add("sdjfn");
            JSONArray array = new JSONArray();
            array.put(names);
            System.out.println(array);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
