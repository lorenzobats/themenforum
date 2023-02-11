package de.hsos.swa.application.service.util;

import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;

/**
 * Die Klasse AuthorizationResultMapper verinheitlicht das Mapping des Statuscodes im Authorization Result aus dem
 * Authorization-Gateway in ein ApplicationResult, welches durch die verschiedenen Use Cases
 * an die Interaktions-Adapter zurückgegeben wird.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.output.auth.AuthorizationGateway           Produziert Authorization Results
 * @see ApplicationResult                                                  Ziel-Result
 * @see AuthorizationResult                                                Enthält Status Codes die gemappt werden
 */
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
