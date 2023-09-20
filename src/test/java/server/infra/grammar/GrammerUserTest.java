package server.infra.grammar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GrammerUserTest {

    @Test
    void nameUserWith5Char(){
        //Grammar fiveChar = new Grammar();
        //String user = "pseudo"
        //assertTrue(user.length <= 5);
    }

    @Test
    void nameUserWith200Char(){
        //Grammar twentyChar = new Grammar();
        //String user = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.
        // Sed ac metus et enim egestas maximus non server.server.utils.utils.view.out.in dui. Nam dictum tincidunt euismod.
        // Pellentesque porta accumsan massa non volutpat. Orci varius non. 1"
        //assertTrue(user.length <= 201);
    }

    @Test
    void nameUserLess5Char(){
        /*String receive = "CONNECT bebou";
        String result = "PARAM";
        assertEquals(result, MessageParser.createAnswer(receive));*/
    }

    @Test
    void nameUserMore200Char(){
        //Grammar tagMore20Char = new Grammar();
        //String user = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean volutpat,
        //                diam server.server.utils.utils.view.out.in convallis auctor, lorem nunc dictum lacus, sit amet pulvinar eros quam nec est.
        // Nullam suscipit neque non velit dolor. "
        //assertFalse(user.length <= 201);
    }

    @Test
    void nameUserWithCharSpecial(){
        //Grammar charSpecial = new Grammar();
        //String user = "XxX_DarK-Shadow_XxX"
        //assertFalse(charSpecial.Name());
    }

}
