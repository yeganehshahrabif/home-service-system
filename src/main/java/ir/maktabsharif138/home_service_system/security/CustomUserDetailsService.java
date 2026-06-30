package ir.maktabsharif138.home_service_system.security;

import ir.maktabsharif138.home_service_system.repository.AdminRepository;
import ir.maktabsharif138.home_service_system.repository.CustomerRepository;
import ir.maktabsharif138.home_service_system.repository.ExpertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final ExpertRepository expertRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return customerRepository.findByEmail(email)
                .map(SecurityUser::new)
                .or(() -> expertRepository.findByEmail(email).map(SecurityUser::new))
                .or(() -> adminRepository.findByEmail(email).map(SecurityUser::new))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}