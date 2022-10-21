package cisi.citysight.auth.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role extends BaseEntity{

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_COMMUNITY_TESTER = "ROLE_COMMUNITY_TESTER";
    public static final String ROLE_PROMOTIONAL = "ROLE_PROMOTIONAL";
    public static final String ROLE_CORPORATE = "ROLE_CORPORATE";
    public static final String ROLE_EDITOR = "ROLE_EDITOR";
    public static final String ROLE_MODERATOR = "ROLE_MODERATOR";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_TESTER = "ROLE_TESTER";
    public static final String ROLE_DEVELOPER = "ROLE_DEVELOPER";
    public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    public static final String ROLE_ROOT = "ROLE_ROOT";

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;

}
