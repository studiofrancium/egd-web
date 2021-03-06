package ee.esutoniagodesu.web.rest;

import ee.esutoniagodesu.Application;
import ee.esutoniagodesu.security.AuthoritiesConstants;
import ee.esutoniagodesu.security.SecurityUtils;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.Serializable;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class WebappTestEnvironment {

    @Resource
    private FilterChainProxy springSecurityFilterChain;

    @Inject
    protected UserDetailsService userDetailsService;

    @Inject
    private WebApplicationContext webApplicationContext;

    @Inject
    protected DataSource dataSource;

    protected MockMvc mockMvc;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static class MockSecurityContext implements SecurityContext, Serializable {

        private static final long serialVersionUID = -1386535243513362694L;

        private Authentication authentication;

        public MockSecurityContext(Authentication authentication) {
            this.authentication = authentication;
        }

        @Override
        public Authentication getAuthentication() {
            return this.authentication;
        }

        @Override
        public void setAuthentication(Authentication authentication) {
            this.authentication = authentication;
        }
    }

    protected MockHttpSession session;
    protected UsernamePasswordAuthenticationToken principal;

    protected UsernamePasswordAuthenticationToken getPrincipal(String username) {

        UserDetails user = this.userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                user,
                user.getPassword(),
                user.getAuthorities());

        return authentication;
    }

    protected void setSession(String username) throws Exception {
        principal = getPrincipal(username);

        session = new MockHttpSession();
        session.setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            new MockSecurityContext(principal)
        );

        UserDetails user = userDetailsService.loadUserByUsername(username);
        Authentication newAuth = new UsernamePasswordAuthenticationToken(user, username, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);


        TestCase.assertTrue(SecurityUtils.isAuthenticated());
        TestCase.assertEquals(SecurityUtils.getUserUuid(), username);
        TestCase.assertTrue(SecurityUtils.isUserInRole(AuthoritiesConstants.ADMIN));


        /*
        MvcResult mvcresult = mockMvc
            .perform(
                get("/oauth/token")
                    .principal(principal)).andDo(print())
            .andExpect(status().isOk()).andReturn();
        MockHttpServletRequest mockhttp = mvcresult.getRequest();
        System.out.println(mockhttp.toString());
        //*/
    }

    @Before
    public void setupMockMvc() throws NamingException {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .addFilters(springSecurityFilterChain)
            .build();
    }
}
