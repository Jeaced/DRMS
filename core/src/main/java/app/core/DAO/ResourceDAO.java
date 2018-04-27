package app.core.DAO;

import app.core.models.Resource;

public interface ResourceDAO {
    Resource findByName(String name);
}
