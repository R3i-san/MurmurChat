package server.infra.parsing;

import server.domain.User;
import server.repo.GRepo;

import java.util.Map;
import java.util.Set;

public class JsonToHtml {

    public static String getHtml(GRepo repo){
        Map<String, Set<User>> trends =  repo.getLocalTrends();
        StringBuilder html = new StringBuilder("<!DOCTYPE html><head></head><body><ul>");
        for (String t : trends.keySet()){
            html.append("<li>").append(t).append("<ul>");

            for (User u : trends.get(t)){
                html.append("<li>").append(u.getName()).append("</li>");
            }

            html.append("</ul></li>");

        }
        return html + "<ul></body><footer>Murmur Client reseaux 2022-2023</footer>";
    }
}
