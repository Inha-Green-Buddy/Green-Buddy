package com.keb.kebsmartfarm.util;

import com.keb.kebsmartfarm.constant.Message.Error;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public SecurityUtil() {
    }

    public static Long getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException(Error.NO_CERTIFICATE_INFORMATION);
        }
        return Long.parseLong(authentication.getName());
    }
}
