package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.dto.CatOwnerDTO;
import com.devsteam.getname.telbot_shelterdc.exception.NoOwnerWithSuchIdException;
import com.devsteam.getname.telbot_shelterdc.exception.OwnerListIsEmptyException;
import com.devsteam.getname.telbot_shelterdc.exception.PetIsNotFreeException;
import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.repository.OwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.PetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.devsteam.getname.telbot_shelterdc.Utils.stringValidation;
import static com.devsteam.getname.telbot_shelterdc.dto.CatOwnerDTO.catOwnerToDTO;
import static com.devsteam.getname.telbot_shelterdc.model.Status.BUSY;
import static com.devsteam.getname.telbot_shelterdc.model.Status.FREE;
import static com.devsteam.getname.telbot_shelterdc.model.StatusOwner.SEARCH;

@Service
public class CatOwnerService {

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;

    private static final Logger logger = LoggerFactory.getLogger(CatOwnerService.class);

    public CatOwnerService(OwnerRepository ownerRepository, PetRepository petRepository) {
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
    }

    /** Метод добавления человека в БД, на вход принимает данные и сохраняет их в базу.
     * @param chatId номер человека в чате.
     * @param fullName ФИО человека.
     * @param phone № телефона.
     * @param address адрес проживания человека.
     */
    public CatOwnerDTO creatCatOwner(Long chatId, String fullName, String phone, String address){
        if (chatId != 0 && stringValidation(fullName)
                && stringValidation(phone)
                && stringValidation(address))
           {
               PetOwner petOwner = new PetOwner(chatId, fullName, phone, address,SEARCH);
               return catOwnerToDTO(ownerRepository.save(petOwner));
               
        } else throw new IllegalArgumentException("Данные человека заполнены не корректно.");
    }

    /** Метод возвращает лист всех сущностей "усыновителей" из базы.
     *
     */
    public List<CatOwnerDTO> getAllCatOwners(){
        List<PetOwner> owners = ownerRepository.findAll();
        if (!owners.isEmpty()) {
            return owners.stream().map(CatOwnerDTO::catOwnerToDTO).collect(Collectors.toList());
        } else throw new OwnerListIsEmptyException();
    }

    /** Метод изменения статуса "усыновителя" кошки.
     * @param idCO id "усыновителя" кошки.
     */
    public void changeStatusOwnerByIdCO(Long idCO, StatusOwner status) {
        PetOwner owner = ownerRepository.findById(idCO).orElseThrow(NoOwnerWithSuchIdException::new);
        owner.setStatusOwner(status);
        ownerRepository.save(owner);
    }

    /** Метод добавления кота (или замены) из БД к "усыновителю" по id с проверкой и сменой статуса кота.
     * Если у кота FREE, то можно передавать и статус меняется на BUSY. Если нет, то Exception.
     * @param idCO id "усыновителя" кошки.
     * @param id id "усыновителя" кошки.
     */
    public void changeCatByIdCO(Long idCO, Long id) {
        PetOwner owner = ownerRepository.findById(idCO).orElseThrow(NoOwnerWithSuchIdException::new);
        Pet pet = petRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (pet.getStatus().equals(FREE)) {
            pet.setStatus(BUSY);
            owner.setCat(pet);
            pet.setCatOwner(owner);
            ownerRepository.save(owner);
            petRepository.save(pet);
        } else throw new PetIsNotFreeException("Животное занято другим человеком.");
    }

    /** Метод удаления у человека животного по какой-либо причине со сменой статуса кота.
     * Например, при отказе в усыновлении или при форс-мажоре.
     * @param idCO id "усыновителя" кошки.
     */
    public void takeTheCatAwayByIdCO(Long idCO) {
        PetOwner owner = ownerRepository.findById(idCO).orElseThrow(NoOwnerWithSuchIdException::new);
        Pet pet = owner.getCat();
        pet.setStatus(Status.FREE);
        pet.setCatOwner(null);
        petRepository.save(pet);
        owner.setCat(null);
        ownerRepository.save(owner);

    }

    /** Метод удаления "усыновителя" животного (или сотрудника приюта) со сменой статуса животного
     * и очистке у него поля "усыновителя".
     * @param idCO id "усыновителя" кошки.
     */
    public void deleteCatOwnerByIdCO(Long idCO){
        try {
            PetOwner owner = ownerRepository.findById(idCO).orElseThrow();
            if (owner.getCat() != null) {
                Pet pet = owner.getCat();
                pet.setCatOwner(null);
                pet.setStatus(FREE);
                petRepository.save(pet);
            }
            ownerRepository.deleteById(idCO);
        } catch (Exception e) {
            throw new IllegalArgumentException("Человека с таким id нет");
        }
    }
}