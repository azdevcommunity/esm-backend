package com.example.medrese.Repository;


import com.example.medrese.Model.ContactUs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContactUsRepository extends JpaRepository<ContactUs, Integer> {

    Page<ContactUs> findAllByRead(Pageable pageable, boolean read);
}
