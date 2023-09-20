package server.infra.grammar;

import org.junit.jupiter.api.Test;

public class GrammarTagTest {

    @Test
    void tagWith5Char(){
        //Grammar fiveChar = new Grammar();
        //String tag = "#5Char"
        //assertTrue(fiveChar.Name());
    }

    @Test
    void tagWith20Char(){
        //Grammar twentyChar = new Grammar();
        //String tag = "#ilyavinghchargrammar"
        //assertTrue(twentyChar.Name());
    }

    @Test
    void tagLess5Char(){
        //Grammar lessFiveChar = new Grammar();
        //String tag = "#5ca"
        //assertFalse(lessFiveChar.Name());
    }

    @Test
    void tagMore20Char(){
        //Grammar tagMore20Char = new Grammar();
        //String tag = "#abcdefghijklomnpqrstuvwxyz"
        //assertFalse(tagMore20Char.Name());
    }

    @Test
    void tagWithCharSpecial(){
        //Grammar charSpecial = new Grammar();
        //String tag = "#lol/gg-ez"
        //assertFalse(charSpecial.Name());
    }

    @Test
    void tagWithoutHashtag(){
        //Grammar withoutHashtag = new Grammar();
        //Strinf tag = "forgiveHashtag"
        //assertFalse(fiveChar.Name());
    }

    @Test
    void extractTagWithHashTag(){
        //Grammar extractTag = new Grammar();
        //Strinf msg = "voila mon tag #XxXmonTag69XxX il est beau nan"
        //assertEquals(#XxXmonTag69XxX, fiveChar.Name());
    }

    @Test
    void extractTagWithoutHashTag(){
        //Grammar extractNoTag = new Grammar();
        //Strinf msg = "voila mon tag #XxXmonTag69XxX"
        //assertEquals(#XxXmonTag69XxX, fiveChar.Name());
    }
}
