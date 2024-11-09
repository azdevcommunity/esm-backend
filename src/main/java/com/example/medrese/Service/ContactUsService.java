package com.example.medrese.Service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.medrese.DTO.Request.Create.CreateContactUsDTO;
import com.example.medrese.DTO.Response.ContactUsResponseDTO;
import com.example.medrese.Model.ContactUs;
import com.example.medrese.Repository.ContactUsRepository;
import com.example.medrese.mapper.ContactUsMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContactUsService {

    private final ContactUsRepository contactUsRepository;
    private final ContactUsMapper contactUsMapper;

    @Transactional
    public ContactUsResponseDTO createContactUs(CreateContactUsDTO dto) {
        ContactUs contactUs = contactUsMapper.toEntity(dto);
        ContactUs savedContactUs = contactUsRepository.save(contactUs);
        return contactUsMapper.toResponseDTO(savedContactUs);
    }

    public Page<ContactUsResponseDTO> getAllContacts(Pageable pageable) {
        return contactUsRepository.findAllByRead(pageable, false).map(contactUsMapper::toResponseDTO);
    }

    public ContactUsResponseDTO getContactById(Integer id) {
        ContactUs contactUs = contactUsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id " + id));
        return contactUsMapper.toResponseDTO(contactUs);
    }

    @Transactional
    public void deleteContact(Integer id) {
        contactUsRepository.deleteById(id);
    }

    @Transactional
    public void updateBatch(List<Integer> ids) {
        List<ContactUs> contacts = contactUsRepository.findAllById(ids);
        contacts.forEach(c -> c.setRead(true));
        contactUsRepository.saveAll(contacts);
    }
}