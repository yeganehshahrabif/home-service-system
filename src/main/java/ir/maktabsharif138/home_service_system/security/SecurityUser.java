package ir.maktabsharif138.home_service_system.security;

import ir.maktabsharif138.home_service_system.entity.BaseUser;
import ir.maktabsharif138.home_service_system.entity.Customer;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.enums.AccountStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SecurityUser implements UserDetails {

    private final BaseUser user;

    public SecurityUser(BaseUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @   Override
    public boolean isAccountNonLocked() {
        return !AccountStatus.INACTIVE.equals(user.getAccountStatus());
    }

    @Override
    public boolean isEnabled() {

        if (user instanceof Expert expert) {
            return expert.isEmailVerified() && expert.getAccountStatus() == AccountStatus.APPROVED;
        }

        if (user instanceof Customer customer) {
            return customer.isEmailVerified();
        }

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public BaseUser getUser() {
        return user;
    }
}