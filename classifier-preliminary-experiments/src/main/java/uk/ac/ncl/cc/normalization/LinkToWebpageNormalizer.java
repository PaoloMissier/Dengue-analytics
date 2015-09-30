package uk.ac.ncl.cc.normalization;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by b4060825
 */

public class LinkToWebpageNormalizer implements Normalizer {
    /**
     * Replaces all the links to webpages  within the token
     * with the Keyword "url"
     *
     * @param token
     * @return token with the links to webpages  replaced with keyword
     */


    private String[] link = new String[]{
            "http",
            "https",
            "ftp",
            "gopher",
            "telnet",
            "file:"
    };

    public LinkToWebpageNormalizer() {
        // default constructor
    }

    public LinkToWebpageNormalizer(String[] more) {
        link = ArrayUtils.addAll(link, more);
    }

    @Override
    public String normalize(String token) {

        if (StringUtils.startsWithAny(token.toLowerCase(), link)) {
            token = "url";
        }
        return token;

    }


}
