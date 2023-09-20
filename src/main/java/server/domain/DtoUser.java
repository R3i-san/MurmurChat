package server.domain;

import java.util.Objects;

public class DtoUser {

    public String name;
    public String bcrypthash;

    public DtoUser(String name, String passwd){
        this.name = name;
        this.bcrypthash = passwd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DtoUser dtoUser = (DtoUser) o;
        return Objects.equals(name, dtoUser.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
