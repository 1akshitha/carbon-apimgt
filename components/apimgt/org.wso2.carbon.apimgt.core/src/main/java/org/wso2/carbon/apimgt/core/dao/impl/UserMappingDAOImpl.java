/*
 *
 *   Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.apimgt.core.dao.impl;

import org.wso2.carbon.apimgt.core.dao.UserMappingDAO;
import org.wso2.carbon.apimgt.core.exception.APIMgtDAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Provides conversion between real user name and pseudo name.
 * This was implemented to GDPR compliance in API Manager.
 */
public class UserMappingDAOImpl implements UserMappingDAO {

    public UserMappingDAOImpl() {
    }

    /**
     *
     * @param pseudoName
     * @return
     * @throws APIMgtDAOException
     */
    @Override
    public String getUserIDByPseudoName(String pseudoName) throws APIMgtDAOException {
        final String query = "SELECT USER_DOMAIN_NAME, USER_REAL_NAME FROM AM_USER_NAME_MAPPING WHERE " +
                "PSEUDO_NAME = ?";
        String realName = null;
        try (Connection connection = DAOUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pseudoName);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    realName = rs.getString("USER_REAL_NAME");
                }
            }
        } catch (SQLException e) {
            throw new APIMgtDAOException(DAOUtil.DAO_ERROR_PREFIX + "getting name mappings", e);
        }
        return realName;
    }

    /**
     *
     * @param userID
     * @return
     * @throws APIMgtDAOException
     */
    @Override
    public String getPseudoNameByUserID(String userID) throws APIMgtDAOException {
        final String query = "SELECT PSEUDO_NAME FROM AM_USER_NAME_MAPPING WHERE USER_REAL_NAME = ?";
        String pseudoName = null;
        try (Connection connection = DAOUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userID);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    pseudoName = rs.getString("PSEUDO_NAME");
                }
            }
            if (pseudoName == null) {
                pseudoName = addUserMapping(userID);
            }
        } catch (SQLException e) {
            throw new APIMgtDAOException(DAOUtil.DAO_ERROR_PREFIX + "getting name mappings", e);
        }
        return pseudoName;
    }

    /**
     *
     * @param userName
     * @return
     * @throws APIMgtDAOException
     */
    public String addUserMapping(String userName) throws APIMgtDAOException {
        String pseudoName = UUID.randomUUID().toString();
        final String query = "INSERT INTO AM_USER_NAME_MAPPING (PSEUDO_NAME, USER_REAL_NAME) VALUES (?,?)";
        try (Connection connection = DAOUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            try {
                connection.setAutoCommit(false);
                statement.setString(1, pseudoName);
                statement.setString(2, userName);
                //statement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                statement.execute();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                String errorMessage = "Error while adding user mapping ";
                throw new APIMgtDAOException(DAOUtil.DAO_ERROR_PREFIX + errorMessage, e);
            } finally {
                connection.setAutoCommit(DAOUtil.isAutoCommit());
            }
        } catch (SQLException e) {
            String errorMessage = "Error while adding user mapping";
            throw new APIMgtDAOException(DAOUtil.DAO_ERROR_PREFIX + errorMessage, e);
        }
        return pseudoName;
    }
}
