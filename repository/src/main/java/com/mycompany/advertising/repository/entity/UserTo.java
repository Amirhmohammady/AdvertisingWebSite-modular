package com.mycompany.advertising.repository.entity;

import com.mycompany.advertising.api.enums.Role;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Amir on 10/29/2019.
 */

@Entity
public class UserTo  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//AUTO
    private Long id;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "phone number may not be blank")
    @Pattern(regexp = "^09[\\d]{9}$", message = "phone number format should some thing like: 09xxxxxxxxx")
    private String username;
    @Column(nullable = false, unique = true, length = 20)
    @Size(min = 4, max = 50, message = "Username should contain 4 .. 20 chars")
    @NotBlank(message = "Username may not be blank")
    private String profilename;
    @Size(min = 6, message = "password should be at least 6 chars")
    private String password;
    @Size(max = 50, message = "Full Name should less than 50 chars")
    private String fullname;
    @Column(columnDefinition = "TEXT", length = 1024)
    @Size(max = 1024, message = "About me should less than 1024 chars")
    private String aboutme;
    @Size(max = 200, message = "Website URL should less than 200 chars")
    private String websiteurl;
    private String email;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(nullable = false)/*java.util.Set and nullable = false create primary key.
    using java.util.List doesn't create primary key*/
    @Enumerated(EnumType.STRING)
    //@OneToMany(cascade = CascadeType.ALL)
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_to_roles", joinColumns = @JoinColumn(name = "user_to_id"))
    @JoinColumn()//reqired for OnDelete
    @OnDelete(action = OnDeleteAction.CASCADE)//for CASCADE in database
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL})//for CASCADE in JPA
    private Set<Role> roles;
    /*@Column(nullable = true)
    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    private TokenForChangePhoneNumberTo tokenForChangePhoneNumberTo;*/

    public UserTo() {
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = false;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getWebsiteurl() {
        return websiteurl;
    }

    public void setWebsiteurl(String websiteurl) {
        this.websiteurl = websiteurl;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfilename() {
        return profilename;
    }

    public void setProfilename(String profilename) {
        this.profilename = profilename;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void grantAuthority(Role authority) {
        if (roles == null) roles = new HashSet<>();
        roles.add(authority);
    }

    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
        return authorities;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", profilename='" + profilename + '\'' +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", enabled=" + enabled +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}