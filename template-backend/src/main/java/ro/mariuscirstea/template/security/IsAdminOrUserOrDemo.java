package ro.mariuscirstea.template.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole(new String[] {'ADMIN', 'USER', 'DEMO', 'MODERATOR'})")
public @interface IsAdminOrUserOrDemo {
}
