package ee.esutoniagodesu.config;

import ee.esutoniagodesu.security.AuthoritiesConstants;
import ee.esutoniagodesu.security.permission.CustomPermissionEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;

@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class MethodSecurityConfig {//extends GlobalMethodSecurityConfiguration {

    //@Bean
    public RoleHierarchyVoter roleHierarchyVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }

    //@Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl rhi = new RoleHierarchyImpl();
        rhi.setHierarchy(
            AuthoritiesConstants.ADMIN + " > " + AuthoritiesConstants.USER + " " +
                AuthoritiesConstants.USER + " > " + AuthoritiesConstants.ANONYMOUS
        );
        return rhi;
    }

    //@Bean
    public CustomPermissionEvaluator permissionEvaluator() {
        return new CustomPermissionEvaluator();
    }

    //@Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        //expressionHandler.setPermissionEvaluator(permissionEvaluator());
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }
}
