package com.company.inventory.customers.services;

import com.company.inventory.categories.model.Category;
import com.company.inventory.categories.response.CategoryResponseRest;
import com.company.inventory.customers.dao.ICustomerDao;
import com.company.inventory.customers.model.Customer;
import com.company.inventory.customers.response.CustomerResponseRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private ICustomerDao customerDao;


    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CustomerResponseRest> search() {

        CustomerResponseRest response = new CustomerResponseRest();
        try {
            List<Customer> customer = (List<Customer>) customerDao.findAll();

            response.getCustomerResponse().setCustomer(customer);
            response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");

        } catch (Exception e) {

            response.setMetadata("Respuesta Nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<CustomerResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<CustomerResponseRest>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CustomerResponseRest> searchById(Long id) {

        CustomerResponseRest response = new CustomerResponseRest();
        List<Customer> list = new ArrayList<>();

        try {

            Optional<Customer> customer = customerDao.findById(id);

            if (customer.isPresent()) {
                list.add(customer.get());
                response.getCustomerResponse().setCustomer(list);
                response.setMetadata("Respuesta ok", "00", "Cliente encontrado");
            } else {
                response.setMetadata("Respuesta Nok", "-1", "Cliente no encontrado");
                return new ResponseEntity<CustomerResponseRest>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {

            response.setMetadata("Respuesta Nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<CustomerResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<CustomerResponseRest>(response, HttpStatus.OK);
    }

    @Override
    @Transactional (readOnly = true)
    public ResponseEntity<CustomerResponseRest> searchByFullName(String fullName) {
        CustomerResponseRest response = new CustomerResponseRest();
        List<Customer> list = new ArrayList<>();
        List<Customer> listAux = new ArrayList<>();

        try {

            //search cliente by name
            listAux = customerDao.findByFullNameContainingIgnoreCase(fullName);


            if( listAux.size() > 0) {

                listAux.stream().forEach( (c) -> {
                    list.add(c);
                });

                response.getCustomerResponse().setCustomer(list);
                response.setMetadata("Respuesta ok", "00", "Cliente encontrado");

            } else {
                response.setMetadata("respuesta nok", "-1", "Cliente no encontrado ");
                return new ResponseEntity<CustomerResponseRest>(response, HttpStatus.NOT_FOUND);
            }


        } catch (Exception e) {
            e.getStackTrace();
            response.setMetadata("respuesta nok", "-1", "Error al buscar Cliente por nombre");
            return new ResponseEntity<CustomerResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return new ResponseEntity<CustomerResponseRest>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomerResponseRest> save(Customer customer) {

        CustomerResponseRest response = new CustomerResponseRest();
        List<Customer> list = new ArrayList<>();

        try{

           Customer customerSaved = customerDao.save(customer);

           list.add(customerSaved);
            response.getCustomerResponse().setCustomer(list);
            response.setMetadata("Respuesta ok", "00", "Cliente registrado");
            response.getCustomerResponse().setCustomer(list);
        }catch(Exception e) {
            response.setMetadata("Respuesta Nok", "-1", "Error al registrar al cliente");
            e.getStackTrace();
            return new ResponseEntity<CustomerResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<CustomerResponseRest>(response,HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomerResponseRest> update(Customer customer, Long id) {
        CustomerResponseRest response = new CustomerResponseRest();
        List<Customer> list = new ArrayList<>();

        try {

            Optional<Customer> customerSearch = customerDao.findById(id);

            if (customerSearch.isPresent()) {
                //se procederá a actualizar el registro
                customerSearch.get().setFullName(customer.getFullName());
                customerSearch.get().setDocument(customer.getDocument());
                customerSearch.get().setPhone(customer.getPhone());

                Customer customerUpdate = customerDao.save(customerSearch.get());

                list.add(customerUpdate);
                response.getCustomerResponse().setCustomer(list);
                response.setMetadata("Respuesta ok", "00", "Cliente actualizado");
            } else {
                response.setMetadata("Respuesta Nok", "-1", "Cliente no encontrado");
                return new ResponseEntity<CustomerResponseRest>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("Respuesta Nok", "-1", "Error al actualizar cliente");
            e.getStackTrace();
            return new ResponseEntity<CustomerResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<CustomerResponseRest>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomerResponseRest> deleteById(Long id) {

        CustomerResponseRest response = new CustomerResponseRest();

        try {
            customerDao.deleteById(id);
            response.setMetadata("respuesta ok", "00", "Registro eliminado");
        } catch (Exception e) {
            response.setMetadata("Respuesta Nok", "-1", "Error al eliminar");
            e.getStackTrace();
            return new ResponseEntity<CustomerResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<CustomerResponseRest>(response, HttpStatus.OK);
    }
}
