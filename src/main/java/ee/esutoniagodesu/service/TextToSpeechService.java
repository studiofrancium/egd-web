package ee.esutoniagodesu.service;

import ee.esutoniagodesu.bean.ProjectDAO;
import ee.esutoniagodesu.util.commons.JCAudio;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.AbstractMap;
import java.util.Map;

@Service
@Transactional
public class TextToSpeechService {

    private static final Logger log = LoggerFactory.getLogger(TextToSpeechService.class);

    @Inject
    private ProjectDAO dao;

    //------------------------------ tts ------------------------------

    public void getSpeechFile(String lang, String q, ServletResponse resp) throws IOException {
        Map.Entry<String, byte[]> file = toSpeech(lang, q);
        resp.setContentType(file.getKey());
        resp.setContentLength(file.getValue().length);
        FileCopyUtils.copy(file.getValue(), resp.getOutputStream());
        resp.flushBuffer();
    }

    private static final String _mime = "audio/mpeg";

    public Map.Entry<String, byte[]> toSpeech(String lang, String text) throws IllegalArgumentException, IOException {
        log.debug("toSpeech: lang=" + lang + ", text=" + text);
        switch (lang) {
            case "et": {
                byte[] bytes = grapFinnishAudioFromGoogle(text);
                return new AbstractMap.SimpleEntry<>(_mime, bytes);
            }
            case "jp": {
                byte[] bytes = grapJapaneseAudioFromGoogle(text);
                return new AbstractMap.SimpleEntry<>(_mime, bytes);
            }
            default:
                throw new IllegalArgumentException("Language not supported");
        }
    }

    public static byte[] grapJapaneseAudioFromGoogle(String string) throws IOException {
        return JCAudio.googleTTSBytes(string, "ja").getValue();
    }

    public static byte[] grapFinnishAudioFromGoogle(String string) throws IOException {
        return JCAudio.googleTTSBytes(string, "fi").getValue();
    }

    //------------------------------ asset management ------------------------------

    @Autowired
    protected ServletContext _servletContext;

    protected static byte[] asBytes(InputStream istream) throws IOException {
        return IOUtils.toByteArray(istream);
    }

    protected InputStream asInputStream(String path) throws FileNotFoundException {
        if (_servletContext != null) {
            return _servletContext.getResourceAsStream(path);
        } else {
            return asInputStream(new File(path));
        }
    }

    private static InputStream asInputStream(File ifile) throws FileNotFoundException {
        return new FileInputStream(ifile);
    }
}
