package com.voloshyna.onlinebankingapplication.service.serviceImplements;

import com.voloshyna.onlinebankingapplication.entity.Client;
import com.voloshyna.onlinebankingapplication.entity.Manager;
import com.voloshyna.onlinebankingapplication.entity.Role;
import com.voloshyna.onlinebankingapplication.entity.User;
import com.voloshyna.onlinebankingapplication.repository.ClientRepository;
import com.voloshyna.onlinebankingapplication.repository.ManagerRepository;
import com.voloshyna.onlinebankingapplication.repository.RoleRepository;
import com.voloshyna.onlinebankingapplication.repository.UserRepository;
import com.voloshyna.onlinebankingapplication.service.interf.ManagerService;
import com.voloshyna.onlinebankingapplication.service.interf.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
@Getter
@Setter
public class ManagerServiceImpl implements ManagerService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;

    @Value("${maxListSize}")
    private Integer maxListSize;
    @Autowired
    private Environment environment;


    @Override
    public Manager createManager(String firstName, String lastName, String level, String userEmail, String userPassword) {
        String friendlyBankDomain = environment.getProperty("friendly.bank.domain");
        Role managerRole = roleRepository.findRoleById(2L);
        Role adminRole = roleRepository.findRoleById(1L);


        if (!(userEmail.contains(friendlyBankDomain))) {
            throw new IllegalArgumentException("You not allowed to be manager");
        }

        User user = userService.registrateUser(userEmail, userPassword);
        if (userEmail.startsWith("admin@")){user.setRole(adminRole);}
        else {user.setRole(managerRole);}

        Manager manager = new Manager(firstName, lastName, level, user);
        if (user.getEmail().contains("manager1")) manager.setLevel("JUNIOR");
        if (user.getEmail().contains("manager2")) manager.setLevel("MIDDLE");
        if (user.getEmail().contains("manager3")) manager.setLevel("SENIOR");


        userRepository.save(user);
        managerRepository.save(manager);
//        roleRepository.save(adminRole);
//        roleRepository.save(managerRole);
        return manager;
    }

    @Override
    public void setClientToManager(String managerEmail, String clientEmail) {
        Client client = clientRepository.findClientByEmail(clientEmail);
        Manager manager = managerRepository.findManagerByEmail(managerEmail);
        List<Client> clients = manager.getClientList();
        if (clients.size() < maxListSize) {
            clients.add(client);
        } else {
            throw new IllegalArgumentException("This manager's list is full. Choose another manager or change list size in main properties");
        }
        manager.setClientList(clients);
        managerRepository.save(manager);
        client.setManager(manager);
        clientRepository.save(client);


    }

    @Override
    public List<Manager> getAllManager() {
        List<Manager> managers = managerRepository.findAll();
        return managers;
    }

    @Override
    public List<Manager> searchManager(String keyword) {
        List<Manager> managers = managerRepository.findManagerByName(keyword);
        return managers;
    }

    @Override
    public Manager findByEmail(String managerEmail) {
        Manager manager = managerRepository.findManagerByEmail(managerEmail);
        return manager;
    }

    @Override
    public List<Manager> findManagersByEmail(String managerEmail) {
        List<Manager> managers = managerRepository.findManagersByEmail(managerEmail);
        return managers;
    }

    @Override
    public List<Manager> findByClientTaxNumber(Long taxNumber) {
        List<Manager> managers = managerRepository.findManagerByClientTaxNumber(taxNumber);
        return managers;
    }

    @Override
    public List<Manager> findByClientBankAccountNumber(String bankAccountNumber) {
        List<Manager> managers = managerRepository.findManagerByClientAccountNumber(bankAccountNumber);
        return managers;
    }

    @Override
    public Manager findById(Long managerId) {
        return managerRepository.findById(managerId).orElseThrow(() -> new EntityNotFoundException("Manager not found"));
    }

    @Override
    public List<Manager> findManagersById(Long managerId) {
        return managerRepository.findManagersByIdIsContaining(managerId);
    }

    @Override
    public List<Manager> findByManagerLevel(String managerLevel) {
        return managerRepository.findManagerByLevelContainsIgnoreCase(managerLevel);
    }

    @Override
    public List<Manager> findAll() {
        return managerRepository.findAll();
    }

//    @Override
//    public Manager saveOrUpdateManager(Manager manager) {
//        //Long managerId = manager.getId(); // Отримуємо ID менеджера
////        Manager existingManager = managerRepository.findById(manager.getId()).orElseThrow(() -> new EntityNotFoundException("Manager not found")); // Отримуємо існуючого менеджера з бази даних
////
////        if (existingManager != null) {
////            // Оновлюємо поля існуючого менеджера на основі вхідних даних
////            existingManager.setFirstName(existingManager.getFirstName());
////            existingManager.setLastName(existingManager.getLastName());
////            existingManager.setLevel(existingManager.getLevel());
////
////            // Зберігаємо оновленого менеджера в базі даних
//            managerRepository.save(manager);
//
//
//
//        return manager;
//    }
@Override
    public Manager saveOrUpdateManager(Long managerId, Manager updatedManager) {
        Manager existingManager = managerRepository.findById(managerId).orElseThrow(() -> new EntityNotFoundException("Manager not found"));

        // Оновлення полів оригінального об'єкта менеджера
        existingManager.setFirstName(updatedManager.getFirstName());
        existingManager.setLastName(updatedManager.getLastName());
        existingManager.setLevel(updatedManager.getLevel());

        // Збереження оновленого менеджера у базі даних
        Manager savedManager = managerRepository.save(existingManager);

        return savedManager;
    }

    @Override
    public void deleteManager(Long managerId) {
        Manager manager  = managerRepository.findById(managerId).orElseThrow(() -> new EntityNotFoundException("Manager not found"));
        if(manager.getClientList().isEmpty())
        managerRepository.delete(manager);
        else throw new IllegalArgumentException("Manager's clients list should be empty before removing. Change manager for all clients");
    }

}
