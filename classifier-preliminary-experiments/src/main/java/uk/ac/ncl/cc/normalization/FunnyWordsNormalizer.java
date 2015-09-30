package uk.ac.ncl.cc.normalization;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by b4060825 
 */

public class FunnyWordsNormalizer implements Normalizer  {
		/**
	     *  Replaces all the links to webpages  within the token
	     * with the Keyword "url"
	     * 
	     * @param token
	     * @return token with the links to webpages  replaced with keyword
	     */	
	
	 @Override
	    public String normalize(String token)
	    {
	    	
	    	if (StringUtils.equals(token.toLowerCase(),"k")) {
             token="funny";
           }
	    	if (StringUtils.equals(token.toLowerCase(),"kk")) {
	             token="funny";
	    	}
	    	if (StringUtils.equals(token.toLowerCase(),"lol")) {
	             token="funny";
	    	}
	    	if (StringUtils.equals(token.toLowerCase(),"rs")) {
	             token="funny";
	    	}
	    	
	    	token=token.toLowerCase().replaceAll(".*(h+e+h+).*","funny");
	    	token=token.toLowerCase().replaceAll(".*(h+a+h+).*","funny");
	    	token=token.toLowerCase().replaceAll("(r+s+r+s+).*","funny");
	    	token=token.toLowerCase().replaceAll(".*(h+u+a+h+u+).*","funny");
	    	token=token.toLowerCase().replaceAll(".*(h+o+h+o+).*","funny");
	    	token=token.toLowerCase().replaceAll(".*(h+a+.*h+a+).*", "funny");
	    	token=token.toLowerCase().replaceAll(".*(h+e+u+.*h+e+u+).*","funny");
			token=token.toLowerCase().replaceAll(".*(h+u+a+.*h+u+a+).*","funny");
	    	return token;
	    	
	    }
	

}
