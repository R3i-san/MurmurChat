package server.infra.parsing;

import java.util.regex.Pattern;

public enum Grammar {

    CHIFFRE("\\d"),
    ROUND(CHIFFRE+"{2}"),
    SPACE(" "),
    PORT(CHIFFRE+"{1,5}"),
    CR_LF("\\r\\n"),
    LETTRE("a-zA-Z"),
    PRINTABLE_CHAR("."),
    SYMBOLE("[\\x21-\\x2F\\x3A-\\x40\\x5B-\\x60]"),
    LETTRE_CHIFFRE("["+ LETTRE + CHIFFRE + "]"),
    DOMAIN("("+LETTRE_CHIFFRE+"|\\.){5,200}"),
    BCRYPTE_HASH("\\$2b\\$"+ROUND+"\\$("+LETTRE_CHIFFRE+"|"+SYMBOLE+"){1,70}"),
    SHA3_HEX("("+LETTRE_CHIFFRE+")"+"{30,200}"),
    SALT_SIZE(CHIFFRE+"{2}"),
    RANDOM22("("+LETTRE_CHIFFRE+"|"+SYMBOLE+"){22}"),
    SALT(""+RANDOM22),
    MESSAGE(PRINTABLE_CHAR+"{1,200}"),
    MESSAGE_INTERNE("(FOLLOW|MSG)"+"{1,500}"),
    NOM_USER(LETTRE_CHIFFRE+"{5,20}"),
    TAG("(#"+LETTRE_CHIFFRE+"{5,20})"),
    NOM_DOMAINE("("+NOM_USER+")"+"@"+"("+DOMAIN+")"),
    TAG_DOMAINE("("+TAG+")"+"@"+"("+DOMAIN+")"),
    ID_DOMAINE("["+CHIFFRE+"]{1,5}@"+DOMAIN),

    // REGEX SPECIFIQUES (parenthese nécessaire pour extraire le groupe grace à la classe Regex)
    CHECKER("^(CONNECT|CONFIRM|REGISTER|FOLLOW|MSG|DISCONNECT|SEND).*("+CR_LF+")?"),

    CHECKER_2("^(FOLLOW|MSG|SEND).*("+CR_LF+")?"),

    //^CONFIRM (([a-zA-Z\d]){30,200})(\r\n)?

    CONNECT("^CONNECT"+SPACE+"("+NOM_USER+")("+CR_LF+")?"),
    CONFIRM("^CONFIRM"+SPACE+"("+SHA3_HEX+")("+CR_LF+")?"),
    REGISTER("^REGISTER"+SPACE+"("+NOM_USER+")"+SPACE+"("+SALT_SIZE+")"+SPACE+"("+BCRYPTE_HASH+")"+"("+CR_LF+")?"),
    FOLLOW("^FOLLOW (#?[a-zA-Z]{5,20})(?:@)([a-zA-Z\\d\\.]{2,200})"+"("+CR_LF+")?"),
    MSG("^MSG"+SPACE+"("+MESSAGE+")"+"(?:"+CR_LF+")?"),
    MSGS("^MSG"+SPACE+"("+NOM_DOMAINE+")"+SPACE+"("+MESSAGE+")"+"("+CR_LF+")?"),
    DISCONNECT("^DISCONNECT"+"("+CR_LF+")?"),
    // REGEX RELAI
    SEND("^SEND"+SPACE+"("+ID_DOMAINE+")"+SPACE+"("+NOM_DOMAINE+")"+SPACE+
            "("+NOM_DOMAINE+"|"+TAG_DOMAINE+")"+SPACE+"("+MESSAGE_INTERNE+")"+"("+CR_LF+")?");

    private final Pattern pattern;

    Grammar(String str) { this.pattern = Pattern.compile(str); }

    @Override
    public String toString() {
        return pattern.toString();
    }

    public Pattern toPattern() { return pattern; }
}
