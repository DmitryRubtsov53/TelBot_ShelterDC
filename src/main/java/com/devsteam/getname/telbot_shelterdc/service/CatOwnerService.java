package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.dto.CatOwnerDTO;
import com.devsteam.getname.telbot_shelterdc.exception.NoOwnerWithSuchIdException;
import com.devsteam.getname.telbot_shelterdc.exception.OwnerListIsEmptyException;
import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.repository.CatOwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.CatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.devsteam.getname.telbot_shelterdc.Utils.stringValidation;
import static com.devsteam.getname.telbot_shelterdc.dto.CatOwnerDTO.catOwnerToDTO;
import static com.devsteam.getname.telbot_shelterdc.model.StatusOwner.SEARCH;

@Service
public class CatOwnerService {

    private final CatOwnerRepository catOwnerRepository;
    private final CatRepository catRepository;

    private static final Logger logger = LoggerFactory.getLogger(CatOwnerService.class);

    public CatOwnerService(CatOwnerRepository catOwnerRepository, CatRepository catRepository) {
        this.catOwnerRepository = catOwnerRepository;
        this.catRepository = catRepository;
    }

    /** Метод на вход принимает данные человека и сохраняет ее в базу.
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
               CatOwner catOwner = new CatOwner(chatId, fullName, phone, address,SEARCH);
               return catOwnerToDTO(catOwnerRepository.save(catOwner));
               
        } else throw new IllegalArgumentException("Данные человека заполнены не корректно.");
    }

    /** Метод возвращает лист всех сущностей "усыновителей" из базы.
     *
     */
    public List<CatOwnerDTO> getAllCatOwners(){
        List<CatOwner> owners = catOwnerRepository.findAll();
        if (!owners.isEmpty()) {
            return owners.stream().map(CatOwnerDTO::catOwnerToDTO).collect(Collectors.toList());
        } else throw new OwnerListIsEmptyException();
    }

    /** Метод изменения статуса "усыновителя" кошки.
     * @param idCO id "усыновителя" кошки.
     */
    public void changeStatusOwnerByIdCO(Long idCO, StatusOwner status) {
        CatOwner owner = catOwnerRepository.findById(idCO).orElseThrow(NoOwnerWithSuchIdException::new);
        owner.setStatusOwner(status);
        catOwnerRepository.save(owner);
    }

    /** Метод добавления кошки (или замены) из БД к "усыновителю по id".
     * @param idCO id "усыновителя" кошки.
     * @param id id "усыновителя" кошки.
     */
    public void changeCatByIdCO(Long idCO, Long id) {
        CatOwner owner = catOwnerRepository.findById(idCO).orElseThrow(NoOwnerWithSuchIdException::new);
        Cat cat = catRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        owner.setCat(cat);
        catOwnerRepository.save(owner);
    }

    /** Метод удаления у "усыновителя" кота при отказе в усыновлении со сменой статуса кота.
     * @param idCO id "усыновителя" кошки.
     */
    public void takeTheCatAwayByIdCO(Long idCO) {
        CatOwner owner = catOwnerRepository.findById(idCO).orElseThrow(NoOwnerWithSuchIdException::new);
        Cat cat = owner.getCat();
        cat.setStatus(Status.FREE);
        catRepository.save(cat);
        owner.setCat(null);
        catOwnerRepository.save(owner);

    }

    /** Метод удаления "усыновителя" кошки (а также сотрудников приюта).
     * @param idCO id "усыновителя" кошки.
     */
    public void deleteCatOwnerByIdCO(Long idCO){
        try {
            CatOwner owner = catOwnerRepository.findById(idCO).orElseThrow();
            Cat cat = owner.getCat();
            cat.setCatOwner(null);
            catRepository.save(cat);
            catOwnerRepository.deleteById(idCO);
        } catch (Exception e) {
            throw new IllegalArgumentException("Человека с таким id нет");
        }
    }
}