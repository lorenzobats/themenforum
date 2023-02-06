package de.hsos.swa.application.service.util;

import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;

public class AuthorizationResultMapper {
    public static <T> ApplicationResult<T> handleRejection(AuthorizationResult.Status status) {
        switch (status){
            case USER_NOT_AUTHENTICATED -> {
                return ApplicationResult.noAuthorization();
            }
            case ACCESS_DENIED -> {
                return ApplicationResult.noAccess();
            }
            case ERROR -> {
                return ApplicationResult.exception();
            }
        }
        return ApplicationResult.exception();
    }
}
