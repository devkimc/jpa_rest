package jparest.practice.common.util;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

@UtilityClass
public class QueryUtils {

    /**
     * QueryDSL 사용 시 Like 예약어를 적용하도록 변환시켜 줍니다.
     */
    public BooleanExpression parseLikeString(StringPath stringPath, String str) {
        return StringUtils.hasText(str) ? stringPath.like("%" + str + "%") : null;
    }
}
