package ee.esutoniagodesu.web.rest;

import ee.esutoniagodesu.bean.GitRepositoryState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/git")
public class GitResource {

    @Autowired
    private GitRepositoryState gitProperties;

    @RequestMapping(value = "",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public GitRepositoryState git() {
        return gitProperties;
    }
}
