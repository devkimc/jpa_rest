package jparest.practice.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public interface ApiDocumentUtils {

//    @Value("{domain.host}")
//    String domain = null;

//    @Value("{server.port}")
//    Integer port = null;

    static OperationRequestPreprocessor getDocumentRequest() {

        return preprocessRequest(
//                modifyUris()
//                        .scheme("https")
                prettyPrint()
        );
    }

    static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(prettyPrint());
    }
}
