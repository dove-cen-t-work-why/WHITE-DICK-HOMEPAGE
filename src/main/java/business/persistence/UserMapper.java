package business.persistence;

import business.exceptions.UserException;
import business.entities.User;

import java.sql.*;

public class UserMapper {
    private Database database;

    public UserMapper(Database database) {
        this.database = database;
    }

    public void createUser(User user) throws UserException {
        try (Connection connection = database.connect()) {
            String sql = "INSERT INTO users (name, street, town, zipCode, email, password, phone) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user.getName());
                ps.setString(2, user.getStreet());
                ps.setString(3, user.getTown());
                ps.setInt(4, user.getZipCode());
                ps.setString(5, user.getEmail());
                ps.setString(6, user.getPassword());
                ps.setInt(7, user.getPhone());
                ps.executeUpdate();
                ResultSet ids = ps.getGeneratedKeys();
                ids.next();
                int id = ids.getInt(1);
                user.setId(id);
            }
        } catch (SQLException ex) {
            throw new UserException(ex.getMessage());
        }
    }

    public User login(String email, String password) throws UserException
    {
        try (Connection connection = database.connect())
        {
            String sql = "SELECT id, name, street, town, zipCode, phone, role FROM `users` WHERE email=? AND password=?";

            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setString(1, email);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if (rs.next())
                {
                    int id = rs.getInt("id");
                    String role = rs.getString("role");
                    int phone = rs.getInt("phone");
                    String name = rs.getString("name");
                    String street = rs.getString("street");
                    String town = rs.getString("town");
                    int zipCode = rs.getInt("zipCode");
                    User user = new User(name, street, town, zipCode, email, password, phone, role);
                    user.setId(id);
                    return user;
                } else
                {
                    throw new UserException("Could not validate user");
                }
            }
            catch (SQLException ex)
            {
                throw new UserException(ex.getMessage());
            }
        }
        catch (SQLException ex)
        {
            throw new UserException("Connection to database could not be established");
        }
    }

//    public User getUserInfo(int id) throws UserException
//    {
//        try (Connection connection = database.connect())
//        {
//            String sql = "SELECT name, street, town, zipCode, phone, role, email FROM `users` WHERE id=?";
//
//            try (PreparedStatement ps = connection.prepareStatement(sql))
//            {
//                ps.setInt(1, id);
//                ResultSet rs = ps.executeQuery();
//                if (rs.next())
//                {
//                    String email = rs.getString("email");
//                    String role = rs.getString("role");
//                    int phone = rs.getInt("phone");
//                    String name = rs.getString("name");
//                    String street = rs.getString("street");
//                    String town = rs.getString("town");
//                    int zipCode = rs.getInt("zipCode");
//                    User user = new User(name, street, town, zipCode, email, phone, role);
//                    user.setId(id);
//                    return user;
//                } else
//                {
//                    throw new UserException("Could not validate user");
//                }
//            }
//            catch (SQLException ex)
//            {
//                throw new UserException(ex.getMessage());
//            }
//        }
//        catch (SQLException ex)
//        {
//            throw new UserException("Connection to database could not be established");
//        }
//    }

}
