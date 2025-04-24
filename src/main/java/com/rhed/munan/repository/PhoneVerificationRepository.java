package com.rhed.munan.repository;

import com.rhed.munan.model.PhoneVerification;
import com.rhed.munan.model.PhoneVerification.ZaloStatus;
import com.rhed.munan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhoneVerificationRepository extends JpaRepository<PhoneVerification, Long> {

    List<PhoneVerification> findByUser(User user);

    List<PhoneVerification> findByZaloStatus(ZaloStatus status);

    @Query("SELECT pv FROM PhoneVerification pv WHERE pv.user.id = :userId ORDER BY pv.requestedAt DESC")
    List<PhoneVerification> findByUserIdOrderByRequestedAtDesc(@Param("userId") UUID userId);

    @Query("SELECT pv FROM PhoneVerification pv WHERE pv.user.id = :userId ORDER BY pv.requestedAt DESC LIMIT 1")
    Optional<PhoneVerification> findLatestByUser(@Param("userId") UUID userId);

    @Query("SELECT pv FROM PhoneVerification pv WHERE pv.user.id = :userId AND pv.zaloStatus = :status ORDER BY pv.requestedAt DESC LIMIT 1")
    Optional<PhoneVerification> findLatestByUserAndStatus(@Param("userId") UUID userId, @Param("status") ZaloStatus status);
}