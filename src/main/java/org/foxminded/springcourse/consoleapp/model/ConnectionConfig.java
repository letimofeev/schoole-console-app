package org.foxminded.springcourse.consoleapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectionConfig {

    private final String url;
    private final String user;
    private final String password;

    public ConnectionConfig(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }
}
