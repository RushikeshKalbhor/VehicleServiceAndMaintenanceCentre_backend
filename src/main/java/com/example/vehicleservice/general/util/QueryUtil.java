package com.example.vehicleservice.general.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryUtil {

    private final EntityManager entityManager;

    private final GeneralUtil generalUtil;

    public QueryUtil(EntityManager entityManager, GeneralUtil generalUtil) {
        this.entityManager = entityManager;
        this.generalUtil = generalUtil;
    }

    /**
     * Retrieves the value of a specific column from a database table based on the entity model, primary key, and column name.
     *
     * @param modelName       The entity class representing the table. e.g. Clinic.Class
     * @param primaryKeyValue The value of the primary key to locate the record. e.g. 1
     * @param columnName      The name of the column to retrieve the value from. e.g. cliName
     * @return The value of the specified column, or null if no record is found.
     */
    public <T> T findColumnValueByColumnName(Class<?> modelName, Integer primaryKeyValue, String columnName) {
        String model = modelName.getSimpleName();
        String primaryKey = generalUtil.getFieldNameWithIdAnnotation(modelName);
        String queryString = "SELECT " + columnName + " FROM "+ model +" WHERE "+ primaryKey +" = "+ primaryKeyValue +"";
        Query query = entityManager.createQuery(queryString);

        List<T> list = query.getResultList();
        if (list != null && !list.isEmpty()) {
            return list.getFirst();
        }
        return null;
    }
}
