package ru.practicum.yandex.security.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;

@Table(value = "roles")
@Data
public class Role implements GrantedAuthority {

    @Id
    int id;

    @Column(value = "rolename")
    String rolename;

    @Override
    public String getAuthority() {
        return "ROLE_"+rolename.toUpperCase();
    }
}
