package jparest.practice.common.utils.fixture;

import jparest.practice.rest.dto.AddFavoriteRestRequest;

public class RestFixture {
    public static final String restId = "223412312";
    public static final String restName= "원할머니 보쌈";
    public static final double latitude = 37.481079886;
    public static final double longitude= 126.9530287;

    public static AddFavoriteRestRequest createFavoriteRest(Long groupId) {
        return new AddFavoriteRestRequest(groupId, restName, latitude, longitude);
    }
}
