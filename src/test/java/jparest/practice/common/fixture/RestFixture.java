package jparest.practice.common.fixture;

import jparest.practice.rest.dto.AddFavoriteRestRequest;

import java.util.List;

public class RestFixture {
    public static final String restId1 = "223412312";
    public static final String restName1 = "원할머니 보쌈";

    public static final String restId2 = "223992312";
    public static final String restName2 = "메가 커피";

    public static final String restId3 = "212412312";
    public static final String restName3 = "새마을식당";

    public static final String restId4 = "223412772";
    public static final String restName4 = "일미집";

    public static final String restId5 = "883412312";
    public static final String restName5 = "정돈";

    public static final double latitude = 37.481079886;
    public static final double longitude= 126.9530287;

    public static List<String> restIdList = List.of(restId1, restId2, restId3, restId4, restId5);
    public static List<String> restNameList = List.of(restName1, restName2, restName3, restName4, restName5);

    public static AddFavoriteRestRequest createAddFavoriteRestRequest(Long groupId, String restName) {
        return new AddFavoriteRestRequest(groupId, restName, latitude, longitude);
    }
}
