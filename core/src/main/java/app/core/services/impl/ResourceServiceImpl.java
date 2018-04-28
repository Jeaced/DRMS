package app.core.services.impl;

import app.core.DAO.ResourceDAO;
import app.core.models.Resource;
import app.core.services.interfaces.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    ResourceDAO resourceDAO;

    @Override
    public List<Resource> findAll() {
        return resourceDAO.findAll();
    }

}
