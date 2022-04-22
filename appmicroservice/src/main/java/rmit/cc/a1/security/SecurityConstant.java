package rmit.cc.a1.security;

public class SecurityConstant {
    // Secret key
    public static final String SECRET ="p2Woc@SK7K5I$%R773X$0&MnP";

    // time of which the JWT will remain valid for (in ms) 86400000 -> 1 day
    public static final long EXPIRATION_TIME = 86400000;
    public static final String TOKEN_PREFIX= "Bearer ";
    public static final String HEADER_STRING = "Authorization";

}
