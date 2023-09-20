package relay.utils;

import java.util.regex.Pattern;

public enum Grammar {


    // REGEX RELAI
    //SEND("^SEND"+SPACE+"("+ID_DOMAINE+")"+SPACE+"("+NOM_DOMAINE+")"+SPACE+
    //        "("+NOM_DOMAINE+"|"+TAG_DOMAINE+")"+SPACE+"("+MESSAGE_INTERNE+")"+"("+CR_LF+")?"),

    EXTRACT_DOMAIN("((#[a-zA-Z\\d]{5,20})@((?:[a-zA-Z0-9]|\\.){5,200})){1}"),
    EXTRACT_COMMAND("((?:FOLLOW|MSGS) .{5,500})");

    private final Pattern pattern;

    Grammar(String str) { this.pattern = Pattern.compile(str); }

    @Override
    public String toString() {
        return pattern.toString();
    }

    public Pattern toPattern() { return pattern; }
}
