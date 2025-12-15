package AnonymousClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test2 {
    
    public static void main(String[] args) {

        List<String> list = new ArrayList<>(Arrays.asList("hello","hash","map","world"));

        for(int i=0; i<list.size(); i++){
            if(list.get(i).startsWith("t")){
                list.remove(i);
                i--;
            }
        }

        System.out.println("-> "+list);

        String s = "hello\nworld";
        List<String> collect = s.lines().toList();
        System.out.println(collect.size());

        String resultWas = s.lines().reduce("result was", (a, b) -> a + "_" + b);
        System.out.println(resultWas);

    }
}
